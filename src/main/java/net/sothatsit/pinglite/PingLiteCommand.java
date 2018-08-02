package net.sothatsit.pinglite;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PingLiteCommand implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getLabel().equalsIgnoreCase("pinglite")) {
            return false;
        }
        
        if (args.length == 0) {
            return msg(sender, "&4Invalid Args > &cValid: /pinglite <set:info>");
        }
        
        String sub = args[0];
        
        if (sub.equalsIgnoreCase("set")) {
            if (args.length == 1) {
                return msg(sender, "&4Invalid Args > &cValid: /pinglite set <line1:line2:playercount:icon>");
            }
            
            sub = args[1];
            
            if (sub.equalsIgnoreCase("line1")) {
                if (!hasPerm(sender, "pinglite.set.lines")) {
                    return msg(sender, "&4Error > &cYou do not have permission to run this command");
                }
                
                if (args.length == 2) {
                    return msg(sender, "&4Invalid Args > &cValid: /pinglite set line1 <message>");
                }
                
                String str = stringFromArgs(args);
                
                PingLite.getInstance().setLine1(str);
                return msg(sender, "&6Line 1 &7set to \"" + str + "&7\"");
            }
            
            if (sub.equalsIgnoreCase("line2")) {
                if (!hasPerm(sender, "pinglite.set.lines")) {
                    return msg(sender, "&4Error > &cYou do not have permission to run this command");
                }
                
                if (args.length == 2) {
                    return msg(sender, "&4Invalid Args > &cValid: /pinglite set line2 <message>");
                }
                
                String str = stringFromArgs(args);
                
                PingLite.getInstance().setLine2(str);
                return msg(sender, "&6Line 2 &7set to \"" + str + "&7\"");
            }
            
            if (sub.equalsIgnoreCase("playercount")) {
                if (!hasPerm(sender, "pinglite.set.playercount")) {
                    return msg(sender, "&4Error > &cYou do not have permission to run this command");
                }
                
                if (args.length != 3) {
                    return msg(sender, "&4Invalid Args > &cValid: /pinglite set playercount <plus-one:standard:number>");
                }
                
                String mode = args[2];
                
                if (mode.equalsIgnoreCase("plusone") || mode.equalsIgnoreCase("+1") || mode.equalsIgnoreCase("plus1") || mode.equalsIgnoreCase("+one")) {
                    mode = "plus-one";
                } else {
                    try {
                        Integer.valueOf(mode);
                    } catch (NumberFormatException e) {
                        if(!mode.equalsIgnoreCase("standard")) {
                            msg(sender, "&4Error > &cUnknown player-count mode &4" + mode);
                        }
                        mode = "standard";
                    }
                }
                
                PingLite.getInstance().setMaxPlayersMode(mode);
                return msg(sender, "&6Player Count &7set to \"" + mode + "&7\"");
            }
            
            if (sub.equalsIgnoreCase("icon")) {
                return msg(sender, "&cThis is not supported yet");
            }
            
            return msg(sender, "&4Invalid Args > &cValid: /pinglite set <line1:line2:playercount:icon>");
        }
        
        if (sub.equalsIgnoreCase("info")) {
            if (!hasPerm(sender, "pinglite.info")) {
                return msg(sender, "&4Error > &cYou do not have permission to run this command");
            }
            
            if (args.length != 1) {
                return msg(sender, "&4Invalid Args > &cValid: /pinglite info");
            }
            
            PingLite lite = PingLite.getInstance();
            String line1 = lite.getLine1();
            String line2 = lite.getLine2();
            String playerCount = lite.getMaxPlayersMode();
            
            msg(sender, "&6&l&m-----&8&l&m[ &5&l Ping&d&lLite &8&l&m ]&6&l&m-----");
            msg(sender, "&6Line-1&8: &7" + line1);
            msg(sender, "&6Line-2&8: &7" + line2);
            msg(sender, "&6Player-Count&8: &7" + playerCount);
            
            return true;
        }
        
        return msg(sender, "&4Invalid Args > &cValid: /pinglite <set:info>");
    }
    
    private static boolean hasPerm(CommandSender sender, String permission) {
        return sender.isOp() || sender.hasPermission(permission);
    }
    
    private static String stringFromArgs(String[] args) {
        StringBuilder builder = new StringBuilder();
        
        for (int i = 2; i < args.length; i++) {
            if (i != 2) {
                builder.append(' ');
            }
            
            builder.append(args[i]);
        }
        
        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }
    
    private static boolean msg(CommandSender sender, String message) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        return true;
    }
    
}
