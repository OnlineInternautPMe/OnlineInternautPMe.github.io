package com.example.onlineinternautpmegithubio.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.WebElementView
import kotlinx.browser.document
import org.w3c.dom.Element
import org.w3c.dom.HTMLDivElement

@OptIn(ExperimentalWasmJsInterop::class)
fun runThreeJsLogic(container: HTMLDivElement) {
    // We use dynamic import('three') which works better in Wasm/ESM environments
    js("""
    import('three').then(THREE => {
        const width = container.clientWidth || 400;
        const height = container.clientHeight || 400;

        const scene = new THREE.Scene();
        const camera = new THREE.PerspectiveCamera(75, width / height, 0.1, 1000);
        
        // Use alpha: true so it doesn't block the Compose background
        const renderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
        renderer.setSize(width, height);
        container.appendChild(renderer.domElement);

        const geometry = new THREE.BoxGeometry(1, 1, 1);
        const material = new THREE.MeshBasicMaterial({ color: 0x00ff00 });
        const cube = new THREE.Mesh(geometry, material);
        scene.add(cube);

        camera.position.z = 5;

        function animate() {
            requestAnimationFrame(animate);
            cube.rotation.x += 0.01;
            cube.rotation.y += 0.01;
            renderer.render(scene, camera);
        }
        
        // Final sanity check: if the size is 0, wait a frame
        if (width === 0) {
            setTimeout(() => {
                renderer.setSize(container.clientWidth, container.clientHeight);
                animate();
            }, 100);
        } else {
            animate();
        }
    }).catch(err => console.error("Failed to load Three.js:", err));
    """)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun ThreeJsAnimation(modifier: Modifier) {
    WebElementView(
        modifier = modifier
            .fillMaxSize(),
        factory = {
            (document.createElement("div") as HTMLDivElement).apply {
                // IMPORTANT: Ensure the div fills its parent so Three.js can see the size
                style.width = "100%"
                style.height = "100%"
                style.backgroundColor = "black" // Visual check: if you see black, the div exists!
            }
        },
        update = { div ->
            if (div.childElementCount == 0) {
                runThreeJsLogic(div)
            }
        }
    )
}