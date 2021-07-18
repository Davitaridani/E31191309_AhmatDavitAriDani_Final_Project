package com.example.dianastore.activity.authActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dianastore.activity.productActivity.ProductRepository
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.InfoLogin
import com.example.dianastore.model.ResponModel
import com.example.dianastore.model.TabelProduct
import com.example.dianastore.model.User
import com.example.dianastore.util.Constants
import com.example.dianastore.util.Constants.EMAIL
import com.example.dianastore.util.Constants.GOOGLE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val s = Prefs(application)
    private val repo = AuthRepository(application)
    private val repoProduct = ProductRepository(application)

    fun loginEmail(user: User) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        repo.loginEmail(user).let {
            endLoading()
            try {
                it.body()!!.user.password = user.password
                setupData(it)
            } catch (e: Exception) {
                setError(e.message!!)
            }
        }
    }

    fun loginGoogle(user: User) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        repo.loginGoogle(user).let {
            endLoading()
            try {
                it.body()!!.user.password = user.password
                setupData(it, true)
            } catch (e: Exception) {
                setError(e.message!!)
            }
        }
    }

    fun register(user: User) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        repo.register(user).let {
            endLoading()
            try {
                it.body()!!.user.password = user.password
                setupData(it)
            } catch (e: Exception) {
                setError(e.message!!)
            }
        }
    }

    private fun setupData(it: Response<ResponModel>, isGoogle: Boolean = false) {
        if (validate(it)) {
            val res = it.body()!!
            val user = it.body()!!.user
            s.setObject(s.user, user)
            s.setStatusLogin(true)
            s.setIsAdmin(user.role == Constants.ADMIN)
            val info = InfoLogin()
            info.email = user.email
            info.password = user.password
            info.type = if (isGoogle) GOOGLE else EMAIL
            s.setObject(s.infoLogin, info)
            success()
            saveProductToDb(res.produks)
        }
    }

    private fun saveProductToDb(products: ArrayList<TabelProduct>) {
        viewModelScope.launch(Dispatchers.IO) {
            repoProduct.insertProduct(products)
        }
    }

    private fun validate(response: Response<ResponModel>): Boolean {
        if (response.isSuccessful) {
            val res = response.body()!!
            if (res.success == 1) {
                return true
            } else setError(res.message)
        } else setError(response.message())
        return false
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _successResponse = MutableLiveData<String>()
    val successResponse: LiveData<String> get() = _successResponse

    private val _errorResponse = MutableLiveData<String>()
    val errorResponse: LiveData<String> get() = _errorResponse

    private fun setError(message: String) {
        _errorResponse.postValue(message)
    }

    private fun success(message: String = "success") {
        _successResponse.postValue(message)
    }

    fun startLoading() {
        _isLoading.postValue(true)
    }

    fun endLoading() {
        _isLoading.postValue(false)
    }
}