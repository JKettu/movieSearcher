package ru.kettu.moviesearcher.activity.fragment

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.ALL_FILMS
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FAVOURITES
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.activity.MainActivity.Companion.SELECTED_SPAN
import ru.kettu.moviesearcher.controller.initRecycleView
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.FilmDetails
import java.util.*
import kotlin.collections.HashSet

class MainFilmListFragment: Fragment(R.layout.fragment_main) {

    var listener: OnMainFragmentAction? = null
    lateinit var progressBar: ProgressBar

    companion object {
        const val MAIN_FRAGMENT = "MAIN_FRAGMENT"

        fun newInstance(bundle: Bundle): MainFilmListFragment{
            val fragment = MainFilmListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    var filmItems = HashSet<FilmItem>()
    var selectedSpan: Int? = null
    var favourites = TreeSet<FilmItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressBar = ProgressBar(this.context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentView = getView()
        savedInstanceState?.let {
            selectedSpan = it.getInt(SELECTED_SPAN)
            val bundle = it.getBundle(FILM_INFO)
            favourites = bundle?.getSerializable(FAVOURITES) as TreeSet<FilmItem>
            filmItems = bundle?.getSerializable(ALL_FILMS) as HashSet<FilmItem>
        }

        currentView?.let {
            progressBar.visibility = View.VISIBLE
            initRecycleView(currentView)
        }
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

    interface OnMainFragmentAction {

        fun onItemsInitFinish(filmItems: Set<FilmItem>?)

        fun onPressInvite()

        fun onPressFavourites()

        fun onAddToFavourites(details: FilmDetails)

        fun onDetailsBtnPressed(filmName: TextView, details: FilmDetails, layoutPosition: Int)

        fun onRestoreMarkedFilmName(filmName: TextView, position: Int)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}