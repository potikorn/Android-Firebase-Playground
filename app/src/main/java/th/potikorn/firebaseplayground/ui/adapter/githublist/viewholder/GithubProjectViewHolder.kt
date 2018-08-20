package th.potikorn.firebaseplayground.ui.adapter.githublist.viewholder

import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.item_project_list.*
import th.potikorn.firebaseplayground.dao.GithubDetailDao
import th.potikorn.firebaseplayground.ui.base.adapter.BaseViewHolder
import th.potikorn.firebaseplayground.ui.demo.detail.DetailActivity
import th.potikorn.firebaseplayground.ui.demo.detail.DetailActivity.Companion.EXTRA_REPOS_NAME

class GithubProjectViewHolder(itemView: View) : BaseViewHolder(itemView) {
    fun onBindData(github: GithubDetailDao?) {
        tvProjectId.text = github?.id.toString()
        tvProjectName.text = github?.name
        itemView.setOnClickListener {
            itemView.context.startActivity(
                Intent(itemView.context, DetailActivity::class.java)
                    .putExtra(EXTRA_REPOS_NAME, github?.name)
            )
        }
    }
}