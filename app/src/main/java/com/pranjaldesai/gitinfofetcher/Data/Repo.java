package com.pranjaldesai.gitinfofetcher.Data;


import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Repo extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private int stargazers_count, watchers_count, forks, size, open_issues;
    private String html_url, description, name, language;
    private Owner owner;

    public Repo(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public int getStargazers_count() {
        return stargazers_count;
    }

    public void setStargazers_count(int stargazers_count) {
        this.stargazers_count = stargazers_count;
    }

    public int getWatchers_count() {
        return watchers_count;
    }

    public void setWatchers_count(int watchers_count) {
        this.watchers_count = watchers_count;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    public int getOpen_issues() {
        return open_issues;
    }

    public void setOpen_issues(int open_issues) {
        this.open_issues = open_issues;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(stargazers_count);
        parcel.writeInt(watchers_count);
        parcel.writeInt(size);
        parcel.writeInt(forks);
        parcel.writeInt(open_issues);
        parcel.writeString(html_url);
        parcel.writeString(description);
        parcel.writeString(name);
        parcel.writeString(language);
        parcel.writeInt(id);
    }

    protected Repo(Parcel in) {
        this.stargazers_count= in.readInt();
        this.watchers_count= in.readInt();
        this.size= in.readInt();
        this.forks= in.readInt();
        this.open_issues= in.readInt();
        this.html_url= in.readString();
        this.description= in.readString();
        this.name= in.readString();
        this.language= in.readString();
        this.id= in.readInt();
    }

    public static final Creator<Repo> CREATOR = new Creator<Repo>() {
        @Override
        public Repo createFromParcel(Parcel in) {
            return new Repo(in);
        }

        @Override
        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };
}
