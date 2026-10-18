// port-lint: source kernel.rs
package io.github.kotlinmania.imageproc.kernel

/**
 * A 2D kernel, used to filter images via convolution.
 */
public class Kernel<K>(
    internal val data: List<K>,
    public val width: UInt,
    public val height: UInt,
) {
    init {
        require(width > 0u && height > 0u) { "width and height must be non-zero" }
        // Widen to ULong to prevent UInt overflow from bypassing the check.
        require(width.toULong() * height.toULong() == data.size.toULong()) {
            "kernel dimensions ($width x $height) do not match data size (${data.size})"
        }
    }

    public constructor(data: Array<K>, width: UInt, height: UInt) : this(data.toList(), width, height)

    /**
     * Get the value in the kernel at the given [x] and [y] position.
     *
     * @throws IllegalArgumentException if (x, y) is out of bounds.
     */
    public fun get(x: UInt, y: UInt): K {
        require(x < width) { "x ($x) must be < width ($width)" }
        require(y < height) { "y ($y) must be < height ($height)" }
        val at = (y * width + x).toInt()
        return data[at]
    }

    /**
     * Get the value in the kernel at the given [x] and [y] position without bounds checking.
     */
    public fun getUnchecked(x: UInt, y: UInt): K {
        val at = (y * width + x).toInt()
        return data[at]
    }

    public fun get_unchecked(x: UInt, y: UInt): K = getUnchecked(x, y)
}

/** The sobel horizontal 3x3 kernel. */
public val SOBEL_HORIZONTAL_3X3: Kernel<Int> =
    Kernel(listOf(-1, 0, 1, -2, 0, 2, -1, 0, 1), 3u, 3u)

/** The sobel vertical 3x3 kernel. */
public val SOBEL_VERTICAL_3X3: Kernel<Int> =
    Kernel(listOf(-1, -2, -1, 0, 0, 0, 1, 2, 1), 3u, 3u)

/** The scharr horizontal 3x3 kernel. */
public val SCHARR_HORIZONTAL_3X3: Kernel<Int> =
    Kernel(listOf(-3, 0, 3, -10, 0, 10, -3, 0, 3), 3u, 3u)

/** The scharr vertical 3x3 kernel. */
public val SCHARR_VERTICAL_3X3: Kernel<Int> =
    Kernel(listOf(-3, -10, -3, 0, 0, 0, 3, 10, 3), 3u, 3u)

/** The prewitt horizontal 3x3 kernel. */
public val PREWITT_HORIZONTAL_3X3: Kernel<Int> =
    Kernel(listOf(-1, 0, 1, -1, 0, 1, -1, 0, 1), 3u, 3u)

/** The prewitt vertical 3x3 kernel. */
public val PREWITT_VERTICAL_3X3: Kernel<Int> =
    Kernel(listOf(-1, -1, -1, 0, 0, 0, 1, 1, 1), 3u, 3u)

/** The roberts horizontal 2x2 kernel. */
public val ROBERTS_HORIZONTAL_2X2: Kernel<Int> =
    Kernel(listOf(1, 0, 0, -1), 2u, 2u)

/** The roberts vertical 2x2 kernel. */
public val ROBERTS_VERTICAL_2X2: Kernel<Int> =
    Kernel(listOf(0, 1, -1, 0), 2u, 2u)

/** The laplacian 3x3 kernel. */
public val LAPLACIAN_3X3: Kernel<Short> =
    Kernel(listOf(0.toShort(), 1.toShort(), 0.toShort(), 1.toShort(), (-4).toShort(), 1.toShort(), 0.toShort(), 1.toShort(), 0.toShort()), 3u, 3u)
