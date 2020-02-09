package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils.isEmpty
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.Companion.FAVOURITES_FRAGMENT
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.activity.fragment.FilmDetailsFragment
import ru.kettu.moviesearcher.activity.fragment.FilmDetailsFragment.Companion.FILM_DETAILS_FRAGMENT
import ru.kettu.moviesearcher.activity.fragment.FilmDetailsFragment.OnFilmDetailsAction
import ru.kettu.moviesearcher.activity.fragment.MainFilmListFragment
import ru.kettu.moviesearcher.activity.fragment.MainFilmListFragment.Companion.MAIN_FRAGMENT
import ru.kettu.moviesearcher.activity.fragment.MainFilmListFragment.OnMainFragmentAction
import ru.kettu.moviesearcher.controller.onDayNightModeSwitch
import ru.kettu.moviesearcher.controller.setDefaultTextColor
import ru.kettu.moviesearcher.controller.setSelectedTextColor
import ru.kettu.moviesearcher.controller.showAlertDialog
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.FilmDetails
import ru.kettu.moviesearcher.network.RetrofitApp
import java.util.*
import kotlin.collections.HashSet


class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener,
    OnMainFragmentAction, OnFavouritesFragmentAction, OnFilmDetailsAction {

    var filmItems = HashSet<FilmItem>()
    var selectedText: TextView? = null
    var selectedSpan: Int? = null
    var favourites = TreeSet<FilmItem>()
    var toggle: ActionBarDrawerToggle? = null
    var lastAddedToFavourite: FilmItem? = null

    companion object {
        const val SELECTED_SPAN = "SELECTED_SPAN"
        const val FILM_INFO = "FILM_INFO"
        const val ALL_FILMS = "ALL_FILMS"
        const val FAVOURITES = "FAVOURITES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (RetrofitApp.theMovieDbApi == null) {
            RetrofitApp.onCreate()
        }

        savedInstanceState?.let {
            selectedSpan = it.getInt(SELECTED_SPAN)
            val bundle = it.getBundle(FILM_INFO)
            favourites = bundle?.getSerializable(FAVOURITES) as TreeSet<FilmItem>
            filmItems = bundle?.getSerializable(ALL_FILMS) as HashSet<FilmItem>
        }

        initToolbar()
        if (supportFragmentManager.findFragmentByTag(MAIN_FRAGMENT) == null)
            openMainFragment()
    }

    private fun initToolbar() {
        setSupportActionBar(mainToolbar)
        supportActionBar?.setTitle(R.string.empty)
        val toggle = ActionBarDrawerToggle(this, navigationDrawer, mainToolbar, R.string.empty, R.string.empty)
        this.toggle = toggle
        navigationDrawer.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)
        val switch = navigationView.menu.findItem(R.id.mode)?.actionView?.findViewById<SwitchCompat>(R.id.modeSwitch)
        switch?.setOnClickListener {
            onDayNightModeSwitch(it as SwitchCompat, this)
        }
    }

    private fun openMainFragment() {
        val bundle = Bundle()
        selectedSpan?.let {
            bundle.putInt(SELECTED_SPAN, selectedSpan!!)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MainFilmListFragment.newInstance(bundle), MAIN_FRAGMENT)
            .addToBackStack(MAIN_FRAGMENT)
            .commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedSpan?.let {
            outState.putInt(SELECTED_SPAN, selectedSpan!!)
        }
        val bundle = Bundle()
        bundle.putSerializable(FAVOURITES, favourites)
        bundle.putSerializable(ALL_FILMS, filmItems as HashSet<FilmItem>)
        outState.putBundle(FILM_INFO, bundle)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1
            && supportFragmentManager.findFragmentByTag(MAIN_FRAGMENT) != null) {
            showAlertDialog()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is MainFilmListFragment -> {
                fragment.listener = this
            }
            is FavouritesFragment -> {
                fragment.listener = this
            }
            is FilmDetailsFragment -> {
                fragment.listener = this
            }
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.mainScreen -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                if (fragment !is MainFilmListFragment)
                    openMainFragment()
            }
            R.id.favouritesScreen -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
                if (fragment !is FavouritesFragment)
                    onPressFavourites()
            }
            R.id.invite -> {
                onPressInvite()
            }
            R.id.exit -> {
                showAlertDialog()
            }
        }
        navigationDrawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onItemsInitFinish(filmItems: Set<FilmItem>?) {
        this.filmItems = if (filmItems == null) HashSet() else filmItems as HashSet<FilmItem>
    }

    override fun onPressInvite() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val title = resources.getString(R.string.chooser)
        val chooser = Intent.createChooser(intent, title)
        intent.resolveActivity(packageManager)?.let {
            startActivity(chooser)
        }
    }

    override fun onPressFavourites() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, FavouritesFragment.newInstance(filmItems, favourites),
                FAVOURITES_FRAGMENT)
            .addToBackStack(FAVOURITES_FRAGMENT)
            .commit()
    }

    override fun onAddToFavourites(details: FilmDetails) {
        val favourite = FilmItem(details.title, details.overview, details.posterPath)
        favourites.add(favourite)
        lastAddedToFavourite = favourite
        val snackbar = Snackbar.make(fragmentContainer, R.string.addToFavourite, Snackbar.LENGTH_LONG)
            .setAction(R.string.cancel) {run {
                lastAddedToFavourite?.let {
                    favourites.remove(lastAddedToFavourite as FilmItem)
                }
            }}
        snackbar.view.setBackgroundColor(getColor(R.color.colorMenuBase))
        snackbar.show()
    }

    override fun onDetailsBtnPressed(filmName: TextView, details: FilmDetails, layoutPosition: Int) {
        filmName.setSelectedTextColor()
        if (selectedText != null)
            selectedText!!.setDefaultTextColor()

        selectedSpan = layoutPosition
        selectedText = filmName
        val filmInfo = FilmItem(details.title, details.overview, details.posterPath)
        val bundle = Bundle()
        bundle.putSerializable(FILM_INFO, filmInfo)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer,
                FilmDetailsFragment.newInstance(bundle), FILM_DETAILS_FRAGMENT)
            .addToBackStack(FILM_DETAILS_FRAGMENT)
            .commit()
    }

    override fun onRestoreMarkedFilmName(filmName: TextView, position: Int) {
        if (selectedSpan == position) {
            filmName.setSelectedTextColor()
            selectedText = filmName
        }
    }

    override fun onDeleteFilm(layoutPosition: Int, film: FilmItem) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment is FavouritesFragment) {
            fragment.films.remove(film)
            fragment.notInFavourites.add(film)
            fragment.recycleViewFav.adapter?.notifyItemRemoved(layoutPosition)
            fragment.filmsToAddRV.adapter?.notifyItemInserted(fragment.notInFavourites.indexOf(film))
            val toast =
                Toast.makeText(this, R.string.deletedFromFavourite, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun onAddFilm(film: FilmItem) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if(fragment is FavouritesFragment) {
            fragment.films.add(film)
            val elemPosition = fragment.notInFavourites.indexOf(film)
            fragment.notInFavourites.remove(film)
            fragment.recycleViewFav.adapter?.notifyItemInserted(fragment.films.indexOf(film))
            fragment.filmsToAddRV.adapter?.notifyItemRemoved(elemPosition)
        }
    }

    override fun onFragmentCreatedInitToolbar(fragment: Fragment) {
        if (fragment is FilmDetailsFragment) {
            mainToolbar.visibility = GONE
            navigationDrawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        } else {
            if (mainToolbar.visibility.equals(GONE)) {
                mainToolbar.visibility = VISIBLE
                setSupportActionBar(mainToolbar)
                val newToggle = ActionBarDrawerToggle(this, navigationDrawer, mainToolbar, R.string.empty, R.string.empty)
                this.toggle = newToggle
                navigationDrawer.addDrawerListener(newToggle)
            }
            (toggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = true
            supportActionBar?.title = getString(R.string.empty)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            navigationDrawer.setDrawerLockMode(LOCK_MODE_UNLOCKED)
        }
        (toggle as ActionBarDrawerToggle).syncState()
    }
}
