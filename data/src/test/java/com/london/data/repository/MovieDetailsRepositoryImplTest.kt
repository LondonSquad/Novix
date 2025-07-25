package com.london.data.repository

import com.london.data.datasource.exception.NetworkException
import com.london.data.datasource.remote.details.moviedetails.MovieDetailsRemoteDataSource
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieActor
import com.london.data.datasource.remote.details.moviedetails.model.moviecast.MovieCastResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.GenreRemote
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.MovieDetailsResponse
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.ProductionCompanyRemote
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.ProductionCountryRemote
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.RemoteCollectionDetails
import com.london.data.datasource.remote.details.moviedetails.model.moviedetails.SpokenLanguageRemote
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.MovieImagesResponse
import com.london.data.datasource.remote.details.moviedetails.model.movieimages.Poster
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMovieRemote
import com.london.data.datasource.remote.details.moviedetails.model.similarmovies.SimilarMoviesResponse
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
    private lateinit var repository: MovieDetailsRepositoryImpl

    @Before
    fun setup() {
        remoteDataSource = mockk(relaxed = true)
        repository = MovieDetailsRepositoryImpl(remoteDataSource)
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

        assertEquals("Inception", result.originalTitle)
        assertEquals(2, result.genres.size)
        assertEquals("Sci-Fi", result.genres[0].name)
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

}
