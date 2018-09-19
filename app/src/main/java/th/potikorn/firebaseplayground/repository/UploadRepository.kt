package th.potikorn.firebaseplayground.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.extensions.getNowTime
import java.io.File

class UploadRepository {

    private val mStorage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    private val mFirebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun requestUploadImage(
        roomRefKey: String,
        imgFile: File,
        onSuccessBlock: ((imgPath: String) -> Unit)? = null
    ) {
        val pathRef = mStorage.reference
            .child("firebase-playground")
            .child(roomRefKey)
            .child(mFirebaseAuth.currentUser?.uid ?: "")
            .child("${getNowTime()}${imgFile.extension}")
        pathRef.putFile(Uri.fromFile(imgFile))
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { uri ->
                    Logger.e(uri.toString())
                    onSuccessBlock?.invoke(uri.toString())
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                Logger.e(it.message.toString())
            }
    }
}