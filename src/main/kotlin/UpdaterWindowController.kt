import javafx.concurrent.Task
import javafx.concurrent.WorkerStateEvent
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ProgressBar
import javafx.stage.Stage
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import kotlin.system.exitProcess

class UpdaterWindowController {
    private val updater: Updater = Updater()

    @FXML
    private lateinit var progressBar: ProgressBar

    @FXML
    private lateinit var cancelButton: Button

    private lateinit var downloadTask: Task<Int>

    @FXML
    fun initialize() {
        downloadTask = createWorker()

        progressBar.progressProperty().bind(downloadTask.progressProperty())
        cancelButton.onAction = EventHandler<ActionEvent> { exitProcess(2) }


        val doneHandler = EventHandler<WorkerStateEvent> {
            (progressBar.scene.window as Stage).close()
            updater.runApplication()
        }

        downloadTask.onSucceeded = doneHandler
        downloadTask.onFailed = doneHandler

        Thread(downloadTask).start()
    }

    fun createWorker(): Task<Int> {
        return object : Task<Int>() {
            @Throws(Exception::class)
             override fun call(): Int {
                try {
                    val url = URL(NewVersion.instance.url)

                    val connection = url.openConnection()
                    connection.connect()
                    val fileLength = connection.contentLength
                    val input = BufferedInputStream(url.openStream())

                    // Copy file
                    var saveFile: String? = null
                    try {
                        saveFile = File(NewVersion.instance.targetPath).absolutePath
                    } catch (e1: IOException) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace()
                    }

                    var progress = 0

                    val output = FileOutputStream(saveFile!!)

                    val data = ByteArray(1024)
                    var count: Int = input.read(data)

                    var total: Long = 0

                    while (count != -1) {
                        total += count.toLong()
                        progress = (total * 100 / fileLength.toLong()).toInt()
                        output.write(data, 0, count)
                        updateProgress(progress.toLong(), 100)
                        count = input.read(data)
                    }

                    output.flush()
                    output.close()
                    input.close()

                } catch (ex: Exception) {
                    System.err.println(ex)
                }

                return 0
            }
        }
    }
}