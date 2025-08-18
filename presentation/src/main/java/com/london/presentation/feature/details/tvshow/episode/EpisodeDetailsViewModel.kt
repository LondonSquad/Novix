package com.london.presentation.feature.details.tvshow.episode

import androidx.lifecycle.SavedStateHandle
import com.london.domain.entity.tvshowdetails.episode.EpisodeDetails
import com.london.domain.usecase.authentication.AuthenticationUseCase
import com.london.domain.usecase.details.tvshow.GetTvEpisodesUseCase
import com.london.domain.usecase.details.tvshow.GetTvShowUseCase
import com.london.domain.usecase.rating.ManageRatingUseCase
import com.london.presentation.navigation.Screen
import com.london.presentation.navigation.getArgs
import com.london.presentation.shared.base.BaseViewModel
import com.london.presentation.shared.base.ErrorState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailsViewModel @Inject constructor(
    private val getTvShowUseCase: GetTvShowUseCase,
    private val getTvEpisodesUseCase: GetTvEpisodesUseCase,
    private val ratingUseCase: ManageRatingUseCase,
    private val authenticationUseCase: AuthenticationUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<EpisodeDetailsUiState, EpisodeDetailsEffect>(EpisodeDetailsUiState()),
    EpisodeDetailsContract {

    private val args = savedStateHandle.getArgs<Screen.EpisodeDetails>()
    private val tvShowId = args?.tvShowId ?: 0
    private val seasonNumber = args?.seasonNumber ?: 0
    private val episodeNumber = args?.episodeNumber ?: 0

    init {
        loadEpisodeDetails()
        loadEpisodeRating()
        loadVideoProvider()
    }

    override fun onBackClick() = emitEffect(EpisodeDetailsEffect.BackNavigation)

    override fun onLoginClick() = emitEffect(EpisodeDetailsEffect.LoginNavigation)

    override fun onRateEpisodeClick() {
        tryToExecute(
            block = { authenticationUseCase.isLoggedIn() },
            onSuccess = { isLoggedIn -> isUserLoggedIn(isLoggedIn) },
            onError = { updateState { copy(error = it) } }
        )
    }

    override fun onSelectRatingClick(rating: Int) {
        tryToExecute(
            block = { episodeRating(rating = rating) },
            onSuccess = { updateEpisodeRating(rating = rating) },
            onError = { handelEpisodeRatingError(it) },
            onCompleted = { updateState { copy(isLoading = false) } },
        )
    }

    private fun loadEpisodeDetails() {
        tryToExecute(
            block = { loadEpisodeData() },
            onStart = ::setLoadingState,
            onSuccess = ::handleEpisodeDataLoaded,
            onError = ::handleError,
            onCompleted = ::clearLoadingState
        )
    }

    private suspend fun loadEpisodeData(): EpisodeDetailsData {
        val episode = getTvEpisodesUseCase.getEpisodeByTvShowId(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        )

        val images = getTvShowUseCase.getImagesTvShowById(tvShowId)
        val tvShowDetails = getTvShowUseCase.getTvShowDetails(tvShowId)

        return EpisodeDetailsData(
            episode = episode,
            images = images,
            genres = tvShowDetails.tvShowGenres.map { it.name }
        )
    }

    private fun setLoadingState() {
        updateState { copy(isLoading = true) }
    }

    private fun handleEpisodeDataLoaded(data: EpisodeDetailsData) {
        updateState {
            copy(
                images = data.images,
                episode = data.episode,
                episodeGenres = data.genres
            )
        }
    }

    private fun handleError(errorState: ErrorState) {
        updateState { copy(error = errorState) }
    }

    private fun clearLoadingState() {
        updateState { copy(isLoading = false) }
    }

//    // Data class to encapsulate the loaded data
    data class EpisodeDetailsData(
    val episode: EpisodeDetails,
    val images: List<String>,
    val genres: List<String>
    )

    private fun loadVideoProvider() {
        tryToExecute(
            block = { loadEpisodeVideo() },
            onStart = { updateState { copy(isLoading = true) } },
            onSuccess = { updateState { copy(videoProvider = videoProvider) } }
        )
    }

    private fun isUserLoggedIn(isLoggedIn: Boolean) {
        if (isLoggedIn)
            updateState { copy(isRateBottomSheetVisible = isRateBottomSheetVisible.not()) }
        else
            updateState {
                copy(
                    isGuestUser = true,
                    isGuestUserBottomSheetVisible = isGuestUserBottomSheetVisible.not()
                )
            }
    }

    private suspend fun episodeRating(rating: Int) {
        ratingUseCase.addTvEpisodeRatingById(
            id = tvShowId,
            rating = rating,
            episodeNumber = episodeNumber,
            seasonNumber = seasonNumber
        )
    }

    private fun updateEpisodeRating(rating: Int) {
        updateState {
            copy(
                isRated = true,
                selectedRating = rating,
                isSuccessfullyRated = true,
                isRateBottomSheetVisible = false
            )
        }
    }

    private fun handelEpisodeRatingError(errorState: ErrorState?) {
        updateState {
            copy(
                error = errorState,
                isSuccessfullyRated = false
            )
        }
    }

    private suspend fun loadEpisodeVideo() {
        getTvEpisodesUseCase.getEpisodeVideos(
            tvShowId = tvShowId,
            seasonNumber = seasonNumber,
            episodeNumber = episodeNumber
        ).first()
    }

    private fun loadEpisodeRatingSuccess(rating: Int) {
        updateState {
            copy(isRated = rating != DEFAULT_RATING && !isGuestUser)
        }
    }

    private fun loadEpisodeRating() {
        tryToExecute(
            block = { fetchCurrentEpisodeRating() },
            onSuccess = ::loadEpisodeRatingSuccess
        )
    }

    private suspend fun fetchCurrentEpisodeRating(): Int {
        return if (authenticationUseCase.isLoggedIn()) {
            ratingUseCase.getRateAccountTvEpisode(
                tvShowId = tvShowId,
                seasonNumber = seasonNumber,
                episodeNumber = episodeNumber,
            )
        } else {
            DEFAULT_RATING
        }
    }

    fun onRetry() {
        updateState { copy(error = null) }
        loadEpisodeDetails()
        loadVideoProvider()
    }

    private companion object {
        const val DEFAULT_RATING = 0
    }
}
