package com.panjikrisnayasa.caripura.viewmodel.guest

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.model.User

class SignUpViewModel(private var mContext: Context) : ViewModel() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUserDatabaseReference: DatabaseReference

    internal fun signUp(fullName: String, phoneNumber: String, email: String, password: String) {
        Log.d("hyperLoop", "signUp view model")
        mAuth = FirebaseAuth.getInstance()
        mUserDatabaseReference = FirebaseDatabase.getInstance().getReference("users")

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val cUser = mAuth.currentUser
                if (cUser != null) {
                    val user = User(cUser.uid, fullName, phoneNumber, email, "contributor")
                    mUserDatabaseReference.child(cUser.uid).setValue(user)

                    cUser.sendEmailVerification().addOnCompleteListener { that ->
                        if (that.isSuccessful) {
                            Toast.makeText(
                                mContext,
                                mContext.getString(R.string.error_message_verification_sent),
                                Toast.LENGTH_LONG
                            ).show()
                            Log.d("hyperLoop", "verification email sent")
                            mAuth.signOut()
                        } else {
                            Toast.makeText(
                                mContext,
                                mContext.getString(R.string.error_message_verification_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("hyperLoop", "failed to send verification email")
                        }
                    }
                } else {
                    Toast.makeText(
                        mContext,
                        mContext.getString(R.string.error_message_sign_up_failed),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("hyperLoop", "sign up failed")
                }
            }
        }
    }
}