// port-lint: source point.rs
package io.github.kotlinmania.imageproc.point

import kotlin.jvm.JvmName
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A 2d point.
 */
public data class Point<T>(
    public val x: T,
    public val y: T,
) {
    public companion object {
        /**
         * Construct a point at (x, y).
         */
        public fun <T> new(x: T, y: T): Point<T> = Point(x, y)
    }
}

@JvmName("plusInt")
public operator fun Point<Int>.plus(other: Point<Int>): Point<Int> =
    Point(this.x + other.x, this.y + other.y)

@JvmName("minusInt")
public operator fun Point<Int>.minus(other: Point<Int>): Point<Int> =
    Point(this.x - other.x, this.y - other.y)

@JvmName("plusFloat")
public operator fun Point<Float>.plus(other: Point<Float>): Point<Float> =
    Point(this.x + other.x, this.y + other.y)

@JvmName("minusFloat")
public operator fun Point<Float>.minus(other: Point<Float>): Point<Float> =
    Point(this.x - other.x, this.y - other.y)

@JvmName("plusDouble")
public operator fun Point<Double>.plus(other: Point<Double>): Point<Double> =
    Point(this.x + other.x, this.y + other.y)

@JvmName("minusDouble")
public operator fun Point<Double>.minus(other: Point<Double>): Point<Double> =
    Point(this.x - other.x, this.y - other.y)

public fun Point<out Number>.toDouble(): Point<Double> =
    Point(this.x.toDouble(), this.y.toDouble())

public fun Point<out Number>.toInt(): Point<Int> =
    Point(this.x.toInt(), this.y.toInt())

public fun Point<out Number>.toShort(): Point<Short> =
    Point(this.x.toShort(), this.y.toShort())

/**
 * Returns the Euclidean distance between two points.
 */
public fun <T : Number> distance(p: Point<T>, q: Point<T>): Double =
    sqrt(distanceSq(p, q))

/**
 * Returns the square of the Euclidean distance between two points.
 */
public fun <T : Number> distanceSq(p: Point<T>, q: Point<T>): Double {
    val pF64 = p.toDouble()
    val qF64 = q.toDouble()
    return (pF64.x - qF64.x).pow(2.0) + (pF64.y - qF64.y).pow(2.0)
}

/**
 * A fixed rotation. This struct exists solely to cache the values of sin(theta) and cos(theta) when
 * applying a fixed rotation to multiple points.
 */
public data class Rotation(
    public val sinTheta: Double,
    public val cosTheta: Double,
) {
    public companion object {
        /**
         * A rotation of theta radians.
         */
        public fun new(theta: Double): Rotation = Rotation(sin(theta), cos(theta))
    }
}

/**
 * Rotates a point.
 */
public fun Point<Double>.rotate(rotation: Rotation): Point<Double> {
    val x = this.x * rotation.cosTheta + this.y * rotation.sinTheta
    val y = this.y * rotation.cosTheta - this.x * rotation.sinTheta
    return Point(x, y)
}

/**
 * Inverts a rotation.
 */
public fun Point<Double>.invertRotation(rotation: Rotation): Point<Double> {
    val x = this.x * rotation.cosTheta - this.y * rotation.sinTheta
    val y = this.y * rotation.cosTheta + this.x * rotation.sinTheta
    return Point(x, y)
}

/**
 * A line of the form Ax + By + C = 0.
 */
public data class Line(
    public val a: Double,
    public val b: Double,
    public val c: Double,
) {
    /**
     * Computes the shortest distance from this line to the given point.
     */
    public fun distanceFromPoint(point: Point<Double>): Double =
        abs(a * point.x + b * point.y + c) / sqrt(a.pow(2.0) + b.pow(2.0))

    public companion object {
        /**
         * Returns the Line that passes through p and q.
         */
        public fun fromPoints(p: Point<Double>, q: Point<Double>): Line {
            val a = p.y - q.y
            val b = q.x - p.x
            val c = p.x * q.y - q.x * p.y
            return Line(a, b, c)
        }
    }
}
