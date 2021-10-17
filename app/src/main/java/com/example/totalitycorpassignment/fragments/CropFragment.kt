package com.example.totalitycorpassignment.fragments

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.totalitycorpassignment.R
import com.example.totalitycorpassignment.activity.EditImageActivity
import com.example.totalitycorpassignment.base.BaseFragment
import com.example.totalitycorpassignment.callbacks.DocumentListener
import com.example.totalitycorpassignment.databinding.FragmentCropBinding
import com.example.totalitycorpassignment.utils.*
import com.isseiaoki.simplecropview.callback.CropCallback
import com.isseiaoki.simplecropview.callback.LoadCallback
import com.isseiaoki.simplecropview.callback.SaveCallback
import java.io.IOException

class CropFragment : BaseFragment<FragmentCropBinding>(R.layout.fragment_crop) {

    private var mSourceUri: Uri? = null

    private var mDocumentListener: DocumentListener? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }


    override fun initView() {

        mDocumentListener = (activity as EditImageActivity)
        mSourceUri = requireArguments().getParcelable(SELECTED_IMAGE)



        mSourceUri?.let { uri ->
            Utility.getBitmapFromUri(mActivity,uri,callback = { bitmap ->
                bitmap?.let {
                    Utility.getCacheImagePath(mActivity,"${System.currentTimeMillis()}".plus(FILE_TYPE),bitmap)?.let { storeUri->
                        mSourceUri=storeUri
                        Glide.with(mActivity).load(storeUri).listener(object :
                            RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                mLoadCallback.onError(e)
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                mLoadCallback.onSuccess()
                                return false
                            }
                        }).into(mDataBinding.cropImage)
                    }?: kotlin.run {
                    }
                }?: kotlin.run {
                }
            })
        }?: kotlin.run {
        }

        mDataBinding.tvDone.setSafeOnClickListener {
            mDataBinding.cropImage.crop(mSourceUri).execute(mCropCallback)
        }
    }


    /**
     *  LoadCallback
     *  for load pick image from gallery or camera
     */
    private val mLoadCallback: LoadCallback = object : LoadCallback {
        override fun onSuccess() {

        }

        override fun onError(e: Throwable) {

        }
    }

    /**
     * CropCallback
     * callback when cropping completes
     */
    private val mCropCallback: CropCallback = object : CropCallback {
        override fun onSuccess(cropped: Bitmap) {
            mSourceUri?.let {
                Utility.getCacheImagePath(mActivity, "${System.currentTimeMillis()}".plus(FILE_TYPE),cropped)?.let {
                    mSaveCallback.onSuccess(it)
                }?: kotlin.run {
                    mSaveCallback.onError(IOException())
                }
            }
        }

        override fun onError(e: Throwable) {

        }
    }

    /**
     * SaveCallback
     * callback when cropped image was saved
     */
    private val mSaveCallback: SaveCallback = object : SaveCallback {
        override fun onSuccess(uri: Uri?) {
            Log.d("TAG","Successfully save cropped image")
            mDocumentListener?.done(uri)
        }

        override fun onError(e: Throwable?) {

        }
    }




    companion object{
        @JvmStatic
        fun newInstance() = CropFragment().apply {

        }
    }

}