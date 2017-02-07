/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject;

import binaryproject.elements.Imagenes;
import binaryproject.graphics.Juego;
import binaryproject.search.LectorDArchivos;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author steven
 */
public class BinaryProject {

    private ArrayList<ImageIcon> recurGraf;
    private ArrayList<ImageIcon> recurMusic;
    private ArrayList<String> jugadores;
    private ArrayList<Object> preguntas;
    String jugadoresRuta;
    public BinaryProject(){
        recurGraf = new ArrayList<ImageIcon>();
        recurMusic = new ArrayList<ImageIcon>();
        jugadores = new ArrayList<String>();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BinaryProject BP = new BinaryProject();
        BP.cargarImagenes();
        BP.cargarJugadores();
        Juego V = new Juego(BP.getRecurGraf(), BP.jugadoresRuta);
        V.setVisible(true);
        V.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        V.getRefresh().start();
        V.setResizable(false);
    }
    
    public void cargarBancoDePreguntas() throws FileNotFoundException, IOException{
        LectorDArchivos reader = new LectorDArchivos(getClass().getResource("niveles/bancoP.txt").getFile());
        reader.cerrarArchivo();
        System.out.println("I found it!");
    }
    
    public void cargarJugadores() throws FileNotFoundException, IOException{
        jugadoresRuta = getClass().getResource("niveles/jugadores.txt").getFile();
        LectorDArchivos reader = new LectorDArchivos(jugadoresRuta);
        ArrayList<String> usuarios = new ArrayList<String>();
        while(reader.getLineaActual()!=null){
            reader.leerLinea();
            usuarios.add(reader.getLineaActual());
        }
        System.out.println("I found it!");
    }
 
    public void cargarImagenes(){
        ImageIcon asd = new ImageIcon(getClass().getResource("imagenes/Pergamino.jpg"));
        recurGraf.add(asd);
    }

    public ArrayList<ImageIcon> getRecurGraf() {
        return recurGraf;
    }
    
    
}
