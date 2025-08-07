package com.london.data.mapper.account

import com.london.data.remote.model.account.AccountInfoResponse
import com.london.data.remote.model.account.AvatarDetails
import com.london.data.remote.model.account.AvatarInfo
import org.junit.Assert.assertEquals
import org.junit.Test

class AccountMapperTest {

    @Test
    fun `toEntity should map AccountInfoResponse with nested avatar path`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "Test User",
            avatar = AvatarInfo(
                tmdb = AvatarDetails(
                    avatarPath = "/test/avatar.jpg"
                )
            )
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("Test User", result.userName)
        assertEquals("https://image.tmdb.org/t/p/w500/test/avatar.jpg", result.avatarPath)
    }

    @Test
    fun `toEntity should map AccountInfoResponse with no avatar`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "Test User",
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("Test User", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should use username when name is empty`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "",
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("testuser", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should use userName when name is null`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = null,
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("testuser", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should use userName when name is blank`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "   ",
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("testuser", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should handle real TMDB API response structure`() {
        // Given - This is what a real TMDB API response might look like
        val accountResponse = AccountInfoResponse(
            id = 12345,
            userName = "testuser",
            name = "Test User",
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(12345, result.id)
        assertEquals("Test User", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should handle avatar with null tmdb`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "Test User",
            avatar = AvatarInfo(tmdb = null)
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("Test User", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should handle avatar with null avatarPath`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "Test User",
            avatar = AvatarInfo(
                tmdb = AvatarDetails(avatarPath = null)
            )
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("Test User", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should handle empty avatarPath`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "Test User",
            avatar = AvatarInfo(
                tmdb = AvatarDetails(avatarPath = "")
            )
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("Test User", result.userName)
        assertEquals("https://image.tmdb.org/t/p/w500", result.avatarPath)
    }

    @Test
    fun `toEntity should handle userName when both name and userName are empty`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "",
            name = "",
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should handle userName when both name and userName are null`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = null,
            name = null,
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("", result.userName)
        assertEquals("", result.avatarPath)
    }

    @Test
    fun `toEntity should verify null avatar behavior`() {
        // Given
        val accountResponse = AccountInfoResponse(
            id = 1,
            userName = "testuser",
            name = "Test User",
            avatar = null
        )

        // When
        val result = accountResponse.toEntity()

        // Then
        assertEquals(1, result.id)
        assertEquals("Test User", result.userName)
        assertEquals("", result.avatarPath)
    }
}
