package sistemagestionproductoslimpieza.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sistemagestionproductoslimpieza.models.SistemaProductosManejador;
import sistemagestionproductoslimpieza.models.entities.Producto;

public class MainViewController implements Initializable {

    private SistemaProductosManejador manager; 

    @FXML private ListView<Producto> listViewProductos; 
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        manager = new SistemaProductosManejador(); 
        listViewProductos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        cargarLista();
    }    
    
    private void cargarLista() {
        listViewProductos.setItems(FXCollections.observableArrayList(manager.getListaProductos()));
        listViewProductos.refresh();
    }
    
    @FXML
    public void agregar() {
        abrirFormulario(null);
    }
    
    @FXML
    public void modificar() {
        Producto seleccionado = listViewProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe seleccionar un producto para modificar.");
        }
    }
    
    @FXML
    public void eliminar() {
        Producto seleccionado = listViewProductos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirmar Eliminacion");
            confirm.setHeaderText(null);
            confirm.setContentText("¿Esta seguro de eliminar: " + seleccionado.getNombreComercial() + "?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                manager.eliminarProducto(seleccionado);
                cargarLista();
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto eliminado.");
            }
        } else {
            mostrarAlerta(Alert.AlertType.WARNING, "Debe seleccionar un producto para eliminar.");
        }
    }
    
    @FXML
    public void verProximosVencer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionproductoslimpieza/views/vencimientoView.fxml"));
            Parent root = loader.load();
            
            VencimientoController controller = loader.getController();
            controller.initData(manager.getProductosPorVencer()); 
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Productos Proximos a Vencer");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
        } catch (IOException e) {
            
            mostrarAlerta(Alert.AlertType.ERROR, "Error al abrir ventana: " + e.getMessage());
        }
    }
    
    private void abrirFormulario(Producto productoEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/sistemagestionproductoslimpieza/views/formularioView.fxml"));
            Parent root = loader.load();
            
            FormularioController controller = loader.getController();
            controller.setManager(manager); 
            
            if (productoEditar != null) {
                controller.setProducto(productoEditar); 
            }

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(productoEditar == null ? "Nuevo Producto" : "Editar Producto");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            
            cargarLista();

        } catch (IOException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error al abrir formulario: " + e.getMessage());
        }
    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}