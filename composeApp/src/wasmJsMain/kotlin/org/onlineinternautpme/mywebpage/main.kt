package org.onlineinternautpme.mywebpage

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.w3c.dom.HTMLElement

@JsFun("""() => { 
    try { 
        var canvas = document.createElement('canvas'); 
        return !!(window.WebGLRenderingContext && (canvas.getContext('webgl') || canvas.getContext('experimental-webgl'))) && typeof THREE !== 'undefined';
    } catch (e) { return false; } 
}""")
external fun canRender3D(): Boolean

@JsFun("() => { if(window.initThreeJsScene) initThreeJsScene(); }")
external fun launchThreeJs()

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val composeTarget = document.getElementById("ComposeTarget") as HTMLElement

    // Always run Compose
    ComposeViewport(composeTarget) {
        App()
    }

    if (canRender3D()) {
        // FIXED: Cast the retrieved Elements to HTMLElement before accessing .style
        val cssContainer = document.getElementById("css-container") as? HTMLElement
        cssContainer?.style?.display = "block"

        val webglContainer = document.getElementById("webgl-container") as? HTMLElement
        webglContainer?.style?.display = "block"

        launchThreeJs()
    } else {
        // Fallback: Make Compose full screen
        composeTarget.style.apply {
            width = "100vw"
            height = "100vh"

            position = "absolute"
            top = "0"
            left = "0"
        }
    }
}