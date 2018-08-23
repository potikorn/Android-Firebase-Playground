package th.potikorn.firebaseplayground.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.dao.UserFireBaseDao
import java.util.Date

class ChatRepository {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mRealTimeDb: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

    fun requestGetChatList(
        onSuccess: ((data: MutableList<ChatListDao>) -> Unit)? = null,
        onFailure: ((errorMessage: String) -> Unit)? = null
    ) {
        mRealTimeDb.getReference("chat-room")
            .orderByChild("updated_at")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Logger.e("get failed with ${databaseError.message}")
                    onFailure?.invoke(databaseError.message)
                }

                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    val chatListDao = mutableListOf<ChatListDao>()
                    for (chatRoom in dataSnapShot.children) {
                        Logger.e(chatRoom.value.toString())
                        val snapShot = chatRoom.getValue(ChatListDao::class.java)
                        snapShot?.members?.filter {
                            it.key == mAuth.currentUser?.uid
                        }?.forEach {
                            chatListDao.add(
                                ChatListDao(
                                    snapShot.chatRoomName,
                                    snapShot.owner,
                                    snapShot.members
                                )
                            )
                        }
                    }
                    onSuccess?.invoke(chatListDao.reversed().toMutableList())
                }
            })
    }

    fun requestCreateChatRoom(
        chatRoomName: String,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((errorMessage: String) -> Unit)? = null
    ) {
        val chatRoom = HashMap<String, Any>()
        chatRoom["chat_room_name"] = chatRoomName
        chatRoom["owner"] = mAuth.currentUser?.uid ?: ""
        chatRoom["members"] = hashMapOf<String, Any>((mAuth.currentUser?.uid ?: "") to true)
        chatRoom["updated_at"] = Date().time
        val key = mRealTimeDb.reference.push().key
        mRealTimeDb.reference
            .child("chat-room")
            .child(key.toString())
            .updateChildren(chatRoom)
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
        mRealTimeDb.getReference("chat-room")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    databaseError.toException().printStackTrace()
                    onFailure(databaseError.message)
                }

                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    dataSnapShot.children.forEach {
                        if (it.child("chat_room_name").value == chatRoomName) {
                            it.ref.removeValue()
                        }
                    }
                    onSuccess()
                }
            })
    }

    fun requestGetChatMessages(
        chatRoomName: String,
        onSuccess: (messages: MutableList<MessagesDao>) -> Unit,
        onFailure: (errorMessage: String) -> Unit
    ) {
        mRealTimeDb.getReference("chat-room")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    databaseError.toException().printStackTrace()
                    onFailure(databaseError.message)
                }

                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    Logger.e(dataSnapShot.value.toString())
                    dataSnapShot.children.forEach { chatRoom ->
                        if (chatRoom.child("chat_room_name").value == chatRoomName) {
                            Logger.e("${chatRoom.value}")
                            chatRoom.child("messages").ref
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onCancelled(databaseError: DatabaseError) {
                                        databaseError.toException().printStackTrace()
                                        onFailure(databaseError.message)
                                    }

                                    override fun onDataChange(messages: DataSnapshot) {
                                        val messagesData = mutableListOf<MessagesDao>()
                                        messages.children.forEach { msg ->
                                            Logger.e("${msg.value}")
                                            val messageDao = msg.getValue(MessagesDao::class.java)
                                            messagesData.add(
                                                MessagesDao(
                                                    messageDao?.user,
                                                    messageDao?.text,
                                                    messageDao?.post_date
                                                )
                                            )
                                        }
                                        onSuccess(messagesData)
                                    }
                                })
                        }
                    }
                }
            })
    }

    fun requestSendMessage(
        payLoad: Pair<String, String?>,
        result: Pair<() -> Unit, (errorMsg: String) -> Unit>? = null
    ) {
        mRealTimeDb.getReference("chat-room")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    databaseError.toException().printStackTrace()
                    result?.second?.invoke(databaseError.message)
                }

                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    Logger.e(dataSnapShot.value.toString())
                    dataSnapShot.children.forEach { chatRoom ->
                        if (chatRoom.child("chat_room_name").value == payLoad.second) {
                            val msgMap = HashMap<String, Any>()
                            msgMap["user"] = mAuth.currentUser?.uid ?: ""
                            msgMap["text"] = payLoad.first
                            msgMap["post_date"] = Date().time
                            chatRoom.child("messages")
                                .ref
                                .push()
                                .setValue(msgMap)
                                .addOnSuccessListener {
                                    result?.first?.invoke()
                                }
                                .addOnFailureListener {
                                    result?.second?.invoke(it.message.toString())
                                }
                        }
                    }
                }
            })
    }
}