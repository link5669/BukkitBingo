package io.github.link5669.AchievementBingo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.bukkit.entity.Player;

public class BingoPlayer {
	private Player player;
	private Boolean[] achievementsFinished = new Boolean[24];
	
	public void setPlayerName(Player p) {
		this.player = p;
		for (int i = 0; i < this.achievementsFinished.length; i++) {
			achievementsFinished[i] = false;
		}
	}
	
	public void setAchievement(int place, Boolean bool) {
		this.achievementsFinished[place] = bool;
	}
	
	public String getPlayerName() {
		return player.getName();
	}
	
	public String[] getAchievementProgress(Path path) {
		try {
			String fileContent = Files.readString(path);
			for (int i = 0; i < fileContent.length(); i++) {
				if (fileContent.charAt(i) == 't') {
					achievementsFinished[i] = true;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String[] stringArray = new String[24];
		for (int i = 0; i < stringArray.length; i++) {
			if (achievementsFinished[i] == false) {
				stringArray[i] = "[x]";
			} else {
				stringArray[i] = "[✓]";
			}
		}
		return stringArray;
	}
}