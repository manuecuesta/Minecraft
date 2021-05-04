/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import org.bukkit.Location;

/**
 *
 * @author Manuel
 */
public class Generador {

    private Location loc;
    private Partida p;
    private TipoGenerador tipo;
    private Equipo equipo;


    public Generador(Location loc, Partida p, TipoGenerador tipo) {
        this.loc = loc;
        this.p = p;
        this.tipo = tipo;
    }

    public Generador(Location loc, Partida p, TipoGenerador tipo, Equipo equipo) {
        this.loc = loc;
        this.p = p;
        this.tipo = tipo;
        this.equipo = equipo;
    }

    public Location getLoc() {
        return loc;
    }

    public Partida getP() {
        return p;
    }

    public TipoGenerador getTipo() {
        return tipo;
    }

    public void setLoc(Location loc) {
        this.loc = loc;
    }

    public void setP(Partida p) {
        this.p = p;
    }

    public void setTipo(TipoGenerador tipo) {
        this.tipo = tipo;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

}
