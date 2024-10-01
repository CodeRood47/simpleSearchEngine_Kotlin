import java.util.*

fun transformList(numbersString: String): String {
    // Converting the String of comma-separated integers to a MutableList of Integers.
    val numbers = numbersString.split(",").map { it.toInt() }.toMutableList()

    // Put your code here to loop over the list and transform the numbers according to the given constraints.
    for ((i, n) in numbers.withIndex()) {
        if (n % 2 == 0) {
            numbers[i] = n / 2
        } else {
            numbers[i] = n * 2
        }

    }

    // After transforming the list, convert it back to a String and return.
    return numbers.joinToString(",")
}

fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)

    // The input will be read as a String, and you will need to parse it to a MutableList of Integers.
    val numbersString = scanner.nextLine()

    val transformedListString = transformList(numbersString)

    // Print the transformed list as a string.
    println(transformedListString)
}