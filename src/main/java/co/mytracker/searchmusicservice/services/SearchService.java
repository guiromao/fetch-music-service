package co.mytracker.searchmusicservice.services;

import co.mytracker.searchmusicservice.models.Album;
import co.mytracker.searchmusicservice.models.Artist;

import java.util.List;

public interface SearchService {

    /**
     * Will retrieve a list of artists, given a string as argument
     *
     * @param name An artist's full or partial name
     * @return list of Artists that correspond to the given argument
     */
    List<Artist> searchArtists(String name);

    /**
     * Will retreive a list of albums, given an artist name
     *
     * @param name Artist's name
     * @return list of recent albums - released in the last 30 days
     */
    List<Album> searchAlbums(String name);

}
