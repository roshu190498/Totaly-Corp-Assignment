package com.example.totalitycorpassignment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.totalitycorpassignment.base.BaseActivity
import com.example.totalitycorpassignment.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun initView() {

    }

}