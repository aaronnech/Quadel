package com.me.quadel;
/*
 * Author: Aaron Nech
 * Project: SquirrelBots
 * Description: This class acts as an abstract implementation of a Menu State
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import simpleui.SimpleButton;
import simpleui.SimpleEvent;
import simpleui.SimpleEventType;
import simpleui.SimpleLabel;
import simpleui.SimpleRadio;
import simpleui.SimpleRadioGroup;
import simpleui.SimpleTreeItem;
import simpleui.SimpleTreeView;
import simpleui.SimpleUIComponent;
import simpleui.SimpleUIListener;
import simpleui.SimpleUIManager;
import gameabstract.GameInput;
import gameabstract.GameState;
import gameabstract.InputEvent;
import gameabstract.MenuState;
import gameabstract.Renderer;

public class MainMenuState extends MenuState implements SimpleUIListener {
	private static final int GROUPS_PER_PAGE = 2;
	private MainMenuScreen screen;
	private List<SimpleTreeView> levelGroups;
	private SimpleRadioGroup selection;
	private SimpleTreeView activeGroup;
	private ScoreDatabase scores;
	private int activeIndex;
	
	//labels
	private SimpleLabel bestScore;
	private SimpleLabel bestTime;
	
	public MainMenuState(GameInput i, MainMenuScreen screen) {
		super(i);
		this.screen = screen;
	}
	
	protected void setupUI() {
		Map<String, List<String>> levelNames = loadFileNames();
		scores = new ScoreDatabase(Quadel.SCORES_FILE);
		selection = new SimpleRadioGroup();
		levelGroups = makeLevelGroups(levelNames, 500, 0);
		activeIndex = 0;
		activeGroup = levelGroups.get(0);
		
		bestScore = new SimpleLabel("best_score", "", MenuRenderer.WIDTH / 2 - 200, MenuRenderer.HEIGHT - 160);
		bestTime = new SimpleLabel("best_score", "", MenuRenderer.WIDTH / 2 - 200, MenuRenderer.HEIGHT - 200);
		
		SimpleButton play = new SimpleButton("play", MenuRenderer.WIDTH / 2 - 200, MenuRenderer.HEIGHT - 100, 400, 50);
		play.setText("Play!");
		ui.addComponent(play,
				this, SimpleEventType.TOUCH, "play");
		ui.addComponent(activeGroup, this, SimpleEventType.TOUCH, "levelSelect");
		ui.addComponent(bestScore);
		ui.addComponent(bestTime);
	}
	
	public void input(InputEvent e) {
		if(e.getType().equals("touchDown")) {
			int tx = ((int[]) e.getData())[0];
			int ty = ((int[]) e.getData())[1];
			System.out.println(tx + ", " + ty);
		}
	}
	
	public void levelSelect(SimpleEvent e) {
		if(e.getMessage() != null) { // we touched a level
			if(e.getMessage().getClass() != SimpleLabel.class) {
				SimpleRadio level = (SimpleRadio) e.getMessage();
				ScoreData data = scores.getMap().get(level.getName());
				if(data != null) {
					bestScore.setTextValue("Best Score: " + data.bestScore + " | Attempts: " + data.attempts);
				} else {
					bestScore.setTextValue("Not finished yet!");
				}
			}
		}
	}
	
	public void play(SimpleEvent e) {
		screen.toPlay(selection.getSelected().getName());
	}
	
	private Map<String, List<String>> loadFileNames() {
		Map<String, List<String>> result = new TreeMap<String, List<String>>();
		FileHandle dirHandle;
		if (Gdx.app.getType() == ApplicationType.Android) {
			dirHandle = Gdx.files.internal("data/maps");
		} else {
			// ApplicationType.Desktop ..
			dirHandle = Gdx.files.internal("./bin/data/maps");
		}

		for(FileHandle entry : dirHandle.list()) {
			String name = entry.name();
			String section = entry.name().split("_")[0];
			if(!result.containsKey(section))
				result.put(section, new ArrayList<String>());
			result.get(section).add(name);
		}
		return result;
	}
	
	private List<SimpleTreeView> makeLevelGroups(Map<String, List<String>> levels, float x, float y) {
		List<SimpleTreeView> result = new ArrayList<SimpleTreeView>();
		
		int clusterCount = 0;
		SimpleTreeView current = null;
		System.out.println(Arrays.toString(levels.keySet().toArray()));
		for(String sectionName : levels.keySet()) {
			if(clusterCount == 0) {
				current = new SimpleTreeView("level_select_group_" + (clusterCount / 3), x, y, 200);
				result.add(current);
			}
			SimpleLabel sectionLabel = new SimpleLabel("sectionHeading_" + sectionName,
					"Section " + sectionName,
					0, 40);
			sectionLabel.setFontSize(28);
			SimpleTreeItem section = new SimpleTreeItem(sectionLabel);
			current.addItem(section);
			for(String fileName : levels.get(sectionName)) {
				SimpleRadio button = new SimpleRadio(fileName, 0, 0, 100, 30);
				button.setText("Level " + fileName.split("_")[1].split("[.]")[0]);
				selection.add(button);
				section.addItem(new SimpleTreeItem(button));
			}
			clusterCount++;
		}
		//bottom message
		SimpleLabel sectionLabel = new SimpleLabel("bottom",
				"More Levels coming soon :)",
				0, 40);
		sectionLabel.setFontSize(28);
		SimpleTreeItem section = new SimpleTreeItem(sectionLabel);
		current.addItem(section);
		return result;
	}
	
	public void reset() {
		
	}

}
