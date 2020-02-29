package ru.kettu.moviesearcher.view.fragment

import android.os.Bundle
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.FilmItemDiffUtilCallback
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.viewmodel.MainFilmListViewModel
import ru.kettu.moviesearcher.view.recyclerview.adapter.FilmListAdapter

class MainFilmListFragment: Fragment(R.layout.fragment_main) {

    private val filmListViewModel by lazy {
        ViewModelProvider(this.activity!!).get(MainFilmListViewModel::class.java)
    }

    var listener: OnMainFragmentAction? = null
    var filmItems = LinkedHashSet<FilmItem>()
    var currentLoadedPage = 1
    var filmsLoading = true

    companion object {
        const val MAIN_FRAGMENT = "MAIN_FRAGMENT"
        const val LOADED_PAGE = "LOADED_PAGE"
        const val FILMS_LOADING = "FILMS_LOADING"

        fun newInstance() = MainFilmListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            currentLoadedPage = it.getInt(LOADED_PAGE)
            filmsLoading = it.getBoolean(FILMS_LOADING)
        }

        filmListViewModel.initRecycleView(recyclerView, filmItems, view.context,
            resources, this.listener as OnFragmentAction)

        if (filmListViewModel.allFilms.value == null || filmListViewModel.allFilms.value!!.isEmpty())
            filmListViewModel.onFilmListScroll(resources, view.context, currentLoadedPage, circle_progress_bar)

        filmListViewModel.allFilms.observe(viewLifecycleOwner, Observer<LinkedHashSet<FilmItem>> {
            currentLoadedPage++
            val newSet = filmListViewModel.allFilms.value as LinkedHashSet<FilmItem>
            val callback = FilmItemDiffUtilCallback(filmItems, newSet)
            val diffResult = DiffUtil.calculateDiff(callback)
            filmItems.addAll(newSet)
            diffResult.dispatchUpdatesTo(recyclerView.adapter as FilmListAdapter)
            filmsLoading = false
            circle_progress_bar.visibility = INVISIBLE
        })

        recyclerView?.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView?.layoutManager as GridLayoutManager).findLastVisibleItemPosition() == filmItems.size - 1
                        && !filmsLoading) {
                    filmsLoading = true
                    circle_progress_bar.visibility = VISIBLE
                    filmListViewModel.onFilmListScroll(resources, view.context, currentLoadedPage, circle_progress_bar)
                }
            }
        })
        listener?.onFragmentCreatedInitToolbar(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(LOADED_PAGE, currentLoadedPage)
        outState.putBoolean(FILMS_LOADING, filmsLoading)
    }

    interface OnMainFragmentAction: OnFragmentAction {

        fun onPressInvite()

        fun onAddToFavourites(item: FilmItem)

        fun onDetailsBtnPressed(filmName: TextView, item: FilmItem, layoutPosition: Int)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}