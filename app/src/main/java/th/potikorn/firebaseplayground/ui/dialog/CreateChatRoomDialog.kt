package th.potikorn.firebaseplayground.ui.dialog

import android.os.Bundle
import kotlinx.android.synthetic.main.dialog_create_chat_room.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.base.BaseDialogFragment
import th.potikorn.firebaseplayground.extensions.showToast

class CreateChatRoomDialog : BaseDialogFragment() {

    private var onSubmitClickListener: ((chatRoomName: String) -> Unit)? = null

    override fun layoutToInflate(): Int = R.layout.dialog_create_chat_room

    override fun startView() {}

    override fun stopView() {}

    override fun initData(argument: Bundle?) {}

    override fun setupView() {
        btnCreateChatRoom.setOnClickListener {
            checkChatRoomName()
        }
    }

    override fun saveInstanceState(outState: Bundle) {}

    override fun restoreView(savedInstanceState: Bundle?) {}

    fun setOnSubmitListener(func: (chatRoomName: String) -> Unit) {
        onSubmitClickListener = func
    }

    private fun checkChatRoomName() {
        when {
            etChatRoomName.text.toString().isEmpty() -> tilChatRoomName.error =
                "Please fill your chat room name."
            etChatRoomName.text.toString().length > 20 -> tilChatRoomName.error =
                "Chat room name is too long."
            else -> {
                onSubmitClickListener?.invoke(etChatRoomName.text.toString())
                dismiss()
            }
        }
    }

    companion object {
        fun newInstance(bundle: Bundle? = null): CreateChatRoomDialog =
            CreateChatRoomDialog().apply {
                arguments = bundle
            }
    }
}