/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.juego.Partida;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class CrearArena {

    public static void crearmundo(Player player, Partida partida) {
        World world;
        world = Bukkit.getServer().getWorld(partida.getNombre());
        if (world == null) {
            WorldCreator wc = new WorldCreator(partida.getNombre());
            wc.environment(Environment.NORMAL);
            wc.type(WorldType.FLAT);
            wc.generateStructures(false);
            wc.generatorSettings("0;0;0;");

            world = Bukkit.getServer().createWorld(wc);
            world.setAutoSave(false);
            world.setSpawnFlags(false, false);
            world.setKeepSpawnInMemory(false);
            world.setAnimalSpawnLimit(0);
            world.setAutoSave(false);
            world.setMonsterSpawnLimit(0);
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setStorm(false);
            world.setThundering(false);
            world.setTime(1000);
            if (player!=null) {
                player.teleport(world.getSpawnLocation());
            }
        }
    }
    
    public static void crearmundo(String nombre) {
        World world;
        world = Bukkit.getServer().getWorld(nombre);
        if (world == null) {
            WorldCreator wc = new WorldCreator(nombre);
            wc.environment(Environment.NORMAL);
            wc.type(WorldType.FLAT);
            wc.generateStructures(false);
            wc.generatorSettings("0;0;0;");

            world = Bukkit.getServer().createWorld(wc);
            world.setAutoSave(false);
            //world.setSpawnFlags(false, false);
            world.setKeepSpawnInMemory(false);
            //world.setAnimalSpawnLimit(0);
            world.setAutoSave(false);
            //world.setMonsterSpawnLimit(0);
            world.setDifficulty(Difficulty.PEACEFUL);
            world.setStorm(false);
            world.setThundering(false);
            world.setTime(1000);
        }
    }
    


}
