package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.SystemUtils;
import net.minecraft.commands.CommandListenerWrapper;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.ChatComponentUtils;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.util.FormattedString;
import net.minecraft.util.INamable;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.level.World;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
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
    public static final DataWatcherObject<Integer> DATA_LINE_WIDTH_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.b);
    public static final DataWatcherObject<Integer> DATA_BACKGROUND_COLOR_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.b);
    private static final DataWatcherObject<Byte> DATA_TEXT_OPACITY_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.a);
    private static final DataWatcherObject<Byte> DATA_STYLE_FLAGS_ID = DataWatcher.a(CalecheTextDisplay.class, DataWatcherRegistry.a);
    private static final IntSet TEXT_RENDER_STATE_IDS = IntSet.of(new int[]{CalecheTextDisplay.DATA_TEXT_ID.a(), CalecheTextDisplay.DATA_LINE_WIDTH_ID.a(), CalecheTextDisplay.DATA_BACKGROUND_COLOR_ID.a(), CalecheTextDisplay.DATA_TEXT_OPACITY_ID.a(), CalecheTextDisplay.DATA_STYLE_FLAGS_ID.a()});
    @Nullable
    private CalecheTextDisplay.CachedInfo clientDisplayCache;
    @Nullable
    private CalecheTextDisplay.e textRenderState;

    public CalecheTextDisplay(EntityTypes<?> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    protected void a_() {
        super.a_();
        this.am.a(CalecheTextDisplay.DATA_TEXT_ID, IChatBaseComponent.h());
        this.am.a(CalecheTextDisplay.DATA_LINE_WIDTH_ID, 200);
        this.am.a(CalecheTextDisplay.DATA_BACKGROUND_COLOR_ID, 1073741824);
        this.am.a(CalecheTextDisplay.DATA_TEXT_OPACITY_ID, (byte) -1);
        this.am.a(CalecheTextDisplay.DATA_STYLE_FLAGS_ID, (byte) 0);
    }

    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        super.a(datawatcherobject);
        if (CalecheTextDisplay.TEXT_RENDER_STATE_IDS.contains(datawatcherobject.a())) {
            this.updateRenderState = true;
        }

    }

    public IChatBaseComponent getText() {
        return (IChatBaseComponent) this.am.b(CalecheTextDisplay.DATA_TEXT_ID);
    }

    public void setText(IChatBaseComponent ichatbasecomponent) {
        this.am.b(CalecheTextDisplay.DATA_TEXT_ID, ichatbasecomponent);
    }

    public int getLineWidth() {
        return (Integer) this.am.b(CalecheTextDisplay.DATA_LINE_WIDTH_ID);
    }

    private void setLineWidth(int i) {
        this.am.b(CalecheTextDisplay.DATA_LINE_WIDTH_ID, i);
    }

    public byte getTextOpacity() {
        return (Byte) this.am.b(CalecheTextDisplay.DATA_TEXT_OPACITY_ID);
    }

    public void setTextOpacity(byte b0) {
        this.am.b(CalecheTextDisplay.DATA_TEXT_OPACITY_ID, b0);
    }

    public int getBackgroundColor() {
        return (Integer) this.am.b(CalecheTextDisplay.DATA_BACKGROUND_COLOR_ID);
    }

    private void setBackgroundColor(int i) {
        this.am.b(CalecheTextDisplay.DATA_BACKGROUND_COLOR_ID, i);
    }

    public byte getFlags() {
        return (Byte) this.am.b(CalecheTextDisplay.DATA_STYLE_FLAGS_ID);
    }

    public void setFlags(byte b0) {
        this.am.b(CalecheTextDisplay.DATA_STYLE_FLAGS_ID, b0);
    }

    private static byte loadFlag(byte b0, NBTTagCompound nbttagcompound, String s, byte b1) {
        return nbttagcompound.q(s) ? (byte) (b0 | b1) : b0;
    }

    @Override
    protected void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.b("line_width", 99)) {
            this.setLineWidth(nbttagcompound.h("line_width"));
        }

        if (nbttagcompound.b("text_opacity", 99)) {
            this.setTextOpacity(nbttagcompound.f("text_opacity"));
        }

        if (nbttagcompound.b("background", 99)) {
            this.setBackgroundColor(nbttagcompound.h("background"));
        }

        byte b0 = loadFlag((byte) 0, nbttagcompound, "shadow", (byte) 1);

        b0 = loadFlag(b0, nbttagcompound, "see_through", (byte) 2);
        b0 = loadFlag(b0, nbttagcompound, "default_background", (byte) 4);
        DataResult<Pair<CalecheTextDisplay.Align, NBTBase>> dataresult = CalecheTextDisplay.Align.CODEC.decode(DynamicOpsNBT.a, nbttagcompound.c("alignment")); // CraftBukkit - decompile error
        Logger logger = CalecheDisplay.LOGGER;

        Objects.requireNonNull(logger);
        Optional<CalecheTextDisplay.Align> optional = dataresult.resultOrPartial(CalecheDisplay.prefix("Display entity", logger::error)).map(Pair::getFirst);

        if (optional.isPresent()) {
            byte b1;

            switch ((CalecheTextDisplay.Align) optional.get()) {
                case CENTER:
                    b1 = b0;
                    break;
                case LEFT:
                    b1 = (byte) (b0 | 8);
                    break;
                case RIGHT:
                    b1 = (byte) (b0 | 16);
                    break;
                default:
                    throw new IncompatibleClassChangeError();
            }

            b0 = b1;
        }

        this.setFlags(b0);
        if (nbttagcompound.b("text", 8)) {
            String s = nbttagcompound.l("text");

            try {
                IChatMutableComponent ichatmutablecomponent = IChatBaseComponent.ChatSerializer.a(s);

                if (ichatmutablecomponent != null) {
                    CommandListenerWrapper commandlistenerwrapper = this.da().a(2);
                    IChatMutableComponent ichatmutablecomponent1 = ChatComponentUtils.a(commandlistenerwrapper, (IChatBaseComponent) ichatmutablecomponent, this, 0);

                    this.setText(ichatmutablecomponent1);
                } else {
                    this.setText(IChatBaseComponent.h());
                }
            } catch (Exception exception) {
                CalecheDisplay.LOGGER.warn("Failed to parse display entity text {}", s, exception);
            }
        }

    }

    private static void storeFlag(byte b0, NBTTagCompound nbttagcompound, String s, byte b1) {
        nbttagcompound.a(s, (b0 & b1) != 0);
    }

    @Override
    protected void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.a("text", IChatBaseComponent.ChatSerializer.a(this.getText()));
        nbttagcompound.a("line_width", this.getLineWidth());
        nbttagcompound.a("background", this.getBackgroundColor());
        nbttagcompound.a("text_opacity", this.getTextOpacity());
        byte b0 = this.getFlags();

        storeFlag(b0, nbttagcompound, "shadow", (byte) 1);
        storeFlag(b0, nbttagcompound, "see_through", (byte) 2);
        storeFlag(b0, nbttagcompound, "default_background", (byte) 4);
        CalecheTextDisplay.Align.CODEC.encodeStart(DynamicOpsNBT.a, getAlign(b0)).result().ifPresent((nbtbase) -> {
            nbttagcompound.a("alignment", nbtbase);
        });
    }

    @Override
    protected void a(boolean flag, float f) {
        if (flag && this.textRenderState != null) {
            this.textRenderState = this.createInterpolatedTextRenderState(this.textRenderState, f);
        } else {
            this.textRenderState = this.createFreshTextRenderState();
        }

        this.clientDisplayCache = null;
    }

    @Nullable
    public CalecheTextDisplay.e textRenderState() {
        return this.textRenderState;
    }

    private CalecheTextDisplay.e createFreshTextRenderState() {
        return new e(this.getText(), this.getLineWidth(), CalecheDisplay.IntInterpolator.constant(this.getTextOpacity()), CalecheDisplay.IntInterpolator.constant(this.getBackgroundColor()), this.getFlags());
    }

    private CalecheTextDisplay.e createInterpolatedTextRenderState(CalecheTextDisplay.e display_textdisplay_e, float f) {
        int i = display_textdisplay_e.backgroundColor.get(f);
        int j = display_textdisplay_e.textOpacity.get(f);

        return new e(this.getText(), this.getLineWidth(), new CalecheDisplay.i(j, this.getTextOpacity()), new CalecheDisplay.ColorInterpolator(i, this.getBackgroundColor()), this.getFlags());
    }

    public CalecheTextDisplay.CachedInfo cacheDisplay(CalecheTextDisplay.LineSplitter display_textdisplay_linesplitter) {
        if (this.clientDisplayCache == null) {
            if (this.textRenderState != null) {
                this.clientDisplayCache = display_textdisplay_linesplitter.split(this.textRenderState.text(), this.textRenderState.lineWidth());
            } else {
                this.clientDisplayCache = new CachedInfo(List.of(), 0);
            }
        }

        return this.clientDisplayCache;
    }

    public static CalecheTextDisplay.Align getAlign(byte b0) {
        return (b0 & 8) != 0 ? CalecheTextDisplay.Align.LEFT : ((b0 & 16) != 0 ? CalecheTextDisplay.Align.RIGHT : CalecheTextDisplay.Align.CENTER);
    }

    public static enum Align implements INamable {

        CENTER("center"), LEFT("left"), RIGHT("right");

        public static final Codec<CalecheTextDisplay.Align> CODEC = INamable.a(CalecheTextDisplay.Align::values);
        private final String name;

        private Align(String s) {
            this.name = s;
        }

        @Override
        public String c() {
            return this.name;
        }
    }

    public static record e(IChatBaseComponent text, int lineWidth, CalecheDisplay.IntInterpolator textOpacity, CalecheDisplay.IntInterpolator backgroundColor, byte flags) {

    }

    public static record CachedInfo(List<CalecheTextDisplay.CachedLine> lines, int width) {

    }

    @FunctionalInterface
    public interface LineSplitter {

        CalecheTextDisplay.CachedInfo split(IChatBaseComponent ichatbasecomponent, int i);
    }

    public static record CachedLine(FormattedString contents, int width) {

    }

}
