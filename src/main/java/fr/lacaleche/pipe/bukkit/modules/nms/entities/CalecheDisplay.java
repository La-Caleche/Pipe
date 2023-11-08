package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.kyori.adventure.text.Component;
import net.minecraft.SystemUtils;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
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
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public abstract class CalecheDisplay extends Entity {

    static final Logger LOGGER = LogUtils.getLogger();
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
    private static final IntSet RENDER_STATE_IDS = IntSet.of(new int[]{DATA_TRANSLATION_ID.a(), DATA_SCALE_ID.a(), DATA_LEFT_ROTATION_ID.a(), DATA_RIGHT_ROTATION_ID.a(), DATA_BILLBOARD_RENDER_CONSTRAINTS_ID.a(), DATA_BRIGHTNESS_OVERRIDE_ID.a(), DATA_SHADOW_RADIUS_ID.a(), DATA_SHADOW_STRENGTH_ID.a()});
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
    private final Quaternionf orientation = new Quaternionf();
    private long interpolationStartClientTick = -2147483648L;
    private int interpolationDuration;
    private float lastProgress;
    private AxisAlignedBB cullingBoundingBox;
    protected boolean updateRenderState;
    private boolean updateStartTick;
    private boolean updateInterpolationDuration;
    @Nullable
    private CalecheDisplay.j renderState;

    public CalecheDisplay(EntityTypes<?> entitytypes, World world) {
        super(entitytypes, world);
        this.ae = true;
        this.as = true;
        this.cullingBoundingBox = this.cE();
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        super.a(datawatcherobject);
        if (DATA_HEIGHT_ID.equals(datawatcherobject) || DATA_WIDTH_ID.equals(datawatcherobject)) {
            this.updateCulling();
        }

        if (DATA_INTERPOLATION_START_DELTA_TICKS_ID.equals(datawatcherobject)) {
            this.updateStartTick = true;
        }

        if (DATA_INTERPOLATION_DURATION_ID.equals(datawatcherobject)) {
            this.updateInterpolationDuration = true;
        }

        if (RENDER_STATE_IDS.contains(datawatcherobject.a())) {
            this.updateRenderState = true;
        }

    }

    public static Transformation createTransformation(DataWatcher datawatcher) {
        Vector3f vector3f = (Vector3f) datawatcher.b(DATA_TRANSLATION_ID);
        Quaternionf quaternionf = (Quaternionf) datawatcher.b(DATA_LEFT_ROTATION_ID);
        Vector3f vector3f1 = (Vector3f) datawatcher.b(DATA_SCALE_ID);
        Quaternionf quaternionf1 = (Quaternionf) datawatcher.b(DATA_RIGHT_ROTATION_ID);

        return new Transformation(vector3f, quaternionf, vector3f1, quaternionf1);
    }

    @Override
    public void l() {
        Entity entity = this.cW();

        if (entity != null && entity.dD()) {
            this.Y();
        }

        if (this.dI().B) {
            if (this.updateStartTick) {
                this.updateStartTick = false;
                int i = this.getInterpolationDelay();

                this.interpolationStartClientTick = (long) (this.ag + i);
            }

            if (this.updateInterpolationDuration) {
                this.updateInterpolationDuration = false;
                this.interpolationDuration = this.getInterpolationDuration();
            }

            if (this.updateRenderState) {
                this.updateRenderState = false;
                boolean flag = this.interpolationDuration != 0;

                if (flag && this.renderState != null) {
                    this.renderState = this.createInterpolatedRenderState(this.renderState, this.lastProgress);
                } else {
                    this.renderState = this.createFreshRenderState();
                }

                this.a(flag, this.lastProgress);
            }
        }

    }

    protected abstract void a(boolean flag, float f);

    @Override
    protected void a_() {
        this.am.a(CalecheDisplay.DATA_INTERPOLATION_START_DELTA_TICKS_ID, 0);
        this.am.a(CalecheDisplay.DATA_INTERPOLATION_DURATION_ID, 0);
        this.am.a(CalecheDisplay.DATA_TRANSLATION_ID, new Vector3f());
        this.am.a(CalecheDisplay.DATA_SCALE_ID, new Vector3f(1.0F, 1.0F, 1.0F));
        this.am.a(CalecheDisplay.DATA_RIGHT_ROTATION_ID, new Quaternionf());
        this.am.a(CalecheDisplay.DATA_LEFT_ROTATION_ID, new Quaternionf());
        this.am.a(CalecheDisplay.DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, CalecheDisplay.BillboardConstraints.FIXED.getId());
        this.am.a(CalecheDisplay.DATA_BRIGHTNESS_OVERRIDE_ID, -1);
        this.am.a(CalecheDisplay.DATA_VIEW_RANGE_ID, 1.0F);
        this.am.a(CalecheDisplay.DATA_SHADOW_RADIUS_ID, 0.0F);
        this.am.a(CalecheDisplay.DATA_SHADOW_STRENGTH_ID, 1.0F);
        this.am.a(CalecheDisplay.DATA_WIDTH_ID, 0.0F);
        this.am.a(CalecheDisplay.DATA_HEIGHT_ID, 0.0F);
        this.am.a(CalecheDisplay.DATA_GLOW_COLOR_OVERRIDE_ID, -1);
    }

    @Override
    protected void a(NBTTagCompound nbttagcompound) {
        // DataResult dataresult; // CraftBukkit - decompile error
        Logger logger;

        if (nbttagcompound.e("transformation")) {
            DataResult<Pair<Transformation, NBTBase>> dataresult = Transformation.b.decode(DynamicOpsNBT.a, nbttagcompound.c("transformation")); // CraftBukkit - decompile error
            logger = CalecheDisplay.LOGGER;
            Objects.requireNonNull(logger);
            dataresult.resultOrPartial(prefix("Display entity", logger::error)).ifPresent((pair) -> {
                this.setTransformation((Transformation) pair.getFirst());
            });
        }

        int i;

        if (nbttagcompound.b("interpolation_duration", 99)) {
            i = nbttagcompound.h("interpolation_duration");
            this.setInterpolationDuration(i);
        }

        if (nbttagcompound.b("start_interpolation", 99)) {
            i = nbttagcompound.h("start_interpolation");
            this.setInterpolationDelay(i);
        }

        if (nbttagcompound.b("billboard", 8)) {
            DataResult<Pair<CalecheDisplay.BillboardConstraints, net.minecraft.nbt.NBTBase>> dataresult = CalecheDisplay.BillboardConstraints.CODEC.decode(DynamicOpsNBT.a, nbttagcompound.c("billboard")); // CraftBukkit - decompile error
            logger = CalecheDisplay.LOGGER;
            Objects.requireNonNull(logger);
            dataresult.resultOrPartial(prefix("Display entity", logger::error)).ifPresent((pair) -> {
                this.setBillboardConstraints((CalecheDisplay.BillboardConstraints) pair.getFirst());
            });
        }

        if (nbttagcompound.b("view_range", 99)) {
            this.setViewRange(nbttagcompound.j("view_range"));
        }

        if (nbttagcompound.b("shadow_radius", 99)) {
            this.setShadowRadius(nbttagcompound.j("shadow_radius"));
        }

        if (nbttagcompound.b("shadow_strength", 99)) {
            this.setShadowStrength(nbttagcompound.j("shadow_strength"));
        }

        if (nbttagcompound.b("width", 99)) {
            this.setWidth(nbttagcompound.j("width"));
        }

        if (nbttagcompound.b("height", 99)) {
            this.setHeight(nbttagcompound.j("height"));
        }

        if (nbttagcompound.b("glow_color_override", 99)) {
            this.setGlowColorOverride(nbttagcompound.h("glow_color_override"));
        }

        if (nbttagcompound.b("brightness", 10)) {
            DataResult<Pair<Brightness, net.minecraft.nbt.NBTBase>> dataresult = Brightness.b.decode(DynamicOpsNBT.a, nbttagcompound.c("brightness")); // CraftBukkit - decompile error
            logger = CalecheDisplay.LOGGER;
            Objects.requireNonNull(logger);
            dataresult.resultOrPartial(prefix("Display entity", logger::error)).ifPresent((pair) -> {
                this.setBrightnessOverride((Brightness) pair.getFirst());
            });
        } else {
            this.setBrightnessOverride((Brightness) null);
        }

    }

    public void setTransformation(Transformation transformation) {
        this.am.b(CalecheDisplay.DATA_TRANSLATION_ID, transformation.d());
        this.am.b(CalecheDisplay.DATA_LEFT_ROTATION_ID, transformation.e());
        this.am.b(CalecheDisplay.DATA_SCALE_ID, transformation.f());
        this.am.b(CalecheDisplay.DATA_RIGHT_ROTATION_ID, transformation.g());
    }

    @Override
    protected void b(NBTTagCompound nbttagcompound) {
        Transformation.b.encodeStart(DynamicOpsNBT.a, createTransformation(this.am)).result().ifPresent((nbtbase) -> {
            nbttagcompound.a("transformation", nbtbase);
        });
        CalecheDisplay.BillboardConstraints.CODEC.encodeStart(DynamicOpsNBT.a, this.getBillboardConstraints()).result().ifPresent((nbtbase) -> {
            nbttagcompound.a("billboard", nbtbase);
        });
        nbttagcompound.a("interpolation_duration", this.getInterpolationDuration());
        nbttagcompound.a("view_range", this.getViewRange());
        nbttagcompound.a("shadow_radius", this.getShadowRadius());
        nbttagcompound.a("shadow_strength", this.getShadowStrength());
        nbttagcompound.a("width", this.getWidth());
        nbttagcompound.a("height", this.getHeight());
        nbttagcompound.a("glow_color_override", this.getGlowColorOverride());
        Brightness brightness = this.getBrightnessOverride();

        if (brightness != null) {
            Brightness.b.encodeStart(DynamicOpsNBT.a, brightness).result().ifPresent((nbtbase) -> {
                nbttagcompound.a("brightness", nbtbase);
            });
        }

    }

    @Override
    public Packet<PacketListenerPlayOut> S() {
        return new PacketPlayOutSpawnEntity(this);
    }

    @Override
    public AxisAlignedBB j_() {
        return this.cullingBoundingBox;
    }

    @Override
    public EnumPistonReaction l_() {
        return EnumPistonReaction.d;
    }

    @Override
    public boolean c_() {
        return true;
    }

    public Quaternionf orientation() {
        return this.orientation;
    }

    @Nullable
    public CalecheDisplay.j renderState() {
        return this.renderState;
    }

    public void setInterpolationDuration(int i) {
        this.am.b(CalecheDisplay.DATA_INTERPOLATION_DURATION_ID, i);
    }

    public int getInterpolationDuration() {
        return (Integer) this.am.b(CalecheDisplay.DATA_INTERPOLATION_DURATION_ID);
    }

    public void setInterpolationDelay(int i) {
        this.am.a(CalecheDisplay.DATA_INTERPOLATION_START_DELTA_TICKS_ID, i, true);
    }

    public int getInterpolationDelay() {
        return (Integer) this.am.b(CalecheDisplay.DATA_INTERPOLATION_START_DELTA_TICKS_ID);
    }

    public void setBillboardConstraints(CalecheDisplay.BillboardConstraints display_billboardconstraints) {
        this.am.b(CalecheDisplay.DATA_BILLBOARD_RENDER_CONSTRAINTS_ID, display_billboardconstraints.getId());
    }

    public CalecheDisplay.BillboardConstraints getBillboardConstraints() {
        return (CalecheDisplay.BillboardConstraints) CalecheDisplay.BillboardConstraints.BY_ID.apply((Byte) this.am.b(CalecheDisplay.DATA_BILLBOARD_RENDER_CONSTRAINTS_ID));
    }

    public void setBrightnessOverride(@Nullable Brightness brightness) {
        this.am.b(CalecheDisplay.DATA_BRIGHTNESS_OVERRIDE_ID, brightness != null ? brightness.a() : -1);
    }

    @Nullable
    public Brightness getBrightnessOverride() {
        int i = (Integer) this.am.b(CalecheDisplay.DATA_BRIGHTNESS_OVERRIDE_ID);

        return i != -1 ? Brightness.a(i) : null;
    }

    private int getPackedBrightnessOverride() {
        return (Integer) this.am.b(CalecheDisplay.DATA_BRIGHTNESS_OVERRIDE_ID);
    }

    public void setViewRange(float f) {
        this.am.b(CalecheDisplay.DATA_VIEW_RANGE_ID, f);
    }

    public float getViewRange() {
        return (Float) this.am.b(CalecheDisplay.DATA_VIEW_RANGE_ID);
    }

    public void setShadowRadius(float f) {
        this.am.b(CalecheDisplay.DATA_SHADOW_RADIUS_ID, f);
    }

    public float getShadowRadius() {
        return (Float) this.am.b(CalecheDisplay.DATA_SHADOW_RADIUS_ID);
    }

    public void setShadowStrength(float f) {
        this.am.b(CalecheDisplay.DATA_SHADOW_STRENGTH_ID, f);
    }

    public float getShadowStrength() {
        return (Float) this.am.b(CalecheDisplay.DATA_SHADOW_STRENGTH_ID);
    }

    public void setWidth(float f) {
        this.am.b(CalecheDisplay.DATA_WIDTH_ID, f);
    }

    public float getWidth() {
        return (Float) this.am.b(CalecheDisplay.DATA_WIDTH_ID);
    }

    public void setHeight(float f) {
        this.am.b(CalecheDisplay.DATA_HEIGHT_ID, f);
    }

    public int getGlowColorOverride() {
        return (Integer) this.am.b(CalecheDisplay.DATA_GLOW_COLOR_OVERRIDE_ID);
    }

    public void setGlowColorOverride(int i) {
        this.am.b(CalecheDisplay.DATA_GLOW_COLOR_OVERRIDE_ID, i);
    }

    public float calculateInterpolationProgress(float f) {
        int i = this.interpolationDuration;

        if (i <= 0) {
            return 1.0F;
        } else {
            float f1 = (float) ((long) this.ag - this.interpolationStartClientTick);
            float f2 = f1 + f;
            float f3 = MathHelper.a(MathHelper.g(f2, 0.0F, (float) i), 0.0F, 1.0F);

            this.lastProgress = f3;
            return f3;
        }
    }

    public float getHeight() {
        return (Float) this.am.b(CalecheDisplay.DATA_HEIGHT_ID);
    }

    @Override
    public void e(double d0, double d1, double d2) {
        super.e(d0, d1, d2);
        this.updateCulling();
    }

    private void updateCulling() {
        float f = this.getWidth();
        float f1 = this.getHeight();

        if (f != 0.0F && f1 != 0.0F) {
            this.as = false;
            float f2 = f / 2.0F;
            double d0 = this.dn();
            double d1 = this.dp();
            double d2 = this.dt();

            this.cullingBoundingBox = new AxisAlignedBB(d0 - (double) f2, d1, d2 - (double) f2, d0 + (double) f2, d1 + (double) f1, d2 + (double) f2);
        } else {
            this.as = true;
        }

    }

    @Override
    public void b_(float f) {
        super.b_(f);
        this.updateOrientation();
    }

    @Override
    public void a_(float f) {
        super.a_(f);
        this.updateOrientation();
    }

    private void updateOrientation() {
        this.orientation.rotationYXZ(-0.017453292F * this.dy(), 0.017453292F * this.dA(), 0.0F);
    }

    @Override
    public boolean a(double d0) {
        return d0 < MathHelper.k((double) this.getViewRange() * 64.0D * cx());
    }

    @Override
    public int k_() {
        int i = this.getGlowColorOverride();

        return i != -1 ? i : super.k_();
    }

    private CalecheDisplay.j createFreshRenderState() {
        return new CalecheDisplay.j(CalecheDisplay.GenericInterpolator.constant(createTransformation(this.am)), this.getBillboardConstraints(), this.getPackedBrightnessOverride(), CalecheDisplay.FloatInterpolator.constant(this.getShadowRadius()), CalecheDisplay.FloatInterpolator.constant(this.getShadowStrength()), this.getGlowColorOverride());
    }

    private CalecheDisplay.j createInterpolatedRenderState(CalecheDisplay.j display_j, float f) {
        Transformation transformation = (Transformation) display_j.transformation.get(f);
        float f1 = display_j.shadowRadius.get(f);
        float f2 = display_j.shadowStrength.get(f);

        return new CalecheDisplay.j(new CalecheDisplay.l(transformation, createTransformation(this.am)), this.getBillboardConstraints(), this.getPackedBrightnessOverride(), new CalecheDisplay.h(f1, this.getShadowRadius()), new CalecheDisplay.h(f2, this.getShadowStrength()), this.getGlowColorOverride());
    }

    public static record j(CalecheDisplay.GenericInterpolator<Transformation> transformation, CalecheDisplay.BillboardConstraints billboardConstraints, int brightnessOverride, CalecheDisplay.FloatInterpolator shadowRadius, CalecheDisplay.FloatInterpolator shadowStrength, int glowColorOverride) {

    }

    public static enum BillboardConstraints implements INamable {

        FIXED((byte) 0, "fixed"), VERTICAL((byte) 1, "vertical"), HORIZONTAL((byte) 2, "horizontal"), CENTER((byte) 3, "center");

        public static final Codec<CalecheDisplay.BillboardConstraints> CODEC = INamable.a(CalecheDisplay.BillboardConstraints::values);
        public static final IntFunction<CalecheDisplay.BillboardConstraints> BY_ID = ByIdMap.a(CalecheDisplay.BillboardConstraints::getId, values(), ByIdMap.a.a);
        private final byte id;
        private final String name;

        private BillboardConstraints(byte b0, String s) {
            this.name = s;
            this.id = b0;
        }

        @Override
        public String c() {
            return this.name;
        }

        byte getId() {
            return this.id;
        }
    }

    @FunctionalInterface
    public interface GenericInterpolator<T> {

        static <T> CalecheDisplay.GenericInterpolator<T> constant(T t0) {
            return (f) -> {
                return t0;
            };
        }

        T get(float f);
    }

    @FunctionalInterface
    public interface FloatInterpolator {

        static CalecheDisplay.FloatInterpolator constant(float f) {
            return (f1) -> {
                return f;
            };
        }

        float get(float f);
    }

    protected static record l(Transformation previous, Transformation current) implements CalecheDisplay.GenericInterpolator<Transformation> {

        @Override
        public Transformation get(float f) {
            return (double) f >= 1.0D ? this.current : this.previous.a(this.current, f);
        }
    }

    protected static record h(float previous, float current) implements CalecheDisplay.FloatInterpolator {

        @Override
        public float get(float f) {
            return MathHelper.a(f, this.previous, this.current);
        }
    }

    protected static record ColorInterpolator(int previous, int current) implements CalecheDisplay.IntInterpolator {

        @Override
        public int get(float f) {
            return ColorUtil.b.a(f, this.previous, this.current);
        }
    }

    protected static record i(int previous, int current) implements CalecheDisplay.IntInterpolator {

        @Override
        public int get(float f) {
            return MathHelper.a(f, this.previous, this.current);
        }
    }

    @FunctionalInterface
    public interface IntInterpolator {

        static CalecheDisplay.IntInterpolator constant(int i) {
            return (f) -> {
                return i;
            };
        }

        int get(float f);
    }

    public static Consumer<String> prefix(String var0, Consumer<String> var1) {
        return (var2) -> {
            var1.accept(var0 + var2);
        };
    }

}
