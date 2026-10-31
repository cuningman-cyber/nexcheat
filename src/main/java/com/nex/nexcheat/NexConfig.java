package com.nex.nexcheat;

public class NexConfig {
    public static boolean autoReload = false;
    public static int reloadAtAmmo = 5;      // przeładuj gdy amunicja <= tyle
    public static int reloadSlot = 1;        // numer slotu na pasku (1-9)

    public static boolean knp = false;       // ukryj królestwo i nick (sidebar scoreboard)

    public static boolean autoApta = false;
    public static int aptaHp = 15;           // włącz gdy HP <= tyle (HP w połówkach serc: 20 = pełne)
    public static int aptaStopHp = 20;       // wróć na stary slot gdy HP >= tyle
    public static int aptaSlot = 2;          // slot z apteczką (1-9)
}
