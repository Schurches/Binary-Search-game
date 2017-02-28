/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject;

import binaryproject.elements.Imagenes;
import binaryproject.graphics.Juego;
import binaryproject.search.LectorDArchivos;
import binaryproject.search.binarySearch;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author steven
 */
public class BinaryProject {

    private ArrayList<ArrayList<ImageIcon>> recurGraf;
    private ArrayList<ImageIcon> recurMusic;
    private ArrayList<String[]> jugadores;
    private ArrayList<Object> preguntas;
    private ArrayList<String[]> listaPreg;
    String jugadoresRuta;
    public BinaryProject(){
        recurGraf = new ArrayList<ArrayList<ImageIcon>>();
        recurMusic = new ArrayList<ImageIcon>();
        jugadores = new ArrayList<String[]>();
        listaPreg = new ArrayList<String[]>();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        /*int[] lol = new int[11];
        lol[0] = 11;
        lol[1] = 12;
        lol[2] = 13;
        lol[3] = 14;
        lol[4] = 15;
        lol[5] = 20;
        lol[6] = 21;
        lol[7] = 22;
        lol[8] = 23;
        lol[9] = 24;
        lol[10] = 25;
        binarySearch asd = new binarySearch(lol);
        asd.search(11);*/ 
        BinaryProject BP = new BinaryProject();
        BP.cargarImagenes();
        BP.cargarJugadores();
        BP.cargarBancoDePreguntas();
        Juego V = new Juego(BP.getRecurGraf(), BP.jugadoresRuta, BP.listaPreg, BP.jugadores);
        V.setVisible(true);
        V.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        V.getRefresh().start();
        V.setResizable(false);
    }
    
    public void cargarBancoDePreguntas() throws FileNotFoundException, IOException{
        LectorDArchivos reader = new LectorDArchivos(getClass().getResource("niveles/bancoP.txt").getFile());
        String[] preguntas = reader.leerLinea();
        while(preguntas != null){
            listaPreg.add(preguntas);
            preguntas = reader.leerLinea();
        }
        reader.cerrarArchivo();
    }
    
    public void cargarJugadores() throws FileNotFoundException, IOException{
        jugadoresRuta = getClass().getResource("niveles/jugadores.txt").getPath();
        LectorDArchivos reader = new LectorDArchivos(jugadoresRuta);
        ArrayList<String[]> usuarios = new ArrayList<String[]>();
        while(reader.leerLinea()!=null){
            usuarios.add(reader.getLineaActual().split(","));
        }
        jugadores = usuarios;
    }
 
    public void cargarImagenes(){
        ArrayList<ImageIcon> imagenes = new ArrayList<ImageIcon>();
        ImageIcon imagen;
        //Botones -- ID = 0
        for (int i = 0; i <= 16; i++) {
            if(i < 10){
                imagen = new ImageIcon(getClass().getResource("imagenes/binarySearch10"+i+".png"));
            }else{
                imagen = new ImageIcon(getClass().getResource("imagenes/binarySearch1"+i+".png"));
            }
            imagenes.add(imagen);
        }
        recurGraf.add(imagenes);
        //Explicaciones --
        imagenes = new ArrayList<ImageIcon>();
        //Nivel1 -- ID = 1
        for (int i = 1; i <= 47; i++) {
            imagen = new ImageIcon(getClass().getResource("imagenes/binarySearch"+i+".jpg"));
            imagenes.add(imagen);
        }
        recurGraf.add(imagenes);
        imagenes = new ArrayList<ImageIcon>();
        //Nivel2 -- ID = 2
        imagenes = new ArrayList<ImageIcon>();
        //Nivel3 -- ID = 3
        
        recurGraf.add(imagenes);
    }

    public ArrayList<ArrayList<ImageIcon>> getRecurGraf() {
        return recurGraf;
    }
    
    
}
