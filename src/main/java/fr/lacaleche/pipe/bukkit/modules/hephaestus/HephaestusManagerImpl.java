package fr.lacaleche.pipe.bukkit.modules.hephaestus;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.Callback;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.interfaces.HephaestusManager;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.track.ModelViewPersistenceHandlerImpl;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.bukkit.BukkitModelEngine;
import team.unnamed.hephaestus.bukkit.ModelView;
import team.unnamed.hephaestus.bukkit.v1_20_R1.BukkitModelEngine_v1_20_R1;
import team.unnamed.hephaestus.reader.blockbench.BBModelReader;
import team.unnamed.hephaestus.view.modifier.BoneModifierType;
import team.unnamed.hephaestus.view.modifier.player.rig.PlayerBoneType;
import team.unnamed.hephaestus.view.modifier.player.rig.PlayerRig;
import team.unnamed.hephaestus.view.modifier.player.skin.Skin;
import team.unnamed.hephaestus.view.modifier.player.skin.SkinProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;

public class HephaestusManagerImpl implements HephaestusManager {

    private final HephaestusModule module;
    private final ModelRegistry modelRegistry;
    private final BukkitModelEngine modelEngine;
    private final SkinProvider skinProvider;

    public HephaestusManagerImpl(HephaestusModule module) {
        this.module = module;
        this.modelRegistry = new ModelRegistry();
        this.modelEngine = BukkitModelEngine_v1_20_R1.create(Pipe.getBukkit().getPlugin(), new ModelViewPersistenceHandlerImpl(this.modelRegistry));
        this.skinProvider = SkinProvider.mojang();
    }

    @Override
    public void start() {
        File modelsDirectory = Core.get().getDirectory("models");
        if (modelsDirectory == null) return ;

        try (Stream<Path> paths = Files.walk(Paths.get(modelsDirectory.getPath()))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".bbmodel"))
                    .sorted()
                    .forEach(path -> {
                        Model model = loadModel(path.toFile());
                        if (model == null) return ;
                        this.getModelRegistry().registerModel(model);
                        Logger.info("Loaded model %s", path.getFileName());
                    });
        } catch (Exception e) {
            Logger.warning("Failed to load models: %s", e.getMessage());
        }

        Logger.info("Loaded %d models: %s", this.getModelRegistry().models().size(), String.join(", ", this.getModelRegistry().modelNames()));
    }

    @Override
    public void stop() {
        this.getModelEngine().close();
        this.getModelRegistry().models().clear();
    }

    @Override
    public ModelView create(Model model, Location location) {
        ModelView view = this.getModelEngine().createViewAndTrack(model, location);
        Pipe.get().getTaskManager().newTask(builder -> builder.run(task -> view.tickAnimations()).loop(true).async(true));
        return view;
    }

    @Override
    public void spawnPlayer(String player, Location location, Callback<Boolean> callback) {
        Pipe.asyncZeroTick(task -> {
            Skin skin = this.skinProvider.fetch(player);
            if (skin == null) {
                if (callback != null) callback.done(false);
                return ;
            }


            Pipe.get().getTaskManager().newTask(builder -> builder.run(t -> {
                final ModelView view = create(this.modelRegistry.model("jeqo"), location);
                final PlayerRig rig = PlayerRig.detailed();

                view.bones().forEach(boneView -> {
                    final PlayerBoneType type = rig.get(boneView.name());
                    if (type == null) return ;

                    boneView.configure(BoneModifierType.PLAYER_PART, part -> {
                        part.type(type);
                        part.skin(skin);
                    });
                });

                if (callback != null) callback.done(true);
            }));
        });
    }

    @Override
    public void remove(ModelView entity) {
        if (entity.base() == null) return ;
        entity.base().remove();
    }

    @Override
    public ModelRegistry getModelRegistry() {
        return this.modelRegistry;
    }

    @Override
    public BukkitModelEngine getModelEngine() {
        return this.modelEngine;
    }

    @Override
    public ModelView getModelEntityById(String id) {
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException ignored) {
            return null;
        }

        final var base = Bukkit.getEntity(uuid);
        return base == null ? null : this.getModelEngine().tracker().getViewOnBase(base);
    }

    private Model loadModel(final @NotNull File file) {
        try (final InputStream input = new FileInputStream(file)) {
            return BBModelReader.blockbench().read(input);
        } catch (final Exception exception) {
            Logger.warning("Failed to load model %s", file.getName());
            return null;
        }
    }
}
