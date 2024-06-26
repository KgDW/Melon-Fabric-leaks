package dev.zenhao.melon.command

import dev.zenhao.melon.command.argument.ArgumentTree
import dev.zenhao.melon.command.argument.impl.StringArgument
import melon.system.util.interfaces.Alias

abstract class Command(
    final override val name: String,
    final override val alias: Array<out String> = emptyArray(),
    val description: String = "Empty"
) : Alias {
    private val root = ArgumentTree(StringArgument(0, name, alias, true))
    protected val builder = CommandBuilder(0, root)

    fun complete(args: List<String>): List<String> {
        return root.complete(args)
    }

    fun invoke(input: String) {
        if (input.isEmpty()) {
            return
        }

        root.invoke(input)
    }

    fun getArgumentTreeString(): String {
        return root.getArgumentTreeString()
    }
}