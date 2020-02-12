package ru.kettu.moviesearcher.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.initNotInFavourites
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.view.recyclerview.adapter.AddToFavouritesAdapter
import ru.kettu.moviesearcher.view.recyclerview.adapter.FavouritesAdapter
import java.util.*

class FavouritesFragment: Fragment(R.layout.fragment_favourites) {

    var listener: OnFavouritesFragmentAction? = null

    companion object {
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT"
        const val ALL_FILMS = "ALL_FILMS"
        const val FAVOURITES = "FAVOURITES"

        fun newInstance(allFilms: Set<FilmItem>, films: Set<FilmItem>): FavouritesFragment {
            val fragment = FavouritesFragment()
            val bundle = Bundle()
            bundle.putSerializable(ALL_FILMS, allFilms as LinkedHashSet<FilmItem>)
            bundle.putSerializable(FAVOURITES, films as LinkedHashSet<FilmItem>)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var films: LinkedHashSet<FilmItem>
    var notInFavourites = LinkedHashSet<FilmItem>()
    lateinit var allFilms: LinkedHashSet<FilmItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentView = getView()
        films = arguments?.get(FAVOURITES) as LinkedHashSet<FilmItem>
        allFilms = arguments?.get(ALL_FILMS) as LinkedHashSet<FilmItem>
        resources.initNotInFavourites(allFilms, films, notInFavourites)
        initFavouritesRecyclerView(currentView?.context)
        initAddFavouritesRecyclerView(currentView?.context)
        listener?.onFragmentCreatedInitToolbar(this)
        if (activity is AppCompatActivity) {
            val toolbar = (activity as AppCompatActivity).supportActionBar
            toolbar?.title = (getString(R.string.favouritesFragmentName))
        }
    }

    private fun initFavouritesRecyclerView(context: Context?) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecorator.setDrawable(resources.getDrawable(R.drawable.separator_line))
        recycleViewFav?.addItemDecoration(itemDecorator)
        recycleViewFav?.adapter =
            FavouritesAdapter(
                LayoutInflater.from(context), films, listener
            )
        recycleViewFav?.layoutManager = layoutManager
    }

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

    interface OnFavouritesFragmentAction {
        fun onDeleteFilm(layoutPosition: Int, film: FilmItem)

        fun onAddFilm(film: FilmItem)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}