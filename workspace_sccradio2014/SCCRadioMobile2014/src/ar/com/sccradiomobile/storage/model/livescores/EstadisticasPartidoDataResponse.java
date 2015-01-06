package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 *
 * Es el modelo que representa nuestro Wrapper principal de los Resultados en Vivo.
 * Puede ser mejorado, segun los datos que vengan desde el servidor y la API.
 */
public class EstadisticasPartidoDataResponse {

    @SerializedName("codigo")
    private String codigo;

    @SerializedName("estado")
    private String estado;

    @SerializedName("copyright")
    private String copyright;

    @SerializedName("attributionText")
    private String attributionText;

    @SerializedName("attributionHTML")
    private String attributionHTML;

    @SerializedName("datos")
    private EstadisticasPartidoDataContainer datos;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getAttributionText() {
        return attributionText;
    }

    public void setAttributionText(String attributionText) {
        this.attributionText = attributionText;
    }

    public String getAttributionHTML() {
        return attributionHTML;
    }

    public void setAttributionHTML(String attributionHTML) {
        this.attributionHTML = attributionHTML;
    }

    public EstadisticasPartidoDataContainer getDatos() {
        return datos;
    }

    public void setDatos(EstadisticasPartidoDataContainer datos) {
        this.datos = datos;
    }

    @Override
    public String toString() {
        return "EstadisticasPartidoDataResponse{" +
                "codigo='" + codigo + '\'' +
                ", estado='" + estado + '\'' +
            //    ", copyright='" + copyright + '\'' +
            //    ", attributionText='" + attributionText + '\'' +
            //    ", attributionHTML='" + attributionHTML + '\'' +
                ", datos=" + datos +
                '}';
    }
}
