package com.london.data.mapper.moviedetails

import com.london.data.remote.model.details.movie.model.moviedetails.GenreRemote
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.moviedetails.ProductionCompanyRemote
import com.london.data.remote.model.details.movie.model.moviedetails.ProductionCountryRemote
import com.london.data.remote.model.details.movie.model.moviedetails.RemoteCollectionDetails
import com.london.data.remote.model.details.movie.model.moviedetails.SpokenLanguageRemote
import com.london.data.remote.model.details.movie.model.similarmovies.SimilarMovieRemote
import com.london.data.utils.asImageUrlOrEmpty
import com.london.data.utils.isTrue
import com.london.data.utils.orZero
import com.london.data.utils.roundToFirstDecimal
import com.london.domain.entity.moviedatails.CollectionDetails
import com.london.domain.entity.Genre
import com.london.domain.entity.moviedatails.MovieDetails
import com.london.domain.entity.moviedatails.ProductionCompany
import com.london.domain.entity.moviedatails.ProductionCountry
import com.london.domain.entity.moviedatails.SimilarMovie
import com.london.domain.entity.moviedatails.SpokenLanguage

fun MovieDetailsResponse.toEntity(): MovieDetails {
    return MovieDetails(
        adult = adult.isTrue,
        backdropUrl = backdropPath.asImageUrlOrEmpty(),
        belongsToCollection = remoteBelongsToCollection?.toEntity() ?: CollectionDetails(0, ""),
        budget = budget.orZero(),
        genres = genreRemote?.map { it.toEntity() }.orEmpty(),
        homepage = homepage.orEmpty(),
        id = id.orZero(),
        imdbId = imdbId.orEmpty(),
        originCountry = originCountry.orEmpty(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterUrl = posterPath.asImageUrlOrEmpty(),
        productionCompanies = productionCompanies?.map { it.toEntity() }.orEmpty(),
        productionCountries = productionCountries?.map { it.toEntity() }.orEmpty(),
        releaseDate = releaseDate.orEmpty(),
        revenue = revenue.orZero(),
        runtime = runtime.orZero(),
        spokenLanguages = spokenLanguages?.map { it.toEntity() }.orEmpty(),
        status = status.orEmpty(),
        tagline = tagline.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.roundToFirstDecimal(),
        voteCount = voteCount.orZero()
    )
}

fun RemoteCollectionDetails.toEntity(): CollectionDetails =
    CollectionDetails(
        id = id.orZero(),
        name = name.orEmpty()
    )


fun GenreRemote.toEntity(): Genre =
    Genre(
        id = id.orZero(),
        name = name.orEmpty()
    )

fun ProductionCompanyRemote.toEntity(): ProductionCompany =
    ProductionCompany(
        id = id.orZero(),
        logoPath = logoPath.asImageUrlOrEmpty(),
        name = name.orEmpty(),
        originCountry = originCountry.orEmpty(),
    )

fun ProductionCountryRemote.toEntity(): ProductionCountry =
    ProductionCountry(
        iso31661 = iso31661.orEmpty(),
        name = name.orEmpty(),
    )

fun SpokenLanguageRemote.toEntity(): SpokenLanguage =
    SpokenLanguage(
        englishName = englishName.orEmpty(),
        iso6391 = iso6391.orEmpty(),
        name = name.orEmpty(),
    )

fun SimilarMovieRemote.toEntity(): SimilarMovie =
    SimilarMovie(
        adult = adult.isTrue,
        backdropPath = backdropPath.asImageUrlOrEmpty(),
        genreIds = genreIds.orEmpty(),
        id = id.orZero(),
        originalLanguage = originalLanguage.orEmpty(),
        originalTitle = originalTitle.orEmpty(),
        overview = overview.orEmpty(),
        popularity = popularity.orZero(),
        posterPath = posterPath.asImageUrlOrEmpty(),
        releaseDate = releaseDate.orEmpty(),
        title = title.orEmpty(),
        video = video.isTrue,
        voteAverage = voteAverage.orZero(),
        voteCount = voteCount.orZero(),
    )

