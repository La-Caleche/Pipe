package fr.lacaleche.pipe.bukkit.tabs.scoreboard.interfaces;

import java.util.Collection;
import java.util.Map;

public interface Scoreboard {

    void registerTeam(String name, Map<String, Object> options);

    void unregisterTeam(String name);

    void updateTeam(String name, Map<String, Object> options);

}
