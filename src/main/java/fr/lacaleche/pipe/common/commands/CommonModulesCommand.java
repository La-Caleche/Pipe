package fr.lacaleche.pipe.common.commands;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.commands.annotations.DisabledOnly;
import fr.lacaleche.core.commands.annotations.Locked;
import fr.lacaleche.core.commands.interfaces.CloudCommand;
import fr.lacaleche.core.modules.ModuleClass;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.modules.features.interfaces.IFeatureValue;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.common.models.client.interfaces.PipeClient;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;
import org.incendo.cloud.context.CommandContext;

public class CommonModulesCommand<C> implements CloudCommand {

    @Command("module|m|mod <module>")
    public void getModuleStatus(
            final PipeCommandContext<C, PipeClient<? extends C>> context,
            @Argument(value = "module") final ModuleClass moduleClass
    ) {
        final boolean enabled = Core.get().getCentralModuleManager().moduleEnabled(moduleClass.clazz());

        context.sendMessage(context.locale()
                .ct("pipe.command.module.informations.enabled", "pipe.command.module.informations.disabled", enabled)
                .arg("module", moduleClass.clazz().getSimpleName())
                .from("Module").buildComponent());
    }

    @Command("module|m|mod feature set <module> <feature> <value>")
    public void setModuleFeature(
            final CommandContext<C> commandContext,
            final PipeCommandContext<C, PipeClient<? extends C>> context,
            @Argument(value = "module") @Locked(Locked.What.FEATURE)
            final IModule enabledModule,
            @Argument(value = "feature") final IFeature feature,
            @Argument(value = "value") @Greedy
            final IFeatureValue value
    ) {
        Core.runAsyncNow(task -> {
            feature.setValue(value.getValue());
            context.sendMessage(context.locale().t("pipe.command.module.features.set.updated")
                    .arg("new_value", value.getValue().toString())
                    .arg("module", enabledModule.getClass().getSimpleName())
                    .arg("feature", feature.name()).from("Module").buildComponent());
        });
    }

    @Command("module|m|mod feature get <module> [feature]")
    public void getModuleFeature(
            final PipeCommandContext<C, PipeClient<? extends C>> context,
            @Argument(value = "module") @Locked(Locked.What.FEATURE)
            final IModule enabledModule,
            @Argument(value = "feature") final IFeature feature
    ) {
        final String moduleName = enabledModule.getClass().getSimpleName();

        if (feature == null) {
            if (enabledModule.getFeatureManager() == null) {
                context.sendMessage(context.locale().t("pipe.command.module.features.list.empty").arg("module", moduleName).from("Module").buildComponent());
                return ;
            }

            context.sendMessage(context.locale().t("pipe.command.module.features.list").arg("module", moduleName).from("Module").buildComponent());
            enabledModule.getFeatureManager().getFeatures().forEach((moduleFeature) -> {
                context.sendMessage(context.locale().t("pipe.command.module.features.list.feature.value")
                        .arg("value", moduleFeature.value().getValue().toString())
                        .arg("feature", moduleFeature.name()).from("Module").buildComponent());
            });

            return ;
        }

        context.sendMessage(context.locale().t("pipe.command.module.features.get.value")
                .arg("value", feature.value().getValue().toString())
                .arg("module", moduleName).arg("feature", feature.name()).from("Module").buildComponent());
    }

    @Command("module|m|mod enable <module>")
    public void enableModule(
            final PipeCommandContext<C, PipeClient<? extends C>> context,
            @Argument(value = "module") @Locked(Locked.What.MODULE) @DisabledOnly
            final ModuleClass moduleClass
    ) {
//        if (moduleClass.clazz().isAnnotationPresent(AGodModule.class)) {
//            GodModule godModule = Core.getModule(GodModule.class);
//            godModule.getGodModuleManager().enableModule(moduleClass);
//        } else {
//            core.getCentralModuleManager().enableModule(modManagerModule.getHandler(), moduleClass);
//        }

        context.sendMessage(context.locale()
                .ct("pipe.command.module.informations.enabled", "pipe.command.module.informations.disabled", false)
                .arg("module", moduleClass.clazz().getSimpleName())
                .from("Module").buildComponent());
    }

}
