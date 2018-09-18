package th.potikorn.firebaseplayground.dao

import com.google.firebase.database.PropertyName

data class MessagesDao(
    var user: String? = null,
    var text: String? = null,
    @get:PropertyName("img_path") @set:PropertyName("img_path") var imgPath: String? = null,
    var post_date: Long? = null
)