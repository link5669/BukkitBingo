package io.github.link5669.AchievementBingo;

import org.bukkit.advancement.Advancement;

public class AdvancementNames {
	private Advancement advancement;
	private String name;
	
	public Advancement getAdvancement() {
		return this.advancement;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setAdvancement(Advancement advancement) {
		this.advancement = advancement;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
