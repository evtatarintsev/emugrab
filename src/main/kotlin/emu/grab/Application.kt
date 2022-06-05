package emu.grab

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


suspend fun main() = coroutineScope {
    val rootDir = "/Users/john/Downloads/emugames"
    val sites = listOf(
        "http://emudendy.ru" to "dendy",
        "http://emusega.ru" to "sega",
    )
    sites.forEach { (site, prefix) ->
        launch {
            Spider(site, prefix, rootDir).grab()
        }
    }
}
