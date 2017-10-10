import java.awt.EventQueue
import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption


class Downloader {

    fun downloadFile(sourceURL: String, targetPath: String): Path
    {
        val url = URL(sourceURL)
        val targetPath = File(targetPath).toPath()
        Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING)

        return targetPath
    }

    fun downloadWithDialog(sourceURL: String, targetPath: String) {
        EventQueue.invokeLater {
            val form = DownloadForm(
                    sourceURL,
                    targetPath,
                    Updater()
            )
            form.isVisible = true
        }
    }

    fun readFileAsStream(sourceURL: String): InputStream {
        val stream = URL(sourceURL).openConnection().getInputStream()

        return stream
    }
}