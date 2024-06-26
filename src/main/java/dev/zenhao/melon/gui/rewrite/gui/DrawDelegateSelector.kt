package dev.zenhao.melon.gui.rewrite.gui

import dev.zenhao.melon.gui.rewrite.gui.render.DrawDelegate

object DrawDelegateSelector {
    private val listeners = mutableListOf<(DrawDelegate) -> Unit>()

    var currentDrawDelegate: DrawDelegate = DefaultDrawDelegate
        private set(value) {
            field = value
            listeners.forEach { it(value) }
        }

    fun changeDrawDelegate(drawDelegate: DrawDelegate) {
        currentDrawDelegate = drawDelegate
    }

    fun listenerChange(callback: (DrawDelegate) -> Unit) {
        listeners.add(callback)
    }
}