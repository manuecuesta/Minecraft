/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import org.bukkit.Location;
import java.util.ArrayList;
import org.bukkit.Color;

/**
 *
 * @author Manuel
 */
public class Equipo {
    
    private ArrayList<Jugador> jugadores;
    private String tipo;
    private Location spawn;
    private Location cama;
    private boolean cama1;
    private Color color;
    private int nivelgenerador;
    
    public Equipo(String tipo) {
        this.jugadores=new java.util.ArrayList<Jugador>();
        this.tipo = tipo;
        this.cama1=true;
        this.color=null;
        this.nivelgenerador=4;
    }

    public int getNivelgenerador() {
        return nivelgenerador;
    }

    public void setNivelgenerador(int nivelgenerador) {
        this.nivelgenerador = nivelgenerador;
    }
    
    
    
    public  Color getcolor() {
        return this.color;
    }
    
    public void setcolor(String color) {
        switch(color) {
            case "BLUE":
                this.color=Color.BLUE;
                break;
            case "GREEN":
                this.color=Color.GREEN;
                break;
            case "RED":
                this.color=Color.RED;
                break;
            case "YELLOW":
                this.color=Color.YELLOW;
                break;
            case "ORANGE":
                this.color=Color.ORANGE;
                break;
            case "PURPLE":
                this.color=Color.PURPLE;
                break;
            case "BLACK":
                this.color=Color.BLACK;
                break;
            case "WHITE":
                this.color=Color.WHITE;
                break;
        }
    }
    

    public boolean isCama1() {
        return cama1;
    }

    public void setCama1(boolean cama1) {
        this.cama1 = cama1;
    }

    public Location getCama() {
        return cama;
    }

    public void setCama(Location cama) {
        this.cama = cama;
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
