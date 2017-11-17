package view

import javafx.scene.layout.BorderPane
import tornadofx.*

class MainWindow : View("My View") {
    override val root: BorderPane by fxml()
}
