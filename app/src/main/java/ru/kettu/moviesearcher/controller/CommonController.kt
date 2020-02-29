package ru.kettu.moviesearcher.controller

import android.text.TextUtils.isEmpty
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.constants.NetworkConstants.POSTER_PREFIX

fun loadImage(imageView: ImageView?, fullImagePath: String?) {
    if (isEmpty(fullImagePath) || POSTER_PREFIX.equals(fullImagePath)) return
    imageView?.let {
        Glide.with(it.context)
            .load(fullImagePath)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(it)
    }
}

fun FragmentManager.loadFragmentWithoutBackStack(fragmentId: Int, fragment: Fragment, name: String) {
    this.beginTransaction()
        .replace(fragmentId, fragment, name)
        .commit()
}