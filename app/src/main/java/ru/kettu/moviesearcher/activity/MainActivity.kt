package ru.kettu.moviesearcher.activity

import android.content.Intent
import android.content.Intent.ACTION_SEND
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.item_main_header.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.activity.FilmDetailActivity.Companion.DETAILS_INFO
import ru.kettu.moviesearcher.adapter.MainActivityAdapter
import ru.kettu.moviesearcher.item.FilmItem
import ru.kettu.moviesearcher.models.FilmDetailsInfo
import ru.kettu.moviesearcher.models.FilmInfo
import ru.kettu.moviesearcher.operations.initFilmItems
import ru.kettu.moviesearcher.operations.showAlertDialog


class MainActivity : AppCompatActivity() {

    lateinit var filmItems: List<FilmItem>
    var isNightModeOn = false
    var selectedText: TextView? = null
    var selectedSpan: Int? = null
    var favourites = HashSet<FilmInfo>()

    companion object {
        const val SELECTED_SPAN = "SELECTED_SPAN"
        const val IS_NIGHT_MODE_ON = "IS_NIGHT_MODE_ON"
        const val FILM_INFO = "FILM_INFO"
        const val FILM_DETAILS_INFO_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        filmItems = initFilmItems()

        savedInstanceState?.let {
            isNightModeOn = it.getBoolean(IS_NIGHT_MODE_ON)
            selectedSpan = it.getInt(SELECTED_SPAN)
        }
        initRecycleView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_NIGHT_MODE_ON, mode.isChecked)
        selectedSpan?.let {
            outState.putInt(SELECTED_SPAN, selectedSpan!!)
        }
    }
    
    fun onInviteBtnClick(view: View?) {
        if (view == null || view !is Button) return
        val intent = Intent(ACTION_SEND)
        intent.type = "text/plain"
        val title = resources.getString(R.string.chooser)
        val chooser = Intent.createChooser(intent, title)
        intent.resolveActivity(packageManager)?.let {
            startActivity(chooser)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FILM_DETAILS_INFO_REQUEST_CODE -> {
                if (RESULT_OK == (resultCode) && data != null) {
                   val detailInfo = data.getParcelableExtra<FilmDetailsInfo>(DETAILS_INFO)
                   detailInfo?.apply {
                       Log.i(MainActivity::class.java.toString(), "FilmInfo: is liked - $isLiked")
                       if (comment.isNotEmpty())
                        Log.i(MainActivity::class.java.toString(), "FilmInfo: comment - $comment")
                   }
                }
            }
        }
    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    fun onModeSwitchClick(view: View?) {
        if (view == null || view !is Switch) return
        if (view.isChecked) {
            setDefaultNightMode(MODE_NIGHT_YES)
        } else {
            setDefaultNightMode(MODE_NIGHT_NO)
        }
        recreate()
    }

    fun initRecycleView() {
        val layoutManager =
            GridLayoutManager( this, resources.getInteger(R.integer.main_activity_columns), VERTICAL, false)
        layoutManager.spanSizeLookup = object : SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0) resources.getInteger(R.integer.main_activity_columns) else 1 //set header width
            }
        }
        recycleView?.adapter = MainActivityAdapter(LayoutInflater.from(this), filmItems, isNightModeOn)
        recycleView?.layoutManager = layoutManager
    }
}
