/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Sign;

/**
 *
 * @author Manuel
 */
public class CristalCarteles {

    public Block ponerbloque(Block bloque) {
        if (bloque.getRelative(BlockFace.SOUTH).getType() != Material.AIR && !bloque.getRelative(BlockFace.SOUTH).getType().name().contains("SIGN")) {
            return bloque.getRelative(BlockFace.SOUTH);
        } else if (bloque.getRelative(BlockFace.NORTH).getType() != Material.AIR && !bloque.getRelative(BlockFace.NORTH).getType().name().contains("SIGN")) {
            return bloque.getRelative(BlockFace.NORTH);
        } else if (bloque.getRelative(BlockFace.EAST).getType() != Material.AIR && !bloque.getRelative(BlockFace.EAST).getType().name().contains("SIGN")) {
            return bloque.getRelative(BlockFace.EAST);
        } else if (bloque.getRelative(BlockFace.WEST).getType() != Material.AIR && !bloque.getRelative(BlockFace.WEST).getType().name().contains("SIGN")) {
            return bloque.getRelative(BlockFace.WEST);
        }
        return null;
    }

    public static Block obtenerbloque(Block bloque) {
        if (bloque.getType() == Material.WALL_SIGN) {
            Sign s = (Sign) bloque.getState().getData();
            Block bloqe = bloque.getRelative(s.getAttachedFace());
            return  bloqe;
        }
        return null;
    }
}
