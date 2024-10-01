package search


import java.awt.desktop.QuitStrategy
import java.io.File


fun main(args: Array<String>) {

    if (args.size < 2 || args[0] != "--data") {
        println("Usage: --data <personalexample.txt>")
        return
    }

    val fileName = args[1]
    val file = File(fileName)

    if (!file.exists()) {
        println("File not found!")
    } else {
        println("File loaded")
    }

    val searchList = searchList(file) // creates the list to search in
    val invertedIndex = buildInvertedIndex(searchList)


    do {
        println()
        println(
            """ 
                === Menu ===
                1. Search information.
                2. Print all data.
                0. Exit.""".trimIndent()
        )
        val userInput = readln()

        when (userInput) {
            "1" -> {
                println("\nSelect a matching strategy: ALL, ANY, NONE")
                val searchStrategy = readln()
                val searchWord = readln().lowercase().trim()
                val result = searchEngine(searchWord, invertedIndex, searchList, searchStrategy)
                if (result.isNotEmpty()) {
                    println("${result.size} person${if (result.size > 1) "s" else ""} found:")
                    for (r in result) {
                        println(r)
                    }
                } else {
                    println("No matching people found.")
                }
            }

            "2" -> {
                println("\n=== List of people ===")
                for (entry in searchList) {
                    println(entry)
                }
            }

            "0" -> {
                println("Bye!")
                return
            }

            else -> println("Incorrect option! Try again.")
        }


    } while (userInput != "0")

}


fun buildInvertedIndex(searchList: MutableList<String>): MutableMap<String, MutableList<Int>> {


    val invertedIndex = mutableMapOf<String, MutableList<Int>>()

    searchList.forEachIndexed { docId, document ->
        val words = document.split(Regex("[\\s,]+")).filter { it.isNotEmpty() }

        for (word in words) {
            val lcaseWord = word.lowercase()
            val docList = invertedIndex.getOrPut(lcaseWord) { mutableListOf() }
            if (!docList.contains(docId)) {  // Voeg alleen toe als de docId nog niet in de lijst staat
                docList.add(docId)
            }

        }
    }
    return invertedIndex
}


// takes the file and creates a list to search in.
fun searchList(file: File): MutableList<String> {
    var firstName = ""
    var lastName = ""
    var email = ""
    val searchList: MutableList<String> = mutableListOf()
    file.forEachLine { line ->
        val parts = line.split(" ")



        when (parts.size) {
            3 -> {
                firstName = parts[0]
                lastName = parts[1]
                email = parts[2]
                searchList.add("$firstName $lastName $email")

            }

            2 -> {
                firstName = parts[0]
                lastName = parts[1]
                searchList.add("$firstName $lastName")
            }

            else -> println("Incorrect format")
        }

    }



    return searchList
}


fun searchEngine(
    searchWord: String,
    invertedIndex: Map<String, List<Int>>,
    searchList: MutableList<String>,
    searchStrategy: String
): List<String> {
    val words = searchWord.split(" ")

    val resultSet = when (searchStrategy) {
        "ALL" -> searchWithAllStrategy(words, invertedIndex, searchList)
        "ANY" -> searchWithAnyStrategy(words, invertedIndex, searchList)
        "NONE" -> searchWithNoneStrategy(words, invertedIndex, searchList)
        else -> {
            println("Invalid strategy. Please choose ALL, ANY, or NONE.")
            return emptyList()
        }
    }
    return resultSet.distinct() // remove duplicats


}

fun searchWithAllStrategy(
    words: List<String>,
    invertedIndex: Map<String, List<Int>>,
    searchList: MutableList<String>
): List<String> {
    val resultIds = mutableListOf<Int>()
    val initialIds = invertedIndex[words[0].lowercase()] ?: return emptyList()
    resultIds.addAll(initialIds)
    for (word in words) {
        val ids = invertedIndex[word.lowercase()] ?: return emptyList()
        resultIds.retainAll(ids)
    }
    return resultIds.map { searchList[it] }
}

fun searchWithAnyStrategy(
    words: List<String>,
    invertedIndex: Map<String, List<Int>>,
    searchList: MutableList<String>
): List<String> {
    val resultIds = mutableSetOf<Int>()

    for (word in words) {
        val ids = invertedIndex[word.lowercase()] ?: continue
        resultIds.addAll(ids) // Voeg alle ID's toe
    }
    return resultIds.map { searchList[it] }
}


fun searchWithNoneStrategy(
    words: List<String>,
    invertedIndex: Map<String, List<Int>>,
    searchList: MutableList<String>
):List<String> {
    val excludedIds = mutableSetOf<Int>()

    for (word in words) {
        val ids = invertedIndex[word.lowercase()] ?: continue
        excludedIds.addAll(ids) // Voeg alle ID's toe
    }

    return searchList.indices.filterNot { excludedIds.contains(it) }.map { searchList[it] }
}