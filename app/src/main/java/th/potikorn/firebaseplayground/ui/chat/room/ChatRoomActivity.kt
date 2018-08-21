package th.potikorn.firebaseplayground.ui.chat.room

import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_chat_room.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.ui.adapter.chatmsg.ChatMessagesAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import java.util.Date

class ChatRoomActivity : BaseActivity() {

    private val chatMessageAdapter: ChatMessagesAdapter by lazy { ChatMessagesAdapter() }

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
                    chatMessageAdapter.insertNewChatMessage(
                        MessagesDao(
                            text = this,
                            post_date = Date().time
                        )
                    )
                }
            }
        }
        rvChatMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter = chatMessageAdapter
        }
    }

    override fun initialize() {}
}