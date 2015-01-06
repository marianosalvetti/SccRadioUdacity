package ar.com.sccradiomobile.storage.model.picasa;

import com.google.gson.annotations.SerializedName;

/**
 * Entry model.
 *
 * @author Mariano Salvetti
 */
public class Entry {
    @SerializedName("media$group")
    private EntryMediaGroup mediaGroup;

    public EntryMediaGroup getMediaGroup() {
        return mediaGroup;
    }

    public void setMediaGroup(EntryMediaGroup mediaGroup) {
        this.mediaGroup = mediaGroup;
    }
}