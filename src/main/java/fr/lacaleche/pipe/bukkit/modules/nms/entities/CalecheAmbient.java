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
import org.bukkit.craftbukkit.v1_20_R1.event.CraftEventFactory;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.time.temporal.ChronoField;

public abstract class CalecheAmbient extends EntityAmbient {

    public CalecheAmbient(EntityTypes<? extends EntityBat> entitytypes, World world) {
        super(entitytypes, world);
    }

    /**
     * isFlapping aP
     */
    @Override
    public boolean aP() {
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
     * getSoundVolume eR
     */
    @Override
    protected float eR() {
        return 0.1F;
    }

    /**
     * getVoicePitch eS
     */
    @Override
    public float eS() {
        return super.eS() * 0.95F;
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
     * getDeathSound g_
     */
    @Override
    protected SoundEffect g_() {
        return null;
    }

    /**
     * isPushable bp
     */
    @Override
    public boolean bp() {
        return false;
    }

    /**
     * doPush A
     */
    @Override
    protected void A(Entity entity) {}

    /**
     * pushEntities fd
     */
    @Override
    protected void fd() {}

    /**
     * tick l
     */
    @Override
    public void l() {
        super.l();

    }

    /**
     * customServerAiStep W
     */
    @Override
    protected void W() {
        super.W();
    }

    /**
     * getMovementEmission aS
     */
    @Override
    protected Entity.MovementEmission aS() {
        return MovementEmission.a;
    }

    /**
     * checkFallDamage a
     */
    @Override
    protected void a(double d0, boolean flag, IBlockData iblockdata, BlockPosition blockposition) {}

    /**
     * isIgnoringBlockTriggers c_
     */
    @Override
    public boolean c_() {
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
