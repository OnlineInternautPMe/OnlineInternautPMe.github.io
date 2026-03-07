// scene.js
function initThreeJsScene() {
    const webglContainer = document.getElementById('webgl-container');
    const cssContainer = document.getElementById('css-container');
    if (!webglContainer || !cssContainer) return;

    // 1. Scene Setup
    const scene = new THREE.Scene();
    // CRITICAL FOR CHROMIUM: Do NOT set scene.background to a color here.
    // It must remain null for the CSS layer to show through properly.

    // --- NEW: Explicit rendering distances ---
    // Feel free to tweak these numbers!
    // Lower number = closer to the viewer.
    const DISTANCE_PORTRAIT = 4.7;  // Brings it closer in portrait mode
    const DISTANCE_LANDSCAPE = 4.3; // Nice and tight for widescreen

    const aspect = window.innerWidth / window.innerHeight;
    const camera = new THREE.PerspectiveCamera(45, aspect, 0.1, 100);
    // Set initial distance based on current orientation
    camera.position.set(0, 0, aspect < 1 ? DISTANCE_PORTRAIT : DISTANCE_LANDSCAPE);

    const webglRenderer = new THREE.WebGLRenderer({ antialias: true, alpha: true });
    webglRenderer.setSize(window.innerWidth, window.innerHeight);
    webglRenderer.setPixelRatio(window.devicePixelRatio);

    // FIX 2: Explicitly force Chromium to clear the WebGL buffer to pure transparency
    webglRenderer.setClearColor(0x000000, 0);
    webglContainer.appendChild(webglRenderer.domElement);

    const cssRenderer = new THREE.CSS3DRenderer();
    cssRenderer.setSize(window.innerWidth, window.innerHeight);
    cssContainer.appendChild(cssRenderer.domElement);

    // 2. High-Contrast Lighting Setup

    // A. Lower ambient light so shadows are deeper and highlights can pop
    const ambientLight = new THREE.AmbientLight(0xffffff, 3.0);
    scene.add(ambientLight);

    // B. Main Key Light (Much stronger, positioned to reflect off the spinning screen and visor)
    const directionalLight = new THREE.DirectionalLight(0xffffff, 2.5); // Cranked up to 2.5
    directionalLight.position.set(5, 5, 8);
    scene.add(directionalLight);

    // C. NEW: Rim Light (A cool blue light from the back-left to highlight the phone's curved edges)
    //const rimLight = new THREE.DirectionalLight(0xaabbff, 1.5);
    //rimLight.position.set(-5, 5, -8);
    //scene.add(rimLight);

    // --- NEW: Immersive Skybox Background Cube (Different Image Per Face) ---
    const bgCubeGeometry = new THREE.BoxGeometry(7, 7, 7);
    bgCubeGeometry.rotateY(Math.PI)

    // 1. Create a single Texture Loader
    const textureLoader = new THREE.TextureLoader();

    // Helper function to load a texture and ensure correct encoding for standard colors
    function loadSkyboxTexture(url) {
        const tex = textureLoader.load(url);
        // encoding must be set for images to look vibrant and not washed out in sRGB space
        tex.colorSpace = THREE.sRGBEncoding;
        return tex;
    }

    // 2. Load six distinct textures using public placeholders (Replace with your own URLs!)
    // Order matters: [+x, -x, +y, -y, +z, -z]
    const cubeImageUrl = 'https://raw.githubusercontent.com/mrdoob/three.js/refs/heads/master/examples/textures/cube/Bridge2/'
    const textures = [
        loadSkyboxTexture(cubeImageUrl + 'posx.jpg'), // Right
        loadSkyboxTexture(cubeImageUrl + 'negx.jpg'), // Left
        loadSkyboxTexture(cubeImageUrl + 'posy.jpg'), // Top
        loadSkyboxTexture(cubeImageUrl + 'negy.jpg'), // Bottom
        loadSkyboxTexture(cubeImageUrl + 'posz.jpg'), // Front
        loadSkyboxTexture(cubeImageUrl + 'negz.jpg')  // Back
    ];

    // 3. Create an array of six MeshBasicMaterials
    const materialsArray = [];
    for (let i = 0; i < 6; i++) {
        materialsArray.push(new THREE.MeshBasicMaterial({
            map: textures[i],
            // We MUST set BackSide on ALL materials so the images render on the inside walls!
            side: THREE.BackSide,
            color: 0xffffff // Maintain subtle darkening so the phone pops!
        }));
    }

    // 4. Create the final background cube passing the materialsArray
    const backgroundCube = new THREE.Mesh(bgCubeGeometry, materialsArray);
    scene.add(backgroundCube);
    // -------------------------------------------------------------------------

    // A. Main Phone Body (Updated with Rounded Edges)

    const phoneGroup = new THREE.Group();

    // 1. Define phone dimensions
    const phoneWidth = 1.5;
    const phoneHeight = 3.2;
    const phoneThickness = 0.15;
    const cornerRadius = 0.1; // Adjust this to make corners sharper or rounder

    // 2. Draw a 2D shape with rounded corners
    const shape = new THREE.Shape();
    const x = -phoneWidth / 2;
    const y = -phoneHeight / 2;

    shape.moveTo(x, y + cornerRadius);
    shape.lineTo(x, y + phoneHeight - cornerRadius);
    shape.quadraticCurveTo(x, y + phoneHeight, x + cornerRadius, y + phoneHeight);
    shape.lineTo(x + phoneWidth - cornerRadius, y + phoneHeight);
    shape.quadraticCurveTo(x + phoneWidth, y + phoneHeight, x + phoneWidth, y + phoneHeight - cornerRadius);
    shape.lineTo(x + phoneWidth, y + cornerRadius);
    shape.quadraticCurveTo(x + phoneWidth, y, x + phoneWidth - cornerRadius, y);
    shape.lineTo(x + cornerRadius, y);
    shape.quadraticCurveTo(x, y, x, y + cornerRadius);

    // 3. Extrude the 2D shape into 3D
    const extrudeSettings = {
        depth: phoneThickness,
        bevelEnabled: true,  // Set to true to slightly round the front/back faces
        bevelSegments: 4,    // Higher number = smoother bevel
        steps: 1,
        bevelSize: 0.015,    // Width of the front/back edge rounding
        bevelThickness: 0.015 // Depth of the front/back edge rounding
    };

    const bodyGeometry = new THREE.ExtrudeGeometry(shape, extrudeSettings);

    // CRITICAL: ExtrudeGeometry builds from the bottom-left corner by default.
    // We must center it so your rotation logic continues to pivot around the middle.
    bodyGeometry.center();

    const bodyMaterial = new THREE.MeshStandardMaterial({ color: 0x2c2c2c, roughness: 0.3, metalness: 0.8 });
    const phoneBody = new THREE.Mesh(bodyGeometry, bodyMaterial);

    phoneGroup.add(phoneBody);

    // B. The Compose Screen (Updated with CSS Border Radius)
    const composeDOMElement = document.getElementById('ComposeTarget');
    composeDOMElement.style.display = 'block';
    composeDOMElement.style.margin = '0';
    composeDOMElement.style.border = 'none';

    // NEW: Apply CSS rounding and clip the contents
    // (A 34px radius visually matches our 3D mask radius of 0.12 based on your scale)
    composeDOMElement.style.borderRadius = '0.01';
    composeDOMElement.style.overflow = 'hidden';

    const cssObject = new THREE.CSS3DObject(composeDOMElement);
    cssObject.scale.set(0.0035, 0.0035, 0.0035);
    cssObject.position.z = 0.091;
    phoneGroup.add(cssObject);

    // C. The Hole Punch Mask (Updated to a Rounded ShapeGeometry)

    // 1. Define mask dimensions (slightly smaller than the phone body)
    const maskWidth = 1.46; // 1.5
    const maskHeight = 3.16; // 3.2
    const maskRadius = 0.12;

    // 2. Draw the 2D rounded shape for the mask
    const maskShape = new THREE.Shape();
    const mx = -maskWidth / 2;
    const my = -maskHeight / 2;

    maskShape.moveTo(mx, my + maskRadius);
    maskShape.lineTo(mx, my + maskHeight - maskRadius);
    maskShape.quadraticCurveTo(mx, my + maskHeight, mx + maskRadius, my + maskHeight);
    maskShape.lineTo(mx + maskWidth - maskRadius, my + maskHeight);
    maskShape.quadraticCurveTo(mx + maskWidth, my + maskHeight, mx + maskWidth, my + maskHeight - maskRadius);
    maskShape.lineTo(mx + maskWidth, my + maskRadius);
    maskShape.quadraticCurveTo(mx + maskWidth, my, mx + maskWidth - maskRadius, my);
    maskShape.lineTo(mx + maskRadius, my);
    maskShape.quadraticCurveTo(mx, my, mx, my + maskRadius);

    // 3. Create a flat ShapeGeometry instead of a BoxGeometry
    const holePunchGeometry = new THREE.ShapeGeometry(maskShape);
    const holePunchMaterial = new THREE.MeshBasicMaterial({
        colorWrite: false,
        depthWrite: true
    });

    const holePunchMesh = new THREE.Mesh(holePunchGeometry, holePunchMaterial);
    holePunchMesh.position.z = 0.091;
    holePunchMesh.renderOrder = -1;
    phoneGroup.add(holePunchMesh);

    // D. Camera Visor
    const visorGeometry = new THREE.BoxGeometry(1.5, 0.35, 0.08);
    const visorMaterial = new THREE.MeshStandardMaterial({ color: 0x111111, roughness: 0.2, metalness: 0.6 });
    const cameraVisor = new THREE.Mesh(visorGeometry, visorMaterial);
    cameraVisor.position.set(0, 0.9, -0.1);
    phoneGroup.add(cameraVisor);

    scene.add(phoneGroup);

    window.addEventListener('resize', () => {
        const newAspect = window.innerWidth / window.innerHeight;
        camera.aspect = newAspect;
        camera.position.z = newAspect < 1 ? 5.5 : 4.0;
        camera.updateProjectionMatrix();

        webglRenderer.setSize(window.innerWidth, window.innerHeight);
        cssRenderer.setSize(window.innerWidth, window.innerHeight);
    });

    // Interaction State
    let isDragging = false;
    let firstDragged = false;
    let previousPointerPosition = { x: 0, y: 0 };
    let targetRotY = 0;
    let targetRotX = 0.1;
    let lastInteractionTime = Date.now();
    const IDLE_TIMEOUT_MS = 2500;
    const rotationSensitivity = window.innerWidth < 768 ? 0.02 : 0.01;

    function handleDragStart(e) {
        isDragging = true;
        firstDragged = true;
        previousPointerPosition = { x: e.clientX, y: e.clientY };
        lastInteractionTime = Date.now();
    }

    function handleDragMove(e) {
        if (!isDragging) return;
        const deltaX = e.clientX - previousPointerPosition.x;
        const deltaY = e.clientY - previousPointerPosition.y;

        targetRotY += deltaX * rotationSensitivity;
        targetRotX += deltaY * rotationSensitivity;
        targetRotX = Math.max(-Math.PI / 3, Math.min(Math.PI / 3, targetRotX));

        previousPointerPosition = { x: e.clientX, y: e.clientY };
        lastInteractionTime = Date.now();
    }

    function handleDragEnd() {
        isDragging = false;
        lastInteractionTime = Date.now();
    }

    document.addEventListener('pointerdown', handleDragStart, { capture: true });
    document.addEventListener('pointermove', handleDragMove, { capture: true });
    document.addEventListener('pointerup', handleDragEnd, { capture: true });
    document.addEventListener('pointercancel', handleDragEnd, { capture: true });

    // 4. Animation Loop
    function animate() {
        requestAnimationFrame(animate);

        // Resume auto-rotation if idle
        if (!isDragging && (Date.now() - lastInteractionTime > IDLE_TIMEOUT_MS)) {
            const time = Date.now() * 0.0015;
            if(!firstDragged) { targetRotY =  Math.sin(time) * 0.2; } else { targetRotY = 0.0; }
            if(!firstDragged) { targetRotX =  (0.1 + Math.sin(time * 0.8) * 0.03); } else {targetRotX = 0.0; }
        }

        // Smoothly rotate the phone
        phoneGroup.rotation.y += (targetRotY - phoneGroup.rotation.y) * 0.05;
        phoneGroup.rotation.x += (targetRotX - phoneGroup.rotation.x) * 0.05;

        // --- NEW: Lock the background room strictly to the user's drag/rotation ---
        // By mapping the room directly to the phone's rotation state,
        // it creates the perfect illusion of a camera panning around a physical space.
        backgroundCube.rotation.y = phoneGroup.rotation.y;
        backgroundCube.rotation.x = phoneGroup.rotation.x;

        webglRenderer.render(scene, camera);
        cssRenderer.render(scene, camera);
    }

    animate();

    // 5. Handle Resize and Device Rotation
    window.addEventListener('resize', () => {
        const newAspect = window.innerWidth / window.innerHeight;
        camera.aspect = newAspect;

        // --- NEW: Instantly adjust the camera distance if the user rotates their device ---
        camera.position.z = newAspect < 1 ? DISTANCE_PORTRAIT : DISTANCE_LANDSCAPE;

        camera.updateProjectionMatrix();

        webglRenderer.setSize(window.innerWidth, window.innerHeight);
        cssRenderer.setSize(window.innerWidth, window.innerHeight);
    });
}