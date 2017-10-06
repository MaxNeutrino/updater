import java.io.File
import java.util.*
import javax.swing.JOptionPane

class Updater {
    private val appFileName = "app.jar"
    private val appPropertiesFileName = "app.info"
    private val appPropertiesUrl = "https://gist.githubusercontent.com/denosauro/4390cdeb9e5458b065dacad72df0efb5/raw/3e26d234c2fa8751da9dcd6b660d2992ed9f4b43/app.properties"

    private val downloader = Downloader()

    fun run() {
        println("Starting updater...")

        checkVersion()
        runApplication()
    }

    fun checkVersion() {
        val properties = Properties()

        try {
            properties.load(downloader.readFileAsStream(appPropertiesUrl))
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(
                    null,
                    "Can't load app properties. Check your internet connection",
                    "Error",
                    JOptionPane.ERROR_MESSAGE)
            System.exit(1)
        }

        val latestVersion = properties.getProperty("latest_version").toDouble()
        val latestVersionFileUrl = properties.getProperty("latest_app")

        println("Latest version: $latestVersion")

        if(getCurrentVersion() < latestVersion || !File(appFileName).exists()) {
            updateApp(latestVersionFileUrl, latestVersion)
        } else {
            println("You're using latest app version")
        }

    }

    fun updateApp(appUrl: String, version: Double) {
        println("Updating...")
        println("Downloading $appUrl")

        //Download file
        try {
            downloader.downloadFile(appUrl, appFileName)
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(
                    null,
                    "Can't download application. $appUrl",
                    "Error",
                    JOptionPane.ERROR_MESSAGE)
            System.exit(1)
        }

        //Save current version info to file
        val properties = Properties()
        properties.setProperty("current_version", version.toString())
        properties.store(File(appPropertiesFileName).outputStream(), "")

        println("Done.")
    }

    fun deleteApp() {
        val appFile = File(appFileName)

        if(appFile.exists()) {
            appFile.delete()
        }

        val currentProperties = File(appPropertiesFileName)
        if(currentProperties.exists()) {
            currentProperties.delete()
        }
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
        val proc = Runtime.getRuntime().exec("java -jar $appFileName")
        val inputStream = proc.inputStream
        var b = -1
        b = inputStream.read()
        while (b != -1) {
            System.out.write(b)
            b = inputStream.read()
        }
        proc.waitFor()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            Updater().run()
        }
    }


}