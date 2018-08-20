package th.potikorn.firebaseplayground.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.dao.ChatListDao

class ChatRepository {

    private val mFireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun requestMyChatList(
        uid: String,
        onSuccess: ((data: MutableList<ChatListDao>) -> Unit)? = null,
        onFailure: ((errorMessage: String) -> Unit)? = null
    ) {
        mFireStore.collection("chat-room")
            .orderBy("created_at")
            .whereEqualTo("uid", uid)
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
                        onSuccess?.invoke(chatList.reversed().toMutableList())
                    }
                    false -> {
                        onFailure?.invoke(it.exception?.message.toString())
                    }
                }
            }.addOnFailureListener {
                Logger.e("get failed with ${it.message}")
            }
    }
}