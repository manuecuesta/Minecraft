package com.gmail.cuestamanue7.worldedit;

import com.gmail.cuestamanue7.Paintball;
import com.google.common.io.Closer;
import com.sk89q.jnbt.CompoundTag;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.EditSessionFactory;
import com.sk89q.worldedit.EmptyClipboardException;
import com.sk89q.worldedit.LocalConfiguration;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardWriter;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.io.file.FilenameException;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.registry.WorldData;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class Manager {

    public static void saveSchem(String filename, int x1, int y1, int z1, int x2, int y2, int z2, org.bukkit.World world, Paintball plugin) {
        BukkitWorld weWorld = new BukkitWorld(world);
        WorldData worldData = weWorld.getWorldData();
        Vector pos1 = new Vector(x1, y1, z1); //First corner of your cuboid
        Vector pos2 = new Vector(x2, y2, z2); //Second corner fo your cuboid
        CuboidRegion cReg = new CuboidRegion(weWorld, pos1, pos2);
        File dataDirectory = new File(plugin.getDataFolder(), "maps");
        File file = new File(dataDirectory, filename+".schematic"); // The schematic file
        try {
            BlockArrayClipboard clipboard = new BlockArrayClipboard(cReg);
            Extent source = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
            Extent destination = clipboard;
            ForwardExtentCopy copy = new ForwardExtentCopy(source, cReg, clipboard.getOrigin(), destination, pos1);
            copy.setSourceMask(new ExistingBlockMask(source));
            Operations.completeLegacy(copy);
            ClipboardFormat.SCHEMATIC.getWriter(new FileOutputStream(file)).write(clipboard, worldData);
            
        } catch (IOException | MaxChangedBlocksException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public static void loadSchem(String filename, int x, int y, int z, org.bukkit.World world, Paintball plugin, Player player) throws MaxChangedBlocksException, FileNotFoundException, DataException {
        File dataDirectory = new File(plugin.getDataFolder(), "maps");
        File file = new File(dataDirectory, filename+".schematic"); // The schematic file
        Vector to = new Vector(x, y, z); // Where you want to paste
        BukkitWorld weWorld = new BukkitWorld(world);
        WorldData worldData = weWorld.getWorldData();
        Clipboard clipboard;
        try {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Mapa pegado correctamente 1"));
            clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(file)).read(worldData);
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, to);
            copy.setSourceMask(new ExistingBlockMask(clipboard));
            loadArea(weWorld.getWorld(), file, to, WorldEdit.getInstance().getEditSessionFactory());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "Mapa pegado correctamente"));
            Operations.completeLegacy(copy);
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }
 
    
    public static void loadArea(World world, File file, Vector origin, EditSessionFactory edit) throws DataException, IOException, MaxChangedBlocksException {
        EditSession editSession = edit.getEditSession((com.sk89q.worldedit.world.World) new BukkitWorld(world), 999999999);
        MCEditSchematicFormat.getFormat(file).load(file).paste(editSession, origin, false);
    }

}
