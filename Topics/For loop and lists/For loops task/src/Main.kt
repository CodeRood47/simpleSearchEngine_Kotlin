fun main() {
    val numList = mutableListOf<Int>()
    for (n in 1..readln().toInt()) {
        val numAdder = readln().toInt()
        numList.add(numAdder)
    }

    val (p, m) = readln().split(" ")
    if (numList.contains(p.toInt()) && numList.contains(m.toInt())) {
        println("YES")
    } else println("NO")
}
