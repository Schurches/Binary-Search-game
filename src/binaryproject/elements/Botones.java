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
    private ImageIcon unlocked;
    private ImageIcon locked;

    /***
     * 
     * @param INDEX index del boton
     * @param X posicion en X a dibujarse
     * @param Y posicion en Y a dibujarse
     * @param ancho anchura de imagen
     * @param alto altura de imagen
     * @param imagen imagen del boton 
     */
    public Botones(int INDEX, int X, int Y, int ancho, int alto, ImageIcon imagen) {
        this.INDEX = INDEX;
        this.X = X;
        this.Y = Y;
        this.ancho = ancho;
        this.alto = alto;
        this.unlocked = imagen;
    }
    
    /***
     * 
     * @param INDEX index del boton
     * @param X posicion en X a dibujarse
     * @param Y posicion en Y a dibujarse
     * @param ancho anchura de la imagen
     * @param alto altura de la imagen
     * @param unlocked Imagen cuando la opcion halla sido desbloqueada
     * @param locked Imagen cuando aun no se ha desbloqueado la opcion
     */
    public Botones(int INDEX, int X, int Y, int ancho, int alto, ImageIcon unlocked, ImageIcon locked) {
        this.INDEX = INDEX;
        this.X = X;
        this.Y = Y;
        this.ancho = ancho;
        this.alto = alto;
        this.unlocked = unlocked;
        this.locked = locked;
    }
    
    public void dibujar(Graphics G, int index, boolean isLocked){
        if(index == getINDEX()){
            G.drawImage(getImagen(isLocked).getImage(), getX()-10, getY()-10, getAncho()+20, getAlto()+20, null);
        }else{
            G.drawImage(getImagen(isLocked).getImage(), getX(), getY(), getAncho(), getAlto(), null);
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

    public ImageIcon getImagen(boolean isLocked) {
        if(isLocked){
            return locked;
        }else{
            return unlocked;
        }
    }
    
    
    
    
    
}
