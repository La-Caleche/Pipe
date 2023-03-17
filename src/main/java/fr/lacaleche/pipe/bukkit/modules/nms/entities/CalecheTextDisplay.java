package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.util.FormattedString;
import net.minecraft.util.INamable;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CalecheTextDisplay extends CalecheDisplay {

    public static final String TAG_TEXT = "text";
    private static final String TAG_LINE_WIDTH = "line_width";
    private static final String TAG_TEXT_OPACITY = "text_opacity";
    private static final String TAG_BACKGROUND_COLOR = "background";
    private static final String TAG_SHADOW = "shadow";
    private static final String TAG_SEE_THROUGH = "see_through";
    private static final String TAG_USE_DEFAULT_BACKGROUND = "default_background";
    private static final String TAG_ALIGNMENT = "alignment";
    public static final byte FLAG_SHADOW = 1;
    public static final byte FLAG_SEE_THROUGH = 2;
    public static final byte FLAG_USE_DEFAULT_BACKGROUND = 4;
    public static final byte FLAG_ALIGN_LEFT = 8;
    public static final byte FLAG_ALIGN_RIGHT = 16;
    private static final byte INITIAL_TEXT_OPACITY = -1;
    public static final int INITIAL_BACKGROUND = 1073741824;
    private static final DataWatcherObject<IChatBaseComponent> DATA_TEXT_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.f);
    private static final DataWatcherObject<Integer> DATA_LINE_WIDTH_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Integer> DATA_BACKGROUND_COLOR_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Byte> DATA_TEXT_OPACITY_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Byte> DATA_STYLE_FLAGS_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.a);
    private final IntInterpolator textOpacity = new IntInterpolator(-1);
    private final IntInterpolator backgroundColor = new ColorInterpolator(1073741824);
    @Nullable
    private CachedInfo clientDisplayCache;

    public CalecheTextDisplay(EntityTypes<?> type, World world) {
        super(type, world);
        this.interpolators.addEntry(DATA_BACKGROUND_COLOR_ID, this.backgroundColor);
        this.interpolators.addEntry(Set.of(DATA_TEXT_OPACITY_ID), (value, dataTracker) -> {
            this.textOpacity.updateValue(value, Integer.valueOf(dataTracker.a(DATA_TEXT_OPACITY_ID) & 255));
        });
    }

    @Override
    protected void a_() {
        super.a_();
        this.am.a(DATA_TEXT_ID, IChatBaseComponent.h());
        this.am.a(DATA_LINE_WIDTH_ID, 200);
        this.am.a(DATA_BACKGROUND_COLOR_ID, 1073741824);
        this.am.a(DATA_TEXT_OPACITY_ID, (byte)-1);
        this.am.a(DATA_STYLE_FLAGS_ID, (byte)0);
    }

    @Override
    public void a(DataWatcherObject<?> data) {
        super.a(data);
        this.clientDisplayCache = null;
    }

    public IChatBaseComponent getText() {
        return this.am.a(DATA_TEXT_ID);
    }

    public void setText(IChatBaseComponent text) {
        this.am.b(DATA_TEXT_ID, text);
    }

    public int getLineWidth() {
        return this.am.a(DATA_LINE_WIDTH_ID);
    }

    public void setLineWidth(int lineWidth) {
        this.am.b(DATA_LINE_WIDTH_ID, lineWidth);
    }

    public byte getTextOpacity(float delta) {
        return (byte)this.textOpacity.get(delta);
    }

    public byte getTextOpacity() {
        return this.am.a(DATA_TEXT_OPACITY_ID);
    }

    public void setTextOpacity(byte textOpacity) {
        this.am.b(DATA_TEXT_OPACITY_ID, textOpacity);
    }

    public int getBackgroundColor(float delta) {
        return this.backgroundColor.get(delta);
    }

    public int getBackgroundColor() {
        return this.am.a(DATA_BACKGROUND_COLOR_ID);
    }

    public void setBackgroundColor(int background) {
        this.am.b(DATA_BACKGROUND_COLOR_ID, background);
    }

    public byte getFlags() {
        return this.am.a(DATA_STYLE_FLAGS_ID);
    }

    public void setFlags(byte flags) {
        this.am.b(DATA_STYLE_FLAGS_ID, flags);
    }

    @Override
    protected void a(NBTTagCompound nbt) {}

    @Override
    protected void b(NBTTagCompound nbt) {}

    public CachedInfo cacheDisplay(LineSplitter splitter) {
        if (this.clientDisplayCache == null) {
            int i = this.getLineWidth();
            this.clientDisplayCache = splitter.split(this.getText(), i);
        }

        return this.clientDisplayCache;
    }

    public Align getAlign(byte flags) {
        if ((flags & 8) != 0) {
            return Align.LEFT;
        } else {
            return (flags & 16) != 0 ? Align.RIGHT : Align.CENTER;
        }
    }

    public enum Align implements INamable {
        CENTER("center"),
        LEFT("left"),
        RIGHT("right");

        public static final Codec<Align> CODEC = INamable.a(Align::values);
        private final String name;

        private Align(String name) {
            this.name = name;
        }

        @Override
        public String c() {
            return this.name;
        }
    }

    public record CachedInfo(List<CachedLine> lines, int width) {
    }

    public record CachedLine(FormattedString contents, int width) {
    }

    @FunctionalInterface
    public interface LineSplitter {
        CachedInfo split(IChatBaseComponent text, int lineWidth);
    }

}
