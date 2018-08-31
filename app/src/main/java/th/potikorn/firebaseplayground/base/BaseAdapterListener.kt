package th.potikorn.firebaseplayground.base

interface BaseAdapterListener {
    fun <DATA> onClick(data: DATA? = null, position: Int) {}
    fun <DATA> onLongClick(data: DATA? = null, position: Int) {}
}