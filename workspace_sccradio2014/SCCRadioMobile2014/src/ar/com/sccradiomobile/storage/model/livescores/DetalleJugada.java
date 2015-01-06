package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 */
public class DetalleJugada {

    @SerializedName("jugadaId")
    private String jugadaId;

    @SerializedName("periodo")
    private String periodo;

    @SerializedName("tiempo")
    private String tiempo;

    @SerializedName("equipo")
    private String equipo;

    @SerializedName("jugador")
    private String jugador;

    @SerializedName("accion")
    private String accion;

    @SerializedName("score")
    private String score;

    @SerializedName("others")
    private OtherInfoPartido others;

    @Override
    public String toString() {
        return "DetalleJugada{" +
                "jugadaId='" + jugadaId + '\'' +
                ", periodo='" + periodo + '\'' +
                ", tiempo='" + tiempo + '\'' +
                ", equipo='" + equipo + '\'' +
                ", jugador='" + jugador + '\'' +
                ", accion='" + accion + '\'' +
                ", score='" + score + '\'' +
                ", others=" + others +
                '}';
    }

    public String getJugadaId() {
        return jugadaId;
    }

    public void setJugadaId(String jugadaId) {
        this.jugadaId = jugadaId;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getJugador() {
        return jugador;
    }

    public void setJugador(String jugador) {
        this.jugador = jugador;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public OtherInfoPartido getOthers() {
        return others;
    }

    public void setOthers(OtherInfoPartido others) {
        this.others = others;
    }
}
