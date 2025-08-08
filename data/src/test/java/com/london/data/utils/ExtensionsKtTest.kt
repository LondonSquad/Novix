package com.london.data.utils

import com.google.common.truth.Truth.assertThat
import com.london.data.BuildConfig
import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun `orZero returns zero when null`() {
        // Given
        val nullableInt: Int? = null
        val nullableDouble: Double? = null
        val nullableLong: Long? = null

        // When & Then
        assertThat(nullableInt.orZero()).isEqualTo(0)
        assertThat(nullableDouble.orZero()).isEqualTo(0.0)
        assertThat(nullableLong.orZero()).isEqualTo(0L)
    }

    @Test
    fun `orZero returns same value when not null`() {
        // Given
        val intValue = 42
        val doubleValue = 3.14
        val longValue = 123L

        // When & Then
        assertThat(intValue.orZero()).isEqualTo(42)
        assertThat(doubleValue.orZero()).isEqualTo(3.14)
        assertThat(longValue.orZero()).isEqualTo(123L)
    }

    @Test
    fun `roundToFirstDecimal formats correctly when positive value`() {
        // Given
        val value = 3.14

        // When
        val result = value.roundToFirstDecimal()

        // Then
        assertThat(result).isEqualTo("3.1")
    }

    @Test
    fun `roundToFirstDecimal formats correctly when negative value`() {
        // Given
        val value = -1.5

        // When
        val result = value.roundToFirstDecimal()

        // Then
        assertThat(result).isEqualTo("-1.5")
    }

    @Test
    fun `roundToFirstDecimal formats correctly when zero`() {
        // Given
        val value = 0.0

        // When
        val result = value.roundToFirstDecimal()

        // Then
        assertThat(result).isEqualTo("0.0")
    }

    @Test
    fun `roundToDecimal converts correctly when positive value`() {
        // Given
        val value = 3.14

        // When
        val result = value.roundToDecimal()

        // Then
        assertThat(result).isEqualTo(3.1)
    }

    @Test
    fun `roundToDecimal converts correctly when negative value`() {
        // Given
        val value = -1.5

        // When
        val result = value.roundToDecimal()

        // Then
        assertThat(result).isEqualTo(-1.5)
    }

    @Test
    fun `roundToDecimal converts correctly when zero`() {
        // Given
        val value = 0.0

        // When
        val result = value.roundToDecimal()

        // Then
        assertThat(result).isEqualTo(0.0)
    }

    @Test
    fun `asImageUrlOrEmpty returns url when path is not null`() {
        // Given
        val original = "/path/to/image.jpg"
        val expected = BuildConfig.IMAGE_URL + original

        // When
        val result = original.asImageUrlOrEmpty()

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `asImageUrlOrEmpty returns empty string when path is null`() {
        // Given
        val original: String? = null

        // When
        val result = original.asImageUrlOrEmpty()

        // Then
        assertThat(result).isEmpty()
    }

    @Test
    fun `asImageUrlOrEmpty returns base url when path is empty string`() {
        // Given
        val original = ""
        val expected = BuildConfig.IMAGE_URL + original

        // When
        val result = original.asImageUrlOrEmpty()

        // Then
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `isTrue returns true when boolean is true`() {
        // Given
        val booleanValue = true

        // When
        val result = booleanValue.isTrue

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `isTrue returns false when boolean is false`() {
        // Given
        val booleanValue = false

        // When
        val result = booleanValue.isTrue

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `isTrue returns false when boolean is null`() {
        // Given
        val booleanValue: Boolean? = null

        // When
        val result = booleanValue.isTrue

        // Then
        assertThat(result).isFalse()
    }
}