package th.potikorn.firebaseplayground.ui.adapter.githublist

import android.view.ViewGroup
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.GithubDetailDao
import th.potikorn.firebaseplayground.extensions.inflate
import th.potikorn.firebaseplayground.ui.adapter.githublist.viewholder.GithubProjectViewHolder
import th.potikorn.firebaseplayground.ui.base.adapter.BaseListAdapter
import th.potikorn.firebaseplayground.ui.base.adapter.ItemType

class GithubAdapter : BaseListAdapter<GithubProjectViewHolder,
    GithubAdapterContract.Adapter,
    GithubAdapterContract.Presenter>(),
    GithubAdapterContract.Adapter {

    override fun createPresenter(): GithubAdapterContract.Presenter =
        GithubAdapterPresenter.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubProjectViewHolder {
        return GithubProjectViewHolder(
            parent.inflate(R.layout.item_project_list)
        )
    }

    override fun getItemCount(): Int = presenter.getItemCount()

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: GithubProjectViewHolder, position: Int) {
        val items = presenter.getItems() as? List<GithubDetailDao>
        holder.onBindData(items?.get(position))
    }

    override fun getItemViewType(position: Int): Int = ItemType.NORMAL
}