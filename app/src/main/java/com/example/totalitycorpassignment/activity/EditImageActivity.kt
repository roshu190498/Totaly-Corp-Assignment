package com.example.totalitycorpassignment.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.totalitycorpassignment.R
import com.example.totalitycorpassignment.base.BaseActivity
import com.example.totalitycorpassignment.databinding.ActivityEditImageBinding
import com.example.totalitycorpassignment.utils.*
import java.io.File
import android.graphics.Matrix
import android.widget.ImageView
import com.example.totalitycorpassignment.callbacks.DocumentListener
import com.example.totalitycorpassignment.fragments.CropFragment


class EditImageActivity : BaseActivity<ActivityEditImageBinding>(R.layout.activity_edit_image),DocumentListener {

    private var REQUEST_CODE: Int? = null
    private var mFileName: String? = null

    private var selectedImage : Uri?= null
    private var finalImage : Uri?= null
    private var cropedImage : Uri?= null
    private var rotationAngle = 90f

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    lateinit var activityResultLauncherCapture: ActivityResultLauncher<Intent>


    var requestPermissionForPickImage =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                activityResultLauncher.launch(getIntentForPickImage())
            }
        }

    var requestPermissioForCamera =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                activityResultLauncherCapture.launch(getIntentForTakeSelfie(this))
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun handleResult(data: Intent?) {
        lateinit var dir: File
        data?.data?.let { uri ->
            this?.let { activity ->
                val fileUri = activity?.let { getFileUri(it, uri) }
                fileUri?.let { fileUri ->
                    selectedImage = fileUri.uri!!
                    finalImage =selectedImage
                    mDataBinding.ivPreview.loadImage(fileUri.uri!!)
                }
            }
        }
    }

    override fun initView() {
        REQUEST_CODE = intent.getIntExtra(OPERATION_CODE, 0)

        finalImage = selectedImage

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    //handle result here
                    handleResult(data)
                }
            }

        activityResultLauncherCapture =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    var bitmap = data?.extras?.get("data") as Bitmap
                    var uri = getImageUri(this,bitmap)
                    selectedImage = uri
                    finalImage = selectedImage
                    mDataBinding.ivPreview.loadImage(uri!!)
                }
            }

        when (REQUEST_CODE) {
            PICK_GALLERY_IMAGE -> {
                requestPermissionForPickImage.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            PICK_CAMERA_IMAGE -> {
                requestPermissioForCamera.launch(Manifest.permission.CAMERA)
            }
            else -> {

            }
        }
        iniClickListner()
    }

    private fun iniClickListner() {
        mDataBinding.tvUndo.setSafeOnClickListener {
            //restore to default
            finalImage = selectedImage
            mDataBinding.ivPreview.loadImage(selectedImage!!)
            if (rotationAngle != 90f){
                val matrix = Matrix()
                mDataBinding.ivPreview.setScaleType(ImageView.ScaleType.MATRIX) //required
                matrix.postRotate(0.toFloat(), mDataBinding.ivPreview.width.div(2).toFloat(),mDataBinding.ivPreview.height.div(2).toFloat())
                rotationAngle=90.toFloat()
                mDataBinding.ivPreview.setImageMatrix(matrix)
            }
        }
        mDataBinding.tvRotate.setSafeOnClickListener {
            val matrix = Matrix()
            mDataBinding.ivPreview.setScaleType(ImageView.ScaleType.MATRIX) //required
            matrix.postRotate(rotationAngle, mDataBinding.ivPreview.drawable.intrinsicWidth.div(2).toFloat(), mDataBinding.ivPreview.drawable.intrinsicHeight.div(2).toFloat())
            rotationAngle+=90f
            mDataBinding.ivPreview.setImageMatrix(matrix)
        }
        mDataBinding.tvCrop.setSafeOnClickListener {
            openCropFragment(selectedImage, PICK_GALLERY_IMAGE)
        }
        mDataBinding.tvSave.setSafeOnClickListener {
            addImageToGallery(selectedImage?.let { it1 -> getFilePath(it1) },this)
            this.toast("Image saved please check in gallary")
            val d = Intent().apply {
                putExtra(IMAGE,finalImage)
            }
            setResult(Activity.RESULT_OK,d)
            finish()
        }
    }



    private fun openCropFragment(uri: Uri?,code:Int){
        mDataBinding.operationContainer.show()
        CropFragment.newInstance().apply {
            arguments= Bundle().apply {
                putParcelable(SELECTED_IMAGE,uri)
                putInt(OPERATION_CODE,code)
            }
        }.also {
            addFragments(it,R.id.operationContainer,true)
        }

    }

    override fun done(uri: Uri?) {
        mDataBinding.operationContainer.hide()
        cropedImage = uri
        finalImage = cropedImage
        cropedImage?.let { mDataBinding.ivPreview.loadImage(it) }
    }

}