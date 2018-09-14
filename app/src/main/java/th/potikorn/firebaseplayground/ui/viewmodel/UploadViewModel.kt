package th.potikorn.firebaseplayground.ui.viewmodel

import android.arch.lifecycle.ViewModel
import th.potikorn.firebaseplayground.repository.UploadRepository
import java.io.File
import javax.inject.Inject

class UploadViewModel @Inject constructor(private val uploadRepository: UploadRepository) : ViewModel() {

    fun uploadImage(roomRefKey: String, imgFile: File) {
        uploadRepository.requestUploadImage(roomRefKey, imgFile)
    }
}