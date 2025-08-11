package com.london.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.london.domain.entity.tvshowdetails.TvShowCastEntity
import com.london.domain.entity.tvshowdetails.TvShowCastMemberEntity
import com.london.domain.entity.tvshowdetails.TvShowRoleEntity
import com.london.domain.repository.TvShowRepository
import com.london.domain.usecase.details.tvshow.ManageTvEpisodesUseCase
import com.london.domain.repository.ActorRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetCastByIdTest {
    private lateinit var tvShowRepository: ActorRepository
    private lateinit var manageTvEpisodesUseCase: ManageTvEpisodesUseCase

    @Before
    fun setUp() {
        tvShowRepository = mockk()
        manageTvEpisodesUseCase = ManageTvEpisodesUseCase(
            repository = tvShowRepository,
        )
    }

    @Test
    fun `should return cast when repository returns cast`() = runTest {
        //given
        coEvery { tvShowRepository.getCastTvShowById(TV_SHOW_ID) } returns mockCast
        //when
        val result = manageTvEpisodesUseCase.getCastById(TV_SHOW_ID)
        //then
        assertThat(result).isEqualTo(mockCast)
    }

    private companion object {
        const val TV_SHOW_ID = 12345
        val mockCast = TvShowCastEntity(
            cast = listOf(
                TvShowCastMemberEntity(
                    id = 1,
                    name = "John Doe",
                    profileUrl = "/profile1.jpg",
                    roles = listOf(
                        TvShowRoleEntity(
                            character = "Main Character",
                            episodeCount = 24
                        )
                    ),
                ),
                TvShowCastMemberEntity(
                    id = 2,
                    name = "Jane Smith",
                    profileUrl = "/profile2.jpg",
                    roles = listOf(
                        TvShowRoleEntity(
                            character = "Supporting Character",
                            episodeCount = 18
                        )
                    ),
                )
            ),
            id = TV_SHOW_ID
        )
    }
}