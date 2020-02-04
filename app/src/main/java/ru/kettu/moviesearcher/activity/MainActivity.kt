package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
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
import ru.kettu.moviesearcher.activity.fragment.MainFragment
import ru.kettu.moviesearcher.activity.fragment.MainFragment.Companion.MAIN_FRAGMENT
import ru.kettu.moviesearcher.activity.fragment.MainFragment.OnMainFragmentAction
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.operations.*
import java.util.*


class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener,
    OnMainFragmentAction, OnFavouritesFragmentAction, OnFilmDetailsAction {

    lateinit var filmItems: List<FilmItem>
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
        filmItems = initFilmItems()

        savedInstanceState?.let {
            selectedSpan = it.getInt(SELECTED_SPAN)
            val bundle = it.getBundle(FAVOURITES)
            favourites = bundle?.getSerializable(FAVOURITES) as TreeSet<FilmItem>
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
        bundle.putSerializable(ALL_FILMS, filmItems as ArrayList<FilmItem>)
        bundle.putSerializable(FAVOURITES, favourites)
        selectedSpan?.let {
            bundle.putInt(SELECTED_SPAN, selectedSpan!!)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, MainFragment.newInstance(bundle), MAIN_FRAGMENT)
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
        outState.putBundle(FAVOURITES, bundle)
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
            is MainFragment -> {
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
                if (fragment !is MainFragment)
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

    override fun onAddToFavourites(posterId: Int, nameId: Int) {
        val filmInfo = resources.getFilmInfoByFilmName(getString(nameId))
        filmInfo?.let {
            val favourite = FilmItem(posterId, nameId)
            favourites.add(favourite)
            lastAddedToFavourite = favourite
        }
    }

    override fun onDetailsBtnPressed(filmName: TextView, layoutPosition: Int) {
        filmName.setSelectedTextColor()
        if (selectedText != null)
            selectedText!!.setDefaultTextColor()

        selectedSpan = layoutPosition
        selectedText = filmName
        val filmInfo = resources.getFilmInfoByFilmName(filmName.text?.toString())
        val bundle = Bundle()
        bundle.putParcelable(FILM_INFO, filmInfo)
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
            recycleViewFav.adapter?.notifyItemRemoved(layoutPosition)
            filmsToAddRV.adapter?.notifyItemInserted(fragment.notInFavourites.indexOf(film))
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
            recycleViewFav.adapter?.notifyItemInserted(fragment.films.indexOf(film))
            filmsToAddRV.adapter?.notifyItemRemoved(elemPosition)
        }
    }

    override fun onFragmentCreatedInitToolbar(fragment: Fragment) {
        if (fragment is FilmDetailsFragment) {
            (toggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = false
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            (toggle as ActionBarDrawerToggle).setToolbarNavigationClickListener{onBackPressed()}
            navigationDrawer.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        } else {
            (toggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = true
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            navigationDrawer.setDrawerLockMode(LOCK_MODE_UNLOCKED)
        }
        (toggle as ActionBarDrawerToggle).syncState()
    }
}
