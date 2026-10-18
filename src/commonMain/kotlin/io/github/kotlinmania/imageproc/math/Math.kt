// port-lint: source math.rs
package io.github.kotlinmania.imageproc.math

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * Assorted mathematical helper functions.
 */

/**
 * L1 norm of a vector.
 */
public fun l1Norm(xs: FloatArray): Float {
    var acc = 0f
    for (x in xs) {
        acc += abs(x)
    }
    return acc
}

public fun l1_norm(xs: FloatArray): Float = l1Norm(xs)

/**
 * L2 norm of a vector.
 */
public fun l2Norm(xs: FloatArray): Float {
    var acc = 0f
    for (x in xs) {
        acc += x * x
    }
    return sqrt(acc)
}

public fun l2_norm(xs: FloatArray): Float = l2Norm(xs)
