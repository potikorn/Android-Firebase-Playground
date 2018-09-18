package th.potikorn.firebaseplayground.ui.adapter.chatmsg.viewholder

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v7.widget.RecyclerView
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_image_message.view.*
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.extensions.getFriendlyTime
import th.potikorn.firebaseplayground.extensions.loadImageView

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun onBindData(message: MessagesDao?){
        itemView?.apply {
            when {
                mAuth.currentUser?.uid != message?.user -> setMessageStyle(true)
                else -> setMessageStyle()
            }
            ivImageMessage.loadImageView(message?.imgPath ?: "")
            tvPostDate.text = message?.post_date?.getFriendlyTime()
        }
    }

    private fun setMessageStyle(otherView: Boolean = false) {
        when {
            otherView -> setConstrainLayout(true)
            else -> setConstrainLayout()
        }
    }

    private fun setConstrainLayout(otherView: Boolean = false) {
        if (otherView) {
            ConstraintSet().apply {
                clone(itemView as ConstraintLayout)
                clear(itemView.layoutMessage.id, ConstraintSet.END)
                connect(
                    itemView.layoutMessage.id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START
                )
                applyTo(itemView)
            }
        } else {
            ConstraintSet().apply {
                clone(itemView as ConstraintLayout)
                clear(itemView.layoutMessage.id, ConstraintSet.START)
                connect(
                    itemView.layoutMessage.id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END
                )
                applyTo(itemView)
            }
        }
    }
}