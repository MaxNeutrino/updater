import java.io.File
import java.util.*

class Updater {
    private val appFileName = "app.jar"
    private val appPropFileUrl = "https://gist.githubusercontent.com/denosauro/4390cdeb9e5458b065dacad72df0efb5/raw/5542aa3647b8e692c70ca2228bcab45542a4b3e9/app.properties"
    private val downloader = Downloader()

    fun run() {
        println("Starting updater...")

        checkVersion()
        runApplication()
    }

    fun runFile(filePath: String) {
        val proc = Runtime.getRuntime().exec("java -jar $filePath").waitFor()
    }

    fun checkVersion() {
        val properties = Properties()
        properties.load(downloader.readFileAsStream(appPropFileUrl))

        val latestVersion = properties.getProperty("latest_version").toDouble()
        val latestVersionFileUrl = properties.getProperty("latest_app")

        println("Latest version: $latestVersion")

        if(getCurrentVersion() < latestVersion) {
            updateApp(latestVersionFileUrl, latestVersion)
        } else {
            println("You're using latest app version")
        }

    }

    fun updateApp(appUrl: String, version: Double) {
        println("Updating...")
        println("Downloading $appUrl")

        //Download file
        downloader.downloadFile(appUrl, appFileName)

        //Save current version info to file
        val properties = Properties()
        properties.setProperty("current_version", version.toString())
        properties.store(File("app.info").outputStream(), "")

        println("Done.")
    }

    fun getCurrentVersion(): Double {
        //Try to read current version info from file
        val file = File("app.info")

        if(file.exists()) {
            val properties = Properties()
            properties.load(file.inputStream())

            return properties.getProperty("current_version").toDouble()
        }

        return -1.0
    }

    fun runApplication() {
        runFile(appFileName)
    }
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Updater().run()
        }
    }


}