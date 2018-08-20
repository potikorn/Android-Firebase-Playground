package th.potikorn.firebaseplayground.ui.adapter.chatlist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.extensions.inflate
import th.potikorn.firebaseplayground.ui.adapter.chatlist.viewholder.ChatRoomViewHolder

class ChatListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<ChatListDao>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ChatRoomViewHolder(parent.inflate(R.layout.item_chat_list))

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatRoomViewHolder -> holder.onBindData(items?.get(position))
        }
    }

    fun setItems(randomList: MutableList<ChatListDao>) {
        items = randomList
        notifyDataSetChanged()
    }

    fun insertNewChatRoom(chatList: ChatListDao) {
        items?.add(0, chatList)
        notifyDataSetChanged()
    }
}