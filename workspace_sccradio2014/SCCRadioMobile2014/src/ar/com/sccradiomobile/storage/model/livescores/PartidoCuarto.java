package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 * TODO: Mejorar este objeto a partir del modelo de datos de la API en el Server.
 */
public class PartidoCuarto {
    @SerializedName("local")
    private String local;

    @SerializedName("visitante")
    private String visitante;

    @SerializedName("finalizado")
    private boolean finalizado;

    @Override
    public String toString() {
        return "PartidoCuarto{" +
                "local='" + local + '\'' +
                ", visitante='" + visitante + '\'' +
                ", finalizado=" + finalizado +
                '}';
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

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
}
