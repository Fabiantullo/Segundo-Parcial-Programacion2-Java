package sistemagestionproductoslimpieza.models.entities;

import sistemagestionproductoslimpieza.exceptions.ProductoInvalidoException;
import java.time.LocalDate;
import sistemagestionproductoslimpieza.exceptions.ConcentracionInvalidaException;
import sistemagestionproductoslimpieza.exceptions.EtiquetaEcologicaVaciaExeption;
import sistemagestionproductoslimpieza.exceptions.FechaVencimientoInvalidaException;
import sistemagestionproductoslimpieza.exceptions.NombreProductoVacioException;
import sistemagestionproductoslimpieza.models.enums.TipoProducto;

public class ProductoEcologico extends Producto {
    
    private String etiquetaEcologica; 

    public ProductoEcologico(String nombreComercial, String concentracion, LocalDate fechaVencimiento, String etiquetaEcologica) 
            throws ProductoInvalidoException, NombreProductoVacioException, EtiquetaEcologicaVaciaExeption, ConcentracionInvalidaException, FechaVencimientoInvalidaException{
        super(nombreComercial, concentracion, fechaVencimiento);
        setEtiquetaEcologica(etiquetaEcologica);
    }

    public String getEtiquetaEcologica() {
        return this.etiquetaEcologica;
    }

    public void setEtiquetaEcologica(String etiquetaEcologica) throws EtiquetaEcologicaVaciaExeption {
        if (etiquetaEcologica == null || etiquetaEcologica.trim().isEmpty()) {
            throw new EtiquetaEcologicaVaciaExeption("Debe especificar la etiqueta ecologica.");
        }
        this.etiquetaEcologica = etiquetaEcologica;
    }

    @Override
    public TipoProducto getTipo() {
        return TipoProducto.ECOLOGICO;
    }

    @Override
    public String getInformacionEspecifica() {
        return "Etiqueta: " + etiquetaEcologica;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" {Etiqueta Ecologica: ").append(this.etiquetaEcologica).append("}");
        return sb.toString();
    }
}