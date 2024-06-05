package dev.zenhao.melon.command.impl

import dev.zenhao.melon.command.Command
import dev.zenhao.melon.module.modules.misc.FakePlayer
import melon.utils.chat.ChatUtil

object FakePlayerCommand : Command("fakeplayer", arrayOf("fp"), "Fast Toggle FakePlayer Module") {
    init {
        builder.literal {
            executor {
                FakePlayer.toggle()
                ChatUtil.sendNoSpamMessage(
                    "Module FakePlayer Has Been ${
                        if (FakePlayer.isEnabled) {
                            "Enabled"
                        } else {
                            "Disabled"
                        }
                    } !"
                )
            }
        }
    }
}