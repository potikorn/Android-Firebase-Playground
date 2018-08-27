package th.potikorn.firebaseplayground.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import th.potikorn.firebaseplayground.dao.UserFireBaseDao

class UserRepository {

    private val mRealTimeDb: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }
    private val mFirebaseInstanceId: FirebaseInstanceId by lazy { FirebaseInstanceId.getInstance() }
    private val mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun requestGetAllUsers(
        members: MutableList<String>,
        onSuccess: ((userList: MutableList<UserFireBaseDao>) -> Unit),
        onFailure: ((errorMsg: String) -> Unit)
    ) {
        mRealTimeDb.getReference("playground-user")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(dataBaseError: DatabaseError) {
                    dataBaseError.toException().printStackTrace()
                    onFailure(dataBaseError.toException().message.toString())
                }

                override fun onDataChange(dataSnapShot: DataSnapshot) {
                    val userList = mutableListOf<UserFireBaseDao>()
                    dataSnapShot.children.filterNot {
                        members.contains(it.key)
                    }.forEach { filteredData ->
                        val dataObj = filteredData.getValue(UserFireBaseDao::class.java)
                        userList.add(
                            UserFireBaseDao(
                                uid = filteredData.key,
                                displayName = dataObj?.displayName,
                                email = dataObj?.email
                            )
                        )
                    }
                    onSuccess.invoke(userList)
                }
            })
    }

    fun requestSaveToken(
        onSuccess: (() -> Unit)? = null,
        onFailure: ((errorMsg: String) -> Unit)
    ) {
        mFirebaseInstanceId.instanceId
            .addOnSuccessListener { instanceId ->
                mRealTimeDb.getReference("playground-user")
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(dataBaseError: DatabaseError) {
                            dataBaseError.toException().printStackTrace()
                            onFailure(dataBaseError.toException().message.toString())
                        }

                        override fun onDataChange(dataSnapShot: DataSnapshot) {
                            dataSnapShot.children.forEach { it ->
                                if (it.key == mAuth.currentUser?.uid) {
                                    it.ref.updateChildren(mapOf("fcm_token" to instanceId.token))
                                }
                            }
                            onSuccess?.invoke()
                        }
                    })
            }
            .addOnFailureListener {
                it.printStackTrace()
                onFailure(it.message.toString())
            }
    }
}