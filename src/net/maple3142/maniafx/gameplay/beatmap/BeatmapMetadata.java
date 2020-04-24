package net.maple3142.maniafx.gameplay.beatmap;

import java.util.Map;

public class BeatmapMetadata {

    static BeatmapMetadata fromMap(Map<String, String> mp) {
        var meta = new BeatmapMetadata();
        if (mp.containsKey("Title")) {
            meta.setTitle(mp.get("Title"));
        }
        if (mp.containsKey("TitleUnicode")) {
            meta.setTitleUnicode(mp.get("TitleUnicode"));
        }
        if (mp.containsKey("Artist")) {
            meta.setArtist(mp.get("Artist"));
        }
        if (mp.containsKey("ArtistUnicode")) {
            meta.setArtistUnicode(mp.get("ArtistUnicode"));
        }
        if (mp.containsKey("Creator")) {
            meta.setCreator(mp.get("Creator"));
        }
        if (mp.containsKey("Version")) {
            meta.setVersion(mp.get("Version"));
        }
        return meta;
    }

    private String title;
    private String titleUnicode;
    private String artist;
    private String artistUnicode;
    private String creator;
    private String version;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleUnicode() {
        return titleUnicode;
    }

    public void setTitleUnicode(String titleUnicode) {
        this.titleUnicode = titleUnicode;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getArtistUnicode() {
        return artistUnicode;
    }

    public void setArtistUnicode(String artistUnicode) {
        this.artistUnicode = artistUnicode;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
