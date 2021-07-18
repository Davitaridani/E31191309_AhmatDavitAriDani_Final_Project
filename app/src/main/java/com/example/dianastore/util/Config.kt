package com.example.dianastore.util

object Config {
    const val baseUrl2 = "http://192.168.1.87/TokoOnline_DianaStore_Web/"

    const val productUrl = baseUrl2 + "public/storage/produk/"

    const val urlmain = baseUrl2 + "public/"
    const val urlStorage = urlmain + "storage"


    fun urlSlider(url: String) = "$urlStorage/slider/$url"
    fun urlBerita(url: String) = "$urlStorage/berita/$url"
    fun urlProduct(url: String) = "$urlStorage/produk/$url"
}