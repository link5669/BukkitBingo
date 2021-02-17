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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public final class AchievementBingo extends JavaPlugin implements Listener, CommandExecutor {
//	private List<BingoPlayer> bingoNameList = new ArrayList<BingoPlayer>();
	private Advancement[] gameAdvancements = new Advancement[25];
	
	@Override
    public void onEnable() {
		getLogger().info("onEnable has been invoked! Building advancement list");
        for (Iterator<Advancement> iter = Bukkit.getServer().advancementIterator(); iter.hasNext(); ) {
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
        String location = "/Users/milesacq/server164vanilla/plugins/bingoSaves";
        Path path = Paths.get(location);
        if (!Files.exists(path)) {
	        try {
	            File saveFile = new File("/Users/milesacq/server164vanilla/plugins/bingoSaves");
	            saveFile.mkdir();
	         } catch(Exception e) {
	            e.printStackTrace();
	         }
        }
		getServer().getPluginManager().registerEvents(this, this);
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("onDisable has been invoked!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    	for (int i = 0; i < this.gameAdvancements.length; i++) {
    		getLogger().info(this.gameAdvancements[i].getKey().toString());
    	}
    	if (cmd.getName().equalsIgnoreCase("bingo") && args.length == 2) {
    		Player target = Bukkit.getPlayer(args[1]);
    		BingoPlayer bPlayer = new BingoPlayer();
    		bPlayer.setPlayerName(target);
    		String location = bPlayer.getFileName();
            Path path = Paths.get(location);
    		if (target == null) {
	        	getLogger().info(args[1] + " is not currently online.");
	        	return false;
    		}
    		if ((Files.exists(path)) && args[0].equalsIgnoreCase("check")) {
		    	String[] progress = bPlayer.getAchievementProgress(path);
    			target.sendMessage(progress[0] +  progress[1] + progress[2] + progress[3] + progress[4]);
    			target.sendMessage(progress[5] +  progress[6] + progress[7] + progress[8] + progress[9]);
    			target.sendMessage(progress[10] +  progress[11] + progress[12] + progress[13] + progress[14]);
    			target.sendMessage(progress[15] +  progress[16] + progress[17] + progress[18] + progress[19]);
    			target.sendMessage(progress[20] +  progress[21] + progress[22] + progress[23] + progress[24]);
		    	return true;
    		} else if (args[0].equalsIgnoreCase("add")) {
    			if (Files.exists(path)) {
    	    		getLogger().info("Already in registry");
    	    		return false;
    			}
    			createNewSave(bPlayer);
    	        getLogger().info("Successfully added player!");
    	        return true;
	    	} else {
	    		getLogger().info("Please add player to registry");
	    		return false; 
	    	}
    	}
    	return false;
    }
    
    @EventHandler
    public void finishAdvancement(PlayerAdvancementDoneEvent event) {
    	Player target = event.getPlayer();
    	Advancement targetAdv = event.getAdvancement();
    	AdvancementProgress avp = target.getAdvancementProgress(targetAdv);
    	BingoPlayer bPlayer = new BingoPlayer();
		bPlayer.setPlayerName(target);
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
    			getLogger().info(achIndex + " index num");
    			try {
    				String fileContent = Files.readString(path);
    				fileContent = fileContent.substring(0, achIndex) + 't' + fileContent.substring(achIndex + 1); 
    				FileWriter myWriter = new FileWriter(location);
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
				System.out.println("File created: " + myObj.getName());
				FileWriter myWriter = new FileWriter(name);
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("fffff");
			    myWriter.write("////////////////////////");
			    myWriter.close();
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
    		char pre = fileContent.charAt(0);
    		FileWriter myWriter = new FileWriter(name);
			String trueTrack = "ttttt";
			if (fileContent.substring(0,5).equals(trueTrack)) {
				fileContent = fileContent.substring(0, 25) + "NT" + fileContent.substring(25 + 2); 
			    myWriter.write(fileContent);
			}
			if (fileContent.substring(5,10).equals(trueTrack)) {
				fileContent = fileContent.substring(0, 27) + "ET" + fileContent.substring(27 + 2); 
			    myWriter.write(fileContent); 
			}
			if (fileContent.substring(10,15).equals(trueTrack)) {
				fileContent = fileContent.substring(0, 29) + "OT" + fileContent.substring(29 + 2); 
			    myWriter.write(fileContent);
			}
			if (fileContent.substring(15,20).equals(trueTrack)) {
				fileContent = fileContent.substring(0, 31) + "SS" + fileContent.substring(31 + 2); 
			    myWriter.write(fileContent);
			}
			if (fileContent.substring(20,25).equals(trueTrack)) {
				fileContent = fileContent.substring(0, 33) + "OV" + fileContent.substring(33 + 2); 
			}
			for (int i = 0; i <= 24; i+=5) {
				if (pre == fileContent.charAt(i)) {
					pre = fileContent.charAt(i);
					if (i == 20) {
						fileContent = fileContent.substring(0, 37) + "PT" + fileContent.substring(37 + 2);
					}
				}
			}
			for (int i = 1; i <= 24; i+=5) {
				if (pre == fileContent.charAt(i)) {
					pre = fileContent.charAt(i);
					if (i == 21) {
						fileContent = fileContent.substring(0, 41) + "VT" + fileContent.substring(41 + 2);
					}
				}
			}
			for (int i = 2; i <= 24; i+=5) {
				if (pre == fileContent.charAt(i)) {
					pre = fileContent.charAt(i);
					if (i == 22) {
						fileContent = fileContent.substring(0, 43) + "MO" + fileContent.substring(43 + 2);
					}
				}
			}
			for (int i = 3; i <= 24; i+=5) {
				if (pre == fileContent.charAt(i)) {
					pre = fileContent.charAt(i);
					if (i == 23) {
						fileContent = fileContent.substring(0, 45) + "MT" + fileContent.substring(45 + 2);
					}
				}
			}
			for (int i = 4; i <= 24; i+=5) {
				if (pre == fileContent.charAt(i)) {
					pre = fileContent.charAt(i);
					if (i == 24) {
						fileContent = fileContent.substring(0, 47) + "EN" + fileContent.substring(47 + 2);
					}
				}
			}
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}