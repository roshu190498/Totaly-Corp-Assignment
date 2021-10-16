package com.example.totalitycorpassignment.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast


fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

/**
 * Extention Toast
 */
fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * show view
 */
fun View.show() {
    this.visibility = View.VISIBLE
}

/**
 * hide view
 */
fun View.hide() {
    this.visibility = View.GONE
}

/**
 * invisible view
 */
fun View.invisible() {
    this.visibility = View.INVISIBLE
}

/**
 * check view is visible
 */
fun View.isViewVisible(): Boolean {
    return this.visibility == View.VISIBLE
}