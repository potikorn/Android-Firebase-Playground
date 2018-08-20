package th.potikorn.firebaseplayground.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import th.potikorn.firebaseplayground.remote.GithubService
import th.potikorn.firebaseplayground.repository.ChatRepository
import javax.inject.Singleton

@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideRemoteGithubService(retrofit: Retrofit): GithubService =
        retrofit.create(GithubService::class.java)

    @Provides
    @Singleton
    fun provideChatRepository() = ChatRepository()
}