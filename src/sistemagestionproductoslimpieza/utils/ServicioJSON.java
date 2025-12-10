package sistemagestionproductoslimpieza.utils;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import sistemagestionproductoslimpieza.models.interfaces.IManejadorArchivos;

public class ServicioJSON<T> implements IManejadorArchivos<T> {

    private final Gson gson;
    private final Type listType;

    public ServicioJSON(Gson gson, Type listType) {
        this.gson = gson;
        this.listType = listType;
    }

    @Override
    public List<T> cargar(String rutaArchivo) throws IOException {
        if (!Files.exists(Paths.get(rutaArchivo))) {
            return new ArrayList<>();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            List<T> lista = gson.fromJson(br, listType);
            return lista != null ? lista : new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void guardar(String rutaArchivo, List<T> datos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
            gson.toJson(datos, listType, writer);

        }
    }
}
