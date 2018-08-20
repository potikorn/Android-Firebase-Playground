package th.potikorn.firebaseplayground.ui.base.adapter

import java.lang.ref.WeakReference

abstract class BaseListAdapterPresenter<A : BaseListAdapterContract.Adapter> :
    BaseListAdapterContract.Presenter<A> {

    private var adapter: WeakReference<A>? = null
    private var items: MutableList<BaseItem> = mutableListOf()

    override fun setAdapter(adapter: A) {
        this.adapter = WeakReference(adapter)
    }

    override fun getAdapter(): A? = adapter?.get()

    override fun getItemViewType(pos: Int): Int = items[pos].itemType

    override fun getItemCount(): Int = items.size

    override fun hasItems(): Boolean = !items.isEmpty()

    override fun getItems(): MutableList<BaseItem> = items

    override fun getItem(pos: Int): BaseItem = items[pos]

    override fun setItems(items: List<BaseItem>?) {
        this.items = items?.toMutableList() ?: mutableListOf()
        adapter?.get()?.notifyDataSetChanged()
    }

    override fun addItem(item: BaseItem) {
        items.add(item)
        adapter?.get()?.notifyItemInserted(getItemCount() - 1)
    }

    override fun addItemAllAt(items: List<BaseItem>?) {
        this.items.addAll(items?.size?.minus(1) ?: 0, items?.toMutableList() ?: mutableListOf())
        adapter?.get()?.notifyDataSetChanged()
    }

    override fun removeItem(index: Int) {
        items.removeAt(index)
        adapter?.get()?.notifyItemRemoved(index)
    }

    override fun removeAllItems() {
        items.clear()
        adapter?.get()?.notifyDataSetChanged()
    }
}