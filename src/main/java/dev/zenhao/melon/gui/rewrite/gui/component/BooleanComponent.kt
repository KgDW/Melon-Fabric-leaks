package dev.zenhao.melon.gui.rewrite.gui.component

import dev.zenhao.melon.gui.rewrite.gui.render.Alignment
import dev.zenhao.melon.gui.rewrite.gui.render.DrawDelegate
import dev.zenhao.melon.gui.rewrite.gui.render.DrawScope
import dev.zenhao.melon.gui.rewrite.gui.render.Padding
import dev.zenhao.melon.module.modules.client.UiSetting
import dev.zenhao.melon.setting.BooleanSetting
import dev.zenhao.melon.utils.animations.Easing
import java.awt.Color

class BooleanComponent(
    val booleanSetting: BooleanSetting,
    private val parentComponent: Component,
    override var x: Float,
    override var y: Float,
    override var width: Float,
    override var height: Float,
    override var drawDelegate: DrawDelegate
) : Component, Visible {
    private var startTime = System.currentTimeMillis()

    private val progress: Float
        get() = if (booleanSetting.value) {
            Easing.OUT_CUBIC.inc(Easing.toDelta(startTime, 300f))
        } else {
            Easing.OUT_CUBIC.dec(Easing.toDelta(startTime, 300f))
        }

    override fun isVisible(): Boolean {
        return booleanSetting.isVisible()
    }

    override fun DrawScope.render(mouseX: Float, mouseY: Float) {
        drawRectBySetting(
            x,
            y,
            width * progress,
            height,
            if (isHovering(mouseX, mouseY)) {
                UiSetting.getThemeSetting().primary.darker()
            } else {
                UiSetting.getThemeSetting().primary
            }
        )

        drawText(
            booleanSetting.name, if (isHovering(mouseX, mouseY)) {
                Color.WHITE.darker()
            } else {
                Color.WHITE
            }, Padding(horizontal = 1f), verticalAlignment = Alignment.CENTER
        )
    }

    override fun mouseClicked(mouseX: Float, mouseY: Float, button: Int): Boolean {
        super.mouseClicked(mouseX, mouseY, button)
        if (isHovering(mouseX, mouseY) && button == 0) {
            booleanSetting.value = !booleanSetting.value
            parentComponent.rearrange()
            startTime = System.currentTimeMillis()
            return true
        }
        return false
    }
}