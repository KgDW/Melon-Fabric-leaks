package melon.system.render.newfont

import dev.zenhao.melon.Melon
import java.awt.Font
import java.awt.FontFormatException
import java.io.IOException
import java.util.*

object FontRenderers {
    lateinit var default: FontAdapter
    lateinit var cn: FontAdapter
    lateinit var lexend: FontAdapter
    lateinit var comfortaa: FontAdapter
    lateinit var knight: FontAdapter
    lateinit var badaboom: FontAdapter
    lateinit var icons: FontAdapter
    lateinit var mid_icons: FontAdapter
    lateinit var big_icons: FontAdapter

    @Throws(IOException::class, FontFormatException::class)
    fun createDefault(size: Float, name: String): RendererFontAdapter {
        return RendererFontAdapter(
            Font.createFont(
                Font.TRUETYPE_FONT, Objects.requireNonNull(
                    Melon::class.java.classLoader.getResourceAsStream("$name.ttf")
                )
            ).deriveFont(Font.BOLD, size * 2), size * 2
        )
    }

    @Throws(IOException::class, FontFormatException::class)
    fun createIcons(size: Float): RendererFontAdapter {
        return RendererFontAdapter(
            Font.createFont(
                Font.TRUETYPE_FONT,
                Objects.requireNonNull(Melon::class.java.classLoader.getResourceAsStream("icons.ttf"))
            ).deriveFont(Font.PLAIN, size * 2), size * 2
        )
    }
}
