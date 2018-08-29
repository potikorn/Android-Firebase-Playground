package th.potikorn.firebaseplayground.ui.adapter.chatmsg.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_my_chat_message.view.*
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.extensions.getFriendlyTime

class OtherMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBindData(message: MessagesDao?) {
        itemView?.apply {
            tvMessage.text = message?.text
            tvPostDate.text = message?.post_date?.getFriendlyTime()
        }
    }
}