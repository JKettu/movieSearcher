package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import kotlinx.android.synthetic.main.fragment_film_detail.*
import kotlinx.android.synthetic.main.item_main_header.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.Companion.FAVOURITES_FRAGMENT
import ru.kettu.moviesearcher.activity.fragment.FavouritesFragment.OnFavouritesFragmentAction
import ru.kettu.moviesearcher.activity.fragment.FilmDetailsFragment
import ru.kettu.moviesearcher.activity.fragment.FilmDetailsFragment.Companion.FILM_DETAILS_FRAGMENT
import ru.kettu.moviesearcher.activity.fragment.MainFragment
import ru.kettu.moviesearcher.activity.fragment.MainFragment.Companion.MAIN_FRAGMENT
import ru.kettu.moviesearcher.activity.fragment.MainFragment.OnMainFragmentAction
import ru.kettu.moviesearcher.models.item.FavouriteItem
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.operations.*
import java.util.*


class MainActivity : AppCompatActivity(), OnMainFragmentAction, OnFavouritesFragmentAction {

    lateinit var filmItems: List<FilmItem>
    var isNightModeOn = false
    var selectedText: TextView? = null
    var selectedSpan: Int? = null
    var favourites = TreeSet<FavouriteItem>()

    companion object {
        const val SELECTED_SPAN = "SELECTED_SPAN"
        const val IS_NIGHT_MODE_ON = "IS_NIGHT_MODE_ON"
        const val FILM_INFO = "FILM_INFO"
        const val ALL_FILMS = "ALL_FILMS"
        const val FAVOURITES = "FAVOURITES"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        filmItems = resources.initFilmItems()

        savedInstanceState?.let {
            isNightModeOn = it.getBoolean(IS_NIGHT_MODE_ON)
            selectedSpan = it.getInt(SELECTED_SPAN)
            val bundle = it.getBundle(FAVOURITES)
            favourites = bundle?.getSerializable(FAVOURITES) as TreeSet<FavouriteItem>
        }

        openMainFragment()
    }

    private fun openMainFragment() {
        if (supportFragmentManager.findFragmentByTag(MAIN_FRAGMENT) == null) {
            val bundle = Bundle()
            bundle.putParcelableArrayList(ALL_FILMS, filmItems as ArrayList<FilmItem>)
            bundle.putSerializable(FAVOURITES, favourites)
            bundle.putBoolean(IS_NIGHT_MODE_ON, isNightModeOn)
            selectedSpan?.let {
                bundle.putInt(SELECTED_SPAN, selectedSpan!!)
            }
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, MainFragment.newInstance(bundle), MAIN_FRAGMENT)
                .addToBackStack(MAIN_FRAGMENT)
                .commit()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_NIGHT_MODE_ON, if (mode == null) isNightModeOn else mode.isChecked)
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
                setSupportActionBar(filmDetailToolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setTitle(R.string.empty)
            }
            is FilmDetailsFragment -> {
                setSupportActionBar(addFavouriteToolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setTitle(R.string.empty)
            }
        }
    }

    override fun onModeSwitch(mode: Switch) {
        if (mode.isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        ActivityCompat.recreate(this)
    }

    override fun onPressInviteBtn() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        val title = resources.getString(R.string.chooser)
        val chooser = Intent.createChooser(intent, title)
        intent.resolveActivity(packageManager)?.let {
            startActivity(chooser)
        }
    }

    override fun onPressFavouritesIcon() {
        if (supportFragmentManager.findFragmentByTag(FAVOURITES_FRAGMENT) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, FavouritesFragment.newInstance(filmItems, favourites),
                    FAVOURITES_FRAGMENT)
                .addToBackStack(FAVOURITES_FRAGMENT)
                .commit()
        }
    }

    override fun onAddToFavourites(posterId: Int, nameId: Int) {
        val filmInfo = resources.getFilmInfoByFilmName(getString(nameId))
        filmInfo?.let {
            val favourite = FavouriteItem(posterId, nameId)
            favourites.add(favourite)
            val toast =
                Toast.makeText(this, R.string.addToFavourite, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    override fun onDetailsBtnPressed(filmName: TextView, layoutPosition: Int) {
        filmName.setSelectedTextColor()
        if (selectedText != null)
            selectedText!!.setDefaultTextColor()

        selectedSpan = layoutPosition
        selectedText = filmName
        val filmInfo = resources.getFilmInfoByFilmName(filmName.text?.toString())

        if (supportFragmentManager.findFragmentByTag(FILM_DETAILS_FRAGMENT) == null) {
            val bundle = Bundle()
            bundle.putParcelable(FILM_INFO, filmInfo)
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer,
                    FilmDetailsFragment.newInstance(bundle), FILM_DETAILS_FRAGMENT)
                .addToBackStack(FILM_DETAILS_FRAGMENT)
                .commit()
        }
    }

    override fun onRestoreMarkedFilmName(filmName: TextView, position: Int) {
        if (selectedSpan == position) {
            filmName.setSelectedTextColor()
            selectedText = filmName
        }
    }

    override fun onDeleteFilm(layoutPosition: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if (fragment is FavouritesFragment) {
            val element = fragment.favourites.elementAt(layoutPosition)
            fragment.favourites.remove(element)
            fragment.notInFavourites.add(element)
            recycleViewFav.adapter?.notifyItemRemoved(layoutPosition)
            filmsToAddRV.adapter?.notifyItemInserted(fragment.notInFavourites.indexOf(element))
        }
    }

    override fun onAddFilm(posterId: Int, filmNameId: Int) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        if(fragment is FavouritesFragment) {
            val newFav = FavouriteItem(posterId, filmNameId)
            fragment.favourites.add(newFav)
            val elemPosition = fragment.notInFavourites.indexOf(newFav)
            fragment.notInFavourites.remove(newFav)
            recycleViewFav.adapter?.notifyItemInserted(fragment.favourites.indexOf(newFav))
            filmsToAddRV.adapter?.notifyItemRemoved(elemPosition)
        }

    }


}
