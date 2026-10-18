// port-lint: tests point.rs
package io.github.kotlinmania.imageproc.point

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PointTest {
    @Test
    fun lineFromPoints() {
        val p = Point.new(5.0, 7.0)
        val q = Point.new(10.0, 3.0)
        assertEquals(
            Line.fromPoints(p, q),
            Line(
                a = 4.0,
                b = 5.0,
                c = -55.0,
            ),
        )
    }

    @Test
    fun distanceBetweenLineAndPoint() {
        val actual = Line(a = 8.0, b = 7.0, c = 5.0).distanceFromPoint(Point.new(2.0, 3.0))
        val expected = 3.9510276472
        assertTrue(abs(actual - expected) < 1e-10)
    }

    @Test
    fun distanceAndRotation() {
        val p = Point.new(0.0, 0.0)
        val q = Point.new(3.0, 4.0)
        assertEquals(5.0, distance(p, q))
        assertEquals(25.0, distanceSq(p, q))

        val rotation = Rotation.new(0.0)
        val rotated = q.rotate(rotation)
        assertEquals(q.x, rotated.x)
        assertEquals(q.y, rotated.y)
    }
}
