package com.me.quadel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.google.gson.*;

import java.util.*;
/*
 * Author: Aaron Nech
 * Project: BotBots
 * Description: This object represents map data, and has the ability to load from JSON format given
 *  by the browser editor
 */

public class MapData {
	private Integer[][] data;
	private Bot[] red;
	private Bot[] green;
	private Bot[] blue;
	private Hole[] holes;
	private Bit[] bits;

	//our arbitrary constructor
	public MapData(Integer[][] data, Bot[] red, Bot[] green, Bot[] blue,
			Hole[] holes, Bit[] bits) {
		this.data = data;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.holes = holes;
		this.bits = bits;
	}
	
	//Object getters
	public Integer[][] getData() {
		return data;
	}
	public Bot[] getRed() {
		return red;
	}
	public Bot[] getGreen() {
		return green;
	}
	public Bot[] getBlue() {
		return blue;
	}
	public Hole[] getHoles() {
		return holes;
	}
	public Bit[] getNuts() {
		return bits;
	}
	
	//Constructs a object loader from a file named section_level.json statically
	//The file must exist
    public static MapData fromFile(String fileName) {

        FileHandle file = Gdx.files.internal("data/maps/" + fileName);
        String content = file.readString();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();

        //load the tiles
        JsonElement root =  parser.parse(content);
        Integer[][] tiles = gson.fromJson(root.getAsJsonObject().get("tileData"), Integer[][].class);

        //load the objects
        JsonObject[] objs = gson.fromJson(root.getAsJsonObject().get("objectData"), JsonObject[].class);
        List<Bot> _red = new ArrayList<Bot>();
        List<Bot> _green = new ArrayList<Bot>();
        List<Bot> _blue = new ArrayList<Bot>();
        List<Hole> _holes = new ArrayList<Hole>();
        List<Bit> _nuts = new ArrayList<Bit>();
        
        //create the objects from the json
        for(JsonObject obj : objs) {
            JsonObject attributes = obj.get("attributes").getAsJsonObject();
            String name = attributes.get("type").getAsString();
            if(name.equals("Bot")) {
                String color = attributes.get("color").getAsString();
                if(color.equals("red")) {
                    _red.add(new Bot(Color.RED, obj.get("x").getAsInt(), obj.get("y").getAsInt()));
                } else if(color.equals("blue")) {
                    _blue.add(new Bot(Color.BLUE, obj.get("x").getAsInt(), obj.get("y").getAsInt()));
                } else if(color.equals("green")) {
                    _green.add(new Bot(Color.GREEN, obj.get("x").getAsInt(), obj.get("y").getAsInt()));
                }
            } else if(name.equals("Hole")) {
                List<Color> pattern = new ArrayList<Color>();
                String[] jsonPattern = gson.fromJson(attributes.getAsJsonArray("order"), String[].class);
                for(String color : jsonPattern) {
                    if(color.equals("red")) {
                        pattern.add(Color.RED);
                    } else if(color.equals("blue")) {
                        pattern.add(Color.BLUE);
                    } else if(color.equals("green")) {
                        pattern.add(Color.GREEN);
                    }
                }
                _holes.add(new Hole(pattern.toArray(new Color[pattern.size()]),
                        obj.get("x").getAsInt(), obj.get("y").getAsInt()));
            } else if(name.equals("Bit")) {
                String color = attributes.get("color").getAsString();
                if(color.equals("red")) {
                    _nuts.add(new Bit(Color.RED, obj.get("x").getAsInt(), obj.get("y").getAsInt()));
                } else if(color.equals("blue")) {
                    _nuts.add(new Bit(Color.BLUE, obj.get("x").getAsInt(), obj.get("y").getAsInt()));
                } else if(color.equals("green")) {
                    _nuts.add(new Bit(Color.GREEN, obj.get("x").getAsInt(), obj.get("y").getAsInt()));
                }
            }
        }

        //return a new instance based on the json
        return new MapData(tiles, _red.toArray(new Bot[_red.size()]), _green.toArray(new Bot[_green.size()]),
                _blue.toArray(new Bot[_blue.size()]), _holes.toArray(new Hole[_holes.size()]),
                _nuts.toArray(new Bit[_nuts.size()]));
    }
}
