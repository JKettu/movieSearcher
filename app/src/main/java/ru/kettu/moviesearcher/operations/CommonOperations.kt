package ru.kettu.moviesearcher.operations

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.View
import ru.kettu.moviesearcher.activity.FilmDetailActivity
import ru.kettu.moviesearcher.models.FilmInfo

fun getActivity(view: View): Activity? {
    var context: Context? = view.context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    return null
}

fun Activity.openFilmDescriptionActivity(descriptionId: Int, posterId: Int, reqCode: Int, extraName: String) {
    val intent = Intent(this, FilmDetailActivity::class.java)
    intent.putExtra(extraName, FilmInfo(descriptionId, posterId))
    this.startActivityForResult(intent, reqCode)
}