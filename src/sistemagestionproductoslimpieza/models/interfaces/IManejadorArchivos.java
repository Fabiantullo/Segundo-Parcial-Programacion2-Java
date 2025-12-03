package sistemagestionproductoslimpieza.models.interfaces;

import java.io.IOException;
import java.util.List;

public interface IManejadorArchivos<T> {
    void guardar(String rutaArchivo, List<T> datos) throws IOException;
    List<T> cargar(String rutaArchivo) throws IOException;
}