package dev.zenhao.melon.command.impl

import dev.zenhao.melon.Melon
import dev.zenhao.melon.command.Command
import melon.utils.chat.ChatUtil

object PrefixCommand : Command("prefix") {
    init {
        builder.literal {
            any { anyArgs ->
                executor {
                    val prefix = anyArgs.value()
                    Melon.commandPrefix.value = prefix
                    ChatUtil.sendMessage("Prefix Has Been Set To: $prefix")
                }
            }
        }
    }
}