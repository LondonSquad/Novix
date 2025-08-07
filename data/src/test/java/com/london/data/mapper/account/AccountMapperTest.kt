package com.london.data.mapper.account

import com.london.data.remote.model.account.AccountInfoResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountInfoMapperTest {

    @Test
    fun `toEntity should map AccountInfoResponse with valid id and userName`() {
        // Given
        val response = AccountInfoResponse(
            id = 100,
            userName = "mohamed"
        )

        // When
        val result = response.toEntity()

        // Then
        assertEquals(100, result.id)
        assertEquals("mohamed", result.userName)
    }

    @Test
    fun `toEntity should map AccountInfoResponse with null userName to empty string`() {
        // Given
        val response = AccountInfoResponse(
            id = 200,
            userName = null
        )

        // When
        val result = response.toEntity()

        // Then
        assertEquals(200, result.id)
        assertEquals("", result.userName)
    }

    @Test
    fun `toEntity should map AccountInfoResponse with empty userName`() {
        // Given
        val response = AccountInfoResponse(
            id = 101,
            userName = ""
        )

        // When
        val result = response.toEntity()

        // Then
        assertEquals(101, result.id)
        assertEquals("", result.userName)
    }
}
