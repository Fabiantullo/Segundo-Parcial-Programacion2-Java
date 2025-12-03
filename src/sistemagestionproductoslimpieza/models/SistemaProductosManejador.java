package sistemagestionproductoslimpieza.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.List;
import sistemagestionproductoslimpieza.exceptions.ProductoDuplicadoException;
import sistemagestionproductoslimpieza.models.entities.Producto;
import sistemagestionproductoslimpieza.models.interfaces.IManejadorArchivos;
import sistemagestionproductoslimpieza.models.interfaces.IRepositorio;
import sistemagestionproductoslimpieza.utils.ProductoDeserializer;
import sistemagestionproductoslimpieza.utils.ServicioJSON;


public class SistemaProductosManejador {
    
    private final IRepositorio<Producto> repositorio;
    private final IManejadorArchivos<Producto> servicioJson;
    
    private final String RUTA_ARCHIVO_PRINCIPAL = "productos.json";

    public SistemaProductosManejador() {
        this.repositorio = new RepositorioProductos();
        
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Producto.class, new ProductoDeserializer())

                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, ctx) -> new JsonPrimitive(date.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, ctx) -> LocalDate.parse(json.getAsString()))

                .create();
        
        Type listType = new TypeToken<List<Producto>>(){}.getType();
        
        this.servicioJson = new ServicioJSON<>(gson, listType);
        
        cargarDatos();
    }
    
    public List<Producto> getListaProductos() {
        return repositorio.obtenerTodos();
    }
    
    public List<Producto> getProductosPorVencer() {
        return repositorio.filtrar(p -> p.estaProximoAVencer());
    }
    
    private void cargarDatos() {
        try {
            List<Producto> datos = servicioJson.cargar(RUTA_ARCHIVO_PRINCIPAL);
            if (datos != null) {
                datos.forEach(repositorio::agregar);
            }
        } catch (IOException e) {
            System.out.println("Error cargando datos: " + e.getMessage());
        }
    }
    
    public void guardarCambios(){
        try {
            servicioJson.guardar(RUTA_ARCHIVO_PRINCIPAL, repositorio.obtenerTodos());
        } catch (IOException e) {
            System.out.println("Error guardando datos: " + e.getMessage());
        }
        
    }
    
    public void exportarReporteVencimiento(String rutaArchivo) throws IOException {
        List<Producto> listaFiltrada = getProductosPorVencer();
        servicioJson.guardar(rutaArchivo, listaFiltrada);
    }
    
    public void agregarProducto(Producto nuevo) throws ProductoDuplicadoException {
        boolean existe = repositorio.existe(p -> 
            p.getNombreComercial().equalsIgnoreCase(nuevo.getNombreComercial()) &&
            p.getConcentracion().equalsIgnoreCase(nuevo.getConcentracion()) &&
            p.getFechaVencimiento().equals(nuevo.getFechaVencimiento())
        );
        
        if (existe) {
            throw new ProductoDuplicadoException("Ya existe un producto con ese nombre, concentración y fecha.");
        }
        
        repositorio.agregar(nuevo);
        guardarCambios();
    }
    
    public void modificarProducto(Producto original, Producto modificado) {
        repositorio.eliminar(original);
        repositorio.agregar(modificado);
        guardarCambios();
    }
    
    public void eliminarProducto(Producto producto) {
        repositorio.eliminar(producto);
        guardarCambios();
    }
}