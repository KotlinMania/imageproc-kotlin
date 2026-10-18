// port-lint: source pixelops.rs
package io.github.kotlinmania.imageproc.pixelops

import io.github.kotlinmania.image.Luma
import io.github.kotlinmania.image.LumaA
import io.github.kotlinmania.image.Rgb
import io.github.kotlinmania.image.Rgba
import io.github.kotlinmania.imageproc.definitions.Clamp

/**
 * Adds pixel channels with the given weights. Results are clamped to prevent arithmetical overflows.
 */
public fun weightedChannelSum(left: UByte, right: UByte, leftWeight: Float, rightWeight: Float): UByte {
    val sum = left.toFloat() * leftWeight + right.toFloat() * rightWeight
    return Clamp.clampUByte(sum)
}

public fun weightedChannelSum(left: UShort, right: UShort, leftWeight: Float, rightWeight: Float): UShort {
    val sum = left.toFloat() * leftWeight + right.toFloat() * rightWeight
    return Clamp.clampUShort(sum)
}

public fun weightedChannelSum(left: Float, right: Float, leftWeight: Float, rightWeight: Float): Float =
    left * leftWeight + right * rightWeight

public fun weightedChannelSum(left: Double, right: Double, leftWeight: Float, rightWeight: Float): Double =
    left * leftWeight.toDouble() + right * rightWeight.toDouble()

/**
 * Adds pixels with the given weights. Results are clamped to prevent arithmetical overflows.
 */
public fun weightedSum(left: Rgb<UByte>, right: Rgb<UByte>, leftWeight: Float, rightWeight: Float): Rgb<UByte> =
    Rgb(
        weightedChannelSum(left.r, right.r, leftWeight, rightWeight),
        weightedChannelSum(left.g, right.g, leftWeight, rightWeight),
        weightedChannelSum(left.b, right.b, leftWeight, rightWeight),
    )

public fun weightedSum(left: Rgba<UByte>, right: Rgba<UByte>, leftWeight: Float, rightWeight: Float): Rgba<UByte> =
    Rgba(
        weightedChannelSum(left.r, right.r, leftWeight, rightWeight),
        weightedChannelSum(left.g, right.g, leftWeight, rightWeight),
        weightedChannelSum(left.b, right.b, leftWeight, rightWeight),
        weightedChannelSum(left.a, right.a, leftWeight, rightWeight),
    )

public fun weightedSum(left: Luma<UByte>, right: Luma<UByte>, leftWeight: Float, rightWeight: Float): Luma<UByte> =
    Luma(weightedChannelSum(left.l, right.l, leftWeight, rightWeight))

public fun weightedSum(left: LumaA<UByte>, right: LumaA<UByte>, leftWeight: Float, rightWeight: Float): LumaA<UByte> =
    LumaA(
        weightedChannelSum(left.l, right.l, leftWeight, rightWeight),
        weightedChannelSum(left.a, right.a, leftWeight, rightWeight),
    )

/**
 * Equivalent to weightedSum(left, right, leftWeight, 1.0f - leftWeight).
 */
public fun interpolate(left: Rgb<UByte>, right: Rgb<UByte>, leftWeight: Float): Rgb<UByte> =
    weightedSum(left, right, leftWeight, 1.0f - leftWeight)

public fun interpolate(left: Rgba<UByte>, right: Rgba<UByte>, leftWeight: Float): Rgba<UByte> =
    weightedSum(left, right, leftWeight, 1.0f - leftWeight)

public fun interpolate(left: Luma<UByte>, right: Luma<UByte>, leftWeight: Float): Luma<UByte> =
    weightedSum(left, right, leftWeight, 1.0f - leftWeight)

public fun interpolate(left: LumaA<UByte>, right: LumaA<UByte>, leftWeight: Float): LumaA<UByte> =
    weightedSum(left, right, leftWeight, 1.0f - leftWeight)
