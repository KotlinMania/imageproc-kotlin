// port-lint: tests pixelops.rs
package io.github.kotlinmania.imageproc.pixelops

import io.github.kotlinmania.image.Rgb
import kotlin.test.Test
import kotlin.test.assertEquals

class PixelOpsTest {
    @Test
    fun testWeightedChannelSum() {
        // Midpoint
        assertEquals(15u.toUByte(), weightedChannelSum(10u.toUByte(), 20u.toUByte(), 0.5f, 0.5f))
        // Mainly left
        assertEquals(11u.toUByte(), weightedChannelSum(10u.toUByte(), 20u.toUByte(), 0.9f, 0.1f))
        // Clamped
        assertEquals(255u.toUByte(), weightedChannelSum(150u.toUByte(), 150u.toUByte(), 1.8f, 0.8f))
    }

    @Test
    fun testWeightedSumRgb() {
        val left = Rgb(10u.toUByte(), 20u.toUByte(), 30u.toUByte())
        val right = Rgb(100u.toUByte(), 80u.toUByte(), 60u.toUByte())

        val sum = weightedSum(left, right, 0.7f, 0.3f)
        assertEquals(Rgb(37u.toUByte(), 38u.toUByte(), 39u.toUByte()), sum)
    }

    @Test
    fun testInterpolateRgb() {
        val left = Rgb(10u.toUByte(), 20u.toUByte(), 30u.toUByte())
        val right = Rgb(100u.toUByte(), 80u.toUByte(), 60u.toUByte())

        val sum = interpolate(left, right, 0.7f)
        assertEquals(Rgb(37u.toUByte(), 38u.toUByte(), 39u.toUByte()), sum)
    }
}
