package com.london.data.di

import com.london.data.remote.service.authentication.AuthenticationApiService
import com.london.data.remote.service.details.actor.ActorDetailsApiService
import com.london.data.remote.service.details.movie.MovieDetailsApiService
import com.london.data.remote.service.details.rating.RatingApiService
import com.london.data.remote.service.details.tvshow.TvShowDetailsApiService
import com.london.data.remote.service.discover.DiscoverApiService
import com.london.data.remote.service.home.PopularApiService
import com.london.data.remote.service.home.UpComingApiService
import com.london.data.remote.service.list.CustomMovieListsApiService
import com.london.data.remote.service.reviews.ReviewsApiService
import com.london.data.remote.service.search.SearchApiService
import com.london.data.remote.service.toprated.TopRatedApiService
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSource
import com.london.data.remote.source.authentication.AuthenticationRemoteDataSourceImpl
import com.london.data.remote.source.details.actor.ActorDetailsRemoteDataSource
import com.london.data.remote.source.details.actor.ActorDetailsRemoteDataSourceImpl
import com.london.data.remote.source.details.movie.MovieDetailsRemoteDataSource
import com.london.data.remote.source.details.movie.MovieDetailsRemoteDataSourceImpl
import com.london.data.remote.source.details.rating.RatingRemoteDataSource
import com.london.data.remote.source.details.rating.RatingRemoteDataSourceImpl
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSource
import com.london.data.remote.source.details.tvshow.TvShowDetailsRemoteDataSourceImpl
import com.london.data.remote.source.details.videoprovider.tvshow.TvShowVideoProviderRemote
import com.london.data.remote.source.details.videoprovider.tvshow.TvShowVideoProviderRemoteImpl
import com.london.data.remote.source.discover.DiscoverRemoteDataSource
import com.london.data.remote.source.discover.DiscoverRemoteDataSourceImpl
import com.london.data.remote.source.home.popular.PopularRemoteDataSource
import com.london.data.remote.source.home.popular.PopularRemoteDataSourceImpl
import com.london.data.remote.source.home.upcoming.UpComingRemoteDataSource
import com.london.data.remote.source.home.upcoming.UpComingRemoteDataSourceImpl
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSource
import com.london.data.remote.source.list.CustomMovieListsRemoteDataSourceImpl
import com.london.data.remote.source.reviews.ReviewsRemoteDataSource
import com.london.data.remote.source.reviews.ReviewsRemoteDataSourceImpl
import com.london.data.remote.source.search.SearchRemoteDataSource
import com.london.data.remote.source.search.SearchRemoteDataSourceImpl
import com.london.data.remote.source.toprated.TopRatedRemoteDataSource
import com.london.data.remote.source.toprated.TopRatedRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteDataSourceModule {

    @Provides
    @Singleton
    fun provideActorDetailsRemoteDataSource(
        apiService: ActorDetailsApiService,
    ): ActorDetailsRemoteDataSource =
        ActorDetailsRemoteDataSourceImpl(actorDetailsApiService = apiService)

    @Provides
    @Singleton
    fun provideTvShowDetailsRemoteDataSource(
        apiService: TvShowDetailsApiService,
    ): TvShowDetailsRemoteDataSource =
        TvShowDetailsRemoteDataSourceImpl(tvShowDetailsApiService = apiService)

    @Provides
    @Singleton
    fun provideReviewsRemoteDataSource(
        apiService: ReviewsApiService,
    ): ReviewsRemoteDataSource = ReviewsRemoteDataSourceImpl(reviewsApiService = apiService)

    @Provides
    @Singleton
    fun providePopularRemoteDataSource(
        apiService: PopularApiService,
    ): PopularRemoteDataSource = PopularRemoteDataSourceImpl(popularApiService = apiService)

    @Provides
    @Singleton
    fun provideSearchRemoteDataSource(
        apiService: SearchApiService,
    ): SearchRemoteDataSource = SearchRemoteDataSourceImpl(searchApiService = apiService)

    @Provides
    @Singleton
    fun provideTopRatedRemoteDataSource(
        apiService: TopRatedApiService,
    ): TopRatedRemoteDataSource =
        TopRatedRemoteDataSourceImpl(topRatedMovieApi = apiService)

    @Provides
    @Singleton
    fun provideAuthenticationRemoteDataSource(
        apiService: AuthenticationApiService,
    ): AuthenticationRemoteDataSource =
        AuthenticationRemoteDataSourceImpl(authApiService = apiService)

    @Provides
    @Singleton
    fun provideMovieDetailsRemoteDataSource(
        apiService: MovieDetailsApiService,
    ): MovieDetailsRemoteDataSource =
        MovieDetailsRemoteDataSourceImpl(movieDetailsApiService = apiService)

    @Provides
    @Singleton
    fun provideTvShowVideoProviderRemote(
        apiService: TvShowDetailsApiService,
    ): TvShowVideoProviderRemote =
        TvShowVideoProviderRemoteImpl(tvShowDetailsApiService = apiService)

    @Provides
    @Singleton
    fun provideRatingRemoteDataSource(
        apiService: RatingApiService,
    ): RatingRemoteDataSource =
        RatingRemoteDataSourceImpl(ratingApiService = apiService)

    @Provides
    @Singleton
    fun provideUpComingMoviesRemoteDataSource(apiService: UpComingApiService): UpComingRemoteDataSource =
        UpComingRemoteDataSourceImpl(apiService)

    @Provides
    @Singleton
    fun provideDiscoverRemoteDataSource(
        apiService: DiscoverApiService,
    ): DiscoverRemoteDataSource =
        DiscoverRemoteDataSourceImpl(discoverApiService = apiService)

    @Provides
    @Singleton
    fun provideCustomMovieListsRemoteDataSource(
        apiService: CustomMovieListsApiService,
    ): CustomMovieListsRemoteDataSource =
        CustomMovieListsRemoteDataSourceImpl(customMovieListsApiService = apiService)

}
