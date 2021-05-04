/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Generador;
import com.gmail.cuestamanue7.juego.Partida;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Manuel
 */
public class PartidaDrops {

    static int taskID;
    static int taskID1;
    static int taskID2;
    static int taskID3;

    public static void drop(World mundo, ItemStack item, Location l) {
        Location loc=new Location(l.getWorld(), l.getBlockX()+0.5, l.getBlockY(), l.getBlockZ()+0.5);
        mundo.dropItem(loc, item);
    }

    public static ArmorStand hologramdrop(ItemStack item, Location l, ItemStack cabeza) {
        Location loc=new Location(l.getWorld(), l.getBlockX()+0.5, l.getBlockY()+1, l.getBlockZ()+0.5);
        ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND); //Spawn the ArmorStand
        as.setGravity(false); //Make sure it doesn't fall
        as.setCanPickupItems(false); //I'm not sure what happens if you leave this as it is, but you might as well disable it
        if (cabeza.getData().equals(Material.IRON_BLOCK)) {
            as.setCustomName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', "&cIron Generator"))); //Set this to the text you want
        } else if (cabeza.getData().equals(Material.DIAMOND_BLOCK)) {
            as.setCustomName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', "&cDiamond Generator")));
        } else {
            as.setCustomName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', "&cEmerald Generator")));
        }
        as.setCustomNameVisible(true); //This makes the text appear no matter if your looking at the entity or not
        if (cabeza != null) {
            as.setHelmet(cabeza);
        }

        as.setVisible(false); //Makes the ArmorStand invisible
        return as;
    }

    public static void dropspawniron(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();
        ArrayList<Generador> generador1 = partida.getgeneradores();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.IRON_BLOCK)));
        }
        taskID = scheduler.scheduleSyncRepeatingTask(bedwars, new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<generador1.size(); i++) {
                    drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                }
                
                if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    for (int i=0; i<generador1.size(); i++) {
                        as.get(i).remove();
                       
                    }
                    return;
                }
            }
        }, 0L, 20L);
    }

    public static void dropspawngold(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.GOLD_INGOT, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();
        ArrayList<Generador> generador1 = partida.getgeneradores();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.IRON_BLOCK)));
        }
        taskID1 = scheduler.scheduleSyncRepeatingTask(bedwars, new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<generador1.size(); i++) {
                    drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                }
                
                if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                    Bukkit.getScheduler().cancelTask(taskID1);
                    for (int i=0; i<generador1.size(); i++) {
                        as.get(i).remove();
                       
                    }
                    return;
                }
            }
        }, 0L, 40L);
    }

    public static void dropdiamante(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.DIAMOND, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();;
        ArrayList<Generador> generador1 = partida.getdiamante();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.DIAMOND_BLOCK)));
        }
        taskID2 = scheduler.scheduleSyncRepeatingTask(bedwars, new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<generador1.size(); i++) {
                    drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                }
                
                if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                    Bukkit.getScheduler().cancelTask(taskID2);
                    for (int i=0; i<generador1.size(); i++) {
                        as.get(i).remove();
                       
                    }
                    return;
                }
            }
        }, 0L, 200L);
    }

    public static void dropesmeralda(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.EMERALD, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();
        ArrayList<Generador> generador1 = partida.getesmeralda();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.EMERALD_BLOCK)));
        }
        taskID3 = scheduler.scheduleSyncRepeatingTask(bedwars, new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<generador1.size(); i++) {
                    drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                }
                
                if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                    Bukkit.getScheduler().cancelTask(taskID3);
                    for (int i=0; i<generador1.size(); i++) {
                        as.get(i).remove();
                       
                    }
                }
            }
        }, 0L, 1200L);
    }

}
