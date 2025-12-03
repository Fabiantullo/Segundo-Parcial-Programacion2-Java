package sistemagestionproductoslimpieza.models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import sistemagestionproductoslimpieza.models.entities.Producto;
import sistemagestionproductoslimpieza.models.interfaces.IRepositorio;


public class RepositorioProductos implements IRepositorio<Producto> {
    
    private List<Producto> listaProductos;
    
    public RepositorioProductos() {
        this.listaProductos = new ArrayList<>();
    }
    
    @Override
    public void agregar(Producto item) {
        listaProductos.add(item);
    }
    
    @Override
    public void actualizar(Producto item) {
        int index = listaProductos.indexOf(item);
        if (index >= 0) {
            listaProductos.set(index, item);
        }
    }
    
    @Override
    public void eliminar(Producto item) {
        listaProductos.remove(item);
    }
    
    @Override
    public List<Producto> obtenerTodos() {
        return new ArrayList<>(listaProductos);
    }
    
    @Override
    public List<Producto> filtrar(Predicate<Producto> criterio) {
        return listaProductos.stream().filter(criterio).collect(Collectors.toList());
    }
    
    @Override
    public boolean existe(Predicate<Producto> criterio) {
        return listaProductos.stream().anyMatch(criterio);
    }
}