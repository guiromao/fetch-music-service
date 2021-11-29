package co.mytracker.searchmusicservice.controllers;

import co.mytracker.searchmusicservice.dtos.RequestDto;
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

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/artists")
    public ResponseEntity<List<Artist>> searchArtists(@RequestBody RequestDto request) {
        return new ResponseEntity<>(searchService.searchArtists(request.getArtistName()), HttpStatus.OK);
    }

    @GetMapping("/albums")
    public ResponseEntity<List<Album>> searchAlbums(@RequestBody RequestDto request) {
        return new ResponseEntity<>(searchService.searchAlbums(request.getArtistName()), HttpStatus.OK);
    }

}
