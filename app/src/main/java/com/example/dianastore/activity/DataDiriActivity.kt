package com.example.dianastore.activity

import android.os.Bundle
import com.example.dianastore.R
import com.example.dianastore.helper.Helper
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.User
import com.inyongtisto.myhelper.base.BaseActivity
import com.inyongtisto.myhelper.extension.setToolbar
import kotlinx.android.synthetic.main.activity_data_diri.*
import kotlinx.android.synthetic.main.activity_data_diri.tv_inisial
import kotlinx.android.synthetic.main.activity_data_diri.tv_name
import kotlinx.android.synthetic.main.toolbar.*

class DataDiriActivity : BaseActivity() {

    private lateinit var s: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data_diri)
        setToolbar(toolbar, "Data Diri")
        Helper().blackStatusBar(this)
        init()


        mainButton()
        observer()
        load()
        setupData()
    }

    private fun init() {
        s = Prefs(this)
        // setup layout
    }

    var user = User()
    private fun setupData() {
        user = s.getUser() ?: return
        val array = user.name.split(" ")
        var inisial = array[0].substring(0, 1)
        if (array.size > 1) inisial += array[1].substring(0, 1)

        tv_inisial.text = inisial
        tv_name.text = user.name
        tv_email.text = user.email
        tv_nomor.text = if (user.phone.isNotEmpty()) user.phone else "-"
    }

    private fun mainButton() {
        btn_ubahNama.setOnClickListener {
            onEditInfo(Info("Nama", user.name))
        }

        btn_ubahEmail.setOnClickListener {
            onEditInfo(Info("Email", user.email))
        }
        btn_ubahNomor.setOnClickListener {
            onEditInfo(Info("Nomor", user.phone))
        }

        btn_audio.setOnClickListener {
//            picImage()
        }

//        btn_ubahPassword.setOnClickListener {
//            startActivity(Intent(this, GantiPasswordActivity::class.java))
//        }
    }


    class Info(var type: String, var data: String = "")

    private fun onEditInfo(data: Info) {
//        val dialog = EditDataBottomSheet(data, this)
//        dialog.onSaved = {
//            val user = ModelUser()
//            user.id = this.user.id
//            when (data.type) {
//                "Nama" -> user.name = it
//                "Nim" -> user.nim = it
//                "Email" -> user.email = it
//                "Kelas" -> user.kelas_id = it
//                "Nomor" -> user.phone = it
//            }
//            vm.update(user)
    }
//        dialog.show(supportFragmentManager, "EditDataBottomSheet")


    private fun load() {

    }

    private fun observer() {
//        vm.isLoading.observe(this, {
//            if (it) progress.show() else progress.dismiss()
//        })
//
//        vm.successResponse.observe(this, {
//            setupData()
//            Toasti.success(this, "Data berhasil di ubah")
//        })
//
//        vm.errorResponse.observe(this, {
//            showErrorDialog(it)
//        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}