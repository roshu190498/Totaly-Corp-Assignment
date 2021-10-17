package com.example.totalitycorpassignment.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import android.provider.MediaStore.Images

import android.graphics.Bitmap
import android.content.ContentValues








const val SELECTION_TYPE_IMAGE = 1
const val SELECTION_TYPE_DOCUMENT = 2
const val SELECTION_TYPE_ALL = 3

fun getIntentForPickImage() : Intent {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
    intent.action = Intent.ACTION_GET_CONTENT
    return intent
}

fun getIntentForTakeSelfie(context: Context) : Intent {
    val mFileName =  "${System.currentTimeMillis()}".plus(FILE_TYPE)
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {

    }
    return intent
}


//
//fun getFileUri(context: Context, uri: Uri): FileUri {
//    var mAttachedFileList: ArrayList<String>? = ArrayList<String>()
//    val contentResolver = context?.contentResolver
//    val cursor: Cursor? = contentResolver?.query(uri, null, null, null, null, null)
//    var size: Long? = null
//    var name: String = ""
//    cursor?.let {
//        it
//        it.use {
//            if (it.moveToFirst()) {
//                mAttachedFileList?.add(cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).toString())
//                name = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).toString()
//                val sizeIndex: Int? = cursor?.getColumnIndex(OpenableColumns.SIZE)
//                sizeIndex?.let {
//                    size = cursor.getString(sizeIndex).toLong()
//                } ?: "Unknown"
//            }
//        }
//    }
//    return FileUri(name, size, cursor, uri, mAttachedFileList)
//}

data class FileUri(
    val fileName: String?,
    val fileSize: Long?,
    val cursor: Cursor?,
    val uri: Uri?,
    val mAttachedFileList: ArrayList<String>?
)

fun validateFileName(fileUri: FileUri) {

}

fun validateFileSize(allocateSize: Long, fileUri: FileUri): Boolean {
    var isFileSizeValid = false

    if (fileUri.fileSize != null && fileUri.fileSize <= allocateSize) {
        isFileSizeValid = true
    }
    return isFileSizeValid
}

fun writeFile(context: Context, fileUri: FileUri, file: File) {
    val filesInFolder = file.listFiles()
    val sourceStream = fileUri.uri?.let { getDocumentArrayStream(context, it) }

    if (!filesInFolder.isNullOrEmpty()) {
        val destinationFile = File(file, fileUri.fileName)
        destinationFile.createNewFile()
        context?.contentResolver?.openOutputStream(Uri.fromFile(destinationFile)).use { out ->
            out?.let { sourceStream?.writeTo(out) }
        }
    } else {
        val destinationFile = File(file, fileUri.fileName)
        destinationFile.createNewFile()
        var fileOutputStream: FileOutputStream? = FileOutputStream(destinationFile)
        sourceStream?.writeTo(fileOutputStream)
    }
}

fun validateTotalSize(allocateSize: Long, fileUri: FileUri, file: File): Boolean {
    var isFileSizeValid = false
    var saveFileSize = 0L
    var fileSize = fileUri.fileSize

    val filesInFolder = file.listFiles()

    filesInFolder.forEach {
        val fileLengthInBytes = fileSize(it)
        saveFileSize = saveFileSize.plus(fileLengthInBytes)
    }
    if (fileSize != null && fileSize?.plus(saveFileSize) <= allocateSize) {
        isFileSizeValid = true
    }

    return isFileSizeValid
}

fun getFilesInDir(file: File): ArrayList<String>? {
    var mAttachedFileList: ArrayList<String>? = ArrayList<String>()
    val filesInFolder = file.listFiles()
    filesInFolder.forEach {
        it
        mAttachedFileList?.add(it.name)
    }
    return mAttachedFileList
}

fun openFileManager(documentType: Int): Intent {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

    if (documentType == SELECTION_TYPE_IMAGE) {
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        intent.action = Intent.ACTION_GET_CONTENT

    } else if (documentType == SELECTION_TYPE_DOCUMENT) {
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "application/pdf"
        intent.flags = intent.flags or Intent.FLAG_GRANT_READ_URI_PERMISSION

    } else if (documentType == SELECTION_TYPE_ALL) {
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.setType("image/*|application/pdf")
        intent.flags = intent.flags or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    return intent
}

fun createDirectory(context: Context, fileName: String?): File {
    val file: File = File(context.getFilesDir(), fileName)
    if (!file.exists()) {
        file.mkdir()
    }
    return file
}

@Throws(IOException::class)
fun getDocumentArrayStream(context: Context, uri: Uri): ByteArrayOutputStream {
    val byteArrayOutputStream = ByteArrayOutputStream()
    context.contentResolver?.openInputStream(uri)?.use { inputStream ->
        val buffer = ByteArray(1024)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead)
        }
    }
    return byteArrayOutputStream
}

fun isDocumentTypeValid(documentName: String?): Boolean {
    val documentType = getDocumentTypeFromDocumentName(documentName)
    when (documentType) {
        "jpeg", "jpg", "png" -> {
            return true
        }
        "pdf" -> {
            return true

        }
        else -> {
            return false
        }
    }
}

fun fileSize(root: File?): Long {
    if (root == null) {
        return 0
    }
    if (root.isFile) {
        return root.length()
    }
    try {
        if (isSymlink(root)) {
            return 0
        }
    } catch (e: IOException) {
        e.printStackTrace()
        return 0
    }
    var length: Long = 0
    val files = root.listFiles() ?: return 0
    for (file in files) {
        length += fileSize(file)
    }
    return length
}

@Throws(IOException::class)
fun isSymlink(file: File): Boolean {
    val canon: File
    canon = if (file.parent == null) {
        file
    } else {
        val canonDir = file.parentFile.canonicalFile
        File(canonDir, file.name)
    }
    return !canon.canonicalFile.equals(canon.absoluteFile)
}

fun getDocumentTypeFromDocumentName(documentName: String?): String? {
    documentName?.let {
        val separator = "."
        val separatorPosition: Int = it.indexOf(separator)
        val getDocumentType = it.substring(separatorPosition + separator.length)
        return getDocumentType
    } ?: return ""
}


fun getFileUri(context: Context, uri: Uri): FileUri {
    var mAttachedFileList: ArrayList<String>? = ArrayList<String>()
    val contentResolver = context?.contentResolver
    val cursor: Cursor? = contentResolver?.query(uri, null, null, null, null, null)
    var size: Long? = null
    var name: String = ""
    cursor?.let {
        it
        it.use {
            if (it.moveToFirst()) {
                mAttachedFileList?.add(cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).toString())
                name = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).toString()
                val sizeIndex: Int? = cursor?.getColumnIndex(OpenableColumns.SIZE)
                sizeIndex?.let {
                    size = cursor.getString(sizeIndex).toLong()
                } ?: "Unknown"
            }
        }
    }

    return FileUri(name, size, cursor, uri, mAttachedFileList)
}


fun addImageToGallery(filePath: String?, context: Context) {
    val values = ContentValues()
    values.put(Images.Media.DATE_TAKEN, System.currentTimeMillis())
    values.put(Images.Media.MIME_TYPE, "image/jpeg")
    values.put(MediaStore.MediaColumns.DATA, "edited"+filePath)
    context.contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
}


fun getFilePath(uri : Uri) : String?{
    val file = File(uri.path) //create path from uri

    val split = file.path.split(":").toTypedArray() //split the path.

    return split[1] //assign it to a string(your choice).

}

fun getImageUri(inContext: Context, inImage: Bitmap?): Uri? {
    val path = Images.Media.insertImage(
        inContext.contentResolver, inImage,
        "Title", null
    )
    return Uri.parse(path)
}
