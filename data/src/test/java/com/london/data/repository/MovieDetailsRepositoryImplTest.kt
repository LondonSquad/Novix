package com.london.data.repository

import com.london.data.local.preference.AuthPreferences
import com.london.data.mapper.moviedetails.toEntity
import com.london.data.remote.exception.NetworkException
import com.london.data.remote.model.ApiResponse
import com.london.data.remote.model.details.movie.model.moviecast.MovieActor
import com.london.data.remote.model.details.movie.model.moviecast.MovieCastResponse
import com.london.data.remote.model.details.movie.model.moviedetails.GenreRemote
import com.london.data.remote.model.details.movie.model.moviedetails.MovieAccountStatesResponse
import com.london.data.remote.model.details.movie.model.moviedetails.MovieDetailsResponse
import com.london.data.remote.model.details.movie.model.moviedetails.ProductionCompanyRemote
import com.london.data.remote.model.details.movie.model.moviedetails.ProductionCountryRemote
import com.london.data.remote.model.details.movie.model.moviedetails.RatingValue
import com.london.data.remote.model.details.movie.model.moviedetails.RemoteCollectionDetails
import com.london.data.remote.model.details.movie.model.moviedetails.SpokenLanguageRemote
import com.london.data.remote.model.details.movie.model.movieimages.MovieImagesResponse
import com.london.data.remote.model.details.movie.model.movieimages.Poster
import com.london.data.remote.model.search.model.SearchMovieRemote
import com.london.data.remote.source.details.movie.MovieDetailsRemoteDataSource
import com.london.data.utils.asImageUrlOrEmpty
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class MovieDetailsRepositoryImplTest {

    private lateinit var remoteDataSource: MovieDetailsRemoteDataSource
    private lateinit var authPreferences: AuthPreferences
    private lateinit var repository: MovieDetailsRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        authPreferences = mockk(relaxed = true)
        repository = MovieDetailsRepositoryImpl(
            remoteDataSource,
            authPreferences = authPreferences
        )
    }

    private fun fakeMovieDetailsRemote() = MovieDetailsResponse(
        adult = false,
        backdropPath = "/b.jpg",
        remoteBelongsToCollection = RemoteCollectionDetails(1, "Coll"),
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
            ProductionCompanyRemote(1, null, "WB", "US")
        ),
        productionCountries = listOf(
            ProductionCountryRemote("US", "USA")
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
            SpokenLanguageRemote("English", "en", "English")
        ),
    )

    private fun fakeSimilarMoviesRemote() = ApiResponse(
        currentPage = 1,
        items = listOf(
            SearchMovieRemote(
                adult = false,
                backdropPath = null,
                genreIds = listOf(1, 2, 3),
                id = 1,
                originalLanguage = "en",
                originalTitle = "",
                overview = "",
                popularity = 0.0,
                posterPath = "",
                releaseDate = "2020-06-15",
                title = "",
                video = false,
                voteAverage = 8.0,
                voteCount = 0,
                originCountry = listOf(""),
                originalName = "",
                firstAirDate = "",
                name = "",
            ),
            SearchMovieRemote(
                adult = false,
                backdropPath = null,
                genreIds = listOf(1, 2, 3),
                id = 1,
                originalLanguage = "en",
                originalTitle = "",
                overview = "",
                popularity = 0.0,
                posterPath = "",
                releaseDate = "2020-06-15",
                title = "",
                video = false,
                voteAverage = 8.0,
                voteCount = 0,
                originCountry = listOf(""),
                originalName = "",
                firstAirDate = "",
                name = "",
            )
        ),
        totalPages = 1,
        totalItems = 1
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
    private fun fakeMovieStatesRemote(): MovieAccountStatesResponse{
        return MovieAccountStatesResponse(
            id = 123,
            favorite = true,
            rated = RatingValue(1),
            watchlist = true
        )
    }

    @Test
    fun `getMovieUsingId should map remote data correctly`() = runTest {
        coEvery { remoteDataSource.getMovieDetails(123) } returns Result.success(
            fakeMovieDetailsRemote()
        )
        coEvery { remoteDataSource.getMovieImages(123) } returns Result.success(
            fakeMovieImagesRemote()
        )
        coEvery { remoteDataSource.getSimilarMovies(123) } returns Result.success(
            fakeSimilarMoviesRemote()
        )
        coEvery { remoteDataSource.getMovieCast(123) } returns Result.success(fakeMovieCastRemote())

        val result = repository.getMovieById(123)

        assertEquals("Inception", result.title)
        assertEquals(2, result.genresId.size)
    }

    @Test
    fun `getSimilarMovies should map similar movies correctly`() = runTest {
        coEvery { remoteDataSource.getSimilarMovies(123) } returns Result.success(
            fakeSimilarMoviesRemote()
        )

        val result = repository.getSimilarMoviesById(123)

        assertEquals(2, result.size)
    }

    @Test
    fun `getMovieImages should return poster file paths`() = runTest {
        coEvery { remoteDataSource.getMovieImages(123) } returns Result.success(
            fakeMovieImagesRemote()
        )

        val result = repository.getMovieImagesById(123)

        assertEquals(
            listOf("/img1.jpg".asImageUrlOrEmpty(), "/img2.jpg".asImageUrlOrEmpty()),
            result
        )
    }

    @Test
    fun `getMovieCast should return actor list with names and characters`() = runTest {
        coEvery { remoteDataSource.getMovieCast(123) } returns Result.success(fakeMovieCastRemote())

        val result = repository.getMovieCastById(123)

        assertEquals(2, result.size)
        assertEquals("Leonardo DiCaprio", result[0].name)
        assertEquals("Cobb", result[0].characterName)
        assertEquals("Arthur", result[1].characterName)
    }

    @Test
    fun `getMovieCast should throw UnAuthorizedException when remote fails`() = runTest {
        coEvery { remoteDataSource.getMovieDetails(123) } throws
                NetworkException.UnAuthorizedException("unauthorized")

        assertThrows<NetworkException.UnAuthorizedException> {
            repository.getMovieById(123)
        }
    }

    @Test
    fun `getMovieImages should throw HttpLockedException when remote fails`() = runTest {
        coEvery { remoteDataSource.getMovieImages(123) } throws
                NetworkException.HttpLockedException("locked")

        assertThrows<NetworkException.HttpLockedException> {
            repository.getMovieImagesById(123)
        }
    }

    @Test
    fun `getMovieCast should throw ValidationException when remote fails`() = runTest {
        coEvery { remoteDataSource.getMovieCast(123) } throws
                NetworkException.ValidationException("validation error")

        assertThrows<NetworkException.ValidationException> {
            repository.getMovieCastById(123)
        }
    }

    @Test
    fun `getSimilarMovies should throw TimeoutException when remote fails`() = runTest {
        coEvery { remoteDataSource.getSimilarMovies(123) } throws
                NetworkException.TimeoutException("timeout")

        assertThrows<NetworkException.TimeoutException> {
            repository.getSimilarMoviesById(123)
        }
    }
    @Test
    fun `getMovieAccountStatesById should use userSessionId when available`() = runTest {
        val movieId = 123
        val userSessionId = "user123"
        val remoteMovieStates = fakeMovieStatesRemote()

        coEvery { authPreferences.getSessionId() } returns userSessionId
        coEvery {
            remoteDataSource.getMovieAccountStates(
                movieId = movieId,
                userSessionId = userSessionId,
                guestSessionId = null
            )
        } returns Result.success(remoteMovieStates)

        val result = repository.getAccountMovieStatesById(movieId)

        assertEquals(remoteMovieStates.toEntity(), result)
    }

    @Test
    fun `getMovieAccountStatesById should use guestSessionId when userSessionId is null`() = runTest {
        val movieId = 123
        val guestSessionId = "guest123"
        val remoteMovieStates = fakeMovieStatesRemote()

        coEvery { authPreferences.getGuestSessionId() } returns guestSessionId
        coEvery {
            remoteDataSource.getMovieAccountStates(
                movieId = movieId,
                userSessionId = null,
                guestSessionId = guestSessionId
            )
        } returns Result.success(remoteMovieStates)

        val result = repository.getAccountMovieStatesById(movieId)

        assertEquals(remoteMovieStates.toEntity(), result)
    }

    @Test
    fun `getMovieAccountStatesById should throw UnknownException when no session id`() = runTest {
        val movieId = 123

        val exception = assertThrows<NetworkException.UnknownException> {
            repository.getAccountMovieStatesById(movieId)
        }
        assertEquals("No session id found", exception.message)
    }
}
