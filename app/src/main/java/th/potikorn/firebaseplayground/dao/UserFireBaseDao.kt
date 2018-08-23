package th.potikorn.firebaseplayground.dao

import com.google.firebase.database.PropertyName

data class UserFireBaseDao(
    @get:PropertyName("display_name") @set:PropertyName("display_name") var displayName: String? = null,
    var email: String? = null,
    var uid: String? = null
)