package com.example.dianastore.activity.authActivity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.inyongtisto.myhelper.extension.isEmpty
import com.example.dianastore.activity.MainActivity
import com.example.dianastore.R
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.User
import com.example.dianastore.room.MyDatabase
import com.inyongtisto.myhelper.base.BaseActivity
import com.inyongtisto.myhelper.extension.showErrorDialog
import kotlinx.android.synthetic.main.activity_register_new.*

class RegisterActivity : BaseActivity() {

    lateinit var s: Prefs
    private lateinit var vm: AuthViewModel
    private lateinit var myDb: MyDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_new)
        init()

        mainButton()
        observer()
    }

    private fun init() {
        s = Prefs(this)
        vm = ViewModelProvider(this).get(AuthViewModel::class.java)
        myDb = MyDatabase.getInstance(this)!!
    }

    @SuppressLint("SetTextI18n")
    private fun dataDummy() {
        edt_name.setText("user")
        edt_email.setText("user@gmail.com")
        edt_phone.setText("08129839021")
        edt_password.setText("12345678")
    }


    private fun mainButton() {
        btn_signup.setOnClickListener {
            register()
        }

        btn_test.setOnLongClickListener {
            dataDummy()
            return@setOnLongClickListener true
        }
    }

    private fun register() {
        if (validate()) {
            val user = User()
            user.email = edt_email.text.toString()
            user.name = edt_name.text.toString()
            user.password = edt_password.text.toString()
            user.phone = edt_phone.text.toString()
            user.role = "USER"
            vm.register(user)
        }
    }

    private fun observer() {
        vm.successResponse.observe(this, Observer {
            val i = Intent(applicationContext, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        })

        vm.errorResponse.observe(this, Observer {
            showErrorDialog(it)
        })

        vm.isLoading.observe(this, Observer {
            if (it) progress.show() else progress.dismiss()
        })
    }

    private fun validate(): Boolean {
        return when {
            edt_name.isEmpty() -> false
            edt_email.isEmpty() -> false
            edt_phone.isEmpty() -> false
            edt_password.isEmpty() -> false
            else -> true
        }
    }

}