/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import java.util.ArrayList;
import nms.Villager;
import nms.Villager.EntityTypes;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author Manuel
 */
public class Tienda {

    public Tienda() {

    }

    public static void cargartiendas(Partida p) {

        ArrayList<Location> tiendas = p.getTienda();

        for (int i = 0; i < tiendas.size(); i++) {

            Location loc = new Location(tiendas.get(i).getWorld(), tiendas.get(i).getX(), tiendas.get(i).getY(), tiendas.get(i).getZ(), tiendas.get(i).getYaw(), tiendas.get(i).getPitch());
            World mundo = tiendas.get(i).getWorld();/*
            mundo.getBlockAt(loc).getRelative(BlockFace.NORTH).setType(Material.BARRIER);
            Location loc1=new Location(mundo, mundo.getBlockAt(loc).getRelative(BlockFace.NORTH).getLocation().getBlockX(), mundo.getBlockAt(loc).getRelative(BlockFace.NORTH).getLocation().getBlockY()+2, mundo.getBlockAt(loc).getRelative(BlockFace.NORTH).getLocation().getBlockZ());
            mundo.getBlockAt(loc1).setType(Material.BARRIER);
            
            mundo.getBlockAt(loc).getRelative(BlockFace.SOUTH).setType(Material.BARRIER);
            Location loc2=new Location(mundo, mundo.getBlockAt(loc).getRelative(BlockFace.SOUTH).getLocation().getBlockX(), mundo.getBlockAt(loc).getRelative(BlockFace.SOUTH).getLocation().getBlockY()+2, mundo.getBlockAt(loc).getRelative(BlockFace.SOUTH).getLocation().getBlockZ());
            mundo.getBlockAt(loc2).setType(Material.BARRIER);

            mundo.getBlockAt(loc).getRelative(BlockFace.EAST).setType(Material.BARRIER);
            Location loc3=new Location(mundo, mundo.getBlockAt(loc).getRelative(BlockFace.EAST).getLocation().getBlockX(), mundo.getBlockAt(loc).getRelative(BlockFace.EAST).getLocation().getBlockY()+2, mundo.getBlockAt(loc).getRelative(BlockFace.EAST).getLocation().getBlockZ());
            mundo.getBlockAt(loc3).setType(Material.BARRIER);
            
            
            mundo.getBlockAt(loc).getRelative(BlockFace.WEST).setType(Material.BARRIER);
            Location loc4=new Location(mundo, mundo.getBlockAt(loc).getRelative(BlockFace.WEST).getLocation().getBlockX(), mundo.getBlockAt(loc).getRelative(BlockFace.WEST).getLocation().getBlockY()+2, mundo.getBlockAt(loc).getRelative(BlockFace.WEST).getLocation().getBlockZ());
            mundo.getBlockAt(loc4).setType(Material.BARRIER);*/

            Location villager = new Location(mundo, loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch());

            EntityTypes.spawnEntity(new Villager(Bukkit.getWorld(mundo.getName())), villager);

        }
    }

    public Inventory abrirtienda() {
        ItemStack item = new ItemStack(Material.DIAMOND, 1);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName("hola");

        item.setItemMeta(meta);
        Inventory inv = Bukkit.getServer().createInventory(null, 54, "Shop");
        inv.setItem(0, new ItemStack(Material.NETHER_STAR, 1));
        inv.setItem(1, new ItemStack(Material.STAINED_CLAY, 1, (byte) 1));
        inv.setItem(2, new ItemStack(Material.GOLD_SWORD, 1));
        inv.setItem(3, new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        inv.setItem(4, new ItemStack(Material.STONE_PICKAXE, 1));
        inv.setItem(5, new ItemStack(Material.BOW, 1));
        inv.setItem(6, new ItemStack(Material.BREWING_STAND_ITEM, 1));
        inv.setItem(7, new ItemStack(Material.TNT, 1));

        ItemStack item1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);

        inv.setItem(9, new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 5));
        inv.setItem(10, item1);
        inv.setItem(11, item1);
        inv.setItem(12, item1);
        inv.setItem(13, item1);
        inv.setItem(14, item1);
        inv.setItem(15, item1);
        inv.setItem(16, item1);
        inv.setItem(17, item1);

        ItemStack item2 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        inv.setItem(19, item2);
        inv.setItem(20, new ItemStack(Material.STONE_SWORD));
        inv.setItem(21, new ItemStack(Material.CHAINMAIL_BOOTS));
        inv.setItem(22, item2);
        inv.setItem(23, new ItemStack(Material.BOW));
        inv.setItem(24, new ItemStack(Material.POTION, 1, (byte) 8227));
        inv.setItem(25, new ItemStack(Material.TNT));
        inv.setItem(28, new ItemStack(Material.WOOD, 16, (byte) 0));
        inv.setItem(29, new ItemStack(Material.IRON_SWORD));
        inv.setItem(30, new ItemStack(Material.IRON_BOOTS));
        inv.setItem(31, new ItemStack(Material.SHEARS));
        inv.setItem(32, new ItemStack(Material.ARROW, 8));
        inv.setItem(33, new ItemStack(Material.POTION, 1, (byte) 8226));
        inv.setItem(34, new ItemStack(Material.WATER_BUCKET));
        inv.setItem(37, item2);
        inv.setItem(38, item2);
        inv.setItem(39, item2);
        inv.setItem(40, item2);
        inv.setItem(41, item2);
        inv.setItem(42, item2);
        inv.setItem(43, item2);

        inv.setItem(53, new ItemStack(Material.BLAZE_POWDER));

        return inv;

    }

    public Inventory abrirmejoras() {
        
        Inventory inv = Bukkit.getServer().createInventory(null, 45, "Upgrades and Traps");

        inv.setItem(10, new ItemStack(Material.IRON_SWORD));
        inv.setItem(11, new ItemStack(Material.IRON_CHESTPLATE));
        inv.setItem(12, new ItemStack(Material.GOLD_PICKAXE));
        inv.setItem(13, new ItemStack(Material.FURNACE));
        inv.setItem(14, new ItemStack(Material.BEACON));
        inv.setItem(15, new ItemStack(Material.DRAGON_EGG));
        inv.setItem(16, new ItemStack(Material.LEATHER));

        ItemStack item1 = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 7);

        inv.setItem(18, item1);
        inv.setItem(19, item1);
        inv.setItem(20, item1);
        inv.setItem(21, item1);
        inv.setItem(22, item1);
        inv.setItem(23, item1);
        inv.setItem(24, item1);
        inv.setItem(25, item1);
        inv.setItem(26, item1);

        ItemStack item2 = new ItemStack(Material.STAINED_GLASS, 1, (byte) 8);
        inv.setItem(30, item2);
        inv.setItem(31, item2);
        inv.setItem(32, item2);
        

        return inv;

    }

}
