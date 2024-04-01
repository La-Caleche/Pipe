package fr.lacaleche.pipe.bukkit.modules.god.modules.time;

import fr.lacaleche.core.modules.FeatureManagerImpl;
import fr.lacaleche.core.modules.features.impl.Feature;
import fr.lacaleche.core.modules.annotations.AModule;
import fr.lacaleche.core.modules.enums.ModuleTarget;
import fr.lacaleche.core.modules.features.interfaces.IFeatureValue;
import fr.lacaleche.core.modules.interfaces.IFeatureManager;
import fr.lacaleche.core.modules.interfaces.IModuleHandler;
import fr.lacaleche.pipe.Pipe;
import fr.lacaleche.pipe.bukkit.events.BukkitPipeListenerManager;
import fr.lacaleche.pipe.bukkit.modules.BukkitModule;
import fr.lacaleche.pipe.bukkit.modules.god.annotations.AGodModule;
import fr.lacaleche.pipe.bukkit.modules.god.modules.time.commands.TimeCommand;
import fr.lacaleche.pipe.bukkit.modules.god.modules.time.listeners.WorldListener;
import fr.lacaleche.pipe.common.tasks.interfaces.Task;

@AGodModule
@AModule(target = ModuleTarget.BUKKIT)
public class TimeModule extends BukkitModule {

    private Task pendingTask;

    public TimeModule(IModuleHandler handler) {
        super(handler);
    }

    @Override
    public void registerListeners() {
        BukkitPipeListenerManager bukkitManager = Pipe.getBukkit().getListenerManager();
        bukkitManager.registerBoth(this, new WorldListener(this));
    }

    @Override
    public void registerCommands() {
        Pipe.getBukkit().getCommandManager().registerNewCommand(this, TimeCommand.class);
    }

    @Override
    public void registerFeatures() {
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_SKIP", true, Boolean.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_TYPE", DayLightType.FIXED, DayLightType.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_TIME_VALUE", 1600L, Long.class));
        this.getFeatureManager().registerFeature(new Feature<>("DN_SPEED_MULTIPLIER", 2, Integer.class));
    }

    public void setFixedTime(long time) {
        this.getFeatureManager().getFeatureByName("DN_TIME_VALUE").setValue(time);
    }

    public void speedTimeFromTo(long from, long to, int multiplier) {
        if (this.pendingTask != null) this.pendingTask.stop();

        final IFeatureManager features = this.getFeatureManager();
        final int oldMultiplier = (int) features.getFeatureByName("DN_SPEED_MULTIPLIER").value().getValue();

        features.getFeatureByName("DN_TIME_VALUE").setValue(from);
        features.getFeatureByName("DN_SPEED_MULTIPLIER").setValue(multiplier);
        features.getFeatureByName("DN_TIME_TYPE").setValue(DayLightType.SPEED_CYCLE);

        this.pendingTask = Pipe.get().getTaskManager().newTask(builder -> builder.run(task -> {
            features.getFeatureByName("DN_TIME_TYPE").setValue(DayLightType.FIXED);
            features.getFeatureByName("DN_TIME_VALUE").setValue(to);
            features.getFeatureByName("DN_SPEED_MULTIPLIER").setValue(oldMultiplier);
        }).startAfter(this.calculateDelay(from, to) / multiplier));
    }

    public void speedTimeTo(long to, int multiplier) {
        final IFeatureManager features = this.getFeatureManager();
        final long time = (long) features.getFeatureByName("DN_TIME_VALUE").value().getValue();
        speedTimeFromTo(time, to, multiplier);
    }

    private int calculateDelay(long from, long to) {
        long directDistance = to - from;
        return (int) Math.abs((directDistance >= 0) ? directDistance : 24000 + directDistance);
    }

}
