/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7;

import com.gmail.cuestamanue7.juego.Equipo;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Generador;
import com.gmail.cuestamanue7.juego.Jugador;
import com.gmail.cuestamanue7.juego.Partida;
import com.gmail.cuestamanue7.juego.TipoGenerador;
import com.gmail.cuestamanue7.managers.CartelesAdmin;
import com.gmail.cuestamanue7.managers.CartelesListeners;
import com.gmail.cuestamanue7.managers.PartidaListener;
import com.gmail.cuestamanue7.managers.PartidaManager;
import com.gmail.cuestamanue7.managers.ScoreboardAdmin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Manuel
 */
public class Bedwars extends JavaPlugin {

    PluginDescriptionFile pdfFile = getDescription();
    public String version = pdfFile.getVersion();
    public String prefix = ChatColor.translateAlternateColorCodes('&', "&9[&fBedWars&9] ");
    private ArrayList<Partida> partidas = new ArrayList<>();
    private FileConfiguration arenas = null;
    private File arenasFile = null;
    private FileConfiguration messages = null;
    private File messagesFile = null;

    @Override
    public void onEnable() {
        try {
            registerCommands();
            registerEvents();
            registerArenas();
            registerConfig();
            registermessages();
            cargarPartidas();

            ScoreboardAdmin scoreboard = new ScoreboardAdmin(this);
            scoreboard.crearScoreboards();
            CartelesAdmin carteles = new CartelesAdmin(this);
            carteles.actualizarCarteles();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onDisable() {
        if (partidas != null) {
            for (int i = 0; i < partidas.size(); i++) {
                if (!partidas.get(i).getEstadoPartida().equals(EstadoPartida.DESACTIVADA)) {
                    try {
                        PartidaManager.finalizarPartida(partidas.get(i), null, this, true);
                    } catch (IOException ex) {
                        Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        /*
        for (int i = 0; i < partidas.size(); i++) {
            Bukkit.getServer().unloadWorld(partidas.get(i).getNombre(), true);
            borrarWorld(partidas.get(i).getNombre());
        }*/
        this.guardarPartidas();
    }

    public boolean borrarWorld(String worldName) {
        File path = new File(worldName);
        if (path.exists()) {
            for (File file : path.listFiles()) {
                if (file.isDirectory()) {
                    borrarWorld(worldName + "\\" + file.getName());
                } else {
                    file.delete();
                }
            }
        }
        return (path.delete());
    }

    public void agregarPartida(Partida partida) {
        partidas.add(partida);
    }

    public Partida getPartida(String nombre) {
        for (int i = 0; i < partidas.size(); i++) {
            if (partidas.get(i).getNombre().equals(nombre)) {
                return partidas.get(i);
            }
        }
        return null;
    }

    public ArrayList<Partida> getPartidas() {
        return this.partidas;
    }

    public Partida getPartidaJugador(String nombre) {
        if (partidas.size() > 0) {
            for (int i = 0; i < partidas.size(); i++) {
                ArrayList<Jugador> jugadores = partidas.get(i).getJugadores();
                if (jugadores != null) {
                    for (int j = 0; j < jugadores.size(); j++) {
                        if (jugadores.get(j).getjugador().getName().equals(nombre)) {
                            return partidas.get(i);
                        }
                    }
                }
                
                ArrayList<Jugador> eliminados = partidas.get(i).getEliminados();
                if (eliminados != null) {
                    for (int j = 0; j < eliminados.size(); j++) {
                        if (eliminados.get(j).getjugador().getName().equals(nombre)) {
                            return partidas.get(i);
                        }
                    }
                }
                
            }

        }
        return null;
    }

    public Partida getPartidamundo(World world) {
        for (int i = 0; i < partidas.size(); i++) {
            if (partidas != null) {
                for (int j = 0; j < partidas.size(); j++) {
                    if (partidas.get(i).getLobby().getWorld() == world) {
                        return partidas.get(i);
                    }
                }
            }
        }
        return null;
    }

    public void removerPartida(String nombre) {
        for (int i = 0; i < partidas.size(); i++) {
            if (partidas.get(i).getNombre().equals(nombre)) {
                partidas.remove(i);
            }
        }
    }

    public void cargarPartidas() throws FileNotFoundException {
        this.partidas = new ArrayList<Partida>();
        FileConfiguration arenas = getArenas();
        if (arenas.contains("Arenas")) {
            for (String nombre : arenas.getConfigurationSection("Arenas").getKeys(false)) {
                //CrearArena.crearmundo(nombre);
                //Manager.loadSchem(nombre, 10000, 100, 10000, Bukkit.getWorld(nombre), this);
                int minPlayers = Integer.valueOf(arenas.getString("Arenas." + nombre + ".min_players"));
                int maxPlayers = Integer.valueOf(arenas.getString("Arenas." + nombre + ".max_players"));
                int time = Integer.valueOf(arenas.getString("Arenas." + nombre + ".time"));
                
                Partida partida = new Partida(nombre);
                Location lobby = null;

                if (arenas.contains("Arenas." + nombre + ".Lobby")) {
                    double xLobby = Double.valueOf(arenas.getString("Arenas." + nombre + ".Lobby.x"));
                    double yLobby = Double.valueOf(arenas.getString("Arenas." + nombre + ".Lobby.y"));
                    double zLobby = Double.valueOf(arenas.getString("Arenas." + nombre + ".Lobby.z"));
                    float yawLobby = Float.valueOf(arenas.getString("Arenas." + nombre + ".Lobby.Yaw"));
                    float PitchLobby = Float.valueOf(arenas.getString("Arenas." + nombre + ".Lobby.Pitch"));
                    World worldLobby = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Lobby.world"));
                    lobby = new Location(worldLobby, xLobby, yLobby, zLobby, yawLobby, PitchLobby);
                }
                
                int i=0;
                while (arenas.getString("Arenas." + nombre + ".Team." + i + ".name") != null) {
                    Location lspawnteam1 = null;
                    Location lcama = null;
                    if (arenas.contains("Arenas." + nombre + ".Team." + i + ".Spawn")) {
                        double xSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Spawn.x"));
                        double ySpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Spawn.y"));
                        double zSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Spawn.z"));
                        float yawSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Spawn.Yaw"));
                        float pitchSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Spawn.Pitch"));
                        World worldspawnteam1 = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Team." + i + ".Spawn.world"));
                        double xbed = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Bed.x"));
                        double ybed = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Bed.y"));
                        double zbed = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Bed.z"));
                        float yawbed = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Bed.Yaw"));
                        float pitchbed = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team." + i + ".Bed.Pitch"));
                        World worldbed = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Team." + i + ".Bed.world"));
                        lspawnteam1 = new Location(worldspawnteam1, xSpawnTeam1, ySpawnTeam1, zSpawnTeam1, yawSpawnTeam1, pitchSpawnTeam1);
                        lcama = new Location(worldbed, xbed, ybed, zbed, yawbed, pitchbed);
                        String nombreequipo = arenas.getString("Arenas." + nombre + ".Team." + i + ".name");
                        Equipo e = new Equipo(nombreequipo);
                        e.setSpawn(lspawnteam1);
                        e.setCama(lcama);
                        e.setcolor(nombreequipo);
                        partida.addequipo(e);
                    }
                    i++;
                }
                
                i = 0;
                while (arenas.getString("Arenas." + nombre + ".Generator_iron." + i) != null) {
                    double xSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_iron." + i + ".x"));
                    double ySpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_iron." + i + ".y"));
                    double zSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_iron." + i + ".z"));
                    World worldspawnteam1 = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Generator_iron." + i + ".world"));
                    String equipo = arenas.getString("Arenas." + nombre + ".Generator_iron." + i + ".name");
                    Location loc=new Location(worldspawnteam1, xSpawnTeam1, ySpawnTeam1, zSpawnTeam1);
                    Generador g=new Generador(loc, partida, TipoGenerador.NORMAL);
                    for (int j=0; j<partida.getequipos().size(); j++) {
                        if (partida.getequipos().get(j).gettipo().equals(nombre)) {
                            g.setEquipo(partida.getequipos().get(i));
                        }
                    }
                    partida.addgeneradores(g);
                    i++;
                }
                
                i = 0;
                while (arenas.getString("Arenas." + nombre + ".Generator_emerald." + i) != null) {
                    double xSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_emerald." + i + ".x"));
                    double ySpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_emerald." + i + ".y"));
                    double zSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_emerald." + i + ".z"));
                    World worldspawnteam1 = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Generator_emerald." + i + ".world"));
                    
                    Location loc=new Location(worldspawnteam1, xSpawnTeam1, ySpawnTeam1, zSpawnTeam1);
                    Generador g=new Generador(loc, partida, TipoGenerador.ESMERALDA);
                    partida.addesmeralda(g);
                    i++;
                }
                i=0;
                while (arenas.getString("Arenas." + nombre + ".Generator_diamond." + i) != null) {
                    double xSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_diamond." + i + ".x"));
                    double ySpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_diamond." + i + ".y"));
                    double zSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Generator_diamond." + i + ".z"));
                    World worldspawnteam1 = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Generator_diamond." + i + ".world"));
                    
                    Location loc=new Location(worldspawnteam1, xSpawnTeam1, ySpawnTeam1, zSpawnTeam1);
                    Generador g=new Generador(loc, partida, TipoGenerador.DIAMANTE);
                    partida.adddiamante(g);
                    i++;
                }
                i=0;
                while (arenas.getString("Arenas." + nombre + ".Shop." + i) != null) {
                    double xSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Shop." + i + ".x"));
                    double ySpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Shop." + i + ".y"));
                    double zSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Shop." + i + ".z"));
                    float yawSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Shop." + i + ".Yaw"));
                    float pitchSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Shop." + i + ".Pitch"));
                    World worldspawnteam1 = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Shop." + i + ".world"));
                    
                    Location loc=new Location(worldspawnteam1, xSpawnTeam1, ySpawnTeam1, zSpawnTeam1, yawSpawnTeam1, pitchSpawnTeam1);
                    partida.addTienda(loc);
                    i++;
                }
                
                String nombreteam1 = arenas.getString("Arenas." + nombre + ".Team." + i + ".name");

                partida.setcantidadmaxjugadores(maxPlayers);
                partida.setcantidadminjugadores(minPlayers);
                partida.setTiempomax(time);
                partida.setLobby(lobby);
                String enabled = arenas.getString("Arenas." + nombre + ".enabled");
                if (enabled.equals("false")) {
                    partida.setEstado(EstadoPartida.DESACTIVADA);
                } else {
                    partida.setEstado(EstadoPartida.ESPERANDO);
                }
                this.partidas.add(partida);
            }
        }

    }

    public void guardarPartidas() {
        FileConfiguration arenas = getArenas();
        arenas.set("Arenas", null);
        for (Partida p : this.partidas) {
            String nombre = p.getNombre();
            arenas.set("Arenas." + nombre + ".min_players", p.getcantidadminimajugadores());
            arenas.set("Arenas." + nombre + ".max_players", p.getcantidadmaximajugadores());
            arenas.set("Arenas." + nombre + ".time", p.getTiempomax());
            Location lobby = p.getLobby();
            if (lobby != null) {
                arenas.set("Arenas." + nombre + ".Lobby.x", lobby.getX());
                arenas.set("Arenas." + nombre + ".Lobby.y", lobby.getY());
                arenas.set("Arenas." + nombre + ".Lobby.z", lobby.getZ());
                arenas.set("Arenas." + nombre + ".Lobby.Yaw", lobby.getYaw());
                arenas.set("Arenas." + nombre + ".Lobby.Pitch", lobby.getPitch());
                arenas.set("Arenas." + nombre + ".Lobby.world", lobby.getWorld().getName());
            }

            ArrayList<Equipo> equipos = p.getequipos();

            for (int i = 0; i < equipos.size(); i++) {
                Location lspawn = equipos.get(i).getSpawn();
                Location cama = equipos.get(i).getCama();
                if (lspawn != null) {
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Spawn.x", lspawn.getX());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Spawn.y", lspawn.getY());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Spawn.z", lspawn.getZ());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Spawn.Yaw", lspawn.getYaw());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Spawn.Pitch", lspawn.getPitch());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Spawn.world", lspawn.getWorld().getName());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".name", equipos.get(i).gettipo());
                }
                if (cama != null) {

                    arenas.set("Arenas." + nombre + ".Team." + i + ".Bed.x", cama.getX());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Bed.y", cama.getY());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Bed.z", cama.getZ());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Bed.Yaw", cama.getYaw());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Bed.Pitch", cama.getPitch());
                    arenas.set("Arenas." + nombre + ".Team." + i + ".Bed.world", cama.getWorld().getName());

                }
            }

            ArrayList<Generador> generadoresiron = p.getgeneradores();
            for (int i = 0; i < generadoresiron.size(); i++) {
                arenas.set("Arenas." + nombre + ".Generator_iron." + i + ".x", generadoresiron.get(i).getLoc().getX());
                arenas.set("Arenas." + nombre + ".Generator_iron." + i + ".y", generadoresiron.get(i).getLoc().getY());
                arenas.set("Arenas." + nombre + ".Generator_iron." + i + ".z", generadoresiron.get(i).getLoc().getZ());
                arenas.set("Arenas." + nombre + ".Generator_iron." + i + ".world", generadoresiron.get(i).getLoc().getWorld().getName());
                arenas.set("Arenas." + nombre + ".Generator_iron." + i + ".Team", generadoresiron.get(i).getEquipo().gettipo());
            }

            ArrayList<Generador> generadoresesmeralda = p.getesmeralda();
            for (int i = 0; i < generadoresesmeralda.size(); i++) {
                arenas.set("Arenas." + nombre + ".Generator_emerald." + i + ".x", generadoresesmeralda.get(i).getLoc().getX());
                arenas.set("Arenas." + nombre + ".Generator_emerald." + i + ".y", generadoresesmeralda.get(i).getLoc().getY());
                arenas.set("Arenas." + nombre + ".Generator_emerald." + i + ".z", generadoresesmeralda.get(i).getLoc().getZ());
                arenas.set("Arenas." + nombre + ".Generator_emerald." + i + ".world", generadoresesmeralda.get(i).getLoc().getWorld().getName());
            }

            ArrayList<Generador> generadordiamantes = p.getdiamante();
            for (int i = 0; i < generadordiamantes.size(); i++) {
                arenas.set("Arenas." + nombre + ".Generator_diamond." + i + ".x", generadoresesmeralda.get(i).getLoc().getX());
                arenas.set("Arenas." + nombre + ".Generator_diamond." + i + ".y", generadoresesmeralda.get(i).getLoc().getY());
                arenas.set("Arenas." + nombre + ".Generator_diamond." + i + ".z", generadoresesmeralda.get(i).getLoc().getZ());
                arenas.set("Arenas." + nombre + ".Generator_diamond." + i + ".world", generadoresesmeralda.get(i).getLoc().getWorld().getName());
            }
            
            ArrayList<Location> tiendas = p.getTienda();
            for (int i = 0; i < tiendas.size(); i++) {
                arenas.set("Arenas." + nombre + ".Shop." + i + ".x", tiendas.get(i).getX());
                arenas.set("Arenas." + nombre + ".Shop." + i + ".y", tiendas.get(i).getY());
                arenas.set("Arenas." + nombre + ".Shop." + i + ".z", tiendas.get(i).getZ());
                arenas.set("Arenas." + nombre + ".Shop." + i + ".Yaw", tiendas.get(i).getYaw());
                arenas.set("Arenas." + nombre + ".Shop." + i + ".Pitch", tiendas.get(i).getPitch());
                arenas.set("Arenas." + nombre + ".Shop." + i + ".world", tiendas.get(i).getWorld().getName());
            }

            if (p.getEstadoPartida().equals(EstadoPartida.DESACTIVADA)) {
                arenas.set("Arenas." + nombre + ".enabled", false);
            } else {
                arenas.set("Arenas." + nombre + ".enabled", true);
            }
        }
        this.saveArenas();
    }

    public void registerCommands() {
        this.getCommand("bedwars").setExecutor(new Comando(this));
    }

    public void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PartidaListener(this), this);
        pm.registerEvents(new CartelesListeners(this), this);

    }

    public void registerConfig() {
        File config = new File(this.getDataFolder(), "config.yml");
        if (!config.exists()) {
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
    }
    
    public void crearmessages() {
        
        File existe = new File(getDataFolder() + "\\messages.yml");
        FileConfiguration messages = getmessages();
        messages.set("BedWars", null);
        
        if (existe.exists()) {
            
        } else {
            try {
                existe.createNewFile();
                messages.set("BedWars." + ".join-arena", "joins the arena");
                messages.set("BedWars." + ".leave-arena", "leaves the arena");
                messages.set("BedWars." + ".already-in-arena", "You are in a game");
            } catch (IOException ex) {
                Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public void reloadmessages() {
        if (messages == null) {
            messagesFile = new File(getDataFolder(), "messages.yml");
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        Reader defConfigStream;
        try {
            File existe = new File(getDataFolder() + "\\messages.yml");
            if (existe.exists()) {
                FileReader fr = new FileReader(getDataFolder() + "\\messages.yml");

                BufferedReader bf = new BufferedReader(fr);
                if (bf != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(bf);
                    messages.setDefaults(defConfig);
                }
            } else {
                crearmessages();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void registermessages() {
        messagesFile = new File(this.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            this.getmessages().options().copyDefaults(true);
            savemessages();
        }
    }

    public void savemessages() {
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getmessages() {
        if (messages == null) {
            reloadmessages();
        }
        return messages;
    }
    

    public void registerArenas() {
        arenasFile = new File(this.getDataFolder(), "arenas.yml");
        if (!arenasFile.exists()) {
            this.getArenas().options().copyDefaults(true);
            saveArenas();
        }
    }

    public void saveArenas() {
        try {
            arenas.save(arenasFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getArenas() {
        if (arenas == null) {
            reloadArenas();
        }
        return arenas;
    }

    public void reloadArenas() {
        if (arenas == null) {
            arenasFile = new File(getDataFolder(), "arenas.yml");
        }
        arenas = YamlConfiguration.loadConfiguration(arenasFile);

        Reader defConfigStream;
        try {
            File existe = new File(getDataFolder() + "\\arenas.yml");
            if (existe.exists()) {
                FileReader fr = new FileReader(getDataFolder() + "\\arenas.yml");

                BufferedReader bf = new BufferedReader(fr);
                if (bf != null) {
                    YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(bf);
                    arenas.setDefaults(defConfig);
                }
            } else {
                existe.createNewFile();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Bedwars.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
