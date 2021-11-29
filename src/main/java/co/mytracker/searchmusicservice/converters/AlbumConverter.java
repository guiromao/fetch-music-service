package co.mytracker.searchmusicservice.converters;

import co.mytracker.searchmusicservice.models.Album;
import co.mytracker.searchmusicservice.utils.DateUtils;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AlbumConverter {

    public static Album spotifyAlbumToAlbum(AlbumSimplified spotifyAlbum) {
        return new Album(spotifyAlbum.getId(),
                spotifyAlbum.getName(),
                DateUtils.stringToDate(spotifyAlbum.getReleaseDate()),
                spotifyAlbum.getUri());
    }

    public static List<Album> listSpotifyAlbumToListAlbum(Paging<AlbumSimplified> spotifyAlbums) {
        return Stream.of(spotifyAlbums.getItems())
                .map(spotifyAlbum -> spotifyAlbumToAlbum(spotifyAlbum))
                .collect(Collectors.toList());
    }

}
