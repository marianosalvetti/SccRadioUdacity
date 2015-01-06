package ar.com.sccradiomobile.storage.model.sccradio;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 */
public class Attachments {

    @SerializedName("id")
    public long id;

    @SerializedName("url")
    public String url;

    @SerializedName("slug")
    public String slug;

    @SerializedName("title")
    public String title;

    @Override
    public String toString() {
        return "Attachments{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
