package th.potikorn.firebaseplayground.ui.base.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

@Suppress("LeakingThis")
abstract class BaseListAdapter<VH : RecyclerView.ViewHolder, A : BaseListAdapterContract.Adapter,
    P : BaseListAdapterContract.Presenter<A>> : RecyclerView.Adapter<VH>(),
    BaseListAdapterContract.Adapter {

    private var loading: Boolean = false

    private var onLoadMoreListener: OnLoadMoreListener? = null

    protected var presenter: P

    init {
        presenter = createPresenter()
        @Suppress("UNCHECKED_CAST")
        presenter.setAdapter(this as A)
    }

    abstract fun createPresenter(): P

    fun getItems(): MutableList<BaseItem> = presenter.getItems()
    fun getItem(pos: Int): BaseItem = presenter.getItem(pos)
    fun hasItems() = presenter.hasItems()
    fun setItems(items: List<BaseItem>?) = presenter.setItems(items)
    fun addItem(item: BaseItem) = presenter.addItem(item)
    fun addItemAllAt(items: List<BaseItem>?) = presenter.addItemAllAt(items)
    fun removeItem(index: Int) = presenter.removeItem(index)
    fun removeAllItems() = presenter.removeAllItems()
    open fun setOnLoadMoreListener(recyclerView: RecyclerView, onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = (recyclerView?.layoutManager as LinearLayoutManager).itemCount
                val lastVisibleItem =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loading && totalItemCount <= (lastVisibleItem + 3)) {
                    onLoadMoreListener.onLoadMore()
                    loading = true
                }
            }
        })
    }

    fun setLoaded() {
        loading = false
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
}