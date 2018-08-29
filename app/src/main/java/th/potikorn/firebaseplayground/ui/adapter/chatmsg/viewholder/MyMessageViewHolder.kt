package th.potikorn.firebaseplayground.ui.adapter.chatmsg.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_my_chat_message.view.*
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.extensions.getFriendlyTime

class MyMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBindData(message: MessagesDao?) {
        itemView?.apply {
            tvMessage.text = message?.text
            // FIXME need to interval time to feel real time message?
            tvPostDate.text = message?.post_date?.getFriendlyTime()
        }
    }
}