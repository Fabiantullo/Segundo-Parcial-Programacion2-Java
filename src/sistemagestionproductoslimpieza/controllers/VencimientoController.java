package sistemagestionproductoslimpieza.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sistemagestionproductoslimpieza.models.entities.Producto;
import sistemagestionproductoslimpieza.utils.ProductoDeserializer;
import sistemagestionproductoslimpieza.utils.ServicioJSON;

public class VencimientoController implements Initializable {

    @FXML private ListView<Producto> listView;
    @FXML private Button btnExportar;
    @FXML private Button btnCerrar;
    
    private List<Producto> listaFiltrada;
    private final ServicioJSON<Producto> servicioJSON;

    public VencimientoController() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Producto.class, new ProductoDeserializer())
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, ctx) -> new JsonPrimitive(date.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, ctx) -> LocalDate.parse(json.getAsString()))
                .create();
        
        Type listType = new TypeToken<List<Producto>>(){}.getType();
        
        this.servicioJSON = new ServicioJSON<>(gson, listType); 
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
    
    public void initData(List<Producto> productos) {
        this.listaFiltrada = productos;
        listView.setItems(FXCollections.observableArrayList(listaFiltrada));
    }

    @FXML
    public void exportarJSON() {
        if (listaFiltrada == null || listaFiltrada.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "No hay productos para exportar.");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte JSON");
        fileChooser.setInitialFileName("productos_proximos_vencer.json");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos JSON", "*.json"));
        
        File file = fileChooser.showSaveDialog(btnExportar.getScene().getWindow());
        
        if (file != null) {
            try {
                servicioJSON.guardar(file.getAbsolutePath(), listaFiltrada);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Archivo exportado correctamente.");
            } catch (IOException e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error al guardar: " + e.getMessage());
            }
        }
    }
    
    @FXML
    public void cerrar() {
        ((Stage) btnCerrar.getScene().getWindow()).close();
    }
    
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        new Alert(tipo, mensaje).showAndWait();
    }
}