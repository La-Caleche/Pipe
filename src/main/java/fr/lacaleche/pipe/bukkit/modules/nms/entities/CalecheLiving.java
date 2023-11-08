package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import fr.lacaleche.core.utils.logger.Logger;
import net.minecraft.core.BlockPosition;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vector3f;
import net.minecraft.core.particles.ParticleParamBlock;
import net.minecraft.core.particles.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.protocol.game.PacketPlayOutEntityEquipment;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.world.EnumHand;
import net.minecraft.world.EnumInteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.entity.projectile.EntityArrow;
import net.minecraft.world.entity.vehicle.EntityMinecartAbstract;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.EnumSkyBlock;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.EnumPistonReaction;
import net.minecraft.world.phys.AxisAlignedBB;
import net.minecraft.world.phys.Vec3D;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public abstract class CalecheLiving extends EntityLiving {

    public CalecheLiving(EntityTypes<? extends EntityLiving> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    public float getBukkitYaw() {
        return this.cm();
    }

    /**
     * refreshDimensions d_
     * */
    @Override
    public void d_() {}

    /**
     * isEffectiveAi cV
     * */
    @Override
    public boolean cV() {
        return false;
    }

    /**
     * defineSynchedData a_
     * */
    @Override
    protected void a_() {
        super.a_();
    }

    /**
     * getHandSlots bI
     * */
    @Override
    public Iterable<ItemStack> bI() {
        return NonNullList.a();
    }

    /**
     * getArmorSlots bJ
     * */
    @Override
    public Iterable<ItemStack> bJ() {
        return NonNullList.a();
    }

    /**
     * getItemBySlot c
     * */
    @Override
    public ItemStack c(EnumItemSlot enumitemslot) {
        return ItemStack.b;
    }

    /**
     * setItemSlot a
     * */
    @Override
    public void a(EnumItemSlot enumitemslot, ItemStack itemstack) {
        this.setItemSlot(enumitemslot, itemstack, false);
    }

    /**
     * canTakeItem f
     * */
    @Override
    public boolean f(ItemStack itemstack) {
        return false;
    }

    /**
     * addAdditionalSaveData b
     * */
    @Override
    public void b(NBTTagCompound nbttagcompound) {}

    /**
     * readAdditionalSaveData a
     * */
    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    /**
     * isCollidable bn
     * */
    @Override
    public boolean bn() {
        return false;
    }

    /**
     * doPush A
     * */
    @Override
    protected void A(Entity entity) {}

    /**
     * pushEntities fd
     * */
    @Override
    protected void fd() {}

    /**
     * interactAt a
     * */
    @Override
    public EnumInteractionResult a(EntityHuman entityhuman, Vec3D vec3d, EnumHand enumhand) {
        return EnumInteractionResult.d;
    }

    /**
     * hurt a
     * */
    @Override
    public boolean a(DamageSource damagesource, float f) {
        return false;
    }

    /**
     * handleEntityEvent b
     * */
    @Override
    public void b(byte b0) {
        super.a(b0);
    }

    /**
     * shouldRenderAtSqrDistance a
     * */
    @Override
    public boolean a(double d0) {
        return false;
    }

    /**
     * tickHeadTurn e
     * */
    @Override
    protected float e(float f, float f1) {
        return 0.0F;
    }

    /**
     * getStandingEyeHeight b
     * */
    @Override
    protected abstract float b(EntityPose entitypose, EntitySize entitysize);

    /**
     * getMyRidingOffset bw
     * */
    @Override
    public abstract double bw();

    /**
     * travel h
     * */
    @Override
    public void h(Vec3D vec3d) {}

    /**
     * setYBodyRot o
     * */
    @Override
    public void o(float f) {}

    /**
     * setYHeadRot n
     * */
    @Override
    public void n(float f) {}

    /**
     * tick l
     * */
    @Override
    public void l() {
        super.l();
    }

    /**
     * isBaby h_
     * */
    @Override
    public boolean h_() {
        return false;
    }

    /**
     * shouldDropExperience dY
     * */
    @Override
    public boolean dY() {
        return false;
    }

    /**
     * kill ah
     * */
    @Override
    public void ah() {
        super.ah();
    }

    /**
     * ignoreExplosion cJ
     * */
    @Override
    public boolean cJ() {
        return true;
    }

    /**
     * getPistonPushReaction l_
     * */
    @Override
    public EnumPistonReaction l_() {
        return EnumPistonReaction.d;
    }

    /**
     * isPickable bo
     * */
    @Override
    public boolean bo() {
        return false;
    }

    /**
     * skipAttackInteraction r
     * */
    @Override
    public boolean r(Entity entity) {
        return true;
    }

    /**
     * getMainArm fh
     * */
    @Override
    public EnumMainHand fh() {
        return EnumMainHand.b;
    }

    /**
     * getFallSounds eC
     * */
    @Nullable
    @Override
    public EntityLiving.a eC() {
        return null;
    }

    /**
     * getHurtSound d
     * */
    @Nullable
    @Override
    protected SoundEffect d(DamageSource damagesource) {
        return null;
    }

    /**
     * getDeathSound g_
     * */
    @Nullable
    @Override
    protected SoundEffect g_() {
        return null;
    }

    /**
     * thunderHit a
     * */
    @Override
    public void a(WorldServer worldserver, EntityLightning entitylightning) {}

    /**
     * isAffectedByPotions ft
     * */
    @Override
    public boolean ft() {
        return false;
    }

    /**
     * onSyncedDataUpdated a
     * */
    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {}

    /**
     * attackable fu
     * */
    @Override
    public boolean fu() {
        return false;
    }

    /**
     * getPickResult dv
     * */
    @Override
    public ItemStack dv() {
        return ItemStack.b;
    }

    /**
     * canBeSeenByAnyone el
     * */
    @Override
    public boolean el() {
        return !this.G_() && this.bs();
    }

}
