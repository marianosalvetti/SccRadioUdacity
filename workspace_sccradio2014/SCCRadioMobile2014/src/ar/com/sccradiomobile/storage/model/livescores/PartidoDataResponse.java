package ar.com.sccradiomobile.storage.model.livescores;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 *
 * Es el modelo que representa nuestro Wrapper principal de los Resultados en Vivo.
 * Puede ser mejorado, segun los datos que vengan desde el servidor y la API.
 */
public class PartidoDataResponse {

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
    private PartidoDataContainer datos;

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

    public PartidoDataContainer getDatos() {
        return datos;
    }

    public void setDatos(PartidoDataContainer datos) {
        this.datos = datos;
    }

    @Override
    public String toString() {
        return "PartidoDataResponse{" +
                "codigo='" + codigo + '\'' +
                ", estado='" + estado + '\'' +
                ", copyright='" + copyright + '\'' +
                ", attributionText='" + attributionText + '\'' +
                ", attributionHTML='" + attributionHTML + '\'' +
                ", datos=" + datos +
                '}';
    }
}
