/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import com.gmail.cuestamanue7.Bedwars;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Manuel
 */
public class Partida {

    private ArrayList<Equipo> equipos;
    private String nombre;
    private int cantmaxjugadores;
    private int cantminjugadores;
    private int cantactualjugadores;
    private EstadoPartida estado;
    private Location lobby;
    private int tiempo;
    private int tiempomax;
    private Location limite1;
    private Location limite2;
    private ArrayList<Generador> diamante;
    private ArrayList<Generador> esmeralda;
    private ArrayList<Generador> generadores;
    private ArrayList<Jugador> eliminados;
    private ArrayList<Location> tienda;
    private TipoPartida modo;
    private ArrayList<Location> bloques;
    private int leveldiamond;
    private int levelemerald;
    private int taskid;
    private int taskid1;
    private int taskid2;
    private int taskid3;
    private int taskidd;
    private int taskid11;
    private int taskid22;
    private int taskid33;
    private int fase;

    public Partida(String nombre) {
        //por defecto
        this.equipos = new ArrayList<Equipo>();
        this.diamante = new ArrayList<Generador>();
        this.esmeralda = new ArrayList<Generador>();
        this.generadores = new ArrayList<Generador>();
        this.eliminados = new ArrayList<Jugador>();
        this.tienda = new ArrayList<Location>();
        this.bloques = new ArrayList<>();
        this.nombre = nombre;
        this.cantactualjugadores = 0;
        this.cantminjugadores = 2;
        this.cantmaxjugadores = 8;
        this.estado = EstadoPartida.DESACTIVADA;
        this.tiempo = 0;
        this.tiempomax = 260;
        this.modo = TipoPartida.SOLO;
        this.leveldiamond = 1;
        this.levelemerald = 1;
        this.fase=1;
    }

    public int getFase() {
        return fase;
    }

    public void setFase(int fase) {
        this.fase = fase;
    }
    
    

    public int getLeveldiamond() {
        return leveldiamond;
    }

    public int getLevelemerald() {
        return levelemerald;
    }

    public void setLeveldiamond(int leveldiamond) {
        this.leveldiamond = leveldiamond;
    }

    public void setLevelemerald(int levelemerald) {
        this.levelemerald = levelemerald;
    }
    
    

    public ArrayList<Location> getBloques() {
        return bloques;
    }

    public void addBloques(Location block) {
        this.bloques.add(block);
    }

    public TipoPartida getModo() {
        return modo;
    }

    public void setModo(String modo) {
        if (modo.equals("SOLO")) {
            this.modo = TipoPartida.SOLO;
        } else if (modo.equals("DOUBLE")) {
            this.modo = TipoPartida.DOUBLE;
        } else {
            this.modo = TipoPartida.TRIPLE;
        }
    }

    public ArrayList<Location> getTienda() {
        return tienda;
    }

    public void addTienda(Location tienda) {
        this.tienda.add(tienda);
    }

    public ArrayList<Jugador> getEliminados() {
        return eliminados;
    }

    public void addEliminados(Jugador jugador) {
        this.eliminados.add(jugador);
    }

    public int getTiempomax() {
        return tiempomax;
    }

    public ArrayList<Generador> getdiamante() {
        return this.diamante;
    }

    public void adddiamante(Generador diamante) {
        this.diamante.add(diamante);
    }

    public ArrayList<Generador> getesmeralda() {
        return this.esmeralda;
    }

    public void addesmeralda(Generador esmeralda) {
        this.esmeralda.add(esmeralda);
    }

    public ArrayList<Generador> getgeneradores() {
        return this.generadores;
    }

    public void addgeneradores(Generador generador) {
        this.generadores.add(generador);
    }

    public void setTiempomax(int tiempomax) {
        this.tiempomax = tiempomax;
    }

    public void setlimite1(Location loc) {
        this.limite1 = loc;
    }

    public Location getlimite1() {
        return this.limite1;
    }

    public void setlimite2(Location loc) {
        this.limite2 = loc;
    }

    public Location getlimite2() {
        return this.limite2;
    }

    public void disminuirtiempo() {
        this.tiempo--;
    }

    public void aumentartiempo() {
        this.tiempo++;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void addequipo(Equipo equipo) {
        equipos.add(equipo);
    }

    public ArrayList<Equipo> getequipos() {
        return equipos;
    }

    public void agregarJugador(Jugador jugador) {
        if (equipos.get(0).agregarjugador(jugador)) {
            this.cantactualjugadores++;
        }
    }

    public void repartirJugadores() {
        int nequipos = equipos.size();
        int i = 0;
        ArrayList<Jugador> jugadores = (ArrayList<Jugador>) equipos.get(0).getJugadores().clone();
        for (Jugador jugador : jugadores) {
            equipos.get(0).eliminarJugador(jugador.getjugador().getName());
            equipos.get(i).agregarjugador(jugador);
            if (i == nequipos - 1) {
                i = 0;
            }
            i++;
        }

    }

    public void removerJugador(String jugador) {
        boolean encontrado = false;
        int i = 0;
        while (!encontrado) {
            if (equipos.get(i).eliminarJugador(jugador) && i < equipos.size()) {
                this.cantactualjugadores--;
                encontrado = true;
            }
            i++;
        }
    }

    public ArrayList<Jugador> getJugadores() {
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        for (int i = 0; i < equipos.size(); i++) {
            for (Jugador j : equipos.get(i).getJugadores()) {
                jugadores.add(j);
            }
        }
        return jugadores;
    }

    public Jugador getjugador(String nombre) {
        ArrayList<Jugador> jugadores = getJugadores();
        for (int i = 0; i < jugadores.size(); i++) {
            if (jugadores.get(i).getjugador().getName().equals(nombre)) {
                return jugadores.get(i);
            }
        }
        ArrayList<Jugador> jugadores1 = getEliminados();
        for (int i = 0; i < jugadores1.size(); i++) {
            if (jugadores1.get(i).getjugador().getName().equals(nombre)) {
                return jugadores1.get(i);
            }
        }
        return null;
    }

    public Equipo getequipojugador(String nombre) {

        for (int i = 0; i < equipos.size(); i++) {
            ArrayList<Jugador> jugadores = equipos.get(i).getJugadores();
            for (int j = 0; j < jugadores.size(); j++) {
                if (jugadores.get(j).getjugador().getName().equals(nombre)) {
                    return equipos.get(i);
                }
            }

        }
        return null;
    }

    public int getcantidadmaximajugadores() {
        return this.cantmaxjugadores;
    }

    public void setcantidadmaxjugadores(int max) {
        this.cantmaxjugadores = max;
    }

    public int getcantidadminimajugadores() {
        return this.cantminjugadores;
    }

    public void setcantidadminjugadores(int min) {
        this.cantminjugadores = min;
    }

    public int getcantidadactualjugadores() {
        return this.cantactualjugadores;
    }

    public void setcantidadactualjugadores(int actual) {
        this.cantactualjugadores = actual;
    }

    public EstadoPartida getEstadoPartida() {
        return this.estado;
    }

    public void setEstado(EstadoPartida estado) {
        this.estado = estado;
    }

    public boolean estaIniciada() {
        if (estado.equals(EstadoPartida.ESPERANDO) || estado.equals(EstadoPartida.DESACTIVADA) || estado.equals(EstadoPartida.COMENZANDO)) {
            return false;
        } else {
            return true;
        }
    }

    public boolean estallena() {
        if (this.cantactualjugadores == cantmaxjugadores) {
            return true;
        } else {
            return false;
        }
    }

    public boolean estaActivada() {
        if (!estado.equals(EstadoPartida.DESACTIVADA)) {
            return true;
        } else {
            return false;
        }
    }

    public void setLobby(Location l) {
        this.lobby = l;
    }

    public Location getLobby() {
        return this.lobby;
    }

    public Equipo getGanador() {
        Equipo ganador = null;
        ArrayList<Equipo> copia = (ArrayList<Equipo>) equipos.clone();
        for (int i = 0; i < copia.size(); i++) {
            if (!copia.get(i).isCama1()) {
                copia.remove(i);
            }
        }

        if (copia.size() == 1) {
            return copia.get(0);
        }

        return ganador;

    }

    public boolean jugadoressoloenunequipo() {
        ArrayList<Jugador> jugadores = getJugadores();
        for (int i = 0; i < equipos.size(); i++) {
            if (jugadores.size() == equipos.get(i).getcantidadjugadores() - 1) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Jugador> getJugadoresKills() {
        ArrayList<Jugador> jugadorescopia = (ArrayList<Jugador>) getJugadores().clone();
        for (int i = 0; i < jugadorescopia.size(); i++) {
            for (int c = i + 1; c < jugadorescopia.size(); c++) {
                if (jugadorescopia.get(i).getasesinatos() < jugadorescopia.get(c).getasesinatos()) {
                    Jugador aux = jugadorescopia.get(i);
                    jugadorescopia.set(i, jugadorescopia.get(c));
                    jugadorescopia.set(c, aux);
                }

            }
        }
        return jugadorescopia;
    }

    public static void drop(World mundo, ItemStack item, Location l) {
        Location loc = new Location(l.getWorld(), l.getBlockX() + 0.5, l.getBlockY(), l.getBlockZ() + 0.5);
        mundo.dropItem(loc, item);
    }

    public ArmorStand hologramdrop(ItemStack item, Location l, ItemStack cabeza, TipoGenerador tipo) {
        Location loc = new Location(l.getWorld(), l.getBlockX() + 0.5, l.getBlockY() + 1, l.getBlockZ() + 0.5);
        ArmorStand as = (ArmorStand) l.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND); //Spawn the ArmorStand
        as.setGravity(false); //Make sure it doesn't fall
        as.setCanPickupItems(false); //I'm not sure what happens if you leave this as it is, but you might as well disable it
        if (tipo.equals(TipoGenerador.NORMAL)) {
            as.setCustomName(ChatColor.translateAlternateColorCodes('&', ChatColor.translateAlternateColorCodes('&', "&cIron Generator"))); //Set this to the text you want
        } else if (tipo.equals(TipoGenerador.DIAMANTE)) {
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

    public void dropspawniron(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.IRON_INGOT, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();
        ArrayList<Generador> generador1 = partida.getgeneradores();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.IRON_BLOCK), TipoGenerador.NORMAL));
        }
        taskidd = Bukkit.getScheduler().scheduleSyncRepeatingTask(bedwars, new BukkitRunnable() {
            boolean previousLoopDone = true;

            @Override
            public void run() {
                if (previousLoopDone) {
                    previousLoopDone = false;
                    taskid = Bukkit.getScheduler().scheduleSyncDelayedTask(bedwars, new Runnable() {
                        public void run() {
                            //smthg
                            previousLoopDone = true;

                            if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                                for (int i = 0; i < generador1.size(); i++) {
                                    as.get(i).remove();
                                }
                                Bukkit.getScheduler().cancelTask(taskid);
                                Bukkit.getScheduler().cancelTask(taskidd);
                            }
                            for (int i = 0; i < generador1.size(); i++) {
                                drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                            }

                        }
                    }, (int) (20 / 1));
                }
            }
        }, 0, 1);
    }

    public void dropspawngold(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.GOLD_INGOT, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();
        ArrayList<Generador> generador1 = partida.getgeneradores();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.IRON_BLOCK), TipoGenerador.NORMAL));
        }

        taskid11 = Bukkit.getScheduler().scheduleSyncRepeatingTask(bedwars, new BukkitRunnable() {
            boolean previousLoopDone = true;

            @Override
            public void run() {
                if (previousLoopDone) {
                    previousLoopDone = false;
                    taskid1 = Bukkit.getScheduler().scheduleSyncDelayedTask(bedwars, new Runnable() {
                        public void run() {
                            //smthg
                            previousLoopDone = true;
                            if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                                for (int i = 0; i < generador1.size(); i++) {
                                    as.get(i).remove();
                                }
                                Bukkit.getScheduler().cancelTask(taskid1);
                                Bukkit.getScheduler().cancelTask(taskid11);

                            }
                            for (int i = 0; i < generador1.size(); i++) {
                                drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                            }

                        }
                    }, (int) (40 / 1));
                }
            }
        }, 0, 1);

    }

    public void dropdiamante(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.DIAMOND, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();;
        ArrayList<Generador> generador1 = partida.getdiamante();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.DIAMOND_BLOCK), TipoGenerador.DIAMANTE));
        }
        taskid22 = Bukkit.getScheduler().scheduleSyncRepeatingTask(bedwars, new BukkitRunnable() {
            boolean previousLoopDone = true;

            @Override
            public void run() {
                if (previousLoopDone) {
                    previousLoopDone = false;
                    taskid2 = Bukkit.getScheduler().scheduleSyncDelayedTask(bedwars, new Runnable() {
                        public void run() {
                            //smthg
                            previousLoopDone = true;
                            for (int i = 0; i < generador1.size(); i++) {
                                drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                            }
                            if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                                for (int i = 0; i < generador1.size(); i++) {
                                    as.get(i).remove();
                                }
                                Bukkit.getScheduler().cancelTask(taskid2);
                                Bukkit.getScheduler().cancelTask(taskid22);
                            }
                        }
                    }, (int) (600 / leveldiamond));
                }
            }
        }, 0, 1);
    }

    public void dropesmeralda(Bedwars bedwars, Partida partida) {
        ItemStack iron = new ItemStack(Material.EMERALD, 1);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        ArrayList<ArmorStand> as = new ArrayList<ArmorStand>();
        ArrayList<Generador> generador1 = partida.getesmeralda();
        for (int i = 0; i < generador1.size(); i++) {
            as.add(hologramdrop(iron, generador1.get(i).getLoc(), new ItemStack(Material.EMERALD_BLOCK), TipoGenerador.ESMERALDA));
        }
        taskid33 = Bukkit.getScheduler().scheduleSyncRepeatingTask(bedwars, new BukkitRunnable() {
            boolean previousLoopDone = true;

            @Override
            public void run() {
                if (previousLoopDone) {
                    previousLoopDone = false;
                    taskid3 = Bukkit.getScheduler().scheduleSyncDelayedTask(bedwars, new Runnable() {
                        public void run() {
                            //smthg
                            previousLoopDone = true;
                            for (int i = 0; i < generador1.size(); i++) {
                                drop(generador1.get(i).getLoc().getWorld(), iron, generador1.get(i).getLoc());
                            }
                            if (!partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
                                for (int i = 0; i < generador1.size(); i++) {
                                    as.get(i).remove();
                                }
                                Bukkit.getScheduler().cancelTask(taskid3);
                                Bukkit.getScheduler().cancelTask(taskid33);
                            }
                        }
                    }, (int) (1200 / levelemerald));
                }
            }
        }, 0, 1);
    }

    public void activargeneradores(Partida p, Bedwars plugin) {
        dropdiamante(plugin, p);
        dropesmeralda(plugin, p);
        dropspawngold(plugin, p);
        dropspawniron(plugin, p);
    }

}
