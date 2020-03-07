package ru.kettu.moviesearcher.view.fragment

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.controller.getAllFilmsList
import ru.kettu.moviesearcher.controller.initRecycleView
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*

class MainFilmListFragment: Fragment(R.layout.fragment_main) {

    var listener: OnMainFragmentAction? = null
    var filmItems = LinkedHashSet<FilmItem>()
    var selectedSpan: Int? = null
    var favourites = LinkedHashSet<FilmItem>()
    var currentLoadedPage = 1

    companion object {
        const val MAIN_FRAGMENT = "MAIN_FRAGMENT"
        const val LOADED_PAGE = "LOADED_PAGE"
        const val ALL_FILMS = "ALL_FILMS"
        const val FAVOURITES = "FAVOURITES"
        const val SELECTED_SPAN = "SELECTED_SPAN"

        fun newInstance(selectedSpan: Int?): MainFilmListFragment {
            val fragment = MainFilmListFragment()
            val bundle = Bundle()
            selectedSpan?.let {
                bundle.putInt(SELECTED_SPAN, selectedSpan)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            selectedSpan = it.getInt(SELECTED_SPAN)
            favourites = it.getSerializable(FAVOURITES) as LinkedHashSet<FilmItem>
            filmItems = it.getSerializable(ALL_FILMS) as LinkedHashSet<FilmItem>
            currentLoadedPage = it.getInt(LOADED_PAGE)
        }

        initRecycleView()
        recycleView?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recycleView?.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    == filmItems.size) {
                    this@MainFilmListFragment.circle_progress_bar.visibility = VISIBLE
                    getAllFilmsList(this@MainFilmListFragment, currentLoadedPage + 1, recyclerView.layoutManager)
                }
            }
        })
        listener?.onFragmentCreatedInitToolbar(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedSpan?.let {
            outState.putInt(SELECTED_SPAN, selectedSpan!!)
        }
        outState.putSerializable(FAVOURITES, favourites)
        outState.putSerializable(ALL_FILMS, filmItems)
        outState.putInt(LOADED_PAGE, currentLoadedPage)
    }

    interface OnMainFragmentAction {

        fun onItemsInitFinish(filmItems: Set<FilmItem>?)

        fun onPressInvite()

        fun openFavouritesFragment()

        fun onAddToFavourites(item: FilmItem)

        fun onDetailsBtnPressed(filmName: TextView, item: FilmItem, layoutPosition: Int)

        fun onRestoreMarkedFilmName(filmName: TextView, position: Int)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}