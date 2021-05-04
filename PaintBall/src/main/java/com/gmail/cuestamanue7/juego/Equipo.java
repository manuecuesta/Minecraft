/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import org.bukkit.Location;
import java.util.ArrayList;

/**
 *
 * @author Manuel
 */
public class Equipo {
    
    private ArrayList<Jugador> jugadores;
    private String tipo;
    private Location spawn;
    
    public Equipo(String tipo) {
        this.jugadores=new java.util.ArrayList<Jugador>();
        this.tipo = tipo;
    }
    
    public void settipo(String tipo) {
        this.tipo=tipo;
    }
    
    public String gettipo() {
        return this.tipo;
    }
    
    public boolean contieneJugador(String jugador) {
        for(int i=0; i<jugadores.size(); i++) {
            if(jugadores.get(i).getjugador().getName().equals(jugador)) {
                return true;
            } 
        }
        return false;
    }
    
    public boolean agregarjugador(Jugador jugador) {
        if (!contieneJugador(jugador.getjugador().getName())) {
            this.jugadores.add(jugador);
            return true;
        } else return false;
    }
    
    public boolean eliminarJugador(String jugador) {
        for(int i=0; i<jugadores.size(); i++) {
            if(jugadores.get(i).getjugador().getName().equals(jugador)) {
                this.jugadores.remove(i);
                return true;
            } 
        }
        return false;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }

    public Location getSpawn() {
        return spawn;
    }

    public void setSpawn(Location spawn) {
        this.spawn = spawn;
    }
    
    public int getcantidadjugadores() {
        return this.jugadores.size();
    }
    
    public int getasesinatostotales() {
        int asesintostotales=0;
        for (int i=0; i< jugadores.size(); i++) {
            asesintostotales+=jugadores.get(i).getasesinatos();
        }
        return asesintostotales;
    }
    
    
}
