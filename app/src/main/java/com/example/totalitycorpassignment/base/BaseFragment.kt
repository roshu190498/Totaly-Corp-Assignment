package com.example.totalitycorpassignment.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<DB : ViewDataBinding>(@LayoutRes val layoutRes: Int) : Fragment() {

    protected lateinit var mDataBinding: DB

    protected lateinit var mActivity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as AppCompatActivity).let {
            this.mActivity = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        mDataBinding.root.isClickable = true
        return mDataBinding.root
    }

    abstract fun initView()

    open fun getRootParentFragment(fragment: Fragment): Fragment? {
        val parent = fragment.parentFragment
        return parent?.let { getRootParentFragment(it) } ?: fragment
    }
}