package th.potikorn.firebaseplayground.di

import dagger.Component
import th.potikorn.firebaseplayground.di.viewmodel.ViewModelInjectionModule
import th.potikorn.firebaseplayground.di.viewmodel.ViewModelModule
import th.potikorn.firebaseplayground.ui.auth.login.LoginActivity
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.chat.ChatListActivity
import th.potikorn.firebaseplayground.ui.chat.room.ChatRoomActivity
import th.potikorn.firebaseplayground.ui.demo.DemoActivity
import th.potikorn.firebaseplayground.ui.demo.detail.DetailActivity
import th.potikorn.firebaseplayground.ui.demo.fragment.DemoListFragment
import th.potikorn.firebaseplayground.ui.user.invite.InviteActivity
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidModule::class, RetrofitModule::class, RemoteModule::class,
        ViewModelInjectionModule::class, ViewModelModule::class]
)

interface AppComponent {
    fun inject(baseActivity: BaseActivity)
    fun inject(demoActivity: DemoActivity)
    fun inject(demoListFragment: DemoListFragment)
    fun inject(detailActivity: DetailActivity)
    fun inject(chatListActivity: ChatListActivity)
    fun inject(chatRoomActivity: ChatRoomActivity)
    fun inject(inviteActivity: InviteActivity)
    fun inject(loginActivity: LoginActivity)
}