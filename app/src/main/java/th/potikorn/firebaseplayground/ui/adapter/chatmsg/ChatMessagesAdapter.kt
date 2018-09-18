package th.potikorn.firebaseplayground.ui.adapter.chatmsg

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.base.BaseAdapterListener
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.extensions.inflate
import th.potikorn.firebaseplayground.ui.adapter.chatmsg.viewholder.MessageViewHolder

class ChatMessagesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TEXT_TYPE = 0
    val IMAGE_TYPE = 1
    private var items: MutableList<MessagesDao>? = mutableListOf()
    private var itemActionsListener: BaseAdapterListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TEXT_TYPE -> MessageViewHolder(parent.inflate(R.layout.item_my_chat_message))
            else -> MessageViewHolder(parent.inflate(R.layout.item_my_chat_message))
        }

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> holder.onBindData(items?.get(position))
        }
    }

//    override fun getItemViewType(position: Int): Int {
//        return when {
//            items?.get(position)?.user == mAuth.currentUser?.uid -> {
//                MY_MESSAGE_TYPE
//            }
//            else -> OTHER_MESSAGE_TYPE
//        }
//    }

    fun setItems(randomList: MutableList<MessagesDao>) {
        items = randomList
        notifyDataSetChanged()
    }

    fun insertNewChatMessage(message: MessagesDao) {
        items?.add(message)
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