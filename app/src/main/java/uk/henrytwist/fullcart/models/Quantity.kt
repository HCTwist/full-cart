package uk.henrytwist.fullcart.models

data class Quantity(val number: Int, val unit: Unit) {

    fun isOneSingle() = number == 1 && unit == Unit.SINGLE

    fun decremented() = unit.getNumberGenerator().decrement(number)

    fun canDecrement() = unit.shouldDecrement() && decremented() > 0

    enum class Unit {

        SINGLE {

            override fun getNumberGenerator() = SingleNumberGenerator
            override fun shouldDecrement() = true
        },
        LITRES {

            override fun getNumberGenerator() = SingleNumberGenerator
            override fun shouldDecrement() = true
        },
        PINTS {

            override fun getNumberGenerator() = SingleNumberGenerator
            override fun shouldDecrement() = true
        },
        PACKS {

            override fun getNumberGenerator() = SingleNumberGenerator
            override fun shouldDecrement() = true
        },
        KILOGRAMS {

            override fun getNumberGenerator() = SingleNumberGenerator
            override fun shouldDecrement() = true
        },
        GRAMS {

            override fun getNumberGenerator() = GramsNumberGenerator
            override fun shouldDecrement() = false
        },
        ML {

            override fun getNumberGenerator() = MlNumberGenerator
            override fun shouldDecrement() = false
        };

        abstract fun getNumberGenerator(): NumberGenerator

        abstract fun shouldDecrement(): Boolean
    }

    interface NumberGenerator {

        fun generate(index: Int): Int

        fun decrement(n: Int): Int
    }

    object SingleNumberGenerator : LinearNumberGenerator(1, 1)

    object GramsNumberGenerator : LinearNumberGenerator(10, 10)

    object MlNumberGenerator : LinearNumberGenerator(25, 25)

    open class LinearNumberGenerator(private val start: Int, private val step: Int) : NumberGenerator {

        override fun generate(index: Int): Int {

            return start + (step * index)
        }

        override fun decrement(n: Int): Int {

            return (n - step).coerceAtLeast(0)
        }
    }
}