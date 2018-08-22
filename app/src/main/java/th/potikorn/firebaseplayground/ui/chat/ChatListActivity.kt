package th.potikorn.firebaseplayground.ui.chat

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat_list.*
import kotlinx.android.synthetic.main.dialog_confirm.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.extensions.hide
import th.potikorn.firebaseplayground.extensions.show
import th.potikorn.firebaseplayground.extensions.showToast
import th.potikorn.firebaseplayground.base.BaseAdapterListener
import th.potikorn.firebaseplayground.dao.UserFireBaseDao
import th.potikorn.firebaseplayground.extensions.navigate
import th.potikorn.firebaseplayground.ui.adapter.chatlist.ChatListAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.chat.room.ChatRoomActivity
import th.potikorn.firebaseplayground.ui.chat.room.ChatRoomActivity.Companion.KEY_CHAT_ROOM_NAME
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
            adapter = chatListAdapter.apply {
                setSimpleListener(object : BaseAdapterListener() {
                    override fun <DATA> onClick(data: DATA?, position: Int) {
                        val chatListDao = data as ChatListDao
                        navigate<ChatRoomActivity> {
                            putExtra(KEY_CHAT_ROOM_NAME, chatListDao.chatRoomName)
                        }
                    }

                    override fun <DATA> onLongClick(data: DATA?, position: Int) {
                        showDeleteConfirmDialog(data as ChatListDao, position)
                    }
                })
            }
        }
        fabAddNewRoom.setOnClickListener {
            when (mAuth.currentUser != null) {
                true -> {
                    createChatRoomDialog.apply {
                        setOnSubmitListener { chatRoomName ->
                            val owner = UserFireBaseDao(
                                mAuth.currentUser?.displayName,
                                mAuth.currentUser?.email,
                                mAuth.currentUser?.uid
                            )
                            chatListAdapter.insertNewChatRoom(
                                ChatListDao(
                                    chatRoomName,
                                    owner,
                                    mutableListOf(owner)
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

    private fun showDeleteConfirmDialog(chatListData: ChatListDao, position: Int) {
        Dialog(this).apply {
            setContentView(R.layout.dialog_confirm)
            tvTitle.text = getString(R.string.title_delete_chat_room)
            tvSubTitle.text = getString(R.string.title_delete_chat_room_confirm)
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            tvOKButton.setOnClickListener {
                chatListAdapter.deleteItem(position)
                chatViewModel.deleteChatRoom(chatListData.chatRoomName ?: "")
                this.dismiss()
            }
            tvCancelButton.setOnClickListener {
                this.dismiss()
            }
            show()
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
        chatViewModel.liveMessageData.observe(this, Observer {
            showToast(it)
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