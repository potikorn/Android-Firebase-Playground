package th.potikorn.firebaseplayground.ui.adapter.chatlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_chat_list.view.*
import th.potikorn.firebaseplayground.dao.ChatListDao
import th.potikorn.firebaseplayground.base.BaseAdapterListener

class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBindData(
        chatList: ChatListDao?,
        callback: BaseAdapterListener?
    ) {
        itemView.apply {
            setOnClickListener { callback?.onClick(chatList, adapterPosition) }
            setOnLongClickListener {
                callback?.onLongClick(chatList, adapterPosition)
                return@setOnLongClickListener true
            }
            tvRoomName.text = chatList?.chatRoomName
        }
    }
}