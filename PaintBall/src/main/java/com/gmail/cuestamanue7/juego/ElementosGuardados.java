/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.juego;

import org.bukkit.GameMode;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Manuel
 */
public class ElementosGuardados {
    
    private ItemStack[] inventarioguardado;
    private ItemStack[] equipamientoguardado;
    private GameMode gamemodeguardado;
    private float experienciaguardada;
    private int nivelguardado;
    private double vidaguardada;
    private double maximavidaguardada;
    
    public ElementosGuardados(ItemStack[] inventarioguardado, ItemStack[] equipamientoguardado, GameMode gamemodeguardado, float experienciaguardada, int nivelguardado, double vidaguardada, double maximavidaguardada) {
        this.inventarioguardado = inventarioguardado;
        this.equipamientoguardado = equipamientoguardado;
        this.gamemodeguardado = gamemodeguardado;
        this.experienciaguardada = experienciaguardada;
        this.nivelguardado = nivelguardado;
        this.vidaguardada = vidaguardada;
        this.maximavidaguardada = maximavidaguardada;
    }

    public ItemStack[] getInventarioguardado() {
        return inventarioguardado;
    }

    public ItemStack[] getEquipamientoguardado() {
        return equipamientoguardado;
    }

    public GameMode getGamemodeguardado() {
        return gamemodeguardado;
    }

    public float getExperienciaguardada() {
        return experienciaguardada;
    }

    public int getNivelguardado() {
        return nivelguardado;
    }

    public double getVidaguardada() {
        return vidaguardada;
    }

    public double getMaximavidaguardada() {
        return maximavidaguardada;
    }

    public void setInventarioguardado(ItemStack[] inventarioguardado) {
        this.inventarioguardado = inventarioguardado;
    }

    public void setEquipamientoguardado(ItemStack[] equipamientoguardado) {
        this.equipamientoguardado = equipamientoguardado;
    }

    public void setGamemodeguardado(GameMode gamemodeguardado) {
        this.gamemodeguardado = gamemodeguardado;
    }

    public void setExperienciaguardada(float experienciaguardada) {
        this.experienciaguardada = experienciaguardada;
    }

    public void setNivelguardado(int nivelguardado) {
        this.nivelguardado = nivelguardado;
    }

    public void setVidaguardada(double vidaguardada) {
        this.vidaguardada = vidaguardada;
    }

    public void setMaximavidaguardada(double maximavidaguardada) {
        this.maximavidaguardada = maximavidaguardada;
    }

    

    
}
