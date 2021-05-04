/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gmail.cuestamanue7.managers;

/**
 *
 * @author Manuel
 */
public class UtilidadesPaintball {
    
    public static String getTiempo(int tiempo) {
        int minutos = tiempo/60;
        int segundos = tiempo-minutos*60;
        String segundosMSG="";
        String minutosMSG="";
        
        if (segundos>=0 && segundos <=9) {
            segundosMSG="0"+segundos;
        } else {
            segundosMSG = segundos+"";
        }
        
        if (minutos>=0 && minutos <=9) {
            minutosMSG="0"+minutos;
        } else {
            minutosMSG = minutos+"";
        }
        
        return minutosMSG+":"+segundosMSG;
        
    }
    
}
