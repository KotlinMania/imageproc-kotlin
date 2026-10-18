// port-lint: source definitions.rs
package io.github.kotlinmania.imageproc.definitions

import io.github.kotlinmania.image.Luma
import io.github.kotlinmania.image.LumaA
import io.github.kotlinmania.image.Rgb
import io.github.kotlinmania.image.Rgba

/**
 * Something with a 2d position.
 */
public interface Position {
    /** x-coordinate. */
    public fun x(): UInt

    /** y-coordinate. */
    public fun y(): UInt
}

/**
 * Something with a score.
 */
public interface Score {
    /** Score of this item. */
    public fun score(): Float
}

/**
 * Pixels which have a named Black value.
 */
public interface HasBlack<Self> {
    public fun black(): Self
}

/**
 * Pixels which have a named White value.
 */
public interface HasWhite<Self> {
    public fun white(): Self
}

/**
 * Clamping utility object.
 */
public object Clamp {
    public fun clampUByte(x: Short): UByte =
        when {
            x < 0 -> 0u
            x > 255 -> 255u
            else -> x.toUByte()
        }

    public fun clampUByte(x: Int): UByte =
        when {
            x < 0 -> 0u
            x > 255 -> 255u
            else -> x.toUByte()
        }

    public fun clampUByte(x: UInt): UByte =
        when {
            x > 255u -> 255u
            else -> x.toUByte()
        }

    public fun clampUByte(x: Float): UByte =
        when {
            x < 0f -> 0u
            x > 255f -> 255u
            else -> x.toInt().toUByte()
        }

    public fun clampUByte(x: Double): UByte =
        when {
            x < 0.0 -> 0u
            x > 255.0 -> 255u
            else -> x.toInt().toUByte()
        }

    public fun clampUShort(x: Int): UShort =
        when {
            x < 0 -> 0u
            x > 65535 -> 65535u
            else -> x.toUShort()
        }

    public fun clampUShort(x: Float): UShort =
        when {
            x < 0f -> 0u
            x > 65535f -> 65535u
            else -> x.toInt().toUShort()
        }

    public fun clampUShort(x: Double): UShort =
        when {
            x < 0.0 -> 0u
            x > 65535.0 -> 65535u
            else -> x.toInt().toUShort()
        }

    public fun clampShort(x: Int): Short =
        when {
            x < Short.MIN_VALUE -> Short.MIN_VALUE
            x > Short.MAX_VALUE -> Short.MAX_VALUE
            else -> x.toShort()
        }
}

public object LumaBlackWhite {
    public fun blackUByte(): Luma<UByte> = Luma(0u)

    public fun whiteUByte(): Luma<UByte> = Luma(255u)

    public fun blackUShort(): Luma<UShort> = Luma(0u)

    public fun whiteUShort(): Luma<UShort> = Luma(65535u)
}

public object LumaABlackWhite {
    public fun blackUByte(): LumaA<UByte> = LumaA(0u, 255u)

    public fun whiteUByte(): LumaA<UByte> = LumaA(255u, 255u)

    public fun blackUShort(): LumaA<UShort> = LumaA(0u, 65535u)

    public fun whiteUShort(): LumaA<UShort> = LumaA(65535u, 65535u)
}

public object RgbBlackWhite {
    public fun blackUByte(): Rgb<UByte> = Rgb(0u, 0u, 0u)

    public fun whiteUByte(): Rgb<UByte> = Rgb(255u, 255u, 255u)

    public fun blackUShort(): Rgb<UShort> = Rgb(0u, 0u, 0u)

    public fun whiteUShort(): Rgb<UShort> = Rgb(65535u, 65535u, 65535u)
}

public object RgbaBlackWhite {
    public fun blackUByte(): Rgba<UByte> = Rgba(0u, 0u, 0u, 255u)

    public fun whiteUByte(): Rgba<UByte> = Rgba(255u, 255u, 255u, 255u)

    public fun blackUShort(): Rgba<UShort> = Rgba(0u, 0u, 0u, 65535u)

    public fun whiteUShort(): Rgba<UShort> = Rgba(65535u, 65535u, 65535u, 65535u)
}
