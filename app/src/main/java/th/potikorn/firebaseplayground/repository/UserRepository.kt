package th.potikorn.firebaseplayground.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.orhanobut.logger.Logger
import th.potikorn.firebaseplayground.dao.UserFireBaseDao

class UserRepository {

    private val mRealTimeDb: FirebaseDatabase by lazy { FirebaseDatabase.getInstance() }

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
}