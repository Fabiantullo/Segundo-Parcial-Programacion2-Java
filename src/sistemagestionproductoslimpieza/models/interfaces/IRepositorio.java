package sistemagestionproductoslimpieza.models.interfaces;

import java.util.List;
import java.util.function.Predicate;

public interface IRepositorio<T> {
    void agregar(T item);
    void actualizar(T item);
    void eliminar(T item);
    List<T> obtenerTodos();
    List<T> filtrar(Predicate<T> criterio);
    boolean existe(Predicate<T> criterio);
}