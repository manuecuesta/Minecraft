package com.gmail.cuestamanue7.worldedit;

import com.gmail.cuestamanue7.Bedwars;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.mask.ExistingBlockMask;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.world.DataException;
import com.sk89q.worldedit.world.registry.WorldData;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;

/**
 *
 * @author Manuel
 */
public class Manager {

    public static void loadSchem(String filename, int x, int y, int z, World world, Bedwars plugin) throws FileNotFoundException {
        
        File dataDirectory = new File (plugin.getDataFolder(), "maps");
        if (!dataDirectory.exists()){
            dataDirectory.mkdirs();
        }
        File file = new File(dataDirectory, filename+".schematic"); // The schematic file
        Vector to = new Vector(x, y, z); // Where you want to paste
        com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
        WorldData worldData = weWorld.getWorldData();
        Clipboard clipboard;
        FileInputStream fil = new FileInputStream(file);
        try {
            clipboard = ClipboardFormat.SCHEMATIC.getReader(fil).read(worldData);
            fil.close();
            Extent source = clipboard;
            Extent destination = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
            ForwardExtentCopy copy = new ForwardExtentCopy(source, clipboard.getRegion(), clipboard.getOrigin(), destination, to);
            copy.setSourceMask(new ExistingBlockMask(clipboard));
            Operations.completeLegacy(copy);
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }   
    
    
    
    public static void saveSchem(String filename, int x1, int y1, int z1, int x2, int y2, int z2, World world, Bedwars plugin) {
        com.sk89q.worldedit.world.World weWorld = new BukkitWorld(world);
        WorldData worldData = weWorld.getWorldData();
        Vector pos1 = new Vector(x1, y1, z1); //First corner of your cuboid
        Vector pos2 = new Vector(x2, y2, z2); //Second corner fo your cuboid
        CuboidRegion cReg = new CuboidRegion(weWorld, pos1, pos2);
        File dataDirectory = new File (plugin.getDataFolder(), "maps");
        File file = new File(dataDirectory, filename + ".schematic"); // The schematic file
        try {
            BlockArrayClipboard clipboard = new BlockArrayClipboard(cReg);
            Extent source = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1);
            Extent destination = clipboard;
            ForwardExtentCopy copy = new ForwardExtentCopy(source, cReg, clipboard.getOrigin(), destination, pos1);
            copy.setSourceMask(new ExistingBlockMask(source));
            Operations.completeLegacy(copy);
            FileOutputStream file1=new FileOutputStream(file, false);
            ClipboardFormat.SCHEMATIC.getWriter(file1).write(clipboard, worldData);
            file1.close();
        } catch (IOException | MaxChangedBlocksException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
 
    
    public static void loadArea(World world, File file, Vector origin, EditSessionFactory edit) throws DataException, IOException, MaxChangedBlocksException {
        EditSession editSession = edit.getEditSession((com.sk89q.worldedit.world.World) new BukkitWorld(world), 999999999);
        MCEditSchematicFormat.getFormat(file).load(file).paste(editSession, origin, false);
    }

}
