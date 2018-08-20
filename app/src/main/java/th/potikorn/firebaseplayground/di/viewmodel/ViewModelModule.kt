package th.potikorn.firebaseplayground.di.viewmodel

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import th.potikorn.firebaseplayground.ui.viewmodel.GithubProjectViewModel

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(GithubProjectViewModel::class)
    abstract fun bindsGithubViewModel(githubProjectViewModel: GithubProjectViewModel): ViewModel
}