package dev.zenhao.melon.module.modules.client

import dev.zenhao.melon.gui.clickgui.GUIRender
import dev.zenhao.melon.gui.clickgui.guis.ClickGuiScreen
import dev.zenhao.melon.gui.rewrite.gui.MelonClickGui
import dev.zenhao.melon.gui.rewrite.gui.MelonHudEditor
import dev.zenhao.melon.manager.FileManager.saveAll
import dev.zenhao.melon.module.Category
import dev.zenhao.melon.module.Module
import dev.zenhao.melon.module.hud.Image
import melon.utils.concurrent.threads.runSafe
import net.minecraft.client.util.InputUtil

object ClickGui : Module(
    name = "ClickGUI",
    langName = "ClickGUI",
    category = Category.CLIENT,
    keyCode = InputUtil.GLFW_KEY_U,
    visible = true
) {
    var chinese = bsetting("ChineseUI", false)
    var notification by bsetting("Notification", false)
    var chat = bsetting("ToggleChat", true)
    var screen: ClickGuiScreen = ClickGuiScreen()

    override fun onEnable() {
        if (mc.currentScreen == MelonHudEditor) {
            MelonHudEditor.close()
        }

        runSafe {
            if (mc.currentScreen is MelonClickGui || mc.currentScreen is ClickGuiScreen) {
                return
            }

            if (UiSetting.enableNewUi) {
                mc.setScreen(MelonClickGui)
            } else {
                GUIRender.init()
                mc.setScreen(screen)
            }

            Image.startTime = System.currentTimeMillis()
        }
    }

    override fun onDisable() {
        runSafe {
            if (mc.currentScreen is ClickGuiScreen || mc.currentScreen is MelonClickGui) {
                mc.setScreen(null)
            }
            saveAll()
        }
    }
}
