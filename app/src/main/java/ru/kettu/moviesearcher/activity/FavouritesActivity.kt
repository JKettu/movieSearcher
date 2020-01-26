package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.item_favourite.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.MainActivity.Companion.FILM_INFO
import ru.kettu.moviesearcher.adapter.FavouritesActivityAdapter
import ru.kettu.moviesearcher.models.FavouriteInfo

class FavouritesActivity: AppCompatActivity() {

    lateinit var favourites: HashSet<FavouriteInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)
        val intentExtras = intent.extras
        val text = filmNameFav
        if (intentExtras == null) {
            text.setText(R.string.couldntFindDesc)
            return
        }
        val bundle = intentExtras.getBundle(FILM_INFO)
        favourites = bundle?.getSerializable(FILM_INFO) as HashSet<FavouriteInfo>
        if (favourites == null) {
            text.setText(R.string.couldntFindDesc)
            return
        }
        initRecyclerView()
    }

    fun initRecyclerView() {
        val layoutManager =
            LinearLayoutManager( this, VERTICAL, false)
        recycleViewFav?.adapter = FavouritesActivityAdapter(LayoutInflater.from(this), favourites)
        recycleViewFav?.layoutManager = layoutManager
    }

    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra(FILM_INFO, favourites)
        setResult(RESULT_OK,intent)
        super.onBackPressed()
    }
}