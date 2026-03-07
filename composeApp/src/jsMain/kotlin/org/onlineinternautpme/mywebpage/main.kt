package org.onlineinternautpme.mywebpage

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLCanvasElement

// Interop: Check if WebGL and Three.js are available
@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("""() => { 
    try { 
        var canvas = document.createElement('canvas'); 
        var hasWebGL = !!(window.WebGLRenderingContext && (canvas.getContext('webgl') || canvas.getContext('experimental-webgl')));
        return hasWebGL && typeof THREE !== 'undefined';
    } catch (e) { 
        return false; 
    } 
}""")
external fun canRender3D(): Boolean

// Interop: Call our updated custom JS function (No model path needed)
@OptIn(ExperimentalWasmJsInterop::class)
@JsFun("(containerId) => { if(window.initThreeJsScene) initThreeJsScene(containerId); }")
external fun launchThreeJs(containerId: String)

/*@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    if (canRender3D()) {
        val composeCanvas = document.getElementById("ComposeTarget") as HTMLCanvasElement
        composeCanvas.style.display = "none"

        val threeContainer = document.getElementById("three-container") as HTMLDivElement
        threeContainer.style.display = "block"

        // Launch 3D scene (only passing the container ID now)
        launchThreeJs("three-container")

    } else {
        ComposeViewport(document.body!!) {
            App()
        }
    }
}*/