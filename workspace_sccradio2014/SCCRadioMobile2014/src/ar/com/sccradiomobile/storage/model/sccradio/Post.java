package ar.com.sccradiomobile.storage.model.sccradio;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Mariano Salvetti
 *
 */
public class Post {

    @SerializedName("id")
    public long id;

    @SerializedName("type")
    public String type;

    @SerializedName("slug")
    public String slug;

    @SerializedName("url")
    public String url;

    @SerializedName("status")
    public String status;

    @SerializedName("title")
    public String title;

    @SerializedName("title_plain")
    public String title_plain;

    @SerializedName("content")
    public String content;

    @SerializedName("excerpt")
    public String excerpt ;

    @SerializedName("date")
    public String date;

    @SerializedName("author")
    public Author author;

    @SerializedName("attachments")
     public ArrayList<Attachments> attachments;

    private Category category;

    public void setCategory(Category category) {
        this.category = category;
    }

    public ArrayList<Attachments> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachments> attachments) {
        this.attachments = attachments;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", slug='" + slug + '\'' +
                ", url='" + url + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
            //    ", title_plain='" + title_plain + '\'' +
            //    ", content='" + content + '\'' +
            //    ", excerpt='" + excerpt + '\'' +
                ", category='" + category+ '\'' +
                ", date='" + date + '\'' +
                ", attachments='" + attachments + '\'' +
                ", author=" + author +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_plain() {
        return title_plain;
    }

    public void setTitle_plain(String title_plain) {
        this.title_plain = title_plain;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }
}
