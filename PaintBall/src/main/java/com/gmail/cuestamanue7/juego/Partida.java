/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import java.util.ArrayList;
import java.util.Random;
import org.bukkit.Location;

/**
 *
 * @author Manuel
 */
public class Partida {

    private Equipo equipo1;
    private Equipo equipo2;
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

    public Partida(String nombre) {
        //por defecto
        this.equipo1 = new Equipo("Azul");
        this.equipo2 = new Equipo("Rojo");
        this.nombre = nombre;
        this.cantactualjugadores = 0;
        this.cantminjugadores = 2;
        this.cantmaxjugadores = 8;
        this.estado = EstadoPartida.DESACTIVADA;
        this.tiempo = 0;
        this.tiempomax = 60;
    }

    public int getTiempomax() {
        return tiempomax;
    }

    public void setTiempomax(int tiempomax) {
        this.tiempomax = tiempomax;
    }
    
    public void setlimite1(Location loc) {
        this.limite1=loc;
    }
    
    public Location getlimite1() {
        return this.limite1;
    }
    
    public void setlimite2(Location loc) {
        this.limite2=loc;
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

    public void agregarJugador(Jugador jugador) {
        if (equipo1.agregarjugador(jugador)) {
            this.cantactualjugadores++;
        }
    }

    public void repartirJugadorteam2(Jugador jugador) {
        this.equipo1.eliminarJugador(jugador.getjugador().getName());
        this.equipo2.agregarjugador(jugador);
    }

    public void removerJugador(String jugador) {
        if (equipo1.eliminarJugador(jugador) || equipo2.eliminarJugador(jugador)) {
            this.cantactualjugadores--;
        }
    }

    public ArrayList<Jugador> getJugadores() {
        ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
        for (Jugador j : equipo1.getJugadores()) {
            jugadores.add(j);
        }
        for (Jugador j : equipo2.getJugadores()) {
            jugadores.add(j);
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
        return null;
    }

    public Equipo getequipojugador(String nombre) {
        ArrayList<Jugador> jugadoresTeam1 = equipo1.getJugadores();
        for (int i = 0; i < jugadoresTeam1.size(); i++) {
            if (jugadoresTeam1.get(i).getjugador().getName().equals(nombre)) {
                return equipo1;
            }
        }

        ArrayList<Jugador> jugadoresTeam2 = equipo2.getJugadores();
        for (int i = 0; i < jugadoresTeam2.size(); i++) {
            if (jugadoresTeam2.get(i).getjugador().getName().equals(nombre)) {
                return equipo2;
            }
        }
        return null;
    }

    public Equipo getEquipo1() {
        return this.equipo1;
    }

    public Equipo getEquipo2() {
        return this.equipo2;
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
        if (equipo1.getJugadores().size() == 0) {
            return equipo2;
        }
        if (equipo2.getJugadores().size() == 0) {
            return equipo1;
        }

        int killteam1 = equipo1.getasesinatostotales();
        int killteam2 = equipo2.getasesinatostotales();
        
        if (killteam1>killteam2) {
            return equipo1;
        } else if (killteam1<killteam2) {
            return equipo2;
        } else return null;

    }

    public ArrayList<Jugador> getJugadoresKills() {
        ArrayList<Jugador> jugadorescopia = (ArrayList<Jugador>) getJugadores().clone();
        for (int i=0; i<jugadorescopia.size(); i++) {
            for (int c=i+1; c<jugadorescopia.size(); c++) {
                if (jugadorescopia.get(i).getasesinatos()<jugadorescopia.get(c).getasesinatos()) {
                    Jugador aux=jugadorescopia.get(i);
                    jugadorescopia.set(i, jugadorescopia.get(c));
                    jugadorescopia.set(c, aux);
                }
            
            }
        }
        return jugadorescopia;        
    }

}
