package com.example.dianastore.activity.productActivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.dianastore.helper.Prefs
import com.example.dianastore.model.ResponModel
import com.example.dianastore.model.TabelProduct
import com.inyongtisto.myhelper.extension.toMultipartBody
import com.inyongtisto.myhelper.extension.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.File

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val s = Prefs(application)
    private val repo = ProductRepository(application)

    var listProduct = repo.listProduct

    fun getProduct() = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        repo.getProduct().let {
            endLoading()
            try {
                if (validate(it)) {
                    success("success")
                    saveProductToDb(it.body()!!.produks)
                }
            } catch (e: Exception) {
                setError(e.message!!)
            }
        }
    }

    fun createProduct(product: TabelProduct, file: File?) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        val name = product.name.toRequestBody()
        val deskripsi = product.deskripsi.toRequestBody()
        val fileImage = file.toMultipartBody()

        repo.createProduct(name, deskripsi, fileImage).let {
            endLoading()
            try {
                if (validate(it)) {
                    saveProductToDb(it.body()!!.produks)
                    success()
                }
            } catch (e: Exception) {
                setError(e.message!!)
            }
        }
    }

    fun updateProduct(product: TabelProduct, file: File?) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        val name = product.name.toRequestBody()
        val deskripsi = product.deskripsi.toRequestBody()
        val fileImage = file.toMultipartBody()

        repo.updateProduct(product.id.toInt(), name, deskripsi, fileImage).let {
            endLoading()
            try {
                if (validate(it)) {
                    saveProductToDb(it.body()!!.produks)
                    success()
                }
            } catch (e: Exception) {
                setError(e.message!!)
            }
        }
    }

    fun deleteProduct(product: TabelProduct) = viewModelScope.launch(Dispatchers.IO) {
        startLoading()
        repo.deleteProduct(product.id.toInt()).let {
            endLoading()
            try {
                if (validate(it)) {
                    deleteProductFromDd(product)
                    success()
                }
            } catch (e: Exception) {
                setError(e.message!!)
            }
        }
    }

    private fun deleteProductFromDd(product: TabelProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteProduct(product)
        }
    }

    private fun saveProductToDb(product: TabelProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insertProduct(product)
        }
    }

    private fun saveProductToDb(products: ArrayList<TabelProduct>) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAll()
            repo.insertProduct(products)
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