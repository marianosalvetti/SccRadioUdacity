package ar.com.sccradiomobile.storage.model.picasa;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti on 31/12/2014.
 */
public class MediaGroupDescription {
    @SerializedName("$t")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
