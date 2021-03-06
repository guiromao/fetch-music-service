package co.mytracker.searchmusicservice.services;

import static co.mytracker.searchmusicservice.converters.AlbumConverter.convertToAppAlbumList;
import static co.mytracker.searchmusicservice.converters.ArtistConverter.convertToAppArtistList;

import co.mytracker.searchmusicservice.models.Album;
import co.mytracker.searchmusicservice.models.Artist;

import static co.mytracker.searchmusicservice.utils.DateUtils.localDateOf;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:credentials.properties")
public class SearchServiceImpl implements SearchService {

    private static final int THIRTY_DAYS = 30;
    private static final int NUMBER_RESULTS = 5;

    private SpotifyApi spotifyApi;

    @Value("${clientId}")
    private String clientId;

    @Value("${clientSecret}")
    private String clientSecret;

    @Override
    public List<Artist> searchArtists(String artistName) {
        refreshSpotifyApi();

        SearchArtistsRequest searchRequest = spotifyApi.searchArtists(artistName)
                .limit(NUMBER_RESULTS)
                .build();

        try {
            Paging<se.michaelthelin.spotify.model_objects.specification.Artist> artists = searchRequest.execute();

            return convertToAppArtistList(artists);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Couldn't retrieve artists for the name provided: " + e);
        }
    }

    @Override
    public List<Album> searchAlbums(String artistName) {
        refreshSpotifyApi();

        SearchAlbumsRequest searchAlbumsRequest = spotifyApi.searchAlbums(artistName)
                .limit(NUMBER_RESULTS)
                .build();

        try {
            Paging<AlbumSimplified> spotifyAlbums = searchAlbumsRequest.execute();
            List<Album> albums = convertToAppAlbumList(spotifyAlbums);

            return selectRecentAlbums(albums);
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            throw new RuntimeException("Couldn't fetch albums for the artist name provided: " + e);
        }
    }

    /**
     * Fetching albums that have been released within the last 30 days
     *
     * @param albums albums retrieved from the Spotify API
     * @return recent albums
     */
    private List<Album> selectRecentAlbums(List<Album> albums) {
        return albums.stream()
                .filter(album -> LocalDate.now().minusDays(THIRTY_DAYS)
                        .isBefore(localDateOf(album.getReleaseDate())))
                .filter(distinctByAlbumName(a -> a.getName()))
                .collect(Collectors.toList());
    }

    //Getting a new access token for Spotify API requests
    private void refreshSpotifyApi() {
        try {
            rebuildSpotifyApi();
            setAccessToken();
        } catch (IOException | SpotifyWebApiException | ParseException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void rebuildSpotifyApi() throws URISyntaxException {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(new URI("http://mytracker.co"))
                .build();
    }

    private void setAccessToken() throws IOException, SpotifyWebApiException, ParseException {
        ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials().build();
        ClientCredentials clientCredentials = clientCredentialsRequest.execute();
        spotifyApi.setAccessToken(clientCredentials.getAccessToken());
    }

    private <T> Predicate<T> distinctByAlbumName(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

}
