package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mariano Salvetti
 */
public class DetalleJugadaDataContainer {

    @SerializedName("partido")
    private String partido;

    @SerializedName("local")
    private String local;

    @SerializedName("visitante")
    private String visitante;

    @SerializedName("escudo_local")
    private String escudoLocal;

    @SerializedName("escudo_visitante")
    private String escudoVisitante;

    @SerializedName("cuarto")
    private String cuarto;

    @SerializedName("finalizado")
    private boolean finalizado;

    @SerializedName("jugadas")
    private ArrayList<DetalleJugada> jugadas;

    public String getPartido() {
        return partido;
    }

    public void setPartido(String partido) {
        this.partido = partido;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getVisitante() {
        return visitante;
    }

    public void setVisitante(String visitante) {
        this.visitante = visitante;
    }

    public String getEscudoLocal() {
        return escudoLocal;
    }

    public void setEscudoLocal(String escudoLocal) {
        this.escudoLocal = escudoLocal;
    }

    public String getEscudoVisitante() {
        return escudoVisitante;
    }

    public void setEscudoVisitante(String escudoVisitante) {
        this.escudoVisitante = escudoVisitante;
    }

    public String getCuarto() {
        return cuarto;
    }

    public void setCuarto(String cuarto) {
        this.cuarto = cuarto;
    }

    public boolean getFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public ArrayList<DetalleJugada> getJugadas() {
        return jugadas;
    }

    public void setJugadas(ArrayList<DetalleJugada> jugadas) {
        this.jugadas = jugadas;
    }

    @Override
    public String toString() {
        return "DetalleJugadaDataContainer{" +
                "partido='" + partido + '\'' +
                ", local='" + local + '\'' +
                ", visitante='" + visitante + '\'' +
                ", escudoLocal='" + escudoLocal + '\'' +
                ", escudoVisitante='" + escudoVisitante + '\'' +
                ", cuarto='" + cuarto + '\'' +
                ", finalizado='" + finalizado + '\'' +
                ", jugadas=" + jugadas +
                '}';
    }
}
