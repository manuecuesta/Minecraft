/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import org.bukkit.event.Listener;
import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Partida;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Manuel
 */
public class CartelesListeners implements Listener {

    private Bedwars plugin;

    public CartelesListeners(Bedwars plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void crearcartel(SignChangeEvent event) {
        Player jugador = event.getPlayer();
        if (jugador.isOp()) {
            if (event.getLine(0).equals("[BedWars]")) {
                String arena = event.getLine(1);
                // [PaintBall]
                // arena1
                Partida partida = plugin.getPartida(arena);
                if (arena != null && partida != null) {
                    String estado = "";
                    if (partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                        estado = "&cEN JUEGO";
                    } else if (partida.getEstadoPartida().equals(EstadoPartida.ESPERANDO)) {
                        estado = "&6COMENZANDO";
                    } else if (partida.getEstadoPartida().equals(EstadoPartida.TERMINANDO)) {
                        estado = "&1ESPERANDO";
                    } else if (partida.getEstadoPartida().equals(EstadoPartida.COMENZANDO)) {
                        estado = "&cTERMINANDO";
                    } else {
                        estado = "&7DESACTIVADA";
                    }

                    List<String> lista = new ArrayList<String>();
                    lista.add("&8[&9BedWars&8]");
                    lista.add("&2" + arena);
                    lista.add(estado);
                    lista.add("&a" + partida.getcantidadactualjugadores() + "&8/&a" + partida.getcantidadmaximajugadores());

                    for (int i = 0; i < lista.size(); i++) {
                        event.setLine(i, ChatColor.translateAlternateColorCodes('&', lista.get(i)));
                    }

                    FileConfiguration config = plugin.getConfig();
                    List<String> listaCarteles = new ArrayList<String>();
                    if (config.contains("Signs."+arena)) {
                        listaCarteles = config.getStringList("Signs."+arena);
                    }
                    listaCarteles.add(event.getBlock().getX() + ";" + event.getBlock().getY() + ";" + event.getBlock().getZ() + ";" + event.getBlock().getWorld().getName());
                    config.set("Signs."+arena, listaCarteles);
                    plugin.saveConfig();
                }
            }
        }
    }

    @EventHandler
    public void eliminarcartel(BlockBreakEvent event) {
        Player jugador = event.getPlayer();
        Block block = event.getBlock();
        if (jugador.isOp()) {
            if (block.getType().name().contains("SIGN")) {
                FileConfiguration config = plugin.getConfig();
                if (config.contains("Signs")) {
                    for (String arena : config.getConfigurationSection("Signs").getKeys(false)) {
                        List<String> listaCarteles = new ArrayList<String>();
                        if (config.contains("Signs." + arena)) {
                            listaCarteles = config.getStringList("Signs." + arena);
                        }
                        for (int i = 0; i < listaCarteles.size(); i++) {
                            String[] separados = listaCarteles.get(i).split(";");
                            int x = Integer.valueOf(separados[0]);
                            int y = Integer.valueOf(separados[1]);
                            int z = Integer.valueOf(separados[2]);
                            World world = Bukkit.getWorld(separados[3]);
                            if (world != null) {
                                if (block.getX() == x && block.getY() == y && block.getZ() == z && block.getWorld().getName().equals(world.getName())) {
                                    listaCarteles.remove(i);
                                    config.set("Signs." + arena, listaCarteles);
                                    plugin.saveConfig();
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void entrarPartida(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block != null && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (block.getType().name().contains("SIGN")) {
                FileConfiguration config = plugin.getConfig();
                if (config.contains("Signs")) {
                    for (String arena : config.getConfigurationSection("Signs").getKeys(false)) {
                        Partida partida = plugin.getPartida(arena);
                        if (partida != null) {
                            List<String> listaCarteles = new ArrayList<String>();
                            if (config.contains("Signs." + arena)) {
                                listaCarteles = config.getStringList("Signs." + arena);
                            }
                            for (int i = 0; i < listaCarteles.size(); i++) {
                                String[] separados = listaCarteles.get(i).split(";");
                                int x = Integer.valueOf(separados[0]);
                                int y = Integer.valueOf(separados[1]);
                                int z = Integer.valueOf(separados[2]);
                                World world = Bukkit.getWorld(separados[3]);
                                if (world != null) {
                                    if (block.getX() == x && block.getY() == y && block.getZ() == z && block.getWorld().getName().equals(world.getName())) {
                                        Player jugador = event.getPlayer();
                                        if (partida.estaActivada()) {
                                            
                                            if (plugin.getPartidaJugador(jugador.getName()) == null) {
                                                if (!partida.estaIniciada()) {
                                                    if (!partida.estallena()) {
                                                        PartidaManager.jugadorEntra(partida, event.getPlayer(), plugin);
                                                    } else {
                                                        jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida esta llena"));
                                                    }
                                                } else {
                                                    jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida ya esta iniciada"));
                                                }
                                            } else {
                                                jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYa estas en una partida"));
                                            }

                                        } else {
                                            jugador.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cLa partida esta desactivada"));
                                        }
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
