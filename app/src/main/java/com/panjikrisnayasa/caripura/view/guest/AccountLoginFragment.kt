package com.panjikrisnayasa.caripura.view.guest


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.panjikrisnayasa.caripura.R
import kotlinx.android.synthetic.main.fragment_account_login.*

/**
 * A simple [Fragment] subclass.
 */
class AccountLoginFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        relative_account_login_sign_up_here.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.relative_account_login_sign_up_here -> {
                val intent = Intent(v.context, SignUpActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
