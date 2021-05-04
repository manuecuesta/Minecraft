/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

import com.gmail.cuestamanue7.Bedwars;
import com.gmail.cuestamanue7.juego.Equipo;
import com.gmail.cuestamanue7.juego.EstadoPartida;
import com.gmail.cuestamanue7.juego.Jugador;
import com.gmail.cuestamanue7.juego.Partida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.scheduler.BukkitScheduler;

/**
 *
 * @author Manuel
 */
public class CooldownManager {
    
    int taskID;
    int tiempo;
    private Partida partida;
    private Bedwars plugin;
    private int fase;
    
    public CooldownManager(Bedwars plugin) {
        this.plugin=plugin;
    }

    void cooldownComenzarJuego(Partida partida) {
        this.partida=partida;
        this.tiempo=10;
        fase=1;
        partida.setFase(1);
        partida.setTiempo(tiempo);
        String prefix= plugin.prefix;
        ArrayList<Jugador> jugadores = partida.getJugadores();
        for(int i=0; i<jugadores.size(); i++) {
            jugadores.get(i).getjugador().sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cEl juego empieza en "+this.tiempo+" segundos"));
        }
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            public void run() {
                if (!ejecutarComenzarJuego()) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);
    }
    
    protected boolean ejecutarComenzarJuego() {
        String prefix= plugin.prefix;
        if (partida!=null && partida.getEstadoPartida().equals(EstadoPartida.COMENZANDO)) {
            if (tiempo<=5 && tiempo >0){
                ArrayList<Jugador> jugadores = partida.getJugadores();
                for(int i=0; i<jugadores.size(); i++) {
                    jugadores.get(i).getjugador().sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cEl juego empieza en "+this.tiempo+" segundos"));
                }
                partida.disminuirtiempo();
                tiempo--;
                return true;
            } else if(tiempo<=0) {
                PartidaManager.iniciarPartida(partida, plugin);
                return false;
            } else {
                partida.disminuirtiempo();
                tiempo--;
                return true;                                
            }
        } else {
            
            ArrayList<Jugador> jugadores = partida.getJugadores();
            for(int i=0; i<jugadores.size(); i++) {
                jugadores.get(i).getjugador().sendMessage(prefix+ChatColor.translateAlternateColorCodes('&', "&cNo hay suficientes jugadores, cuenta atras cancelada"));
            }
            return false;
        }
    }

    void cooldownJuego(Partida partida) {
        this.partida=partida;
        this.tiempo=partida.getTiempomax();
        partida.setTiempo(tiempo);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            public void run() {
                if (!ejecutarJuego()) {
                    Bukkit.getScheduler().cancelTask(taskID);
                    return;
                }
            }
        }, 0L, 20L);        
    }
    
    protected boolean ejecutarJuego() {
        if (partida!=null && partida.getEstadoPartida().equals(EstadoPartida.JUGANDO)) {
            partida.disminuirtiempo();
            if (tiempo==0 && partida.getFase()==4){
                PartidaManager.iniciarfasefinalizacion(partida, plugin);
                return false;
            } else if (tiempo==0 && partida.getFase()==1){
                fase=2;
                partida.setFase(2);
                tiempo=360;
                partida.setTiempo(tiempo);
                partida.setLeveldiamond(2);
                partida.setFase(2);
                return true;
                                
            } else if (tiempo==0 && partida.getFase()==2){
                fase=3;
                partida.setFase(3);
                tiempo=360;
                partida.setTiempo(tiempo);
                partida.setLevelemerald(2);
                partida.setFase(3);
                return true;
                                
            } else if (tiempo==0 && partida.getFase()==3){
                fase=4;
                partida.setFase(4);
                tiempo=360;
                partida.setTiempo(tiempo);
                partida.setLeveldiamond(3);  
                partida.setFase(4);
                return true;
            }else {
                tiempo--;
                return true;
            }
    
        } else {
            return false;
        }
    }

    void cooldownfinJuego(Partida partida, Equipo ganador) {
        this.partida=partida;
        this.tiempo = 10;
        partida.setTiempo(tiempo);
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        taskID = scheduler.scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            public void run() {
                try {
                    if (!ejecutarfinJuego(ganador)) {
                        Bukkit.getScheduler().cancelTask(taskID);
                        partida.getLobby().getWorld().getEntities().stream().filter(Item.class::isInstance).forEach(Entity::remove);
                        PartidaManager.jugadoreliminadotolobby(partida, plugin);
                        return;
                    }
                } catch (IOException ex) {
                    Logger.getLogger(CooldownManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }, 0L, 20L);
        
        
        
    }
    
    protected boolean ejecutarfinJuego(Equipo ganador) throws IOException {
        if (partida!=null && partida.getEstadoPartida().equals(EstadoPartida.TERMINANDO)) {
            partida.disminuirtiempo();
            if (tiempo==0){
                PartidaManager.finalizarPartida(partida, ganador, plugin, false);
                return false;
            } else {
                tiempo--;
                if (ganador!=null) {
                    PartidaManager.lanzarFuegos(ganador.getJugadores());
                }
                return true;
            }
    
        } else {
            return false;
        }
    }
    
}
