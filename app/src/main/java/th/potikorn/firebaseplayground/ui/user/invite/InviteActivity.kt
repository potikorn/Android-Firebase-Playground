package th.potikorn.firebaseplayground.ui.user.invite

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_invite.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.ui.adapter.userlist.UserListAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.chat.room.ChatRoomActivity
import th.potikorn.firebaseplayground.ui.viewmodel.UserViewModel

class InviteActivity : BaseActivity() {

    private val userListAdapter: UserListAdapter by lazy { UserListAdapter() }
    private val userViewModel: UserViewModel by lazy { bindViewModel<UserViewModel>() }

    private var members: MutableList<String> = mutableListOf()

    override fun layoutToInflate(): Int = R.layout.activity_invite

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
        rvUserList.apply {
            layoutManager = LinearLayoutManager(this@InviteActivity)
            adapter = userListAdapter
        }
    }

    override fun initialize() {
        getIntenData()
        userViewModel.liveUserListData.observe(this, Observer {
            it?.let { userList ->
                userListAdapter.setItems(userList)
            }
        })
        userViewModel.getAllUsers(members)
    }

    private fun getIntenData() {
        intent?.let {
            val hashMembers = it.getSerializableExtra(ChatRoomActivity.KEY_MEMBERS) as HashMap<*, *>
            hashMembers.forEach { member ->
                members.add(member.key.toString())
            }
            Logger.e(members.toString())
        }
    }
}