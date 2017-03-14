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
import java.io.File;
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
    private ArrayList<String[]> logs;
    private ArrayList<String[]> listaPreg;
    String jugadoresRuta;
    String rutaPlayers;
    String rutaLogs;
    
    public BinaryProject(){
        recurGraf = new ArrayList<ArrayList<ImageIcon>>();
        recurMusic = new ArrayList<ImageIcon>();
        jugadores = new ArrayList<String[]>();
        listaPreg = new ArrayList<String[]>();
        logs = new ArrayList<String[]>();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        BinaryProject BP = new BinaryProject();
        BP.cargarImagenes();
        BP.cargarJugadores();
        BP.cargarBancoDePreguntas();
        BP.cargarSesiones();
        Juego V = new Juego(BP.getRecurGraf(), BP.jugadoresRuta, BP.listaPreg, BP.jugadores, BP.rutaPlayers, BP.logs);
        V.setVisible(true);
        V.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        V.getRefresh().start();
        V.setResizable(false);
        System.out.println("hola");
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
    
    public void cargarSesiones() throws IOException{
        rutaLogs = getClass().getResource("niveles/logs.txt").getPath();
        File logsFile = new File(rutaLogs);
        if(!logsFile.exists()){
            logsFile.createNewFile();
        }
        LectorDArchivos lector = new LectorDArchivos(rutaLogs);
        lector.nextLogLine();
        lector.nextLogLine();
        lector.nextLogLine();
        while(lector.getLineaActual() != null){
           logs.add(lector.getLineaActual().split(";"));
           lector.nextLogLine();
        }
    }
    
    public void cargarJugadores() throws FileNotFoundException, IOException{
        rutaPlayers = getClass().getResource("niveles/").getPath();
        File playerFile = new File(rutaPlayers+"jugadores.txt");
        if(!playerFile.exists()){
            playerFile.createNewFile();
        }
        jugadoresRuta = playerFile.getPath();
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
