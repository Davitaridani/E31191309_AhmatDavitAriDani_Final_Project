package com.example.dianastore.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.dianastore.R
import com.example.dianastore.activity.authActivity.AuthViewModel
import com.example.dianastore.activity.authActivity.LoginActivity
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.InfoLogin
import com.example.dianastore.model.User
import com.example.dianastore.util.Constants
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog

class SplashActivity : AppCompatActivity() {

    lateinit var vm: AuthViewModel
    private lateinit var s: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        vm = ViewModelProvider(this).get(AuthViewModel::class.java)
        s = Prefs(this)

        observer()
        relogin()
    }

    private fun relogin() {
        if (s.getStatusLogin()) {
            val user = User()
            user.email = "user"
            user.password = "pass"
            val infoLogin = s.getObject(s.infoLogin, InfoLogin::class.java)
            if (infoLogin != null) {
                user.email = infoLogin.email
                user.name = infoLogin.email
                user.password = infoLogin.password
                if (infoLogin.type == Constants.GOOGLE) {
                    vm.loginGoogle(user)
                    return
                }
            }
            vm.loginEmail(user)
        } else {
            startActivityMainDelay()
        }
    }

    private fun observer() {
        vm.successResponse.observe(this, Observer {
            startActivityMainDelay()
        })

        vm.errorResponse.observe(this, Observer { pesan ->
            SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Oopss...")
                    .setContentText(pesan)
                    .setConfirmText("Login Ulang")
                    .setCancelText("Tutup")
                    .setConfirmClickListener {
                        startActivity(Intent(this, LoginActivity::class.java))
                        it.dismiss()
                    }
                    .setCancelClickListener {
                        finish()
                        it.dismiss()
                    }.show()
        })
    }

    private fun startActivityMainDelay() {
        Handler(Looper.myLooper()!!).postDelayed({
            val i = if (s.getStatusLogin()) MainActivity::class.java else LoginActivity::class.java
            startActivity(Intent(this, i))
            finish()
        }, 1500)
    }
}