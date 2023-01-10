package ai.app

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal.valueOf as d

class KNearestNeighborsTest {

    @Test
    fun kNearestNeighbor() {
        // Create some sample learning data. In this data,
        // the first two instances belong to a class, the
        // four next belong to another class and the last
        // three to yet another.
        val inputs = listOf(
            // The first two are from class 0
            listOf(-5, -2, -1),
            listOf(-5, -5, -6),

            // The next four are from class 1
            listOf(2, 1, 1),
            listOf(1, 1, 2),
            listOf(1, 2, 2),
            listOf(3, 1, 2),

            // The last three are from class 2
            listOf(11, 5, 4),
            listOf(15, 5, 6),
            listOf(10, 5, 6),
        ).matrix()

        val outputs = listOf(
            0, 0,        // First two from class 0
            1, 1, 1, 1,  // Next four from class 1
            2, 2, 2      // Last three from class 2
        ).vector()


        // Now we will create the K-Nearest Neighbors algorithm. For this
        // example, we will be choosing k = 3. This means that, for a given
        // instance, it's nearest 3 neighbors will be used to cast a decision.
        val knn = KNearestNeighbors(3, 3, inputs, outputs)

        // After the algorithm has been created, we can classify a new instance:
        assertEquals(d(2), knn.compute(listOf(11, 5, 3).vector()))
    }
}
