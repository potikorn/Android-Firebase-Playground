package th.potikorn.firebaseplayground.ui.chat

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_chat_list.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.extensions.showToast
import th.potikorn.firebaseplayground.ui.adapter.chatlist.ChatListAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.dialog.CreateChatRoomDialog
import th.potikorn.firebaseplayground.ui.viewmodel.ChatViewModel
import java.util.Date

class ChatListActivity : BaseActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mFireStore: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val chatListAdapter: ChatListAdapter by lazy { ChatListAdapter() }
    private val createChatRoomDialog: CreateChatRoomDialog by lazy { CreateChatRoomDialog.newInstance() }
    private val chatViewModel: ChatViewModel by lazy { bindViewModel<ChatViewModel>() }

    override fun layoutToInflate(): Int = R.layout.activity_chat_list

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
        rvChatList.apply {
            layoutManager = LinearLayoutManager(this@ChatListActivity)
            adapter = chatListAdapter
        }
        fabAddNewRoom.setOnClickListener {
            when (mAuth.currentUser != null) {
                true -> {
                    createChatRoomDialog.apply {
                        setOnSubmitListener { chatRoomName ->
                            chatListAdapter.insertNewChatRoom(
                                ChatListDao(
                                    chatRoomName,
                                    mAuth.currentUser?.displayName,
                                    mAuth.currentUser?.uid
                                )
                            )
                            saveNewChatRoomToDB(chatRoomName)
                        }
                        show(supportFragmentManager, CreateChatRoomDialog::class.java.simpleName)
                    }
                }
                false -> {
                    showToast("Please sing in!")
                }
            }

        }
    }

    override fun initialize() {
        chatViewModel.liveChatListData.observe(this, Observer {
            it?.let { data ->
                chatListAdapter.setItems(data)
            }
        })
        chatViewModel.getMyChatList(mAuth.currentUser?.uid)
    }

    private fun saveNewChatRoomToDB(chatRoomName: String) {
        val chatRoom = HashMap<String, Any>()
        chatRoom["chat_room_name"] = chatRoomName
        chatRoom["uid"] = mAuth.currentUser?.uid ?: ""
        chatRoom["owner"] = mAuth.currentUser?.displayName.toString()
        chatRoom["messages"] = listOf<String>()
        chatRoom["created_at"] = Date()
        mFireStore.collection("chat-room")
            .add(chatRoom)
            .addOnSuccessListener {
                showToast("SUCCESS")
            }
            .addOnFailureListener {
                showToast("FAILURE")
            }
    }
}