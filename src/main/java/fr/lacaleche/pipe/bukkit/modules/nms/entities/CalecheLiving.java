package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import net.minecraft.core.BlockPosition;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vector3f;
import net.minecraft.core.particles.ParticleParamBlock;
import net.minecraft.core.particles.Particles;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
import java.util.function.Predicate;

public abstract class CalecheLiving extends EntityLiving {

    private int lastTick = MinecraftServer.currentTick;
    private boolean ce;

    public CalecheLiving(EntityTypes<? extends EntityArmorStand> entitytypes, World world) {
        super(entitytypes, world);
    }

    @Override
    public float getBukkitYaw() {
        return this.dn();
    }

    /**
     * refreshDimensions z_
     * */
    @Override
    public void z_() {}

    /**
     * isEffectiveAi eI
     * */
    @Override
    public boolean eI() {
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
     * getHandSlots bB
     * */
    @Override
    public Iterable<ItemStack> bB() {
        return NonNullList.a();
    }

    /**
     * getArmorSlots bC
     * */
    @Override
    public Iterable<ItemStack> bC() {
        return NonNullList.a();
    }

    /**
     * getItemBySlot b
     * */
    @Override
    public ItemStack b(EnumItemSlot enumitemslot) {
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
     * setItemSlot
     * */
    @Override
    public void setItemSlot(EnumItemSlot enumitemslot, ItemStack itemstack, boolean silent) {}

    /**
     * canTakeItem g
     * */
    @Override
    public boolean g(ItemStack itemstack) {
        return false;
    }

    /**
     * addAdditionalSaveData b
     * */
    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    /**
     * readAdditionalSaveData a
     * */
    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    /**
     * isPushable bi
     * */
    @Override
    public boolean bi() {
        return false;
    }

    /**
     * doPush A
     * */
    @Override
    protected void A(Entity entity) {}

    /**
     * pushEntities eG
     * */
    @Override
    protected void eG() {}

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
     * handleEntityEvent a
     * */
    @Override
    public void a(byte b0) {
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
     * getMyRidingOffset bp
     * */
    @Override
    public abstract double bp();

    /**
     * travel h
     * */
    @Override
    public void h(Vec3D vec3d) {}

    /**
     * setYBodyRot m
     * */
    @Override
    public void m(float f) {}

    /**
     * setYHeadRot l
     * */
    @Override
    public void l(float f) {}

    /**
     * tick k
     * */
    @Override
    public void k() {
        super.k();
        this.lastTick = MinecraftServer.currentTick;
    }

    /**
     * updateInvisibilityStatus F
     * */
    @Override
    protected void F() {
        this.j(this.ce);
    }

    /**
     * setInvisible j
     * */
    @Override
    public void j(boolean flag) {
        this.ce = flag;
        super.j(flag);
    }

    /**
     * isBaby y_
     * */
    @Override
    public boolean y_() {
        return false;
    }

    /**
     * shouldDropExperience dI
     * */
    @Override
    protected boolean dI() {
        return false;
    }

    /**
     * kill ag
     * */
    @Override
    public void ag() {
        super.ag();
    }

    /**
     * ignoreExplosion cC
     * */
    @Override
    public boolean cC() {
        return true;
    }

    /**
     * getPistonPushReaction c_
     * */
    @Override
    public EnumPistonReaction c_() {
        return EnumPistonReaction.d;
    }

    /**
     * isPickable bh
     * */
    @Override
    public boolean bh() {
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
     * getMainArm eL
     * */
    @Override
    public EnumMainHand eL() {
        return EnumMainHand.b;
    }

    /**
     * getFallSounds eg
     * */
    @Nullable
    @Override
    public EntityLiving.a eg() {
        return null;
    }

    /**
     * getHurtSound c
     * */
    @Nullable
    @Override
    protected SoundEffect c(DamageSource damagesource) {
        return null;
    }

    /**
     * getDeathSound x_
     * */
    @Nullable
    @Override
    protected SoundEffect x_() {
        return null;
    }

    /**
     * thunderHit a
     * */
    @Override
    public void a(WorldServer worldserver, EntityLightning entitylightning) {}

    /**
     * isAffectedByPotions eX
     * */
    @Override
    public boolean eX() {
        return false;
    }

    /**
     * onSyncedDataUpdated a
     * */
    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        super.a(datawatcherobject);
    }

    /**
     * attackable eY
     * */
    @Override
    public boolean eY() {
        return false;
    }

    /**
     * getDimensions a
     * */
    @Override
    public EntitySize a(EntityPose entitypose) {
        return super.a(entitypose);
    }

    /**
     * getLightProbePosition j
     * */
    @Override
    public Vec3D j(float f) {
        return super.j(f);
    }

    /**
     * getPickResult dk
     * */
    @Override
    public ItemStack dk() {
        return ItemStack.b;
    }

    /**
     * canBeSeenByAnyone dU
     * */
    @Override
    public boolean dU() {
        return !this.bU();
    }

}
