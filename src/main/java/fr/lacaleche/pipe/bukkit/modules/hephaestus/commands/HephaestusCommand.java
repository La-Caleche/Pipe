package fr.lacaleche.pipe.bukkit.modules.hephaestus.commands;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.utils.Callback;
import fr.lacaleche.core.utils.logger.Logger;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.commands.arguments.BukkitPlayerArgument;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.HephaestusModule;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments.AnimationsArgument;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments.BonesArgument;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments.ModelsArgument;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.commands.arguments.ViewsArgument;
import fr.lacaleche.pipe.bukkit.modules.hephaestus.interfaces.HephaestusManager;
import fr.lacaleche.pipe.common.commands.annotations.ArgumentsManager;
import fr.lacaleche.pipe.common.commands.annotations.CommandChild;
import fr.lacaleche.pipe.common.commands.annotations.CommandExecutor;
import fr.lacaleche.pipe.common.commands.annotations.MinecraftCommand;
import fr.lacaleche.pipe.common.commands.argument.arguments.BooleanArgument;
import fr.lacaleche.pipe.common.commands.argument.arguments.StringArgument;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import team.unnamed.hephaestus.Bone;
import team.unnamed.hephaestus.Model;
import team.unnamed.hephaestus.animation.Animation;
import team.unnamed.hephaestus.bukkit.BoneView;
import team.unnamed.hephaestus.bukkit.ModelView;
import team.unnamed.hephaestus.view.modifier.BoneModifierType;
import team.unnamed.hephaestus.view.modifier.player.rig.PlayerRig;
import team.unnamed.hephaestus.view.modifier.player.skin.Skin;
import team.unnamed.hephaestus.view.modifier.player.skin.SkinProvider;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@MinecraftCommand(label = "hephaestus", aliases = {"models"}, description = "pipe.commands.hephaestus.description")
public class HephaestusCommand {

    @CommandChild(label = "spawn", arguments = {"model"}, description = "pipe.commands.hephaestus.spawn.description")
    public static class Spawn {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new ModelsArgument("model"));
        }

        @CommandExecutor(executors = {CommandExecutor.Executor.PLAYER})
        public boolean execute(Command<Player> command) {
            HephaestusModule module = Core.getModule(HephaestusModule.class);
            HephaestusManager manager = module.getManager();

            String modelId = command.args().getString("model");
            @Nullable Model model = manager.getModelRegistry().model(modelId);
            if (model == null) {
                command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.model_not_found").arg("model", modelId).from("Hephaestus").ct());
                return true;
            }

            manager.create(model, command.sender().getLocation());
            command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.spawn.success").arg("model", modelId).from("Hephaestus").ct());
            return true;
        }

    }

    @CommandChild(label = "player", arguments = {"player"}, description = "pipe.commands.hephaestus.spawn.player.description")
    public static class SpawnPlayer {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new BukkitPlayerArgument("player"));
        }

        @CommandExecutor(executors = {CommandExecutor.Executor.PLAYER})
        public boolean execute(Command<Player> command) {
            HephaestusModule module = Core.getModule(HephaestusModule.class);
            HephaestusManager manager = module.getManager();
            String player = command.args().getString("player");

            manager.spawnPlayer(player, command.sender().getLocation(), new Callback<String>() {
                @Override
                public void done(String error) {
                    if (error.equals("success")) {
                        command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.spawn.player.success").arg("player", player).from("Hephaestus").ct());
                        return ;
                    }

                    command.sender().sendMessage(command.locale().t(error).arg("player", player).from("Hephaestus").ct());
                }
            });

            return true;
        }

    }

    @CommandChild(label = "delete", arguments = {"view"}, description = "pipe.commands.hephaestus.delete.description")
    public static class Delete {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new ViewsArgument("view"));
        }

        @CommandExecutor(executors = {CommandExecutor.Executor.PLAYER})
        public boolean execute(Command<Player> command) {
            HephaestusModule module = Core.getModule(HephaestusModule.class);
            HephaestusManager manager = module.getManager();

            String viewId = command.args().getString("view");
            final ModelView view = manager.getModelEntityById(viewId);

            if (view == null || view.base() == null) {
                command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.view_not_found").arg("view", viewId).from("Hephaestus").ct());
                return true;
            }
            view.base().remove();
            command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.delete.success").arg("view", viewId).from("Hephaestus").ct());
            return true;
        }

    }

    @CommandChild(label = "colorize", arguments = {"view", "color", "bone"}, description = "pipe.commands.hephaestus.colorize.description")
    public static class Colorize {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new ViewsArgument("view"));
            manager.addArgument(new StringArgument("color"));
            manager.addArgument(new BonesArgument("bone").optional());
        }

        @CommandExecutor(executors = {CommandExecutor.Executor.PLAYER})
        public boolean execute(Command<Player> command) {
            HephaestusModule module = Core.getModule(HephaestusModule.class);
            HephaestusManager manager = module.getManager();

            String viewId = command.args().getString("view");
            final ModelView view = manager.getModelEntityById(viewId);

            if (view == null || view.base() == null) {
                command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.view_not_found").arg("view", viewId).from("Hephaestus").ct());
                return true;
            }

            String colorArg = command.args().getString("color");
            int color = 0;
            try {
                if (!colorArg.startsWith("#"))
                    colorArg = "#" + colorArg;

                color = Integer.parseInt(colorArg.substring(1), 16);
            } catch (NumberFormatException exception) {
                command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.colorize.invalid_color").arg("color", colorArg).from("Hephaestus").ct());
                return true;
            }

            if (!command.args().blank("bone")) {
                String boneId = command.args().getString("bone");
                List<BoneView> boneViews;
                if (boneId.startsWith("!"))
                    boneViews = List.of(view.bone(boneId.substring(1)));
                else boneViews = manager.getBoneChildren(view, boneId);

                if (boneViews == null || boneViews.isEmpty()) {
                    command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.bone_not_found").arg("bone", boneId).from("Hephaestus").ct());
                    return true;
                }

                int finalColor = color;
                boneViews.forEach(boneView -> boneView.colorize(finalColor));
            } else view.colorize(color);

            command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.colorize.success").arg("view", viewId).arg("color", colorArg).from("Hephaestus").ct());
            return true;
        }

    }

    @CommandChild(label = "animate")
    public static class Animate {

        @CommandChild(label = "start", arguments = {"view", "animation"}, description = "pipe.commands.hephaestus.animate.start.description")
        public static class StartAnimation {
            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new ViewsArgument("view"));
                manager.addArgument(new AnimationsArgument("animation"));
            }

            @CommandExecutor(executors = {CommandExecutor.Executor.PLAYER})
            public boolean execute(Command<Player> command) {
                HephaestusModule module = Core.getModule(HephaestusModule.class);
                HephaestusManager manager = module.getManager();

                String viewId = command.args().getString("view");
                final ModelView view = manager.getModelEntityById(viewId);

                if (view == null || view.base() == null) {
                    command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.view_not_found").arg("view", viewId).from("Hephaestus").ct());
                    return true;
                }

                Map<String, Animation> animations = view.model().animations();
                @Nullable Animation animation = animations.get(command.args().getString("animation"));

                if (animation == null) {
                    command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.animate.animation_not_found").arg("animation", command.args().getString("animation")).from("Hephaestus").ct());
                    return true;
                }

                view.animationPlayer().add(animation);
                command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.animate.animation_played").arg("animation", command.args().getString("animation")).from("Hephaestus").ct());

                return true;
            }
        }

        @CommandChild(label = "stop", arguments = {"view"}, description = "pipe.commands.hephaestus.animate.stop.description")
        public static class StopAnimation {
            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new ViewsArgument("view"));
            }

            @CommandExecutor(executors = {CommandExecutor.Executor.PLAYER})
            public boolean execute(Command<Player> command) {
                HephaestusModule module = Core.getModule(HephaestusModule.class);
                HephaestusManager manager = module.getManager();

                String viewId = command.args().getString("view");
                final ModelView view = manager.getModelEntityById(viewId);

                if (view == null || view.base() == null) {
                    command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.view_not_found").arg("view", viewId).from("Hephaestus").ct());
                    return true;
                }

                view.animationPlayer().clear();
                command.sender().sendMessage(command.locale().t("pipe.commands.hephaestus.animate.animation_stopped").from("Hephaestus").ct());
                return true;
            }
        }


    }

}
