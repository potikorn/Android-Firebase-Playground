package th.potikorn.firebaseplayground.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat.startActivityForResult
import android.support.v4.content.FileProvider
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.BuildConfig
import th.potikorn.firebaseplayground.constants.CAPTURE_IMAGE_REQUEST
import th.potikorn.firebaseplayground.constants.PICK_IMAGE_REQUEST
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

object UploadImageFileManager {

    var mCurrentPhotoPath: String? = null

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("ddMMyyyy_HHmmss").format(Date())
        val imageFileName = "${timeStamp}_"
        val storageDir = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM
            ), "Camera"
        )
        val image = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
        )

        mCurrentPhotoPath = "file:${image.absolutePath}"
        return image
    }

    fun scanMediaFile(context: Context, imgPath: Uri) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(imgPath.path),
            null
        ) { _, _ ->
            Logger.e("Scan Complete")
        }
    }

    fun dispatchTakePicture(activity: Activity) {
        var photoFile: File? = null
        try {
            photoFile = UploadImageFileManager.createImageFile()
        } catch (ex: IOException) {
            ex.printStackTrace()
        }
        photoFile?.let { file ->
            val photoUri = FileProvider.getUriForFile(
                activity.applicationContext,
                BuildConfig.APPLICATION_ID + ".provider", file
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(activity.packageManager) != null) {
                startActivityForResult(
                    activity,
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri),
                    CAPTURE_IMAGE_REQUEST,
                    null
                )
            }
        } ?: kotlin.run {
            //            showToast(getString(R.string.msg_please_try_again))
        }
    }

    fun pickImageFromGallery(activity: Activity) {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_PICK
        }
        startActivityForResult(
            activity,
            Intent.createChooser(intent, "Select Picture"),
            PICK_IMAGE_REQUEST,
            null
        )
    }
}