package anirudhrocks.com.animeinformer;

public class Anime {

    private String animeTitle, latestEpisode, languageType;


    public Anime(String animeTitle, String languageType) {
        this(animeTitle, "--", languageType);
    }


    public Anime(String animeTitle, String latestEpisode, String languageType) {
        this.animeTitle = animeTitle;
        this.latestEpisode = latestEpisode;
        this.languageType = languageType;
    }


    public String getAnimeTitle() {
        return animeTitle;
    }

    public void setAnimeTitle(String animeTitle) {
        this.animeTitle = animeTitle;
    }

    public String getLatestEpisode() {
        return latestEpisode;
    }

    public void setLatestEpisode(String latestEpisode) {
        this.latestEpisode = latestEpisode;
    }

    public String getLanguageType() {
        return languageType;
    }

    public void setLanguageType(String languageType) {
        this.languageType = languageType;
    }

}
