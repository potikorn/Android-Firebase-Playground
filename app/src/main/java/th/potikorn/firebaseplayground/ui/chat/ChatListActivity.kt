package th.potikorn.firebaseplayground.ui.chat

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat_list.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.extensions.hide
import th.potikorn.firebaseplayground.extensions.show
import th.potikorn.firebaseplayground.extensions.showToast
import th.potikorn.firebaseplayground.ui.adapter.chatlist.ChatListAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.dialog.CreateChatRoomDialog
import th.potikorn.firebaseplayground.ui.viewmodel.ChatViewModel

class ChatListActivity : BaseActivity() {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
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
        srlChatList.setOnRefreshListener {
            chatViewModel.getMyChatList(true)
        }
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
                            chatViewModel.createChatRoom(chatRoomName)
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
        chatViewModel.liveLoadingState.observe(this, Observer {
            when (it) {
                true -> pbLoading.show()
                false -> pbLoading.hide()
            }
        })
        chatViewModel.liveRefreshState.observe(this, Observer {
            srlChatList.isRefreshing = it ?: false
        })
        chatViewModel.liveErrorData.observe(this, Observer {
            showToast(it)
        })
        chatViewModel.liveChatListData.observe(this, Observer {
            it?.let { data ->
                chatListAdapter.setItems(data)
            }
        })
        chatViewModel.getMyChatList()
    }
}