package co.mytracker.searchmusicservice.services;

import co.mytracker.searchmusicservice.models.Album;
import co.mytracker.searchmusicservice.models.Artist;

import java.util.List;

public interface SearchService {

    List<Artist> searchArtists(String name);

    List<Album> searchAlbums(String name);

}
