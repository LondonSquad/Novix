package com.london.data.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.london.data.BuildConfig
import com.london.data.local.preference.AuthPreferences
import com.london.data.local.preference.SharedPrefsTokenProvider
import com.london.data.local.preference.readLanguageCode
import com.london.data.remote.interceptor.AuthInterceptor
import com.london.data.remote.service.account.AccountApiService
import com.london.data.remote.service.actor.ActorApiService
import com.london.data.remote.service.authentication.AuthenticationApiService
import com.london.data.remote.service.list.CustomMovieListsApiService
import com.london.data.remote.service.movie.MovieApiService
import com.london.data.remote.service.search.SearchApiService
import com.london.data.remote.service.tvshow.TvShowApiService
import com.london.data.utils.CrashReporter
import com.london.data.utils.FirebaseCrashReporter
import com.london.domain.repository.SessionTokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@OptIn(ExperimentalSerializationApi::class)
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = BuildConfig.DEBUG
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    @Provides
    @Singleton
    fun provideApiInterceptor(@ApplicationContext context: Context): Interceptor =
        Interceptor { chain ->
            val original = chain.request()
            val deviceLanguage = readLanguageCode(context)

            val newUrl = original.url.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .apply {
                    if (!original.url.encodedPath.endsWith("/images")) {
                        addQueryParameter("language", deviceLanguage)
                    }
                }.build()

            val newRequest = original.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        api: Interceptor,
        auth: AuthInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(api)
        .addInterceptor(auth)
        .addInterceptor(logging)
        .cache(Cache(File(context.cacheDir, "http_cache"), 10L * 1024 * 1024))
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()


    @Provides
    @Singleton
    fun provideActorDetailsApiService(retrofit: Retrofit): ActorApiService =
        retrofit.create(ActorApiService::class.java)

    @Provides
    @Singleton
    fun provideMovieDetailsApiService(retrofit: Retrofit): MovieApiService =
        retrofit.create(MovieApiService::class.java)

    @Provides
    @Singleton
    fun provideTvShowDetailsApiService(retrofit: Retrofit): TvShowApiService =
        retrofit.create(TvShowApiService::class.java)

    @Provides
    @Singleton
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService =
        retrofit.create(SearchApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthenticationApiService =
        retrofit.create(AuthenticationApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences =
        AuthPreferences(context.getSharedPreferences("auth", Context.MODE_PRIVATE))

    @Provides
    @Singleton
    fun provideSessionTokenProvider(authPreferences: AuthPreferences): SessionTokenProvider =
        SharedPrefsTokenProvider(authPreferences = authPreferences)

    @Provides
    @Singleton
    fun provideCrashReporter(): CrashReporter = FirebaseCrashReporter()

    @Provides
    @Singleton
    fun provideCustomMovieListsApiService(retrofit: Retrofit): CustomMovieListsApiService =
        retrofit.create(CustomMovieListsApiService::class.java)

    @Provides
    @Singleton
    fun provideAccountApiService(retrofit: Retrofit): AccountApiService =
        retrofit.create(AccountApiService::class.java)
}