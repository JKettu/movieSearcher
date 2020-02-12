package ru.kettu.moviesearcher.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_add_favorites.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.initFirstFavouritesLoading
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.recyclerview.adapter.AddToFavouritesAdapter
import java.util.*
import kotlin.collections.LinkedHashSet

class FavouritesFragment: Fragment(R.layout.fragment_favourites) {

    var listener: OnFavouritesFragmentAction? = null

    companion object {
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT"
        const val ALL_FILMS = "ALL_FILMS"
        const val FAVOURITES = "FAVOURITES"
        const val FAV_ITEMS_LOADED = "FAV_ITEMS_LOADED"

        fun newInstance(allFilms: Set<FilmItem>, films: Set<FilmItem>): FavouritesFragment {
            val fragment = FavouritesFragment()
            val bundle = Bundle()
            bundle.putSerializable(ALL_FILMS, allFilms as LinkedHashSet<FilmItem>)
            bundle.putSerializable(FAVOURITES, films as LinkedHashSet<FilmItem>)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var favourites: LinkedHashSet<FilmItem>
    var favouritesLoaded = LinkedHashSet<FilmItem>()
    var notInFavourites = LinkedHashSet<FilmItem>()
    lateinit var allFilms: LinkedHashSet<FilmItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentView = getView()
        favourites = arguments?.get(FAVOURITES) as LinkedHashSet<FilmItem>
        allFilms = arguments?.get(ALL_FILMS) as LinkedHashSet<FilmItem>
        savedInstanceState?.let {
            favouritesLoaded = it.getSerializable(FAV_ITEMS_LOADED) as LinkedHashSet<FilmItem>
        }

        //resources.initNotInFavourites(allFilms, favourites, notInFavourites)
        initFirstFavouritesLoading(this, favourites)

        initAddFavouritesRecyclerView(currentView?.context)
        listener?.onFragmentCreatedInitToolbar(this)
        if (activity is AppCompatActivity) {
            val toolbar = (activity as AppCompatActivity).supportActionBar
            toolbar?.title = (getString(R.string.favouritesFragmentName))
        }
    }

    /*private fun initFavouritesRecyclerView(context: Context?) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecorator.setDrawable(resources.getDrawable(R.drawable.separator_line))
        recycleViewFav?.addItemDecoration(itemDecorator)
        recycleViewFav?.adapter =
            FavouritesAdapter(LayoutInflater.from(context), favourites, listener)
        recycleViewFav?.layoutManager = layoutManager
    }*/

    private fun initAddFavouritesRecyclerView(context: Context?) {
        val layoutManager =
            GridLayoutManager( context, resources.getInteger(R.integer.columns),
                RecyclerView.VERTICAL, false)
        filmsToAddRV?.adapter =
            AddToFavouritesAdapter(
                LayoutInflater.from(context), notInFavourites, listener
            )
        filmsToAddRV?.layoutManager = layoutManager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(FAV_ITEMS_LOADED, favouritesLoaded)
    }

    interface OnFavouritesFragmentAction {
        fun onDeleteFilm(layoutPosition: Int, film: FilmItem)

        fun onAddFilm(film: FilmItem)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}