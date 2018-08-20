package th.potikorn.firebaseplayground.repository

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import th.potikorn.firebaseplayground.base.BaseSubscriber
import th.potikorn.firebaseplayground.dao.GithubDetailDao
import th.potikorn.firebaseplayground.remote.RemoteGithubDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubRepository @Inject constructor(private val remoteGithubDataSource: RemoteGithubDataSource) {

    fun getProjectList(
        userName: String,
        callback: GithubContract.GithubProjectListener
    ) {
        remoteGithubDataSource.requestGithubRepositories(userName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(BaseSubscriber(object :
                BaseSubscriber.SubscribeCallback<MutableList<GithubDetailDao>> {
                override fun onSuccess(body: MutableList<GithubDetailDao>?) =
                    callback.onLoadSuccess(body)

                override fun onUnSuccess(message: String?) = callback.onUnSuccess(message)
                override fun onObservableError(message: String?) = callback.onObserveFailed(message)
                override fun onUnAuthorized() = callback.onUnSuccess("on Unauthorized")
            }))
    }
}