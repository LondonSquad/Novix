package com.london.presentation.feature.details.tvshow.episode

import android.annotation.SuppressLint
import androidx.lifecycle.SavedStateHandle
import com.london.domain.usecase.GetEpisodeByTvShowId
import com.london.domain.usecase.GetEpisodeVideoProviderUseCase
import com.london.domain.usecase.GetTvShowImagesByIdUseCase
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.tvshow.ManageTvShowDetailsUseCase
import com.london.domain.usecase.rating.RatingUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val getTvShowImages: GetTvShowImagesByIdUseCase,
    private val getEpisodeByTvShowIdUseCase: GetEpisodeByTvShowId,
    private val manageTvShowDetailsUseCase: ManageTvShowDetailsUseCase,
    private val getVideoProvider: GetEpisodeVideoProviderUseCase,
    private val ratingUseCase: RatingUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<EpisodeDetailsUiState, EpisodeDetailsEffect>(EpisodeDetailsUiState()),
    EpisodeDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.EpisodeDetails>()
    val tvShowId = args?.tvShowId ?: 0
    private val seasonNumber = args?.seasonNumber ?: 0
    private val episodeNumber = args?.episodeNumber ?: 0

    init {
        loadEpisodeDetails()
        loadEpisodeRating()
        loadVideoProvider()
    }

    override fun onBackClicked() {
        emitEffect(EpisodeDetailsEffect.NavigationBack)
    }

    override fun onLoginClick() = emitEffect(EpisodeDetailsEffect.OnLoginNavigation)

    override fun onRateBottomSheetClick() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
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
            block = {
                ratingUseCase.addTvEpisodeRatingById(
                    id = tvShowId,
                    rating = rating,
                    episodeNumber = episodeNumber,
                    seasonNumber = seasonNumber
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

    @SuppressLint("SuspiciousIndentation")
    private fun loadEpisodeRating(){
       tryToExecute(
           block = {
           val data =  if (authenticationUseCase.isLoggedIn()) {
               ratingUseCase.getRateAccountTvEpisode(
                       tvShowId = tvShowId,
                       seasonNumber = seasonNumber,
                       episodeNumber =episodeNumber,
                   )
               } else 0
               data
           },
           onSuccess = { rating ->
               updateState {
                   copy(
                       isRated = rating != 0 && state.value.isGuestUser.not(),
                   )
               }
           }
       )
    }

    private fun loadEpisodeDetails() {
        tryToExecute(
            block = {
                val episode = getEpisodeByTvShowIdUseCase(
                    tvShowId, seasonNumber, episodeNumber
                )
                val images = getTvShowImages.invoke(tvShowId)
                val tvShowDetails = manageTvShowDetailsUseCase.getTvShowDetails(tvShowId)

                Triple(episode, images, tvShowDetails)
            },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { (episode, images, tvShowDetails) ->
                updateState {
                    copy(
                        tvImages = images,
                        episodeGenres = tvShowDetails.tvShowGenres.map { it.name },
                        airDate = episode.airDate.orEmpty(),
                        name = episode.name,
                        overview = episode.overview,
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

    fun onRetry() {
        updateState { copy(error = null) }
        loadEpisodeDetails()
        loadVideoProvider()
    }
}
