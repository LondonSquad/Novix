package com.london.data.di

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.london.data.BuildConfig
import com.london.data.local.preference.AuthPreferences
import com.london.data.local.preference.SharedPrefsTokenProvider
import com.london.data.local.source.device.DeviceConfigurationDataSource
import com.london.data.remote.interceptor.AuthInterceptor
import com.london.data.remote.service.authentication.AuthenticationApiService
import com.london.data.remote.service.details.actor.ActorDetailsApiService
import com.london.data.remote.service.details.movie.MovieDetailsApiService
import com.london.data.remote.service.details.tvshow.TvShowDetailsApiService
import com.london.data.remote.service.home.PopularApiService
import com.london.data.remote.service.home.TrendingApiService
import com.london.data.remote.service.reviews.ReviewsApiService
import com.london.data.remote.service.search.SearchApiService
import com.london.data.remote.service.toprated.TopRatedMovieApiService
import com.london.data.remote.service.toprated.TopRatedTvSeriesApiService
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
import org.koin.core.annotation.Singleton
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

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
            val deviceLanguage = DeviceConfigurationDataSource(context).getCurrentLanguage()

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
    fun provideActorDetailsApiService(retrofit: Retrofit): ActorDetailsApiService =
        retrofit.create(ActorDetailsApiService::class.java)

    @Provides
    fun provideMovieDetailsApiService(retrofit: Retrofit): MovieDetailsApiService =
        retrofit.create(MovieDetailsApiService::class.java)

    @Provides
    fun provideTvShowDetailsApiService(retrofit: Retrofit): TvShowDetailsApiService =
        retrofit.create(TvShowDetailsApiService::class.java)

    @Provides
    fun provideSearchApiService(retrofit: Retrofit): SearchApiService =
        retrofit.create(SearchApiService::class.java)

    @Provides
    fun provideReviewsApiService(retrofit: Retrofit): ReviewsApiService =
        retrofit.create(ReviewsApiService::class.java)

    @Provides
    fun provideTrendingApiService(retrofit: Retrofit): TrendingApiService =
        retrofit.create(TrendingApiService::class.java)

    @Provides
    fun providePopularApiService(retrofit: Retrofit): PopularApiService =
        retrofit.create(PopularApiService::class.java)

    @Provides
    fun provideTopRatedMovieApiService(retrofit: Retrofit): TopRatedMovieApiService =
        retrofit.create(TopRatedMovieApiService::class.java)

    @Provides
    fun provideTopRatedTvShowApiService(retrofit: Retrofit): TopRatedTvSeriesApiService =
        retrofit.create(TopRatedTvSeriesApiService::class.java)

    @Provides
    fun provideAuthApiService(retrofit: Retrofit): AuthenticationApiService =
        retrofit.create(AuthenticationApiService::class.java)

    @Provides
    @Singleton
    fun provideAuthPreferences(@ApplicationContext context: Context): AuthPreferences =
        AuthPreferences(context.getSharedPreferences("auth", Context.MODE_PRIVATE))

    @Provides
    fun provideSessionTokenProvider(authPreferences: AuthPreferences): SessionTokenProvider =
        SharedPrefsTokenProvider(authPreferences = authPreferences)

    @Provides
    @Singleton
    fun provideCrashReporter(): CrashReporter = FirebaseCrashReporter()
}
