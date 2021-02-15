package top.kealine.zuccoj.structure.scoreboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

import static top.kealine.zuccoj.constant.ContestType.CF;
import static top.kealine.zuccoj.constant.ContestType.ICPC;
import static top.kealine.zuccoj.constant.ContestType.IOI;
import static top.kealine.zuccoj.constant.ContestType.OI;

public class ScoreboardJsonify {
    public static String jsonify(Scoreboard scoreboard, ObjectMapper mapper) {
        return jsonify(scoreboard, mapper, false);
    }

    public static String jsonify(Scoreboard scoreboard, ObjectMapper mapper, boolean format) {
        if (!scoreboard.isDone()) {
            scoreboard.buildAndSort();
        }

        ObjectNode root = mapper.createObjectNode();
        root.put("updateTime", scoreboard.getUpdateTime());
        root.put("problemCount", scoreboard.getProblemCount());
        root.put("contestType", scoreboard.getContestType());
        ArrayNode scoreboardArray = root.putArray("scoreboard");


        List<Scoreboard.ScoreboardEach> list = scoreboard.getScoreboardList();
        for (Scoreboard.ScoreboardEach each: list) {
            ObjectNode eachJson = mapper.createObjectNode();
            eachJson.put("rank", each.getRank());
            eachJson.put("username", each.getUsername());
            eachJson.put("nickname", each.getNickname());

            switch (scoreboard.getContestType()) {
                case ICPC: complementICPCBoardItem(each, eachJson, mapper); break;
                case OI:  complementOIBoardItem(each, eachJson, mapper); break;
                case IOI: complementIOIBoardItem(each, eachJson, mapper); break;
                case CF: complementCFBoardItem(each, eachJson, mapper); break;
                default: eachJson.put("point", each.getPoint()); break;
            }

            scoreboardArray.add(eachJson);
        }

        return format ? root.toPrettyString() : root.toString();
    }

    private static void complementICPCBoardItem(Scoreboard.ScoreboardEach eachData, ObjectNode eachJson, ObjectMapper mapper) {
        eachJson.put("point", eachData.getPoint());
        eachJson.put("penalty", eachData.getPenalty());
        ArrayNode eachArray = eachJson.putArray("problems");
        List<Scoreboard.ScoreboardCell> cells = eachData.getCells();
        for (Scoreboard.ScoreboardCell cell: cells) {
            ObjectNode cellJson = mapper.createObjectNode();
            cellJson.put("wrongTryCount", cell.getWrongTryCount());
            cellJson.put("pendingTryCont", cell.getPendingTryCont());
            cellJson.put("firstBlood", cell.isFirstBlood());
            if (cell.getRightTime() != null) {
                cellJson.put("rightTime", cell.getRightTime());
                cellJson.put("rightTimeInSecond", cell.getRightTimeInSecond());
            }
            eachArray.add(cellJson);
        }
    }

    private static void complementOIBoardItem(Scoreboard.ScoreboardEach eachData, ObjectNode eachJson, ObjectMapper mapper) {
        eachJson.put("score", eachData.getScore());
        ArrayNode eachArray = eachJson.putArray("problems");
        List<Scoreboard.ScoreboardCell> cells = eachData.getCells();
        for (Scoreboard.ScoreboardCell cell: cells) {
            ObjectNode cellJson = mapper.createObjectNode();
            cellJson.put("score", cell.getScore());
            cellJson.put("firstBlood", cell.isFirstBlood());
            eachArray.add(cellJson);
        }
    }

    private static void complementIOIBoardItem(Scoreboard.ScoreboardEach eachData, ObjectNode eachJson, ObjectMapper mapper) {
        complementOIBoardItem(eachData, eachJson, mapper);
    }

    private static void complementCFBoardItem(Scoreboard.ScoreboardEach eachData, ObjectNode eachJson, ObjectMapper mapper) {
    }
}
