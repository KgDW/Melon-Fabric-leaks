package dev.zenhao.melon.command.impl

import dev.zenhao.melon.command.Command
import melon.utils.chat.ChatUtil

object HelpCommand : Command("help", description = "Print commands description and usage.") {
    init {
        builder.executor {
            val helpMessage = dev.zenhao.melon.command.CommandManager.getHelpMessage()
            ChatUtil.sendMessage(helpMessage)
        }
    }
}