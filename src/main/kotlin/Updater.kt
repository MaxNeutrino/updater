import java.io.File
import javax.swing.JOptionPane

class Updater {
    private val applicationFileName = "accountant.jar"

    private val downloader = Downloader()

    fun run() {
        runApplication()
    }

    fun updateAndRun(url: String) {
        updateApplication(url)
        runApplication()
    }


    fun updateApplication(appUrl: String) {
        println("Updating...")
        println("Downloading $appUrl")

        //Download file
        try {
            downloader.downloadFile(appUrl, applicationFileName)
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

    fun deleteApplication() {
        val applicationFile = File(applicationFileName)

        if(applicationFile.exists()) {
            applicationFile.delete()
        }
    }

    fun runApplication() {
        val applicationPath = File("").absolutePath +
                File.separator + applicationFileName

        if(!File(applicationPath).exists()) {
            JOptionPane.showMessageDialog(
                    null,
                    "Can't find application file. $applicationPath",
                    "Error",
                    JOptionPane.ERROR_MESSAGE)
            System.exit(1)
        }

        val javaBin =
                if(System.getProperty("os.name").contains("Windows"))
                    System.getProperty("java.home") + File.separator + "bin" + File.separator + "java.exe"
                else
                    System.getProperty("java.home") + File.separator + "bin" + File.separator + "java"

        println(javaBin)
        println(applicationPath)

        val command = arrayListOf<String>(javaBin, "-jar", applicationPath)

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