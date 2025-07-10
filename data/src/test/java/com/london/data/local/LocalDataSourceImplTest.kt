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
import com.london.data.dto.search.SearchActorsResponse
import com.london.data.dto.search.SearchMoviesResponse
import com.london.data.dto.search.SearchTvShowsResponse
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class LocalDataSourceImplTest {

    private lateinit var searchTvShowDao: SearchTvShowDao
    private lateinit var searchMoviesDao: SearchMoviesDao
    private lateinit var searchActorsDao: SearchActorsDao
    private lateinit var localDataSource: LocalDataSourceImpl

    private val mockMoviesResponse = mockk<SearchMoviesResponse>()
    private val mockTvShowsResponse = mockk<SearchTvShowsResponse>()
    private val mockActorsResponse = mockk<SearchActorsResponse>()
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
    fun `insertMovie should call dao insert successfully`() {
        // Given
        every { searchMoviesDao.insert(mockMoviesResponse) } just Runs

        // When
        localDataSource.insertMovie(mockMoviesResponse)

        // Then
        verify(exactly = 1) { searchMoviesDao.insert(mockMoviesResponse) }
    }

    @Test
    fun `insertMovie should throw InsertMovieExpansion when dao throws exception`() {
        // Given
        every { searchMoviesDao.insert(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(InsertMovieExpansion::class.java) {
            localDataSource.insertMovie(mockMoviesResponse)
        }
        verify(exactly = 1) { searchMoviesDao.insert(mockMoviesResponse) }
    }

    @Test
    fun `insertTvShow should call dao insert successfully`() {
        // Given
        every { searchTvShowDao.insert(mockTvShowsResponse) } just Runs

        // When
        localDataSource.insertTvShow(mockTvShowsResponse)

        // Then
        verify(exactly = 1) { searchTvShowDao.insert(mockTvShowsResponse) }
    }

    @Test
    fun `insertTvShow should throw InsertTvShowExpansion when dao throws exception`() {
        // Given
        every { searchTvShowDao.insert(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(InsertTvShowExpansion::class.java) {
            localDataSource.insertTvShow(mockTvShowsResponse)
        }
        verify(exactly = 1) { searchTvShowDao.insert(mockTvShowsResponse) }
    }

    @Test
    fun `insertActor should call dao insert successfully`() {
        // Given
        every { searchActorsDao.insert(mockActorsResponse) } just Runs

        // When
        localDataSource.insertActor(mockActorsResponse)

        // Then
        verify(exactly = 1) { searchActorsDao.insert(mockActorsResponse) }
    }

    @Test
    fun `insertActor should throw InsertActorExpansion when dao throws exception`() {
        // Given
        every { searchActorsDao.insert(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(InsertActorExpansion::class.java) {
            localDataSource.insertActor(mockActorsResponse)
        }
        verify(exactly = 1) { searchActorsDao.insert(mockActorsResponse) }
    }
    // endregion

    // region UPDATE TESTS
    @Test
    fun `updateMovie should call dao update successfully`() {
        // Given
        every { searchMoviesDao.update(mockMoviesResponse) } just Runs

        // When
        localDataSource.updateMovie(mockMoviesResponse)

        // Then
        verify(exactly = 1) { searchMoviesDao.update(mockMoviesResponse) }
    }

    @Test
    fun `updateMovie should throw UpdateMovieExpansion when dao throws exception`() {
        // Given
        every { searchMoviesDao.update(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(UpdateMovieExpansion::class.java) {
            localDataSource.updateMovie(mockMoviesResponse)
        }
        verify(exactly = 1) { searchMoviesDao.update(mockMoviesResponse) }
    }

    @Test
    fun `updateTvShow should call dao update successfully`() {
        // Given
        every { searchTvShowDao.update(mockTvShowsResponse) } just Runs

        // When
        localDataSource.updateTvShow(mockTvShowsResponse)

        // Then
        verify(exactly = 1) { searchTvShowDao.update(mockTvShowsResponse) }
    }

    @Test
    fun `updateTvShow should throw UpdateTvShowExpansion when dao throws exception`() {
        // Given
        every { searchTvShowDao.update(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(UpdateTvShowExpansion::class.java) {
            localDataSource.updateTvShow(mockTvShowsResponse)
        }
        verify(exactly = 1) { searchTvShowDao.update(mockTvShowsResponse) }
    }

    @Test
    fun `updateActor should call dao update successfully`() {
        // Given
        every { searchActorsDao.update(mockActorsResponse) } just Runs

        // When
        localDataSource.updateActor(mockActorsResponse)

        // Then
        verify(exactly = 1) { searchActorsDao.update(mockActorsResponse) }
    }

    @Test
    fun `updateActor should throw UpdateActorExpansion when dao throws exception`() {
        // Given
        every { searchActorsDao.update(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(UpdateActorExpansion::class.java) {
            localDataSource.updateActor(mockActorsResponse)
        }
        verify(exactly = 1) { searchActorsDao.update(mockActorsResponse) }
    }
    // endregion

    // region DELETE TESTS
    @Test
    fun `deleteMovie should call dao delete successfully`() {
        // Given
        every { searchMoviesDao.delete(mockMoviesResponse) } just Runs

        // When
        localDataSource.deleteMovie(mockMoviesResponse)

        // Then
        verify(exactly = 1) { searchMoviesDao.delete(mockMoviesResponse) }
    }

    @Test
    fun `deleteMovie should throw DeleteMovieExpansion when dao throws exception`() {
        // Given
        every { searchMoviesDao.delete(mockMoviesResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(DeleteMovieExpansion::class.java) {
            localDataSource.deleteMovie(mockMoviesResponse)
        }
        verify(exactly = 1) { searchMoviesDao.delete(mockMoviesResponse) }
    }

    @Test
    fun `deleteTvShow should call dao delete successfully`() {
        // Given
        every { searchTvShowDao.delete(mockTvShowsResponse) } just Runs

        // When
        localDataSource.deleteTvShow(mockTvShowsResponse)

        // Then
        verify(exactly = 1) { searchTvShowDao.delete(mockTvShowsResponse) }
    }

    @Test
    fun `deleteTvShow should throw DeleteTvShowExpansion when dao throws exception`() {
        // Given
        every { searchTvShowDao.delete(mockTvShowsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(DeleteTvShowExpansion::class.java) {
            localDataSource.deleteTvShow(mockTvShowsResponse)
        }
        verify(exactly = 1) { searchTvShowDao.delete(mockTvShowsResponse) }
    }

    @Test
    fun `deleteActor should call dao delete successfully`() {
        // Given
        every { searchActorsDao.delete(mockActorsResponse) } just Runs

        // When
        localDataSource.deleteActor(mockActorsResponse)

        // Then
        verify(exactly = 1) { searchActorsDao.delete(mockActorsResponse) }
    }

    @Test
    fun `deleteActor should throw DeleteActorExpansion when dao throws exception`() {
        // Given
        every { searchActorsDao.delete(mockActorsResponse) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(DeleteActorExpansion::class.java) {
            localDataSource.deleteActor(mockActorsResponse)
        }
        verify(exactly = 1) { searchActorsDao.delete(mockActorsResponse) }
    }
    // endregion

    // region GET ALL TESTS
    @Test
    fun `getMovies should return movies from dao successfully`() {
        // Given
        every { searchMoviesDao.getAll() } returns mockMoviesResponse

        // When
        val result = localDataSource.getMovies()

        // Then
        assertEquals(mockMoviesResponse, result)
        verify(exactly = 1) { searchMoviesDao.getAll() }
    }

    @Test
    fun `getMovies should throw GetMoviesAllExpansion when dao throws exception`() {
        // Given
        every { searchMoviesDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetMoviesAllExpansion::class.java) {
            localDataSource.getMovies()
        }
        verify(exactly = 1) { searchMoviesDao.getAll() }
    }

    @Test
    fun `getTvShows should return tv shows from dao successfully`() {
        // Given
        every { searchTvShowDao.getAll() } returns mockTvShowsResponse

        // When
        val result = localDataSource.getTvShows()

        // Then
        assertEquals(mockTvShowsResponse, result)
        verify(exactly = 1) { searchTvShowDao.getAll() }
    }

    @Test
    fun `getTvShows should throw GetTvShowsAllExpansion when dao throws exception`() {
        // Given
        every { searchTvShowDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetTvShowsAllExpansion::class.java) {
            localDataSource.getTvShows()
        }
        verify(exactly = 1) { searchTvShowDao.getAll() }
    }

    @Test
    fun `getActors should return actors from dao successfully`() {
        // Given
        every { searchActorsDao.getAll() } returns mockActorsResponse

        // When
        val result = localDataSource.getActors()

        // Then
        assertEquals(mockActorsResponse, result)
        verify(exactly = 1) { searchActorsDao.getAll() }
    }

    @Test
    fun `getActors should throw GetActorsAllExpansion when dao throws exception`() {
        // Given
        every { searchActorsDao.getAll() } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetActorsAllExpansion::class.java) {
            localDataSource.getActors()
        }
        verify(exactly = 1) { searchActorsDao.getAll() }
    }
    // endregion

    // region GET BY DATE TESTS
    @Test
    fun `getMovieByDate should return movie from dao successfully`() {
        // Given
        every { searchMoviesDao.getCurrentSearch(testDate) } returns mockMoviesResponse

        // When
        val result = localDataSource.getMovieByDate(testDate)

        // Then
        assertEquals(mockMoviesResponse, result)
        verify(exactly = 1) { searchMoviesDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getMovieByDate should throw GetMovieByDateExpansion when dao throws exception`() {
        // Given
        every { searchMoviesDao.getCurrentSearch(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetMovieByDateExpansion::class.java) {
            localDataSource.getMovieByDate(testDate)
        }
        verify(exactly = 1) { searchMoviesDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getTvShowByDate should return tv show from dao successfully`() {
        // Given
        every { searchTvShowDao.getCurrentSearch(testDate) } returns mockTvShowsResponse

        // When
        val result = localDataSource.getTvShowByDate(testDate)

        // Then
        assertEquals(mockTvShowsResponse, result)
        verify(exactly = 1) { searchTvShowDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getTvShowByDate should throw GetTvShowByDateExpansion when dao throws exception`() {
        // Given
        every { searchTvShowDao.getCurrentSearch(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetTvShowByDateExpansion::class.java) {
            localDataSource.getTvShowByDate(testDate)
        }
        verify(exactly = 1) { searchTvShowDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getActorByDate should return actor from dao successfully`() {
        // Given
        every { searchActorsDao.getCurrentSearch(testDate) } returns mockActorsResponse

        // When
        val result = localDataSource.getActorByDate(testDate)

        // Then
        assertEquals(mockActorsResponse, result)
        verify(exactly = 1) { searchActorsDao.getCurrentSearch(testDate) }
    }

    @Test
    fun `getActorByDate should throw GetActorByDateExpansion when dao throws exception`() {
        // Given
        every { searchActorsDao.getCurrentSearch(testDate) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetActorByDateExpansion::class.java) {
            localDataSource.getActorByDate(testDate)
        }
        verify(exactly = 1) { searchActorsDao.getCurrentSearch(testDate) }
    }
    // endregion

    // region GET BY QUERY TESTS
    @Test
    fun `getActorByQuery should return actor from dao successfully`() {
        // Given
        val testQuery = "test query"
        every { searchActorsDao.getSearchByQuery(testQuery) } returns mockActorsResponse

        // When
        val result = localDataSource.getActorByQuery(testQuery)

        // Then
        assertEquals(mockActorsResponse, result)
        verify(exactly = 1) { searchActorsDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getActorByQuery should throw GetActorByQueryExpansion when dao throws exception`() {
        // Given
        val testQuery = "test query"
        every { searchActorsDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetActorByQueryExpansion::class.java) {
            localDataSource.getActorByQuery(testQuery)
        }
        verify(exactly = 1) { searchActorsDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getTvShowByQuery should return tv show from dao successfully`() {
        // Given
        val testQuery = "test query"
        every { searchTvShowDao.getSearchByQuery(testQuery) } returns mockTvShowsResponse

        // When
        val result = localDataSource.getTvShowByQuery(testQuery)

        // Then
        assertEquals(mockTvShowsResponse, result)
        verify(exactly = 1) { searchTvShowDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getTvShowByQuery should throw GetTvShowByQueryExpansion when dao throws exception`() {
        // Given
        val testQuery = "test query"
        every { searchTvShowDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetTvShowByQueryExpansion::class.java) {
            localDataSource.getTvShowByQuery(testQuery)
        }
        verify(exactly = 1) { searchTvShowDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getMovieByQuery should return movie from dao successfully`() {
        // Given
        val testQuery = "test query"
        every { searchMoviesDao.getSearchByQuery(testQuery) } returns mockMoviesResponse

        // When
        val result = localDataSource.getMovieByQuery(testQuery)

        // Then
        assertEquals(mockMoviesResponse, result)
        verify(exactly = 1) { searchMoviesDao.getSearchByQuery(testQuery) }
    }

    @Test
    fun `getMovieByQuery should throw GetMovieByQueryExpansion when dao throws exception`() {
        // Given
        val testQuery = "test query"
        every { searchMoviesDao.getSearchByQuery(testQuery) } throws RuntimeException("Database error")

        // When & Then
        assertThrows(GetMovieByQueryExpansion::class.java) {
            localDataSource.getMovieByQuery(testQuery)
        }
        verify(exactly = 1) { searchMoviesDao.getSearchByQuery(testQuery) }
    }
    // endregion
}