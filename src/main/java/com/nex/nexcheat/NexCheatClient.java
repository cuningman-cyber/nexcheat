package com.nex.nexcheat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NexCheatClient implements ClientModInitializer {
    private static KeyBinding menuKey;
    private static final Pattern AMMO = Pattern.compile("(\\d+)\\s*/\\s*(\\d+)");

    private int reloadCooldown = 0;
    private int previousSlot = -1;
    private boolean healing = false;
    private int useCooldown = 0;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.nexcheat.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "key.categories.nexcheat"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new NexScreen());
            }
            if (client.player == null || client.interactionManager == null) return;
            if (client.currentScreen != null) return;
            tickAutoReload(client);
            tickAutoApta(client);
        });
    }

    /** AutoReload: trzymasz slot 1 i amunicja <= ustawionej -> "Q" (przeładowanie na serwerze). */
    private void tickAutoReload(MinecraftClient mc) {
        if (reloadCooldown > 0) reloadCooldown--;
        if (!NexConfig.autoReload || reloadCooldown > 0) return;
        ClientPlayerEntity p = mc.player;
        if (p.getInventory().selectedSlot != NexConfig.reloadSlot - 1) return;
        var stack = p.getMainHandStack();
        if (stack.isEmpty()) return;

        // Amunicja: z nazwy przedmiotu w formacie "12/30"; jeśli brak - z liczby przedmiotów w stacku.
        int ammo = stack.getCount();
        Matcher m = AMMO.matcher(stack.getName().getString());
        if (m.find()) ammo = Integer.parseInt(m.group(1));

        if (ammo <= NexConfig.reloadAtAmmo) {
            p.dropSelectedItem(false); // to samo co klawisz Q
            reloadCooldown = 40;
        }
    }

    /** AutoApta: HP <= próg -> slot apteczki + prawy klik; po regeneracji wraca na poprzedni slot. */
    private void tickAutoApta(MinecraftClient mc) {
        if (!NexConfig.autoApta) { healing = false; return; }
        ClientPlayerEntity p = mc.player;
        float hp = p.getHealth();

        if (!healing) {
            if (hp <= NexConfig.aptaHp && hp > 0) {
                previousSlot = p.getInventory().selectedSlot;
                p.getInventory().selectedSlot = NexConfig.aptaSlot - 1;
                healing = true;
                useCooldown = 3;
            }
            return;
        }

        if (hp >= NexConfig.aptaStopHp || hp <= 0) {
            if (previousSlot >= 0) p.getInventory().selectedSlot = previousSlot;
            healing = false;
            return;
        }
        if (useCooldown > 0) { useCooldown--; return; }
        mc.interactionManager.interactItem(p, Hand.MAIN_HAND);
        useCooldown = 10;
    }
}
