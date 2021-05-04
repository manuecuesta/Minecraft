/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.Equipo;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Jugador;
import com.gmail.cuestamanue7.juego.Partida;
import com.gmail.cuestamanue7.lib.netherboard.BPlayerBoard;
import com.gmail.cuestamanue7.lib.netherboard.Netherboard;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Manuel
 */
public class ScoreboardAdmin {

    int taskID;

    private Bedwars plugin;

    public ScoreboardAdmin(Bedwars plugin) {
        this.plugin = plugin;
    }

    public void crearScoreboards() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    actualizarScoreboard(player);
                }
            }
        }, 0L, 20l);
    }

    protected void actualizarScoreboard(Player player) {
        Partida partida = plugin.getPartidaJugador(player.getName());
        if (partida != null) {
            Jugador j = partida.getjugador(player.getName());
            BPlayerBoard board = Netherboard.instance().getBoard(player);
            if (board == null) {
                board = Netherboard.instance().createBoard(player, ChatColor.translateAlternateColorCodes('&', "&cBedWars"));
            }

            List<String> lista = new ArrayList<String>();
            lista.add(ChatColor.translateAlternateColorCodes('&', "&1"));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fArena: &a" + partida.getNombre()));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&2"));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fEstado: &a"));
            lista.add(ChatColor.translateAlternateColorCodes('&', getestado(partida)));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&3"));

            ArrayList<Equipo> equipos = partida.getequipos();

            for (int i = 0; i < equipos.size(); i++) {
                if (equipos.get(i).isCama1()) {
                    switch (equipos.get(i).gettipo()) {
                        case "BLUE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&9" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                        case "GREEN":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&a" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                        case "RED":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&c" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                        case "YELLOW":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&e" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                        case "ORANGE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&6" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                        case "PURPLE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&d" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                        case "BLACK":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&0" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                        case "WHITE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&f" + equipos.get(i).gettipo() + ":&a&l ✓️"));
                            break;
                    }
                } else {
                    switch (equipos.get(i).gettipo()) {
                        case "BLUE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&9" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                        case "GREEN":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&a" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                        case "RED":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&c" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                        case "YELLOW":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&e" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                        case "ORANGE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&6" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                        case "PURPLE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&d" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                        case "BLACK":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&0" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                        case "WHITE":
                            lista.add(ChatColor.translateAlternateColorCodes('&', "&f" + equipos.get(i).gettipo() + ":&c&l X️"));
                            break;
                    }
                    
                }
            }
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fTus asesinatos: &a" + j.getasesinatos()));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&4"));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fJugadores: &a" + partida.getcantidadactualjugadores() + "&8/&a" + partida.getcantidadmaximajugadores()));

            for (int i = 0; i < lista.size(); i++) {
                int pos = lista.size() - i;
                board.set(lista.get(i), pos);
            }
        } else {
            actualizarScoreboardlobby(player);
        }
    }

    protected void actualizarScoreboardlobby(Player player) {
        Partida partida = plugin.getPartidaJugador(player.getName());
        if (partida == null) {
            BPlayerBoard board = Netherboard.instance().getBoard(player);
            if (board == null) {
                board = Netherboard.instance().createBoard(player, ChatColor.translateAlternateColorCodes('&', "&cBedWars"));
            }
            List<String> lista = new ArrayList<String>();
            lista.add(ChatColor.translateAlternateColorCodes('&', "&1"));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fLobby BedWars: "));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&2"));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fIp: Minecraft.net&a"));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&3"));
            int stats[] = Estadisticas.recuperarEstadisticas(player, plugin);
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fAsesinatos: &a" + stats[0]));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fMuertes &c" + stats[1]));
            lista.add(ChatColor.translateAlternateColorCodes('&', "&4"));
            List<Player> lista1 = new ArrayList<>(Bukkit.getOnlinePlayers());
            lista.add(ChatColor.translateAlternateColorCodes('&', "&fJugadores: &a") + lista1.size());

            for (int i = 0; i < lista.size(); i++) {
                int pos = lista.size() - i;
                board.set(lista.get(i), pos);
            }
        } else {
            if (Netherboard.instance().getBoard(player) != null) {
                Netherboard.instance().deleteBoard(player);
            }
        }
    }

    private String getestado(Partida partida) {
        if (partida.getEstadoPartida().equals(EstadoPartida.ESPERANDO)) {
            return "&7ESPERANDO";
        } else if (partida.getEstadoPartida().equals(EstadoPartida.COMENZANDO)) {
            int tiempo = partida.getTiempo();
            return "&7EMPEZANDO EN: &a" + UtilidadesPaintball.getTiempo(tiempo);
        } else if (partida.getEstadoPartida().equals(EstadoPartida.JUGANDO) && partida.getFase()==1) {
            int tiempo = partida.getTiempo();
            return "&7DIAMANTE II EN: &a" + UtilidadesPaintball.getTiempo(tiempo);
        }   else if (partida.getEstadoPartida().equals(EstadoPartida.JUGANDO) && partida.getFase()==2) {
            int tiempo = partida.getTiempo();
            return "&7EMERALD II EN: &a" + UtilidadesPaintball.getTiempo(tiempo);
        }   else if (partida.getEstadoPartida().equals(EstadoPartida.JUGANDO) && partida.getFase()==3) {
            int tiempo = partida.getTiempo();
            return "&7DIAMANTE III EN: &a" + UtilidadesPaintball.getTiempo(tiempo);
        }   else if (partida.getEstadoPartida().equals(EstadoPartida.JUGANDO) && partida.getFase()==4) {
            int tiempo = partida.getTiempo();
            return "&7EMERALD III EN: &a" + UtilidadesPaintball.getTiempo(tiempo);
        } else if (partida.getEstadoPartida().equals(EstadoPartida.TERMINANDO)) {
            int tiempo = partida.getTiempo();
            return "&7FINALIZANDO EN: &a" + UtilidadesPaintball.getTiempo(tiempo);
        }
        return null;
    }

}
