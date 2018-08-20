package th.potikorn.firebaseplayground.ui.adapter.chatlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_chat_list.view.*
import th.potikorn.firebaseplayground.dao.ChatListDao

class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBindData(chatList: ChatListDao?) {
        itemView.apply {
            tvRoomName.text = chatList?.chatRoomName
        }
    }
}