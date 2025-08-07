package com.london.data.utils

import com.google.common.truth.Truth.assertThat
import com.london.data.BuildConfig
import org.junit.Test

class ExtensionsKtTest {

    @Test
    fun `orZero returns zero when null`() {
        val nullableInt: Int? = null
        val nullableDouble: Double? = null

        assertThat(nullableInt.orZero()).isEqualTo(0)
        assertThat(nullableDouble.orZero()).isEqualTo(0.0)
    }

    @Test
    fun `orZero returns same value when not null`() {
        assertThat(42.orZero()).isEqualTo(42)
        assertThat(3.14.orZero()).isEqualTo(3.14)
    }

    @Test
    fun `roundToFirstDecimal formats correctly`() {
        assertThat(3.14.roundToFirstDecimal()).isEqualTo("3.1")
        assertThat(2.0.roundToFirstDecimal()).isEqualTo("2.0")
    }

    @Test
    fun `asImageUrlOrEmpty returns url when not null`() {
        val original = "/path/to/image.jpg"
        val expected = BuildConfig.IMAGE_URL + original
        assertThat(original.asImageUrlOrEmpty()).isEqualTo(expected)
    }

    @Test
    fun `asImageUrlOrEmpty returns empty string for null`() {
        val original: String? = null
        assertThat(original.asImageUrlOrEmpty()).isEmpty()
    }

    @Test
    fun `asImageUrlOrEmpty debug test`() {
        // Test null value
        val nullValue: String? = null
        val nullResult = nullValue.asImageUrlOrEmpty()
        println("nullValue.asImageUrlOrEmpty() = '$nullResult'")
        assertThat(nullResult).isEmpty()
        
        // Test empty string
        val emptyValue = ""
        val emptyResult = emptyValue.asImageUrlOrEmpty()
        println("emptyValue.asImageUrlOrEmpty() = '$emptyResult'")
        assertThat(emptyResult).isEqualTo(BuildConfig.IMAGE_URL + "")
        
        // Test actual path
        val pathValue = "/test/path.jpg"
        val pathResult = pathValue.asImageUrlOrEmpty()
        println("pathValue.asImageUrlOrEmpty() = '$pathResult'")
        assertThat(pathResult).isEqualTo(BuildConfig.IMAGE_URL + "/test/path.jpg")
    }

    @Test
    fun `asImageUrlOrEmpty returns base url for empty string`() {
        val original = ""
        val expected = BuildConfig.IMAGE_URL + original
        assertThat(original.asImageUrlOrEmpty()).isEqualTo(expected)
    }

    @Test
    fun `isTrue returns true only for true`() {
        val trueResult = true
        val falseResult = false
        val nullResult: Boolean? = null

        assertThat(trueResult.isTrue).isTrue()
        assertThat(falseResult.isTrue).isFalse()
        assertThat(nullResult.isTrue).isFalse()
    }
}