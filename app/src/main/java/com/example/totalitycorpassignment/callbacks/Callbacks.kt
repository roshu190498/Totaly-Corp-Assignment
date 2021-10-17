package com.example.totalitycorpassignment.callbacks

import android.net.Uri
import com.isseiaoki.simplecropview.callback.Callback


interface LoadCallback : Callback {
    fun onSuccess()
}



interface DocumentListener {
    fun done(uri: Uri?)
}