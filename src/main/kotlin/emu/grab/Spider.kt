package emu.grab

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.charset.Charset
import java.util.zip.ZipInputStream

class Spider(val baseUrl: String, prefix: String, rootDir: String) {
    private val cp1251 = Charset.forName("Windows-1251")
    private val httpClient = HttpClient(CIO) {
        Charsets {
            register(cp1251)
            responseCharsetFallback = cp1251
        }
    }
    private val dir = File(rootDir, baseUrl.substringAfter("//")).also {
        if (!it.exists()) {
            it.mkdir()
        }
    }

    private val rootPages = buildList {
        add(baseUrl + "/${prefix}_1/")
        for (l in 'a'..'z') {
            add(baseUrl + "/${prefix}_$l/")
        }
    }

    suspend fun grab() = coroutineScope {
        rootPages.forEach {
            launch { parseRootPage(it) }
        }
    }

    private suspend fun parseRootPage(url: String) {
        getPage(url) {
            getGameLinks().forEach {
                parseGamePage(it)
            }
            getPaginationLinks().forEach {
                parseContentPage(it)
            }
        }
    }

    private suspend fun parseContentPage(url: String) {
        getPage(url) {
            getGameLinks().forEach {
                parseGamePage(it)
            }
        }
    }

    private suspend fun parseGamePage(url: String) {
        getPage(url) {
            getArchiveLink()?.let {
                downloadGame(it)
            }
        }
    }

    private suspend fun getPage(url: String, body: suspend EmuGamePageParser.() -> Unit) {
        try {
            val response = httpClient.get(url)
            if (response.status == HttpStatusCode.OK) {
                val gamePageParser = EmuGamePageParser(baseUrl, response.body())
                gamePageParser.body()
            }
        } catch (err: Exception) {
            println("error process $url: $err")
        }
    }

    private suspend fun downloadGame(url: String) {
        val response = try {
            httpClient.get(url)
        } catch (err: Exception) {
            println("error download archive $url: $err")
            return
        }

        ZipInputStream(response.body()).use { zis ->
            zis.nextEntry?.let { entry ->
                println("saving ${entry.name}")
                File(dir, entry.name).writeBytes(zis.readAllBytes())
            }
        }
    }
}