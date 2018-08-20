package th.potikorn.firebaseplayground.ui.base.adapter

class ItemType {
    companion object {
        const val PROGRESS = 1
        const val NORMAL = 2
        const val HEADER = 3
        const val FOOTER = 4
    }
}

open class BaseItem(val itemType: Int = ItemType.NORMAL)