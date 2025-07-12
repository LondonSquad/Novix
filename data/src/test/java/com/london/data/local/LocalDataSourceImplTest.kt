package com.london.data.local

import com.london.data.datasource.local.DeleteException
import com.london.data.datasource.local.GetException
import com.london.data.datasource.local.InsertException
import com.london.data.datasource.local.UpdateException
import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.localDataSourceImpl.ActorLocalDataSourceImpl
import com.london.data.datasource.local.localDataSourceImpl.MovieLocalDataSourceImpl
import com.london.data.datasource.local.localDataSourceImpl.TvShowLocalDataSourceImpl
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
import com.london.data.datasource.util.executeGetByQuery
import com.london.data.datasource.util.generateHash
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertNull
import kotlin.test.assertFailsWith

class LocalDataSourceImplTest {

    private lateinit var searchTvShowDao: SearchTvShowDao
    private lateinit var searchMoviesDao: SearchMoviesDao
    private lateinit var searchActorsDao: SearchActorsDao

    private lateinit var actorLocalDataSource: ActorLocalDataSourceImpl
    private lateinit var movieLocalDataSource: MovieLocalDataSourceImpl
    private lateinit var tvShowLocalDataSource: TvShowLocalDataSourceImpl

    private val mockMoviesResponse = mockk<SearchMoviesLocal>()
    private val mockTvShowsResponse = mockk<SearchTvShowLocal>()
    private val mockActorsResponse = mockk<SearchActorsLocal>()
    private val testDate = 1234567890L

    @Before
    fun setup() {
        searchTvShowDao = mockk(relaxed = true)
        searchMoviesDao = mockk(relaxed = true)
        searchActorsDao = mockk(relaxed = true)

        actorLocalDataSource = ActorLocalDataSourceImpl(searchActorsDao)
        movieLocalDataSource = MovieLocalDataSourceImpl(searchMoviesDao)
        tvShowLocalDataSource = TvShowLocalDataSourceImpl(searchTvShowDao)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    //region INSERT TEST
    @Test
    fun `insertMovie should call dao insert successfully`() = runTest {
        // Given
        coEvery { searchMoviesDao.insert(mockMoviesResponse) } just Runs

        // When
        movieLocalDataSource.insert(mockMoviesResponse)

        // Then
        coVerify(exactly = 1) { searchMoviesDao.insert(mockMoviesResponse) }
    }

    @Test
    fun `insertMovie should throw InsertException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.insert(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertException> {
            movieLocalDataSource.insert(mockMoviesResponse)
        }
        coVerify(exactly = 1) { searchMoviesDao.insert(mockMoviesResponse) }
    }

    @Test
    fun `insertTvShow should call dao insert successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.insert(mockTvShowsResponse) } just Runs

        // When
        tvShowLocalDataSource.insert(mockTvShowsResponse)

        // Then
        coVerify(exactly = 1) { searchTvShowDao.insert(mockTvShowsResponse) }
    }

    @Test
    fun `insertTvShow should throw InsertException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.insert(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertException> {
            tvShowLocalDataSource.insert(mockTvShowsResponse)
        }
        coVerify(exactly = 1) { searchTvShowDao.insert(mockTvShowsResponse) }
    }

    @Test
    fun `insertActor should call dao insert successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.insert(mockActorsResponse) } just Runs

        // When
        actorLocalDataSource.insert(mockActorsResponse)

        // Then
        coVerify(exactly = 1) { searchActorsDao.insert(mockActorsResponse) }
    }

    @Test
    fun `insertActor should throw InsertException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.insert(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertException> {
            actorLocalDataSource.insert(mockActorsResponse)
        }
        coVerify(exactly = 1) { searchActorsDao.insert(mockActorsResponse) }
    }
    // endregion

    // region UPDATE TESTS
    @Test
    fun `updateMovie should call dao update successfully`() = runTest {
        // Given
        coEvery { searchMoviesDao.update(mockMoviesResponse) } just Runs

        // When
        movieLocalDataSource.update(mockMoviesResponse)

        // Then
        coVerify(exactly = 1) { searchMoviesDao.update(mockMoviesResponse) }
    }

    @Test
    fun `updateMovie should throw UpdateException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.update(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateException> {
            movieLocalDataSource.update(mockMoviesResponse)
        }
        coVerify(exactly = 1) { searchMoviesDao.update(mockMoviesResponse) }
    }

    @Test
    fun `updateTvShow should call dao update successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.update(mockTvShowsResponse) } just Runs

        // When
        tvShowLocalDataSource.update(mockTvShowsResponse)

        // Then
        coVerify(exactly = 1) { searchTvShowDao.update(mockTvShowsResponse) }
    }

    @Test
    fun `updateTvShow should throw UpdateException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.update(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateException> {
            tvShowLocalDataSource.update(mockTvShowsResponse)
        }
        coVerify(exactly = 1) { searchTvShowDao.update(mockTvShowsResponse) }
    }

    @Test
    fun `updateActor should call dao update successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.update(mockActorsResponse) } just Runs

        // When
        actorLocalDataSource.update(mockActorsResponse)

        // Then
        coVerify(exactly = 1) { searchActorsDao.update(mockActorsResponse) }
    }

    @Test
    fun `updateActor should throw UpdateException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.update(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateException> {
            actorLocalDataSource.update(mockActorsResponse)
        }
        coVerify(exactly = 1) { searchActorsDao.update(mockActorsResponse) }
    }
    // endregion

    // region DELETE TESTS
    @Test
    fun `deleteMovie should call dao delete successfully`() = runTest {
        // Given
        coEvery { searchMoviesDao.delete(mockMoviesResponse) } just Runs

        // When
        movieLocalDataSource.delete(mockMoviesResponse)

        // Then
        coVerify(exactly = 1) { searchMoviesDao.delete(mockMoviesResponse) }
    }

    @Test
    fun `deleteMovie should throw DeleteException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.delete(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteException> {
            movieLocalDataSource.delete(mockMoviesResponse)
        }
        coVerify(exactly = 1) { searchMoviesDao.delete(mockMoviesResponse) }
    }

    @Test
    fun `deleteTvShow should call dao delete successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.delete(mockTvShowsResponse) } just Runs

        // When
        tvShowLocalDataSource.delete(mockTvShowsResponse)

        // Then
        coVerify(exactly = 1) { searchTvShowDao.delete(mockTvShowsResponse) }
    }

    @Test
    fun `deleteTvShow should throw DeleteException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.delete(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteException> {
            tvShowLocalDataSource.delete(mockTvShowsResponse)
        }
        coVerify(exactly = 1) { searchTvShowDao.delete(mockTvShowsResponse) }
    }

    @Test
    fun `deleteActor should call dao delete successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.delete(mockActorsResponse) } just Runs

        // When
        actorLocalDataSource.delete(mockActorsResponse)

        // Then
        coVerify(exactly = 1) { searchActorsDao.delete(mockActorsResponse) }
    }

    @Test
    fun `deleteActor should throw DeleteException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.delete(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteException> {
            actorLocalDataSource.delete(mockActorsResponse)
        }
        coVerify(exactly = 1) { searchActorsDao.delete(mockActorsResponse) }
    }
    // endregion

    // region GET ALL TESTS
    @Test
    fun `getMovies should return movies from dao successfully`() = runTest {
        // Given
        val mockMoviesList = listOf(mockMoviesResponse)
        coEvery { searchMoviesDao.getAll() } returns mockMoviesList

        // When
        val result = movieLocalDataSource.get()

        // Then
        assertEquals(mockMoviesList, result)
        coVerify(exactly = 2) { searchMoviesDao.getAll() }
    }

    @Test
    fun `getMovies should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            movieLocalDataSource.get()
        }
        coVerify(exactly = 2) { searchMoviesDao.getAll() }
    }

    @Test
    fun `getTvShows should return tv shows from dao successfully`() = runTest {
        // Given
        val mockTvShowsList = listOf(mockTvShowsResponse)
        coEvery { searchTvShowDao.getAll() } returns mockTvShowsList

        // When
        val result = tvShowLocalDataSource.get()

        // Then
        assertEquals(mockTvShowsList, result)
        coVerify(exactly = 2) { searchTvShowDao.getAll() }
    }


    @Test
    fun `getTvShows should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            tvShowLocalDataSource.get()
        }
        coVerify(exactly = 2) { searchTvShowDao.getAll() }
    }

    @Test
    fun `getActors should return actors from dao successfully`() = runTest {
        // Given
        val mockActorsList = listOf(mockActorsResponse)
        coEvery { searchActorsDao.getAll() } returns mockActorsList

        // When
        val result = actorLocalDataSource.get()

        // Then
        assertEquals(mockActorsList, result)
        coVerify(exactly = 2) { searchActorsDao.getAll() }
    }

    @Test
    fun `getActors should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            actorLocalDataSource.get()
        }
        coVerify(exactly = 2) { searchActorsDao.getAll() }
    }
    // endregion

    // region GET BY DATE TESTS
    @Test
    fun `getMovieByDate should return movie from dao successfully`() = runTest {
        // Given
        coEvery { searchMoviesDao.getCurrentSearchByDate(testDate) } returns mockMoviesResponse

        // When
        val result = movieLocalDataSource.getByDate(testDate)

        // Then
        assertEquals(mockMoviesResponse, result)
        coVerify(exactly = 1) { searchMoviesDao.getCurrentSearchByDate(testDate) }
    }

    @Test
    fun `getMovieByDate should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.getCurrentSearchByDate(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            movieLocalDataSource.getByDate(testDate)
        }
        coVerify(exactly = 1) { searchMoviesDao.getCurrentSearchByDate(testDate) }
    }

    @Test
    fun `getTvShowByDate should return tv show from dao successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.getCurrentSearchByDate(testDate) } returns mockTvShowsResponse

        // When
        val result = tvShowLocalDataSource.getByDate(testDate)

        // Then
        assertEquals(mockTvShowsResponse, result)
        coVerify(exactly = 1) { searchTvShowDao.getCurrentSearchByDate(testDate) }
    }

    @Test
    fun `getTvShowByDate should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.getCurrentSearchByDate(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            tvShowLocalDataSource.getByDate(testDate)
        }
        coVerify(exactly = 1) { searchTvShowDao.getCurrentSearchByDate(testDate) }
    }

    @Test
    fun `getActorByDate should return actor from dao successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.getCurrentSearchByDate(testDate) } returns mockActorsResponse

        // When
        val result = actorLocalDataSource.getByDate(testDate)

        // Then
        assertEquals(mockActorsResponse, result)
        coVerify(exactly = 1) { searchActorsDao.getCurrentSearchByDate(testDate) }
    }

    @Test
    fun `getActorByDate should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.getCurrentSearchByDate(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            actorLocalDataSource.getByDate(testDate)
        }
        coVerify(exactly = 1) { searchActorsDao.getCurrentSearchByDate(testDate) }
    }
    // endregion

    // region GET BY QUERY TESTS
    @Test
    fun `getActorByQuery should return actor from dao successfully`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchActorsDao.getSearchByQuery(testQuery.generateHash()) } returns mockActorsResponse

        // When
        val result = actorLocalDataSource.getByQuery(testQuery)

        // Then
        assertEquals(mockActorsResponse, result)
        coVerify(exactly = 1) { searchActorsDao.getSearchByQuery(testQuery.generateHash()) }
    }

    @Test
    fun `getActorByQuery should return null when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchActorsDao.executeGetByQuery(testQuery.generateHash()) } throws RuntimeException(
            "Database error"
        )

        // When
        val result = actorLocalDataSource.getByQuery(testQuery)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { searchActorsDao.executeGetByQuery(testQuery.generateHash()) }
    }

    @Test
    fun `getTvShowByQuery should return tv show from dao successfully`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchTvShowDao.getSearchByQuery(testQuery.generateHash()) } returns mockTvShowsResponse

        // When
        val result = tvShowLocalDataSource.getByQuery(testQuery)

        // Then
        assertEquals(mockTvShowsResponse, result)
        coVerify(exactly = 1) { searchTvShowDao.getSearchByQuery(testQuery.generateHash()) }
    }

    @Test
    fun `getTvShowByQuery should return null when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchTvShowDao.executeGetByQuery(testQuery.generateHash()) } throws RuntimeException(
            "Database error"
        )

        // When
        val result = tvShowLocalDataSource.getByQuery(testQuery)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { searchTvShowDao.executeGetByQuery(testQuery.generateHash()) }
    }

    @Test
    fun `getMovieByQuery should return movie from dao successfully`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchMoviesDao.getSearchByQuery(testQuery.generateHash()) } returns mockMoviesResponse

        // When
        val result = movieLocalDataSource.getByQuery(testQuery)

        // Then
        assertEquals(mockMoviesResponse, result)
        coVerify(exactly = 1) { searchMoviesDao.getSearchByQuery(testQuery.generateHash()) }
    }

    @Test
    fun `getMovieByQuery should return null when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchMoviesDao.getSearchByQuery(testQuery.generateHash()) } throws RuntimeException(
            "Database error"
        )

        // When
        val result = movieLocalDataSource.getByQuery(testQuery)

        // Then
        assertNull(result)
        coVerify(exactly = 1) {
            searchMoviesDao.getSearchByQuery(testQuery.generateHash())
        }
        // endregion
    }
}