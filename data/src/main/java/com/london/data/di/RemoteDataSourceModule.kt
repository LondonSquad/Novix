package com.london.data.di

import com.london.data.remote.service.authentication.AuthenticationApiService
import com.london.data.remote.service.details.actor.ActorDetailsApiService
import com.london.data.remote.service.details.movie.MovieDetailsApiService
import com.london.data.remote.service.details.tvshow.TvShowDetailsApiService
import com.london.data.remote.service.home.PopularApiService
import com.london.data.remote.service.reviews.ReviewsApiService
import com.london.data.remote.service.search.SearchApiService
import com.london.data.remote.service.toprated.TopRatedMovieApiService
import com.london.data.remote.service.toprated.TopRatedTvSeriesApiService
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSourceImpl
import com.london.data.remote.source.details.actor.ActorDetailsRemoteDataSourceImpl
import com.london.data.remote.source.details.movie.MovieDetailsRemoteDataSourceImpl
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSourceImpl
import com.london.data.remote.source.details.videoprovider.movie.MovieVideoProviderRemoteImpl
import com.london.data.remote.source.details.videoprovider.tvshow.TvShowVideoProviderRemoteImpl
import com.london.data.remote.source.home.popular.PopularRemoteDataSourceImpl
import com.london.data.remote.source.reviews.ReviewsRemoteDataSourceImpl
import com.london.data.remote.source.search.SearchRemoteDataSourceImpl
import com.london.data.remote.source.toprated.movie.TopRatedMovieRemoteDataSourceImpl
import com.london.data.remote.source.toprated.tvseries.TopRatedTvRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    fun provideActorDetailsRemoteDataSource(
        apiService: ActorDetailsApiService,
    ) = ActorDetailsRemoteDataSourceImpl(actorDetailsApiService = apiService)

    @Provides
    fun provideTvShowDetailsRemoteDataSource(
        apiService: TvShowDetailsApiService,
    ) = TvShowDetailsRemoteDataSourceImpl(tvShowDetailsApiService = apiService)

    @Provides
    fun provideReviewsRemoteDataSource(
        apiService: ReviewsApiService,
    ) = ReviewsRemoteDataSourceImpl(reviewsApiService = apiService)

    @Provides
    fun providePopularRemoteDataSource(
        apiService: PopularApiService,
    ) = PopularRemoteDataSourceImpl(popularApiService = apiService)

    @Provides
    fun provideSearchRemoteDataSource(
        apiService: SearchApiService,
    ) = SearchRemoteDataSourceImpl(searchApiService = apiService)

    @Provides
    fun provideTopRatedMovieRemoteDataSource(
        apiService: TopRatedMovieApiService,
    ) = TopRatedMovieRemoteDataSourceImpl(topRatedMovieApi = apiService)

    @Provides
    fun provideTopRatedTvRemoteDataSource(
        apiService: TopRatedTvSeriesApiService,
    ) = TopRatedTvRemoteDataSourceImpl(topRatedTvSeriesApi = apiService)

    @Provides
    fun provideAuthenticationRemoteDataSource(
        apiService: AuthenticationApiService,
    ) = AuthenticationRemoteDataSourceImpl(authApiService = apiService)

    @Provides
    fun provideMovieDetailsRemoteDataSource(
        apiService: MovieDetailsApiService,
    ) = MovieDetailsRemoteDataSourceImpl(movieDetailsApiService = apiService)

    @Provides
    fun provideMovieVideoProviderRemote(
        apiService: MovieDetailsApiService,
    ) = MovieVideoProviderRemoteImpl(movieDetailsApiService = apiService)

    @Provides
    fun provideTvShowVideoProviderRemote(
        apiService: TvShowDetailsApiService,
    ) = TvShowVideoProviderRemoteImpl(tvShowDetailsApiService = apiService)
}
