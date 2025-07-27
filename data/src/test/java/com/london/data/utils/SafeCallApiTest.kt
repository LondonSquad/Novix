package com.london.data.utils

import com.google.common.truth.Truth.assertThat
import com.london.domain.error.NetworkException
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SafeCallApiTest {

    @Test
    fun `when call safeCallApi should returns data on success`() = runTest {
        // Given
        val expected = "success"

        // When
        val result = safeCallApi { expected }

        // Then
        assertThat(result).isEqualTo("success")
    }

    @Test
    fun `when call safeCallApi should throws RuntimeException on 401 error`() = runTest {
        val httpException = createHttpException(401)

        val exception = runCatching {
            safeCallApi<String> { throw httpException }
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(RuntimeException::class.java)
        assertThat(exception?.message).isEqualTo("Unauthorized access")
    }

    @Test
    fun `when call safeCallApi should throws RuntimeException on unknown HTTP error`() = runTest {
        val httpException = createHttpException(418)

        val exception = runCatching {
            safeCallApi<String> { throw httpException }
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(RuntimeException::class.java)
        assertThat(exception?.message).isEqualTo("HTTP error: 418")
    }

    @Test
    fun `when call safeCallApi should throws NoInternetException on IOException`() = runTest {
        val ioException = IOException("timeout")

        val exception = runCatching {
            safeCallApi<String> { throw ioException }
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(NetworkException.NoInternetException::class.java)
        assertThat(exception?.message).contains("Network connection failed")
    }

    companion object {
        private fun createHttpException(code: Int): HttpException {
            val errorResponse = Response.error<String>(
                code,
                "error".toResponseBody(null)
            )
            return HttpException(errorResponse)
        }
    }
}
