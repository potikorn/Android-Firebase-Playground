package th.potikorn.firebaseplayground.ui.base.adapter

class BaseListAdapterContract {
    interface Adapter {
        fun notifyDataSetChanged()
        fun notifyItemInserted(index: Int)
        fun notifyItemRemoved(index: Int)
    }

    interface Presenter<A : Adapter> {
        fun setAdapter(adapter: A)
        fun getAdapter(): A?
        fun getItemViewType(pos: Int): Int
        fun getItemCount(): Int
        fun hasItems(): Boolean
        fun getItems(): MutableList<BaseItem>
        fun getItem(pos: Int): BaseItem
        fun setItems(items: List<BaseItem>?)
        fun addItem(item: BaseItem)
        fun addItemAllAt(items: List<BaseItem>?)
        fun removeItem(index: Int)
        fun removeAllItems()
    }
}