/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.sql.Date;

/**
 *
 * @author Al$x
 */
public class Venta {
    
    private String documento;
    private String caja;
    private String numero;
    private Date fecha;
    private String estatus;
    private String vendedor;
    private double precio;
    private double importe;
    private String concepto;
    private double impuesto;
    private double total;

    public Venta(String documento, String caja, String numero, Date fecha, String estatus, String vendedor, double precio, double importe, String concepto, double impuesto, double total) {
        this.documento = documento;
        this.caja = caja;
        this.numero = numero;
        this.fecha = fecha;
        this.estatus = estatus;
        this.vendedor = vendedor;
        this.precio = precio;
        this.importe = importe;
        this.concepto = concepto;
        this.impuesto = impuesto;
        this.total = total;
    }

    public Venta() {
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getVendedor() {
        return vendedor;
    }

    public void setVendedor(String vendedor) {
        this.vendedor = vendedor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public double getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(double impuesto) {
        this.impuesto = impuesto;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
    
    
    
}
