package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 */
public class Partido {

    @SerializedName("partido")
    private String partido;

    @SerializedName("local")
    private String local;

    @SerializedName("escudo_local")
    private String escudo_local;

    @SerializedName("visitante")
    private String visitante;

    @SerializedName("escudo_visitante")
    private String escudo_visitante;

    @SerializedName("finalizado")
    private boolean finalizado;

    @SerializedName("q1")
    private PartidoCuarto q1;

    @SerializedName("q2")
    private PartidoCuarto q2;

    @SerializedName("q3")
    private PartidoCuarto q3;

    @SerializedName("q4")
    private PartidoCuarto q4;

    @Override
    public String toString() {
        return "Partido{" +
                "partido='" + partido + '\'' +
                ", finalizado='" + finalizado + '\'' +
                ", local='" + local + '\'' +
                ", visitante='" + visitante + '\'' +
                ", escudo_local='" + escudo_local + '\'' +
                ", escudo_visitante='" + escudo_visitante + '\'' +
                ", q1=" + q1 +
                ", q2=" + q2 +
                ", q3=" + q3 +
                ", q4=" + q4 +
                '}';
    }

    public String getEscudo_local() {
        return escudo_local;
    }

    public void setEscudo_local(String escudo_local) {
        this.escudo_local = escudo_local;
    }

    public String getEscudo_visitante() {
        return escudo_visitante;
    }

    public void setEscudo_visitante(String escudo_visitante) {
        this.escudo_visitante = escudo_visitante;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

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

    public PartidoCuarto getQ1() {
        return q1;
    }

    public void setQ1(PartidoCuarto q1) {
        this.q1 = q1;
    }

    public PartidoCuarto getQ2() {
        return q2;
    }

    public void setQ2(PartidoCuarto q2) {
        this.q2 = q2;
    }

    public PartidoCuarto getQ3() {
        return q3;
    }

    public void setQ3(PartidoCuarto q3) {
        this.q3 = q3;
    }

    public PartidoCuarto getQ4() {
        return q4;
    }

    public void setQ4(PartidoCuarto q4) {
        this.q4 = q4;
    }
}
