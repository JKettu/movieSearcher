package ru.kettu.moviesearcher.view.fragment

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.favouritesLoading
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
    var isFavLoadingInProcess = false
    var isAddToFavLoadingInProcess = false

    companion object {
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT"
        const val NOT_IN_FAVOURITES = "NOT_IN_FAVOURITES"
        const val FAVOURITES = "FAVOURITES"
        const val FAV_ITEMS_LOADED = "FAV_ITEMS_LOADED"
        const val CURRENT_LOADED_PAGE = "CURRENT_LOADED_PAGE"
        const val IS_FAV_LOADING_IN_PROCESS = "IS_FAV_LOADING_IN_PROCESS"
        const val IS_NOT_IN_FAV_LOADING_IN_PROCESS = "IS_NOT_IN_FAV_LOADING_IN_PROCESS"

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
            isFavLoadingInProcess = it.getBoolean(IS_FAV_LOADING_IN_PROCESS)
            isAddToFavLoadingInProcess = it.getBoolean(IS_NOT_IN_FAV_LOADING_IN_PROCESS)
        }

        recycleViewFav?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    == recyclerView.size - 1
                    && favourites.size != recyclerView.size) {
                    favouritesLoading(this@FavouritesFragment, favourites)
                }
            }
        })

        initFirstFavouritesLoading(this, favourites)
        filmsToAddRV?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    == notInFavourites.size - 1) {
                    add_to_fav_progress_bar.visibility = VISIBLE
                    isAddToFavLoadingInProcess  = true
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
        outState.putBoolean(IS_FAV_LOADING_IN_PROCESS, isFavLoadingInProcess)
        outState.putBoolean(IS_NOT_IN_FAV_LOADING_IN_PROCESS, isAddToFavLoadingInProcess)
    }

    interface OnFavouritesFragmentAction {
        fun onDeleteFilm(layoutPosition: Int, film: FilmItem)

        fun onAddFilm(film: FilmItem)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}