package fr.lacaleche.pipe.bukkit.modules.hephaestus.interfaces;

import fr.lacaleche.core.utils.Callback;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.ModelRegistry;
import org.bukkit.Location;
import team.unnamed.hephaestus.Bone;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.BoneView;
import team.unnamed.hephaestus.bukkit.BukkitModelEngine;
import team.unnamed.hephaestus.bukkit.ModelView;

import java.util.List;

public interface HephaestusManager {

    ModelRegistry getModelRegistry();

    BukkitModelEngine getModelEngine();

    ModelView getModelEntityById(String id);

    void start();

    void stop();

    ModelView create(Model model, Location location);

    void spawnPlayer(String player, Location location, Callback<String> callback);

    void remove(ModelView entity);

    List<Bone> getBones(Model model);

    List<Bone> getBones(ModelView modelView);

    List<BoneView> getBoneChildren(ModelView entity, Bone bone);

    List<BoneView> getBoneChildren(ModelView entity, String bone);

}
