package com.orca.oneblocksurvival;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class OneBlockSurvival implements ModInitializer {
    public static final String MOD_ID = "one-block-survival";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static final BlockPos ONE_BLOCK_POS = new BlockPos(0, 64, 0);
    private static final Random RANDOM = new Random();

    private static final Map<UUID, Integer> blocksBreakCount = new HashMap<>();

    private static final Block[] COMMON_BLOCKS = {
        Blocks.DIRT, Blocks.COBBLESTONE, Blocks.OAK_LOG, Blocks.SAND,
        Blocks.GRAVEL, Blocks.CLAY, Blocks.OAK_PLANKS, Blocks.STONE
    };

    private static final Block[] UNCOMMON_BLOCKS = {
        Blocks.IRON_ORE, Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.GOLD_ORE,
        Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE, Blocks.PUMPKIN, Blocks.MELON,
        Blocks.MOSSY_COBBLESTONE, Blocks.BRICKS
    };

    private static final Block[] RARE_BLOCKS = {
        Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.OBSIDIAN,
        Blocks.GLOWSTONE, Blocks.BOOKSHELF, Blocks.CHEST
    };

    private static final Block[] SPECIAL_BLOCKS = {
        Blocks.SPAWNER, Blocks.LAVA, Blocks.WATER, Blocks.TNT
    };

    private static final Item[] BONUS_ITEMS = {
        Items.APPLE, Items.BREAD, Items.COOKED_BEEF, Items.IRON_INGOT,
        Items.GOLD_INGOT, Items.DIAMOND, Items.EMERALD, Items.BONE,
        Items.STRING, Items.LEATHER, Items.WHEAT_SEEDS, Items.CARROT,
        Items.POTATO, Items.EGG, Items.FEATHER, Items.COAL
    };

    @Override
    public void onInitialize() {
        LOGGER.info("One Block Survival initialized!");

        PlayerBlockBreakEvents.AFTER.register(this::onBlockBreak);
    }

    private void onBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        if (world.isClient()) return;

        if (pos.equals(ONE_BLOCK_POS)) {
            ServerWorld serverWorld = (ServerWorld) world;
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

            int count = blocksBreakCount.getOrDefault(player.getUuid(), 0) + 1;
            blocksBreakCount.put(player.getUuid(), count);

            Block nextBlock = getRandomBlock(count);

            serverWorld.getServer().execute(() -> {
                serverWorld.setBlockState(ONE_BLOCK_POS, nextBlock.getDefaultState());
            });

            if (RANDOM.nextInt(100) < 30) {
                Item bonusItem = BONUS_ITEMS[RANDOM.nextInt(BONUS_ITEMS.length)];
                int amount = 1 + RANDOM.nextInt(3);
                player.giveItemStack(new ItemStack(bonusItem, amount));
                serverPlayer.sendMessage(Text.literal("§aBonus! +" + amount + " " + getItemName(bonusItem)), true);
            } else {
                serverPlayer.sendMessage(Text.literal("§eBlocks broken: §6" + count), true);
            }

            LOGGER.info("Player {} broke the one block (count: {}), next block: {}",
                player.getName().getString(), count, Registries.BLOCK.getId(nextBlock));
        }
    }

    private Block getRandomBlock(int count) {
        int roll = RANDOM.nextInt(100);

        int rareChance = Math.min(5 + (count / 20), 15);
        int uncommonChance = Math.min(20 + (count / 10), 40);
        int specialChance = Math.min(2 + (count / 50), 8);

        if (roll < specialChance) {
            return SPECIAL_BLOCKS[RANDOM.nextInt(SPECIAL_BLOCKS.length)];
        } else if (roll < specialChance + rareChance) {
            return RARE_BLOCKS[RANDOM.nextInt(RARE_BLOCKS.length)];
        } else if (roll < specialChance + rareChance + uncommonChance) {
            return UNCOMMON_BLOCKS[RANDOM.nextInt(UNCOMMON_BLOCKS.length)];
        } else {
            return COMMON_BLOCKS[RANDOM.nextInt(COMMON_BLOCKS.length)];
        }
    }

    private String getItemName(Item item) {
        String id = Registries.ITEM.getId(item).getPath();
        return id.replace("_", " ");
    }
}
