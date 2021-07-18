package com.example.dianastore.activity.productActivity

import android.app.Application
import com.example.dianastore.app.ApiConfig
import com.example.dianastore.model.TabelProduct
import com.example.dianastore.room.MyDatabase
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProductRepository(application: Application) {

    val myDb = MyDatabase.getInstance(application)!!

    var listProduct = myDb.daoProduct().getAll()

    suspend fun insertProduct(tikets: List<TabelProduct>) = myDb.daoProduct().insert(tikets)

    suspend fun insertProduct(tikets: TabelProduct) = myDb.daoProduct().insert(tikets)

    suspend fun deleteProduct(tikets: TabelProduct) = myDb.daoProduct().delete(tikets)

    suspend fun deleteAll() = myDb.daoProduct().deleteAll()

    suspend fun getProduct() = ApiConfig.instanceRetrofit.getProduct()

    suspend fun createProduct(name: RequestBody, deskripsi: RequestBody, image: MultipartBody.Part? = null) =
            ApiConfig.instanceRetrofit.createProduct(name, deskripsi, image)

    suspend fun updateProduct(id: Int, name: RequestBody, deskripsi: RequestBody, image: MultipartBody.Part? = null) =
            ApiConfig.instanceRetrofit.updateProduct(id, name, deskripsi, image)

    suspend fun deleteProduct(id: Int) = ApiConfig.instanceRetrofit.deleteProduct(id)
}