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
import java.util.Random;
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
    private ArrayList<Imagenes> graphicRes;
    private int selected_choise;
    private int selected_option;
    private int selected_menu; 
    //0 = main, 1 = levels, 2 = login/register, 3 = nivel1, 4 = nivel2, 5 = nivel3
    private Colores paleta;
    //Niveles
    private ArrayList<String[]> preguntas;
    private boolean[] gatheredMedals;
    //Jugadores
    private Jugador player;
    //Cuenta
    private String Name;
    private String Password;
    private String rutaP;
    //Constantes
    private final int choiseX;
    private final int choiseY;
    private int correctCounter;
    private int questionIndex;
    private boolean created;
    private boolean taken;
    
    public Juego(ArrayList<ArrayList<ImageIcon>> IRes, String BDJugadores, ArrayList<String[]> bancoP){
        //Frame
        this.ancho_mapa = 1280;
        this.alto_mapa = getAncho_mapa() / 16*9;
        inicializarFrame();
        //Recursos
        Imagenes I;
        graphicRes = new ArrayList<Imagenes>();
        for (int i = 0; i <= 1; i++) {
            I = new Imagenes(IRes.get(i));
            graphicRes.add(I);
        }
        preguntas = new ArrayList<String[]>();
        preguntas = bancoP;
        paleta = new Colores();
        gatheredMedals = new boolean[5];
        choiseX = 70;
        choiseY = 400;
        //Cuentas
        Name = "";
        Password = "";
        this.rutaP = BDJugadores;
        created = false;
        taken = false;
        //Acciones
        selected_option = 1;
        selected_menu = 0;
        selected_choise = 4;
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
                    drawMenu();
                    try{
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
        //Botones menu 0 (Principal)
        lista.add(new Botones(0, 400, 350, 80, 120, graphicRes.get(0).obtenerImagen(0)));
        lista.add(new Botones(1, 600, 350, 80, 120, graphicRes.get(0).obtenerImagen(0)));
        lista.add(new Botones(2, 800, 350, 80, 120, graphicRes.get(0).obtenerImagen(0)));
        lista.add(new Botones(3, 580, 500, 150, 150, graphicRes.get(0).obtenerImagen(8)));
        menu.add(lista);
        //Botones menu 1 (Niveles)
        lista = new ArrayList<Botones>();
        lista.add(new Botones(0, 240, 350, 80, 120, graphicRes.get(0).obtenerImagen(0)));
        lista.add(new Botones(1, 600, 350, 80, 120, graphicRes.get(0).obtenerImagen(0)));
        lista.add(new Botones(2, 950, 350, 80, 120, graphicRes.get(0).obtenerImagen(0)));
        menu.add(lista);
        //Botones menu 2 (Login)
        lista = new ArrayList<Botones>();
        lista.add(new Botones(0, 300, 300, 120, 160, graphicRes.get(0).obtenerImagen(0)));
        lista.add(new Botones(1, 850, 300, 120, 160, graphicRes.get(0).obtenerImagen(0)));
        menu.add(lista);
        //Botones menu 3 (Ganar/Perder & save)
        lista = new ArrayList<Botones>();
        lista.add(new Botones(4, 350, 500, 150, 150, graphicRes.get(0).obtenerImagen(6)));
        lista.add(new Botones(5, 550, 500, 150, 150, graphicRes.get(0).obtenerImagen(7)));
        lista.add(new Botones(6, 750, 500, 150, 150, graphicRes.get(0).obtenerImagen(8)));
        menu.add(lista);
    }
    
    public void drawText(String texto, Color color, int tamaño, int X, int Y){
        graficos.setColor(color);
        graficos.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, tamaño));
        graficos.drawString(texto,X,Y);        
    }
    
    public void drawLevelStatistics(){
        if(player!=null){
            drawText("Score: " + player.getPuntajes().get(0), paleta.getColores().get(paleta.BLUE), 20, 240, 530);
            drawText("Time: " + player.getTiempos().get(0), paleta.getColores().get(paleta.BLUE), 20, 240, 550);
            drawText("Score: " + player.getPuntajes().get(1), paleta.getColores().get(paleta.BLUE), 20, 590, 530);
            drawText("Time: " + player.getTiempos().get(1), paleta.getColores().get(paleta.BLUE), 20, 590, 550);
            drawText("Score: " + player.getPuntajes().get(2), paleta.getColores().get(paleta.BLUE), 20, 940, 530);
            drawText("Time: " + player.getTiempos().get(2), paleta.getColores().get(paleta.BLUE), 20, 940, 550);
        }else{
            drawText("Score: " + 0.0, paleta.getColores().get(paleta.BLUE), 20, 240, 530);
            drawText("Score: " + 0.0, paleta.getColores().get(paleta.BLUE), 20, 590, 530);
            drawText("Score: " + 0.0, paleta.getColores().get(paleta.BLUE), 20, 940, 530);
        }
        
    }
    
    public void drawMenu(){
        String censor = "";
        if(Password.length() !=  0){
            for (int i = 0; i < Password.length(); i++) {
                censor+= "•";
            }
        }
        graficos = lienzo.getBufferStrategy().getDrawGraphics();
        graficos.setColor(paleta.getColores().get(paleta.getWHITE()));
        graficos.fillRect(0, 0, ancho_mapa, alto_mapa);
        ArrayList<Botones> botones;
        switch(selected_menu){
            case 0:
                //////Menu principal
                drawText("Binary Search", paleta.getColores().get(paleta.getBLACK()), 50, 420, 100);
                drawText("Play", paleta.getColores().get(paleta.getBLACK()), 20, 410, 330);
                drawText("Scoreboard", paleta.getColores().get(paleta.getBLACK()), 20, 585, 330);
                drawText("Accounts", paleta.getColores().get(paleta.getBLACK()), 20, 790, 330);
                drawText("Log out", paleta.getColores().get(paleta.getBLACK()), 20, 610, 510);
                break;
            case 1:
                //////Menu de niveles
                drawText("Level Selection", paleta.getColores().get(paleta.getBLACK()), 50, 420, 100);
                drawLevelStatistics();
                break;
            case 2:
                //////Accounts menu
                if(selected_option < 2){
                    drawText("Accounts", paleta.getColores().get(paleta.getBLACK()), 50, 500, 100);
                    drawText("Register", paleta.getColores().get(paleta.getBLACK()), 25, 310, 280);
                    if(player==null){
                        drawText("Log in", paleta.getColores().get(paleta.getBLACK()), 25, 860, 280);
                    }else{
                        drawText("Welcome back, "+player.getNombre()+"!", paleta.getColores().get(paleta.getGREEN()), 25, 0, 20);
                        drawText("Logged in", paleta.getColores().get(paleta.getGREEN()), 25, 860, 280);
                    }
                }else if(selected_option >= 2){
                    if(selected_option < 4){
                        drawText("Registration", paleta.getColores().get(paleta.getBLACK()), 50, 500, 100);
                        if(created){
                            drawText("New player registered!", paleta.getColores().get(paleta.getGREEN()), 25, 0, 20);
                        }else if(taken){
                            drawText("An account already exists with this name!", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
                        }
                    }else if(selected_option >= 4){
                        drawText("Login", paleta.getColores().get(paleta.getBLACK()), 50, 500, 100);
                    }
                    drawText("Username", paleta.getColores().get(paleta.getBLACK()), 30, 400, 280);
                    drawText(Name, paleta.getColores().get(paleta.RED), 20, 410, 300);
                    drawText("Password", paleta.getColores().get(paleta.getBLACK()), 30, 400, 400);
                    drawText(censor, paleta.getColores().get(paleta.RED), 20, 410, 420);
                }
                break;
            case 3:
                //Nivel 1
                if(selected_option<34){
                    explicaciones(1);
                }else{
                    quiz(1);
                }
                break;
            case 4:
                //Nivel 2
                if(selected_option<12){
                    explicaciones(2);
                }else{
                    quiz(2);
                }
                break;
            case 5:
                quiz(3);
                break;
        }
        //////Imagen de opciones (BotoneS)
        if(selected_option==39 && selected_menu==3 || selected_option==17 && selected_menu == 4 || selected_menu == 5 && selected_option == 5){
            botones = menu.get(3);
            for (int i = 0; i < botones.size(); i++) {
                if((i!=1 || correctCounter<=2)){
                    botones.get(i).dibujar(graficos, selected_choise);
                }
            }
        }else if((selected_menu<3) && (selected_menu!=2 || selected_option<2)){
            botones = menu.get(selected_menu);
            for (int i = 0; i < botones.size(); i++) {
                botones.get(i).dibujar(graficos, selected_option);
            }
        }
        lienzo.getBufferStrategy().show();
    }
    
    public void showQuestion(int firstQuestionPage, int winningPage, int fontSize){
        if(selected_option>=firstQuestionPage && selected_option<winningPage){
            int questionNumb = selected_option-firstQuestionPage;
            String question = preguntas.get(questionIndex)[0];
            if(question.length()>80){
                drawText((questionNumb+1)+"): "+question.split(":")[0], paleta.getColores().get(paleta.getBLACK()), fontSize+5, choiseX-10, choiseY-100);
                drawText(question.split(":")[1], paleta.getColores().get(paleta.getBLACK()), fontSize+5, choiseX+20, choiseY+(fontSize+5)-100);
            }else{
                drawText((questionNumb+1)+"): "+question, paleta.getColores().get(paleta.getBLACK()), fontSize+5, choiseX-10, choiseY-100);
            }
            drawText("a): "+preguntas.get(questionIndex)[1], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX, choiseY);
            drawText("b): "+preguntas.get(questionIndex)[2], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX, choiseY+50);
            drawText("c): "+preguntas.get(questionIndex)[3], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX, choiseY+100);
            drawMedals(questionNumb);
            drawInfo();
            drawSelectedChoise();
        }else if(selected_option==winningPage){
            drawWinningMenu();
        }
    }
    
    public void quiz(int nivel){
        switch(nivel){
            case 1:
                showQuestion(34, 39, 20);
                break;
            case 2:
                showQuestion(12, 17, 20);
                break;
            case 3:
                break;
        }
    }
    
    public void drawSelectedChoise(){
        graficos.drawImage(graphicRes.get(0).obtenerImagen(3).getImage(), choiseX-4, (choiseY-18)+((selected_choise-1)*50),22,22,null);
    }
    
    public void drawMedals(int questionNumber){
        int X;
        int Y = 50;
        int ancho = 100;
        int alto = 80;
        for (int i = 0; i < 5; i++) {
            X = 200+2*i*ancho;
            if(!gatheredMedals[i]){
                graficos.drawImage(graphicRes.get(0).obtenerImagen(1).getImage(), X, Y, ancho,alto,null);
            }else{
                graficos.drawImage(graphicRes.get(0).obtenerImagen(2).getImage(), X, Y, ancho,alto,null);
            }
        }
    }
    
    public void drawInfo(){
    
    }
    
    public void explicaciones(int nivel){
        if(nivel==1){
            graficos.drawImage(graphicRes.get(1).obtenerImagen(selected_option).getImage(), 0, 0, ancho_mapa-10, alto_mapa-10, null);
        }else if(nivel==2){
            graficos.drawImage(graphicRes.get(1).obtenerImagen(selected_option+35).getImage(), 0, 0, ancho_mapa-10, alto_mapa-10, null);
        }
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
                String newPlayer = writer.numberOfPlayers()+","+Name+","+Password+",0,0,0,0,0,0,0,0,0,0";
                writer.escribir(newPlayer);
                writer.cerrar();
                created = true;
                taken = false;
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            taken = true;
        }
    }
    
    public void syncData(String[] playerInformation){
        int ID = Integer.parseInt(playerInformation[0]);
        ArrayList<Long> tiempos = new ArrayList<Long>();
        ArrayList<Float> puntos = new ArrayList<Float>();
        ArrayList<Integer> intentos = new ArrayList<Integer>();
        tiempos.add(Long.parseLong(playerInformation[3]));
        tiempos.add(Long.parseLong(playerInformation[4]));
        tiempos.add(Long.parseLong(playerInformation[5]));
        puntos.add(Float.parseFloat(playerInformation[6]));
        puntos.add(Float.parseFloat(playerInformation[7]));
        puntos.add(Float.parseFloat(playerInformation[8]));
        intentos.add(Integer.parseInt(playerInformation[9]));
        intentos.add(Integer.parseInt(playerInformation[10]));
        intentos.add(Integer.parseInt(playerInformation[11]));
        player = new Jugador(ID,Name, Password, tiempos, puntos, intentos, Integer.parseInt(playerInformation[12]));        
    }
    
    public void cargarDatos() throws FileNotFoundException, IOException{
        if(existePlayer()){
            LectorDArchivos reader = new LectorDArchivos(rutaP);
            String[] jugador = reader.leerLinea();
            while(!jugador[1].equals(Name)){
                jugador = reader.leerLinea();
            }
            if(jugador[2].equals(Password)){
                syncData(jugador);
                Name = "";
                Password = "";
                selected_option = 0;
            }else{
                drawText("Failed to log in", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
            }
            reader.cerrarArchivo();
        }else{
            drawText("Player doesn't exist", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
        }
    }
    
    public boolean existePlayer() throws FileNotFoundException, IOException{
        LectorDArchivos reader = new LectorDArchivos(rutaP);
        String[] linea = reader.leerLinea();
        while(linea != null){
            if(linea[1].equals(Name)){
                reader.cerrarArchivo();
                return true;
            }
            linea = reader.leerLinea();
        }
        reader.cerrarArchivo();
        return false;
    }
    
    /***
     * Escribe en pantalla el desempeño del jugador que acaba de cursar el nivel
     * @param won Si el gano el nivel o lo perdio (true = gana / false = pierde)
     * @param X pivote en X donde preferencialmente apareceran los mensajes
     * @param Y pivote en X donde preferencialmente apareceran los mensajes
     * @param ancho ancho de las imagenes (medallas)
     * @param alto alto de las imagenes (medallas)
     */
    public void hasThePlayerWon(boolean won, int X, int Y, int ancho, int alto){
        if(won){
            drawText("Congratulations "+Name+", You've passed this test!", paleta.getColores().get(paleta.getGREEN()),25, X-100, Y-50);
            graficos.drawImage(graphicRes.get(0).obtenerImagen(4).getImage(), X-25, Y, ancho, alto, null);
            for (int i = 1; i <= correctCounter; i++) {
                graficos.drawImage(graphicRes.get(0).obtenerImagen(2).getImage(), X+i*110, Y, ancho, alto, null);
            }
            drawText("You've scored: "+correctCounter+".0 since you answered "+correctCounter+" questions correctly", paleta.getColores().get(paleta.getBLACK()), 25, X-150, Y+50+alto+50);
            drawText("A new test has been unlocked!", paleta.getColores().get(paleta.getGREEN()), 30, X, Y+50+alto+150);
        }else{
            drawText("I'm afraid that something went wrong, "+Name+". You'll have to study harder!", paleta.getColores().get(paleta.getRED()),22, X-280, Y-50);
            graficos.drawImage(graphicRes.get(0).obtenerImagen(5).getImage(), X+25, Y, ancho, alto, null);
            for (int i = 1; i <= correctCounter; i++) {
                graficos.drawImage(graphicRes.get(0).obtenerImagen(2).getImage(), X+25+i*200, Y, ancho, alto, null);
            }
            drawText("You've scored: "+correctCounter+".0 since you answered "+correctCounter+" questions correctly", paleta.getColores().get(paleta.getBLACK()), 25, X-180, Y+50+alto+50);
            drawText("Take it easy, rome wasn't built in a day.", paleta.getColores().get(paleta.getRED()), 30, X-120, Y+50+alto+150);
        }
    
    }
    /***
     * Dibuja la informacion del ultimo menu (Si gano el jugador o no)
     */
    public void drawWinningMenu(){
        int X = 350;
        int Y = 150;
        int ancho = 100;
        int alto = 80;
        correctCounter = 0;
        for (int i = 0; i < 5; i++) {
            if(gatheredMedals[i]){
                correctCounter++;
            }
        }
        if(correctCounter>2){
            hasThePlayerWon(true,X,Y,ancho,alto);
        }else{
            hasThePlayerWon(false, X, Y, ancho, alto);
        }
    }
    /***
     * Verifica si la respuesta seleccionada es la correcta.
     * @param index posicion de la pregunta en el vector
     * @return      Si la opcion seleccionada es la correcta
     */
    public boolean isAnswerCorrect(int index){
        if(selected_choise==Integer.parseInt(preguntas.get(index)[4])){
            return true;
        }
        return false;
    }
    
    /***
     * Carga la explicacion del nivel (1 o 2)
     * @param level nivel a cargar
     */
    public void startLevel(int level){
        switch (level){
            case 1:
                selected_menu = 3;
                break;
            case 2:
                selected_menu = 4;
                break;
            case 3:
                selected_menu = 5;
                break;
        }
        selected_option = 0;
        selected_choise = 4;
    }
    
    public void loadNextQuestion(int level){
        if(level==1){
            questionIndex = (int) Math.floor(Math.random()*10);
        }else if(level==2){
            questionIndex = (int) Math.floor(Math.random()*10)+10;
        }else{
            questionIndex = (int) Math.floor(Math.random()*10)+20;
        }   
    }
    
    /***
     * Funcion para moverse a traves de las opciones explicaciones y las opciones de respuesta de cada nivel
     * @param firstQuestionIndex index de la primera pregunta
     * @param winningScreen index del screen de ganar/perder
     * @param direction direccion a moverse. 0 = arriba; 1 = derecha; 2 = abajo; 3 = izquierda
     * @param level nivel actual
     */
    public void moveThroughLevelOptions(int firstQuestionIndex, int winningScreen, int direction, int level){
        switch(direction){
            case 0: //up
                if(selected_choise>1 && selected_option>=firstQuestionIndex  && selected_option < winningScreen){
                    selected_choise--;
                }
                break;
            case 1: //right
                if(selected_option<firstQuestionIndex){
                    selected_option++;
                    if(selected_option==firstQuestionIndex){
                        selected_choise = 1;
                        loadNextQuestion(level);
                    }
                }
                //Save/Replay/Exit
                else if(selected_option == winningScreen){
                    if(selected_choise < 6 && correctCounter<=2){
                        selected_choise++;
                    }else if(selected_choise < 6 && correctCounter>=3){
                        selected_choise = 6;
                    }
                }
                break;
            case 2: //down
                if(selected_choise<3 && selected_option>=firstQuestionIndex && selected_option < winningScreen){
                    selected_choise++;
                }
                break;
            case 3: //left
                //Navigate through explanations
                if(selected_option!=0 && selected_option < firstQuestionIndex){
                    selected_option--;
                }
                //Save/Replay/Exit
                else if(selected_option == winningScreen){
                    if(selected_choise > 4 && correctCounter >=3){
                        selected_choise = 4;
                    }else if(selected_choise > 4 && correctCounter<=2){
                        selected_choise--;
                    }
                }
                break;
            case 4: //enter
                //Select answer
                if(selected_option>=firstQuestionIndex && selected_option < winningScreen){
                    if(isAnswerCorrect(questionIndex)){
                        gatheredMedals[selected_option-firstQuestionIndex] = true;
                    }
                    selected_choise = 1;
                    selected_option++;
                    if(selected_option == winningScreen){
                        selected_choise = 4;
                    }else{
                        loadNextQuestion(level);
                    }
                }
                //Choose option (Save/Replay/Exit)
                else if(selected_option==winningScreen){
                    switch(selected_choise){
                        case 4: //Save 
                            break;
                        case 5: //Retry
                            selected_choise = 1;
                            //If attempts > 1 then make skip available
                            break;
                        case 6: //Return to level menu
                            selected_menu = 1;
                            break;
                    }
                    selected_option = 0;
                    correctCounter = 0;
                    gatheredMedals = new boolean[5];
                }      
                break;
        }
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
                                    if(player!=null){
                                        selected_option--;
                                    }else{
                                        selected_option = 3;
                                    }
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
                                    if(player!=null){
                                        selected_option = 0;
                                    }else{
                                        selected_option = 1;
                                    }
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
                                        if(player.getPuntajes().get(0)>=3){
                                            selected_option++;
                                        }
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        if(player.getPuntajes().get(1)>=3){
                                            selected_option = 2;
                                        }
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        startLevel(1);
                                    }
                                    break;
                                case 1:
                                    if(movement == KeyEvent.VK_RIGHT){
                                        if(player.getPuntajes().get(1)>=3){
                                            selected_option++;
                                        }
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        selected_option--;
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        startLevel(2);
                                    }
                                    break;
                                case 2:
                                    if(movement == KeyEvent.VK_RIGHT){
                                        selected_option = 0;
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        selected_option--;
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        //startLevel(3);
                                    }
                                    break;
                            }
                        }
                       break;
                    case 2:
                        if(selected_option < 2){
                            if((movement == KeyEvent.VK_RIGHT || movement == KeyEvent.VK_LEFT) && player==null){
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
                                created = false;
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
                                            crearNuevoJugador();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }else if((selected_option == 4 || selected_option == 5) && player==null){
                                        try {
                                            cargarDatos();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                                lienzo.getBufferStrategy().show();
                            }
                        }
                        break;
                    case 3:
                        //Nivel 1
                        if(movement==KeyEvent.VK_UP){
                            moveThroughLevelOptions(34, 39, 0, 1);
                        }else if(movement==KeyEvent.VK_RIGHT){
                            moveThroughLevelOptions(34, 39, 1, 1);
                        }else if(movement==KeyEvent.VK_DOWN){
                            moveThroughLevelOptions(34, 39, 2, 1);
                        }else if(movement==KeyEvent.VK_LEFT){
                            moveThroughLevelOptions(34, 39, 3, 1);
                        }else if(movement==KeyEvent.VK_ENTER){
                            moveThroughLevelOptions(34, 39, 4, 1);
                        }
                        break;
                    case 4:
                        //Nivel 2
                        if(movement==KeyEvent.VK_UP){
                            moveThroughLevelOptions(12, 17, 0, 2);
                        }else if(movement==KeyEvent.VK_RIGHT){
                            moveThroughLevelOptions(12, 17, 1, 2);
                        }else if(movement==KeyEvent.VK_DOWN){
                            moveThroughLevelOptions(12, 17, 2, 2);
                        }else if(movement==KeyEvent.VK_LEFT){
                            moveThroughLevelOptions(12, 17, 3, 2);
                        }else if(movement==KeyEvent.VK_ENTER){
                            moveThroughLevelOptions(12, 17, 4, 2);
                        }
                        break;
                    case 5:
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
