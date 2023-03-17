package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.PacketListenerPlayOut;
import net.minecraft.network.protocol.game.PacketPlayOutSpawnEntity;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.util.*;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;
import net.minecraft.world.level.material.EnumPistonReaction;
import net.minecraft.world.phys.AxisAlignedBB;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.IntFunction;

public class CalecheDisplay extends Entity {

    private static final float INITIAL_UPDATE_PROGRESS = Float.POSITIVE_INFINITY;
    public static final int NO_BRIGHTNESS_OVERRIDE = -1;
    private static final DataWatcherObject<Integer> DATA_INTERPOLATION_START_DELTA_TICKS_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> DATA_INTERPOLATION_DURATION_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Vector3f> DATA_TRANSLATION_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.A);
    private static final DataWatcherObject<Vector3f> DATA_SCALE_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.A);
    private static final DataWatcherObject<Quaternionf> DATA_LEFT_ROTATION_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.B);
    private static final DataWatcherObject<Quaternionf> DATA_RIGHT_ROTATION_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.B);
    private static final DataWatcherObject<Byte> DATA_BILLBOARD_RENDER_CONSTRAINTS_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Integer> DATA_BRIGHTNESS_OVERRIDE_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Float> DATA_VIEW_RANGE_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.d);
    private static final DataWatcherObject<Float> DATA_SHADOW_RADIUS_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.d);
    private static final DataWatcherObject<Float> DATA_SHADOW_STRENGTH_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.d);
    private static final DataWatcherObject<Float> DATA_WIDTH_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.d);
    private static final DataWatcherObject<Float> DATA_HEIGHT_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.d);
    private static final DataWatcherObject<Integer> DATA_GLOW_COLOR_OVERRIDE_ID = DataWatcher.a(CalecheDisplay.class, DataWatcherRegistry.b);
    private static final float INITIAL_SHADOW_RADIUS = 0.0F;
    private static final float INITIAL_SHADOW_STRENGTH = 1.0F;
    private static final int NO_GLOW_COLOR_OVERRIDE = -1;
    public static final String TAG_INTERPOLATION_DURATION = "interpolation_duration";
    public static final String TAG_START_INTERPOLATION = "start_interpolation";
    public static final String TAG_TRANSFORMATION = "transformation";
    public static final String TAG_BILLBOARD = "billboard";
    public static final String TAG_BRIGHTNESS = "brightness";
    public static final String TAG_VIEW_RANGE = "view_range";
    public static final String TAG_SHADOW_RADIUS = "shadow_radius";
    public static final String TAG_SHADOW_STRENGTH = "shadow_strength";
    public static final String TAG_WIDTH = "width";
    public static final String TAG_HEIGHT = "height";
    public static final String TAG_GLOW_COLOR_OVERRIDE = "glow_color_override";
    private final GenericInterpolator<Transformation> transformation = new GenericInterpolator<Transformation>(Transformation.a()) {
        @Override
        protected Transformation interpolate(float delta, Transformation start, Transformation end) {
            return start.a(end, delta);
        }
    };
    private final FloatInterpolator shadowRadius = new FloatInterpolator(0.0F);
    private final FloatInterpolator shadowStrength = new FloatInterpolator(1.0F);
    private final Quaternionf orientation = new Quaternionf();
    protected final InterpolatorSet interpolators = new InterpolatorSet();
    private long interpolationStartClientTick;
    private float lastProgress;
    private AxisAlignedBB cullingBoundingBox;
    private boolean updateInterpolators;
    private boolean updateTime;

    public CalecheDisplay(EntityTypes<?> var0, World var1) {
        super(var0, var1);

        this.ae = true;
        this.as = true;
        this.cullingBoundingBox = this.cD();
        this.interpolators.addEntry(Set.of(DATA_TRANSLATION_ID, DATA_LEFT_ROTATION_ID, DATA_SCALE_ID, DATA_RIGHT_ROTATION_ID), (value, dataTracker) -> {
            this.transformation.updateValue(value, createTransformation(dataTracker));
        });
        this.interpolators.addEntry(DATA_SHADOW_STRENGTH_ID, this.shadowStrength);
        this.interpolators.addEntry(DATA_SHADOW_RADIUS_ID, this.shadowRadius);
    }

    @Override
    public void a(List<DataWatcher.b<?>> dataEntries) {
        super.a(dataEntries);
        boolean bl = false;

        for(DataWatcher.b<?> dataValue : dataEntries) {
            bl |= this.interpolators.shouldTriggerUpdate(dataValue.a());
        }

        if (bl) {
            boolean bl2 = this.ag <= 0;
            if (bl2) {
                this.interpolators.updateValues(Float.POSITIVE_INFINITY, this.am);
            } else {
                this.updateInterpolators = true;
            }
        }

    }

    @Override
    public void a(DataWatcherObject<?> data) {
        super.a(data);
        if (DATA_HEIGHT_ID.equals(data) || DATA_WIDTH_ID.equals(data)) {
            this.updateCulling();
        }

        if (DATA_INTERPOLATION_START_DELTA_TICKS_ID.equals(data)) {
            this.updateTime = true;
        }

    }

    @Override
    public void l() {
        Entity entity = this.cV();
        if (entity != null && entity.dB()) {
            this.bz();
        }

        if (this.H.B) {
            if (this.updateTime) {
                this.updateTime = false;
                int i = this.getInterpolationDelay();
                this.interpolationStartClientTick = (long)(this.ag + i);
            }

            if (this.updateInterpolators) {
                this.updateInterpolators = false;
                this.interpolators.updateValues(this.lastProgress, this.am);
            }
        }
    }

    @Override
    protected void a_() {
        this.am.a(DATA_INTERPOLATION_START_DELTA_TICKS_ID, 0);
        this.am.a(DATA_INTERPOLATION_DURATION_ID, 0);
        this.am.a(DATA_TRANSLATION_ID, new Vector3f());
        this.am.a(DATA_SCALE_ID, new Vector3f(1.0F, 1.0F, 1.0F));
        this.am.a(DATA_RIGHT_ROTATION_ID, new Quaternionf());
        this.am.a(DATA_LEFT_ROTATION_ID, new Quaternionf());
        this.am.a(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, BillboardConstraints.FIXED.getId());
        this.am.a(DATA_BRIGHTNESS_OVERRIDE_ID, -1);
        this.am.a(DATA_VIEW_RANGE_ID, 1.0F);
        this.am.a(DATA_SHADOW_RADIUS_ID, 0.0F);
        this.am.a(DATA_SHADOW_STRENGTH_ID, 1.0F);
        this.am.a(DATA_WIDTH_ID, 0.0F);
        this.am.a(DATA_HEIGHT_ID, 0.0F);
        this.am.a(DATA_GLOW_COLOR_OVERRIDE_ID, -1);
    }

    @Override
    protected void a(NBTTagCompound var0) {}

    @Override
    protected void b(NBTTagCompound var0) {}

    @Override
    public Packet<PacketListenerPlayOut> S() {
        return new PacketPlayOutSpawnEntity(this);
    }

    @Override
    public AxisAlignedBB A_() {
        return this.cullingBoundingBox;
    }

    @Override
    public EnumPistonReaction C_() {
        return EnumPistonReaction.d;
    }

    @Override
    public void ah() {
        this.a(Entity.RemovalReason.a);
    }

    public Quaternionf orientation() {
        return this.orientation;
    }

    public Transformation transformation(float delta) {
        return this.transformation.get(delta);
    }

    public void setTransformation(Transformation transformation) {
        this.am.b(DATA_TRANSLATION_ID, transformation.d());
        this.am.b(DATA_LEFT_ROTATION_ID, transformation.e());
        this.am.b(DATA_SCALE_ID, transformation.f());
        this.am.b(DATA_RIGHT_ROTATION_ID, transformation.g());
    }

    public void setInterpolationDuration(int interpolationDuration) {
        this.am.b(DATA_INTERPOLATION_DURATION_ID, interpolationDuration);
    }

    public int getInterpolationDuration() {
        return this.am.a(DATA_INTERPOLATION_DURATION_ID);
    }

    public void setInterpolationDelay(int startInterpolation) {
        this.am.a(DATA_INTERPOLATION_START_DELTA_TICKS_ID, startInterpolation, true);
    }

    public int getInterpolationDelay() {
        return this.am.a(DATA_INTERPOLATION_START_DELTA_TICKS_ID);
    }

    public void setBillboardConstraints(BillboardConstraints billboardMode) {
        this.am.b(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, billboardMode.getId());
    }

    public BillboardConstraints getBillboardConstraints() {
        return BillboardConstraints.BY_ID.apply(this.am.a(DATA_BILLBOARD_RENDER_CONSTRAINTS_ID));
    }

    public void setBrightnessOverride(@Nullable Brightness brightness) {
        this.am.b(DATA_BRIGHTNESS_OVERRIDE_ID, brightness != null ? brightness.a() : -1);
    }

    @Nullable
    public Brightness getBrightnessOverride() {
        int i = this.am.a(DATA_BRIGHTNESS_OVERRIDE_ID);
        return i != -1 ? Brightness.a(i) : null;
    }

    public int getPackedBrightnessOverride() {
        return this.am.a(DATA_BRIGHTNESS_OVERRIDE_ID);
    }

    public void setViewRange(float viewRange) {
        this.am.b(DATA_VIEW_RANGE_ID, viewRange);
    }

    public float getViewRange() {
        return this.am.a(DATA_VIEW_RANGE_ID);
    }

    public void setShadowRadius(float shadowRadius) {
        this.am.b(DATA_SHADOW_RADIUS_ID, shadowRadius);
    }

    public float getShadowRadius() {
        return this.am.a(DATA_SHADOW_RADIUS_ID);
    }

    public float getShadowRadius(float delta) {
        return this.shadowRadius.get(delta);
    }

    public void setShadowStrength(float shadowStrength) {
        this.am.b(DATA_SHADOW_STRENGTH_ID, shadowStrength);
    }

    public float getShadowStrength() {
        return this.am.a(DATA_SHADOW_STRENGTH_ID);
    }

    public float getShadowStrength(float delta) {
        return this.shadowStrength.get(delta);
    }

    public void setWidth(float width) {
        this.am.b(DATA_WIDTH_ID, width);
    }

    public float getWidth() {
        return this.am.a(DATA_WIDTH_ID);
    }

    public void setHeight(float height) {
        this.am.b(DATA_HEIGHT_ID, height);
    }

    public int getGlowColorOverride() {
        return this.am.a(DATA_GLOW_COLOR_OVERRIDE_ID);
    }

    public void setGlowColorOverride(int glowColorOverride) {
        this.am.b(DATA_GLOW_COLOR_OVERRIDE_ID, glowColorOverride);
    }

    public float calculateInterpolationProgress(float delta) {
        int i = this.getInterpolationDuration();
        if (i <= 0) {
            return 1.0F;
        } else {
            float f = (float)((long)this.ag - this.interpolationStartClientTick);
            float g = f + delta;
            float h = MathHelper.a(MathHelper.g(g, 0.0F, (float)i), 0.0F, 1.0F);
            this.lastProgress = h;
            return h;
        }
    }

    public float getHeight() {
        return this.am.a(DATA_HEIGHT_ID);
    }

    @Override
    public void e(double x, double y, double z) {
        super.e(x, y, z);
        this.updateCulling();
    }

    private void updateCulling() {
        float f = this.getWidth();
        float g = this.getHeight();
        if (f != 0.0F && g != 0.0F) {
            this.as = false;
            float h = f / 2.0F;
            double d = this.dl();
            double e = this.dn();
            double i = this.dr();
            this.cullingBoundingBox = new AxisAlignedBB(d - (double)h, e, i - (double)h, d + (double)h, e + (double)g, i + (double)h);
        } else {
            this.as = true;
        }

    }

    @Override
    public void e(float pitch) {
        super.e(pitch);
        this.updateOrientation();
    }

    @Override
    public void f(float yaw) {
        super.f(yaw);
        this.updateOrientation();
    }

    private void updateOrientation() {
        this.orientation.rotationYXZ(-0.017453292F * this.dw(), ((float)Math.PI / 180F) * this.dy(), 0.0F);
    }

    @Override
    public boolean a(double distance) {
        return distance < MathHelper.k((double)this.getViewRange() * 64.0D * cw());
    }

    @Override
    public int B_() {
        int i = this.getGlowColorOverride();
        return i != -1 ? i : super.B_();
    }

    public enum BillboardConstraints implements INamable {
        FIXED((byte)0, "fixed"),
        VERTICAL((byte)1, "vertical"),
        HORIZONTAL((byte)2, "horizontal"),
        CENTER((byte)3, "center");

        public static final Codec<BillboardConstraints> CODEC = INamable.a(BillboardConstraints::values);
        public static final IntFunction<BillboardConstraints> BY_ID = ByIdMap.a(BillboardConstraints::getId, values(), FIXED);
        private final byte id;
        private final String name;

        private BillboardConstraints(byte index, String name) {
            this.name = name;
            this.id = index;
        }

        @Override
        public String c() {
            return this.name;
        }

        byte getId() {
            return this.id;
        }
    }


    public static Transformation createTransformation(DataWatcher dataTracker) {
        Vector3f vector3f = dataTracker.a(DATA_TRANSLATION_ID);
        Quaternionf quaternionf = dataTracker.a(DATA_LEFT_ROTATION_ID);
        Vector3f vector3f2 = dataTracker.a(DATA_SCALE_ID);
        Quaternionf quaternionf2 = dataTracker.a(DATA_RIGHT_ROTATION_ID);
        return new Transformation(vector3f, quaternionf, vector3f2, quaternionf2);
    }

    static class ColorInterpolator extends IntInterpolator {
        protected ColorInterpolator(int value) {
            super(value);
        }

        @Override
        protected int interpolate(float delta, int start, int end) {
            return ColorUtil.b.a(delta, start, end);
        }
    }

    static class FloatInterpolator extends Interpolator<Float> {
        protected FloatInterpolator(float value) {
            super(value);
        }

        protected float interpolate(float delta, float start, float end) {
            return MathHelper.a(delta, start, end);
        }

        public float get(float delta) {
            return !((double)delta >= 1.0D) && this.lastValue != null ? this.interpolate(delta, this.lastValue, this.currentValue) : this.currentValue;
        }

        @Override
        protected Float getGeneric(float f) {
            return this.get(f);
        }
    }

    abstract static class GenericInterpolator<T> extends Interpolator<T> {
        protected GenericInterpolator(T value) {
            super(value);
        }

        protected abstract T interpolate(float delta, T start, T end);

        public T get(float delta) {
            return (T)(!((double)delta >= 1.0D) && this.lastValue != null ? this.interpolate(delta, this.lastValue, this.currentValue) : this.currentValue);
        }

        @Override
        protected T getGeneric(float value) {
            return this.get(value);
        }
    }

    static class IntInterpolator extends Interpolator<Integer> {
        protected IntInterpolator(int value) {
            super(value);
        }

        protected int interpolate(float delta, int start, int end) {
            return MathHelper.a(delta, start, end);
        }

        public int get(float value) {
            return !((double)value >= 1.0D) && this.lastValue != null ? this.interpolate(value, this.lastValue, this.currentValue) : this.currentValue;
        }

        @Override
        protected Integer getGeneric(float f) {
            return this.get(f);
        }
    }

    @FunctionalInterface
    interface IntepolatorUpdater {
        void update(float value, DataWatcher dataTracker);
    }

    abstract static class Interpolator<T> {
        @Nullable
        protected T lastValue;
        protected T currentValue;

        protected Interpolator(T value) {
            this.currentValue = value;
        }

        protected abstract T getGeneric(float value);

        public void updateValue(float prevValue, T value) {
            if (prevValue != Float.POSITIVE_INFINITY) {
                this.lastValue = this.getGeneric(prevValue);
            }

            this.currentValue = value;
        }
    }

    static class InterpolatorSet {
        private final IntSet interpolatedData = new IntOpenHashSet();
        private final List<IntepolatorUpdater> updaters = new ArrayList<>();

        protected <T> void addEntry(DataWatcherObject<T> data, Interpolator<T> interpolator) {
            this.interpolatedData.add(data.a());
            this.updaters.add((value, dataTracker) -> {
                interpolator.updateValue(value, dataTracker.a(data));
            });
        }

        protected void addEntry(Set<DataWatcherObject<?>> dataSet, IntepolatorUpdater updater) {
            for(DataWatcherObject<?> entityDataAccessor : dataSet) {
                this.interpolatedData.add(entityDataAccessor.a());
            }

            this.updaters.add(updater);
        }

        public boolean shouldTriggerUpdate(int id) {
            return this.interpolatedData.contains(id);
        }

        public void updateValues(float value, DataWatcher dataTracker) {
            for(IntepolatorUpdater intepolatorUpdater : this.updaters) {
                intepolatorUpdater.update(value, dataTracker);
            }

        }
    }

}
