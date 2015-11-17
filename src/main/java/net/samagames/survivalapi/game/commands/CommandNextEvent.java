package net.samagames.survivalapi.game.commands;

import net.samagames.survivalapi.game.SurvivalGame;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandNextEvent implements CommandExecutor
{
    private static SurvivalGame game;

    public static void setGame(SurvivalGame instance)
    {
        game = instance;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args)
    {
        if (game.getSurvivalGameLoop() != null && game.isGameStarted())
            game.getSurvivalGameLoop().forceNextEvent();
        else
            commandSender.sendMessage(ChatColor.RED + "Erreur: la partie n'a pas commencé !");

        return true;
    }
}
