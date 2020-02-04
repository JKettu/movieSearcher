package ru.kettu.moviesearcher.activity.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.ALL_FILMS
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FAVOURITES
import ru.kettu.moviesearcher.activity.MainActivity.Companion.SELECTED_SPAN
import ru.kettu.moviesearcher.adapter.FilmListAdapter
import ru.kettu.moviesearcher.models.item.FilmItem
import java.util.*

class MainFragment: Fragment(R.layout.fragment_main) {

    var listener: OnMainFragmentAction? = null

    companion object {
        const val MAIN_FRAGMENT = "MAIN_FRAGMENT"

        fun newInstance(bundle: Bundle): MainFragment{
            val fragment = MainFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    lateinit var filmItems: List<FilmItem>
    var isNightModeOn = false
    var selectedSpan: Int? = null
    var favourites = TreeSet<FilmItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentView = getView()
        savedInstanceState?.let {
            selectedSpan = it.getInt(SELECTED_SPAN)
            val bundle = it.getBundle(FAVOURITES)
            favourites = bundle?.getSerializable(FAVOURITES) as TreeSet<FilmItem>
        }
        filmItems = arguments?.get(ALL_FILMS) as ArrayList<FilmItem>

        initRecycleView(currentView)
        listener?.onFragmentCreatedInitToolbar(this)
    }

    fun initRecycleView(currentView: View?) {
        val layoutManager =
            GridLayoutManager( currentView?.context, resources.getInteger(R.integer.columns), RecyclerView.VERTICAL, false)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) resources.getInteger(R.integer.columns) else 1 //set header width
            }
        }
        recycleView?.adapter = FilmListAdapter(LayoutInflater.from(currentView?.context),
            filmItems, listener, resources)
        recycleView?.layoutManager = layoutManager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectedSpan?.let {
            outState.putInt(SELECTED_SPAN, selectedSpan!!)
        }
        val bundle = Bundle()
        bundle.putSerializable(FAVOURITES, favourites)
        outState.putBundle(FAVOURITES, bundle)
    }

    interface OnMainFragmentAction {

        fun onPressInvite()

        fun onPressFavourites()

        fun onAddToFavourites(posterId: Int, nameId: Int)

        fun onDetailsBtnPressed(filmName: TextView, layoutPosition: Int)

        fun onRestoreMarkedFilmName(filmName: TextView, position: Int)

        fun onFragmentCreatedInitToolbar(fragment: Fragment)
    }
}