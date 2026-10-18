// port-lint: tests kernel.rs
package io.github.kotlinmania.imageproc.kernel

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KernelTest {
    @Test
    fun kernel_new_rejects_overflowing_dimensions() {
        // 2 * 2_147_483_649 overflows u32 to 2, matching data.len().
        // The widened u64 check must catch this.
        val data = listOf(0.0f, 0.0f)
        assertFailsWith<IllegalArgumentException> {
            Kernel(data, 2u, 2147483649u)
        }
    }

    @Test
    fun kernel_indexing() {
        val k = SOBEL_HORIZONTAL_3X3
        assertEquals(3u, k.width)
        assertEquals(3u, k.height)
        assertEquals(-1, k.get(0u, 0u))
        assertEquals(0, k.get(1u, 0u))
        assertEquals(1, k.get(2u, 0u))
        assertEquals(-2, k.get(0u, 1u))
        assertEquals(0, k.get(1u, 1u))
        assertEquals(2, k.get(2u, 1u))
        assertEquals(-1, k.get(0u, 2u))
        assertEquals(0, k.get(1u, 2u))
        assertEquals(1, k.get(2u, 2u))
    }
}
