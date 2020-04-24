package com.panjikrisnayasa.caripura.view.guest


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.viewmodel.guest.AccountLoginViewModel
import kotlinx.android.synthetic.main.fragment_account_login.*
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class AccountLoginFragment : Fragment(), View.OnClickListener, TextWatcher {

    private lateinit var mAccountLoginViewModel: AccountLoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAccountLoginViewModel = AccountLoginViewModel(this.context)
        relative_account_login_sign_up_here.setOnClickListener(this)
        button_account_login.setOnClickListener(this)
        text_input_edit_text_account_login_password.addTextChangedListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.relative_account_login_sign_up_here -> {
                val intent = Intent(v.context, SignUpActivity::class.java)
                startActivity(intent)
            }
            R.id.button_account_login -> {
                var isNull = false
                var isEmailInvalid = false

                val email = text_input_edit_text_account_login_email.text.toString()
                val password = text_input_edit_text_account_login_password.text.toString()

                //email address validation
                val pattern = Pattern.compile(
                    "^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_.-])+\\.([a-zA-Z])+([a-zA-Z])+",
                    Pattern.CASE_INSENSITIVE
                )
                val matcher = pattern.matcher(email)

                if (!email.isNotBlank()) {
                    isNull = true
                    text_input_edit_text_account_login_email.error =
                        getString(R.string.error_message_fill_this_field)
                } else if (!matcher.matches()) {
                    isEmailInvalid = true
                    text_input_edit_text_account_login_email.error =
                        getString(R.string.error_message_invalid_email)
                }
                if (!password.isNotBlank()) {
                    isNull = true
                    text_input_layout_account_login_password.isPasswordVisibilityToggleEnabled =
                        false
                    text_input_edit_text_account_login_password.error =
                        getString(R.string.error_message_fill_this_field)
                }

                if (!isNull && !isEmailInvalid) {
                    mAccountLoginViewModel.authentication(email, password)
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        text_input_layout_account_login_password.isPasswordVisibilityToggleEnabled = true
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

}
