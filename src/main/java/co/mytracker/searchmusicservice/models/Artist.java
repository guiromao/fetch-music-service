package co.mytracker.searchmusicservice.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.michaelthelin.spotify.model_objects.specification.Image;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Artist {

    private String id;
    private String name;
    private String uri;
    private Image[] images;
    private Integer popularity;
    private Integer numberFollowers;

}
