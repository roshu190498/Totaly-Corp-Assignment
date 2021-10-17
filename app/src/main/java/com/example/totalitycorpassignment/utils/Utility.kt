package com.example.totalitycorpassignment.utils

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.OpenableColumns
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class Utility {
    companion object{
        /**
         * useful to create cache directory
         */
        fun getCacheImagePath(context: Context, fileName: String): Uri? {
            val path = File(context.externalCacheDir, "camera")
            if (!path.exists()) path.mkdirs()
            val image = File(path, fileName)
            return image.toUri()
        }

        /**
         * get bitmap from uri
         */
        fun getBitmapFromUri(context: Context,uri: Uri,callback:(bitmap: Bitmap?)->Unit){
            Glide.with(context)
                .asBitmap()
                .load(uri)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        callback.invoke(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        callback.invoke(null)
                    }
                })
        }

        /**
         * store bitmap in external cache and get Uri
         */
        fun getCacheImagePath(context: Context,fileName: String,bitmap: Bitmap): Uri? {
            val path = File(context.externalCacheDir, "camera")
            if (!path.exists()) path.mkdirs()
            val image = File(path, fileName)
            try {
                FileOutputStream(image).apply {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, this)
                    flush()
                    close()
                }
            }catch (e: FileNotFoundException){
            }catch (e: IOException){
            }catch (e:Exception){
            }

            return image.toUri()
        }

        /**
         * Get file path from uri
         */
        fun getFileProviderFilePath(context: Context,uri: Uri):String{
            return (File(File(context.externalCacheDir, "camera"), queryName(context.contentResolver, uri)?:"").path).also {

            }
        }



        /**
         * Get file name from uri
         */
        fun queryName(resolver: ContentResolver, uri: Uri): String? {
            val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
            val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            val name: String = returnCursor.getString(nameIndex)
            returnCursor.close()
            return name
        }

    }



}