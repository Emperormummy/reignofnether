package com.solegendary.reignofnether.resources;

import com.solegendary.reignofnether.util.MyRenderer;
import net.minecraft.util.FormattedCharSequence;

// defined here because we need to be able to access in both
// static (for ProductionItems) and nonstatic (for getCurrentPopulation) contexts
// and we can't declare static getters in the Unit interface
public class ResourceCosts {

    public static FormattedCharSequence getFormattedCost(ResourceCost resCost) {
        String str = "";
        if (resCost.food > 0)
            str += "\uE000  " + resCost.food + "     ";
        if (resCost.wood > 0)
            str += "\uE001  " + resCost.wood + "     ";
        if (resCost.ore > 0)
            str += "\uE002  " + resCost.ore + "     ";
        str = str.trim();
        return FormattedCharSequence.forward(str, MyRenderer.iconStyle);
    }
    public static FormattedCharSequence getFormattedCostPopAndTime(ResourceCost resCost) {
        return FormattedCharSequence.forward("\uE003  " + resCost.population + "     \uE004  " + resCost.ticks/ResourceCost.TICKS_PER_SECOND + "s", MyRenderer.iconStyle);
    }
    public static FormattedCharSequence getFormattedCostTime(ResourceCost resCost) {
        return FormattedCharSequence.forward("\uE004  " + resCost.ticks/ResourceCost.TICKS_PER_SECOND + "s", MyRenderer.iconStyle);
    }

    public static final int REPLANT_WOOD_COST = 3;
    public static final int MAX_POPULATION = 100; // max possible pop you can have regardless of buildings

    // ******************* UNITS ******************* //
    // Monsters
    public static ResourceCost ZOMBIE_VILLAGER = ResourceCost.Unit(50,0,0,15,1);
    public static ResourceCost CREEPER = ResourceCost.Unit(50,0,100,35,2);
    public static ResourceCost SKELETON = ResourceCost.Unit(70,50,0,20,1);
    public static ResourceCost ZOMBIE = ResourceCost.Unit(90,0,0,20,1);
    public static ResourceCost SPIDER = ResourceCost.Unit(80,60,0,25,2);
    public static ResourceCost POISON_SPIDER = ResourceCost.Unit(80,0,60,25,2);
    public static ResourceCost ENDERMAN = ResourceCost.Unit(100,100,100,30,3);

    // Villagers
    public static ResourceCost VILLAGER = ResourceCost.Unit(50,0,0,15,1);
    public static ResourceCost IRON_GOLEM = ResourceCost.Unit(0,0,250,45,6);
    public static ResourceCost PILLAGER = ResourceCost.Unit(100,80,0,30,2);
    public static ResourceCost VINDICATOR = ResourceCost.Unit(150,0,0,30,2);
    public static ResourceCost WITCH = ResourceCost.Unit(100,100,100,35,3);


    // ******************* BUILDINGS ******************* //
    public static ResourceCost STOCKPILE = ResourceCost.Building(0,75,0, 0);

    // Monsters
    public static ResourceCost MAUSOLEUM = ResourceCost.Building(0,300,150, 10);
    public static ResourceCost HAUNTED_HOUSE = ResourceCost.Building(0,100,0, 10);
    public static ResourceCost PUMPKIN_FARM = ResourceCost.Building(0,120,0, 0);
    public static ResourceCost GRAVEYARD = ResourceCost.Building(0,150,0, 0);
    public static ResourceCost LABORATORY = ResourceCost.Building(0,250,150, 0);
    public static ResourceCost SPIDER_LAIR = ResourceCost.Building(0,150,100, 0);

    // Villagers
    public static ResourceCost TOWN_CENTRE = ResourceCost.Building(0,300,150, 10);
    public static ResourceCost VILLAGER_HOUSE = ResourceCost.Building(0,100,0, 10);
    public static ResourceCost WHEAT_FARM = ResourceCost.Building(0,100,0, 0);
    public static ResourceCost BARRACKS = ResourceCost.Building(0,150,0, 0);
    public static ResourceCost BLACKSMITH = ResourceCost.Building(0,250,150, 0);


    // ******************* RESEARCH ******************* //
    public static ResourceCost RESEARCH_VINDICATOR_AXES = ResourceCost.Research(0,200,400, 120);
    public static ResourceCost RESEARCH_PILLAGER_CROSSBOWS = ResourceCost.Research(0,500,250, 120);
    public static ResourceCost RESEARCH_LAB_LIGHTNING_ROD = ResourceCost.Research(0,100,500, 120);
    public static ResourceCost RESEARCH_RESOURCE_CAPACITY = ResourceCost.Research(200,200,0, 90);
    public static ResourceCost RESEARCH_SPIDER_JOCKEYS = ResourceCost.Research(300,300,0, 120);
    public static ResourceCost RESEARCH_POISON_SPIDERS = ResourceCost.Research(300,0,300, 100);
}
