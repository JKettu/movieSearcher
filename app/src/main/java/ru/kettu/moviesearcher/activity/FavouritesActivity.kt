package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.content_add_favorites.*
import kotlinx.android.synthetic.main.item_favourite.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.ALL_FILMS
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.adapter.AddToFavouritesAdapter
import ru.kettu.moviesearcher.adapter.FavouritesActivityAdapter
import ru.kettu.moviesearcher.models.FavouriteInfo
import ru.kettu.moviesearcher.models.FilmInfo
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.operations.initNotInFavourites
import java.util.*

class FavouritesActivity: AppCompatActivity() {

    lateinit var favourites: TreeSet<FavouriteInfo>
    var notInFavourites = TreeSet<FavouriteInfo>()
    lateinit var allFilms: ArrayList<FilmItem>

    companion object {
        const val FILMS_LISTS = "FILMS_LISTS"
        const val FAVOURITES = "FAVOURITES"
        const val NOT_IN_FAVOURITES = "NOT_IN_FAVOURITES"
        const val ALL_FILMS = "ALL_FILMS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        val intentExtras = intent.extras
        val text = filmNameFav
        if (intentExtras == null) {
            text.setText(R.string.couldntFindDesc)
            return
        }
        savedInstanceState?.let {
            val savedBundle = it.getBundle(FILMS_LISTS)
            favourites = savedBundle?.getSerializable(FAVOURITES) as TreeSet<FavouriteInfo>
            notInFavourites = savedBundle?.getSerializable(NOT_IN_FAVOURITES) as TreeSet<FavouriteInfo>
            allFilms = savedBundle?.getParcelableArrayList<FilmItem>(ALL_FILMS) as ArrayList<FilmItem>
        }

        if (savedInstanceState?.getBundle(FILMS_LISTS) == null) {
            val bundle = intentExtras.getBundle(FILM_INFO)
            favourites = bundle?.getSerializable(FILM_INFO) as TreeSet<FavouriteInfo>
            allFilms = bundle?.getParcelableArrayList<FilmInfo>(ALL_FILMS) as ArrayList<FilmItem>
            if (favourites == null) {
                text.setText(R.string.couldntFindDesc)
                return
            }
            notInFavourites.initNotInFavourites(allFilms, favourites)
        }

        initFavouritesRecyclerView()
        initAddFavouritesRecyclerView()
    }

    fun initFavouritesRecyclerView() {
        val layoutManager =
            LinearLayoutManager( this, VERTICAL, false)
        val itemDecorator = DividerItemDecoration(this, VERTICAL)
        itemDecorator.setDrawable(resources.getDrawable(R.drawable.separator_line))
        recycleViewFav?.addItemDecoration(itemDecorator)
        recycleViewFav?.adapter = FavouritesActivityAdapter(LayoutInflater.from(this), favourites)
        recycleViewFav?.layoutManager = layoutManager
    }

    fun initAddFavouritesRecyclerView() {
        val layoutManager =
            GridLayoutManager( this, resources.getInteger(R.integer.columns),
                RecyclerView.VERTICAL, false)
        filmsToAddRV?.adapter = AddToFavouritesAdapter(LayoutInflater.from(this), notInFavourites)
        filmsToAddRV?.layoutManager = layoutManager
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val bundle = Bundle()
        bundle.putSerializable(FAVOURITES, favourites)
        bundle.putSerializable(NOT_IN_FAVOURITES, notInFavourites)
        bundle.putParcelableArrayList(ALL_FILMS, allFilms)
        outState.putBundle(FILMS_LISTS, bundle)
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(FILM_INFO, favourites)
        setResult(RESULT_OK,intent)
        super.onBackPressed()
    }
}