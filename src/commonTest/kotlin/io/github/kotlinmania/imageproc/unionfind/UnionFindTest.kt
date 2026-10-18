// port-lint: tests union_find.rs
package io.github.kotlinmania.imageproc.unionfind

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UnionFindTest {
    @Test
    fun test_root() {
        val forest =
            DisjointSetForest(
                count = 4,
                parent = intArrayOf(1, 3, 2, 3),
                treeSize = intArrayOf(1, 2, 1, 3),
            )

        assertEquals(3, forest.root(0))
        assertEquals(3, forest.parent[0])
    }

    @Test
    fun test_find() {
        val forest =
            DisjointSetForest(
                count = 4,
                parent = intArrayOf(0, 0, 2, 2),
                treeSize = intArrayOf(2, 1, 2, 1),
            )

        assertTrue(forest.find(0, 1))
        assertFalse(forest.find(0, 2))
    }

    @Test
    fun test_trees() {
        //    3         4
        //    |        /  \
        //    1       5    7
        //   /  \     |
        //  0    2    6
        val forest =
            DisjointSetForest(
                count = 8,
                // element:  0, 1, 2, 3, 4, 5, 6, 7
                parent = intArrayOf(1, 3, 1, 3, 4, 4, 5, 4),
                treeSize = intArrayOf(1, 3, 1, 4, 4, 2, 1, 1),
            )

        assertEquals(
            listOf(listOf(0, 1, 2, 3), listOf(4, 5, 6, 7)),
            forest.trees(),
        )
    }

    @Test
    fun test_union_find_sequence() {
        val forest = DisjointSetForest(6)
        // 0  1  2  3  4  5

        //                               0, 1, 2, 3, 4, 5
        assertContentEquals(intArrayOf(0, 1, 2, 3, 4, 5), forest.parent)
        assertEquals(6, forest.numTrees())

        forest.union(0, 4)
        // 0  1  2  3  5
        // |
        // 4

        //                               0, 1, 2, 3, 4, 5
        assertContentEquals(intArrayOf(0, 1, 2, 3, 0, 5), forest.parent)
        assertEquals(5, forest.numTrees())

        forest.union(1, 3)
        // 0  1  2  5
        // |  |
        // 4  3

        //                               0, 1, 2, 3, 4, 5
        assertContentEquals(intArrayOf(0, 1, 2, 1, 0, 5), forest.parent)
        assertEquals(4, forest.numTrees())

        forest.union(3, 2)
        // 0    1     5
        // |   / \
        // 4  3   2

        //                               0, 1, 2, 3, 4, 5
        assertContentEquals(intArrayOf(0, 1, 1, 1, 0, 5), forest.parent)
        assertEquals(3, forest.numTrees())

        forest.union(2, 4)
        //    1     5
        //  / | \
        // 0  3  2
        // |
        // 4

        //                               0, 1, 2, 3, 4, 5
        assertContentEquals(intArrayOf(1, 1, 1, 1, 0, 5), forest.parent)
        assertEquals(2, forest.numTrees())
    }

    @Test
    fun proptest_root_of_singleton_is_self() {
        for (count in 1..32) {
            for (i in 0 until count) {
                val forest = DisjointSetForest(count)
                assertEquals(i, forest.root(i))
            }
        }
    }

    @Test
    fun proptest_find_in_singleton_forest_matches_index_equality() {
        for (count in 1..32) {
            for (i in 0 until count) {
                for (j in 0 until count) {
                    val forest = DisjointSetForest(count)
                    assertEquals(i == j, forest.find(i, j))
                }
            }
        }
    }
}
