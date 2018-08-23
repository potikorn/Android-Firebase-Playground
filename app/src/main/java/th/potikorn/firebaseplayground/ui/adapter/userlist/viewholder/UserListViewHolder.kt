package th.potikorn.firebaseplayground.ui.adapter.userlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_user_inviting.view.*
import th.potikorn.firebaseplayground.base.BaseAdapterListener
import th.potikorn.firebaseplayground.dao.UserFireBaseDao

class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBindData(
        userFireBaseDao: UserFireBaseDao?,
        itemActionsListener: BaseAdapterListener?
    ) {
        itemView?.apply {
            setOnClickListener {
                when (containerUser.isSelected) {
                    true -> {
                        containerUser.isSelected = false
                        cbUserSelect.isChecked = false
                        itemActionsListener?.onClick(userFireBaseDao, position = adapterPosition)
                    }
                    false -> {
                        containerUser.isSelected = true
                        cbUserSelect.isChecked = true
                        itemActionsListener?.onClick(userFireBaseDao, position = adapterPosition)
                    }
                }
            }
            tvUserName.text = userFireBaseDao?.email
        }
    }
}