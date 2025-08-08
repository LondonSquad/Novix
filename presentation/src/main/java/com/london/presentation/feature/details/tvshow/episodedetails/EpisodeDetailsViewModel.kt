package com.london.presentation.feature.details.tvshow.episodedetails

import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetAccountTvEpisodeUseCase
import com.london.domain.usecase.GetEpisodeByTvShowId
import com.london.domain.usecase.GetEpisodeVideoProviderUseCase
import com.london.domain.usecase.GetImagesById
import com.london.domain.usecase.GetTvShowDetails
import com.london.domain.usecase.LoggedInUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val getTvShowImages: GetImagesById,
    private val getEpisodeByTvShowIdUseCase: GetEpisodeByTvShowId,
    private val getTvShowDetails: GetTvShowDetails,
    private val getVideoProvider: GetEpisodeVideoProviderUseCase,
    private val getAccountTvEpisodeUseCase: GetAccountTvEpisodeUseCase,
    private val getUserLoggedInUseCase: LoggedInUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<EpisodeDetailsUiState, EpisodeDetailsEffect>(EpisodeDetailsUiState()),
    EpisodeDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.EpisodeDetails>()
    val tvShowId = args?.tvShowId ?: 0
    private val seasonNumber = args?.seasonNumber ?: 0
    private val episodeNumber = args?.episodeNumber ?: 0

    init {
        loadEpisodeDetails()
        loadVideoProvider()
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
                        id = episode.id,
                        tvImages = images,
                        episodeGenres = tvShowDetails.tvShowGenres.map { it.name },
                        airDate = episode.airDate ?: "",
                        episodeTypes = episode.episodeTypes,
                        name = episode.name,
                        overview = episode.overview,
                        stillPath = episode.stillPath ?: "",
                        voteAverage = episode.voteAverage,
                        voteCount = episode.voteCount,
                        guestStars = episode.guestStars,
                        seasonNumber = episode.seasonNumber,
                        )
                }
            },
            onError = { errorState -> updateState { copy(error = errorState) } },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun loadVideoProvider() {
        tryToExecute(
            block = {
                val videoProviders =
                    getVideoProvider.invoke(
                        tvShowId, seasonNumber, episodeNumber
                    ).first()
                videoProviders
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { videoProvider ->
                updateState {
                    copy(
                        videoProvider = videoProvider
                    )
                }
            }
        )
    }

    fun onRetry(){
        updateState { copy(error = null) }
        loadEpisodeDetails()
        loadVideoProvider()
    }

    override fun onBackClicked() {
        emitEffect(EpisodeDetailsEffect.NavigationBack)
    }

    override fun onLoginClick() = emitEffect(EpisodeDetailsEffect.OnLoginNavigation)

    override fun onRateBottomSheetClick() {
        tryToExecute(
            block = { getUserLoggedInUseCase.invoke() },
            onSuccess = { isLoggedIn ->
                if (isLoggedIn)
                    updateState { copy(isRateBottomSheetVisible = isRateBottomSheetVisible.not()) }
                else
                    updateState {
                        copy(
                            isGuestUserBottomSheetVisible = isGuestUserBottomSheetVisible.not(),
                            isGuestUser = true
                        )

                    }
            },
            onError = { errorState ->
                updateState { copy(error = errorState) }
            }
        )
    }

    override fun onSelectRatingClick(rating: Int) {
        tryToExecute(
            onStart = { updateState { copy(isSuccessfullyRated = null,error = null) } },
            block = {
                getAccountTvEpisodeUseCase.invoke(
                    tvShowId, seasonNumber,
                    episodeNumber = episodeNumber
                )
            },
            onSuccess = {
                updateState {
                    copy(
                        selectedRating = rating,
                        isRateBottomSheetVisible = false,
                        isSuccessfullyRated = true,
                        isRated = true
                    )
                }
            },
            onError = { errorState ->
                updateState {
                    copy(
                        error = errorState,
                        isSuccessfullyRated = false
                    )
                }
            },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    override fun onNavigateToCast(actorId: Int) {
        updateState { copy( isSuccessfullyRated = null,error = null) }
        emitEffect(EpisodeDetailsEffect.NavigateToCast(actorId))
    }

}
