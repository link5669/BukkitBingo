package io.github.link5669.AchievementBingo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public final class AchievementBingo extends JavaPlugin implements Listener, CommandExecutor {
	private Advancement[] gameAdvancements = new Advancement[25];
	
	@Override
    public void onEnable() {
		getLogger().info("onEnable has been invoked! Building advancement list");
		Iterator<Advancement> iter = Bukkit.getServer().advancementIterator();
		while(iter.hasNext()) {
            Advancement adv = iter.next();
            String caseKey = adv.getKey().toString();
            switch (caseKey) {
            case "minecraft:story/form_obsidian":
                this.gameAdvancements[0] = adv;
            case "minecraft:nether/brew_potion":
                this.gameAdvancements[1] = adv;
            case "minecraft:nether/distract_piglin":
                this.gameAdvancements[2] = adv;
            case "minecraft:nether/get_wither_skull":
                this.gameAdvancements[3] = adv;
    		case "minecraft:story/follow_ender_eye":
                this.gameAdvancements[4] = adv;
    		case "minecraft:adventure/ol_betsy":
                this.gameAdvancements[5] = adv;
    		case "minecraft:adventure/hero_of_the_village":
                this.gameAdvancements[6] = adv;
    		case "minecraft:nether/ride_strider":
                this.gameAdvancements[7] = adv;
    		case "minecraft:nether/obtain_blaze_rod":
                this.gameAdvancements[8] = adv;
    		case "minecraft:end/enter_end_gateway":
                this.gameAdvancements[9] = adv;
    		case "minecraft:story/enchant_item":
                this.gameAdvancements[10] = adv;
    		case "minecraft:adventure/trade":
                this.gameAdvancements[11] = adv;
    		case "minecraft:adventure/kill_a_mob":
                this.gameAdvancements[12] = adv;
    		case "minecraft:adventure/summon_iron_golem":
                this.gameAdvancements[13] = adv;
    		case "minecraft:story/shiny_gear":
                this.gameAdvancements[14] = adv;
    		case "minecraft:adventure/whos_the_pillager_now":
                this.gameAdvancements[15] = adv;
    		case "minecraft:story/voluntary_exile":
                this.gameAdvancements[16] = adv;
    		case "minecraft:husbandry/bee_our_guest":
                this.gameAdvancements[17] = adv;
    		case "minecraft:safely_harvest_honey":
    			this.gameAdvancements[18] = adv;
    		case "minecraft:end/kill_dragon":
    			this.gameAdvancements[19] = adv;
    		case "minecraft:story/deflect_arrow":
    			this.gameAdvancements[20] = adv;
    		case "minecraft:husbandry/breed_an_animal":
    			this.gameAdvancements[21] = adv;
    		case "minecraft:husbandry/tame_an_animal":
    			this.gameAdvancements[22] = adv;
    		case "minecraft:husbandry/tactical_fishing":
    			this.gameAdvancements[23] = adv;
    		case "minecraft:end/root":
    			this.gameAdvancements[24] = adv;
            }
        }
        String location = "plugins" + File.separator + "bingoSaves/";
        Path path = Paths.get(location);
        if (!Files.exists(path)) {
	        try {
	            File saveFile = new File("plugins" + File.separator + "bingoSaves/");
	            saveFile.mkdir();
	         } catch(Exception e) {
	            e.printStackTrace();
	         }
        }
		getServer().getPluginManager().registerEvents(this, this);
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("Closing AchievementBingo");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	if (cmd.getName().equalsIgnoreCase("bingo") && args.length == 2) {
    		BingoPlayer bPlayer = new BingoPlayer();
    		bPlayer.setPlayerName(Bukkit.getPlayer(args[1]));
    		String location = bPlayer.getFileName();
            Path path = Paths.get(location);
    		if (bPlayer.getPlayer() == null) {
	        	getLogger().info(args[1] + " is not currently online.");
	        	return false;
    		}
    		if ((Files.exists(path)) && args[0].equalsIgnoreCase("check")) {
		    	String[] progress = bPlayer.getAchievementProgress(path);
    			bPlayer.getPlayer().sendMessage(progress[0] +  progress[1] + progress[2] + progress[3] + progress[4]);
    			bPlayer.getPlayer().sendMessage(progress[5] +  progress[6] + progress[7] + progress[8] + progress[9]);
    			bPlayer.getPlayer().sendMessage(progress[10] +  progress[11] + progress[12] + progress[13] + progress[14]);
    			bPlayer.getPlayer().sendMessage(progress[15] +  progress[16] + progress[17] + progress[18] + progress[19]);
    			bPlayer.getPlayer().sendMessage(progress[20] +  progress[21] + progress[22] + progress[23] + progress[24]);
    			String fileContent;
				try {
					fileContent = Files.readString(path);
					if (fileContent.substring(49,52).equals("---")) {
						bPlayer.getPlayer().sendMessage("Points: " + 0);
    				} else if (fileContent.substring(51,52).equals("-")) {
    					int points = Integer.parseInt(fileContent.substring(49, 51));
    					bPlayer.getPlayer().sendMessage("Points: " + points);
    				} else {
	    				int points = Integer.parseInt(fileContent.substring(49, 52));
	    				bPlayer.getPlayer().sendMessage("Points: " + points);
    				}
				} catch (IOException e) {
					e.printStackTrace();
				}
		    	return true;
    		} else if (args[0].equalsIgnoreCase("add")) {
    			if (Files.exists(path)) {
    				bPlayer.getPlayer().sendMessage("Already in registry");
    	    		getLogger().info("Already in registry");
    	    		return false;
    			}
    			createNewSave(bPlayer);
    	        return true;
	    	} else {
	    		bPlayer.getPlayer().sendMessage("Please add player to registry");
	    		getLogger().info("Please add player to registry");
	    		return false; 
	    	}
    	}
    	return false;
    }
    
    @EventHandler
    public void finishAdvancement(PlayerAdvancementDoneEvent event) {
    	Advancement targetAdv = event.getAdvancement();
    	AdvancementProgress avp = event.getPlayer().getAdvancementProgress(targetAdv);
    	BingoPlayer bPlayer = new BingoPlayer();
		bPlayer.setPlayerName(event.getPlayer());
    	String location = bPlayer.getFileName();
        Path path = Paths.get(location);
    	if (Files.exists(path)) {
    		if ((targetAdv.getKey().toString().charAt(10) == 'r')) {
    			return;
    		} else if (avp != null && avp.isDone() && containsAdvancement(targetAdv)) {
    			int achIndex = 1000;
    			for (int i = 0; i < this.gameAdvancements.length; i++) {
    				if (this.gameAdvancements[i] == targetAdv) {
    					achIndex = i;
        				break;
    				}
    			}
    			try {
    				String fileContent = Files.readString(path);
    				FileWriter myWriter = new FileWriter(location);
    				if (fileContent.substring(49,52).equals("---")) {
    					int points = 10;
    					fileContent = fileContent.substring(0, 49) + points + fileContent.substring(51); 
    				} else if (fileContent.substring(51,52).equals("-")) {
    					int points = Integer.parseInt(fileContent.substring(49, 51));
    					points +=10;
        				fileContent = fileContent.substring(0, 49) + points + fileContent.substring(51); 
    				} else {
	    				int points = Integer.parseInt(fileContent.substring(49, 52));
	    				points +=10;
	    				fileContent = fileContent.substring(0, 49) + points + fileContent.substring(50); 
    				}
    				fileContent = fileContent.substring(0, achIndex) + 't' + fileContent.substring(achIndex + 1);
    				myWriter.write(fileContent);
    				myWriter.close();
    				checkTrack(bPlayer);
				} catch (IOException e) {
					e.printStackTrace();
				}
    		} else {
    			getLogger().info("incompatable advancement");
    		}
		} else {
		    getLogger().info("player isnt registered");
		}
    }
    
    public Boolean containsAdvancement(Advancement advancement) {
    	for (int i = 0; i < this.gameAdvancements.length; i++) {
    		if ( this.gameAdvancements[i] == advancement) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public void createNewSave(BingoPlayer player) {
    	String name = player.getFileName();
		Path path = Paths.get(name);
		  if (!Files.exists(path)) {
			File myObj = new File(name);
			try {
				myObj.createNewFile();
				getLogger().info("File created: " + myObj.getName());
				FileWriter myWriter = new FileWriter(name);
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("////////////////////////---");
			    myWriter.flush();
			    myWriter.close();
			    getLogger().info("Successfully added player!");
    	        player.getPlayer().sendMessage("Sucessfully added player " + player.getPlayerName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		  } else {
		    System.out.println("File already exists.");
		  }
    }
	
    public void checkTrack(BingoPlayer player) {
    	String name = player.getFileName();
		Path path = Paths.get(name);
    	try {
    		String fileContent = Files.readString(path);
    		PreContent var = new PreContent();
			var.setChar(fileContent.charAt(0));
    		FileWriter myWriter = new FileWriter(name);
			checkRow(25, 0, "NT", fileContent);
			checkRow(27, 5, "ET", fileContent);
			checkRow(29, 10, "OT", fileContent);
			checkRow(31, 15, "SS", fileContent);
			checkRow(33, 20, "OV", fileContent);
			var.setString(fileContent);
			var = checkColumn(0, 35, 20, var, "PT", myWriter);
			var = checkColumn(1, 37, 21, var, "VT", myWriter);
			var = checkColumn(2, 39, 22, var, "MO", myWriter);
			var = checkColumn(3, 41, 23, var, "MT", myWriter);
			var = checkColumn(4, 43, 24, var, "EN", myWriter);
			myWriter.write(var.getString());
			myWriter.flush();
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
    
    private PreContent checkColumn(int a, int b, int c, PreContent var, String code, FileWriter myWriter) {
    	var.setChar(var.getString().charAt(a));
    	for (int i = a; i <= 24; i+=5) {
			if ((var.getChar() == var.getString().charAt(i)) && (var.getChar() == 't') && !(var.getString().substring(b,b+2).equals(code))) {
				char j = var.getString().charAt(i);
				var.setChar(j);
				if (i == c) {
				    if (var.getString().substring(51,52).equals("-")) {
						int points = Integer.parseInt(var.getString().substring(49, 51));
						points +=20;
						var.setString(var.getString().substring(0, 49) + points + var.getString().substring(51)); 
				    } else {
	    				int points = Integer.parseInt(var.getString().substring(49, 52));
	    				points +=20;
	    				var.setString(var.getString().substring(0, 49) + points + var.getString().substring(50)); 
					}
					var.setString(var.getString().substring(0, b) + code + var.getString().substring(b + 2));
				}
			}
    	}
    	return var;
    }
    
    private String checkRow(int a, int c, String code, String fileContent) {
    	if (fileContent.substring(c,c+5).equals("ttttt") && !(fileContent.substring(a,a+2).equals(code))) {
			fileContent = fileContent.substring(0, a) + code + fileContent.substring(a + 2);
			checkPointLengthHorizontal(fileContent);
		}
    	return fileContent;
    }
    
    private String checkPointLengthHorizontal(String fileContent) {
    	if (fileContent.substring(51,52).equals("-")) {
			int points = Integer.parseInt(fileContent.substring(49, 51));
			points +=20;
			fileContent = fileContent.substring(0, 49) + points + fileContent.substring(51); 
	    } else {
			int points = Integer.parseInt(fileContent.substring(49, 52));
			points +=20;
			fileContent = fileContent.substring(0, 49) + points + fileContent.substring(50); 
		}
    	return fileContent;
    }
}