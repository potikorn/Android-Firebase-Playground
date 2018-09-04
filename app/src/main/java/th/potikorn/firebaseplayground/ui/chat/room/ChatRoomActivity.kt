package th.potikorn.firebaseplayground.ui.chat.room

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat_room.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.extensions.navigate
import th.potikorn.firebaseplayground.ui.adapter.chatmsg.ChatMessagesAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.user.invite.InviteActivity
import th.potikorn.firebaseplayground.ui.viewmodel.ChatViewModel
import java.util.Date

class ChatRoomActivity : BaseActivity() {

    private val chatViewModel: ChatViewModel by lazy { bindViewModel<ChatViewModel>() }
    private val chatMessageAdapter: ChatMessagesAdapter by lazy { ChatMessagesAdapter() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private var chatRoomName: String? = null

    override fun layoutToInflate(): Int = R.layout.activity_chat_room

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
        fabMembers.setOnClickListener {
            navigate<InviteActivity> {
                putExtra(KEY_CHAT_ROOM_NAME, intent.getStringExtra(KEY_CHAT_ROOM_NAME))
                putExtra(KEY_MEMBERS, intent.getSerializableExtra(KEY_MEMBERS))
            }
        }
        ivIconSend.apply {
            setOnClickListener {
                etSendMessage.text.toString().takeIf { txt ->
                    txt.isNotEmpty()
                }?.apply {
                    etSendMessage.setText("")
                    chatMessageAdapter.insertNewChatMessage(
                        MessagesDao(
                            text = this,
                            post_date = Date().time,
                            user = mAuth.currentUser?.uid
                        )
                    )
                    rvChatMessages.smoothScrollToPosition(chatMessageAdapter.itemCount.minus(1))
                    chatViewModel.sendMessage(this, chatRoomName)
                }
            }
        }
        rvChatMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatRoomActivity).also {
                it.stackFromEnd = true
            }
            adapter = chatMessageAdapter
        }
    }

    override fun initialize() {
        getIntentData()
        chatViewModel.liveChatMessages.observe(this, Observer { messages ->
            messages?.let {
                chatMessageAdapter.setItems(it)
                // FIXME should use DiffUtil and scroll to bottom instead?
                chatMessageAdapter.itemCount.takeIf { itemCount ->
                    itemCount != 0
                }.apply {
                    rvChatMessages.smoothScrollToPosition(this?.minus(1) ?: 0)
                }
            }
        })
        chatViewModel.getChatMessages(chatRoomName ?: "")
    }

    private fun getIntentData() {
        intent?.let {
            chatRoomName = it.getStringExtra(KEY_CHAT_ROOM_NAME)
        }
    }

    companion object {
        const val KEY_CHAT_ROOM_NAME = "KEY_CHAT_ROOM_NAME"
        const val KEY_MEMBERS = "KEY_MEMBERS"
    }
}