package com.me.quadel;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ScoreDatabase {
	private ScoreData[] results;
	private String fileName;

	public ScoreDatabase(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        this.fileName = fileName;
        String content = file.readString();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonObject root =  parser.parse(content).getAsJsonObject();
        
        results = gson.fromJson(root.get("data"), ScoreData[].class);
	}
	public void toFile() {
        FileHandle file = Gdx.files.internal(fileName);
        Gson gson = new Gson();
        JsonObject root = new JsonObject();
        JsonParser parser = new JsonParser();
        root.add("data", parser.parse(gson.toJson(results, ScoreData[].class)));
        file.writeString(root.toString(), false);
	}
	
	public Map<String, ScoreData> getMap() {
		Map<String, ScoreData> dataMap = new HashMap<String, ScoreData>();
        for(ScoreData result : results) {
        	dataMap.put(result.mapFile, result);
        }
        return dataMap;
	}
	
	public void setData(String mapName, ScoreData result) {
		int toSet = -1;
		for(int i = 0; i < results.length; i++) {
			if(result.mapFile.equals(mapName))
				toSet = i;
		}
		if(toSet != -1)
			results[toSet] = result;
		else
			throw new IllegalStateException("Can't find map " + mapName + " in setData");
	}
	
}
