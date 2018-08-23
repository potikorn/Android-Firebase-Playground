package th.potikorn.firebaseplayground.dao

import com.google.firebase.database.PropertyName

data class ChatListDao(
    @get:PropertyName("chat_room_name") @set:PropertyName("chat_room_name") var chatRoomName: String? = null,
    var owner: String? = null,
    var members: HashMap<String, Boolean>? = null
)