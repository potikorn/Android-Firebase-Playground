package th.potikorn.firebaseplayground.ui.adapter.chatmsg

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.base.BaseAdapterListener
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.extensions.inflate
import th.potikorn.firebaseplayground.ui.adapter.chatmsg.viewholder.MyMessageViewHolder

class ChatMessagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<MessagesDao>? = mutableListOf()
    private var itemActionsListener: BaseAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyMessageViewHolder(parent.inflate(R.layout.item_my_chat_message))

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MyMessageViewHolder -> holder.onBindData(items?.get(position))
        }
    }

    fun setItems(randomList: MutableList<MessagesDao>) {
        items = randomList
        notifyDataSetChanged()
    }

    fun insertNewChatMessage(message: MessagesDao) {
        items?.add(message)
        Logger.e(items.toString())
        notifyItemInserted(items?.size?.plus(1) ?: 0)
    }

    fun setSimpleListener(callback: BaseAdapterListener) {
        itemActionsListener = callback
    }

    fun deleteItem(position: Int) {
        items?.removeAt(position)
        notifyItemRemoved(position)
    }
}