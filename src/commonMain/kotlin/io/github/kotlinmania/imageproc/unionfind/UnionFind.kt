// port-lint: source union_find.rs
package io.github.kotlinmania.imageproc.unionfind

/**
 * Data structure for efficient union find.
 */
public class DisjointSetForest public constructor(
    /** Number of forest elements. */
    public val count: Int,
    /**
     * parent[i] is the index of the parent
     * of the element with index i. If parent[i] == i
     * then i is a root.
     */
    public val parent: IntArray,
    /** tree_size[i] is the size of the tree rooted at i. */
    public val treeSize: IntArray,
) {
    /**
     * Constructs forest of singletons with count elements.
     */
    public constructor(count: Int) : this(
        count = count,
        parent = IntArray(count) { it },
        treeSize = IntArray(count) { 1 },
    )

    public val tree_size: IntArray
        get() = treeSize

    /**
     * Returns the number of trees in the forest.
     */
    public fun numTrees(): Int {
        var acc = 0
        for (i in 0 until count) {
            if (i == parent[i]) {
                acc += 1
            }
        }
        return acc
    }

    public fun num_trees(): Int = numTrees()

    /**
     * Returns index of the root of the tree containing i.
     * Performs path compression.
     */
    public fun root(i: Int): Int {
        require(i in 0 until count) { "Index $i out of bounds for count $count" }
        var j = i
        while (true) {
            val p = parent[j]
            parent[j] = parent[p]
            if (j == p) {
                break
            }
            j = p
        }
        return j
    }

    /**
     * Returns true if i and j are in the same tree.
     */
    public fun find(i: Int, j: Int): Boolean {
        require(i in 0 until count && j in 0 until count) { "Indices must be in range 0 until $count" }
        return root(i) == root(j)
    }

    /**
     * Unions the trees containing i and j.
     */
    public fun union(i: Int, j: Int) {
        require(i in 0 until count && j in 0 until count) { "Indices must be in range 0 until $count" }
        val p = root(i)
        val q = root(j)
        if (p == q) {
            return
        }
        val pSize = treeSize[p]
        val qSize = treeSize[q]
        if (pSize < qSize) {
            parent[p] = q
            treeSize[q] = pSize + qSize
        } else {
            parent[q] = p
            treeSize[p] = pSize + qSize
        }
    }

    /**
     * Returns the elements of each tree.
     */
    public fun trees(): List<List<Int>> {
        val rootSets = mutableMapOf<Int, Int>()
        val sets = mutableListOf<MutableList<Int>>()
        for (i in 0 until count) {
            val root = root(i)
            val setIdx = rootSets[root]
            if (setIdx != null) {
                sets[setIdx].add(i)
            } else {
                val idx = sets.size
                sets.add(mutableListOf(i))
                rootSets[root] = idx
            }
        }
        return sets
    }
}
