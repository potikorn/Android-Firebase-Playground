package th.potikorn.firebaseplayground.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import th.potikorn.firebaseplayground.dao.GithubDetailDao
import th.potikorn.firebaseplayground.repository.GithubContract
import th.potikorn.firebaseplayground.repository.GithubRepository
import javax.inject.Inject

class GithubProjectViewModel @Inject constructor(private val githubRepository: GithubRepository) :
    ViewModel(), GithubContract.GithubProjectListener {

    val liveGithubProject = MutableLiveData<MutableList<GithubDetailDao>>()
    val liveIsLoading = MutableLiveData<Boolean>()
    val liveErrorData = MutableLiveData<String>()

    fun getProjectListByUserName(userName: String) {
        liveIsLoading.value = false
        githubRepository.getProjectList(userName, this)
    }

    override fun onLoadSuccess(data: MutableList<GithubDetailDao>?) {
        liveIsLoading.value = false
        liveGithubProject.value = data
    }

    override fun onObserveFailed(message: String?) {
        liveIsLoading.value = false
        liveErrorData.value = message
    }

    override fun onUnSuccess(message: String?) {
        liveIsLoading.value = false
        liveErrorData.value = message
    }
}