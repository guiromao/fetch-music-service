package co.mytracker.searchmusicservice.services;

import co.mytracker.searchmusicservice.converters.AlbumConverter;
import co.mytracker.searchmusicservice.converters.ArtistConverter;
import co.mytracker.searchmusicservice.models.Album;
import co.mytracker.searchmusicservice.models.Artist;
import co.mytracker.searchmusicservice.utils.DateUtils;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.AlbumSimplified;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:credentials.properties")
public class SearchServiceImpl implements SearchService {

    private static final int THIRTY_DAYS = 30;

    private SpotifyApi spotifyApi;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Override
    public List<Artist> searchArtists(String artistName) {
        refreshSpotifyApi();

        SearchArtistsRequest searchRequest = spotifyApi.searchArtists(artistName)
                .limit(10)
                .build();

        try {
            Paging<se.michaelthelin.spotify.model_objects.specification.Artist> artists = searchRequest.execute();

            return ArtistConverter.listSpotifyArtistToListArtist(artists);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Couldn't retrieve artists for the name provided: " + e);
        }
    }

    @Override
    public List<Album> searchAlbums(String artistName) {
        refreshSpotifyApi();

        SearchAlbumsRequest searchAlbumsRequest = spotifyApi.searchAlbums(artistName)
                .limit(5)
                .build();

        try {
            Paging<AlbumSimplified> spotifyAlbums = searchAlbumsRequest.execute();
            List<Album> albums = AlbumConverter.listSpotifyAlbumToListAlbum(spotifyAlbums);

            return fetchRecentAlbums(albums);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Couldn't fetch albums for the artist name provided: " + e);
        }
    }

    private List<Album> fetchRecentAlbums(List<Album> albums) {
        return albums.stream()
                .filter(album -> LocalDate.now().minusDays(THIRTY_DAYS).isBefore(DateUtils.localDateOf(album.getReleaseDate())))
                .collect(Collectors.toList());
    }

    private void refreshSpotifyApi() {
        try {
            spotifyApi = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .setRedirectUri(new URI("http://mytracker.co"))
                    .build();

            ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
            ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

        } catch (IOException | SpotifyWebApiException | ParseException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
