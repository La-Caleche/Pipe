package fr.lacaleche.pipe.bukkit.modules.nms.entities;

import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.sounds.SoundEffect;
import net.minecraft.sounds.SoundEffects;
import net.minecraft.util.MathHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeProvider;
import net.minecraft.world.entity.ai.attributes.GenericAttributes;
import net.minecraft.world.entity.ai.targeting.PathfinderTargetCondition;
import net.minecraft.world.entity.ambient.EntityAmbient;
import net.minecraft.world.entity.ambient.EntityBat;
import net.minecraft.world.entity.player.EntityHuman;
import net.minecraft.world.level.GeneratorAccess;
import net.minecraft.world.level.World;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.phys.Vec3D;
import org.bukkit.craftbukkit.v1_19_R3.event.CraftEventFactory;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public abstract class CalecheAmbient extends EntityAmbient {

    public CalecheAmbient(EntityTypes<? extends EntityBat> entitytypes, World world) {
        super(entitytypes, world);
    }

    /**
     * isFlapping aN
     */
    @Override
    public boolean aN() {
        return false;
    }

    /**
     * defineSynchedData a_
     */
    @Override
    protected void a_() {
        super.a_();
    }

    /**
     * getSoundVolume eN
     */
    @Override
    protected float eN() {
        return 0.1F;
    }

    /**
     * getVoicePitch eO
     */
    @Override
    public float eO() {
        return super.eO() * 0.95F;
    }

    /**
     * getAmbientSound s
     */
    @Override
    public @Nullable SoundEffect s() {
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
     * isCollidable bn
     */
    @Override
    public boolean bn() {
        return false;
    }

    /**
     * doPush A
     */
    @Override
    protected void A(Entity entity) {}

    /**
     * pushEntities eZ
     */
    @Override
    protected void eZ() {}

    /**
     * tick l
     */
    @Override
    public void l() {
        super.l();

    }

    /**
     * customServerAiStep U
     */
    @Override
    protected void U() {
        super.U();
    }

    /**
     * getMovementEmission aQ
     */
    @Override
    protected Entity.MovementEmission aQ() {
        return MovementEmission.a;
    }

    /**
     * checkFallDamage a
     */
    @Override
    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {}

    /**
     * isIgnoringBlockTriggers cq
     */
    @Override
    public boolean cq() {
        return true;
    }

    /**
     * hurt a
     */
    @Override
    public boolean a(DamageSource damagesource, float f) {
        return false;
    }

    /**
     * readAdditionalSaveData a
     */
    @Override
    public void a(NBTTagCompound nbttagcompound) {}

    /**
     * addAdditionalSaveData b
     */
    @Override
    public void b(NBTTagCompound nbttagcompound) {}

    /**
     * getStandingEyeHeight b
     */
    @Override
    protected abstract float b(EntityPose entitypose, EntitySize entitysize);
}
