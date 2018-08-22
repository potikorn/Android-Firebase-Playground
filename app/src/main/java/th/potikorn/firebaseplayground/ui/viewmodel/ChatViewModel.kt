package th.potikorn.firebaseplayground.ui.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.repository.ChatRepository
import javax.inject.Inject

class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    val liveChatListData = MutableLiveData<MutableList<ChatListDao>>()
    val liveErrorData = MutableLiveData<String>()
    val liveMessageData = MutableLiveData<String>()
    val liveLoadingState = MutableLiveData<Boolean>()
    val liveRefreshState = MutableLiveData<Boolean>()
    val liveChatMessages = MutableLiveData<MutableList<MessagesDao>>()

    fun getMyChatList(isRefresh: Boolean = false) {
        loadOrRefresh(isRefresh, true)
        chatRepository.requestGetChatList(
            {
                loadOrRefresh(isRefresh, false)
                liveChatListData.value = it
            }, {
                loadOrRefresh(isRefresh, false)
                liveErrorData.value = it
            })
    }

    fun createChatRoom(chatRoomName: String) {
        liveLoadingState.value = true
        chatRepository.requestCreateChatRoom(chatRoomName,
            {
                liveLoadingState.value = false
            }, {
                liveLoadingState.value = false
                liveErrorData.value = it
            })
    }

    fun deleteChatRoom(chatRoomName: String) {
        chatRepository.requestRemoveChatRoom(chatRoomName,
            {
                liveMessageData.value = "Delete Successful"
            }, {
                liveErrorData.value = it
            })
    }

    fun getChatMessages(chatRoomName: String) {
        chatRepository.requestGetChatMessages(chatRoomName,
            {
                liveChatMessages.value = it
            }, {
                liveErrorData.value = it
            })
    }

    private fun loadOrRefresh(isRefresh: Boolean, state: Boolean) {
        when (isRefresh) {
            true -> liveRefreshState.value = state
            false -> liveLoadingState.value = state
        }
    }
}