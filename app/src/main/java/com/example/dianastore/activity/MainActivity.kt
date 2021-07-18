package com.example.dianastore.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
//import com.google.firebase.messaging.FirebaseMessaging
import com.example.dianastore.R
import com.example.dianastore.activity.authActivity.LoginActivity
import com.example.dianastore.ui.fragment.AkunFragment
import com.example.dianastore.ui.fragment.HomeFragment
import com.example.dianastore.ui.fragment.KeranjangFragment
import com.example.dianastore.ui.fragment.RiwayatFragment
import com.example.dianastore.helper.Helper
import com.example.dianastore.helper.Prefs
import com.inyongtisto.myhelper.extension.logs
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private val fragmentHome: Fragment = HomeFragment()
    private val fragmentKeranjang: Fragment = KeranjangFragment()
    private var fragmentRiwayat: Fragment = RiwayatFragment()
    private var fragmentAkun: Fragment = AkunFragment()
    private val fm: FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView

    private var statusLogin = false

    private lateinit var s: Prefs

    private var dariDetail: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Helper().blackStatusBar(this)

        s = Prefs(this)

        setUpBottomNav()

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessage, IntentFilter("event:keranjang"))
    }

    val mMessage: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            dariDetail = true
            callFargment(2, fragmentKeranjang)
        }
    }

    private fun setUpBottomNav() {
        fm.beginTransaction().add(R.id.container, fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container, fragmentKeranjang).hide(fragmentKeranjang).commit()
        fm.beginTransaction().add(R.id.container, fragmentRiwayat).hide(fragmentRiwayat).commit()
        fm.beginTransaction().add(R.id.container, fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    callFargment(0, fragmentHome)
                }
                R.id.navigation_riwayat -> {
                    callFargment(1, fragmentRiwayat)
                }
                R.id.navigation_keranjang -> {
                    callFargment(2, fragmentKeranjang)
                }
                R.id.navigation_akun -> {
                    if (s.getStatusLogin()) {
                        callFargment(3, fragmentAkun)
                    } else {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
            }

            false
        }
    }

    fun callFargment(int: Int, fragment: Fragment) {
        try {
            menuItem = menu.getItem(int)
            menuItem.isChecked = true
            fm.beginTransaction().hide(active).show(fragment).commit()
            active = fragment
        } catch (e: Exception) {
            logs("cek ini:" + e.message)
        }
    }

    override fun onResume() {
        if (dariDetail) {
            dariDetail = false
            callFargment(2, fragmentKeranjang)
        }
        super.onResume()
    }
}
