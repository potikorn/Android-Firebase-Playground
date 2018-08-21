package th.potikorn.firebaseplayground.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.dao.UserFireBaseDao
import java.util.Date

class ChatRepository {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun requestGetChatList(
        onSuccess: ((data: MutableList<ChatListDao>) -> Unit)? = null,
        onFailure: ((errorMessage: String) -> Unit)? = null
    ) {
        mFireStore.collection("chat-room")
            .orderBy("updated_at", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> {
                        val chatList = mutableListOf<ChatListDao>()
                        val snapShot = it.result.toObjects(ChatListDao::class.java)
                        snapShot.filter { rawData ->
                            rawData.members?.contains(
                                UserFireBaseDao(
                                    mAuth.currentUser?.displayName,
                                    mAuth.currentUser?.email,
                                    mAuth.currentUser?.uid
                                )
                            ) ?: false
                        }.forEach { filtered ->
                            chatList.add(
                                ChatListDao(
                                    filtered.chatRoomName,
                                    filtered.owner,
                                    filtered.members
                                )
                            )
                        }
                        onSuccess?.invoke(chatList)
                    }
                    false -> {
                        it.exception?.printStackTrace()
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
        val ownerChatRoom = HashMap<String, Any>()
        ownerChatRoom["email"] = mAuth.currentUser?.email ?: ""
        ownerChatRoom["uid"] = mAuth.currentUser?.uid ?: ""
        ownerChatRoom["display_name"] = mAuth.currentUser?.displayName.toString()
        val chatRoom = HashMap<String, Any>()
        chatRoom["chat_room_name"] = chatRoomName
        chatRoom["owner"] = ownerChatRoom
        chatRoom["members"] = listOf(ownerChatRoom)
        chatRoom["messages"] = listOf<String>()
        chatRoom["updated_at"] = Date()
        mFireStore.collection("chat-room")
            .add(chatRoom)
            .addOnSuccessListener {
                onSuccess?.invoke()
            }
            .addOnFailureListener {
                it.printStackTrace()
                onFailure?.invoke(it.message ?: "")
            }
    }

    fun requestRemoveChatRoom(
        chatRoomName: String,
        onSuccess: () -> Unit,
        onFailure: (message: String) -> Unit
    ) {
        val dbRef = mFireStore.collection("chat-room").whereEqualTo("chat_room_name", chatRoomName)
        dbRef.get().continueWith {
            it.result.forEach { snapShot ->
                snapShot
                    .reference
                    .delete()
                    .addOnSuccessListener { _ ->
                        onSuccess()
                    }
                    .addOnFailureListener { exception ->
                        exception.printStackTrace()
                        onFailure(exception.message.toString())
                    }
            }
        }
    }
}