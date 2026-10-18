// port-lint: tests definitions.rs
package io.github.kotlinmania.imageproc.definitions

import kotlin.test.Test
import kotlin.test.assertEquals

class DefinitionsTest {
    @Test
    fun test_clamp_f32_u8() {
        val t: UByte = Clamp.clampUByte(255f)
        assertEquals(255u.toUByte(), t)
        val u: UByte = Clamp.clampUByte(300f)
        assertEquals(255u.toUByte(), u)
        val v: UByte = Clamp.clampUByte(0f)
        assertEquals(0u.toUByte(), v)
        val w: UByte = Clamp.clampUByte(-5f)
        assertEquals(0u.toUByte(), w)
    }

    @Test
    fun test_clamp_f32_u16() {
        val t: UShort = Clamp.clampUShort(65535f)
        assertEquals(65535u.toUShort(), t)
        val u: UShort = Clamp.clampUShort(300000f)
        assertEquals(65535u.toUShort(), u)
        val v: UShort = Clamp.clampUShort(0f)
        assertEquals(0u.toUShort(), v)
        val w: UShort = Clamp.clampUShort(-5f)
        assertEquals(0u.toUShort(), w)
    }

    @Test
    fun testBlackWhite() {
        assertEquals(0u.toUByte(), LumaBlackWhite.blackUByte().l)
        assertEquals(255u.toUByte(), LumaBlackWhite.whiteUByte().l)
        assertEquals(0u.toUByte(), RgbBlackWhite.blackUByte().r)
        assertEquals(255u.toUByte(), RgbBlackWhite.whiteUByte().r)
    }
}
