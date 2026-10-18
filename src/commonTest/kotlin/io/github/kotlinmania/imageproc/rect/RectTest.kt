// port-lint: tests rect.rs
package io.github.kotlinmania.imageproc.rect

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RectTest {
    @Test
    fun testRejectsEmptyRectangle() {
        assertFailsWith<IllegalArgumentException> {
            Rect.at(1, 2).ofSize(0u, 1u)
        }
    }

    @Test
    fun testContainsInt() {
        val r = Rect.at(5, 5).ofSize(6u, 6u)
        assertTrue(r.contains(5, 5))
        assertTrue(r.contains(10, 10))
        assertFalse(r.contains(10, 11))
        assertFalse(r.contains(11, 10))
    }

    @Test
    fun testContainsFloat() {
        val r = Rect.at(5, 5).ofSize(6u, 6u)
        assertTrue(r.contains(5f, 5f))
        assertFalse(r.contains(10.1f, 10f))
    }

    @Test
    fun testIntersect() {
        val r = Rect.at(4, 5).ofSize(6u, 7u)
        assertEquals(r, r.intersect(r))

        val s = Rect.at(0, 0).ofSize(5u, 5u)
        val t = Rect.at(1, 4).ofSize(10u, 12u)
        val i = Rect.at(1, 4).ofSize(4u, 1u)
        assertEquals(i, s.intersect(t))

        val disjoint1 = Rect.at(0, 0).ofSize(5u, 5u)
        val disjoint2 = Rect.at(10, 10).ofSize(100u, 12u)
        assertNull(disjoint1.intersect(disjoint2))
    }
}
