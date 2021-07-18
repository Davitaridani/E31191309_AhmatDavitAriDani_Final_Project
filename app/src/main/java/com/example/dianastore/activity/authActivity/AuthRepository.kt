package com.example.dianastore.activity.authActivity

import android.app.Application
import com.example.dianastore.app.ApiConfig
import com.example.dianastore.model.User

class AuthRepository(var application: Application) {

    suspend fun loginEmail(user: User) = ApiConfig.instanceRetrofit.login(user)

    suspend fun loginGoogle(user: User) = ApiConfig.instanceRetrofit.loginGoogle(user)

    suspend fun register(user: User) = ApiConfig.instanceRetrofit.register(user)
}