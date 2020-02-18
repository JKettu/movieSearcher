package ru.kettu.moviesearcher.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_add_favorites.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.getNotInFavouritesList
import ru.kettu.moviesearcher.controller.initFirstFavouritesLoading
import ru.kettu.moviesearcher.controller.initFirstNotInFavouritesLoading
import ru.kettu.moviesearcher.models.item.FilmItem

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    var listener: OnFavouritesFragmentAction? = null
    lateinit var favourites: LinkedHashSet<FilmItem>
    var favouritesLoaded = LinkedHashSet<FilmItem>()
    var notInFavourites = LinkedHashSet<FilmItem>()
    var currentLoadedPage = 1

    companion object {
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT"
        const val NOT_IN_FAVOURITES = "NOT_IN_FAVOURITES"
        const val FAVOURITES = "FAVOURITES"
        const val FAV_ITEMS_LOADED = "FAV_ITEMS_LOADED"
        const val CURRENT_LOADED_PAGE = "CURRENT_LOADED_PAGE"

        fun newInstance(films: Set<FilmItem>): FavouritesFragment {
            val fragment = FavouritesFragment()
            val bundle = Bundle()
            bundle.putSerializable(FAVOURITES, films as LinkedHashSet<FilmItem>)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favourites = arguments?.get(FAVOURITES) as LinkedHashSet<FilmItem>
        savedInstanceState?.let {
            favouritesLoaded = it.getSerializable(FAV_ITEMS_LOADED) as LinkedHashSet<FilmItem>
            currentLoadedPage = it.getInt(CURRENT_LOADED_PAGE)
            notInFavourites = it.getSerializable(NOT_IN_FAVOURITES) as LinkedHashSet<FilmItem>
        }

        initFirstFavouritesLoading(this, favourites)
        filmsToAddRV?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    == notInFavourites.size - 1) {
                    getNotInFavouritesList(this@FavouritesFragment, currentLoadedPage + 1)
                }
            }
        })

        initFirstNotInFavouritesLoading(this)
        if (activity is AppCompatActivity) {
            val toolbar = (activity as AppCompatActivity).supportActionBar
            toolbar?.title = (getString(R.string.favouritesFragmentName))
        }
        listener?.onFragmentCreatedInitToolbar(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(FAV_ITEMS_LOADED, favouritesLoaded)
        outState.putInt(CURRENT_LOADED_PAGE, currentLoadedPage)
        outState.putSerializable(NOT_IN_FAVOURITES, notInFavourites)
    }

    interface OnFavouritesFragmentAction {
        fun onDeleteFilm(layoutPosition: Int, film: FilmItem)

        fun onAddFilm(film: FilmItem)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}