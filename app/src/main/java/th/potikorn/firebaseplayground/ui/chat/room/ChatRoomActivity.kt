package th.potikorn.firebaseplayground.ui.chat.room

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_chat_room.*
import th.potikorn.firebaseplayground.R
import th.potikorn.firebaseplayground.constants.PICK_IMAGE_REQUEST
import th.potikorn.firebaseplayground.dao.MessagesDao
import th.potikorn.firebaseplayground.di.AppComponent
import th.potikorn.firebaseplayground.extensions.getRealPath
import th.potikorn.firebaseplayground.extensions.navigate
import th.potikorn.firebaseplayground.extensions.showToast
import th.potikorn.firebaseplayground.ui.adapter.chatmsg.ChatMessagesAdapter
import th.potikorn.firebaseplayground.ui.base.BaseActivity
import th.potikorn.firebaseplayground.ui.user.invite.InviteActivity
import th.potikorn.firebaseplayground.ui.viewmodel.ChatViewModel
import th.potikorn.firebaseplayground.ui.viewmodel.UploadViewModel
import java.io.File
import java.io.FileNotFoundException
import java.util.Date

class ChatRoomActivity : BaseActivity() {

    private val chatViewModel: ChatViewModel by lazy { bindViewModel<ChatViewModel>() }
    private val chatMessageAdapter: ChatMessagesAdapter by lazy { ChatMessagesAdapter() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val uploadViewModel: UploadViewModel by lazy { bindViewModel<UploadViewModel>() }

    private var chatRoomName: String? = null

    override fun layoutToInflate(): Int = R.layout.activity_chat_room

    override fun doInjection(appComponent: AppComponent) = appComponent.inject(this)

    override fun startView() {}

    override fun stopView() {}

    override fun destroyView() {}

    override fun setupInstance() {}

    override fun setupView() {
        fabMembers.setOnClickListener {
            navigate<InviteActivity> {
                putExtra(KEY_CHAT_ROOM_NAME, intent.getStringExtra(KEY_CHAT_ROOM_NAME))
                putExtra(KEY_MEMBERS, intent.getSerializableExtra(KEY_MEMBERS))
            }
        }
        ivIconSend.apply {
            setOnClickListener {
                etSendMessage.text.toString().takeIf { txt ->
                    txt.isNotEmpty()
                }?.apply {
                    etSendMessage.setText("")
                    chatMessageAdapter.insertNewChatMessage(
                        MessagesDao(
                            text = this,
                            post_date = Date().time,
                            user = mAuth.currentUser?.uid
                        )
                    )
                    rvChatMessages.smoothScrollToPosition(chatMessageAdapter.itemCount.minus(1))
                    chatViewModel.sendMessage(this, chatRoomName)
                }
            }
        }
        ivIconPix.setOnClickListener {
            createSheetBottomDialog()
        }
        rvChatMessages.apply {
            layoutManager = LinearLayoutManager(this@ChatRoomActivity).also {
                it.stackFromEnd = true
            }
            adapter = chatMessageAdapter
        }
    }

    override fun initialize() {
        getIntentData()
        chatViewModel.liveChatMessages.observe(this, Observer { messages ->
            messages?.let {
                chatMessageAdapter.setItems(it)
                // FIXME should use DiffUtil and scroll to bottom instead?
                chatMessageAdapter.itemCount.takeIf { itemCount ->
                    itemCount != 0
                }.apply {
                    rvChatMessages.smoothScrollToPosition(this?.minus(1) ?: 0)
                }
            }
        })
        chatViewModel.getChatMessages(chatRoomName ?: "")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        resultCode.takeIf { it == Activity.RESULT_OK }.apply {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    getFileFromPickImage(data)
                }
            }
        }
    }

    private fun getFileFromPickImage(data: Intent?) {
        try {
            val imageUri = data?.data
            Logger.e(imageUri?.getRealPath(this@ChatRoomActivity).toString())
            uploadViewModel.uploadImage(
                chatRoomName ?: "",
                File(imageUri?.getRealPath(this@ChatRoomActivity))
            )
//            val imageStream = contentResolver.openInputStream(imageUri)
//            val selectedImage = BitmapFactory.decodeStream(imageStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Toast.makeText(this@ChatRoomActivity, "Something went wrong", Toast.LENGTH_LONG).show()
        }
    }

    private fun getIntentData() {
        intent?.let {
            chatRoomName = it.getStringExtra(KEY_CHAT_ROOM_NAME)
        }
    }

    private fun createSheetBottomDialog() {
        Dexter.withActivity(this)
            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    val intent = Intent().apply {
                        type = "image/*"
                        action = Intent.ACTION_GET_CONTENT
                    }
                    startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        PICK_IMAGE_REQUEST
                    )
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showToast(R.string.app_need_this_permission)
                }
            }).check()
    }

    companion object {
        const val KEY_CHAT_ROOM_NAME = "KEY_CHAT_ROOM_NAME"
        const val KEY_MEMBERS = "KEY_MEMBERS"
    }
}