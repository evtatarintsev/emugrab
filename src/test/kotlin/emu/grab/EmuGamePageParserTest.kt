package emu.grab

import org.junit.Test
import kotlin.test.assertEquals

internal class EmuGamePageParserTest {

    @Test
    fun getPaginationLinks() {
        val expectedLinks = listOf(
            "http://emusega.ru/sega_b/p2/",
            "http://emusega.ru/sega_b/p3/",
            "http://emusega.ru/sega_b/p4/",
            "http://emusega.ru/sega_b/p5/",
            "http://emusega.ru/sega_b/p6/",
            "http://emusega.ru/sega_b/p7/",
            "http://emusega.ru/sega_b/p8/",
        )
        val html = this::class.java.getResource("/root.html")!!.readText()

        val actualLinks = EmuGamePageParser("http://emusega.ru", html).getPaginationLinks()

        assertEquals(expectedLinks, actualLinks)
    }

    @Test
    fun getGameLinks() {
        val expectedLinks = listOf(
            "http://emusega.ru/sega_b/tjp.html",
            "http://emusega.ru/sega_b/tlm.html",
            "http://emusega.ru/sega_b/pmf.html",
            "http://emusega.ru/sega_b/tjm.html",
            "http://emusega.ru/sega_b/fmf.html",
            "http://emusega.ru/sega_b/pmm.html",
            "http://emusega.ru/sega_b/tej.html",
            "http://emusega.ru/sega_b/trr.html",
            "http://emusega.ru/sega_b/pln.html",
            "http://emusega.ru/sega_b/tfp.html",
        )
        val html = this::class.java.getResource("/root.html")!!.readText()

        val actualLinks = EmuGamePageParser("http://emusega.ru", html).getGameLinks()

        assertEquals(expectedLinks, actualLinks)
    }
}