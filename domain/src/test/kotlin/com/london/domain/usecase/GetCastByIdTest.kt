package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.GetCastByIdFailedException
import com.london.domain.entity.tvshowdetails.CastEntity
import com.london.domain.entity.tvshowdetails.CastMemberEntity
import com.london.domain.entity.tvshowdetails.RoleEntity
import com.london.domain.repository.DetailsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class GetCastByIdTest {
    lateinit var detailsRepository: DetailsRepository
    lateinit var getCastById: GetCastById

    @Before
    fun setUp() {
        detailsRepository = mockk()
        getCastById = GetCastById(detailsRepository)
    }

    @Test
    fun `should return cast when repository returns cast`() = runTest {
        //given
        coEvery { detailsRepository.getCastTvShowById(TV_SHOW_ID) } returns mockCast
        //when
        val result = getCastById(TV_SHOW_ID)
        //then
        assertThat(result).isEqualTo(mockCast)
    }

    @Test
    fun `should throw exception when repository throws exception`() = runTest {
        //given
        coEvery { detailsRepository.getCastTvShowById(TV_SHOW_ID) } throws GetCastByIdFailedException()
        //when & then
        assertThrows<GetCastByIdFailedException> {
            getCastById(TV_SHOW_ID)
        }
    }

    private companion object {
        const val TV_SHOW_ID = 12345
        val mockCast = CastEntity(
            cast = listOf(
                CastMemberEntity(
                    adult = false,
                    gender = 1,
                    id = 1,
                    knownForDepartment = "Acting",
                    name = "John Doe",
                    originalName = "John Doe",
                    popularity = 85.5,
                    profilePath = "/profile1.jpg",
                    roles = listOf(
                        RoleEntity(
                            creditId = "credit1",
                            character = "Main Character",
                            episodeCount = 24
                        )
                    ),
                    totalEpisodeCount = 24,
                    order = 0
                ),
                CastMemberEntity(
                    adult = false,
                    gender = 2,
                    id = 2,
                    knownForDepartment = "Acting",
                    name = "Jane Smith",
                    originalName = "Jane Smith",
                    popularity = 78.2,
                    profilePath = "/profile2.jpg",
                    roles = listOf(
                        RoleEntity(
                            creditId = "credit2",
                            character = "Supporting Character",
                            episodeCount = 18
                        )
                    ),
                    totalEpisodeCount = 18,
                    order = 1
                )
            ),
            id = TV_SHOW_ID
        )
    }
}