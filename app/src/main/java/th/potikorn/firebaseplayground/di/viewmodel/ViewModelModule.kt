package th.potikorn.firebaseplayground.di.viewmodel

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import th.potikorn.firebaseplayground.ui.viewmodel.ChatViewModel
import th.potikorn.firebaseplayground.ui.viewmodel.GithubProjectViewModel
import th.potikorn.firebaseplayground.ui.viewmodel.UserViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GithubProjectViewModel::class)
    abstract fun bindsGithubViewModel(githubProjectViewModel: GithubProjectViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChatViewModel::class)
    abstract fun bindsChatViewModel(chatViewModel: ChatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel::class)
    abstract fun bindsUserViewModel(userViewModel: UserViewModel): ViewModel
}