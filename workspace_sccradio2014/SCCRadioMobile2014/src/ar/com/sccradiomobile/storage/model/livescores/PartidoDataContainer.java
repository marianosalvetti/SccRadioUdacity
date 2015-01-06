package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mariano Salvetti
 */
public class PartidoDataContainer {

    @SerializedName("totales")
    private String totales;

    @SerializedName("cantidad")
    private String cantidad;

    @SerializedName("offset")
    private String offset;

    @SerializedName("limite")
    private String limite;

    @SerializedName("partidos")
    private ArrayList<Partido> partidos;

    @Override
    public String toString() {
        return "PartidoDataContainer{" +
                "totales='" + totales + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", offset='" + offset + '\'' +
                ", limite='" + limite + '\'' +
                ", partidos=" + partidos +
                '}';
    }

    public String getTotales() {
        return totales;
    }

    public void setTotales(String totales) {
        this.totales = totales;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getLimite() {
        return limite;
    }

    public void setLimite(String limite) {
        this.limite = limite;
    }

    public ArrayList<Partido> getPartidos() {
        return partidos;
    }

    public void setPartidos(ArrayList<Partido> partidos) {
        this.partidos = partidos;
    }
}
