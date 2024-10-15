package io.bluestaggo.divergeprog.item;

import io.bluestaggo.divergeprog.DivergentProgression;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {
    // Bars
    public static final Item FLINT_BAR = register(new Item(new Item.Settings()), "flint_bar");
    public static final Item COPPER_BAR = register(new Item(new Item.Settings()), "copper_bar");
    public static final Item IRON_BAR = register(new Item(new Item.Settings()), "iron_bar");
    public static final Item GOLD_BAR = register(new Item(new Item.Settings()), "gold_bar");
    public static final Item DIAMOND_BAR = register(new Item(new Item.Settings()), "diamond_bar");
    public static final Item RAW_COPPER_BAR = register(new Item(new Item.Settings()), "raw_copper_bar");
    public static final Item RAW_IRON_BAR = register(new Item(new Item.Settings()), "raw_iron_bar");
    public static final Item RAW_GOLD_BAR = register(new Item(new Item.Settings()), "raw_gold_bar");

    // Flint Tools
    public static final Item FLINT_SWORD = register(
        new SwordItem(
            ModToolMaterials.FLINT,
            3, -2.4f,
            new FabricItemSettings()
        ),
        "flint_sword"
    );
    public static final Item FLINT_SHOVEL = register(
        new ShovelItem(
            ModToolMaterials.FLINT,
            1.5f, -3.0f,
            new FabricItemSettings()),
        "flint_shovel"
    );
    public static final Item FLINT_PICKAXE = register(
        new PickaxeItem(
            ModToolMaterials.FLINT,
            1, -2.8f,
            new FabricItemSettings()),
        "flint_pickaxe"
    );
    public static final Item FLINT_AXE = register(
        new AxeItem(
            ModToolMaterials.FLINT,
            6.0f, -3.2f,
            new FabricItemSettings()
        ), "flint_axe"
    );
    public static final Item FLINT_HOE = register(
        new HoeItem(
            ModToolMaterials.FLINT,
            1, -3.0f,
            new FabricItemSettings()),
        "flint_hoe"
    );

    // Copper Tools
    public static final Item COPPER_SWORD = register(
        new SwordItem(
            ModToolMaterials.COPPER,
        3, -2.4f, new FabricItemSettings()
        ), "copper_sword");
    public static final Item COPPER_SHOVEL = register(
        new ShovelItem(
            ModToolMaterials.COPPER,
            1.5f, -3.0f, new FabricItemSettings()),
        "copper_shovel"
    );
    public static final Item COPPER_PICKAXE = register(
        new PickaxeItem(ModToolMaterials.COPPER,
            1, -2.8f, new FabricItemSettings()),
        "copper_pickaxe"
    );
    public static final Item COPPER_AXE = register(
        new AxeItem(ModToolMaterials.COPPER,
            7.0f, -3.2f, new FabricItemSettings()
        ), "copper_axe"
    );
    public static final Item COPPER_HOE = register(
        new HoeItem(ModToolMaterials.COPPER,
            1, -3.0f, new FabricItemSettings()
        ), "copper_hoe"
    );

    private static Item register(Item item, String name) {
        return Registry.register(Registries.ITEM, DivergentProgression.id(name), item);
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register(group -> {
                    group.addAfter(Items.FLINT, FLINT_BAR);
                    group.addAfter(Items.COPPER_INGOT, COPPER_BAR);
                    group.addAfter(Items.IRON_INGOT, IRON_BAR);
                    group.addAfter(Items.GOLD_INGOT, GOLD_BAR);
                    group.addAfter(Items.DIAMOND, DIAMOND_BAR);
                    group.addAfter(Items.RAW_COPPER, RAW_COPPER_BAR);
                    group.addAfter(Items.RAW_IRON, RAW_IRON_BAR);
                    group.addAfter(Items.RAW_GOLD, RAW_GOLD_BAR);
                });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(group -> {
                    group.addAfter(Items.WOODEN_SWORD, FLINT_SWORD);
                    group.addAfter(Items.WOODEN_AXE, FLINT_AXE);
                    group.addAfter(Items.STONE_SWORD, COPPER_SWORD);
                    group.addAfter(Items.STONE_AXE, COPPER_AXE);
                });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register(group -> {
                    group.addAfter(Items.WOODEN_HOE,
                            FLINT_SHOVEL,
                            FLINT_PICKAXE,
                            FLINT_AXE,
                            FLINT_HOE);
                    group.addAfter(Items.STONE_HOE,
                            COPPER_SHOVEL,
                            COPPER_PICKAXE,
                            COPPER_AXE,
                            COPPER_HOE);
                });
    }
}
