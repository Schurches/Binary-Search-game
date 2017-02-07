/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.elements;

import java.awt.Graphics;
import javax.swing.ImageIcon;

/**
 *
 * @author steven
 */
public class Botones {
    
    private final int INDEX;
    private int X;
    private int Y;
    private int ancho;
    private int alto;
    private ImageIcon imagen;

    public Botones(int INDEX, int X, int Y, int ancho, int alto, ImageIcon imagen) {
        this.INDEX = INDEX;
        this.X = X;
        this.Y = Y;
        this.ancho = ancho;
        this.alto = alto;
        this.imagen = imagen;
    }
    
    public void dibujar(Graphics G, int index){
        if(index == getINDEX()){
            G.drawImage(getImagen().getImage(), getX()-10, getY()-10, getAncho()+20, getAlto()+20, null);
        }else{
            G.drawImage(getImagen().getImage(), getX(), getY(), getAncho(), getAlto(), null);
        }
    }

    public int getINDEX() {
        return INDEX;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public ImageIcon getImagen() {
        return imagen;
    }
    
    
    
    
    
}
