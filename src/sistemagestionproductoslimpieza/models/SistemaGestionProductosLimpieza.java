package sistemagestionproductoslimpieza.models;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SistemaGestionProductosLimpieza extends Application {

    public static void main(String[] args) {
        Application.launch(args);

    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../views/mainView.fxml"));
        stage.setTitle("Sistema Gestion Productos de Limpieza");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
