package dev.zenhao.melon.module.hud

import dev.zenhao.melon.module.Category
import dev.zenhao.melon.module.HUDModule
import dev.zenhao.melon.module.modules.client.Colors
import dev.zenhao.melon.utils.math.LagCompensator
import melon.system.render.newfont.FontRenderers
import melon.utils.chat.ChatUtil
import net.minecraft.client.gui.DrawContext

object TpsHUD : HUDModule(name = "TpsHUD", langName = "TPS显示", x = 160f, y = 160f, category = Category.HUD) {
    override fun onRender(context: DrawContext) {
        val fontColor = Colors.hudColor.value.rgb
        val finalString = "TPS " + ChatUtil.SECTIONSIGN + "f" + String.format("%.2f", LagCompensator.tickRate)
        FontRenderers.lexend.drawString(context.matrices, finalString, x + 2.0, y + 3.0, fontColor)
        width = FontRenderers.lexend.getStringWidth(finalString) + 4
    }
}