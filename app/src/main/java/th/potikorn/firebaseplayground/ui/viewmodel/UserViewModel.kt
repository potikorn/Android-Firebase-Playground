package th.potikorn.firebaseplayground.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.dao.UserFireBaseDao
import th.potikorn.firebaseplayground.repository.UserRepository
import javax.inject.Inject

class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val liveUserListData = MutableLiveData<MutableList<UserFireBaseDao>>()

    fun getAllUsers(members: MutableList<String>) {
        userRepository.requestGetAllUsers(members,
            {
                liveUserListData.value = it
            }, {
                Logger.e(it)
            })
    }
}