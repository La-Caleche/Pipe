package fr.lacaleche.pipe.common.clients.moderation.serializers;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.lacaleche.core.utils.serializer.annotations.Serializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Serializer(variables = {"author", "player", "reason", "date"})
public class Kick {
    @JsonProperty("author")
    private String author;
    @JsonProperty("player")
    private String player;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("date")
    private String date;

    public Kick() {
    }

    public Kick(String author, String player, String reason) {
        this.author = author;
        this.player = player;
        this.reason = reason;
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }


    public String getAuthor() {
        return this.author;
    }
    public String getPlayer() {
        return this.player;
    }

    public String getReason() {
        return this.reason;
    }

    public String getDate() {
        return this.date;
    }
}
