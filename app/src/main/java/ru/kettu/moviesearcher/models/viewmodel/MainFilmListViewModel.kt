package ru.kettu.moviesearcher.models.viewmodel

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.models.enums.LoadResult
import ru.kettu.moviesearcher.models.enums.LoadResult.FAILED
import ru.kettu.moviesearcher.models.enums.LoadResult.SUCCESS
import ru.kettu.moviesearcher.models.item.FilmItem
import ru.kettu.moviesearcher.models.network.LoaderResponse
import ru.kettu.moviesearcher.FilmSearchApp
import ru.kettu.moviesearcher.network.interactor.Loader
import ru.kettu.moviesearcher.network.interactor.TheMovieDbLoader
import ru.kettu.moviesearcher.view.fragment.OnFragmentAction
import ru.kettu.moviesearcher.view.recyclerview.adapter.FilmListAdapter


class MainFilmListViewModel : ViewModel() {
    private val allFilmsLiveData = MutableLiveData<LinkedHashSet<FilmItem>>()
    private val lastAddedToFavouriteLiveData = MutableLiveData<FilmItem>()
    private val additionWasCanceledLiveData = MutableLiveData<Boolean>()
    private val selectedFilmItemPositionLivaData = MutableLiveData<Int>()
    private val filmsInitLoadResultLiveData = MutableLiveData<LoadResult>()

    private val theMovieDb = FilmSearchApp.theMovieDbApi

    val allFilms: LiveData<LinkedHashSet<FilmItem>>
        get() = allFilmsLiveData

    val lastAddedToFavourite: LiveData<FilmItem>
        get() = lastAddedToFavouriteLiveData

    val additionWasCanceled: LiveData<Boolean>
        get() = additionWasCanceledLiveData

    val selectedFilmItemPosition: LiveData<Int>
        get() = selectedFilmItemPositionLivaData

    val filmsInitLoadResult: LiveData<LoadResult>
        get() = filmsInitLoadResultLiveData

    fun initRecycleView(recyclerView: RecyclerView, filmItem: LinkedHashSet<FilmItem>, context: Context,
                        resources: Resources, listener: OnFragmentAction) {
        val layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.columns), RecyclerView.VERTICAL, false)

        recyclerView.adapter =
            FilmListAdapter(LayoutInflater.from(context), filmItem, listener, resources)
        recyclerView.layoutManager = layoutManager
    }

    fun onFilmListScroll(resources: Resources, context: Context, currentLoadedPage: Int,
                         progressBar: ProgressBar) {
        val call = theMovieDb?.getNowPlayingFilms(resources.configuration.locale.language, currentLoadedPage)
        call?.let {
            TheMovieDbLoader.loadFilmSet(call as Call<LoaderResponse>, object: Loader.LoaderCallback {
                override fun onFailed(errorIntId: Int) {
                    Toast.makeText(context, errorIntId, Toast.LENGTH_LONG).show()
                    progressBar.visibility = INVISIBLE
                    if (allFilms.value == null)
                        filmsInitLoadResultLiveData.postValue(FAILED)
                    allFilms.value?.let { films ->
                        if (films.isEmpty()) {
                            filmsInitLoadResultLiveData.postValue(FAILED)
                        }
                    }
                }

                override fun onSucceed(item: LinkedHashSet<FilmItem>) {
                    if (allFilmsLiveData.value == null)
                        allFilmsLiveData.postValue(item)
                    else {
                        (allFilmsLiveData.value as LinkedHashSet<FilmItem>).addAll(item)
                        allFilmsLiveData.postValue(allFilmsLiveData.value)
                    }
                    filmsInitLoadResultLiveData.postValue(SUCCESS)
                }
            })
        }
    }

    fun onDetailsButtonClick(previousSelectedText: TextView?, item: FilmItem, layoutPosition: Int) {
        previousSelectedText?.apply {
            val colorAccent = resources.getColor(R.color.colorAccent)
            this.setTextColor(colorAccent)
        }

        item.isSelected = true
        selectedFilmItemPositionLivaData.value = layoutPosition
    }

    fun setLastAddedFavourite(resources: Resources, view: View, item: FilmItem) {
        lastAddedToFavouriteLiveData.value = item
        additionWasCanceledLiveData.postValue(false)
        val snackbar = Snackbar.make(view, R.string.addToFavourite, Snackbar.LENGTH_LONG)
            .setAction(R.string.cancel) {
                run {
                    additionWasCanceledLiveData.postValue(true)
                }
            }
        snackbar.view.setBackgroundColor(resources.getColor(R.color.colorMenuBase))
        snackbar.show()
    }

    fun showAlertDialog(activity: AppCompatActivity) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setMessage(R.string.exitQuestion)
        builder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        builder.setPositiveButton(R.string.yes) { dialog, _ ->
            run {
                dialog.dismiss()
                activity.finish()
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}