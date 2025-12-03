package sistemagestionproductoslimpieza.models.entities;

import sistemagestionproductoslimpieza.exceptions.ProductoInvalidoException;
import java.time.LocalDate;
import sistemagestionproductoslimpieza.exceptions.AdvertenciaVaciaException;
import sistemagestionproductoslimpieza.exceptions.ConcentracionInvalidaException;
import sistemagestionproductoslimpieza.exceptions.FechaVencimientoInvalidaException;
import sistemagestionproductoslimpieza.exceptions.NombreProductoVacioException;
import sistemagestionproductoslimpieza.models.enums.TipoProducto;

public class ProductoQuimico extends Producto {
    
    private String tipoAdvertencia; 

    public ProductoQuimico(String nombreComercial, String concentracion, LocalDate fechaVencimiento, String tipoAdvertencia) 
            throws ProductoInvalidoException, NombreProductoVacioException, AdvertenciaVaciaException, ConcentracionInvalidaException, FechaVencimientoInvalidaException{
        super(nombreComercial, concentracion, fechaVencimiento);
        setTipoAdvertencia(tipoAdvertencia);
    }

    public String getTipoAdvertencia() {
        return tipoAdvertencia;
    }

    public void setTipoAdvertencia(String tipoAdvertencia) throws AdvertenciaVaciaException {
        if (tipoAdvertencia == null || tipoAdvertencia.trim().isEmpty()) {
            throw new AdvertenciaVaciaException("Debe especificar una advertencia (ej: Toxico).");
        }
        this.tipoAdvertencia = tipoAdvertencia;
    }

    @Override
    public TipoProducto getTipo() {
        return TipoProducto.QUIMICO;
    }

    @Override
    public String getInformacionEspecifica() {
        return "Advertencia: " + tipoAdvertencia;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" { Advertencia: ").append(tipoAdvertencia).append("}");
        return sb.toString();
    }
}
