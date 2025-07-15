package com.london.data.datasource.remote.moviedetails.model
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsRemote(
    val adult: Boolean,
    @SerialName("backdrop_path")
    val backdropPath: String?,
    @SerialName("belongs_to_collection")
    val belongsToCollection: CollectionDetails?,
    val budget: Int,
    @SerialName("genres")
    val genreRemotes: List<GenreRemote>,
    val homepage: String,
    val id: Int,
    @SerialName("imdb_id")
    val imdbId: String?,
    @SerialName("origin_country")
    val originCountry: List<String>,
    @SerialName("original_language")
    val originalLanguage: String,
    @SerialName("original_title")
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("production_companies")
    val productionCompanies: List<ProductionCompanyRemote>,
    @SerialName("production_countries")
    val productionCountries: List<ProductionCountryRemote>,
    @SerialName("release_date")
    val releaseDate: String?,
    val revenue: Long,
    val runtime: Int,
    @SerialName("spoken_languages")
    val spokenLanguageRemotes: List<SpokenLanguageRemote>,
    val status: String,
    val tagline: String,
    val title: String,
    val video: Boolean,
    @SerialName("vote_average")
    val voteAverage: Double,
    @SerialName("vote_count")
    val voteCount: Int
)

@Serializable
data class CollectionDetails(
    val id: Int,
    val name: String
)

@Serializable
data class SpokenLanguageRemote(
    @SerialName("english_name")
    val englishName: String,
    @SerialName("iso_639_1")
    val iso6391: String,
    val name: String
)

@Serializable
data class ProductionCountryRemote(
    @SerialName("iso_3166_1")
    val iso31661: String,
    val name: String
)

@Serializable
data class ProductionCompanyRemote(
    val id: Int,
    @SerialName("logo_path")
    val logoPath: String?,
    val name: String,
    @SerialName("origin_country")
    val originCountry: String
)

@Serializable
data class GenreRemote(
    val id: Int,
    val name: String
)