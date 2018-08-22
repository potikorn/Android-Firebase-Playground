package th.potikorn.firebaseplayground.dao

import com.google.firebase.database.PropertyName

data class ChatListDao(
    @get:PropertyName("chat_room_name") @set:PropertyName("chat_room_name") var chatRoomName: String? = null,
    var owner: UserFireBaseDao? = null,
    var members: MutableList<UserFireBaseDao>? = null
)