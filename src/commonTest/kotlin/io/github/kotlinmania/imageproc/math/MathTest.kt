// port-lint: tests math.rs
package io.github.kotlinmania.imageproc.math

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MathTest {
    @Test
    fun test_l1_norm() {
        assertEquals(0.0f, l1Norm(floatArrayOf()))
        assertEquals(6.5f, l1Norm(floatArrayOf(1.0f, -2.0f, 3.5f)))
    }

    @Test
    fun test_l2_norm() {
        assertEquals(0.0f, l2Norm(floatArrayOf()))
        assertTrue(abs(l2Norm(floatArrayOf(3.0f, 4.0f)) - 5.0f) < 1e-6f)
        assertTrue(abs(l2Norm(floatArrayOf(1.0f, -2.0f, 2.0f)) - 3.0f) < 1e-6f)
    }
}
