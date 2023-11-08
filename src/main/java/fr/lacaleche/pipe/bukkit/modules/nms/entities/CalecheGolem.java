package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.EntityGolem;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;
import net.minecraft.world.level.block.state.IBlockData;

import javax.annotation.Nullable;

public abstract class CalecheGolem extends EntityGolem {

    public CalecheGolem(EntityTypes<? extends EntityGolem> entitytypes, World world) {
        super(entitytypes, world);
    }

    /**
     * registerGoals x
     */
    @Override
    protected void x() {
    }

    /**
     * defineSynchedData a_
     */
    @Override
    protected void a_() {
        super.a_();
    }

    /**
     * isBaby h_
     */
    @Override
    public boolean h_() {
        return false;
    }

    /**
     * getExperienceReward ea
     */
    @Override
    public int ea() {
        return 0;
    }

    /**
     * setBaby a
     */
    @Override
    public void a(boolean flag) {}

    /**
     * onSyncedDataUpdated a
     */
    @Override
    public void a(DataWatcherObject<?> datawatcherobject) {
        super.a(datawatcherobject);
    }

    /**
     * tick l
     */
    @Override
    public void l() {
        super.l();
    }

    /**
     * hurt a
     */
    @Override
    public boolean a(DamageSource damagesource, float f) {
        return false;
    }

    /**
     * doHurtTarget z
     */
    @Override
    public boolean z(Entity entity) {
        return false;
    }

    /**
     * getAmbientSound s
     */
    @Override
    protected SoundEffect s() {
        return null;
    }

    /**
     * getHurtSound d
     */
    @Override
    protected SoundEffect d(DamageSource damagesource) {
        return null;
    }

    /**
     * getDeathSound g_
     */
    @Override
    protected SoundEffect g_() {
        return null;
    }

    /**
     * playStepSound b
     */
    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {}

    /**
     * getMobType eN
     */
    public abstract EnumMonsterType eN();

    /**
     * populateDefaultEquipmentSlots a
     */
    @Override
    protected void a(RandomSource randomsource, DifficultyDamageScaler difficultydamagescaler) {
        super.a(randomsource, difficultydamagescaler);
    }

    /**
     * addAdditionalSaveData b
     */
    @Override
    public void b(NBTTagCompound nbttagcompound) {}

    /**
     * readAdditionalSaveData a
     */
    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    /**
     * wasKilled a
     */
    @Override
    public boolean a(WorldServer worldserver, EntityLiving entityliving) {
        return false;
    }

    /**
     * getStandingEyeHeight b
     */
    @Override
    protected abstract float b(EntityPose entitypose, EntitySize entitysize);

    /**
     * canHoldItem k
     */
    @Override
    public boolean k(ItemStack itemstack) {
        return false;
    }

    /**
     * wantsToPickUp j
     */
    @Override
    public boolean j(ItemStack itemstack) {
        return false;
    }

    /**
     * finalizeSpawn a
     */
    @Nullable
    @Override
    public GroupDataEntity a(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) {
        return super.a(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
    }

    /**
     * getMyRidingOffset bw
     */
    @Override
    public abstract double bw();

    /**
     * dropCustomDeathLoot a
     */
    @Override
    protected void a(DamageSource damagesource, int i, boolean flag) {}

}
