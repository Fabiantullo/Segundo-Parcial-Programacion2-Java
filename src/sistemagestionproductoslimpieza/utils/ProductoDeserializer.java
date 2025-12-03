package sistemagestionproductoslimpieza.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import sistemagestionproductoslimpieza.exceptions.ProductoInvalidoException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import sistemagestionproductoslimpieza.models.entities.Producto;
import sistemagestionproductoslimpieza.models.entities.ProductoEcologico;
import sistemagestionproductoslimpieza.models.entities.ProductoQuimico;

public class ProductoDeserializer implements JsonSerializer<Producto>, JsonDeserializer<Producto> {

    @Override
    public JsonElement serialize(Producto producto, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        
        jsonObject.addProperty("tipo", producto.getTipo().toString());
        jsonObject.addProperty("nombreComercial", producto.getNombreComercial());
        jsonObject.addProperty("concentracion", producto.getConcentracion());
        
        jsonObject.addProperty("fechaVencimiento", producto.getFechaVencimiento().toString());

        if (producto instanceof ProductoQuimico) {
            ProductoQuimico pq = (ProductoQuimico) producto;
            jsonObject.addProperty("tipoAdvertencia", pq.getTipoAdvertencia());
        } else if (producto instanceof ProductoEcologico) {
            ProductoEcologico pe = (ProductoEcologico) producto;
            jsonObject.addProperty("etiquetaEcologica", pe.getEtiquetaEcologica());
        }

        return jsonObject;
    }

    @Override
    public Producto deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException{
        JsonObject jsonObject = json.getAsJsonObject();

        if (!jsonObject.has("tipo")) {
            throw new JsonParseException("El JSON no tiene propiedad 'tipo'");
        }

        String tipo = jsonObject.get("tipo").getAsString();
        

        String fechaStr = jsonObject.get("fechaVencimiento").getAsString();
        LocalDate fecha = LocalDate.parse(fechaStr);


        String nombre = jsonObject.get("nombreComercial").getAsString();
        String concentracion = jsonObject.get("concentracion").getAsString();

        try {

            switch (tipo) {
                case "QUIMICO": 
                case "Quimico":
                    String advertencia = jsonObject.get("tipoAdvertencia").getAsString();
                    return new ProductoQuimico(nombre, concentracion, fecha, advertencia);
                    
                case "ECOLOGICO":
                case "Ecologico":
                    String etiqueta = jsonObject.get("etiquetaEcologica").getAsString();
                    return new ProductoEcologico(nombre, concentracion, fecha, etiqueta);
                    
                default:
                    throw new JsonParseException("Tipo desconocido: " + tipo);
            }
        }catch(ProductoInvalidoException e){
            throw new JsonParseException("Error al crear producto (datos invalidos): " + e.getMessage());
            
        
        } catch (Exception e) {
            throw new JsonParseException("Error general al deserializar: " + e.getMessage());
        }
    }
}