package com.london.data.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.london.data.BuildConfig
import com.london.data.local.preference.AuthPreferences
import com.london.data.remote.interceptor.AuthInterceptor
import com.london.data.local.preference.SharedPrefsTokenProvider
import com.london.data.local.source.device.DeviceConfigurationDataSource
import com.london.data.remote.interceptor.AuthInterceptor
import com.london.data.remote.service.auth.AuthApiService
import com.london.data.remote.service.details.actor.ActorDetailsApiService
import com.london.data.remote.service.details.movie.MovieDetailsApiService
import com.london.data.remote.service.details.tvshow.TvShowDetailsApiService
import com.london.data.remote.service.home.PopularApiService
import com.london.data.remote.service.reviews.ReviewsApiService
import com.london.data.remote.service.search.SearchApiService
import com.london.data.remote.service.toprated.movie.TopRatedMovieApiService
import com.london.data.remote.service.toprated.tvseries.TopRatedTvSeriesApiService
import com.london.data.remote.service.toprated.TopRatedMovieApiService
import com.london.data.remote.service.toprated.TopRatedTvSeriesApiService
import com.london.domain.repository.SessionTokenProvider
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalSerializationApi::class)
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
        return HttpLoggingInterceptor {
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
                .build()

            chain.proceed(newRequest)
        }
    }

    @Single
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        apiInterceptor: Interceptor,
        authInterceptor: AuthInterceptor,
        context: Context
    ): OkHttpClient {
        val cacheSize = 10L * 1024 * 1024
        val cache = Cache(
            directory = File(context.cacheDir, "http_cache"),
            maxSize = cacheSize
        )
        return OkHttpClient.Builder()
            .addInterceptor(apiInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Single
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit {
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

    @Single
    fun providePopularMoviesApiService(retrofit: Retrofit): PopularApiService =
        retrofit.create(PopularApiService::class.java)
    @Single
    fun provideSessionTokenProvider(authPreferences: AuthPreferences): SessionTokenProvider {
        return SharedPrefsTokenProvider(authPreferences)
    }

    @Single
    fun provideAuthPreferences(context: Context): AuthPreferences {
        return AuthPreferences(context.getSharedPreferences("auth", Context.MODE_PRIVATE))
    }

    @Single
    fun provideAuthApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Single
    fun provideTopRatedMovieApi(retrofit: Retrofit): TopRatedMovieApiService =
        retrofit.create(TopRatedMovieApiService::class.java)

    @Single
    fun provideTopRatedTvShowApi(retrofit: Retrofit): TopRatedTvSeriesApiService =
        retrofit.create(TopRatedTvSeriesApiService::class.java)
}