/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Partida;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Manuel
 */
public class CartelesAdmin {

    int taskID;

    private Bedwars plugin;

    public CartelesAdmin(Bedwars plugin) {
        this.plugin = plugin;
    }

    public void actualizarCarteles() {
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ejecutarActualizaCarteles();
                }
            }
        }, 0L, 20L);
    }

    protected void ejecutarActualizaCarteles() {
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
                            int chunkX = x >> 4;
                            int chunkZ = z >> 4;
                            if (world.isChunkLoaded(chunkX, chunkZ)) {
                                Block bloque = world.getBlockAt(x, y, z);
                                if (bloque.getType().name().contains("SIGN")) {
                                    Sign sign = (Sign) bloque.getState();
                                    String estado = "";
                                    if (partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                                        estado = "&cEN JUEGO";
                                        Block bloqe = CristalCarteles.obtenerbloque(bloque);
                                        bloqe.setType(Material.STAINED_GLASS);
                                        bloqe.setData((byte) 14);
                                    } else if (partida.getEstadoPartida().equals(EstadoPartida.ESPERANDO)) {
                                        estado = "&1ESPERANDO";
                                        Block bloqe = CristalCarteles.obtenerbloque(bloque);

                                        bloqe.setType(Material.STAINED_GLASS);
                                        bloqe.setData((byte) 5);

                                    } else if (partida.getEstadoPartida().equals(EstadoPartida.TERMINANDO)) {
                                        estado = "&cTERMINANDO";
                                        Block bloqe = CristalCarteles.obtenerbloque(bloque);

                                        bloqe.setType(Material.STAINED_GLASS);
                                        bloqe.setData((byte) 14);

                                    } else if (partida.getEstadoPartida().equals(EstadoPartida.COMENZANDO)) {
                                        estado = "&6COMENZANDO";
                                        Block bloqe = CristalCarteles.obtenerbloque(bloque);

                                        bloqe.setType(Material.STAINED_GLASS);
                                        bloqe.setData((byte) 4);

                                    } else {
                                        estado = "&7DESACTIVADA";
                                        Block bloqe = CristalCarteles.obtenerbloque(bloque);

                                        bloqe.setType(Material.STAINED_GLASS);
                                        bloqe.setData((byte) 8);
                                    }

                                    List<String> lista = new ArrayList<String>();
                                    lista.add("&8[&9BedWars&8]");
                                    lista.add("&2" + arena);
                                    lista.add(estado);
                                    lista.add("&a" + partida.getcantidadactualjugadores() + "&8/&a" + partida.getcantidadmaximajugadores());

                                    for (int c = 0; c < lista.size(); c++) {
                                        sign.setLine(c, ChatColor.translateAlternateColorCodes('&', lista.get(c)));
                                    }

                                    sign.update();
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
