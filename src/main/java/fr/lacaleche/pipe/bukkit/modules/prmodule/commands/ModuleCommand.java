package fr.lacaleche.pipe.bukkit.modules.prmodule.commands;


import fr.lacaleche.core.CalecheCore;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.core.modules.interfaces.ModuleFeature;
import fr.lacaleche.pipe.bukkit.modules.prmodule.ModsManagerModule;
import fr.lacaleche.pipe.common.commands.annotations.*;
import fr.lacaleche.pipe.common.commands.argument.arguments.*;
import fr.lacaleche.pipe.common.commands.argument.interfaces.ArgumentManager;
import fr.lacaleche.pipe.common.commands.interfaces.Arguments;
import fr.lacaleche.pipe.common.commands.interfaces.Command;
import fr.lacaleche.pipe.common.i18n.interfaces.Locale;
import org.bukkit.command.CommandSender;

import java.util.Collections;

@MinecraftCommand(label = "module", aliases = {"m", "mod"}, description = "pipe.command.module.description", arguments = {"module"})
public class ModuleCommand {

    @ArgumentsManager
    public void manager(ArgumentManager manager) {
        manager.addArgument(new AvailableModuleArgument("module"));
    }

    @CommandExecutor
    public boolean execute(Command<CommandSender> command) {
        CalecheCore core = CalecheCore.get();
        IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

        String moduleName = command.args().getString("module");
        Class<? extends IModule> classModule = core.getCentralModuleManager().getAnyAvailableModule(moduleName);

        command.sender().sendMessage(command.locale().ct("pipe.command.module.informations.enabled", "pipe.command.module.informations.disabled", core.getCentralModuleManager().moduleEnabled(classModule)).arg("module", classModule.getSimpleName()).from("Module").ct());

        return true;
    }

    @CommandChild(label = "features", description = "pipe.command.module.features.description")
    public static class Features {

        @CommandChild(label = "set", description = "pipe.command.module.features.set.description")
        public static class Set {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new EnabledModuleArgument("module"));
                manager.addArgument(new ModuleFeaturesArgument("feature"));
                manager.addArgument(new BooleanArgument("status"));
            }

            @CommandExecutor
            public boolean execute(Command<CommandSender> command) {
                CalecheCore core = CalecheCore.get();
                IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

                IModule module = ModuleCommand.getModule(command.sender(), command.locale(), command.args());
                if (module == null) return true;

                ModuleFeature feature = module.getFeatureByName(command.args().getString("feature"));

                if (feature == null) {
                    command.sender().sendMessage(command.locale().t("pipe.command.module.features.invalid").arg("feature", command.args().getString("feature")).from("Module").ct());
                    return true;
                }

                module.toggleFeature(feature, command.args().getBoolean("status"));

                command.sender().sendMessage(command.locale().ct("pipe.command.module.features.set.enabled", "pipe.command.module.features.set.disabled", command.args().getBoolean("status")).arg("module", module.getClass().getSimpleName()).arg("feature", feature.toString()).from("Module").ct());

                return true;
            }

        }

        @CommandChild(label = "get", description = "pipe.command.module.features.get.description")
        public static class Get {

            @ArgumentsManager
            public void manager(ArgumentManager manager) {
                manager.addArgument(new EnabledModuleArgument("module"));
                manager.addArgument(new ModuleFeaturesArgument("feature").optional());
            }

            @CommandExecutor
            public boolean execute(Command<CommandSender> command) {
                CalecheCore core = CalecheCore.get();
                IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

                IModule module = ModuleCommand.getModule(command.sender(), command.locale(), command.args());
                if (module == null) return true;

                if (command.args().blank("feature")) {
                    command.sender().sendMessage(command.locale().t("pipe.command.module.features.list").arg("module", module.getClass().getSimpleName()).from("Module").ct());
                    module.getFeatures().forEach((moduleFeature, aBoolean) -> {
                        command.sender().sendMessage(command.locale().ct("pipe.command.module.features.list.feature.enabled", "pipe.command.module.features.list.feature.disabled", aBoolean).arg("feature", moduleFeature).from("Module").ct());
                    });
                } else {
                    ModuleFeature feature = module.getFeatureByName(command.args().getString("feature"));

                    if (feature == null) {
                        command.sender().sendMessage(command.locale().t("pipe.command.module.features.invalid").arg("feature", command.args().getString("feature")).from("Module").ct());
                        return true;
                    }

                    command.sender().sendMessage(command.locale().ct("pipe.command.module.features.get.enabled", "pipe.command.module.features.get.disabled", module.isFeatureEnabled(feature)).arg("module", module.getClass().getSimpleName()).arg("feature", feature).from("Module").ct());
                }

                return true;
            }

        }

    }

    @CommandChild(label = "enable", description = "pipe.command.module.enable.description")
    public static class Enable {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new DisabledModuleArgument("module").optional());
        }

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {
            CalecheCore core = CalecheCore.get();
            IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

            if (!command.args().blank("module")) {
                Class<? extends IModule> moduleClass = ModuleCommand.getClassModule(command.sender(), command.locale(), command.args());
                if (moduleClass == null) return true;

                core.getCentralModuleManager().enableModule(modManagerModule.getHandler(), moduleClass);
                command.sender().sendMessage(command.locale().t("pipe.command.module.enabled").arg("module", moduleClass.getSimpleName()).from("Module").ct());
            } else {
                core.getCentralModuleManager().enableModulesExcept(modManagerModule.getHandler(), Collections.singletonList(modManagerModule.getClass()));
                command.sender().sendMessage(command.locale().t("pipe.command.module.all_enabled").from("Module").ct());
            }

            return true;
        }

    }

    @CommandChild(label = "disable", description = "pipe.command.module.disable.description")
    public static class Disable {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new EnabledModuleArgument("module").optional());
        }

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {
            CalecheCore core = CalecheCore.get();
            IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

            if (!command.args().blank("module")) {
                IModule module = ModuleCommand.getModule(command.sender(), command.locale(), command.args());
                if (module == null) return true;

                core.getCentralModuleManager().disableModule(module);
                command.sender().sendMessage(command.locale().t("pipe.command.module.disabled").arg("module", module.getClass().getSimpleName()).from("Module").ct());
            } else {
                core.getCentralModuleManager().disableModulesExcept(modManagerModule);
                command.sender().sendMessage(command.locale().t("pipe.command.module.all_disabled").from("Module").ct());
            }

            return true;
        }

    }

    @CommandChild(label = "reload", description = "pipe.command.module.reload.description")
    public static class Reload {

        @ArgumentsManager
        public void manager(ArgumentManager manager) {
            manager.addArgument(new EnabledModuleArgument("module").optional());
        }

        @CommandExecutor
        public boolean execute(Command<CommandSender> command) {
            CalecheCore core = CalecheCore.get();
            IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

            if (!command.args().blank("module")) {
                IModule module = ModuleCommand.getModule(command.sender(), command.locale(), command.args());
                if (module == null) return true;

                core.getCentralModuleManager().reloadModule(module);
                command.sender().sendMessage(command.locale().t("pipe.command.module.reloaded").arg("module", module.getClass().getSimpleName()).from("Module").ct());
            } else {
                core.getCentralModuleManager().reloadModulesExcept(modManagerModule);
                command.sender().sendMessage(command.locale().t("pipe.command.module.all_reloaded").from("Module").ct());
            }

            return true;
        }

    }

    private static IModule getModule(CommandSender sender, Locale locale, Arguments arguments) {
        CalecheCore core = CalecheCore.get();
        IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

        if (!arguments.blank("module")) {
            String moduleName = arguments.getString("module");
            IModule module = core.getCentralModuleManager().getAnyModule(moduleName);

            if (module == null) {
                sender.sendMessage(locale.t("global.module_not_found").arg("module", moduleName).from("Module").ct());
                return null;
            }

            if (modManagerModule == module) {
                sender.sendMessage(locale.t("pipe.command.module.not_allowed").from("Module").ct());
                return null;
            }

            return module;
        }

        return null;
    }

    private static Class<? extends IModule> getClassModule(CommandSender sender, Locale locale, Arguments arguments) {
        CalecheCore core = CalecheCore.get();
        IModule modManagerModule = core.getCentralModuleManager().getModule(ModsManagerModule.class);

        if (!arguments.blank("module")) {
            String moduleName = arguments.getString("module");
            Class<? extends IModule> moduleclass = core.getCentralModuleManager().getAnyAvailableModule(moduleName);

            if (moduleclass == null) {
                sender.sendMessage(locale.t("global.module_not_found").arg("module", moduleName).from("Module").ct());
                return null;
            }

            if (modManagerModule.getClass() == moduleclass) {
                sender.sendMessage(locale.t("pipe.command.module.not_allowed").from("Module").ct());
                return null;
            }

            return moduleclass;
        }

        return null;
    }

}
