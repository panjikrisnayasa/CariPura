package com.panjikrisnayasa.caripura.view.guest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.viewmodel.guest.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var mSignUpViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mSignUpViewModel = SignUpViewModel(this)

        text_input_edit_text_sign_up_password.addTextChangedListener(this)
        relative_sign_up_login_here.setOnClickListener(this)
        button_sign_up.setOnClickListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.relative_sign_up_login_here -> finish()
            R.id.button_sign_up -> {
                var isNull = false
                var isPasswordUnsafe = false
                var isEmailInvalid = false

                val fullName = text_input_edit_text_sign_up_full_name.text.toString()
                val phoneNumber = text_input_edit_text_sign_up_phone_number.text.toString()
                val email = text_input_edit_text_sign_up_email.text.toString()
                val password = text_input_edit_text_sign_up_password.text.toString()

                //email address validation
                val pattern = Pattern.compile(
                    "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+",
                    Pattern.CASE_INSENSITIVE
                )
                val matcher = pattern.matcher(email)

                if (!fullName.isNotBlank()) {
                    isNull = true
                    text_input_edit_text_sign_up_full_name.error =
                        getString(R.string.error_message_fill_this_field)
                }

                if (!phoneNumber.isNotBlank()) {
                    isNull = true
                    text_input_edit_text_sign_up_phone_number.error =
                        getString(R.string.error_message_fill_this_field)
                }

                if (!email.isNotBlank()) {
                    isNull = true
                    text_input_edit_text_sign_up_email.error =
                        getString(R.string.error_message_fill_this_field)
                } else if (!matcher.matches()) {
                    isEmailInvalid = true
                    text_input_edit_text_sign_up_email.error =
                        getString(R.string.error_message_invalid_email)
                }

                if (!password.isNotBlank()) {
                    isNull = true
                    text_input_layout_sign_up_password.isPasswordVisibilityToggleEnabled = false
                    text_input_edit_text_sign_up_password.error =
                        getString(R.string.error_message_fill_this_field)
                } else if (password.length < 6) {
                    isPasswordUnsafe = true
                    text_input_layout_sign_up_password.isPasswordVisibilityToggleEnabled = false
                    text_input_edit_text_sign_up_password.error =
                        getString(R.string.error_message_password_length)
                }

                if (!isNull && !isPasswordUnsafe && !isEmailInvalid) {
                    mSignUpViewModel.signUp(fullName, phoneNumber, email, password)
                    finish()
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        text_input_layout_sign_up_password.isPasswordVisibilityToggleEnabled = true
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}
