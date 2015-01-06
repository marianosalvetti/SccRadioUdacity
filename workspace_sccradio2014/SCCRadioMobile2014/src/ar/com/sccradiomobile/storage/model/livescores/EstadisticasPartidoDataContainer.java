package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mariano Salvetti
 * TODO: trabajar en este modelo
 */
public class EstadisticasPartidoDataContainer {

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

    @SerializedName("puntos_local")
    private String puntosLocal;

    @SerializedName("puntos_visitante")
    private String puntosVisitante;

    public String getPuntosLocal() {
        return puntosLocal;
    }

    public void setPuntosLocal(String puntosLocal) {
        this.puntosLocal = puntosLocal;
    }

    public String getPuntosVisitante() {
        return puntosVisitante;
    }

    public void setPuntosVisitante(String puntosVisitante) {
        this.puntosVisitante = puntosVisitante;
    }

    @SerializedName("estadisticas_local")
    private ArrayList<Estadistica> estadisticas_local;

    @SerializedName("estadisticas_visitante")
    private ArrayList<Estadistica> estadisticas_visitante;

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

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    public ArrayList<Estadistica> getEstadisticas_local() {
        return estadisticas_local;
    }

    public void setEstadisticas_local(ArrayList<Estadistica> estadisticas_local) {
        this.estadisticas_local = estadisticas_local;
    }

    public ArrayList<Estadistica> getEstadisticas_visitante() {
        return estadisticas_visitante;
    }

    public void setEstadisticas_visitante(ArrayList<Estadistica> estadisticas_visitante) {
        this.estadisticas_visitante = estadisticas_visitante;
    }

    @Override
    public String toString() {
        return "EstadisticasPartidoDataContainer{" +
                "partido='" + partido + '\'' +
                ", local='" + local + '\'' +
                ", visitante='" + visitante + '\'' +
                ", puntosLocal='" + puntosLocal + '\'' +
                ", puntos visitante='" + puntosVisitante + '\'' +
                ", escudoLocal='" + escudoLocal + '\'' +
                ", escudoVisitante='" + escudoVisitante + '\'' +
                ", cuarto='" + cuarto + '\'' +
                ", finalizado=" + finalizado +
                ", estadisticas_local=" + estadisticas_local +
                ", estadisticas_visitante=" + estadisticas_visitante +
                '}';
    }
}
