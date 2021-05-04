/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.Jugador;
import com.gmail.cuestamanue7.juego.Partida;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class Estadisticas {

    private static FileConfiguration stats = null;
    private static File statsfile = null;

    public static void guardarEstadisticas(Partida partida, Bedwars plugin, Player player) throws IOException {
        File estadisticas;
        if (stats == null) {
            statsfile = new File(plugin.getDataFolder(), "estadisticas.yml");
        }
        estadisticas = new File(plugin.getDataFolder(), "estadisticas.yml");
        stats = YamlConfiguration.loadConfiguration(statsfile);
        Reader defConfigStream;
        try {
            FileReader fr = new FileReader(plugin.getDataFolder() + "\\estadisticas.yml");
            BufferedReader bf = new BufferedReader(fr);
            if (bf != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(bf);
                stats.setDefaults(defConfig);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
        }

        ArrayList<Jugador> jugadores = partida.getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            int muertes = 0;
            int asesinatos = 0;
            boolean entra=false;
            for (String nombre : stats.getConfigurationSection("Jugadores").getKeys(false)) {
                if (nombre.equals(jugadores.get(i).getjugador().getName())) {
                    asesinatos = Integer.valueOf(stats.getString("Jugadores." + nombre + ".asesinatos"));
                    muertes = Integer.valueOf(stats.getString("Jugadores." + nombre + ".muertes"));
                    asesinatos = asesinatos+partida.getjugador(nombre).getasesinatos();
                    muertes = muertes+partida.getjugador(nombre).getmuertes();
                    stats.set("Jugadores." + nombre + ".asesinatos", asesinatos);
                    stats.set("Jugadores." + nombre + ".muertes", muertes);
                    entra=true;
                }
            }
            if (!entra) {
                asesinatos = asesinatos+partida.getjugador(player.getName()).getasesinatos();
                muertes = muertes+partida.getjugador(player.getName()).getmuertes();
                stats.set("Jugadores." + player.getName() + ".asesinatos", asesinatos);
                stats.set("Jugadores." + player.getName() + ".muertes", muertes);
                
            }
        }

        stats.save(statsfile);

    }

    public static int[] recuperarEstadisticas(Player player, Bedwars plugin) {
        File estadisticas;
        if (stats == null) {
            statsfile = new File(plugin.getDataFolder(), "estadisticas.yml");
        }
        estadisticas = new File(plugin.getDataFolder(), "estadisticas.yml");
        stats = YamlConfiguration.loadConfiguration(statsfile);
        Reader defConfigStream;
        try {
            FileReader fr = new FileReader(plugin.getDataFolder() + "\\estadisticas.yml");
            BufferedReader bf = new BufferedReader(fr);
            if (bf != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(bf);
                stats.setDefaults(defConfig);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
        }
        int muertes = 0;
        int asesinatos = 0;

        for (String nombre : stats.getConfigurationSection("Jugadores").getKeys(false)) {
            if (nombre.equals(player.getName())) {
                asesinatos = Integer.valueOf(stats.getString("Jugadores." + nombre + ".asesinatos"));
                muertes = Integer.valueOf(stats.getString("Jugadores." + nombre + ".muertes"));
            }
        }

        int[] a = new int[2];
        a[0] = asesinatos;
        a[1] = muertes;

        return a;

    }

}
