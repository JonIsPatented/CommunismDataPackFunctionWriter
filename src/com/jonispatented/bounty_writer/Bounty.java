package com.jonispatented.bounty_writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;

public record Bounty(String itemID, long itemValue, Position chestPosition) {

    public void generateBountyFile(int bountyNumber) throws IOException {
        BufferedWriter writer = new BufferedWriter(new PrintWriter("bounty_output/bounty_" + bountyNumber));
        writer.write("execute as @e[tag=Overseer,limit=1] store result score @s " +
                        "item_count run data get block -396 76 -65 " +
                        "Items[{id:\"" + itemID + "\"}].Count\n" +

                        "scoreboard players set @e[tag=Overseer] multiplier " + itemValue + "\n" +
                        "\n" +

                        "scoreboard players operation @e[tag=Overseer] item_count " +
                        "*= @e[tag=Overseer,limit=1] multiplier\n" +

                        "execute positioned -395.5 75.00 -60.5 run scoreboard players operation " +
                        "@a[distance=0..2,limit=1] generosity += @e[tag=Overseer] item_count\n" +

                        "execute positioned -395.5 75.00 -60.5 run scoreboard players operation " +
                        "@a[distance=0..2,limit=1] communist-points += @e[tag=Overseer] item_count\n" +

                        "scoreboard players set @e[tag=Overseer] item_count 0\n" +
                        "\n" +

                        "loot insert " + chestPosition.x() + ' ' + chestPosition.y() + ' ' + chestPosition.z() +
                        " mine -396 76 -65 minecraft:air{drop_contents:1b}\n" +
                        "data remove block -396 76 -65 Items[{id:\"" + itemID + "\"}]");
        writer.close();
    }

}