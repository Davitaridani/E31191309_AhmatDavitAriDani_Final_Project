package com.example.dianastore.helper

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.internal.Primitives
import com.example.dianastore.model.User
import java.lang.reflect.Type

class Prefs {

    val login = "login"
    val nama = "nama"
    val phone = "phone"
    val email = "email"
    val isAdmin = "isAdmin"
    val infoLogin = "infoLogin"
    val setting = "setting"
    val home = "home"
    val listBerita = "listBerita"

    val user = "user"

    private val mypref = "MAIN_PREF"
    private val sp: SharedPreferences?

    constructor(context: Activity) {
        sp = context.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }

    constructor(context: Context) {
        sp = context.getSharedPreferences(mypref, Context.MODE_PRIVATE)
    }

    fun setObject(key: String, src: Any) {
        val json = Gson().toJson(src)
        sp!!.edit().putString(key, json).apply()
    }

    fun <T> getObject(key: String, classOfT: Class<T>): T? {
        val json = sp!!.getString(key, null) ?: return null
        val obj = Gson().fromJson<Any>(json, classOfT as Type)
        return Primitives.wrap(classOfT).cast(obj)!!
    }



    fun setIsAdmin(status: Boolean) {
        sp!!.edit().putBoolean(isAdmin, status).apply()
    }

    fun getIsAdmin(): Boolean {
        return sp!!.getBoolean(isAdmin, false)
    }

    fun setStatusLogin(status: Boolean) {
        sp!!.edit().putBoolean(login, status).apply()
    }

    fun getStatusLogin(): Boolean {
        return sp!!.getBoolean(login, false)
    }

    fun setUser(value: User) {
        val data: String = Gson().toJson(value, User::class.java)
        sp!!.edit().putString(user, data).apply()
    }

    fun getUser(): User? {
        val data:String = sp!!.getString(user, null) ?: return null
        return Gson().fromJson<User>(data, User::class.java)
    }

    fun setString(key: String, vlaue: String) {
        sp!!.edit().putString(key, vlaue).apply()
    }

    fun getString(key: String): String {
        return sp!!.getString(key, "")!!
    }

    fun clear(){
        sp!!.edit().clear().apply()
    }

}