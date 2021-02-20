package io.github.link5669.AchievementBingo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.server.BroadcastMessageEvent;

import net.md_5.bungee.api.chat.*;

import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public final class AchievementBingo extends JavaPlugin implements Listener, CommandExecutor {
	private AdvancementNames[] gameAdvancements = new AdvancementNames[25];
	private Boolean shuffled = true;
	//TODO add announcements when any player gets a bingo
	@Override
    public void onEnable() {
		getLogger().info("onEnable has been invoked! Building advancement list");
		Iterator<Advancement> iter = Bukkit.getServer().advancementIterator();
		while(iter.hasNext()) {
            Advancement adv = iter.next();
            String caseKey = adv.getKey().toString();
            switch (caseKey) {
            case "minecraft:story/form_obsidian": makeAdv(0, adv, "• Ice Bucket Challenge");
            	break;
            case "minecraft:nether/brew_potion": makeAdv(1, adv, "• Local Brewery");
        		break;
            case "minecraft:nether/distract_piglin":  makeAdv(2, adv, "• Oh Shiny");
            	break;
            case "minecraft:nether/get_wither_skull": makeAdv(3, adv, "• Spooky Scary Skeleton");
            	break;
    		case "minecraft:story/follow_ender_eye":  makeAdv(4, adv, "• Eye Spy");
            	break;
    		case "minecraft:adventure/ol_betsy": makeAdv(5, adv, "• Ol' Betsy");
            	break;
    		case "minecraft:adventure/hero_of_the_village": makeAdv(6, adv,"• Hero of the Village");
            	break;
    		case "minecraft:nether/ride_strider": makeAdv(7, adv, "• This Boat Has Legs");
            	break;
    		case "minecraft:nether/obtain_blaze_rod": makeAdv(8, adv, "• Into Fire");
            	break;
    		case "minecraft:end/enter_end_gateway": makeAdv(9, adv, "• Remote Getaway");
            	break;
    		case "minecraft:story/enchant_item": makeAdv(10, adv, "• Enchanter");
            	break;
    		case "minecraft:adventure/trade": makeAdv(11, adv, "• What A Deal!");
            	break;
    		case "minecraft:adventure/kill_a_mob": makeAdv(12, adv, "• Monster Hunter");
            	break;
    		case "minecraft:adventure/summon_iron_golem": makeAdv(13, adv, "• Hired Help");
            	break;
    		case "minecraft:story/shiny_gear": makeAdv(14, adv, "• Cover Me With Diamonds");
            	break;
    		case "minecraft:adventure/whos_the_pillager_now": makeAdv(15, adv, "• Who's The Pillager Now?");
            	break;
    		case "minecraft:adventure/voluntary_exile": makeAdv(16, adv, "• Voluntary Exile");
            	break;
    		case "minecraft:husbandry/safely_harvest_honey": makeAdv(17, adv, "• Bee Our Guest");
            	break;
    		case "minecraft:adventure/honey_block_slide": makeAdv(18, adv, "• Sticky Situation");
    			break;
    		case "minecraft:end/kill_dragon": makeAdv(19, adv, "• Free The End");
    			break;
    		case "minecraft:story/deflect_arrow": makeAdv(20, adv, "• Not Today, Thank You");
    			break;
    		case "minecraft:husbandry/breed_an_animal": makeAdv(21, adv, "• The Parrots and the Bats");
    			break;
    		case "minecraft:husbandry/tame_an_animal": makeAdv(22, adv, "• Best Friends Forever");
    			break;
    		case "minecraft:husbandry/tactical_fishing": makeAdv(23, adv, "• Tactical Fishing");
    			break;
    		case "minecraft:end/root": makeAdv(24, adv, "• The End?");
    			break;
            }
        }
		for (int i = 0; i < this.gameAdvancements.length; i++) {
    		getLogger().info(this.gameAdvancements[i].getName() + "");
		}
		if (shuffled == true) {
			List<AdvancementNames> arrList = Arrays.asList(this.gameAdvancements);
			Collections.shuffle(arrList);
			arrList.toArray(this.gameAdvancements);
		}
        String location = "plugins" + File.separator + "bingoSaves" + File.separator;
        Path path = Paths.get(location);
        if (!Files.exists(path)) {
	        try {
	            File saveFile = new File("plugins" + File.separator + "bingoSaves" + File.separator);
	            saveFile.mkdir();
	         } catch(Exception e) {
	            e.printStackTrace();
	         }
        }
        
		getServer().getPluginManager().registerEvents(this, this);
    }
    
	public void makeAdv(int i, Advancement adv, String name) {
		AdvancementNames advance = new AdvancementNames();
		this.gameAdvancements[i] = advance;
    	this.gameAdvancements[i].setAdvancement(adv);
		this.gameAdvancements[i].setName(name);
	}
	
    @Override
    public void onDisable() {
    	getLogger().info("Closing AchievementBingo");
    }
    
    @SuppressWarnings("deprecation")
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
    			getRankings(bPlayer);	
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
    				if (this.gameAdvancements[i].getAdvancement() == targetAdv) {
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
    		if ( this.gameAdvancements[i].getAdvancement() == advancement) {
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
    	        giveBook(player);
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
			checkRow(25, 0, "NT", fileContent, player);
			checkRow(27, 5, "ET", fileContent, player);
			checkRow(29, 10, "OT", fileContent, player);
			checkRow(31, 15, "SS", fileContent, player);
			checkRow(33, 20, "OV", fileContent, player);
			var.setString(fileContent);
			var = checkColumn(0, 35, 20, var, "PT", myWriter, player);
			var = checkColumn(1, 37, 21, var, "VT", myWriter, player);
			var = checkColumn(2, 39, 22, var, "MO", myWriter, player);
			var = checkColumn(3, 41, 23, var, "MT", myWriter, player);
			var = checkColumn(4, 43, 24, var, "EN", myWriter, player);
			myWriter.write(var.getString());
			myWriter.flush();
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    } 
    
    private PreContent checkColumn(int a, int b, int c, PreContent var, String code, FileWriter myWriter, BingoPlayer player) {
    	var.setChar(var.getString().charAt(a));
    	for (int i = a; i <= 24; i+=5) {
			if ((var.getChar() == var.getString().charAt(i)) && (var.getChar() == 't') && !(var.getString().substring(b,b+2).equals(code))) {
				char j = var.getString().charAt(i);
				var.setChar(j);
				if (i == c) {
					Bukkit.broadcast(player.getPlayerName() + "got a bingo!", "");
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
    
    private String checkRow(int a, int c, String code, String fileContent, BingoPlayer player) {
    	if (fileContent.substring(c,c+5).equals("ttttt") && !(fileContent.substring(a,a+2).equals(code))) {
			fileContent = fileContent.substring(0, a) + code + fileContent.substring(a + 2);
			checkPointLengthHorizontal(fileContent);
			Bukkit.broadcast(player.getPlayerName() + "got a bingo!", "");
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
    
    private void getRankings(BingoPlayer bPlayer) {
    	String path = "plugins" + File.separator + "bingoSaves" + File.separator;
    	File dir = new File(path);
    	int numPlayers = dir.listFiles().length;
    	getLogger().info(dir.listFiles().toString());
    	PlayerScore[] players;
    	players = new PlayerScore[numPlayers];
    	PlayerScore pScoreN = new PlayerScore();
    	for (int o = 0; o < numPlayers; o++ ) {
    		players[o] = pScoreN;
    	}
    	int i = 0;
    	String fileContent = " ";
    	for (File file : dir.listFiles()) {
			try {
				Path toPath = Paths.get(path + file.getName());
				fileContent = Files.readString(toPath);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String fileName = file.getName().toString().replaceAll(".txt", "");
    		players[i].setName(fileName);
    		if (fileContent.substring(49,52).equals("---")) {
    			players[i].setPlayerScore(0);
			} else if (fileContent.substring(51,52).equals("-")) {
				players[i].setPlayerScore(Integer.parseInt(fileContent.substring(49,51)));
			} else {
				players[i].setPlayerScore(Integer.parseInt(fileContent.substring(49,52)));
			}
    		i++;
    	}
    	bubbleSort(players);
    	for (int j = 0; j < numPlayers; j++) {
    		bPlayer.getPlayer().sendMessage(players[j].getName() + ": " + players[j].getPlayerScore());
    	}
    }
    
    public static void bubbleSort(PlayerScore[] a) {
        boolean sorted = false;
        int temp;
        while(!sorted) {
            sorted = true;
            for (int i = 0; i < a.length - 1; i++) {
                if (a[i].getPlayerScore() > a[i+1].getPlayerScore()) {
                    temp = a[i].getPlayerScore();
                    a[i].setPlayerScore(a[i+1].getPlayerScore());
                    a[i+1].setPlayerScore(temp);
                    sorted = false;
                }
            }
        }
    }
    
    public void giveBook(BingoPlayer player) {
    	if (shuffled==true) {
        	ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        	BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();
        	bookMeta.setTitle("Bingo");
        	bookMeta.setAuthor("link5669");
        	String text = "";
        	int j = 1;
        	for (int i = 0; i < this.gameAdvancements.length; i++) {
        		text += this.gameAdvancements[i].getName();
        		text += "\n";
        		if (i % 9 == 0) {
        			bookMeta.addPage("");
        			bookMeta.setPage(j, text);
        			j++;
        			text = "";
        		}
        	}
        	bookMeta.addPage(text);
        	writtenBook.setItemMeta(bookMeta);
        	player.getPlayer().getInventory().addItem(writtenBook);
        }
    }
}