package th.potikorn.firebaseplayground.repository

import com.potikorn.firebaseplayground.base.BaseFetchApiListener
import th.potikorn.firebaseplayground.dao.GithubDetailDao

class GithubContract {

    interface GithubProjectListener : BaseFetchApiListener {
        fun onLoadSuccess(data: MutableList<GithubDetailDao>?)
        fun onObserveFailed(message: String?)
    }
}