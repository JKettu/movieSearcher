package ru.kettu.moviesearcher.activity.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
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
import ru.kettu.moviesearcher.models.item.FavouriteItem
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.operations.initNotInFavourites
import java.util.*

class FavouritesFragment: Fragment(R.layout.fragment_favourites) {

    interface OnFavouritesFragmentAction {
        fun onDeleteFilm(layoutPosition: Int)

        fun onAddFilm(posterId: Int, filmNameId: Int)
    }

    var listener: OnFavouritesFragmentAction? = null

    companion object {
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT"

        fun newInstance(allFilms: List<FilmItem>, favourites: TreeSet<FavouriteItem>): FavouritesFragment{
            val fragment = FavouritesFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ALL_FILMS, allFilms as ArrayList<FilmItem>)
            bundle.putSerializable(FAVOURITES, favourites)
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var favourites: TreeSet<FavouriteItem>
    var notInFavourites = TreeSet<FavouriteItem>()
    lateinit var allFilms: ArrayList<FilmItem>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentView = getView()
        favourites = arguments?.get(FAVOURITES) as TreeSet<FavouriteItem>
        allFilms = arguments?.get(ALL_FILMS) as ArrayList<FilmItem>
        resources.initNotInFavourites(allFilms, favourites, notInFavourites)
        initFavouritesRecyclerView(currentView?.context)
        initAddFavouritesRecyclerView(currentView?.context)
    }

    private fun initFavouritesRecyclerView(context: Context?) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecorator.setDrawable(resources.getDrawable(R.drawable.separator_line))
        recycleViewFav?.addItemDecoration(itemDecorator)
        recycleViewFav?.adapter = FavouritesAdapter(LayoutInflater.from(context), favourites, listener, resources)
        recycleViewFav?.layoutManager = layoutManager
    }

    private fun initAddFavouritesRecyclerView(context: Context?) {
        val layoutManager =
            GridLayoutManager( context, resources.getInteger(R.integer.columns),
                RecyclerView.VERTICAL, false)
        filmsToAddRV?.adapter = AddToFavouritesAdapter(LayoutInflater.from(context), notInFavourites, listener, resources)
        filmsToAddRV?.layoutManager = layoutManager
    }
}