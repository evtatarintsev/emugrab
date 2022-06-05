package emu.grab

import org.jsoup.Jsoup

class EmuGamePageParser(private val baseUrl: String, html: String) {
    private val doc = Jsoup.parse(html) ?: throw IllegalArgumentException("can not parse html")
    private val pageListingSelector = "div.page_listing a[href]"
    private val gameLinkSelector = "td.centercont a[href]"
    private val archiveLink = "h2 a[href]"

    fun getPaginationLinks(): List<String> {
        val tags = doc.select(pageListingSelector)
        val hrefs = buildList {
            for (a in tags) {
                add(a.attr("href"))
            }
        }
        return hrefs.toSet().map { baseUrl + it }
    }

    fun getGameLinks(): List<String> {
        val tags = doc.select(gameLinkSelector)
        val hrefs = buildList {
            for (a in tags) {
                add(a.attr("href"))
            }
        }
        return hrefs.toSet().map { baseUrl + it }
    }

    fun getArchiveLink(): String? {
        val tag = doc.select(archiveLink).first()
        if (tag != null) {
            return baseUrl + tag.attr("href")
        }
        return null
    }
}