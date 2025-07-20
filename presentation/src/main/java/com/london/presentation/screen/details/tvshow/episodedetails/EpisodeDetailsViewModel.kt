package com.london.presentation.screen.details.tvshow.episodedetails

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetEpisodeByTvShowId
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.presentation.features.base.BaseViewModel
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EpisodeDetailsViewModel(
    private val getTvShowImages: GetImagesById,
    private val getEpisodeByTvShowIdUseCase: GetEpisodeByTvShowId,
    private val getTvShowDetails: GetTvShowDetails,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<EpisodeDetailsUiState, EpisodeDetailsEffect>(EpisodeDetailsUiState()), EpisodeDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.EpisodeDetails>()
    val tvShowId = args?.tvShowId ?: 0
    val seasonNumber = args?.seasonNumber ?: 0
    val episodeNumber = args?.episodeNumber ?: 0

    init {
        loadEpisodeDetails()
    }

    private fun loadEpisodeDetails() {
        tryToExecute(
            block = {
                val episode = getEpisodeByTvShowIdUseCase(
                    tvShowId, seasonNumber, episodeNumber
                )
                val images = getTvShowImages(tvShowId)
                val tvShowDetails = getTvShowDetails(tvShowId)

                Triple(episode, images, tvShowDetails)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { (episode, images, tvShowDetails) ->
                updateState {
                    copy(
                        tvImages = images,
                        episodeGenres = tvShowDetails.tvShowGenres.map { it.name },
                        airDate = episode.airDate ?: "",
                        episodeTypes = episode.episodeTypes,
                        name = episode.name,
                        overview = episode.overview,
                        stillPath = episode.stillPath ?: "",
                        voteAverage = episode.voteAverage,
                        voteCount = episode.voteCount,
                        guestStars = episode.guestStars
                    )
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onBackClicked() {
        emitEffect(EpisodeDetailsEffect.NavigationBack)
    }
}
