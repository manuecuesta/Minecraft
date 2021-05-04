/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7;

import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Jugador;
import com.gmail.cuestamanue7.juego.Partida;
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
public class Paintball extends JavaPlugin {

    PluginDescriptionFile pdfFile = getDescription();
    public String version = pdfFile.getVersion();
    public String prefix = ChatColor.translateAlternateColorCodes('&', "&9[&fPaintBall&9] ");
    private ArrayList<Partida> partidas = new ArrayList<>();
    private FileConfiguration arenas = null;
    private File arenasFile = null;

    @Override
    public void onEnable() {
        registerCommands();
        registerEvents();
        registerArenas();
        registerConfig();

        cargarPartidas();
        
        ScoreboardAdmin scoreboard = new ScoreboardAdmin(this);
        scoreboard.crearScoreboards();
        CartelesAdmin carteles = new CartelesAdmin(this);
        carteles.actualizarCarteles();
    }

    @Override
    public void onDisable() {
        if (partidas!=null) {
            for (int i=0; i<partidas.size(); i++) {
                if (!partidas.get(i).getEstadoPartida().equals(EstadoPartida.DESACTIVADA)) {
                    try {
                        PartidaManager.finalizarPartida(partidas.get(i), null, this, true);
                    } catch (IOException ex) {
                        Logger.getLogger(Paintball.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        this.guardarPartidas();
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
        for (int i = 0; i < partidas.size(); i++) {
            ArrayList<Jugador> jugadores = partidas.get(i).getJugadores();
            for (int j = 0; j < jugadores.size(); j++) {
                if (jugadores.get(j).getjugador().getName().equals(nombre)) {
                    return partidas.get(i);
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

    public void cargarPartidas() {
        this.partidas = new ArrayList<Partida>();
        FileConfiguration arenas = getArenas();
        if (arenas.contains("Arenas")) {
            for (String nombre : arenas.getConfigurationSection("Arenas").getKeys(false)) {
                int minPlayers = Integer.valueOf(arenas.getString("Arenas." + nombre + ".min_players"));
                int maxPlayers = Integer.valueOf(arenas.getString("Arenas." + nombre + ".max_players"));
                int time = Integer.valueOf(arenas.getString("Arenas." + nombre + ".time"));

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

                String nombreteam1 = arenas.getString("Arenas." + nombre + ".Team1.name");
                Location lspawnteam1 = null;

                if (arenas.contains("Arenas." + nombre + ".Team1.Spawn")) {
                    double xSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team1.Spawn.x"));
                    double ySpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team1.Spawn.y"));
                    double zSpawnTeam1 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team1.Spawn.z"));
                    float yawSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team1.Spawn.Yaw"));
                    float pitchSpawnTeam1 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team1.Spawn.Pitch"));
                    World worldspawnteam1 = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Team1.Spawn.world"));
                    lspawnteam1 = new Location(worldspawnteam1, xSpawnTeam1, ySpawnTeam1, zSpawnTeam1, yawSpawnTeam1, pitchSpawnTeam1);
                }

                String nombreteam2 = arenas.getString("Arenas." + nombre + ".Team2.name");
                Location lspawnteam2 = null;

                if (arenas.contains("Arenas." + nombre + ".Team2.Spawn")) {
                    double xSpawnTeam2 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team2.Spawn.x"));
                    double ySpawnTeam2 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team2.Spawn.y"));
                    double zSpawnTeam2 = Double.valueOf(arenas.getString("Arenas." + nombre + ".Team2.Spawn.z"));
                    float yawSpawnTeam2 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team2.Spawn.Yaw"));
                    float pitchSpawnTeam2 = Float.valueOf(arenas.getString("Arenas." + nombre + ".Team2.Spawn.Pitch"));
                    World worldspawnteam2 = Bukkit.getWorld(arenas.getString("Arenas." + nombre + ".Team2.Spawn.world"));
                    lspawnteam2 = new Location(worldspawnteam2, xSpawnTeam2, ySpawnTeam2, zSpawnTeam2, yawSpawnTeam2, pitchSpawnTeam2);
                }

                Partida partida = new Partida(nombre);
                partida.setcantidadmaxjugadores(maxPlayers);
                partida.setcantidadminjugadores(minPlayers);
                partida.setTiempomax(time);
                partida.setLobby(lobby);
                partida.getEquipo1().settipo(nombreteam1);
                partida.getEquipo1().setSpawn(lspawnteam1);
                partida.getEquipo2().setSpawn(lspawnteam2);
                partida.getEquipo2().settipo(nombreteam2);
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

            Location lspawnteam1 = p.getEquipo1().getSpawn();
            if (lspawnteam1 != null) {
                arenas.set("Arenas." + nombre + ".Team1.Spawn.x", lspawnteam1.getX());
                arenas.set("Arenas." + nombre + ".Team1.Spawn.y", lspawnteam1.getY());
                arenas.set("Arenas." + nombre + ".Team1.Spawn.z", lspawnteam1.getZ());
                arenas.set("Arenas." + nombre + ".Team1.Spawn.Yaw", lspawnteam1.getYaw());
                arenas.set("Arenas." + nombre + ".Team1.Spawn.Pitch", lspawnteam1.getPitch());
                arenas.set("Arenas." + nombre + ".Team1.Spawn.world", lspawnteam1.getWorld().getName());
            }
            arenas.set("Arenas." + nombre + ".Team1.name", p.getEquipo1().gettipo());

            Location lspawnteam2 = p.getEquipo2().getSpawn();
            if (lspawnteam1 != null) {
                arenas.set("Arenas." + nombre + ".Team2.Spawn.x", lspawnteam2.getX());
                arenas.set("Arenas." + nombre + ".Team2.Spawn.y", lspawnteam2.getY());
                arenas.set("Arenas." + nombre + ".Team2.Spawn.z", lspawnteam2.getZ());
                arenas.set("Arenas." + nombre + ".Team2.Spawn.Yaw", lspawnteam2.getYaw());
                arenas.set("Arenas." + nombre + ".Team2.Spawn.Pitch", lspawnteam2.getPitch());
                arenas.set("Arenas." + nombre + ".Team2.Spawn.world", lspawnteam2.getWorld().getName());
            }
            arenas.set("Arenas." + nombre + ".Team2.name", p.getEquipo2().gettipo());

            if (p.getEstadoPartida().equals(EstadoPartida.DESACTIVADA)) {
                arenas.set("Arenas." + nombre + ".enabled", false);
            } else {
                arenas.set("Arenas." + nombre + ".enabled", true);
            }
        }
        this.saveArenas();
    }

    public void registerCommands() {
        this.getCommand("paintball").setExecutor(new Comando(this));
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
            FileReader fr =new FileReader(getDataFolder()+"\\arenas.yml");
            BufferedReader bf = new BufferedReader(fr);
            if (bf != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(bf);
                arenas.setDefaults(defConfig);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Paintball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
