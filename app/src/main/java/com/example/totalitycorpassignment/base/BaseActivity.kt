package com.example.totalitycorpassignment.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.setPadding
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.totalitycorpassignment.R

abstract class BaseActivity<DB : ViewDataBinding>(@LayoutRes val layoutRes: Int) :
    AppCompatActivity() {

    protected lateinit var mDataBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDataBinding = DataBindingUtil.setContentView(this, layoutRes)
    }

    abstract fun initView()
    fun setUpToolBar(toolbar: Toolbar, toolBarTitle: String, textView: AppCompatTextView, isHomeAsUpEnabled:Boolean?=true) {
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        textView.text = toolBarTitle
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(isHomeAsUpEnabled?:true)
            setDisplayShowHomeEnabled(true)
            setDisplayShowTitleEnabled(false)
        }
        if(isHomeAsUpEnabled==false){
            textView.setPadding(resources.getDimension(R.dimen._12sdp).toInt())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}