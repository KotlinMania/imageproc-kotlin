// port-lint: source rect.rs
package io.github.kotlinmania.imageproc.rect

/**
 * A geometrical representation of a set of 2D points with coordinate type [T].
 */
public interface Region<T> {
    /**
     * Whether this region contains the given point.
     */
    public fun contains(x: T, y: T): Boolean
}

/**
 * Position of the top left of a rectangle.
 * Only used when building a [Rect].
 */
public data class RectPosition(
    public val left: Int,
    public val top: Int,
) {
    /**
     * Construct a rectangle from a position and size. Width and height
     * are required to be strictly positive.
     */
    public fun ofSize(width: UInt, height: UInt): Rect {
        require(width > 0u) { "width must be strictly positive" }
        require(height > 0u) { "height must be strictly positive" }
        return Rect(left, top, width, height)
    }
}

/**
 * A rectangular region of non-zero width and height.
 */
@ConsistentCopyVisibility
public data class Rect internal constructor(
    private val left: Int,
    private val top: Int,
    private val width: UInt,
    private val height: UInt,
) : Region<Int> {
    /**
     * Smallest y-coordinate reached by rect.
     */
    public fun top(): Int = top

    /**
     * Smallest x-coordinate reached by rect.
     */
    public fun left(): Int = left

    /**
     * Greatest y-coordinate reached by rect.
     */
    public fun bottom(): Int = top + height.toInt() - 1

    /**
     * Greatest x-coordinate reached by rect.
     */
    public fun right(): Int = left + width.toInt() - 1

    /**
     * Width of rect.
     */
    public fun width(): UInt = width

    /**
     * Height of rect.
     */
    public fun height(): UInt = height

    /**
     * Returns the intersection of self and other, or null if they are disjoint.
     */
    public fun intersect(other: Rect): Rect? {
        val left = maxOf(this.left, other.left)
        val top = maxOf(this.top, other.top)
        val right = minOf(this.right(), other.right())
        val bottom = minOf(this.bottom(), other.bottom())

        if (right < left || bottom < top) {
            return null
        }

        val width = (right - left).toUInt() + 1u
        val height = (bottom - top).toUInt() + 1u
        return at(left, top).ofSize(width, height)
    }

    override fun contains(x: Int, y: Int): Boolean =
        this.left <= x && x <= this.right() && this.top <= y && y <= this.bottom()

    /**
     * Whether this region contains the given floating-point point.
     */
    public fun contains(x: Float, y: Float): Boolean =
        this.left.toFloat() <= x &&
            x <= this.right().toFloat() &&
            this.top.toFloat() <= y &&
            y <= this.bottom().toFloat()

    public companion object {
        /**
         * Reduces possibility of confusing coordinates and dimensions
         * when specifying rects.
         */
        public fun at(x: Int, y: Int): RectPosition = RectPosition(x, y)
    }
}
