package com.panjikrisnayasa.caripura.view


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.panjikrisnayasa.caripura.R
import com.panjikrisnayasa.caripura.util.SharedPrefManager
import com.panjikrisnayasa.caripura.viewmodel.AccountLoginViewModel
import kotlinx.android.synthetic.main.fragment_account_login.*
import java.util.regex.Pattern

/**
 * A simple [Fragment] subclass.
 */
class AccountLoginFragment : Fragment(), View.OnClickListener, TextWatcher {

    private lateinit var mViewModel: AccountLoginViewModel
    private lateinit var mSharedPref: SharedPrefManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                AccountLoginViewModel::class.java
            )

        mSharedPref = SharedPrefManager.getInstance(context)
        checkLogin()

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
                showLoading(true)
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
                    hideKeyboard()
                    mViewModel.authenticate(email, password)
                        .observe(this, Observer { code ->
                            showLoading(false)
                            if (code != null) {
                                when (code) {
                                    1 -> {
                                        mViewModel.getCurrentUser().observe(this, Observer { user ->
                                            if (user != null) {
                                                mSharedPref.setLogin(user)
                                                replaceFragment(LoggedInFragment())
                                            }
                                        })
                                    }
                                    2 -> {
                                        Toast.makeText(
                                            context,
                                            getString(R.string.error_message_verify_email),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    3 -> {
                                        Toast.makeText(
                                            context,
                                            getString(R.string.error_message_email_password_incorrect),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        })
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

    private fun replaceFragment(fragment: Fragment) {
        fragmentManager?.beginTransaction()?.replace(
            R.id.frame_layout_main,
            fragment,
            fragment::class.java.simpleName
        )?.commit()
    }

    private fun checkLogin() {
        if (mSharedPref.isLoggedIn()) {
            replaceFragment(LoggedInFragment())
        }
    }

    private fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            view_account_login_background.visibility = View.VISIBLE
            progress_account_login.visibility = View.VISIBLE
        } else {
            view_account_login_background.visibility = View.GONE
            progress_account_login.visibility = View.GONE
        }
    }
}
