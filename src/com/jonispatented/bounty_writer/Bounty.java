package com.jonispatented.bounty_writer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public record Bounty(String itemID, long itemValue, Position chestPosition) {

    public void generateBountyFile(int bountyNumber) throws IOException {
        BufferedWriter writer = new BufferedWriter(new PrintWriter("bounty_output/bounty_"
                + bountyNumber + ".mcfunction"));
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

    public static void generateBucketCheck(List<Bounty> bounties) throws IOException {
        BufferedWriter writer = new BufferedWriter(new PrintWriter("bounty_output/bucket_check.mcfunction"));

        for (int i = 0; i < bounties.size(); i++)
            writer.write("execute if data block -396 76 -65 {Items:[{id:\"" +
                    bounties.get(i).itemID() + "\"}]} run function communism:bounty_" + (i + 1) + '\n');

        writer.write("\ndata remove block -396 76 -65 Items");
        writer.close();
    }

    public static void generateLedgerLegis(List<Bounty> bounties) throws IOException {
        BufferedWriter writer = new BufferedWriter(new PrintWriter("bounty_output/give_ledger_legis.mcfunction"));

        writer.write("give @s written_book{pages:['{\"text\":\"");

        for (int i = 0; i < bounties.size(); i++) {
            String bountyName = bounties.get(i).itemID();
            bountyName = bountyName.substring(bountyName.indexOf(':') + 1);
            bountyName = bountyName.replaceAll("_", " ");
            bountyName = bountyName.toUpperCase();
            writer.write(bountyName + ": " + bounties.get(i).itemValue() + "\\\\n");

            if ((i + 1) % 7 == 0 && i < bounties.size() - 1)
                    writer.write("\"}','{\"text\":\"");
        }

        writer.write("\"}'],title:\"Ledger Legis\"," +
                "author:\"The Overseer\",generation:3," +
                "display:{Lore:['{\"text\":\"" +
                "This book contains the Council Gospel from the Overseer\"}','{\"text\":\"" +
                "regarding the point rewards for your generous donations.\"}']}}");

        writer.close();
    }

}
