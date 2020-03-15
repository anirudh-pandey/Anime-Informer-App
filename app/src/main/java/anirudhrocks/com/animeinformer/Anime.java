package anirudhrocks.com.animeinformer;

public class Anime {

    private String animeTitle, latestEpisode;

    public Anime(String animeTitle, String latestEpisode) {
        this.animeTitle = animeTitle;
        this.latestEpisode = latestEpisode;
    }

    public Anime(String animeTitle) {
        this(animeTitle, "--");
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
}
