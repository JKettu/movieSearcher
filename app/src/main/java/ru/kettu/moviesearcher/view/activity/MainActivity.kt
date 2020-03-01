package ru.kettu.moviesearcher.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.SwitchCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.loadFragmentWithoutBackStack
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.viewmodel.FavouritesViewModel
import ru.kettu.moviesearcher.models.viewmodel.MainFilmListViewModel
import ru.kettu.moviesearcher.network.FilmSearchApp
import ru.kettu.moviesearcher.view.fragment.FavouritesFragment
import ru.kettu.moviesearcher.view.fragment.FavouritesFragment.Companion.FAVOURITES_FRAGMENT
import ru.kettu.moviesearcher.view.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.view.fragment.FilmDetailsFragment
import ru.kettu.moviesearcher.view.fragment.FilmDetailsFragment.Companion.FILM_DETAILS_FRAGMENT
import ru.kettu.moviesearcher.view.fragment.FilmDetailsFragment.OnFilmDetailsAction
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment.Companion.MAIN_FRAGMENT
import ru.kettu.moviesearcher.view.fragment.MainFilmListFragment.OnMainFragmentAction
import ru.kettu.moviesearcher.view.recyclerview.adapter.FilmListAdapter


class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener,
    OnMainFragmentAction, OnFavouritesFragmentAction, OnFilmDetailsAction {

    private val filmListViewModel by lazy {
        ViewModelProvider(this).get(MainFilmListViewModel::class.java)
    }

    private val favouritesViewModel by lazy {
        ViewModelProvider(this).get(FavouritesViewModel::class.java)
    }

    var selectedText: TextView? = null
    var favourites = LinkedHashSet<FilmItem>()
    var toggle: ActionBarDrawerToggle? = null
    var currentFragmentName: String? = MAIN_FRAGMENT

    companion object {
        const val FILM_INFO = "FILM_INFO"
        const val FAVOURITES = "FAVOURITES"
        const val CURRENT_FRAGMENT_NAME = "CURRENT_FRAGMENT_NAME"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (FilmSearchApp.theMovieDbApi == null) {
            FilmSearchApp.onCreate()
        }

        savedInstanceState?.let {
            val bundle = it.getBundle(FILM_INFO)
            favourites = bundle?.getSerializable(FAVOURITES) as LinkedHashSet<FilmItem>
            currentFragmentName = bundle.getString(CURRENT_FRAGMENT_NAME)
        }

        initToolbar()

        if (supportFragmentManager.findFragmentByTag(currentFragmentName) == null)
            openFragment()

        main_swipe_refresh.setOnRefreshListener {
            val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .detach(it)
                    .attach(it)
                    .commit()
            }
            main_swipe_refresh.isRefreshing = false
        }

        filmListViewModel.additionWasCanceled.observe(this, Observer { wasCanceled ->
            filmListViewModel.lastAddedToFavourite.value?.let { item ->
                favouritesViewModel.onFilmItemLongPress(item, wasCanceled)
            }
        })
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
        switch?.setOnClickListener { mode ->
            setDefaultNightMode(if ((mode as SwitchCompat).isChecked) MODE_NIGHT_YES else MODE_NIGHT_NO)
            recreate()
        }
    }

    private fun updateToolbarParameters(supportFragmentManager: FragmentManager, toolbar: Toolbar) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (currentFragment is FilmDetailsFragment) {
            toolbar.visibility = View.GONE
            navigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            if (toolbar.visibility.equals(View.GONE)) {
                toolbar.visibility = View.VISIBLE
                setSupportActionBar(toolbar)
                val newToggle =
                    ActionBarDrawerToggle(currentFragment?.activity, navigationDrawer,
                        toolbar, R.string.empty, R.string.empty)
                toggle = newToggle
                navigationDrawer.addDrawerListener(newToggle)
            }
            (toggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = true
            supportActionBar?.title = getString(R.string.empty)
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            navigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
        (toggle as ActionBarDrawerToggle).syncState()
    }

    private fun openFragment() {
        when(currentFragmentName) {
            MAIN_FRAGMENT -> openMainFragment()
            FAVOURITES_FRAGMENT -> openFavouritesFragment()
        }
    }

    private fun openMainFragment() {
        currentFragmentName = MAIN_FRAGMENT
        supportFragmentManager
            .loadFragmentWithoutBackStack(R.id.fragmentContainer,
                MainFilmListFragment.newInstance(), MAIN_FRAGMENT)
    }

    private fun openFavouritesFragment() {
        currentFragmentName = FAVOURITES_FRAGMENT
        supportFragmentManager
            .loadFragmentWithoutBackStack(R.id.fragmentContainer,
                FavouritesFragment.newInstance(), FAVOURITES_FRAGMENT)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = Bundle()
        bundle.putSerializable(FAVOURITES, favourites)
        bundle.putString(CURRENT_FRAGMENT_NAME, currentFragmentName)
        outState.putBundle(FILM_INFO, bundle)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            filmListViewModel.showAlertDialog(this)
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
                    openFavouritesFragment()
            }
            R.id.invite -> {
                onPressInvite()
            }
            R.id.exit -> {
                filmListViewModel.showAlertDialog(this)
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

    override fun onAddToFavourites(item: FilmItem) {
        filmListViewModel.setLastAddedFavourite(resources, fragmentContainer, item)
    }

    override fun onDetailsBtnPressed(filmName: TextView, item: FilmItem, layoutPosition: Int) {
        filmListViewModel.selectedFilmItemPosition.value?.let {
            (recyclerView.adapter as FilmListAdapter).items.elementAt(it).isSelected = false
        }
        filmListViewModel.onDetailsButtonClick(selectedText, item, layoutPosition)
        selectedText = filmName
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, FilmDetailsFragment.newInstance(item), FILM_DETAILS_FRAGMENT)
            .addToBackStack(FILM_DETAILS_FRAGMENT)
            .commit()
    }

    override fun onDeleteFilm(layoutPosition: Int, film: FilmItem) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment is FavouritesFragment) {
            favouritesViewModel.onRemoveFromFavourites(film)
            Toast.makeText(this, R.string.deletedFromFavourite, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onAddFilm(film: FilmItem) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if(fragment is FavouritesFragment) {
            favouritesViewModel.onAddToFavourites(resources, this, film)
        }
    }

    override fun onFragmentCreatedInitToolbar(fragment: Fragment) {
        when(fragment) {
            is FavouritesFragment, is FilmDetailsFragment -> main_swipe_refresh.isEnabled = false
            else -> main_swipe_refresh.isEnabled = true
        }
        updateToolbarParameters(supportFragmentManager, mainToolbar)
    }
}
