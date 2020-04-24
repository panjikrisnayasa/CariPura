package com.panjikrisnayasa.caripura.viewmodel.guest

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.panjikrisnayasa.caripura.R

class AccountLoginViewModel(private var mContext: Context?) : ViewModel() {

    private lateinit var mAuth: FirebaseAuth

    internal fun authentication(email: String, password: String) {
        mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            val cUser = mAuth.currentUser
            if (it.isSuccessful) {
//                if (!cUser?.isEmailVerified!!) {
//                    Toast.makeText(
//                        mContext,
//                        mContext?.getString(R.string.error_message_verify_email),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    mAuth.signOut()
//                } else {
                Toast.makeText(
                    mContext,
                    "Login success",
                    Toast.LENGTH_SHORT
                ).show()
//                }
            } else {
                Toast.makeText(
                    mContext,
                    mContext?.getString(R.string.error_message_email_password_incorrect),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}