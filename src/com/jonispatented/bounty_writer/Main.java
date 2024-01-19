package com.jonispatented.bounty_writer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        try {
            List<Bounty> bounties = generateBountiesFromJSON("bounty_info/bounties.json");

            for (int i = 0; i < bounties.size(); i++) {
                bounties.get(i).generateBountyFile(i + 1);
            }

            Bounty.generateBucketCheck(bounties);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private static List<Bounty> generateBountiesFromJSON(String filePath) throws IOException, ParseException {

        List<Bounty> bounties = new ArrayList<>();

        JSONArray bountyList = (JSONArray) ((JSONObject) new JSONParser()
                .parse(new FileReader(filePath))).get("bounties");

        for (Object bountyEntry : bountyList) {
            JSONObject bountyEntryJson = (JSONObject) bountyEntry;
            JSONObject chestPositionJson = (JSONObject) bountyEntryJson.get("chest_position");
            Position chestPosition = new Position(
                    (long) chestPositionJson.get("x"),
                    (long) chestPositionJson.get("y"),
                    (long) chestPositionJson.get("z"));
            JSONArray bountiesInEntry = (JSONArray) bountyEntryJson.get("bounties");
            for (Object item : bountiesInEntry) {
                JSONObject itemJson = (JSONObject) item;
                bounties.add(new Bounty((String) itemJson.get("item"), (long) itemJson.get("value"), chestPosition));
            }
        }

        return bounties;

    }

}
