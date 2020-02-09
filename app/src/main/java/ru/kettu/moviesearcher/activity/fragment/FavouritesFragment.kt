package ru.kettu.moviesearcher.activity.fragment

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
import ru.kettu.moviesearcher.activity.MainActivity.Companion.ALL_FILMS
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FAVOURITES
import ru.kettu.moviesearcher.adapter.AddToFavouritesAdapter
import ru.kettu.moviesearcher.adapter.FavouritesAdapter
import ru.kettu.moviesearcher.controller.initNotInFavourites
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*
import kotlin.collections.HashSet

class FavouritesFragment: Fragment(R.layout.fragment_favourites) {

    var listener: OnFavouritesFragmentAction? = null

    companion object {
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT"

        fun newInstance(allFilms: Set<FilmItem>, films: Set<FilmItem>): FavouritesFragment{
            val fragment = FavouritesFragment()
            val bundle = Bundle()
            bundle.putSerializable(ALL_FILMS, allFilms as HashSet<FilmItem>)
            bundle.putSerializable(FAVOURITES, films as TreeSet<FilmItem>)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var films: TreeSet<FilmItem>
    var notInFavourites = TreeSet<FilmItem>()
    lateinit var allFilms: HashSet<FilmItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentView = getView()
        films = arguments?.get(FAVOURITES) as TreeSet<FilmItem>
        allFilms = arguments?.get(ALL_FILMS) as HashSet<FilmItem>
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
        recycleViewFav?.adapter = FavouritesAdapter(LayoutInflater.from(context), films, listener)
        recycleViewFav?.layoutManager = layoutManager
    }

    private fun initAddFavouritesRecyclerView(context: Context?) {
        val layoutManager =
            GridLayoutManager( context, resources.getInteger(R.integer.columns),
                RecyclerView.VERTICAL, false)
        filmsToAddRV?.adapter = AddToFavouritesAdapter(LayoutInflater.from(context), notInFavourites, listener)
        filmsToAddRV?.layoutManager = layoutManager
    }

    interface OnFavouritesFragmentAction {
        fun onDeleteFilm(layoutPosition: Int, film: FilmItem)

        fun onAddFilm(film: FilmItem)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}