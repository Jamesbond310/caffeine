import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the

val Project.libs
  get() = the<LibrariesForLibs>()

fun Project.version(major: Int, minor: Int, patch: Int, releaseBuild: Boolean) {
  version = "$major.$minor.$patch" + if (releaseBuild) "" else "-SNAPSHOT"
}

fun Project.defaultJvmArgs(): List<String> {
  val jvmArgs = mutableListOf("-Xmx4g")
  if (System.getenv("GRAALVM") == "true") {
    jvmArgs += listOf(
      "-XX:+UnlockExperimentalVMOptions", "-Dgraal.ShowConfiguration=info",
      "-XX:+EnableJVMCI", "-XX:+UseJVMCICompiler", "-XX:+EagerJVMCI")
  }
  val arguments = findProperty("jvmArgs") as String?
  if (arguments != null) {
    jvmArgs += arguments.split(",")
  }
  return jvmArgs
}

fun caffeineSystemProperties(): Map<String, Any> {
  return System.getProperties().stringPropertyNames()
    .filter { it.startsWith("caffeine") }
    .associateWith { System.getProperties().getProperty(it) }
}

fun isCI(): Boolean {
  return !System.getenv("CI").isNullOrEmpty()
}

val DisableStrongEncapsulationJvmArgs = listOf(
  "--add-exports", "jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED",
  "--add-exports", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
  "--add-exports", "jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED",
  "--add-exports", "jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED",
  "--add-exports", "jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED",
  "--add-exports", "jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
  "--add-exports", "jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED",
  "--add-exports", "jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED",
  "--add-opens", "jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED",
  "--add-opens", "jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED",
  "--add-opens", "jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED")
