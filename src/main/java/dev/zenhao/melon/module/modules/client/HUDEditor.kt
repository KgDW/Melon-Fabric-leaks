package dev.zenhao.melon.module.modules.client

import dev.zenhao.melon.gui.clickgui.guis.HUDEditorScreen
import dev.zenhao.melon.gui.rewrite.gui.MelonClickGui
import dev.zenhao.melon.gui.rewrite.gui.MelonHudEditor
import dev.zenhao.melon.manager.FileManager.saveAll
import dev.zenhao.melon.module.Category
import dev.zenhao.melon.module.Module
import dev.zenhao.melon.module.hud.Image
import melon.utils.Wrapper

object HUDEditor : Module(name = "HUDEditor", langName = "HUD编辑器", category = Category.CLIENT, visible = false) {
    var screen: HUDEditorScreen = HUDEditorScreen()

    override fun onEnable() {
        if (mc.currentScreen == MelonClickGui) {
            MelonClickGui.close()
        }

        if (Wrapper.player != null && mc.currentScreen !is HUDEditorScreen && mc.currentScreen !is MelonHudEditor) {
            if (UiSetting.enableNewUi) {
                mc.setScreen(MelonHudEditor)
            } else {
                mc.setScreen(screen)
            }
            Image.startTime = System.currentTimeMillis()
        }
    }

    override fun onDisable() {
        if (mc.currentScreen is HUDEditorScreen || mc.currentScreen is MelonHudEditor) {
            mc.setScreen(null)
        }
        saveAll()
    }
}
