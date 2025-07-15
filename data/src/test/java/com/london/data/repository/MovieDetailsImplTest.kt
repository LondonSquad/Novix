package com.london.data.repository

import com.london.data.datasource.remote.moviedetails.GetMovieCastException
import com.london.data.datasource.remote.moviedetails.GetMovieDetailsException
import com.london.data.datasource.remote.moviedetails.GetMovieImagesException
import com.london.data.datasource.remote.moviedetails.GetSimilarMoviesException
import com.london.data.datasource.remote.moviedetails.MovieDetailsRemoteDataSource
import com.london.data.datasource.remote.moviedetails.model.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class MovieDetailsImplTest {

    private lateinit var remoteDataSource: MovieDetailsRemoteDataSource
    private lateinit var repository: MovieDetailsImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = MovieDetailsImpl(remoteDataSource)
    }

    private fun fakeMovieDetailsRemote() = MovieDetailsRemote(
        adult = false,
        backdropPath = "/b.jpg",
        belongsToCollection = CollectionDetails(1, "Coll"),
        budget = 1,
        genreRemotes = listOf(
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
            ProductionCompanyRemote(1, null, "WB", "US")
        ),
        productionCountries = listOf(
            ProductionCountryRemote("US", "USA")
        ),
        releaseDate = "2010-07-16",
        revenue = 1,
        runtime = 148,
        spokenLanguageRemotes = listOf(
            SpokenLanguageRemote("English", "en", "English")
        ),
        status = "Released",
        tagline = "Mind crime",
        title = "Inception",
        video = false,
        voteAverage = 8.8,
        voteCount = 100
    )

    private fun fakeSimilarMoviesRemote() = SimilarMovies(
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

    private fun fakeMovieImagesRemote() = MovieImages(
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

    private fun fakeMovieCastRemote() = MovieCastRemote(
        id = 123,
        actorRemote = listOf(
            ActorRemote(
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
            ActorRemote(
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

        val result = repository.getMovieUsingId(123)

        assertEquals("Inception", result.movieName)
        assertEquals(2, result.genres.size)
        assertEquals("Sci-Fi", result.genres[0].name)
    }

    @Test
    fun `getSimilarMovies should map similar movies correctly`() = runTest {
        coEvery { remoteDataSource.getSimilarMovies(123) } returns fakeSimilarMoviesRemote()

        val result = repository.getSimilarMoviesUsingId(123)

        assertEquals(2, result.size)
    }

    @Test
    fun `getMovieImages should return poster file paths`() = runTest {
        coEvery { remoteDataSource.getMovieImages(123) } returns fakeMovieImagesRemote()

        val result = repository.getMovieImagesUsingId(123)

        assertEquals(listOf("/img1.jpg", "/img2.jpg"), result)
    }

    @Test
    fun `getMovieCast should return actor list with names and characters`() = runTest {
        coEvery { remoteDataSource.getMovieCast(123) } returns fakeMovieCastRemote()

        val result = repository.getMovieCastUsingId(123)

        assertEquals(2, result.size)
        assertEquals("Leonardo DiCaprio", result[0].name)
        assertEquals("Cobb", result[0].characterName)
        assertEquals("Arthur", result[1].characterName)
    }


    @Test
    fun `getMovieUsingId should propagate GetMovieDetailsException`() = runTest {
        coEvery { remoteDataSource.getMovieDetails(123) } throws RuntimeException("Network error")

        val ex = assertThrows<GetMovieDetailsException> {
                repository.getMovieUsingId(123)

        }
        assertEquals("Failed to fetch movie details", ex.message)
    }

    @Test
    fun `getSimilarMovies should propagate GetSimilarMoviesException`() = runTest {
        coEvery { remoteDataSource.getSimilarMovies(123) } throws RuntimeException("API failed")

        val ex = assertThrows<GetSimilarMoviesException> {
                repository.getSimilarMoviesUsingId(123)

        }
        assertEquals("Failed to fetch similar movies", ex.message)
    }

    @Test
    fun `getMovieImages should propagate GetMovieImagesException`() = runTest {
        coEvery { remoteDataSource.getMovieImages(123) } throws RuntimeException("Server error")

        val ex = assertThrows<GetMovieImagesException> {
                repository.getMovieImagesUsingId(123)

        }
        assertEquals("Failed to fetch movie images", ex.message)
    }

    @Test
    fun `getMovieCast should propagate GetMovieCastException`() = runTest {
        coEvery { remoteDataSource.getMovieCast(123) } throws RuntimeException("ActorRemote API down")

        val ex = assertThrows<GetMovieCastException> {
            repository.getMovieCastUsingId(123)
        }
        assertEquals("Failed to fetch movie actorRemote", ex.message)
    }
}
