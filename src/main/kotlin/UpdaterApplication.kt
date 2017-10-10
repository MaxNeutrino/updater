import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class UpdaterApplication : Application() {

    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val controller = UpdaterWindowController()
        val loader = FXMLLoader(javaClass.getResource("updater_window.fxml"))
        loader.setController(controller)
        val root = loader.load<Parent>()
        primaryStage.title = "Accountant updater"
        primaryStage.isResizable = false
        primaryStage.scene = Scene(root, 400.0, 150.0)
        primaryStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(UpdaterApplication::class.java)
        }
    }
}