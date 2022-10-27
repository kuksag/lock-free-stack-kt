package example.stack

import org.jetbrains.kotlinx.lincheck.*
import org.jetbrains.kotlinx.lincheck.annotations.Operation
import org.jetbrains.kotlinx.lincheck.strategy.managed.modelchecking.*
import org.jetbrains.kotlinx.lincheck.strategy.stress.*
import org.jetbrains.kotlinx.lincheck.verifier.VerifierState
import org.junit.jupiter.api.Test

class TreiberStackTest {
    private val q = TreiberStack<Int>()

    @Operation
    fun push(x: Int): Unit = q.push(x)

    @Operation
    fun pop(): Int? = q.pop()

    @Test
    fun modelCheckingTest() {
        ModelCheckingOptions()
            .iterations(100)
            .invocationsPerIteration(10_000)
            .threads(3)
            .actorsPerThread(3)
            .checkObstructionFreedom()
            .sequentialSpecification(IntStackSequential::class.java)
            .check(this::class.java)
    }

    @Test
    fun stressTest() {
        StressOptions()
            .iterations(100)
            .invocationsPerIteration(50_000)
            .threads(3)
            .actorsPerThread(3)
            .sequentialSpecification(IntStackSequential::class.java)
            .check(this::class.java)
    }
}

class IntStackSequential : VerifierState() {
    private val q = ArrayDeque<Int>()

    fun push(x: Int) {
        q.addLast(x)
    }

    fun pop(): Int? = q.removeLastOrNull()

    override fun extractState() = q
}
