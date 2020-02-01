package ru.kettu.moviesearcher.activity.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.item_main_header.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.ALL_FILMS
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FAVOURITES
import ru.kettu.moviesearcher.activity.MainActivity.Companion.IS_NIGHT_MODE_ON
import ru.kettu.moviesearcher.activity.MainActivity.Companion.SELECTED_SPAN
import ru.kettu.moviesearcher.adapter.MainActivityAdapter
import ru.kettu.moviesearcher.models.item.FavouriteItem
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
    var favourites = TreeSet<FavouriteItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filmItems = arguments?.get(ALL_FILMS) as ArrayList<FilmItem>
        Log.i("$this", "onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentView = getView()
        savedInstanceState?.let {
            isNightModeOn = it.getBoolean(IS_NIGHT_MODE_ON)
            selectedSpan = it.getInt(SELECTED_SPAN)
            val bundle = it.getBundle(FAVOURITES)
            favourites = bundle?.getSerializable(FAVOURITES) as TreeSet<FavouriteItem>
        }
        initRecycleView(currentView)
        Log.i("$this", "onViewCreated")
    }

    fun initRecycleView(currentView: View?) {
        val layoutManager =
            GridLayoutManager( currentView?.context, resources.getInteger(R.integer.columns), RecyclerView.VERTICAL, false)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) resources.getInteger(R.integer.columns) else 1 //set header width
            }
        }
        recycleView?.adapter = MainActivityAdapter(LayoutInflater.from(currentView?.context),
            filmItems, isNightModeOn, listener, resources)
        recycleView?.layoutManager = layoutManager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_NIGHT_MODE_ON, if (mode == null) isNightModeOn else mode.isChecked)
        selectedSpan?.let {
            outState.putInt(SELECTED_SPAN, selectedSpan!!)
        }
        val bundle = Bundle()
        bundle.putSerializable(FAVOURITES, favourites)
        outState.putBundle(FAVOURITES, bundle)
    }

    interface OnMainFragmentAction {
        fun onModeSwitch(mode: Switch)

        fun onPressInviteBtn()

        fun onPressFavouritesIcon()

        fun onAddToFavourites(posterId: Int, nameId: Int)

        fun onDetailsBtnPressed(filmName: TextView, layoutPosition: Int)

        fun onRestoreMarkedFilmName(filmName: TextView, position: Int)
    }
}