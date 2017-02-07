/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.graphics;

import binaryproject.elements.Botones;
import binaryproject.elements.Colores;
import binaryproject.elements.Imagenes;
import binaryproject.elements.Jugador;
import binaryproject.search.EscritorDArchivos;
import binaryproject.search.LectorDArchivos;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author steven
 */
public class Juego extends JFrame{
    
    //Frame
    private Graphics graficos;
    private Thread refresh;
    private final int ancho_mapa;
    private final int alto_mapa;
    private volatile Canvas lienzo;
    //Menus
    private ArrayList<ArrayList<Botones>> menu;
    private Imagenes graphicRes;
    private int selected_option;
    private int selected_menu;
    private Colores paleta;
    //Niveles
    private ArrayList<String> preguntas;
    //Jugadores
    private Jugador player;
    //Cuenta
    private String Name;
    private String Password;
    private String rutaP;
    
    public Juego(ArrayList<ImageIcon> IRes, String BDJugadores){
        //Frame
        this.ancho_mapa = 1280;
        this.alto_mapa = getAncho_mapa() / 16*9;
        inicializarFrame();
        //Recursos
        graphicRes = new Imagenes(IRes);
        preguntas = new ArrayList<String>();
        paleta = new Colores();
        //Cuentas
        Name = "";
        Password = "";
        this.rutaP = BDJugadores;
        //Acciones
        selected_option = 0;
        selected_menu = 0;
        controls();
        crearBotones();
        instanciarHiloPrincipal();
    }

    public void instanciarHiloPrincipal(){
        refresh = new Thread(new Runnable() {
            @Override
            public void run() {
                lienzo.createBufferStrategy(2);
                while(true){
                    try{
                        drawMenu();
                        Thread.sleep(30);
                    }catch(Exception e){
                        System.out.println("Algo paso");
                    }
                }
            }
        });
    }
    
    public void inicializarFrame(){
        this.setSize(getAncho_mapa(), getAlto_mapa());
        lienzo = new Canvas();
        this.add(lienzo);
    }
    
    public void crearBotones(){
        menu = new ArrayList<ArrayList<Botones>>();
        ArrayList<Botones> lista;
        lista = new ArrayList<Botones>();
        //Botones menu 0
        lista.add(new Botones(0, 400, 350, 80, 120, graphicRes.obtenerImagen(0)));
        lista.add(new Botones(1, 600, 350, 80, 120, graphicRes.obtenerImagen(0)));
        lista.add(new Botones(2, 800, 350, 80, 120, graphicRes.obtenerImagen(0)));
        lista.add(new Botones(3, 600, 500, 80, 120, graphicRes.obtenerImagen(0)));
        menu.add(lista);
        //Botones menu 1
        lista = new ArrayList<Botones>();
        lista.add(new Botones(0, 240, 350, 80, 120, graphicRes.obtenerImagen(0)));
        lista.add(new Botones(1, 600, 350, 80, 120, graphicRes.obtenerImagen(0)));
        lista.add(new Botones(2, 950, 350, 80, 120, graphicRes.obtenerImagen(0)));
        menu.add(lista);
        //Botones menu 26 (3 temporalmente)
        lista = new ArrayList<Botones>();
        lista.add(new Botones(0, 300, 300, 120, 160, graphicRes.obtenerImagen(0)));
        lista.add(new Botones(1, 850, 300, 120, 160, graphicRes.obtenerImagen(0)));
        //lista.add(new Botones(2, 950, 350, 80, 120, graphicRes.obtenerImagen(0)));
        //lista.add(new Botones(2, 950, 350, 80, 120, graphicRes.obtenerImagen(0)));
        //lista.add(new Botones(2, 950, 350, 80, 120, graphicRes.obtenerImagen(0)));
        menu.add(lista);
        
    
    }
    
    public void drawText(String texto, Color color, int tamaño, int X, int Y){
        graficos.setColor(color);
        graficos.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, tamaño));
        graficos.drawString(texto,X,Y);        
    }
    
    public void drawMenu(){
        String censor = "";
        if(Password.length() !=  0){
            for (int i = 0; i < Password.length(); i++) {
                censor+= "•";
            }
        }
        graficos = lienzo.getBufferStrategy().getDrawGraphics();
        graficos.setColor(paleta.getColores().get(paleta.getBLACK()));
        graficos.fillRect(0, 0, ancho_mapa, alto_mapa);
        int opcion=0;
        ArrayList<Botones> botones;
        /*switch(selected_menu){
                    case 0:
                        opcion = 0;
                        break;
                    case 1:
                        opcion = 5;
                        break;
        }*/
        switch(selected_menu){
            case 0:
                //////Menu principal
                drawText("Binary Search", paleta.getColores().get(paleta.WHITE), 50, 420, 100);
                break;
            case 1:
                //////Menu de niveles
                drawText("Level Selection", paleta.getColores().get(paleta.WHITE), 50, 420, 100);
                drawText("Score: " + 0, paleta.getColores().get(paleta.BLUE), 20, 240, 530);
                drawText("Score: " + 0, paleta.getColores().get(paleta.BLUE), 20, 590, 530);
                drawText("Score: " + 0, paleta.getColores().get(paleta.BLUE), 20, 940, 530);
                break;
            case 2:
                //////Accounts menu
                if(selected_option < 2){
                    drawText("Accounts", paleta.getColores().get(paleta.WHITE), 50, 500, 100);
                    drawText("Register", paleta.getColores().get(paleta.WHITE), 25, 310, 280);
                    drawText("Login", paleta.getColores().get(paleta.WHITE), 25, 860, 280);
                }else if(selected_option >= 2){
                    if(selected_option < 4){
                        drawText("Registration", paleta.getColores().get(paleta.WHITE), 50, 500, 100);
                    }else if(selected_option >= 4){
                        drawText("Login", paleta.getColores().get(paleta.WHITE), 50, 500, 100);
                    }
                    drawText("Username", paleta.getColores().get(paleta.WHITE), 30, 400, 280);
                    drawText(Name, paleta.getColores().get(paleta.RED), 20, 410, 300);
                    drawText("Password", paleta.getColores().get(paleta.WHITE), 30, 400, 400);
                    drawText(censor, paleta.getColores().get(paleta.RED), 20, 410, 420);
                }
                break;
            case 3:
                
                break;
        
        }
        //////Imagen de opciones (BotoneS)
        if(selected_menu != 2 || selected_option < 2){
            botones = menu.get(selected_menu);
            for (int i = 0; i < botones.size(); i++) {
                botones.get(i).dibujar(graficos, selected_option);
            }
        }
        
        lienzo.getBufferStrategy().show();
    
    }
    
    public void crearNuevoJugador() throws IOException{
        /*
        (ID) (Nombre),(Contraseña(tiempoNivel1),(tiempoNivel2),(tiempoNivel3),
          0, Astrobix, awaker2130,      0,             0,             0,
        (PuntajeN1),(PuntajeN2),(PuntajeN3),(IntentoN1),(IntentoN2),(IntentoN3)
            0,           0,          0,          0,          0,          0,
        (Score)
           0
        */
        if(!existePlayer()){
            try{
            EscritorDArchivos writer = new EscritorDArchivos(rutaP);
            String newPlayer = "0,"+Name+","+Password+",0,0,0,0,0,0,0,0,0,0";
            writer.escribir(newPlayer);
            writer.cerrar();
            System.out.println("A new player has been created!");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
    public void cargarDatos() throws FileNotFoundException, IOException{
        if(existePlayer()){
            LectorDArchivos reader = new LectorDArchivos(rutaP);
            while(!reader.leerLinea()[0].equals(Name)){}
            String[] jugador = reader.getLineaActual().split(",");
            if(jugador[0].equals(Name)){
                if(jugador[1].equals(Password)){
                    System.out.println("Logged in!");
                }else{
                    System.out.println("Failed to log in");
                }
            }
            reader.cerrarArchivo();
        }else{
            System.out.println("Doesn't exist");
        }
    }
    
    public boolean existePlayer() throws FileNotFoundException, IOException{
        LectorDArchivos reader = new LectorDArchivos(rutaP);
        String[] linea = reader.leerLinea();
        while(linea != null){
            if(linea[0].equals(Name) && linea[1].equals(Password)){
                return true;
            }
            linea = reader.leerLinea();
        }
        return false;
    }
    
    public void controls(){
        lienzo.addKeyListener(new KeyListener() {
           
            @Override
            public void keyPressed(KeyEvent tecla) {
                int movement = tecla.getKeyCode();
                switch(selected_menu){
                    case 0:
                        switch(selected_option){
                            case 0:
                                if(movement == KeyEvent.VK_RIGHT){
                                    selected_option++;
                                }else if(movement == KeyEvent.VK_LEFT){
                                    selected_option = 3;
                                }else if(movement == KeyEvent.VK_ENTER){
                                    selected_menu = 1; //Seleccion de niveles
                                    selected_option = 0;
                                }
                                break;
                            case 1:
                                if(movement == KeyEvent.VK_RIGHT){
                                    selected_option++;
                                }else if(movement == KeyEvent.VK_LEFT){
                                    selected_option--;
                                }else if(movement == KeyEvent.VK_ENTER){
                                    graficos.setColor(Color.white);
                                    graficos.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 20));
                                    graficos.drawString("NOT IMPLEMENTED YET", 0, 20);
                                    lienzo.getBufferStrategy().show();
                                }
                                break;
                            case 2:
                                if(movement == KeyEvent.VK_RIGHT){
                                    selected_option++;
                                }else if(movement == KeyEvent.VK_LEFT){
                                    selected_option--;
                                }else if(movement == KeyEvent.VK_ENTER){
                                    selected_menu = 2; // Accounts menu
                                    selected_option = 0;
                                };
                                break;
                            case 3:
                                if(movement == KeyEvent.VK_RIGHT){
                                    selected_option = 0;
                                }else if(movement == KeyEvent.VK_LEFT){
                                    selected_option--;
                                }else if(movement == KeyEvent.VK_ENTER){
                                    System.exit(0);
                                };
                                break;
                        }
                        break;
                    case 1:
                        if(movement == KeyEvent.VK_ESCAPE){
                            selected_menu = 0;
                            selected_option = 0;
                        }else{
                            switch(selected_option){
                                case 0:
                                    if(movement == KeyEvent.VK_RIGHT){
                                        selected_option++;
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        selected_option = 2;
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        //Explicacion nivel 1
                                    }
                                    break;
                                case 1:
                                    if(movement == KeyEvent.VK_RIGHT){
                                        selected_option++;
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        selected_option--;
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        //Explicacion nivel 2
                                    }
                                    break;
                                case 2:
                                    if(movement == KeyEvent.VK_RIGHT){
                                        selected_option = 0;
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        selected_option--;
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        //Quiz nivel 3
                                    }
                                    break;
                            }
                        }
                       break;
                    case 2:
                        if(selected_option < 2){
                            if(movement == KeyEvent.VK_RIGHT || movement == KeyEvent.VK_LEFT){
                                selected_option = (selected_option == 0)? 1:0;
                            }else if(movement == KeyEvent.VK_ENTER){
                                selected_option = (selected_option == 0) ? 2:4;
                            }else if(movement == KeyEvent.VK_ESCAPE){
                                selected_menu = 0;
                                selected_option = 2;
                            }
                        }else if(selected_option >= 2){
                            
                            if(movement == KeyEvent.VK_ESCAPE){
                                selected_option = 0;
                                Name = "";
                                Password = "";
                            }
                            
                            if(movement == KeyEvent.VK_RIGHT || movement == KeyEvent.VK_LEFT){
                                if(selected_option < 4){
                                    selected_option = (selected_option == 2)? 3:2;
                                }else{
                                    selected_option = (selected_option == 4)? 5:4;
                                }   
                            }
                            
                            else if(movement >= KeyEvent.VK_A && movement <= KeyEvent.VK_Z){
                                if(selected_option == 2 || selected_option == 4){
                                    Name+=tecla.getKeyChar();
                                }else if (selected_option == 3 || selected_option == 5){
                                    Password+=tecla.getKeyChar();
                                }
                            }
                            
                            else if(movement == KeyEvent.VK_BACK_SPACE){
                                if(selected_option == 2 || selected_option == 4){
                                    if(Name.length()!=0){
                                        Name = Name.substring(0, Name.length()-1);
                                    }
                                }else if (selected_option == 3 || selected_option == 5){
                                    if(Password.length()!=0){
                                        Password = Password.substring(0, Password.length()-1);
                                    }
                                }
                            }
                            
                            else if(movement == KeyEvent.VK_ENTER){
                                if(Name.length() == 0 || Password.length() == 0){
                                    drawText("Fill both spaces", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
                                }else{
                                    if(selected_option == 2 || selected_option == 3){
                                        try {
                                            //Crear nuevo jugador
                                            crearNuevoJugador();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        drawText("New player registered!", paleta.getColores().get(paleta.getGREEN()), 25, 0, 20);
                                    }else if(selected_option == 4 || selected_option == 5){
                                        //Cargar user
                                        drawText("Welcome back, "+ Name+"!", paleta.getColores().get(paleta.getGREEN()), 25, 0, 20);
                                    }
                                }
                                lienzo.getBufferStrategy().show();
                            }
                        }
                        break;
                    case 3:
                       break;
                       
               
               
               }
               
           }
           
           @Override
           public void keyTyped(KeyEvent e) {
           }
           
           @Override
           public void keyReleased(KeyEvent e) {
           }
       });
        
        
        lienzo.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
   
   }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////Getter and Setter/////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public Graphics getGraficos() {
        return graficos;
    }

    public Thread getRefresh() {
        return refresh;
    }

    public int getSelected_option() {
        return selected_option;
    }

    public int getAncho_mapa() {
        return ancho_mapa;
    }

    public int getAlto_mapa() {
        return alto_mapa;
    }

    public Canvas getLienzo() {
        return lienzo;
    }
    
    
    
}
