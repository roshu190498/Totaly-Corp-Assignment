package com.example.totalitycorpassignment.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.example.totalitycorpassignment.R
import com.example.totalitycorpassignment.base.BaseActivity
import com.example.totalitycorpassignment.databinding.ActivityHomeBinding
import com.example.totalitycorpassignment.utils.setSafeOnClickListener
import com.example.totalitycorpassignment.utils.toast

class HomeActivity : BaseActivity<ActivityHomeBinding>(R.layout.activity_home) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

        initClickLisnter()
    }

    private fun initClickLisnter() {

        mDataBinding.btnTakeSelfie.setSafeOnClickListener {
            this.toast(getString(R.string.txtTakeSelfie))
        }

        mDataBinding.btnPickImage.setSafeOnClickListener {
            this.toast(getString(R.string.txtPickImage))
        }


    }
}