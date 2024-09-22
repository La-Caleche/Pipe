package fr.lacaleche.pipe.common.modules.common;

import fr.lacaleche.core.Core;
import fr.lacaleche.core.modules.features.interfaces.IFeature;
import fr.lacaleche.core.modules.features.interfaces.IFeatureValue;
import fr.lacaleche.core.modules.interfaces.IModule;
import fr.lacaleche.pipe.common.commands.GlobalPipeCommandContext;
import fr.lacaleche.pipe.common.commands.annotations.Locked;
import fr.lacaleche.pipe.common.commands.interfaces.CloudCommand;
import org.incendo.cloud.annotation.specifier.Greedy;
import org.incendo.cloud.annotations.Argument;
import org.incendo.cloud.annotations.Command;

public class CommonModulesCommand<C> implements CloudCommand {

    @Command("module|m|mod <module>")
    public void getModuleStatus(
            final GlobalPipeCommandContext<C> context,
            @Argument(value = "module") final ModuleClass moduleClass
    ) {
        final boolean enabled = Core.get().getCentralModuleManager().moduleEnabled(moduleClass.clazz());

        context.sendMessage(context.locale()
                .ct("pipe.command.module.informations.enabled", "pipe.command.module.informations.disabled", enabled)
                .arg("module", moduleClass.clazz().getSimpleName())
                .from("Module").ct());
    }

    @Command("module|m|mod feature set <module> <feature> <value>")
    public void setModuleFeature(
            final GlobalPipeCommandContext<C> context,
            @Argument(value = "module") @Locked(Locked.What.FEATURE)
            final IModule enabledModule,
            @Argument(value = "feature") final IFeature feature,
            @Argument(value = "value") @Greedy
            final IFeatureValue value
    ) {
        context.sendMessage("Module: " + enabledModule.getClass().getSimpleName() + " Feature: " + feature.name() + " Value type: " + value.type() + " Value: " + value.getValue());
    }

}
