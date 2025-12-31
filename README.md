# OnlineInternautPMeGithbIo

Just an static web about me (made with Kotlin Multiplatform Web)

## Run and Build deploy Application

- Run (on macOS/Linux)
  ```shell
  # ./gradlew kotlinWasmUpgradeYarnLock
  ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
  ```
- Build deploy (on macOS/Linux)
  ```shell
  # ./gradlew kotlinWasmUpgradeYarnLock
  ./gradlew buildDeploymentAndMoveToDocs --no-configuration-cache
  ```

## Update GitHub Pages

- Run Build deploy task
- Push changes to main branch