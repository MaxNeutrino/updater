import java.io.File
import javax.swing.JOptionPane

class Updater {
    private val appFileName = "app.jar"

    private val downloader = Downloader()

    fun run() {
        println("Starting updater...")

        runApplication()
    }

    fun updateAndRun(url: String) {
        updateApp(url)
        runApplication()
    }


    fun updateApp(appUrl: String) {
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
            //System.exit(1)
        }

        println("Done.")
    }

    fun deleteApp() {
        val appFile = File(appFileName)

        if(appFile.exists()) {
            appFile.delete()
        }
    }

    fun runApplication() {
        if(!File(appFileName).exists()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Can't find application file.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE)
            System.exit(1)
        }

        val javaBin =
                if(System.getProperty("os.name").contains("Windows"))
                    System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe"
                else
                    System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"

        val appPath = File(Updater::class.java!!.protectionDomain.codeSource.location.toURI()).absolutePath +
                File.separator + appFileName

        println(javaBin)
        println(appPath)

        val command = arrayListOf<String>(javaBin, "-jar", appPath)

        //Start app
        val process = ProcessBuilder(command)
        process.start()
    }

    companion object {
        @JvmStatic fun main(args: Array<String>) {
            if(args.isNotEmpty())
                Updater().updateAndRun(args[0])

            Updater().run()
        }
    }


}