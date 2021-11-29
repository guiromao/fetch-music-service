package co.mytracker.searchmusicservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.michaelthelin.spotify.model_objects.specification.Image;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {

    private String id;
    private String name;
    private String type;
    private Date releaseDate;
    private String spotifyUri;
    private Image[] images;

}
