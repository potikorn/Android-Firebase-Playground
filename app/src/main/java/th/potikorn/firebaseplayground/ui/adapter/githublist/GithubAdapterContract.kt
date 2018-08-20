package th.potikorn.firebaseplayground.ui.adapter.githublist

import th.potikorn.firebaseplayground.ui.base.adapter.BaseListAdapterContract

class GithubAdapterContract {

    interface Adapter : BaseListAdapterContract.Adapter

    interface Presenter : BaseListAdapterContract.Presenter<Adapter>
}