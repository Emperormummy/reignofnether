package com.solegendary.reignofnether.resources;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

import java.util.List;

public class ResourceSources {
    public static final List<Material> CLEAR_MATERIALS = List.of(Material.WATER, Material.AIR, Material.GRASS);

    public static final int TICKS_PER_SECOND = 20;

    public static ResourceSource getFromBlockPos(BlockPos bp, Level level) {
        Block block = level.getBlockState(bp).getBlock();

        for (List<ResourceSource> resourceBlocks : List.of(FOOD_BLOCKS, WOOD_BLOCKS, ORE_BLOCKS))
            for (ResourceSource resourceBlock : resourceBlocks)
                if (resourceBlock.validBlocks.contains(block))
                    return resourceBlock;
        return null;
    }

    public static ResourceName getBlockResourceName(BlockPos bp, Level level) {
        Block block = level.getBlockState(bp).getBlock();

        if (block == Blocks.FARMLAND)
            return ResourceName.FOOD;

        ResourceSource resBlock = getFromBlockPos(bp, level);
        if (resBlock != null)
            return resBlock.resourceName;

        return ResourceName.NONE;
    }

    // is the given item an item that is worth resources?
    // used for unit item pickups and player resource deposits
    public static ResourceSource getFromItem(Item item) {
        for (List<ResourceSource> resourceBlocks : List.of(FOOD_BLOCKS, WOOD_BLOCKS, ORE_BLOCKS))
            for (ResourceSource resourceBlock : resourceBlocks)
                if (resourceBlock.items.contains(item))
                    return resourceBlock;
        return null;
    }

    public static Resources getTotalResourcesFromItems(List<ItemStack> itemStacks) {
        Resources resources = new Resources("", 0,0,0);
        for (ItemStack itemStack : itemStacks) {
            ResourceSource resBlock = getFromItem(itemStack.getItem());
            if (resBlock != null) {
                int value = resBlock.resourceValue * itemStack.getCount();
                switch (resBlock.resourceName) {
                    case FOOD -> resources.changeInstantly(value, 0, 0);
                    case WOOD -> resources.changeInstantly(0, value, 0);
                    case ORE -> resources.changeInstantly(0, 0, value);
                }
            }
        }
        return resources;
    }

    public static final int REPLANT_TICKS_MAX = 20;

    public static final List<ResourceSource> FOOD_BLOCKS = List.of(
            new ResourceSource("Sugar", // item only
                    List.of(),
                    List.of(Items.SUGAR),
                    0,
                    1,
                    ResourceName.FOOD
            ),
            new ResourceSource("Farmland",
                    List.of(Blocks.FARMLAND),
                    List.of(),
                    0,
                    0,
                    ResourceName.FOOD,
                    (bs) -> bs.getValue(BlockStateProperties.MOISTURE) == 7
            ),
            new ResourceSource("Wheat",
                    List.of(Blocks.WHEAT),
                    List.of(Items.WHEAT),
                    TICKS_PER_SECOND * 7,
                    10,
                    ResourceName.FOOD,
                    (bs) -> bs.getValue(BlockStateProperties.AGE_7) == 7
            ),
            new ResourceSource("Carrots",
                    List.of(Blocks.CARROTS),
                    List.of(Items.CARROT),
                    TICKS_PER_SECOND * 7,
                    14,
                    ResourceName.FOOD,
                    (bs) -> bs.getValue(BlockStateProperties.AGE_7) == 7
            ),
            new ResourceSource("Potatoes",
                    List.of(Blocks.POTATOES),
                    List.of(Items.POTATO, Items.BAKED_POTATO),
                    TICKS_PER_SECOND * 7,
                    18,
                    ResourceName.FOOD,
                    (bs) -> bs.getValue(BlockStateProperties.AGE_7) == 7
            ),
            new ResourceSource("Beetroots",
                    List.of(Blocks.BEETROOTS),
                    List.of(Items.BEETROOT),
                    TICKS_PER_SECOND * 7,
                    10,
                    ResourceName.FOOD,
                    (bs) -> bs.getValue(BlockStateProperties.AGE_3) == 3
            ),
            new ResourceSource("Gourds",
                    List.of(Blocks.MELON, Blocks.PUMPKIN),
                    List.of(Items.MELON, Items.PUMPKIN),
                    TICKS_PER_SECOND * 10,
                    10,
                    ResourceName.FOOD
            ),
            new ResourceSource("Mushrooms",
                    List.of(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM),
                    List.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM),
                    TICKS_PER_SECOND * 5,
                    20,
                    ResourceName.FOOD
            ),
            new ResourceSource("Misc. Forageable",
                    List.of(Blocks.SUGAR_CANE, Blocks.SWEET_BERRY_BUSH),
                    List.of(Items.SWEET_BERRIES, Items.SUGAR_CANE),
                    TICKS_PER_SECOND * 5,
                    20,
                    ResourceName.FOOD
            ),


            new ResourceSource("Extra large food",
                    List.of(),
                    List.of(Items.COOKED_BEEF,  Items.COOKED_CHICKEN, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT,
                            Items.COOKED_MUTTON, Items.CAKE, Items.PUMPKIN_PIE, Items.RABBIT_STEW, Items.ENCHANTED_GOLDEN_APPLE),
                    0,
                    100,
                    ResourceName.FOOD
            ),
            new ResourceSource("Large food",
                    List.of(),
                    List.of(Items.BEEF,Items.CHICKEN, Items.PORKCHOP, Items.MUTTON, Items.RABBIT, Items.MUSHROOM_STEW, Items.BEETROOT_SOUP, Items.GOLDEN_APPLE, Items.GOLDEN_CARROT),
                    0,
                    50,
                    ResourceName.FOOD
            ),
            new ResourceSource("Medium food",
                    List.of(),
                    List.of(Items.EGG, Items.APPLE, Items.BREAD, Items.HONEY_BOTTLE, Items.COD, Items.COOKED_COD, Items.SALMON, Items.COOKED_SALMON, Items.GLOW_BERRIES),
                    0,
                    25,
                    ResourceName.FOOD
            ),
            new ResourceSource("Small food",
                    List.of(),
                    List.of(Items.ROTTEN_FLESH, Items.MELON_SLICE, Items.SPIDER_EYE, Items.POISONOUS_POTATO),
                    0,
                    5,
                    ResourceName.FOOD
            )
    );

    public static final List<ResourceSource> WOOD_BLOCKS = List.of(
            new ResourceSource("Stick", // item only
                    List.of(),
                    List.of(Items.STICK),
                    0,
                    1,
                    ResourceName.WOOD
            ),
            new ResourceSource("Planks",
                    List.of(Blocks.OAK_PLANKS, Blocks.BIRCH_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.MANGROVE_PLANKS, Blocks.SPRUCE_PLANKS),
                    List.of(Items.OAK_PLANKS, Items.BIRCH_PLANKS, Items.ACACIA_PLANKS, Items.DARK_OAK_PLANKS, Items.JUNGLE_PLANKS, Items.MANGROVE_PLANKS, Items.SPRUCE_PLANKS),
                    TICKS_PER_SECOND * 2,
                    2,
                    ResourceName.WOOD
            ),
            new ResourceSource("Logs",
                    List.of(Blocks.OAK_LOG, Blocks.BIRCH_LOG, Blocks.ACACIA_LOG, Blocks.DARK_OAK_LOG, Blocks.JUNGLE_LOG, Blocks.MANGROVE_LOG, Blocks.SPRUCE_LOG,
                            Blocks.STRIPPED_OAK_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.STRIPPED_SPRUCE_LOG,
                            Blocks.OAK_WOOD, Blocks.BIRCH_WOOD, Blocks.ACACIA_WOOD, Blocks.DARK_OAK_WOOD, Blocks.JUNGLE_WOOD, Blocks.MANGROVE_WOOD, Blocks.SPRUCE_WOOD,
                            Blocks.STRIPPED_OAK_WOOD, Blocks.STRIPPED_BIRCH_WOOD, Blocks.STRIPPED_ACACIA_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD),
                    List.of(Items.OAK_LOG, Items.BIRCH_LOG, Items.ACACIA_LOG, Items.DARK_OAK_LOG, Items.JUNGLE_LOG, Items.MANGROVE_LOG, Items.SPRUCE_LOG,
                            Items.STRIPPED_OAK_LOG, Items.STRIPPED_BIRCH_LOG, Items.STRIPPED_ACACIA_LOG, Items.STRIPPED_DARK_OAK_LOG, Items.STRIPPED_JUNGLE_LOG, Items.STRIPPED_MANGROVE_LOG, Items.STRIPPED_SPRUCE_LOG,
                            Items.OAK_WOOD, Items.BIRCH_WOOD, Items.ACACIA_WOOD, Items.DARK_OAK_WOOD, Items.JUNGLE_WOOD, Items.MANGROVE_WOOD, Items.SPRUCE_WOOD,
                            Items.STRIPPED_OAK_WOOD, Items.STRIPPED_BIRCH_WOOD, Items.STRIPPED_ACACIA_WOOD, Items.STRIPPED_DARK_OAK_WOOD, Items.STRIPPED_JUNGLE_WOOD, Items.STRIPPED_MANGROVE_WOOD, Items.STRIPPED_SPRUCE_WOOD),
                    TICKS_PER_SECOND * 10,
                    10,
                    ResourceName.WOOD
            ),
            new ResourceSource("Leaves",
                    List.of(Blocks.ACACIA_LEAVES, Blocks.AZALEA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.MANGROVE_LEAVES, Blocks.OAK_LEAVES, Blocks.SPRUCE_LEAVES),
                    List.of(Items.ACACIA_LEAVES, Items.AZALEA_LEAVES, Items.BIRCH_LEAVES, Items.FLOWERING_AZALEA_LEAVES, Items.JUNGLE_LEAVES, Items.DARK_OAK_LEAVES, Items.MANGROVE_LEAVES, Items.OAK_LEAVES, Items.SPRUCE_LEAVES),
                    5,
                    1,
                    ResourceName.WOOD
            )
    );

    public static final List<ResourceSource> ORE_BLOCKS = List.of(
            new ResourceSource("Stone", // item only
                    List.of(),
                    List.of(Items.STONE),
                    0,
                    1,
                    ResourceName.ORE
            ),
            new ResourceSource("Tier 1 Ores",
                    List.of(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE, Blocks.NETHER_QUARTZ_ORE),
                    List.of(Items.COAL, Items.QUARTZ),
                    TICKS_PER_SECOND * 12,
                    25,
                    ResourceName.ORE
            ),
            new ResourceSource("Tier 2 Ores",
                    List.of(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_IRON_ORE, Blocks.DEEPSLATE_LAPIS_ORE, Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.NETHER_GOLD_ORE),
                    List.of(Items.RAW_IRON, Items.RAW_COPPER, Items.LAPIS_LAZULI, Items.REDSTONE),
                    TICKS_PER_SECOND * 12,
                    50,
                    ResourceName.ORE
            ),
            new ResourceSource("Tier 3 Ores",
                    List.of(Blocks.GOLD_ORE, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.DEEPSLATE_EMERALD_ORE),
                    List.of(Items.RAW_GOLD, Items.EMERALD),
                    TICKS_PER_SECOND * 12,
                    100,
                    ResourceName.ORE
            ),
            new ResourceSource("Tier 4 Ores",
                    List.of(Blocks.DIAMOND_ORE, Blocks.ANCIENT_DEBRIS, Blocks.DEEPSLATE_DIAMOND_ORE),
                    List.of(Items.DIAMOND, Items.ANCIENT_DEBRIS),
                    TICKS_PER_SECOND * 12,
                    250,
                    ResourceName.ORE
            )
    );
}