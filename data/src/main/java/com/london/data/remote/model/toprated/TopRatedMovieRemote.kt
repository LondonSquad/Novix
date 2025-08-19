package com.london.data.remote.model.toprated


import com.london.data.mapper.genre.GenreMapper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopRatedMovieRemote(
    @SerialName("genre_ids")
    val genreIds: List<Int>? = null,
    @SerialName("id")
    val id: Int? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null ,
    @SerialName("title")
    val title: String? = null,
) : GenreMapper
