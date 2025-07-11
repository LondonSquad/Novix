package com.london.data.local

import com.london.data.datasource.local.DeleteException
import com.london.data.datasource.local.GetException
import com.london.data.datasource.local.InsertException
import com.london.data.datasource.local.LocalDataSourceImpl
import com.london.data.datasource.local.UpdateException
import com.london.data.datasource.local.dao.SearchActorsDao
import com.london.data.datasource.local.dao.SearchMoviesDao
import com.london.data.datasource.local.dao.SearchTvShowDao
import com.london.data.datasource.local.model.SearchActorsLocal
import com.london.data.datasource.local.model.SearchMoviesLocal
import com.london.data.datasource.local.model.SearchTvShowLocal
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
import kotlin.test.assertFailsWith

class LocalDataSourceImplTest {

    private lateinit var searchTvShowDao: SearchTvShowDao
    private lateinit var searchMoviesDao: SearchMoviesDao
    private lateinit var searchActorsDao: SearchActorsDao
    private lateinit var localDataSource: LocalDataSourceImpl

    private val mockMoviesResponse = mockk<SearchMoviesLocal>()
    private val mockTvShowsResponse = mockk<SearchTvShowLocal>()
    private val mockActorsResponse = mockk<SearchActorsLocal>()
    private val testDate = 1234567890L

    @Before
    fun setup() {
        searchTvShowDao = mockk()
        searchMoviesDao = mockk()
        searchActorsDao = mockk()

        localDataSource = LocalDataSourceImpl(
            searchTvShowDao = searchTvShowDao,
            searchMoviesDao = searchMoviesDao,
            searchActorsDao = searchActorsDao
        )
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
        localDataSource.insertMovie(mockMoviesResponse)

        // Then
        coVerify(exactly = 1) { searchMoviesDao.insert(mockMoviesResponse) }
    }

    @Test
    fun `insertMovie should throw InsertException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.insert(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertException> {
            localDataSource.insertMovie(mockMoviesResponse)
        }
        coVerify(exactly = 1) { searchMoviesDao.insert(mockMoviesResponse) }
    }

    @Test
    fun `insertTvShow should call dao insert successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.insert(mockTvShowsResponse) } just Runs

        // When
        localDataSource.insertTvShow(mockTvShowsResponse)

        // Then
        coVerify(exactly = 1) { searchTvShowDao.insert(mockTvShowsResponse) }
    }

    @Test
    fun `insertTvShow should throw InsertException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.insert(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertException> {
            localDataSource.insertTvShow(mockTvShowsResponse)
        }
        coVerify(exactly = 1) { searchTvShowDao.insert(mockTvShowsResponse) }
    }

    @Test
    fun `insertActor should call dao insert successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.insert(mockActorsResponse) } just Runs

        // When
        localDataSource.insertActor(mockActorsResponse)

        // Then
        coVerify(exactly = 1) { searchActorsDao.insert(mockActorsResponse) }
    }

    @Test
    fun `insertActor should throw InsertException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.insert(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertException> {
            localDataSource.insertActor(mockActorsResponse)
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
        localDataSource.updateMovie(mockMoviesResponse)

        // Then
        coVerify(exactly = 1) { searchMoviesDao.update(mockMoviesResponse) }
    }

    @Test
    fun `updateMovie should throw UpdateException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.update(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateException> {
            localDataSource.updateMovie(mockMoviesResponse)
        }
        coVerify(exactly = 1) { searchMoviesDao.update(mockMoviesResponse) }
    }

    @Test
    fun `updateTvShow should call dao update successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.update(mockTvShowsResponse) } just Runs

        // When
        localDataSource.updateTvShow(mockTvShowsResponse)

        // Then
        coVerify(exactly = 1) { searchTvShowDao.update(mockTvShowsResponse) }
    }

    @Test
    fun `updateTvShow should throw UpdateException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.update(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateException> {
            localDataSource.updateTvShow(mockTvShowsResponse)
        }
        coVerify(exactly = 1) { searchTvShowDao.update(mockTvShowsResponse) }
    }

    @Test
    fun `updateActor should call dao update successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.update(mockActorsResponse) } just Runs

        // When
        localDataSource.updateActor(mockActorsResponse)

        // Then
        coVerify(exactly = 1) { searchActorsDao.update(mockActorsResponse) }
    }

    @Test
    fun `updateActor should throw UpdateException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.update(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateException> {
            localDataSource.updateActor(mockActorsResponse)
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
        localDataSource.deleteMovie(mockMoviesResponse)

        // Then
        coVerify(exactly = 1) { searchMoviesDao.delete(mockMoviesResponse) }
    }

    @Test
    fun `deleteMovie should throw DeleteException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.delete(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteException> {
            localDataSource.deleteMovie(mockMoviesResponse)
        }
        coVerify(exactly = 1) { searchMoviesDao.delete(mockMoviesResponse) }
    }

    @Test
    fun `deleteTvShow should call dao delete successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.delete(mockTvShowsResponse) } just Runs

        // When
        localDataSource.deleteTvShow(mockTvShowsResponse)

        // Then
        coVerify(exactly = 1) { searchTvShowDao.delete(mockTvShowsResponse) }
    }

    @Test
    fun `deleteTvShow should throw DeleteException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.delete(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteException> {
            localDataSource.deleteTvShow(mockTvShowsResponse)
        }
        coVerify(exactly = 1) { searchTvShowDao.delete(mockTvShowsResponse) }
    }

    @Test
    fun `deleteActor should call dao delete successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.delete(mockActorsResponse) } just Runs

        // When
        localDataSource.deleteActor(mockActorsResponse)

        // Then
        coVerify(exactly = 1) { searchActorsDao.delete(mockActorsResponse) }
    }

    @Test
    fun `deleteActor should throw DeleteException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.delete(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteException> {
            localDataSource.deleteActor(mockActorsResponse)
        }
        coVerify(exactly = 1) { searchActorsDao.delete(mockActorsResponse) }
    }
    // endregion

    // region GET ALL TESTS
    @Test
    fun `getMovies should return movies from dao successfully`() = runTest {
        // Given
        coEvery { searchMoviesDao.getAll() } returns mockMoviesResponse

        // When
        val result = localDataSource.getMovies()

        // Then
        assertEquals(mockMoviesResponse, result)
        coVerify(exactly = 1) { searchMoviesDao.getAll() }
    }

    @Test
    fun `getMovies should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            localDataSource.getMovies()
        }
        coVerify(exactly = 1) { searchMoviesDao.getAll() }
    }

    @Test
    fun `getTvShows should return tv shows from dao successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.getAll() } returns mockTvShowsResponse

        // When
        val result = localDataSource.getTvShows()

        // Then
        assertEquals(mockTvShowsResponse, result)
        coVerify(exactly = 1) { searchTvShowDao.getAll() }
    }

    @Test
    fun `getTvShows should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            localDataSource.getTvShows()
        }
        coVerify(exactly = 1) { searchTvShowDao.getAll() }
    }

    @Test
    fun `getActors should return actors from dao successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.getAll() } returns mockActorsResponse

        // When
        val result = localDataSource.getActors()

        // Then
        assertEquals(mockActorsResponse, result)
        coVerify(exactly = 1) { searchActorsDao.getAll() }
    }

    @Test
    fun `getActors should throw GetException when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            localDataSource.getActors()
        }
        coVerify(exactly = 1) { searchActorsDao.getAll() }
    }
    // endregion

    // region GET BY DATE TESTS
    @Test
    fun `getMovieByDate should return movie from dao successfully`() = runTest {
        // Given
        coEvery { searchMoviesDao.getCurrentSearchByDate(testDate) } returns mockMoviesResponse

        // When
        val result = localDataSource.getMovieByDate(testDate)

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
            localDataSource.getMovieByDate(testDate)
        }
        coVerify(exactly = 1) { searchMoviesDao.getCurrentSearchByDate(testDate) }
    }

    @Test
    fun `getTvShowByDate should return tv show from dao successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.getCurrentSearchByDate(testDate) } returns mockTvShowsResponse

        // When
        val result = localDataSource.getTvShowByDate(testDate)

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
            localDataSource.getTvShowByDate(testDate)
        }
        coVerify(exactly = 1) { searchTvShowDao.getCurrentSearchByDate(testDate) }
    }

    @Test
    fun `getActorByDate should return actor from dao successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.getCurrentSearchByDate(testDate) } returns mockActorsResponse

        // When
        val result = localDataSource.getActorByDate(testDate)

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
            localDataSource.getActorByDate(testDate)
        }
        coVerify(exactly = 1) { searchActorsDao.getCurrentSearchByDate(testDate) }
    }
    // endregion

    // region GET BY QUERY TESTS
    @Test
    fun `getActorByQuery should return actor from dao successfully`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchActorsDao.getSearchByQuery(testQuery) } returns mockActorsResponse

        // When
        val result = localDataSource.getActorByQuery(testQuery)

        // Then
        assertEquals(mockActorsResponse, result)
        coVerify(exactly = 1) { searchActorsDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getActorByQuery should throw GetException when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchActorsDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            localDataSource.getActorByQuery(testQuery)
        }
        coVerify(exactly = 1) { searchActorsDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getTvShowByQuery should return tv show from dao successfully`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchTvShowDao.getSearchByQuery(testQuery) } returns mockTvShowsResponse

        // When
        val result = localDataSource.getTvShowByQuery(testQuery)

        // Then
        assertEquals(mockTvShowsResponse, result)
        coVerify(exactly = 1) { searchTvShowDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getTvShowByQuery should throw GetException when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchTvShowDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            localDataSource.getTvShowByQuery(testQuery)
        }
        coVerify(exactly = 1) { searchTvShowDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getMovieByQuery should return movie from dao successfully`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchMoviesDao.getSearchByQuery(testQuery) } returns mockMoviesResponse

        // When
        val result = localDataSource.getMovieByQuery(testQuery)

        // Then
        assertEquals(mockMoviesResponse, result)
        coVerify(exactly = 1) { searchMoviesDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getMovieByQuery should throw GetException when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchMoviesDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetException> {
            localDataSource.getMovieByQuery(testQuery) // this is a suspending call
        }

        coVerify(exactly = 1) { searchMoviesDao.getSearchByQuery(testQuery) }
    }
    // endregion
}