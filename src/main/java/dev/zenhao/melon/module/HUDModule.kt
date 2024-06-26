package dev.zenhao.melon.module

import dev.zenhao.melon.gui.clickgui.guis.HUDEditorScreen
import dev.zenhao.melon.gui.rewrite.gui.DrawDelegateSelector
import dev.zenhao.melon.gui.rewrite.gui.MelonHudEditor
import dev.zenhao.melon.gui.rewrite.gui.component.Component
import dev.zenhao.melon.gui.rewrite.gui.render.DrawDelegate
import dev.zenhao.melon.gui.rewrite.gui.render.DrawScope
import dev.zenhao.melon.module.modules.client.UiSetting
import dev.zenhao.melon.setting.BooleanSetting
import melon.events.render.Render2DEvent
import melon.system.event.safeEventListener
import melon.system.render.graphic.Render2DEngine
import net.minecraft.client.gui.DrawContext
import java.awt.Color

abstract class HUDModule(
    name: String,
    langName: String = "Undefined",
    override var x: Float = 0f,
    override var y: Float = 0f,
    category: Category = Category.HUD,
    description: String = "",
    visible: Boolean = false
) : AbstractModule(), Component {
    override var drawDelegate: DrawDelegate = DrawDelegateSelector.currentDrawDelegate

    override var width: Float = 10f
    override var height: Float = 10f

    private val pinned0 = bsetting("Pinned", true)
    private val pinned by pinned0
    private var relativeX by fsetting("RelativeX", 0f, -1f, 2f).isTrue { false }
    private var relativeY by fsetting("RelativeY", 0f, -1f, 2f).isTrue { false }

    private var lastX = 0f
    private var lastY = 0f

    init {
        moduleName = name
        moduleCName = langName
        moduleCategory = category
        this.description = description
        this.isVisible = visible

        pinned0.onChange<BooleanSetting> { _, input ->
            if (input) {
                synchronized(this) {
                    relativeX = x / mc.window.scaledWidth
                    relativeY = y / mc.window.scaledHeight
                }
            }
        }

        safeEventListener<Render2DEvent> {
            if (mc.currentScreen !is MelonHudEditor && mc.currentScreen !is HUDEditorScreen)
                return@safeEventListener

            if (UiSetting.getThemeSetting().rounded) {
                Render2DEngine.drawRound(it.drawContext.matrices, x, y, width, height, 2f, Color(0, 0, 0, 50))
            } else {
                Render2DEngine.drawRect(it.drawContext.matrices, x, y, width, height, Color(0, 0, 0, 50))
            }
        }
    }

    protected var dragging = false
    protected var dragX = 0f
    protected var dragY = 0f

    final override fun DrawScope.render(mouseX: Float, mouseY: Float) {
        if (dragging) {
            try {
                this@HUDModule.x = (mouseX - dragX).coerceIn(0f, mc.window.scaledWidth - width)
                this@HUDModule.y = (mouseY - dragY).coerceIn(0f, mc.window.scaledHeight - height)
            } catch (e: IllegalArgumentException) {
                this@HUDModule.x = 0f
                this@HUDModule.y = 0f
            }

            relativeX = x / mc.window.scaledWidth.toFloat()
            relativeY = y / mc.window.scaledHeight.toFloat()
        }

        if (isEnabled) {
            renderOnGui(mouseX, mouseY)
        }
    }

    fun renderDelegateOnGame(context: DrawContext) {
        if (pinned && !dragging) {
            var calculatedX: Float
            var calculatedY: Float

            try {
                calculatedX =
                    (mc.window.scaledWidth.toFloat() * relativeX).coerceIn(0f, mc.window.scaledWidth - width)
                calculatedY =
                    (mc.window.scaledHeight.toFloat() * relativeY).coerceIn(0f, mc.window.scaledHeight - height)
            } catch (e: IllegalArgumentException) {
                calculatedX = 0f
                calculatedY = 0f
            }


            if (lastX != calculatedX || lastY != calculatedY) {
                lastX = calculatedX
                lastY = calculatedY

                this@HUDModule.x = calculatedX
                this@HUDModule.y = calculatedY

                rearrange()
            }
        }

        DrawScope(x, y, width, height, drawDelegate, context).renderOnGame()
        onRender(context)
    }

    open fun DrawScope.renderOnGame() {}

    open fun DrawScope.renderOnGui(mouseX: Float, mouseY: Float) {}

    override fun mouseClicked(mouseX: Float, mouseY: Float, button: Int): Boolean {
        if (isHovering(mouseX, mouseY) && button == 0) {
            dragging = true
            dragX = mouseX - x
            dragY = mouseY - y
            return true
        }
        return false
    }

    override fun mouseReleased(mouseX: Float, mouseY: Float, button: Int): Boolean {
        if (dragging && button == 0) {
            dragging = false
            return true
        }
        return false
    }
}
