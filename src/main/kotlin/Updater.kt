import java.io.File
import javax.swing.JOptionPane
import kotlin.system.exitProcess

class Updater {
    private val applicationFileName = "accountant.jar"

    fun run() {
        runApplication()
    }

    fun updateAndRun(url: String) {
        updateApplication(url)
    }


    fun updateApplication(appUrl: String) {
        println("Updating...")
        println("Downloading $appUrl")

        NewVersion.instance.url = appUrl
        NewVersion.instance.targetPath = File("").absolutePath +
                File.separator + applicationFileName

        UpdaterApplication.main(emptyArray())
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
            exitProcess(1)
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
            if(args.isNotEmpty()) {
                Updater().updateAndRun(args[0])
            } else {
                Updater().run()
            }
        }
    }


}