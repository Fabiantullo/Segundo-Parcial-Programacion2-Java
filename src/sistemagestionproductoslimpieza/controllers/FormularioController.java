package sistemagestionproductoslimpieza.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sistemagestionproductoslimpieza.models.SistemaProductosManejador;
import sistemagestionproductoslimpieza.models.entities.Producto;
import sistemagestionproductoslimpieza.models.entities.ProductoEcologico;
import sistemagestionproductoslimpieza.models.entities.ProductoQuimico;
import sistemagestionproductoslimpieza.models.enums.TipoProducto;

public class FormularioController implements Initializable {
    
    private SistemaProductosManejador manager;
    private Producto productoActual;
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtConcentracion;
    @FXML private DatePicker dpVencimiento;
    @FXML private ComboBox<TipoProducto> comboTipo;
    
    @FXML private Pane boxEspecifico;
    @FXML private Label lblEspecifico;
    @FXML private TextField txtEspecifico;
    
    @FXML private Button btnGuardar;

    public void setManager(SistemaProductosManejador manager) {
        this.manager = manager;
    }

    public void setProducto(Producto producto) {
        this.productoActual = producto;
        cargarDatosProducto();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboTipo.getItems().setAll(TipoProducto.values());
        
        comboTipo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal != null) actualizarCampoEspecifico(newVal);
        });

        boxEspecifico.setVisible(false);
        boxEspecifico.setManaged(false);
    }
    
    private void actualizarCampoEspecifico(TipoProducto tipo) {
        boxEspecifico.setVisible(true);
        boxEspecifico.setManaged(true);
        txtEspecifico.clear();
        
        switch (tipo) {
            case QUIMICO -> {
                lblEspecifico.setText("Tipo de Advertencia:");
                txtEspecifico.setPromptText("Ej: Toxico");
            }
            case ECOLOGICO -> {
                lblEspecifico.setText("Etiqueta Ecologica:");
                txtEspecifico.setPromptText("Ej: Biodegradable");
            }
        }
    }
    
    private void cargarDatosProducto() {
        if (productoActual == null) return;
        
        txtNombre.setText(productoActual.getNombreComercial());
        txtConcentracion.setText(productoActual.getConcentracion());
        dpVencimiento.setValue(productoActual.getFechaVencimiento());
        comboTipo.setValue(productoActual.getTipo());
        comboTipo.setDisable(true); 
        
        if (productoActual instanceof ProductoQuimico) {
            ProductoQuimico pq = (ProductoQuimico) productoActual;
            txtEspecifico.setText(pq.getTipoAdvertencia());
        } else if (productoActual instanceof ProductoEcologico) {
            ProductoEcologico pe = (ProductoEcologico) productoActual;
            txtEspecifico.setText(pe.getEtiquetaEcologica());
        }
    }

    @FXML
    public void guardar() {
        try {
            Producto nuevoProducto = construirProducto();
            
            if (productoActual == null) {
                manager.agregarProducto(nuevoProducto);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto agregado correctamente.");
            } else {
                manager.modificarProducto(productoActual, nuevoProducto);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Producto modificado correctamente.");
            }

            cerrarVentana();

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }
    
    private Producto construirProducto() throws Exception {
        String nombre = txtNombre.getText();
        String concentracion = txtConcentracion.getText();
        LocalDate vencimiento = dpVencimiento.getValue();
        String especifico = txtEspecifico.getText();
        TipoProducto tipo = comboTipo.getValue();
        
        if (tipo == null) throw new Exception("Debe seleccionar un tipo.");
        
        switch (tipo) {
            case QUIMICO:
                return new ProductoQuimico(nombre, concentracion, vencimiento, especifico);
            case ECOLOGICO:
                return new ProductoEcologico(nombre, concentracion, vencimiento, especifico);
            default:
                throw new Exception("Tipo no soportado.");
        }
    }

    @FXML
    public void cancelar() {
        cerrarVentana();
    }
    
    private void cerrarVentana() {
        ((Stage) btnGuardar.getScene().getWindow()).close();
    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        new Alert(tipo, mensaje).showAndWait();
    }
}