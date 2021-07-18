package com.example.dianastore.activity.authActivity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
//import com.google.android.gms.auth.api.signin.GoogleSignIn
//import com.google.android.gms.auth.api.signin.GoogleSignInClient
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions
//import com.google.android.gms.common.api.ApiException
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
import com.example.dianastore.activity.MainActivity
import com.example.dianastore.R
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.User
import com.inyongtisto.myhelper.base.BaseActivity
import com.inyongtisto.myhelper.extension.logs
import com.inyongtisto.myhelper.extension.showErrorDialog
import kotlinx.android.synthetic.main.activity_login_new.*

class LoginActivity : BaseActivity() {

    private lateinit var vm: AuthViewModel
    private lateinit var s: Prefs

//    private lateinit var auth: FirebaseAuth
//    private lateinit var googleSignInClient: GoogleSignInClient

    private var typeLogin = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_new)

        init()
        setupGoogle()
        mainButton()
        observer()
        signOut()
    }

    private fun init() {
        s = Prefs(this)
        vm = ViewModelProvider(this).get(AuthViewModel::class.java)
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

    private fun mainButton() {
        btn_login.setOnClickListener {
            if (validate()) {
                val user = User()
                user.email = edt_email.text.toString()
                user.password = edt_password.text.toString()
                vm.loginEmail(user)
            }
        }

        btn_daftar.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btn_google.setOnClickListener {
//            val signInIntent = googleSignInClient.signInIntent
//            resultLauncher.launch(signInIntent)
        }
    }

    private fun validate(): Boolean {
        return when {
            edt_email.text.isEmpty() -> {
                edt_email.error = "Email tidak boleh kosong"
                edt_email.requestFocus()
                false
            }
            edt_password.text.isEmpty() -> {
                edt_password.error = "Password tidak boleh kosong"
                edt_password.requestFocus()
                false
            }
            else -> {
                true
            }
        }
    }

//    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // There are no request codes
//            val data: Intent? = result.data
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            try {
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
//                firebaseAuthWithGoogle(account.idToken!!)
//            } catch (e: ApiException) {
//                Log.w(TAG, "Google sign in failed", e)
//            }
////            doSomeOperations()
//        } else {
//            Log.d(TAG, "Result failed" + result.resultCode)
//        }
//    }

    private fun firebaseAuthWithGoogle(idToken: String) {
//        vm.startLoading()
//        val credential = GoogleAuthProvider.getCredential(idToken, null)
//        auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
//            if (task.isSuccessful) {
//                Log.d(TAG, "signInWithCredential:success")
//                val user = auth.currentUser!!
//
//                val data = User()
//                data.email = user.email!!
//                data.name = user.displayName!!
//                vm.loginGoogle(data)
//
//            } else {
//                Log.e(TAG, "signInWithCredential:failure", task.exception)
//                Toast.makeText(this, "Authentication Failed.", Toast.LENGTH_SHORT).show()
////                updateUI(null)
//            }
//        }
    }

    private fun signOut() {
//        auth.signOut()
//        googleSignInClient.signOut()
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private fun setupGoogle() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
//        auth = Firebase.auth
    }

    override fun onPause() {
        logs("onPaus")
        super.onPause()
    }

    override fun onDetachedFromWindow() {
        logs("onDetachedFromWindow")
        super.onDetachedFromWindow()
    }
}