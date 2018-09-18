package th.potikorn.firebaseplayground.ui.adapter.chatmsg.viewholder

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.item_my_chat_message.view.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.extensions.getFriendlyTime

class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun onBindData(message: MessagesDao?) {
        itemView?.apply {
            if (mAuth.currentUser?.uid != message?.user) {
                setMessageStyle(true)
            } else {
                setMessageStyle()
            }
            tvMessage.text = message?.text
            // FIXME need to interval time to feel real time message?
            tvPostDate.text = message?.post_date?.getFriendlyTime()
        }
    }

    private fun setMessageStyle(otherView: Boolean = false) {
        if (otherView) {
            setConstrainLayout(true)
            changeColorState(true)
        } else {
            setConstrainLayout()
            changeColorState()
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

    private fun changeColorState(otherView: Boolean = false) {
        var gravity = Gravity.END
        var backgroundColor = R.drawable.background_my_message
        var textColor = android.R.color.white
        if (otherView) {
            gravity = Gravity.START
            backgroundColor = R.drawable.background_other_message
            textColor = android.R.color.black
        }
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.gravity = gravity
        itemView.tvMessage.apply {
            setTextColor(ContextCompat.getColor(itemView.context, textColor))
            background =
                ContextCompat.getDrawable(itemView.context, backgroundColor)
            layoutParams = params
        }
        itemView.tvPostDate.layoutParams = params
    }
}