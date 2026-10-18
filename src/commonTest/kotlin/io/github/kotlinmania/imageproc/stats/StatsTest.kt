// port-lint: tests stats.rs
package io.github.kotlinmania.imageproc.stats

import io.github.kotlinmania.image.images.GrayImage
import io.github.kotlinmania.image.images.ImageBuffer
import io.github.kotlinmania.image.images.RgbImage
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class StatsTest {
    private fun makeGray(width: UInt, height: UInt, vararg values: Int): GrayImage {
        val buf = ByteArray(values.size) { values[it].toByte() }
        return ImageBuffer.createGray(width, height, buf) ?: error("Failed to create gray image")
    }

    private fun makeRgb(width: UInt, height: UInt, vararg values: Int): RgbImage {
        val buf = ByteArray(values.size) { values[it].toByte() }
        return ImageBuffer.createRgb(width, height, buf) ?: error("Failed to create rgb image")
    }

    @Test
    fun test_range() {
        val image =
            makeRgb(
                5u,
                1u,
                1,
                10,
                0,
                2,
                20,
                3,
                3,
                30,
                255,
                2,
                20,
                7,
                1,
                10,
                8,
            )

        assertEquals(
            listOf(
                MinMax(1u.toUByte(), 3u.toUByte()),
                MinMax(10u.toUByte(), 30u.toUByte()),
                MinMax(0u.toUByte(), 255u.toUByte()),
            ),
            minMax(image),
        )
    }

    @Test
    fun test_cumulative_histogram() {
        val image = makeGray(5u, 1u, 1, 2, 3, 2, 1)
        val hist = cumulativeHistogram(image).channels[0]

        assertEquals(0u, hist[0])
        assertEquals(2u, hist[1])
        assertEquals(4u, hist[2])
        assertEquals(5u, hist[3])
        for (i in 4 until 256) {
            assertEquals(5u, hist[i])
        }
    }

    @Test
    fun test_cumulative_histogram_rgb() {
        val image =
            makeRgb(
                5u,
                1u,
                1,
                10,
                1,
                2,
                20,
                2,
                3,
                30,
                3,
                2,
                20,
                2,
                1,
                10,
                1,
            )

        val hist = cumulativeHistogram(image)
        val r = hist.channels[0]
        val g = hist.channels[1]
        val b = hist.channels[2]

        assertEquals(0u, r[0])
        assertEquals(2u, r[1])
        assertEquals(4u, r[2])
        assertEquals(5u, r[3])
        for (i in 4 until 256) {
            assertEquals(5u, r[i])
        }

        for (i in 0 until 10) {
            assertEquals(0u, g[i])
        }
        for (i in 10 until 20) {
            assertEquals(2u, g[i])
        }
        for (i in 20 until 30) {
            assertEquals(4u, g[i])
        }
        assertEquals(5u, g[30])
        for (i in 30 until 256) {
            assertEquals(5u, b[i])
        }

        assertEquals(0u, b[0])
        assertEquals(2u, b[1])
        assertEquals(4u, b[2])
        assertEquals(5u, b[3])
        for (i in 4 until 256) {
            assertEquals(5u, b[i])
        }
    }

    @Test
    fun test_histogram() {
        val image = makeGray(5u, 1u, 1, 2, 3, 2, 1)
        val hist = histogram(image).channels[0]

        assertEquals(0u, hist[0])
        assertEquals(2u, hist[1])
        assertEquals(2u, hist[2])
        assertEquals(1u, hist[3])
    }

    @Test
    fun test_histogram_rgb() {
        val image =
            makeRgb(
                5u,
                1u,
                1,
                10,
                1,
                2,
                20,
                2,
                3,
                30,
                3,
                2,
                20,
                2,
                1,
                10,
                1,
            )

        val hist = histogram(image)
        val r = hist.channels[0]
        val g = hist.channels[1]
        val b = hist.channels[2]

        assertEquals(0u, r[0])
        assertEquals(2u, r[1])
        assertEquals(2u, r[2])
        assertEquals(1u, r[3])
        for (i in 4 until 256) {
            assertEquals(0u, r[i])
        }

        for (i in 0 until 10) {
            assertEquals(0u, g[i])
        }
        assertEquals(2u, g[10])
        for (i in 11 until 20) {
            assertEquals(0u, g[i])
        }
        assertEquals(2u, g[20])
        for (i in 21 until 30) {
            assertEquals(0u, g[i])
        }
        assertEquals(1u, g[30])
        for (i in 31 until 256) {
            assertEquals(0u, b[i])
        }

        assertEquals(0u, b[0])
        assertEquals(2u, b[1])
        assertEquals(2u, b[2])
        assertEquals(1u, b[3])
        for (i in 4 until 256) {
            assertEquals(0u, b[i])
        }
    }

    @Test
    fun test_percentile_boundaries_and_repeated_values() {
        val image = makeGray(4u, 1u, 1, 1, 2, 4)

        assertEquals(0u.toUByte(), percentile(image, 0u.toUByte()))
        assertEquals(1u.toUByte(), percentile(image, 25u.toUByte()))
        assertEquals(1u.toUByte(), percentile(image, 50u.toUByte()))
        assertEquals(2u.toUByte(), percentile(image, 51u.toUByte()))
        assertEquals(4u.toUByte(), percentile(image, 100u.toUByte()))
    }

    @Test
    fun test_percentile_rejects_values_above_one_hundred() {
        val image = makeGray(3u, 1u, 1, 2, 3)
        assertFailsWith<IllegalArgumentException> {
            percentile(image, 101u.toUByte())
        }
    }

    @Test
    fun test_root_mean_squared_error_grayscale() {
        val left = makeGray(3u, 2u, 1, 2, 3, 4, 5, 6)
        val right = makeGray(3u, 2u, 8, 4, 7, 6, 9, 1)

        val rms = rootMeanSquaredError(left, right)
        val expected = sqrt(114.0 / 6.0)
        assertEquals(expected, rms)
    }

    @Test
    fun test_root_mean_squared_error_rgb() {
        val left = makeRgb(2u, 1u, 1, 2, 3, 4, 5, 6)
        val right = makeRgb(2u, 1u, 8, 4, 7, 6, 9, 1)

        val rms = rootMeanSquaredError(left, right)
        val expected = sqrt(114.0 / 6.0)
        assertEquals(expected, rms)
    }

    @Test
    fun test_peak_signal_to_noise_ratio_for_known_error() {
        val original = makeGray(4u, 1u, 0, 10, 20, 30)
        val noisy = makeGray(4u, 1u, 1, 11, 21, 31)

        val psnr = peakSignalToNoiseRatio(original, noisy)
        val expected = 20.0 * log10(255.0)

        assertTrue(abs(psnr - expected) < 1e-12)
    }

    @Test
    fun test_peak_signal_to_noise_ratio_of_identical_images_is_infinite() {
        val image = makeRgb(2u, 1u, 1, 2, 3, 4, 5, 6)

        val psnr = peakSignalToNoiseRatio(image, image)

        assertTrue(psnr.isInfinite())
        assertTrue(psnr > 0.0)
    }

    @Test
    fun test_peak_signal_to_noise_ratio_rejects_mismatched_dimensions() {
        val original = makeGray(2u, 1u, 1, 2)
        val noisy = makeGray(1u, 2u, 1, 2)
        assertFailsWith<IllegalArgumentException> {
            peakSignalToNoiseRatio(original, noisy)
        }
    }

    @Test
    fun test_root_mean_squares_rejects_mismatched_dimensions() {
        val left = makeGray(2u, 1u, 1, 2)
        val right = makeGray(1u, 2u, 8, 4)
        assertFailsWith<IllegalArgumentException> {
            rootMeanSquaredError(left, right)
        }
    }
}
