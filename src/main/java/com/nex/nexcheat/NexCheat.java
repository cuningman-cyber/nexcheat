package com.nex.nexcheat;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class NexCheat implements ModInitializer {
    public static Item ANTY_WAZON;

    @Override
    public void onInitialize() {
        // AntyWazon: wygląda jak dekoracyjny wazon (doniczka), nie ma żadnego ekwipunku ani przechowywania.
        RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("nexcheat", "antywazon"));
        ANTY_WAZON = Registry.register(Registries.ITEM, key,
                new Item(new Item.Settings().registryKey(key).maxCount(16)));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(e -> e.add(ANTY_WAZON));
    }
}
