package com.bestoffers.enjoeepharmacy

import com.bestoffers.enjoeepharmacy.Models.RegisterApiModel
import com.bestoffers.enjoeepharmacy.Models.ResendOtpModel
import com.bestoffers.enjoeepharmacy.Models.VerifyOtpModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {

    @POST("register")
    fun register(@Body register: RegisterBody): retrofit2.Call<RegisterApiModel>

    @POST("otp-verify")
    fun verifyOtp(@Body register: VerifyOTPBody): retrofit2.Call<VerifyOtpModel>

    @POST("resend-verify-otp")
    fun resendVerifyOtp(@Body register: ResendVerifyOTPBody): retrofit2.Call<ResendOtpModel>
}

class RetroFitClient {
    companion object {

        val BASE_URL: String = AppConstants.BASE_URL;
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build();

        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }

    }
}

data class RegisterBody(val name: String,val country_code:String, val mobile: String, val password: String);
data class LoginBody(val email: String, val password: String);
data class VerifyOTPBody(val id: String, val otp: String);
data class ResendVerifyOTPBody(val id: String);