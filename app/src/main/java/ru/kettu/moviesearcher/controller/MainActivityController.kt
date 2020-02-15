package ru.kettu.moviesearcher.controller

import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import ru.kettu.moviesearcher.R
import ru.kettu.moviesearcher.view.activity.MainActivity
import ru.kettu.moviesearcher.view.fragment.FilmDetailsFragment

fun MainActivity.showAlertDialog() {
    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
    builder.setMessage(R.string.exitQuestion)
    builder.setNegativeButton(R.string.no) { dialog, which ->
        dialog.dismiss()
    }
    builder.setPositiveButton(R.string.yes) { dialog, which ->
        run {
            dialog.dismiss()
            this.finish()
        }
    }
    val dialog: AlertDialog = builder.create()
    dialog.show()
}

fun MainActivity.updateToolbarParameters(fragment: Fragment) {
    if (fragment is FilmDetailsFragment) {
        mainToolbar.visibility = View.GONE
        navigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    } else {
        if (mainToolbar.visibility.equals(View.GONE)) {
            mainToolbar.visibility = View.VISIBLE
            setSupportActionBar(mainToolbar)
            val newToggle = ActionBarDrawerToggle(this, navigationDrawer, mainToolbar, R.string.empty, R.string.empty)
            this.toggle = newToggle
            navigationDrawer.addDrawerListener(newToggle)
        }
        (toggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = true
        supportActionBar?.title = getString(R.string.empty)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        navigationDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
    (toggle as ActionBarDrawerToggle).syncState()
}