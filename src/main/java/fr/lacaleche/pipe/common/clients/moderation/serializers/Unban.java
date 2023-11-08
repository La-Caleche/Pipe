package fr.lacaleche.pipe.common.clients.moderation.serializers;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.lacaleche.core.utils.seripet.annotations.Serializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Serializer(variables = {"author", "player", "date"})
public class Unban {
    @JsonProperty("author")
    private String author;
    @JsonProperty("player")
    private String player;

    @JsonProperty("date")
    private String date;

    public Unban() {
    }

    public Unban(String author, String player) {
        this.author = author;
        this.player = player;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getAuthor() {
        return this.author;
    }
    public String getPlayer() {
        return this.player;
    }
}
