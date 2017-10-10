import java.awt.EventQueue
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JProgressBar
import javax.swing.SwingWorker

class DownloadForm(val url:String, val targetPath: String, val updater: Updater) : JFrame("Updater") {

    private val progressBar: JProgressBar

    init {
        isResizable = false
        setSize(525, 200)
        setLocation(500, 280)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        contentPane.layout = null

        // Label Title
        val lblTitle = JLabel("Updating...", JLabel.CENTER)
        lblTitle.setBounds(73, 24, 370, 14)
        contentPane.add(lblTitle)

        // ProgressBar
        progressBar = JProgressBar()
        progressBar.isStringPainted = true
        progressBar.minimum = 0
        progressBar.maximum = 100
        progressBar.setBounds(166, 99, 190, 24)
        contentPane.add(progressBar)

        EventQueue.invokeLater { BackgroundWorker().execute() }
    }

    inner class BackgroundWorker : SwingWorker<Void, Void>() {
        init {
            addPropertyChangeListener { progressBar.value = progress }
        }

        override fun done() {
            dispose()
            updater.runApplication()
        }

        @Throws(Exception::class)
        override fun doInBackground(): Void? {

            try {
                val url = URL(url)

                val connection = url.openConnection()
                connection.connect()
                val fileLength = connection.contentLength
                val input = BufferedInputStream(url.openStream())

                // Copy file
                var saveFile: String? = null
                try {
                    saveFile = File(targetPath).absolutePath
                } catch (e1: IOException) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace()
                }

                val output = FileOutputStream(saveFile!!)

                val data = ByteArray(1024)
                var count: Int = input.read(data)

                var total: Long = 0

                while (count != -1) {
                    total += count.toLong()
                    progress = (total * 100 / fileLength).toInt()
                    output.write(data, 0, count)
                    count = input.read(data)
                }

                output.flush()
                output.close()
                input.close()

            } catch (ex: Exception) {
                System.err.println(ex)
            }

            return null
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            EventQueue.invokeLater {
                val form = DownloadForm(
                        "https://github.com/denosauro/updater-test/raw/master/Main-0.02.jar",
                        "accountant.jar",
                        Updater()
                )
                form.isVisible = true
            }
        }
    }

}