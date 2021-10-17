package com.example.totalitycorpassignment.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.totalitycorpassignment.R
import com.example.totalitycorpassignment.base.BaseActivity
import com.example.totalitycorpassignment.databinding.ActivityHomeBinding
import com.example.totalitycorpassignment.utils.*

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

            startActivityForResult(Intent(this@HomeActivity, EditImageActivity::class.java).apply {
                putExtra(OPERATION_CODE, PICK_CAMERA_IMAGE)
            }, PICK_CAMERA_IMAGE)
        }

        mDataBinding.btnPickImage.setSafeOnClickListener {
            startActivityForResult(Intent(this, EditImageActivity::class.java).apply {
                putExtra(OPERATION_CODE, PICK_GALLERY_IMAGE)
            }, PICK_GALLERY_IMAGE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_GALLERY_IMAGE -> {
                    val imageData = data?.extras?.get(IMAGE)?.let { it as Uri }
                    imageData?.let { mDataBinding.ivPreview.loadImage(it) }
                }
                PICK_CAMERA_IMAGE -> {
                    val imageData = data?.extras?.get(IMAGE)?.let { it as Uri }
                    imageData?.let { mDataBinding.ivPreview.loadImage(it) }
                }
                else -> {
                    //  failToUploaDocument()
                }
            }
        }
    }
}