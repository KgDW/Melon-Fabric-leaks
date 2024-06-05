package dev.zenhao.melon.gui.rewrite.gui

import dev.zenhao.melon.gui.rewrite.gui.component.Panel
import dev.zenhao.melon.module.Category
import dev.zenhao.melon.module.ModuleManager
import dev.zenhao.melon.module.modules.client.HUDEditor

object MelonHudEditor : GuiScreen() {
    init {
        elements.add(
            Panel(
                ModuleManager.hUDModules,
                Category.HUD,
                this,
                5f,
                5f,
                105f,
                15f,
                DrawDelegateSelector.currentDrawDelegate
            )
        )

        container.addAll(elements)
    }

    override fun close() {
        super.close()
        HUDEditor.disable()
    }
}