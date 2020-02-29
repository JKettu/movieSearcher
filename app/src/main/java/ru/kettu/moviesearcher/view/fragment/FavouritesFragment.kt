package ru.kettu.moviesearcher.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.fragment_favourites.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.FilmItemDiffUtilCallback
import ru.kettu.moviesearcher.models.enum.LoadResult.SUCCESS
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.viewmodel.FavouritesViewModel
import ru.kettu.moviesearcher.models.viewmodel.MainFilmListViewModel
import ru.kettu.moviesearcher.view.recyclerview.adapter.AddToFavouritesAdapter
import ru.kettu.moviesearcher.view.recyclerview.adapter.FavouritesAdapter

class FavouritesFragment : Fragment(R.layout.fragment_favourites) {

    private val filmListViewModel by lazy {
        ViewModelProvider(this.activity!!).get(MainFilmListViewModel::class.java)
    }

    private val favouritesViewModel by lazy {
        ViewModelProvider(this.activity!!).get(FavouritesViewModel::class.java)
    }

    var listener: OnFavouritesFragmentAction? = null
    var favourites = LinkedHashSet<FilmItem>()
    var notInFavourites = LinkedHashSet<FilmItem>()
    var currentLoadedPage = 1
    var favouritesWereLoaded = false
    var notInFavIsLoading = true

    companion object {
        const val FAVOURITES_FRAGMENT = "FAVOURITES_FRAGMENT"
        const val NOT_IN_FAVOURITES = "NOT_IN_FAVOURITES"
        const val FAVOURITES = "FAVOURITES"
        const val CURRENT_LOADED_PAGE = "CURRENT_LOADED_PAGE"
        const val NOT_IN_FAV_LOADING = "NOT_IN_FAV_LOADING"
        const val FAVOURITES_WERE_LOADED = "FAVOURITES_WERE_LOADED"

        fun newInstance() = FavouritesFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            favourites = it.getSerializable(FAVOURITES) as LinkedHashSet<FilmItem>
            currentLoadedPage = it.getInt(CURRENT_LOADED_PAGE)
            notInFavourites = it.getSerializable(NOT_IN_FAVOURITES) as LinkedHashSet<FilmItem>
            notInFavIsLoading = it.getBoolean(NOT_IN_FAV_LOADING)
            favouritesWereLoaded = it.getBoolean(FAVOURITES_WERE_LOADED)
        }

        favouriteRecyclerViewInit(view.context)
        notInFavouritesRecyclerViewInit(view.context)

        favouritesViewModel.favouritesLoaded.observe(viewLifecycleOwner, Observer {
            val callback = FilmItemDiffUtilCallback((recycleViewFav.adapter as FavouritesAdapter).items, it)
            val duffResult = DiffUtil.calculateDiff(callback)
            favourites.clear()
            favourites.addAll(it)
            favouritesWereLoaded = true
            duffResult.dispatchUpdatesTo(recycleViewFav.adapter as FavouritesAdapter)
            circle_progress_bar.visibility = INVISIBLE
        })

        favouritesViewModel.notInFavourite.observe(viewLifecycleOwner, Observer {
            currentLoadedPage++
            val callback = FilmItemDiffUtilCallback((NotInFavRecyclerView.adapter as AddToFavouritesAdapter).addItems, it)
            val diffResult = DiffUtil.calculateDiff(callback)
            notInFavourites.clear()
            notInFavourites.addAll(it)
            diffResult.dispatchUpdatesTo(NotInFavRecyclerView.adapter as AddToFavouritesAdapter)
            add_to_fav_progress_bar.visibility = INVISIBLE
            notInFavIsLoading = false
        })

        favouritesViewModel.favInitLoadResult.observe(viewLifecycleOwner, Observer { initLoadResult ->
            when (initLoadResult) {
                SUCCESS -> favTryAgainImg.visibility = INVISIBLE
                else -> favTryAgainImg.visibility = VISIBLE
            }
        })

        favouritesViewModel.notInFavInitLoadResult.observe(viewLifecycleOwner, Observer { initLoadResult ->
            when (initLoadResult) {
                SUCCESS -> notInFavTryAgainImg.visibility = GONE
                else -> notInFavTryAgainImg.visibility = VISIBLE
            }
        })

        favTryAgainImg.setOnClickListener {
            favouritesViewModel.loadFavouritesList(favourites, resources, view.context, circle_progress_bar)
            it.visibility = GONE
            circle_progress_bar.visibility = VISIBLE

        }

        notInFavTryAgainImg.setOnClickListener {
            favouritesViewModel.onNotInFavouritesScroll(resources, view.context, currentLoadedPage,
                add_to_fav_progress_bar)
            it.visibility = GONE
            add_to_fav_progress_bar.visibility = VISIBLE
        }

        if (activity is AppCompatActivity) {
            val toolbar = (activity as AppCompatActivity).supportActionBar
            toolbar?.title = (getString(R.string.favouritesFragmentName))
        }
        listener?.onFragmentCreatedInitToolbar(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        filmListViewModel.favourites.value?.let { fav ->
            if (SUCCESS.equals(favouritesViewModel.favInitLoadResult)) {
                fav.clear()
                fav.addAll(favourites)
            } else {
                fav.addAll(favourites)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(FAVOURITES, favourites)
        outState.putInt(CURRENT_LOADED_PAGE, currentLoadedPage)
        outState.putSerializable(NOT_IN_FAVOURITES, notInFavourites)
        outState.putBoolean(NOT_IN_FAV_LOADING, notInFavIsLoading)
        outState.putBoolean(FAVOURITES_WERE_LOADED, favouritesWereLoaded)
    }

    private fun favouriteRecyclerViewInit(context: Context) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val itemDecorator = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        itemDecorator.setDrawable(resources.getDrawable(R.drawable.separator_line))
        recycleViewFav.addItemDecoration(itemDecorator)
        recycleViewFav.adapter =
            FavouritesAdapter(LayoutInflater.from(context), favourites, listener)
        recycleViewFav.layoutManager = layoutManager
        circle_progress_bar.visibility = VISIBLE
        //first init based on received favourites list from main screen
        if (favourites.isEmpty()) {
            filmListViewModel.favourites.value?.let {
                favourites.addAll(filmListViewModel.favourites.value as LinkedHashSet<FilmItem>)
            }
            favouritesViewModel.initFavouritesLoadedLiveData(favourites)
        } else
        //other loadings based on modified list of favourites
        if (favouritesViewModel.favouritesLoaded.value!!.isEmpty() && favourites.isNotEmpty()) {
            favouritesViewModel.loadFavouritesList(favourites, resources, context, circle_progress_bar)
        }
    }

    private fun notInFavouritesRecyclerViewInit(context: Context) {
        val layoutManager = GridLayoutManager(context, resources.getInteger(R.integer.columns), RecyclerView.VERTICAL, false)
        NotInFavRecyclerView.adapter = AddToFavouritesAdapter(LayoutInflater.from(context), notInFavourites, listener)
        NotInFavRecyclerView.layoutManager = layoutManager

        //first init
        if (favouritesViewModel.notInFavourite.value == null) {
            add_to_fav_progress_bar.visibility = VISIBLE
            favouritesViewModel.initNotInFavouritesLiveData()
            favouritesViewModel.onNotInFavouritesScroll(resources, context, currentLoadedPage, add_to_fav_progress_bar)
        }

        NotInFavRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if ((recyclerView.layoutManager as GridLayoutManager).findLastVisibleItemPosition()
                    == notInFavourites.size - 1 && !notInFavIsLoading) {
                    add_to_fav_progress_bar.visibility = VISIBLE
                    notInFavIsLoading = true
                    favouritesViewModel.onNotInFavouritesScroll(resources, context,
                        currentLoadedPage, add_to_fav_progress_bar)
                }
            }
        })
    }

    interface OnFavouritesFragmentAction: OnFragmentAction {
        fun onDeleteFilm(layoutPosition: Int, film: FilmItem)

        fun onAddFilm(film: FilmItem)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}