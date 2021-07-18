package com.example.dianastore.ui.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase

import com.example.dianastore.R
import com.example.dianastore.activity.DataDiriActivity
import com.example.dianastore.activity.RiwayatActivity
import com.example.dianastore.activity.authActivity.LoginActivity
import com.example.dianastore.helper.Prefs

/**
 * A simple [Fragment] subclass.
 */
class AkunFragment : Fragment() {

    lateinit var s: Prefs


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_akun, container, false)
        init(view)

        s = Prefs(requireActivity())

        setupLayout()
        mainButton()
        setData()
        return view
    }

    private fun setupLayout() {
    }

    private fun mainButton() {
        btnLogout.setOnClickListener {
            s.setStatusLogin(false)
            s.clear()
            val intent = Intent(activity, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        btnRiwayat.setOnClickListener {
            startActivity(Intent(requireActivity(), RiwayatActivity::class.java))
        }

        btnProfile.setOnClickListener {
            startActivity(Intent(requireActivity(), DataDiriActivity::class.java))
        }

        btnProfile1.setOnClickListener {
            startActivity(Intent(requireActivity(), DataDiriActivity::class.java))
        }
    }

    private fun setData() {

        if (s.getUser() != null) {
            val user = s.getUser()!!
            val array = user.name.split(" ")
            var inisial = array[0].substring(0, 1)
            if (array.size > 1) inisial += array[1].substring(0, 1)
            tvInisial.text = inisial
            tvName.text = user.name
        }

//        tvNama.text = user.name
//        tvEmail.text = user.email
//        tvPhone.text = user.phone
    }

    lateinit var btnLogout: LinearLayout
    lateinit var btnRiwayat: LinearLayout
    lateinit var btnManagement: LinearLayout
    lateinit var btnProfile1: LinearLayout
    lateinit var btnProfile: LinearLayout

    //    lateinit var tvNama: TextView
//    lateinit var tvEmail: TextView
//    lateinit var tvPhone: TextView
    lateinit var tvInisial: TextView
    lateinit var tvName: TextView

    fun init(view: View) {
        tvInisial = view.findViewById(R.id.tv_inisial)
        tvName = view.findViewById(R.id.tv_name)
        btnLogout = view.findViewById(R.id.btn_logout)
        btnRiwayat = view.findViewById(R.id.btn_riwayat)
        btnManagement = view.findViewById(R.id.btn_management)
        btnProfile = view.findViewById(R.id.btn_profile)
        btnProfile1 = view.findViewById(R.id.btn_profile1)
    }

}
