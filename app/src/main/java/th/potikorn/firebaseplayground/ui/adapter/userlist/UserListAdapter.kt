package th.potikorn.firebaseplayground.ui.adapter.userlist

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.UserFireBaseDao
import th.potikorn.firebaseplayground.extensions.inflate
import th.potikorn.firebaseplayground.ui.adapter.userlist.viewholder.UserListViewHolder

class UserListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: MutableList<UserFireBaseDao>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        UserListViewHolder(parent.inflate(R.layout.item_user_inviting))

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as UserListViewHolder).onBindData(items?.get(position))

    fun setItems(userList: MutableList<UserFireBaseDao>) {
        items = userList
        notifyDataSetChanged()
    }
}