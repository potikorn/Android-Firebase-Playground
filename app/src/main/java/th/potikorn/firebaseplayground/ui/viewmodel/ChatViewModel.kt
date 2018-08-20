package th.potikorn.firebaseplayground.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.repository.ChatRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    val liveChatListData = MutableLiveData<MutableList<ChatListDao>>()
    val liveErrorData = MutableLiveData<String>()

    fun getMyChatList(uid: String?) {
        chatRepository.requestMyChatList(uid ?: "" ,
            {
            liveChatListData.value = it
        }, {
            liveErrorData.value = it
        })
    }
}