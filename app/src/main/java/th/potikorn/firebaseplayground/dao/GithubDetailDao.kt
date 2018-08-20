package th.potikorn.firebaseplayground.dao

import com.google.gson.annotations.SerializedName
import th.potikorn.firebaseplayground.ui.base.adapter.BaseItem
import th.potikorn.firebaseplayground.ui.base.adapter.ItemType

data class GithubDetailDao(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("full_name") var fullName: String? = null,
    @SerializedName("owner") var owner: OwnerDao? = null
) : BaseItem(ItemType.NORMAL)

data class OwnerDao(
    @SerializedName("login") var loginName: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("avatar_url") var avatarUrl: String? = null,
    @SerializedName("url") var profileUrl: String? = null
)
