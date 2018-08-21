package th.potikorn.firebaseplayground.base

abstract class BaseAdapterListener {
    open fun <DATA> onClick(data: DATA? = null, position: Int) {}
    open fun <DATA> onLongClick(data: DATA? = null, position: Int) {}
}