package com.panjikrisnayasa.caripura.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.viewmodel.SignUpViewModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity(), View.OnClickListener, TextWatcher {

    private lateinit var mViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SignUpViewModel::class.java)

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
                val fullName = text_input_edit_text_sign_up_full_name.text.toString()
                val phoneNumber = text_input_edit_text_sign_up_phone_number.text.toString()
                val email = text_input_edit_text_sign_up_email.text.toString()
                val password = text_input_edit_text_sign_up_password.text.toString()

                signUp(fullName, phoneNumber, email, password)
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

    private fun signUp(fullName: String, phoneNumber: String, email: String, password: String) {
        var isNull = false
        var isPasswordUnsafe = false
        var isEmailInvalid = false

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
            hideKeyboard()
            showLoading(true)
            mViewModel.signUp(fullName, phoneNumber, email, password)
                .observe(this, Observer { code ->
                    when (code) {
                        1 -> {
                            Toast.makeText(
                                this,
                                getString(R.string.error_message_verification_sent),
                                Toast.LENGTH_LONG
                            ).show()
                            finish()
                        }
                        2 -> {
                            Toast.makeText(
                                this,
                                getString(R.string.error_message_verification_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        3 -> {
                            Toast.makeText(
                                this,
                                getString(R.string.error_message_sign_up_failed),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    showLoading(false)
                })
        }
    }

    private fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            view_sign_up_background.visibility = View.VISIBLE
            progress_sign_up.visibility = View.VISIBLE
        } else {
            view_sign_up_background.visibility = View.GONE
            progress_sign_up.visibility = View.GONE
        }
    }
}
