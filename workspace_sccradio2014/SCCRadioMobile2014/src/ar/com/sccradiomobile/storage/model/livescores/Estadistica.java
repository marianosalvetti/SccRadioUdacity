package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 *
 */

public class Estadistica {

    @SerializedName("jugadorId")
    private String jugadorId;

    @SerializedName("numero")
    private int numero;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("pts")
    private int puntos;

    @SerializedName("1c")
    private int libresConvertidos;

    @SerializedName("1l")
    private int libresLanzados;

    @SerializedName("1%")
    private int porcentajeLibres;

    @SerializedName("2c")
    private int doblesConvertidos;

    @SerializedName("2l")
    private int doblesLanzados;

    @SerializedName("2%")
    private int porcentajeDobles;

    @SerializedName("3c")
    private int triplesConvertidos;

    @SerializedName("3l")
    private int triplesLanzados;

    @SerializedName("3%")
    private int porcentajeTriples;

    @SerializedName("cc")
    private int conversionesConvertidas;

    @SerializedName("cl")
    private int conversionesLanzadas;

    @SerializedName("c%")
    private int porcentajeC;

    @SerializedName("rt")
    private int rebotesTotales;

    @SerializedName("ro")
    private int rebotesOfensivos;

    @SerializedName("rd")
    private int rebotesDefensivos;

    @SerializedName("as")
    private int asistencias;

    @SerializedName("ta")
    private int tapones;

    @SerializedName("pr")
    private int pelotasRecuperadas;

    @SerializedName("pp")
    private int pelotasPerdidas;

    @SerializedName("FP")
    private int faltasPersonales;

    @SerializedName("FR")
    private int faltasRecibidas;

    @SerializedName("TJ")
    private float tiempoDeJuego;

    @SerializedName("VAL")
    private int valoracion;

    @Override
    public String toString() {
        return "Estadistica{" +
                "jugadorId='" + jugadorId + '\'' +
                ", numero=" + numero +
                ", nombre='" + nombre + '\'' +
                ", pts=" + puntos +
                ", libresConvertidos=" + libresConvertidos +
                ", libresLanzados=" + libresLanzados +
                ", porcentajeLibres=" + porcentajeLibres +
                ", doblesConvertidos=" + doblesConvertidos +
                ", doblesLanzados=" + doblesLanzados +
                ", porcentajeDobles=" + porcentajeDobles +
                ", triplesConvertidos=" + triplesConvertidos +
                ", triplesLanzados=" + triplesLanzados +
                ", porcentajeTriples=" + porcentajeTriples +
                ", conversionesConvertidas=" + conversionesConvertidas +
                ", conversionesLanzadas=" + conversionesLanzadas +
                ", porcentajeC=" + porcentajeC +
                ", rebotesTotales=" + rebotesTotales +
                ", rebotesOfensivos=" + rebotesOfensivos +
                ", rebotesDefensivos=" + rebotesDefensivos +
                ", asistencias=" + asistencias +
                ", tapones=" + tapones +
                ", pelotasRecuperadas=" + pelotasRecuperadas +
                ", pelotasPerdidas=" + pelotasPerdidas +
                ", faltasPersonales=" + faltasPersonales +
                ", faltasRecibidas=" + faltasRecibidas +
                ", tiempoDeJuego=" + tiempoDeJuego +
                ", valoracion=" + valoracion +
                '}';
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getLibresConvertidos() {
        return libresConvertidos;
    }

    public void setLibresConvertidos(int libresConvertidos) {
        this.libresConvertidos = libresConvertidos;
    }

    public int getLibresLanzados() {
        return libresLanzados;
    }

    public void setLibresLanzados(int libresLanzados) {
        this.libresLanzados = libresLanzados;
    }

    public int getPorcentajeLibres() {
        return porcentajeLibres;
    }

    public void setPorcentajeLibres(int porcentajeLibres) {
        this.porcentajeLibres = porcentajeLibres;
    }

    public int getDoblesConvertidos() {
        return doblesConvertidos;
    }

    public void setDoblesConvertidos(int doblesConvertidos) {
        this.doblesConvertidos = doblesConvertidos;
    }

    public int getDoblesLanzados() {
        return doblesLanzados;
    }

    public void setDoblesLanzados(int doblesLanzados) {
        this.doblesLanzados = doblesLanzados;
    }

    public int getPorcentajeDobles() {
        return porcentajeDobles;
    }

    public void setPorcentajeDobles(int porcentajeDobles) {
        this.porcentajeDobles = porcentajeDobles;
    }

    public int getTriplesConvertidos() {
        return triplesConvertidos;
    }

    public void setTriplesConvertidos(int triplesConvertidos) {
        this.triplesConvertidos = triplesConvertidos;
    }

    public int getTriplesLanzados() {
        return triplesLanzados;
    }

    public void setTriplesLanzados(int triplesLanzados) {
        this.triplesLanzados = triplesLanzados;
    }

    public int getPorcentajeTriples() {
        return porcentajeTriples;
    }

    public void setPorcentajeTriples(int porcentajeTriples) {
        this.porcentajeTriples = porcentajeTriples;
    }

    public int getConversionesConvertidas() {
        return conversionesConvertidas;
    }

    public void setConversionesConvertidas(int conversionesConvertidas) {
        this.conversionesConvertidas = conversionesConvertidas;
    }

    public int getConversionesLanzadas() {
        return conversionesLanzadas;
    }

    public void setConversionesLanzadas(int conversionesLanzadas) {
        this.conversionesLanzadas = conversionesLanzadas;
    }

    public int getPorcentajeC() {
        return porcentajeC;
    }

    public void setPorcentajeC(int porcentajeC) {
        this.porcentajeC = porcentajeC;
    }

    public int getRebotesTotales() {
        return rebotesTotales;
    }

    public void setRebotesTotales(int rebotesTotales) {
        this.rebotesTotales = rebotesTotales;
    }

    public int getRebotesOfensivos() {
        return rebotesOfensivos;
    }

    public void setRebotesOfensivos(int rebotesOfensivos) {
        this.rebotesOfensivos = rebotesOfensivos;
    }

    public int getRebotesDefensivos() {
        return rebotesDefensivos;
    }

    public void setRebotesDefensivos(int rebotesDefensivos) {
        this.rebotesDefensivos = rebotesDefensivos;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(int asistencias) {
        this.asistencias = asistencias;
    }

    public int getTapones() {
        return tapones;
    }

    public void setTapones(int tapones) {
        this.tapones = tapones;
    }

    public int getPelotasRecuperadas() {
        return pelotasRecuperadas;
    }

    public void setPelotasRecuperadas(int pelotasRecuperadas) {
        this.pelotasRecuperadas = pelotasRecuperadas;
    }

    public int getPelotasPerdidas() {
        return pelotasPerdidas;
    }

    public void setPelotasPerdidas(int pelotasPerdidas) {
        this.pelotasPerdidas = pelotasPerdidas;
    }

    public int getFaltasPersonales() {
        return faltasPersonales;
    }

    public void setFaltasPersonales(int faltasPersonales) {
        this.faltasPersonales = faltasPersonales;
    }

    public int getFaltasRecibidas() {
        return faltasRecibidas;
    }

    public void setFaltasRecibidas(int faltasRecibidas) {
        this.faltasRecibidas = faltasRecibidas;
    }

    public float getTiempoDeJuego() {
        return tiempoDeJuego;
    }

    public void setTiempoDeJuego(float tiempoDeJuego) {
        this.tiempoDeJuego = tiempoDeJuego;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public String getEstadisticaLibres() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getLibresConvertidos());
        sb.append("-");
        sb.append(this.getLibresLanzados());
        sb.append("(").append(this.getPorcentajeLibres()).append("%)");
        return  sb.toString();
    }

    public String getEstadisticaDobles() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getDoblesConvertidos());
        sb.append("-");
        sb.append(this.getDoblesLanzados());
        sb.append("(").append(this.getPorcentajeDobles()).append("%)");
        return  sb.toString();
    }

    public String getEstadisticaTriples() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTriplesConvertidos());
        sb.append("-");
        sb.append(this.getTriplesLanzados());
        sb.append("(").append(this.getPorcentajeTriples()).append("%)");
        return sb.toString();
    }

    public String getEstadisticaConversiones() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getConversionesConvertidas());
        sb.append("-");
        sb.append(this.getConversionesLanzadas());
        sb.append("(").append(this.getPorcentajeC()).append("%)");
        return  sb.toString();
    }

    public String getEstadisticarRebotesTotales() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getRebotesTotales());
        sb.append("(");
        sb.append(this.getRebotesDefensivos());
        sb.append("-").append(this.getRebotesOfensivos()).append(")");
        return  sb.toString();
    }
}
