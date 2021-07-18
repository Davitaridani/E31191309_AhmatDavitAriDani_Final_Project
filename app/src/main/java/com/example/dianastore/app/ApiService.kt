package com.example.dianastore.app

import com.example.dianastore.model.*
import com.example.dianastore.model.rajaongkir.ResponOngkir
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //    @Headers(API)
    @POST("login")
    suspend fun login(
            @Body user: User
    ): Response<ResponModel>

    @POST("login/google")
    suspend fun loginGoogle(
            @Body user: User
    ): Response<ResponModel>

    @POST("register")
    suspend fun register(
            @Body user: User
    ): Response<ResponModel>

    @POST("chekout")
    fun chekout(
            @Body data: Chekout
    ): Call<ResponModel>

    @GET("produk")
    fun getProduk(): Call<ResponModel>

    @GET("home")
    fun getHome(): Call<ResponModel>

    @FormUrlEncoded
    @POST("voucher")
    fun getVoucher(
            @Field("kode") kode: String,
            @Field("userId") id: Int
    ): Call<ResponModel>

    @GET("province")
    fun getProvinsi(
            @Header("key") key: String
    ): Call<ResponModel>

    @GET("city")
    fun getKota(
            @Header("key") key: String,
            @Query("province") id: String
    ): Call<ResponModel>

    @GET("kecamatan")
    fun getKecamatan(
            @Query("id_kota") id: Int
    ): Call<ResponModel>

    @FormUrlEncoded
    @POST("cost")
    fun ongkir(
            @Header("key") key: String,
            @Field("origin") origin: String,
            @Field("destination") destination: String,
            @Field("weight") weight: Int,
            @Field("courier") courier: String
    ): Call<ResponOngkir>

    @GET("chekout/user/{id}")
    fun getRiwayat(
            @Path("id") id: Int
    ): Call<ResponModel>

    @POST("chekout/batal/{id}")
    fun batalChekout(
            @Path("id") id: Int
    ): Call<ResponModel>

    @GET("produk")
    suspend fun getProduct(): Response<ResponModel>

    @Multipart
    @POST("product")
    suspend fun createProduct(
            @Part("name") name: RequestBody,
            @Part("deskripsi") bahan: RequestBody,
            @Part image: MultipartBody.Part? = null
    ): Response<ResponModel>

    @Multipart
    @POST("product/{id}")
    suspend fun updateProduct(
            @Path("id") id: Int,
            @Part("name") name: RequestBody,
            @Part("deskripsi") bahan: RequestBody,
            @Part image: MultipartBody.Part? = null
    ): Response<ResponModel>

    @DELETE("product/{id}")
    suspend fun deleteProduct(
            @Path("id") id: Int
    ): Response<ResponModel>
}