package co.mytracker.searchmusicservice.controllers;

import co.mytracker.searchmusicservice.models.Album;
import co.mytracker.searchmusicservice.models.Artist;
import co.mytracker.searchmusicservice.services.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/artists")
    public ResponseEntity<List<Artist>> searchArtists(@RequestBody Map<String, String> searchQuery) {
        return new ResponseEntity<>(searchService.searchArtists(searchQuery.get("artistName")), HttpStatus.OK);
    }

    @GetMapping("/albums")
    public ResponseEntity<List<Album>> searchAlbums(@RequestBody Map<String, String> searchQuery) {
        return new ResponseEntity<>(searchService.searchAlbums(searchQuery.get("artistName")), HttpStatus.OK);
    }

}
