package me.boxadactle.coordinatesdisplay.util;

import me.boxadactle.coordinatesdisplay.CoordinatesDisplay;

public class BiomeColors {

    public static int getBiomeColor(String name, int defaultColor) {

        int returning = 0;

        switch (name) {

            case "Eroded Badlands":
            case "Badlands":
                returning = 0xb55a26;
                break;

            case "Bamboo Jungle":
                returning = 0x2be625;
                break;

            case "Snowy Beach":
            case "Snowy Plains":
            case "Snowy Slopes":
            case "Snowy Taiga":
            case "Basalt Deltas":
                returning = 0xadadad;
                break;

            case "Beach":
                returning = 0xc5c93a;
                break;

            case "Birch Forest":
            case "Old Growth Birch Forest":
                returning = 0xdecc7a;
                break;

            case "Cold Ocean":
                returning = 0x738ae6;
                break;

            case "Crimson Forest":
            case "Nether Wastes":
                returning = 0xad201d;
                break;

            case "Dark Forest":
                returning = 0x452309;
                break;

            case "River":
            case "Ocean":
            case "Deep Cold Ocean":
                returning = 0x161d78;
                break;

            case "Deep Dark":
                returning = 0x03273d;
                break;

            case "Deep Frozen Ocean":
                returning = 0x1e4054;
                break;

            case "Deep Lukewarm Ocean":
                returning = 0x235b63;
                break;

            case "Deep Ocean":
                returning = 0x15115c;
                break;

            case "End Barrens":
            case "End Highlands":
            case "End Midlands":
            case "Small End Islands":
            case "Desert":
                returning = 0xb3ac30;
                break;

            case "Dripstone Caves":
                returning = 0x665f50;
                break;

            case "Flower Forest":
            case "Forest":
            case "Lush Caves":
            case "Meadow":
                returning = 0x32701c;
                break;

            case "Frozen Ocean":
            case "Frozen Peaks":
            case "Frozen River":
            case "Ice Spikes":
                returning = 0x34c4c9;
                break;

            case "Grove":
            case "Jagged Peaks":
                returning = 0xacb0a7;
                break;

            case "Jungle":
                returning = 0x85c41f;
                break;

            case "Lukewarm Ocean":
                returning = 0x3d9ba8;
                break;

            case "Mushroom Fields":
                returning = 0x4c4654;
                break;

            case "Old Growth Pine Taiga":
            case "Old Growth Spruce Forest":
                returning = 0x3b230d;
                break;

            case "Plains":
                returning = 0x4dd115;
                break;

            case "Savanna":
            case "Savanna Plateau":
                returning = 0x5c701c;
                break;
            default:
                returning = defaultColor;
                break;
        }

        return returning;

    }

}