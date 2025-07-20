package com.london.data.di

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.london.data.BuildConfig
import com.london.data.datasource.device.DeviceConfigurationDataSource
import com.london.data.datasource.remote.details.actordetails.api.ActorDetailsApiService
import com.london.data.datasource.remote.details.moviedetails.api.MovieDetailsApiService
import com.london.data.datasource.remote.details.tvshowdetails.api.TvShowDetailsApiService
import com.london.data.datasource.remote.reviews.api.ReviewsApiService
import com.london.data.datasource.remote.search.api.SearchApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Single
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            isLenient = true
            prettyPrint = BuildConfig.DEBUG
            encodeDefaults = true
        }
    }

    @Single
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Log.i("DEBUGGING", message)
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Single
    fun provideApiInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            val deviceLanguage = DeviceConfigurationDataSource(context).getCurrentLanguage()

            val newUrl = originalUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .addQueryParameter("language", deviceLanguage)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .addHeader("Authorization", "Bearer ${BuildConfig.AUTHORIZATION_KEY}")
                .build()

            chain.proceed(newRequest)
        }
    }

    @Single
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiInterceptor: Interceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Single
    fun provideRetrofit(okHttpClient: OkHttpClient,
                        json: Json

    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(contentType = "application/json".toMediaType())
            )
            .build()
    }

    @Single
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService {
        return retrofit.create(SearchApiService::class.java)
    }
    @Single
    fun provideMovieDetailsApiService(retrofit: Retrofit): MovieDetailsApiService =
        retrofit.create(MovieDetailsApiService::class.java)

    @Single
    fun provideTvShowDetailsApiService(retrofit: Retrofit): TvShowDetailsApiService =
        retrofit.create(TvShowDetailsApiService::class.java)

    @Single
    fun provideActorDetailsApiService(retrofit: Retrofit): ActorDetailsApiService =
        retrofit.create(ActorDetailsApiService::class.java)

    @Single
    fun provideReviewsApiService(retrofit: Retrofit): ReviewsApiService =
        retrofit.create(ReviewsApiService::class.java)

}
