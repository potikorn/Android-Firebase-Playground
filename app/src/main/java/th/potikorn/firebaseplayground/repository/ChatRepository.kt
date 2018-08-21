package th.potikorn.firebaseplayground.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.extensions.showToast
import java.util.Date

class ChatRepository {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun requestMyChatList(
        onSuccess: ((data: MutableList<ChatListDao>) -> Unit)? = null,
        onFailure: ((errorMessage: String) -> Unit)? = null
    ) {
        mFireStore.collection("chat-room")
            .orderBy("created_at", Query.Direction.DESCENDING)
            .whereEqualTo("uid", mAuth.currentUser?.uid)
            .get().addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val chatList = mutableListOf<ChatListDao>()
                        it.result.forEach { snapShot ->
                            chatList.add(
                                ChatListDao(
                                    snapShot.data["chat_room_name"].toString(),
                                    snapShot.data["owner"].toString(),
                                    snapShot.data["uid"].toString()
                                )
                            )
                        }
                        onSuccess?.invoke(chatList)
                    }
                    false -> {
                        onFailure?.invoke(it.exception?.message.toString())
                    }
                }
            }.addOnFailureListener {
                Logger.e("get failed with ${it.message}")
            }
    }

    fun requestCreateChatRoom(
        chatRoomName: String,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((errorMessage: String) -> Unit)? = null
    ) {
        val chatRoom = HashMap<String, Any>()
        chatRoom["chat_room_name"] = chatRoomName
        chatRoom["uid"] = mAuth.currentUser?.uid ?: ""
        chatRoom["owner"] = mAuth.currentUser?.displayName.toString()
        chatRoom["messages"] = listOf<String>()
        chatRoom["created_at"] = Date()
        mFireStore.collection("chat-room")
            .add(chatRoom)
            .addOnSuccessListener {
                Logger.e(it.toString())
                onSuccess?.invoke()
            }
            .addOnFailureListener {
                onFailure?.invoke(it.message ?: "")
            }
    }
}