package com.example.totalitycorpassignment.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


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


/**
 * Open Permission Screen
 */
fun Context.openPermissionSettings() {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.parse("package:$packageName")
    ).apply {
        addCategory(Intent.CATEGORY_DEFAULT)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(this)
    }
}



/**
 * Glide load image
 */
fun AppCompatImageView.loadImage(uri: Uri, @DrawableRes placeholder: Int?=null) {
    Glide.with(context)
        .load(uri)
        .transition(DrawableTransitionOptions.withCrossFade()).apply(){
            placeholder?.let{
                error(it)
            }
        }.into(this)
}


fun AppCompatActivity.addFragments(fragment: Fragment, frameId: Int, addToStack: Boolean? = false) {
    supportFragmentManager.inTransaction {
        if (addToStack == true) {
            add(frameId, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
        } else {
            add(frameId, fragment, fragment.javaClass.simpleName)
        }
    }
}

/**
 * Fragment Transaction
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}