package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.sounds.SoundCategory;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.tags.TagsFluid;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyDamageScaler;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifiable;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalHurtByTarget;
import net.minecraft.world.entity.ai.goal.target.PathfinderGoalNearestAttackableTarget;
import net.minecraft.world.entity.ai.navigation.Navigation;
import net.minecraft.world.entity.ai.util.PathfinderGoalUtil;
import net.minecraft.world.entity.animal.EntityChicken;
import net.minecraft.world.entity.animal.EntityIronGolem;
import net.minecraft.world.entity.animal.EntityTurtle;
import net.minecraft.world.entity.monster.EntityMonster;
import net.minecraft.world.entity.npc.EntityVillager;
import net.minecraft.world.entity.npc.EntityVillagerAbstract;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.World;
import net.minecraft.world.level.WorldAccess;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.IBlockData;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTransformEvent;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class CalecheMonster extends EntityMonster {

    private int inWaterTime;
    public int conversionTime;
    private int lastTick = MinecraftServer.currentTick;

    public CalecheMonster(EntityTypes<? extends EntityMonster> entitytypes, World world) {
        super(entitytypes, world);
    }

    /**
     * registerGoals u
     */
    @Override
    protected void u() {
    }

    /**
     * defineSynchedData a_
     */
    @Override
    protected void a_() {
        super.a_();
    }

    /**
     * isBaby y_
     */
    @Override
    public boolean y_() {
        return false;
    }

    /**
     * getExperienceReward d
     */
    @Override
    protected int d(EntityHuman entityhuman) {
        return super.d(entityhuman);
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
     * tick k
     */
    @Override
    public void k() {
        super.k();
        this.lastTick = MinecraftServer.currentTick;
    }

    /**
     * aiStep w_
     */
    @Override
    public void w_() {
        super.w_();
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
     * getAmbientSound r
     */
    @Override
    protected SoundEffect r() {
        return null;
    }

    /**
     * getHurtSound c
     */
    @Override
    protected SoundEffect c(DamageSource damagesource) {
        return null;
    }

    /**
     * getDeathSound x_
     */
    @Override
    protected SoundEffect x_() {
        return null;
    }

    /**
     * playStepSound b
     */
    @Override
    protected void b(BlockPosition blockposition, IBlockData iblockdata) {}

    /**
     * getMobType er
     */
    public abstract EnumMonsterType er();

    /**
     * populateDefaultEquipmentSlots a
     */
    @Override
    protected void a(DifficultyDamageScaler difficultydamagescaler) {
        super.a(difficultydamagescaler);
    }

    /**
     * addAdditionalSaveData b
     */
    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
    }

    /**
     * readAdditionalSaveData a
     */
    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
    }

    /**
     * killed a
     */
    @Override
    public void a(WorldServer worldserver, EntityLiving entityliving) {
        super.a(worldserver, entityliving);
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
     * wantsToPickUp l
     */
    @Override
    public boolean l(ItemStack itemstack) {
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
     * getMyRidingOffset bp
     */
    @Override
    public abstract double bp();

    /**
     * dropCustomDeathLoot a
     */
    @Override
    protected void a(DamageSource damagesource, int i, boolean flag) {
        super.a(damagesource, i, flag);
    }

}
