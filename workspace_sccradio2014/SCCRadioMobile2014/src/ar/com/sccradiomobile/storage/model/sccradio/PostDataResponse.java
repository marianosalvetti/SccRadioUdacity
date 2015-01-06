package ar.com.sccradiomobile.storage.model.sccradio;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mariano Salvetti
 *
 * Es el modelo que representa nuestro Wrapper principal de las Noticias
 * Puede ser mejorado, segun los posts que vengan desde el servidor y la API.
 */
public class PostDataResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("count")
    private String count;

    @SerializedName("count_total")
    private String count_total;

    @SerializedName("pages")
    private String pages;

    @SerializedName("posts")
    private ArrayList<Post> posts;

    @SerializedName("category")
    private Category category;

    public Category getCategory() {
        return category;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCountTotal() {
        return count_total;
    }

    public void setCountTotal(String count_total) {
        this.count_total = count_total;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public  ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts( ArrayList<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "PostDataResponse{" +
                "count='" + count + '\'' +
                ", status='" + status + '\'' +
                ", count_total='" + count_total + '\'' +
                ", pages='" + pages + '\'' +
                ", posts=" + posts +
                '}';
    }
}
