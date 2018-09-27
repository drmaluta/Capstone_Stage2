package com.maluta.newsnow.models;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 9/11/2018.
 */

@Entity(tableName = "article")
public class Article implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @Embedded
    @SerializedName("source")
    @Expose
    private Source source;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("urlToImage")
    @Expose
    private String urlToImage;
    @SerializedName("publishedAt")
    @Expose
    private String publishedAt;
    private boolean favorite = false;

    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Article() {
    }

    /**
     *
     * @param publishedAt
     * @param author
     * @param urlToImage
     * @param title
     * @param source
     * @param description
     * @param url
     */
    public Article(long id, Source source, String author, String title, String description, String url, String urlToImage, String publishedAt, boolean favorite) {
        super();
        this.id = id;
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.favorite = favorite;
    }

    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        if(TextUtils.isEmpty(urlToImage))
            return null;
        // Some images do not have http: in their url
        if(!(urlToImage.startsWith("https:") || urlToImage.startsWith("http:")))
            urlToImage = "https:" + urlToImage;
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public final static Parcelable.Creator<Article> CREATOR = new Creator<Article>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return (new Article[size]);
        }

    };

    private Article(Parcel in) {
        this.id = in.readLong();
        this.source = ((Source) in.readValue((Source.class.getClassLoader())));
        this.author = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.url = in.readString();
        this.urlToImage = in.readString();
        this.publishedAt = in.readString();
        this.favorite = in.readByte() != 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeValue(source);
        dest.writeString(author);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(urlToImage);
        dest.writeString(publishedAt);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    public int describeContents() {
        return 0;
    }
}
