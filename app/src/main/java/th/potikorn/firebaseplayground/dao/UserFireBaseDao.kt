package th.potikorn.firebaseplayground.dao

import com.google.firebase.database.PropertyName

data class UserFireBaseDao(
    @get:PropertyName("username") @set:PropertyName("username") var displayName: String? = null,
    var email: String? = null,
    var uid: String? = null
)