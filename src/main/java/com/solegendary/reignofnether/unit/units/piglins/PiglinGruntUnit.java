package com.solegendary.reignofnether.unit.units.piglins;

import com.solegendary.reignofnether.building.buildings.netherlings.Portal;
import com.solegendary.reignofnether.hud.AbilityButton;
import com.solegendary.reignofnether.keybinds.Keybindings;
import com.solegendary.reignofnether.research.ResearchClient;
import com.solegendary.reignofnether.research.ResearchServer;
import com.solegendary.reignofnether.research.researchItems.ResearchResourceCapacity;
import com.solegendary.reignofnether.resources.ResourceCosts;
import com.solegendary.reignofnether.unit.UnitClientEvents;
import com.solegendary.reignofnether.unit.goals.*;
import com.solegendary.reignofnether.unit.interfaces.ArmSwingingUnit;
import com.solegendary.reignofnether.unit.interfaces.AttackerUnit;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.interfaces.WorkerUnit;
import com.solegendary.reignofnether.ability.Ability;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PiglinGruntUnit extends Piglin implements Unit, WorkerUnit, AttackerUnit, ArmSwingingUnit {
    // region
    private final ArrayList<BlockPos> checkpoints = new ArrayList<>();
    private int checkpointTicksLeft = UnitClientEvents.CHECKPOINT_TICKS_MAX;
    public ArrayList<BlockPos> getCheckpoints() { return checkpoints; };
    public int getCheckpointTicksLeft() { return checkpointTicksLeft; }
    public void setCheckpointTicksLeft(int ticks) { checkpointTicksLeft = ticks; }
    private boolean isCheckpointGreen = true;
    public boolean isCheckpointGreen() { return isCheckpointGreen; };
    public void setIsCheckpointGreen(boolean green) { isCheckpointGreen = green; };
    private int entityCheckpointId = -1;
    public int getEntityCheckpointId() { return entityCheckpointId; };
    public void setEntityCheckpointId(int id) { entityCheckpointId = id; };

    GarrisonGoal garrisonGoal;
    public GarrisonGoal getGarrisonGoal() { return garrisonGoal; }
    public boolean canGarrison() { return true; }

    public Faction getFaction() {return Faction.PIGLINS;}
    public List<AbilityButton> getAbilityButtons() {return abilityButtons;};
    public List<Ability> getAbilities() {return abilities;}
    public List<ItemStack> getItems() {return items;};
    public MoveToTargetBlockGoal getMoveGoal() {return moveGoal;}
    public SelectedTargetGoal<? extends LivingEntity> getTargetGoal() {return targetGoal;}
    public BuildRepairGoal getBuildRepairGoal() {return buildRepairGoal;}
    public GatherResourcesGoal getGatherResourceGoal() {return gatherResourcesGoal;}
    public ReturnResourcesGoal getReturnResourcesGoal() {return returnResourcesGoal;}
    public int getMaxResources() {return maxResources;}

    private MoveToTargetBlockGoal moveGoal;
    private SelectedTargetGoal<? extends LivingEntity> targetGoal;
    public BuildRepairGoal buildRepairGoal;
    public GatherResourcesGoal gatherResourcesGoal;
    private ReturnResourcesGoal returnResourcesGoal;
    private MeleeAttackUnitGoal attackGoal;

    public LivingEntity getFollowTarget() { return followTarget; }
    public boolean getHoldPosition() { return holdPosition; }
    public void setHoldPosition(boolean holdPosition) { this.holdPosition = holdPosition; }

    // if true causes moveGoal and attackGoal to work together to allow attack moving
    // moves to a block but will chase/attack nearby monsters in range up to a certain distance away
    private LivingEntity followTarget = null; // if nonnull, continuously moves to the target
    private boolean holdPosition = false;
    private BlockPos attackMoveTarget = null;

    // which player owns this unit? this format ensures its synched to client without having to use packets
    public String getOwnerName() { return this.entityData.get(ownerDataAccessor); }
    public void setOwnerName(String name) { this.entityData.set(ownerDataAccessor, name); }
    public static final EntityDataAccessor<String> ownerDataAccessor =
            SynchedEntityData.defineId(PiglinGruntUnit.class, EntityDataSerializers.STRING);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ownerDataAccessor, "");
    }

    // combat stats
    public float getMovementSpeed() {return movementSpeed;}
    public float getUnitMaxHealth() {return maxHealth;}
    public float getUnitArmorValue() {return armorValue;}
    public int getPopCost() {return popCost;}
    public boolean getWillRetaliate() {return willRetaliate;}
    public int getAttackCooldown() {return (int) (20 / attacksPerSecond);}
    public float getAttacksPerSecond() {return attacksPerSecond;}
    public float getAggroRange() {return aggroRange;}
    public boolean getAggressiveWhenIdle() {return aggressiveWhenIdle && !isVehicle();}
    public float getAttackRange() {return attackRange;}
    public float getUnitAttackDamage() {return attackDamage;}
    public BlockPos getAttackMoveTarget() { return attackMoveTarget; }
    public boolean canAttackBuildings() {return canAttackBuildings;}
    public Goal getAttackGoal() { return attackGoal; }
    public AttackBuildingGoal getAttackBuildingGoal() { return null; }
    public void setAttackMoveTarget(@Nullable BlockPos bp) { this.attackMoveTarget = bp; }
    public void setFollowTarget(@Nullable LivingEntity target) { this.followTarget = target; }

    // endregion

    public BlockState getReplantBlockState() {
        return Blocks.WHEAT.defaultBlockState();
    }

    final static public float attackDamage = 1.0f;
    final static public float attacksPerSecond = 0.5f;
    final static public float attackRange = 2; // only used by ranged units or melee building attackers
    final static public float aggroRange = 0;
    final static public boolean willRetaliate = false; // will attack when hurt by an enemy
    final static public boolean aggressiveWhenIdle = false;
    final static public boolean canAttackBuildings = false;
    final static public float maxHealth = 25.0f;
    final static public float armorValue = 0.0f;
    final static public float movementSpeed = 0.25f;
    final static public int popCost = ResourceCosts.PIGLIN_GRUNT.population;
    public int maxResources = 100;

    private final List<AbilityButton> abilityButtons = new ArrayList<>();
    private final List<Ability> abilities = new ArrayList<>();
    private final List<ItemStack> items = new ArrayList<>();

    private boolean isSwingingArmOnce = false;
    private int swingTime = 0;

    public int getSwingTime() {
        return swingTime;
    }

    public void setSwingTime(int time) {
        this.swingTime = time;
    }

    public boolean isSwingingArmOnce() { return isSwingingArmOnce; }

    public void setSwingingArmOnce(boolean swing) {
        isSwingingArmOnce = swing;
    }

    public boolean isSwingingArmRepeatedly() {
        return (this.getGatherResourceGoal().isGathering() || this.getBuildRepairGoal().isBuilding());
    }

    public PiglinGruntUnit(EntityType<? extends Piglin> entityType, Level level) {
        super(entityType, level);

        if (level.isClientSide()) {
            this.abilityButtons.add(Portal.getBuildButton(Keybindings.keyQ));
        }
    }

    @Override
    public boolean removeWhenFarAway(double d) { return false; }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.ATTACK_DAMAGE, PiglinGruntUnit.attackDamage)
                .add(Attributes.MOVEMENT_SPEED, PiglinGruntUnit.movementSpeed)
                .add(Attributes.MAX_HEALTH, PiglinGruntUnit.maxHealth)
                .add(Attributes.ARMOR, PiglinGruntUnit.armorValue);
    }

    @Override
    public boolean isLeftHanded() { return false; }
    @Override // prevent vanilla logic for picking up items
    protected void pickUpItem(ItemEntity pItemEntity) { }
    @Override
    public boolean isConverting() { return false; }
    @Override
    protected void customServerAiStep() { }
    @Override
    public LivingEntity getTarget() {
        return this.targetGoal.getTarget();
    }

    public void tick() {
        this.setCanPickUpLoot(true);
        super.tick();
        Unit.tick(this);
        AttackerUnit.tick(this);
        WorkerUnit.tick(this);
    }

    public void initialiseGoals() {
        this.moveGoal = new MoveToTargetBlockGoal(this, false, 0);
        this.targetGoal = new SelectedTargetGoal<>(this, true, true);
        this.garrisonGoal = new GarrisonGoal(this);
        this.attackGoal = new MeleeAttackUnitGoal(this, getAttackCooldown(), false);
        this.buildRepairGoal = new BuildRepairGoal(this);
        this.gatherResourcesGoal = new GatherResourcesGoal(this);
        this.returnResourcesGoal = new ReturnResourcesGoal(this);
    }

    @Override
    protected void registerGoals() {
        initialiseGoals();

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, attackGoal);
        this.goalSelector.addGoal(2, buildRepairGoal);
        this.goalSelector.addGoal(2, gatherResourcesGoal);
        this.goalSelector.addGoal(2, returnResourcesGoal);
        this.goalSelector.addGoal(2, garrisonGoal);
        this.targetSelector.addGoal(2, targetGoal);
        this.goalSelector.addGoal(3, moveGoal);
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
    }

    @Override
    public void setupEquipmentAndUpgradesClient() {
        if (ResearchClient.hasResearch(ResearchResourceCapacity.itemName))
            this.maxResources = 200;
    }

    @Override
    public void setupEquipmentAndUpgradesServer() {
        if (ResearchServer.playerHasResearch(this.getOwnerName(), ResearchResourceCapacity.itemName))
            this.maxResources = 200;
    }
}
