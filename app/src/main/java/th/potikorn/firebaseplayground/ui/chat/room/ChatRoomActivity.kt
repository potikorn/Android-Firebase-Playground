package th.potikorn.firebaseplayground.ui.chat.room

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_chat_room.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.dao.UserFireBaseDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.ui.adapter.chatmsg.ChatMessagesAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.viewmodel.ChatViewModel
import java.util.Date

class ChatRoomActivity : BaseActivity() {

    private val chatViewModel: ChatViewModel by lazy { bindViewModel<ChatViewModel>() }
    private val chatMessageAdapter: ChatMessagesAdapter by lazy { ChatMessagesAdapter() }
    private var chatRoomName: String? = null
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun layoutToInflate(): Int = R.layout.activity_chat_room

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
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
                            user = UserFireBaseDao(
                                mAuth.currentUser?.displayName,
                                mAuth.currentUser?.email,
                                mAuth.currentUser?.uid
                            )
                        )
                    )
                    chatViewModel.sendMessage(this, chatRoomName)
                }
            }
        }
        rvChatMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter = chatMessageAdapter
        }
    }

    override fun initialize() {
        getIntentData()
        chatViewModel.liveChatMessages.observe(this, Observer { messages ->
            messages?.let {
                chatMessageAdapter.setItems(it)
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
    }
}