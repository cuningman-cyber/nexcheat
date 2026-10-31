package com.nex.nexcheat;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class NexScreen extends Screen {
    public NexScreen() { super(Text.literal("NEX CHEAT 1.0.0")); }

    @Override
    protected void init() {
        int cx = width / 2, y = height / 2 - 100, w = 150;
        y = toggle(cx, y, w, "AutoReload", () -> NexConfig.autoReload, v -> NexConfig.autoReload = v);
        y = number(cx, y, "Przeładuj od", () -> NexConfig.reloadAtAmmo, v -> NexConfig.reloadAtAmmo = v, 0, 64);
        y = toggle(cx, y, w, "KNP", () -> NexConfig.knp, v -> NexConfig.knp = v);
        y = toggle(cx, y, w, "AutoApta", () -> NexConfig.autoApta, v -> NexConfig.autoApta = v);
        y = number(cx, y, "HP start", () -> NexConfig.aptaHp, v -> NexConfig.aptaHp = v, 1, 19);
        y = number(cx, y, "Slot apteczki", () -> NexConfig.aptaSlot, v -> NexConfig.aptaSlot = v, 1, 9);
        addDrawableChild(ButtonWidget.builder(Text.literal("Zamknij"), b -> close())
                .dimensions(cx - 50, y + 10, 100, 20).build());
    }

    private int toggle(int cx, int y, int w, String name, BooleanSupplier get, Consumer<Boolean> set) {
        addDrawableChild(ButtonWidget.builder(label(name, get.getAsBoolean()), b -> {
            set.accept(!get.getAsBoolean());
            b.setMessage(label(name, get.getAsBoolean()));
        }).dimensions(cx - w / 2, y, w, 20).build());
        return y + 24;
    }

    private int number(int cx, int y, String name, IntSupplier get, IntConsumer set, int min, int max) {
        ButtonWidget[] val = new ButtonWidget[1];
        addDrawableChild(ButtonWidget.builder(Text.literal("-"), b -> {
            set.accept(MathHelper.clamp(get.getAsInt() - 1, min, max));
            val[0].setMessage(Text.literal(name + ": " + get.getAsInt()));
        }).dimensions(cx - 75, y, 20, 20).build());
        val[0] = addDrawableChild(ButtonWidget.builder(Text.literal(name + ": " + get.getAsInt()), b -> {})
                .dimensions(cx - 53, y, 106, 20).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("+"), b -> {
            set.accept(MathHelper.clamp(get.getAsInt() + 1, min, max));
            val[0].setMessage(Text.literal(name + ": " + get.getAsInt()));
        }).dimensions(cx + 55, y, 20, 20).build());
        return y + 24;
    }

    private Text label(String n, boolean on) {
        return Text.literal(n + ": " + (on ? "§aON" : "§cOFF"));
    }

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        super.render(ctx, mx, my, delta);
        ctx.drawCenteredTextWithShadow(textRenderer, Text.literal("§b§lNEX CHEAT 1.0.0"), width / 2, height / 2 - 120, 0xFFFFFF);
    }
}
