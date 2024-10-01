fun main() {
    val numbers = mutableListOf<Int>()
    val size = readln().toInt()
    for (n in 1..size) {
        val numAdd = readln().toInt()
        numbers.add(numAdd)
    }
    var trippleCounter = 0
    for (i in 0 until numbers.size - 2) {

        if (numbers[i + 2] == numbers[i] + 2 && numbers[i + 1] == numbers[i] + 1) {
            trippleCounter++
        }
        
    }
    println(trippleCounter)
}
