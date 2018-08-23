package th.potikorn.firebaseplayground.ui.adapter.chatmsg.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_my_chat_message.view.*
import th.potikorn.firebaseplayground.dao.MessagesDao
import java.util.Date

class MyMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBindData(message: MessagesDao?) {
        itemView?.apply {
            tvMessage.text = message?.text
            tvPostDate.text = Date(message?.post_date ?: 0).toString()
        }
    }
}