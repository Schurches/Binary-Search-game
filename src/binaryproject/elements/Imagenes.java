/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.elements;

import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author steven
 */
public class Imagenes {
    
    private ArrayList<ImageIcon> imagenes;
    
    public Imagenes(){
        this.imagenes = new ArrayList<ImageIcon>();
    }
    
    public Imagenes(ArrayList<ImageIcon> imagenes){
        this.imagenes = imagenes;
    }
    
    public void agregarImagen(ImageIcon imagen){
       this.imagenes.add(imagen);
    } 
    
    public ImageIcon obtenerImagen(int index){
        return this.imagenes.get(index);
    }
    public ArrayList<ImageIcon> obtenerLista(){
        return this.imagenes;
    }
    
    
}
