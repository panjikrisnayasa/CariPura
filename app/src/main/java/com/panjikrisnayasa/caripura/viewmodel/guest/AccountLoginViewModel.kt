package com.panjikrisnayasa.caripura.viewmodel.guest

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.User

class AccountLoginViewModel : ViewModel() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDatabaseReference: DatabaseReference
    private val mUser = MutableLiveData<User>()

    fun authenticate(
        email: String,
        password: String,
        context: Context?
    ): LiveData<User> {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            val cUser = mAuth.currentUser
            if (it.isSuccessful) {
                if (cUser != null) {
                    val cUid = cUser.uid
                    mDatabaseReference =
                        FirebaseDatabase.getInstance().getReference("users").child(cUid)
                    mDatabaseReference.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            p0.message
                        }

                        override fun onDataChange(p0: DataSnapshot) {
                            if (p0.exists()) {
                                val tUser = p0.getValue(User::class.java)
                                if (tUser?.role == "contributor") {
                                    if (!cUser.isEmailVerified) {
                                        Toast.makeText(
                                            context,
                                            context?.getString(R.string.error_message_verify_email),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        mAuth.signOut()
                                        mUser.postValue(null)
                                        return
                                    }
                                }
                                mUser.postValue(tUser)
                            }
                        }
                    })
                }
            } else {
                Toast.makeText(
                    context,
                    context?.getString(R.string.error_message_email_password_incorrect),
                    Toast.LENGTH_SHORT
                ).show()
                mUser.postValue(null)
            }
        }
        return mUser
    }
}