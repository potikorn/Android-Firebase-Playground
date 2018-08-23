package th.potikorn.firebaseplayground.ui.adapter.userlist.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_user_inviting.view.*
import th.potikorn.firebaseplayground.dao.UserFireBaseDao

class UserListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun onBindData(userFireBaseDao: UserFireBaseDao?) {
        itemView?.apply {
            tvUserName.text = userFireBaseDao?.displayName
        }
    }
}