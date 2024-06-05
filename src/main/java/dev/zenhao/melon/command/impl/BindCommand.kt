package dev.zenhao.melon.command.impl

import dev.zenhao.melon.command.Command
import melon.utils.chat.ChatUtil

object BindCommand : Command("bind", description = "Bind module to key.") {
    init {
        builder.literal {
            module { moduleArgument ->
                key { keyArgument ->
                    executor {
                        val module = moduleArgument.value()

                        module.bind = keyArgument.value()
                        ChatUtil.sendMessage("Bind ${module.moduleName} to ${keyArgument.originValue()}")
                    }
                }
            }
        }
    }
}