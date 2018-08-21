package th.potikorn.firebaseplayground.dao

data class MessagesDao(
    var user: UserFireBaseDao? = null,
    var text: String? = null,
    var post_date: Long? = null
)