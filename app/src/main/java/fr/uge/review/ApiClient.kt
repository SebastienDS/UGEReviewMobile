package fr.uge.review

import android.content.Context
import fr.uge.review.service.RegistrationService
import fr.uge.review.service.ReviewService
import fr.uge.review.service.SessionManager
import fr.uge.review.service.UserService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient(context: Context) {
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080/"  // localhost on emulator
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient(context))
            .build()
    }

    private fun okhttpClient(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .build()
    }

    val registrationService: RegistrationService by lazy {
        retrofit.create(RegistrationService::class.java)
    }

    val reviewService: ReviewService by lazy {
        retrofit.create(ReviewService::class.java)
    }

    val userService: UserService by lazy {
        retrofit.create(UserService::class.java)
    }
}

class AuthInterceptor(context: Context) : Interceptor {
    private val sessionManager = SessionManager(context)

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()

        sessionManager.getAuthToken()?.let {
            requestBuilder.addHeader("Authorization", it)
        }

        return chain.proceed(requestBuilder.build())
    }
}