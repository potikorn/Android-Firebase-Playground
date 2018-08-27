package th.potikorn.firebaseplayground.dao

data class MessagesDao(
    var user: String? = null,
    var text: String? = null,
    var post_date: Long? = null
)