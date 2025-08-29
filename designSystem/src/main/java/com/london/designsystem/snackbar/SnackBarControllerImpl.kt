package com.london.designsystem.snackbar

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SnackBarState(
    val data: SnackBarData? = null,
    val isVisible: Boolean = false
)

class SnackBarControllerImpl(
    private val coroutineScope: CoroutineScope,
    private val animationConfig: SnackBarAnimationConfig = SnackBarAnimationConfig()
) : SnackBarController {
    private val _state = MutableStateFlow(SnackBarState())
    val state = _state.asStateFlow()

    private var currentJob: Job? = null
    private val messageQueue = ArrayDeque<SnackBarData>()

    override fun showSnackBar(snackBarData: SnackBarData) {
        messageQueue.add(snackBarData)

        if (currentJob?.isActive != true) {
            processNextMessage()
        }
    }

    private fun processNextMessage() {
        if (messageQueue.isEmpty()) return

        val nextMessage = messageQueue.removeFirst()
        currentJob = coroutineScope.launch {
            _state.value = SnackBarState(data = nextMessage, isVisible = true)
            delay(nextMessage.snackbarDuration.toMillis())
            _state.value = _state.value.copy(isVisible = false)
            delay(animationConfig.durationMillis.toLong())
            _state.value = SnackBarState()

            nextMessage.onComplete()
            processNextMessage()
        }
    }
}
