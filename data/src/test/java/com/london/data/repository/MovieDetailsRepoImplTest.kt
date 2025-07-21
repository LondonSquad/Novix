package com.london.data.repository

import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemoteDataSource
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieActor
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.CollectionDetails
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.GenreRemote
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.ProductionCompany
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.ProductionCountry
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.SpokenLanguage
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.MovieImagesResponse
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.Poster
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMovieRemote
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse
import com.london.data.utils.asImageUrlOrEmpty
import com.london.domain.GetMovieCastFailedException
import com.london.domain.GetMovieDetailsFailedException
import com.london.domain.GetMovieImagesFailedException
import com.london.domain.GetSimilarMoviesFailedException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class MovieDetailsRepoImplTest {

    private lateinit var remoteDataSource: MovieDetailsRemoteDataSource
    private lateinit var repository: MovieDetailsRepoImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = MovieDetailsRepoImpl(remoteDataSource)
    }

    private fun fakeMovieDetailsRemote() = MovieDetailsResponse(
        adult = false,
        backdropPath = "/b.jpg",
        belongsToCollection = CollectionDetails(1, "Coll"),
        budget = 1,
        genreRemote = listOf(
            GenreRemote(1, "Sci-Fi"),
            GenreRemote(2, "Thriller")
        ),
        homepage = "url",
        id = 123,
        imdbId = "tt1",
        originCountry = listOf("US"),
        originalLanguage = "en",
        originalTitle = "Inception",
        overview = "dream",
        popularity = 1.0,
        posterPath = "/p.jpg",
        productionCompanies = listOf(
            ProductionCompany(1, null, "WB", "US")
        ),
        productionCountries = listOf(
            ProductionCountry("US", "USA")
        ),
        releaseDate = "2010-07-16",
        revenue = 1,
        runtime = 148,
        status = "Released",
        tagline = "Mind crime",
        title = "Inception",
        video = false,
        voteAverage = 8.8,
        voteCount = 100,
        spokenLanguages = listOf(
            SpokenLanguage("English", "en", "English")
        ),
    )

    private fun fakeSimilarMoviesRemote() = SimilarMoviesResponse(
        page = 1,
        totalPages = 1,
        totalResults = 2,
        similarMovieRemotes = listOf(
            SimilarMovieRemote(
                adult = false,
                backdropPath = "/b1.jpg",
                genreIds = listOf(1),
                id = 1,
                originalLanguage = "en",
                originalTitle = "Interstellar",
                overview = "Space",
                popularity = 1.0,
                posterPath = "/p1.jpg",
                releaseDate = "2014-11-07",
                title = "Interstellar",
                video = false,
                voteAverage = 8.5,
                voteCount = 1000
            ),
            SimilarMovieRemote(
                adult = false,
                backdropPath = "/b2.jpg",
                genreIds = listOf(2),
                id = 2,
                originalLanguage = "en",
                originalTitle = "Tenet",
                overview = "Time",
                popularity = 1.0,
                posterPath = "/p2.jpg",
                releaseDate = "2020-08-26",
                title = "Tenet",
                video = false,
                voteAverage = 7.5,
                voteCount = 900
            )
        )
    )

    private fun fakeMovieImagesRemote() = MovieImagesResponse(
        backdrops = emptyList(),
        id = 123,
        logos = emptyList(),
        posters = listOf(
            Poster(
                aspectRatio = 0.67,
                filePath = "/img1.jpg",
                height = 100,
                iso6391 = "en",
                voteAverage = 8.0,
                voteCount = 10,
                width = 50
            ),
            Poster(
                aspectRatio = 0.67,
                filePath = "/img2.jpg",
                height = 100,
                iso6391 = "en",
                voteAverage = 7.5,
                voteCount = 8,
                width = 50
            )
        )
    )

    private fun fakeMovieCastRemote() = MovieCastResponse(
        id = 123,
        actorRemote = listOf(
            MovieActor(
                adult = false,
                gender = 2,
                id = 1,
                knownForDepartment = "Acting",
                name = "Leonardo DiCaprio",
                originalName = "Leonardo DiCaprio",
                popularity = 90.0,
                profilePath = "/leo.jpg",
                castId = 10,
                character = "Cobb",
                creditId = "abcd",
                order = 0
            ),
            MovieActor(
                adult = false,
                gender = 2,
                id = 2,
                knownForDepartment = "Acting",
                name = "Joseph Gordon-Levitt",
                originalName = "Joseph Gordon-Levitt",
                popularity = 80.0,
                profilePath = "/jgl.jpg",
                castId = 11,
                character = "Arthur",
                creditId = "efgh",
                order = 1
            )
        ),
        crew = emptyList()
    )

    @Test
    fun `getMovieUsingId should map remote data correctly`() = runTest {
        coEvery { remoteDataSource.getMovieDetails(123) } returns fakeMovieDetailsRemote()

        val result = repository.getMovieById(123)

        assertEquals("Inception", result.movieName)
        assertEquals(2, result.genres.size)
        assertEquals("Sci-Fi", result.genres[0].name)
    }

    @Test
    fun `getSimilarMovies should map similar movies correctly`() = runTest {
        coEvery { remoteDataSource.getSimilarMovies(123) } returns fakeSimilarMoviesRemote()

        val result = repository.getSimilarMoviesById(123)

        assertEquals(2, result.size)
    }

    @Test
    fun `getMovieImages should return poster file paths`() = runTest {
        coEvery { remoteDataSource.getMovieImages(123) } returns fakeMovieImagesRemote()

        val result = repository.getMovieImagesById(123)

        assertEquals(
            listOf("/img1.jpg".asImageUrlOrEmpty(), "/img2.jpg".asImageUrlOrEmpty()),
            result
        )
    }

    @Test
    fun `getMovieCast should return actor list with names and characters`() = runTest {
        coEvery { remoteDataSource.getMovieCast(123) } returns fakeMovieCastRemote()

        val result = repository.getMovieCastById(123)

        assertEquals(2, result.size)
        assertEquals("Leonardo DiCaprio", result[0].name)
        assertEquals("Cobb", result[0].characterName)
        assertEquals("Arthur", result[1].characterName)
    }


    @Test
    fun `getMovieUsingId should propagate GetMovieDetailsFailedException`() = runTest {
        coEvery { remoteDataSource.getMovieDetails(123) } throws GetMovieDetailsFailedException("Network error")

        val ex = assertThrows<GetMovieDetailsFailedException> {
                repository.getMovieById(123)

        }
        assertEquals("Network error", ex.message)
    }

    @Test
    fun `getSimilarMovies should propagate GetSimilarMoviesFailedException`() = runTest {
        coEvery { remoteDataSource.getSimilarMovies(123) } throws GetSimilarMoviesFailedException("API failed")

        val ex = assertThrows<GetSimilarMoviesFailedException> {
                repository.getSimilarMoviesById(123)

        }
        assertEquals("API failed", ex.message)
    }

    @Test
    fun `getMovieImages should propagate GetMovieImagesFailedException`() = runTest {
        coEvery { remoteDataSource.getMovieImages(123) } throws GetMovieImagesFailedException("Server error")

        val ex = assertThrows<GetMovieImagesFailedException> {
                repository.getMovieImagesById(123)

        }
        assertEquals("Server error", ex.message)
    }

    @Test
    fun `getMovieCast should propagate GetMovieCastFailedException`() = runTest {
        coEvery { remoteDataSource.getMovieCast(123) } throws GetMovieCastFailedException("MovieActor API down")

        val ex = assertThrows<GetMovieCastFailedException> {
            repository.getMovieCastById(123)
        }
        assertEquals("MovieActor API down", ex.message)
    }
}
