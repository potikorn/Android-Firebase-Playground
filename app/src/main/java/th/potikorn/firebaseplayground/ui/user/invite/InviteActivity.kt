package th.potikorn.firebaseplayground.ui.user.invite

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_invite.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.base.BaseAdapterListener
import th.potikorn.firebaseplayground.dao.UserFireBaseDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.extensions.showToast
import th.potikorn.firebaseplayground.ui.adapter.userlist.UserListAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.chat.room.ChatRoomActivity
import th.potikorn.firebaseplayground.ui.viewmodel.ChatViewModel
import th.potikorn.firebaseplayground.ui.viewmodel.UserViewModel
import java.util.concurrent.CopyOnWriteArrayList

class InviteActivity : BaseActivity() {

    private val userListAdapter: UserListAdapter by lazy { UserListAdapter() }
    private val userViewModel: UserViewModel by lazy { bindViewModel<UserViewModel>() }
    private val chatViewModel: ChatViewModel by lazy { bindViewModel<ChatViewModel>() }

    private var chatRoomName: String? = null
    private var members = CopyOnWriteArrayList<String>()

    override fun layoutToInflate(): Int = R.layout.activity_invite

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {
        userViewModel.liveUserListData.removeObservers(this)
    }

    override fun setupInstance() {}

    override fun setupView() {
        rvUserList.apply {
            layoutManager = LinearLayoutManager(this@InviteActivity)
            adapter = userListAdapter.apply {
                setOnActionListener(object : BaseAdapterListener {
                    override fun <DATA> onClick(data: DATA?, position: Int) {
                        val userData = data as UserFireBaseDao
                        when (members.contains(userData.uid)) {
                            true -> members.remove(userData.uid.toString())
                            false -> members.add(userData.uid.toString())
                        }
                        Logger.e(members.toString())
                    }
                })
            }
        }
        btnInvite.setOnClickListener {
            chatViewModel.inviteToChat(members.toMutableList() to chatRoomName) {
                finish()
            }
        }
    }

    override fun initialize() {
        getIntentData()
        userViewModel.liveUserListData.observe(this, Observer {
            it?.let { userList ->
                userListAdapter.setItems(userList)
            }
        })
        chatViewModel.liveMessageData.observe(this, Observer { showToast(it) })
        chatViewModel.liveErrorData.observe(this, Observer { showToast(it) })
        userViewModel.getAllUsers(members)
    }

    private fun getIntentData() {
        intent?.let {
            chatRoomName = it.getStringExtra(ChatRoomActivity.KEY_CHAT_ROOM_NAME)
            val hashMembers = it.getSerializableExtra(ChatRoomActivity.KEY_MEMBERS) as HashMap<*, *>
            hashMembers.forEach { member ->
                members.add(member.key.toString())
            }
        }
    }
}