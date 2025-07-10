package com.london.data.local

import com.london.data.datasource.local.DeleteActorExpansion
import com.london.data.datasource.local.DeleteMovieExpansion
import com.london.data.datasource.local.DeleteTvShowExpansion
import com.london.data.datasource.local.GetActorByDateExpansion
import com.london.data.datasource.local.GetActorByQueryExpansion
import com.london.data.datasource.local.GetActorsAllExpansion
import com.london.data.datasource.local.GetMovieByDateExpansion
import com.london.data.datasource.local.GetMovieByQueryExpansion
import com.london.data.datasource.local.GetMoviesAllExpansion
import com.london.data.datasource.local.GetTvShowByDateExpansion
import com.london.data.datasource.local.GetTvShowByQueryExpansion
import com.london.data.datasource.local.GetTvShowsAllExpansion
import com.london.data.datasource.local.InsertActorExpansion
import com.london.data.datasource.local.InsertMovieExpansion
import com.london.data.datasource.local.InsertTvShowExpansion
import com.london.data.datasource.local.LocalDataSourceImpl
import com.london.data.datasource.local.UpdateActorExpansion
import com.london.data.datasource.local.UpdateMovieExpansion
import com.london.data.datasource.local.UpdateTvShowExpansion
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
    fun `insertMovie should throw InsertMovieExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.insert(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertMovieExpansion> {
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
    fun `insertTvShow should throw InsertTvShowExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.insert(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertTvShowExpansion> {
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
    fun `insertActor should throw InsertActorExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.insert(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<InsertActorExpansion> {
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
    fun `updateMovie should throw UpdateMovieExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.update(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateMovieExpansion> {
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
    fun `updateTvShow should throw UpdateTvShowExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.update(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateTvShowExpansion> {
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
    fun `updateActor should throw UpdateActorExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.update(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<UpdateActorExpansion> {
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
    fun `deleteMovie should throw DeleteMovieExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.delete(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteMovieExpansion> {
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
    fun `deleteTvShow should throw DeleteTvShowExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.delete(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteTvShowExpansion> {
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
    fun `deleteActor should throw DeleteActorExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.delete(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<DeleteActorExpansion> {
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
    fun `getMovies should throw GetMoviesAllExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetMoviesAllExpansion> {
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
    fun `getTvShows should throw GetTvShowsAllExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetTvShowsAllExpansion> {
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
    fun `getActors should throw GetActorsAllExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetActorsAllExpansion> {
            localDataSource.getActors()
        }
        coVerify(exactly = 1) { searchActorsDao.getAll() }
    }
    // endregion

    // region GET BY DATE TESTS
    @Test
    fun `getMovieByDate should return movie from dao successfully`() = runTest {
        // Given
        coEvery { searchMoviesDao.getCurrentSearch(testDate) } returns mockMoviesResponse

        // When
        val result = localDataSource.getMovieByDate(testDate)

        // Then
        assertEquals(mockMoviesResponse, result)
        coVerify(exactly = 1) { searchMoviesDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getMovieByDate should throw GetMovieByDateExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchMoviesDao.getCurrentSearch(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetMovieByDateExpansion> {
            localDataSource.getMovieByDate(testDate)
        }
        coVerify(exactly = 1) { searchMoviesDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getTvShowByDate should return tv show from dao successfully`() = runTest {
        // Given
        coEvery { searchTvShowDao.getCurrentSearch(testDate) } returns mockTvShowsResponse

        // When
        val result = localDataSource.getTvShowByDate(testDate)

        // Then
        assertEquals(mockTvShowsResponse, result)
        coVerify(exactly = 1) { searchTvShowDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getTvShowByDate should throw GetTvShowByDateExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchTvShowDao.getCurrentSearch(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetTvShowByDateExpansion> {
            localDataSource.getTvShowByDate(testDate)
        }
        coVerify(exactly = 1) { searchTvShowDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getActorByDate should return actor from dao successfully`() = runTest {
        // Given
        coEvery { searchActorsDao.getCurrentSearch(testDate) } returns mockActorsResponse

        // When
        val result = localDataSource.getActorByDate(testDate)

        // Then
        assertEquals(mockActorsResponse, result)
        coVerify(exactly = 1) { searchActorsDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getActorByDate should throw GetActorByDateExpansion when dao throws exception`() = runTest {
        // Given
        coEvery { searchActorsDao.getCurrentSearch(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetActorByDateExpansion> {
            localDataSource.getActorByDate(testDate)
        }
        coVerify(exactly = 1) { searchActorsDao.getCurrentSearch(testDate) }
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
    fun `getActorByQuery should throw GetActorByQueryExpansion when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchActorsDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetActorByQueryExpansion> {
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
    fun `getTvShowByQuery should throw GetTvShowByQueryExpansion when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchTvShowDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetTvShowByQueryExpansion> {
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
    fun `getMovieByQuery should throw GetMovieByQueryExpansion when dao throws exception`() = runTest {
        // Given
        val testQuery = "test query"
        coEvery { searchMoviesDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertFailsWith<GetMovieByQueryExpansion> {
            localDataSource.getMovieByQuery(testQuery) // this is a suspending call
        }

        coVerify(exactly = 1) { searchMoviesDao.getSearchByQuery(testQuery) }
    }
    // endregion
}