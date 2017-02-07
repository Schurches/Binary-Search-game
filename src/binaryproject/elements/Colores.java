/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.elements;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author steven
 */
public class Colores {
    
    public final int BLACK=0;
    public final int WHITE=1;
    public final int RED=2;
    public final int GREEN=3;
    public final int BLUE=4;
    private ArrayList<Color> colores;

    public Colores() {
        this.colores = new ArrayList<Color>();
        colores.add(new Color(0,0,0));
        colores.add(new Color(255,255,255));
        colores.add(new Color(142, 7, 7));
        colores.add(new Color(22, 142, 7));
        colores.add(new Color(10,111,142));
    }

    public int getWHITE() {
        return WHITE;
    }

    public int getBLACK() {
        return BLACK;
    }

    public int getRED() {
        return RED;
    }

    public int getGREEN() {
        return GREEN;
    }

    public int getBLUE() {
        return BLUE;
    }

    public ArrayList<Color> getColores() {
        return colores;
    }
    
    


}
