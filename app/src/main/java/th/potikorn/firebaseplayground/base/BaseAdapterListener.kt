package th.potikorn.firebaseplayground.base

abstract class BaseAdapterListener {
    open fun <DATA> onClick(data: DATA) {}
    open fun <DATA> onLongClick(data: DATA) {}
}