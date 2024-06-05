package dev.zenhao.melon.command.impl

import dev.zenhao.melon.command.Command
import dev.zenhao.melon.gui.rewrite.gui.MelonClickGui
import dev.zenhao.melon.gui.rewrite.gui.MelonHudEditor
import melon.utils.chat.ChatUtil

object ResetUiCommand : Command("resetui", arrayOf("reui"), "Reset ClickGui component positions") {
    init {
        builder.executor {
            MelonClickGui.resetUiComponentPositions()
            MelonHudEditor.resetUiComponentPositions()
            ChatUtil.sendMessage("Reset position successfully")
        }
    }
}