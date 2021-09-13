## pathway4

### RandomDice

```kotlin
fun main() {

    &#47; val diceRange = 1..6
    val diceRange : IntRange = 1..6
    println("diceRange: ${diceRange}")

    val randomNumber = diceRange.random()
	println("Random number1: ${randomNumber}")
    println("Random number2: ${(1..6).random()}")
    
    
    val firstDice = Dice()
    val firstRoll = firstDice.roll()
    println("firstDice side: ${firstDice.sides} and roll: ${firstRoll}")
	firstDice.sides=20
    println("firstDice side: ${firstDice.sides} and roll: ${firstDice.roll()}")
    
    
    val secondDice = Dice2(6)
    val secondRoll = secondDice.roll()
    println("secondDice side: ${secondDice.numSides} and roll: ${secondRoll}")
    
    
    val thirdDice = Dice()
}


class Dice {
    var sides = 6
    fun roll(): Int {
        return (1..sides).random()
    }
}


class Dice2(val numSides: Int) {
     fun roll(): Int {
        return (1..numSides).random()
    }
}
```

 
