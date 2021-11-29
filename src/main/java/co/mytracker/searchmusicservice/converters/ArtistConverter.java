package co.mytracker.searchmusicservice.converters;

import co.mytracker.searchmusicservice.models.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArtistConverter {

    public static Artist spotifyArtistToArtist(se.michaelthelin.spotify.model_objects.specification.Artist spotifyArtist) {
        return new Artist(spotifyArtist.getId(),
                spotifyArtist.getName(),
                spotifyArtist.getUri());
    }

    public static List<Artist> listSpotifyArtistToListArtist(Paging<se.michaelthelin.spotify.model_objects.specification.Artist> listSpotifyArtist) {
        return Stream.of(listSpotifyArtist.getItems())
                .map(spotifyArtist -> spotifyArtistToArtist(spotifyArtist))
                .collect(Collectors.toList());
    }

}
