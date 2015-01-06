package ar.com.sccradiomobile.storage.model.sccradio;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mariano Salvetti
 */
public class Category {

    @SerializedName("id")
    public long id;

    @SerializedName("slug")
    public String slug;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
