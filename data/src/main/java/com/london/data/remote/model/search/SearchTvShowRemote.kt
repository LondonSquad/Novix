package com.london.data.remote.model.search

import com.london.data.mapper.genre.GenreMapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchTvShowRemote(
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("first_air_date")
    val firstAirDate: String? = null,
    @SerialName("name")
    val name: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double? = null,
) : GenreMapper
