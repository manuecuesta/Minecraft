/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 *
 * @author Manuel
 */
public class Jugador {
    
    private Player jugador;
    private int muertes;
    private int asesinatos;
    private boolean asesinadorecientemente;
    private ElementosGuardados guardados;
    private int muertestotales;
    private int asesinatostotales;
    private int vidas;
    
    public Jugador(Player jugador) {
        this.jugador=jugador;
        this.guardados=new ElementosGuardados(jugador.getInventory().getContents(), jugador.getInventory().getArmorContents(), jugador.getGameMode(), jugador.getExp(), jugador.getLevel(), jugador.getHealth(), jugador.getMaxHealth());
        this.asesinatos=0;
        this.muertes=0;
        this.vidas=0;
    }

    public int getVidas() {
        return vidas;
    }

    public ElementosGuardados getGuardados() {
        return this.guardados;
    }
    
    public void aumentarasesinatos() {
        this.asesinatos++;
    }
    
    public void aumentarmuertes() {
        this.muertes++;
    }
    
    public int getasesinatos() {
        return this.asesinatos;
    }
    
    public int getmuertes() {
        return this.muertes;
    }
    
    public Player getjugador() {
        return this.jugador;
    }

    public boolean isAsesinadorecientemente() {
        return asesinadorecientemente;
    }

    public void setAsesinadorecientemente(boolean asesinadorecientemente) {
        this.asesinadorecientemente = asesinadorecientemente;
    }
    
    
    
}
