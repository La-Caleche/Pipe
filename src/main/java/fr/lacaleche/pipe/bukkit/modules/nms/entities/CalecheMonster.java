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
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.entity.monster.EntityZombie;
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

    public CalecheMonster(EntityTypes<? extends EntityMonster> entitytypes, World world) {
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
     * isBaby y_
     */
    @Override
    public boolean y_() {
        return false;
    }

    /**
     * getExperienceReward dX
     */
    @Override
    public int dX() {
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
     * getMobType eJ
     */
    public abstract EnumMonsterType eJ();

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
     * getMyRidingOffset bu
     */
    @Override
    public abstract double bu();

    /**
     * dropCustomDeathLoot a
     */
    @Override
    protected void a(DamageSource damagesource, int i, boolean flag) {}

}
