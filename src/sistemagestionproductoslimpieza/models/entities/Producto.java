package sistemagestionproductoslimpieza.models.entities;

import sistemagestionproductoslimpieza.exceptions.ProductoInvalidoException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import sistemagestionproductoslimpieza.exceptions.ConcentracionInvalidaException;
import sistemagestionproductoslimpieza.exceptions.FechaVencimientoInvalidaException;
import sistemagestionproductoslimpieza.exceptions.NombreProductoVacioException;
import sistemagestionproductoslimpieza.models.enums.TipoProducto;
import sistemagestionproductoslimpieza.models.interfaces.SerializableJson;

public abstract class Producto extends SerializableJson {
    
    protected String nombreComercial;
    protected String concentracion;
    protected LocalDate fechaVencimiento;

    public Producto(String nombreComercial, String concentracion, LocalDate fechaVencimiento) throws ProductoInvalidoException, NombreProductoVacioException, ConcentracionInvalidaException, FechaVencimientoInvalidaException{
        setNombreComercial(nombreComercial);
        setConcentracion(concentracion);
        setFechaVencimiento(fechaVencimiento);
    }

    public String getNombreComercial() {
        return nombreComercial;
    }

    public void setNombreComercial(String nombreComercial) throws NombreProductoVacioException {
        if (nombreComercial == null || nombreComercial.trim().isEmpty()) {
            throw new NombreProductoVacioException("El nombre no puede estar vacío.");
        }
        this.nombreComercial = nombreComercial;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) throws ConcentracionInvalidaException {
        if (concentracion == null || concentracion.trim().isEmpty()) {
            throw new ConcentracionInvalidaException("La concentración no puede estar vacía.");
        }
        this.concentracion = concentracion;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) throws FechaVencimientoInvalidaException {
        if (fechaVencimiento == null) {
            throw new FechaVencimientoInvalidaException("La fecha de vencimiento es obligatoria.");
        }

        if (fechaVencimiento.isBefore(LocalDate.now())) {
            throw new FechaVencimientoInvalidaException("No se puede cargar un producto ya vencido.");
        }
        this.fechaVencimiento = fechaVencimiento;
    }
    

    public boolean estaProximoAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(60);
        return !fechaVencimiento.isBefore(hoy) && fechaVencimiento.isBefore(limite);
    }

    public abstract TipoProducto getTipo();
    public abstract String getInformacionEspecifica();


    @Override
    public int hashCode() {

        return Objects.hash(nombreComercial, concentracion, fechaVencimiento);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Producto other = (Producto) obj;
        return Objects.equals(nombreComercial, other.nombreComercial) &&
               Objects.equals(concentracion, other.concentracion) &&
               Objects.equals(fechaVencimiento, other.fechaVencimiento);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombreComercial).append(" - ").append(concentracion);
        sb.append(" (Vence: ").append(fechaVencimiento.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))).append(")");
        sb.append(" [").append(getTipo().getDescripcion()).append("]");
        return sb.toString();
    }
}
