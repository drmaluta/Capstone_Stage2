package com.maluta.newsnow.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 9/11/2018.
 */

public class Source implements Parcelable {
    @ColumnInfo(name = "source_id")
    @SerializedName("id")
    @Expose
    private String id;
    @ColumnInfo(name = "source_name")
    @SerializedName("name")
    @Expose
    private String name;


    /**
     * No args constructor for use in serialization
     *
     */
    @Ignore
    public Source() {
    }

    /**
     *
     * @param id
     * @param name
     */
    public Source(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public final static Creator<Source> CREATOR = new Creator<Source>() {


        @SuppressWarnings({"unchecked"})
        public Source createFromParcel(Parcel in) {
            return new Source(in);
        }

        public Source[] newArray(int size) {
            return (new Source[size]);
        }

    };

    private Source(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    public int describeContents() {
        return 0;
    }
}
