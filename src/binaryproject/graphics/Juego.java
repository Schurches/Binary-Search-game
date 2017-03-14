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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
    private int selected_menu; //0 = main, 1 = levels, 2 = login/register, 3 = nivel1, 4 = nivel2, 5 = nivel3
    private Colores paleta;
    //Niveles
    private ArrayList<String[]> preguntas;
    private ArrayList<String[]> playerLogs;
    private boolean[] questionWasAlreadyShown;
    private boolean[][] gatheredMedals;
    private int correctCounter;
    private int questionIndex;
    //Jugadores
    ArrayList<String[]> jugadores;
    private Jugador player;
    //Cuenta
    private String Name;
    private String Code;
    private String Password;
    private String rutaArchivos;
    private boolean created;
    private boolean taken;
    //Constantes
    private final int choiseX;
    private final int choiseY;
    private long timeEntrada;
    
    public Juego(ArrayList<ArrayList<ImageIcon>> IRes, String BDJugadores, ArrayList<String[]> rutaBancoP, ArrayList<String[]> players, String rutaArchivos, ArrayList<String[]> sesiones){
        //Frame
        this.ancho_mapa = 1280;
        this.alto_mapa = getAncho_mapa() / 16*9;
        inicializarFrame();
        choiseX = 70;
        choiseY = 400;
        //Recursos
        graphicRes = new ArrayList<Imagenes>();
        Imagenes I;
        for (int i = 0; i <= 1; i++) {
            I = new Imagenes(IRes.get(i));
            graphicRes.add(I);
        }
        preguntas = rutaBancoP;
        paleta = new Colores();
        gatheredMedals = new boolean[3][5];
        //Cuentas
        Name = "";
        Code = "";
        Password = "";
        this.rutaArchivos = rutaArchivos;
        this.questionWasAlreadyShown = new boolean[30];
        created = false;
        taken = false;
        //Acciones
        selected_option = 1;
        selected_menu = 0;
        selected_choise = 4;
        jugadores = players;
        playerLogs = sesiones;
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
        lista.add(new Botones(1, 600, 350, 100, 120, graphicRes.get(0).obtenerImagen(9)));
        lista.add(new Botones(2, 800, 350, 100, 100, graphicRes.get(0).obtenerImagen(14)));
        lista.add(new Botones(3, 580, 500, 150, 150, graphicRes.get(0).obtenerImagen(8)));
        menu.add(lista);
        //Botones menu 1 (Niveles)
        lista = new ArrayList<Botones>();
        lista.add(new Botones(0, 240, 350, 120, 120, graphicRes.get(0).obtenerImagen(10)));
        lista.add(new Botones(1, 600, 350, 120, 120, graphicRes.get(0).obtenerImagen(11), graphicRes.get(0).obtenerImagen(13)));
        lista.add(new Botones(2, 950, 350, 120, 120, graphicRes.get(0).obtenerImagen(12), graphicRes.get(0).obtenerImagen(13)));
        menu.add(lista);
        //Botones menu 2 (Accounts)
        lista = new ArrayList<Botones>();
        lista.add(new Botones(0, 300, 300, 140, 140, graphicRes.get(0).obtenerImagen(15)));
        lista.add(new Botones(1, 850, 300, 140, 140, graphicRes.get(0).obtenerImagen(16), graphicRes.get(0).obtenerImagen(13)));
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
        int tiempo;
        if(player!=null){
            tiempo = 0;
            tiempo = (int) Math.floor(player.getTiempos().get(0)[2]);
            tiempo = tiempo / 1000;
            drawText("Score: " + player.getPuntajes().get(0), paleta.getColores().get(paleta.BLUE), 20, 240, 530);
            drawText("Time: " + tiempo +" seg", paleta.getColores().get(paleta.BLUE), 20, 240, 550);
            drawText("Attempts: " + player.getIntentos().get(0), paleta.getColores().get(paleta.BLUE), 20, 240, 570);
            if(player.getIntentos().get(1) != 0){
                tiempo = 0;
                tiempo = (int) Math.floor(player.getTiempos().get(1)[2]);
                tiempo = tiempo / 1000;
                drawText("Score: " + player.getPuntajes().get(1), paleta.getColores().get(paleta.BLUE), 20, 590, 530);
                drawText("Time: " + tiempo +" segs", paleta.getColores().get(paleta.BLUE), 20, 590, 550);
                drawText("Attempts: " + player.getIntentos().get(1), paleta.getColores().get(paleta.BLUE), 20, 590, 570);
            }
            if(player.getIntentos().get(2) != 0){
                tiempo = (int) Math.floor(player.getTiempos().get(2)[2]);
                tiempo = tiempo / 1000;
                drawText("Score: " + player.getPuntajes().get(2), paleta.getColores().get(paleta.BLUE), 20, 940, 530);
                drawText("Time: " + tiempo + " segs", paleta.getColores().get(paleta.BLUE), 20, 940, 550);
                drawText("Attempts: " + player.getIntentos().get(2), paleta.getColores().get(paleta.BLUE), 20, 940, 570);
            }
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
                drawText("Leaderboard", paleta.getColores().get(paleta.getBLACK()), 20, 585, 330);
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
                    if(selected_option < 5){
                        drawText("Registration", paleta.getColores().get(paleta.getBLACK()), 50, 500, 100);
                        drawText("Codigo", paleta.getColores().get(paleta.getBLACK()), 30, 400, 400);
                        drawText(Code+"", paleta.getColores().get(paleta.RED), 20, 410, 420);
                        drawText("Password", paleta.getColores().get(paleta.getBLACK()), 30, 400, 520);
                        drawText(censor, paleta.getColores().get(paleta.RED), 20, 410, 540);
                        if(created){
                            drawText("New player registered!", paleta.getColores().get(paleta.getGREEN()), 25, 0, 20);
                        }else if(taken){
                            drawText("An account already exists with this name!", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
                        }
                    }else if(selected_option >= 4){
                        drawText("Login", paleta.getColores().get(paleta.getBLACK()), 50, 500, 100);
                        drawText("Password", paleta.getColores().get(paleta.getBLACK()), 30, 400, 400);
                        drawText(censor, paleta.getColores().get(paleta.RED), 20, 410, 420);
                    }
                    drawText("Username", paleta.getColores().get(paleta.getBLACK()), 30, 400, 280);
                    drawText(Name, paleta.getColores().get(paleta.RED), 20, 410, 300);   
                }
                break;
            case 3:
                //Nivel 1
                if(selected_option<34){
                    explicaciones(1);
                }else{
                    quiz(1);
                }
                break;//////////////////////////////
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
                    botones.get(i).dibujar(graficos, selected_choise,false);
                }
            }
        }else if((selected_menu<3) && (selected_menu!=2 || selected_option<2)){
            botones = menu.get(selected_menu);
            if(selected_menu==0){
                for (int i = 0; i < botones.size(); i++) {
                    botones.get(i).dibujar(graficos, selected_option, false);
                }
            }else{
                botones.get(0).dibujar(graficos, selected_option, false);
            }
            if(selected_menu == 1){
                if(player.getPuntajes().get(0) > 2){
                    botones.get(1).dibujar(graficos, selected_option, false);
                }else{
                    botones.get(1).dibujar(graficos, selected_option, true);
                }
                if(player.getPuntajes().get(1) > 2){
                    botones.get(2).dibujar(graficos, selected_option, false);
                }else{
                    botones.get(2).dibujar(graficos, selected_option, true);
                }
            }else if(selected_menu == 2){
                if(player!=null){
                    botones.get(1).dibujar(graficos, selected_option, true);
                }else{
                    botones.get(1).dibujar(graficos, selected_option, false);
                }
            }
        }
        lienzo.getBufferStrategy().show();
    }
    
    public void showQuestion(int firstQuestionPage, int winningPage, int fontSize){
        if(selected_option>=firstQuestionPage && selected_option<winningPage){
            int questionNumb = selected_option-firstQuestionPage;
            String question = preguntas.get(questionIndex)[0];
            if(question.length()>180){
                drawText((questionNumb+1)+"): "+question.split(":")[0], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX-10, choiseY-100);
                drawText(question.split(":")[1], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX+20, choiseY+(fontSize+5)-100);
                drawText(question.split(":")[2], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX+20, choiseY+2*(fontSize+5)-100);
            }else if(question.length()>80){
                drawText((questionNumb+1)+"): "+question.split(":")[0], paleta.getColores().get(paleta.getBLACK()), fontSize+5, choiseX-10, choiseY-100);
                drawText(question.split(":")[1], paleta.getColores().get(paleta.getBLACK()), fontSize+5, choiseX+20, choiseY+(fontSize+5)-100);
            }else{
                drawText((questionNumb+1)+"): "+question, paleta.getColores().get(paleta.getBLACK()), fontSize+5, choiseX-10, choiseY-100);
            }
            drawText("a): "+preguntas.get(questionIndex)[1], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX, choiseY);
            drawText("b): "+preguntas.get(questionIndex)[2], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX, choiseY+50);
            drawText("c): "+preguntas.get(questionIndex)[3], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX, choiseY+100);
            if(firstQuestionPage==0){
                drawText("d): "+preguntas.get(questionIndex)[4], paleta.getColores().get(paleta.getBLACK()), fontSize, choiseX, choiseY+150);
            }
            switch(winningPage){
                case 39:
                    drawMedals(questionNumb,0);
                    break;
                case 17:
                    drawMedals(questionNumb,1);
                    break;
                case 5:
                    drawMedals(questionNumb,2);
                    break;
            }
            drawSelectedChoise();
        }else if(selected_option==winningPage){
            switch(winningPage){
                case 39:
                    drawWinningMenu(0);
                    break;
                case 17:
                    drawWinningMenu(1);
                    break;
                case 5:
                    drawWinningMenu(2);
                    break;
            }   
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
                showQuestion(0, 5, 20);
                break;
        }
    }
    
    public void drawSelectedChoise(){
        graficos.drawImage(graphicRes.get(0).obtenerImagen(3).getImage(), choiseX-4, (choiseY-18)+((selected_choise-1)*50),22,22,null);
    }
    
    public void drawMedals(int questionNumber, int level){
        int X;
        int Y = 50;
        int ancho = 100;
        int alto = 80;
        for (int i = 0; i < 5; i++) {
            X = 200+2*i*ancho;
            if(!gatheredMedals[level][i]){
                graficos.drawImage(graphicRes.get(0).obtenerImagen(1).getImage(), X, Y, ancho,alto,null);
            }else{
                graficos.drawImage(graphicRes.get(0).obtenerImagen(2).getImage(), X, Y, ancho,alto,null);
            }
        }
    }
    
    public void explicaciones(int nivel){
        if(nivel==1){
            graficos.drawImage(graphicRes.get(1).obtenerImagen(selected_option).getImage(), 0, 0, ancho_mapa-10, alto_mapa-10, null);
            if(player.getIntentos().get(0)>1 || selected_option > 24){
                drawText("Press 'S' to skip explanation", paleta.getColores().get(paleta.getBLUE()), 20, 0, 20);
            }
        }else if(nivel==2){
            graficos.drawImage(graphicRes.get(1).obtenerImagen(selected_option+35).getImage(), 0, 0, ancho_mapa-10, alto_mapa-10, null);
            if(player.getIntentos().get(1)>1){
                drawText("Press 'S' to skip explanation", paleta.getColores().get(paleta.getBLUE()), 20, 0, 20);
            }
        }
    }
    
    public void crearNuevoJugador() throws IOException{
        int ID = existePlayer(Name);
        if(ID == jugadores.size()){
            String newPlayer = Code+","+Name+",0,"+Password+",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0";
            jugadores.add(newPlayer.split(","));
            created = true;
            taken = false;
        }else{
            taken = true;
        }
    }
    
    public Jugador toPlayerData(String[] playerInformation){
        ArrayList<Long[]> tiempos = new ArrayList<Long[]>();
        ArrayList<Float> puntos = new ArrayList<Float>();
        ArrayList<Integer> intentos = new ArrayList<Integer>();
        Long[] time = new Long[3];
        time[0] = Long.parseLong(playerInformation[4]);
        time[1] = Long.parseLong(playerInformation[5]);
        time[2] = Long.parseLong(playerInformation[6]);
        tiempos.add(time);
        time = new Long[3];
        time[0] = Long.parseLong(playerInformation[7]);
        time[1] = Long.parseLong(playerInformation[8]);
        time[2] = Long.parseLong(playerInformation[9]);
        tiempos.add(time);
        time = new Long[3];
        time[0] = Long.parseLong(playerInformation[10]);
        time[1] = Long.parseLong(playerInformation[11]);
        time[2] = Long.parseLong(playerInformation[12]);
        tiempos.add(time);
        puntos.add(Float.parseFloat(playerInformation[13]));
        puntos.add(Float.parseFloat(playerInformation[14]));
        puntos.add(Float.parseFloat(playerInformation[15]));
        intentos.add(Integer.parseInt(playerInformation[16]));
        intentos.add(Integer.parseInt(playerInformation[17]));
        intentos.add(Integer.parseInt(playerInformation[18]));
        boolean[] writenInf = new boolean[3];
        for (int i = 0; i < 3; i++) {
            if(playerInformation[20+i].equals(1)){
                writenInf[i] = true;
            }else{
                writenInf[i] = false;
            }
        }
        return new Jugador(Integer.parseInt(playerInformation[0]), playerInformation[1], Long.parseLong(playerInformation[2]), playerInformation[3], tiempos, puntos, intentos, Float.parseFloat(playerInformation[19]), writenInf);        
    }
    
    public void actualizarAchivoJugadoreS() throws IOException{
        EscritorDArchivos writer = new EscritorDArchivos(rutaArchivos+"jugadores.txt",false); //Esto es para que el archivo se vuelva a crear
        writer.cerrar(); 
        writer = new EscritorDArchivos(rutaArchivos+"jugadores.txt", true); //ahora si se escribe en el
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador Temp = toPlayerData(jugadores.get(i));
            writer.escribir(Temp.toString());
        }
        writer.cerrar();
    }
    
    public void actualizarDatos() throws IOException{
        player.actualizarPuntaje();
        int playerID = existePlayer(player.getNombre());
        jugadores.set(playerID, player.playerInfoAsArray()); //correjir esto
       actualizarAchivoJugadoreS();
    }
    
    public void cargarDatos(){
        int ID = existePlayer(Name);
        if(ID == jugadores.size()){
            drawText("Player doesn't exist", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
        }else{
            if(jugadores.get(ID)[3].equals(Password)){
                player = toPlayerData(jugadores.get(ID));
                player.setLastTimeLogin(System.currentTimeMillis());
                Name = "";
                Password = "";
                selected_option = 0;
            }else{
                drawText("Failed to log in", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
            }
        }
    }
    
    public int existePlayer(String playerName){
        boolean found = false;
        int index = 0;
        while(index < jugadores.size() && !found){
            if(jugadores.get(index)[1].equals(playerName)){
                return index;
            }else{
                index++;
            }
        }
        return index;
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
            drawText("Congratulations "+player.getNombre()+", You've passed this test!", paleta.getColores().get(paleta.getGREEN()),25, X-100, Y-50);
            graficos.drawImage(graphicRes.get(0).obtenerImagen(4).getImage(), X-25, Y, ancho, alto, null);
            for (int i = 1; i <= correctCounter; i++) {
                graficos.drawImage(graphicRes.get(0).obtenerImagen(2).getImage(), X+i*110, Y, ancho, alto, null);
            }
            drawText("You've scored: "+correctCounter+".0 since you answered "+correctCounter+" questions correctly", paleta.getColores().get(paleta.getBLACK()), 25, X-150, Y+50+alto+50);
            drawText("A new test has been unlocked!", paleta.getColores().get(paleta.getGREEN()), 30, X, Y+50+alto+150);
        }else{
            drawText("I'm afraid that something went wrong, "+player.getNombre()+". You'll have to study harder!", paleta.getColores().get(paleta.getRED()),22, X-280, Y-50);
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
    public void drawWinningMenu(int level){
        int X = 350;
        int Y = 150;
        int ancho = 100;
        int alto = 80;
        correctCounter = 0;
        for (int i = 0; i < 5; i++) {
            if(gatheredMedals[level][i]){
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
    public boolean isAnswerCorrect(int index, int correctQuestionIndex){
        if(selected_choise==Integer.parseInt(preguntas.get(index)[correctQuestionIndex])){
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
                selected_choise = 4;
                break;
            case 2:
                selected_menu = 4;
                selected_choise = 4;
                break;
            case 3:
                loadNextQuestion(3);
                selected_menu = 5;
                selected_choise = 1;
                break;
        }
        questionWasAlreadyShown = new boolean[30];
        selected_option = 0;
    }
    
    public void loadNextQuestion(int level){
        if(level==1){
            do{
                questionIndex = (int) Math.floor(Math.random()*10);
            }while(questionWasAlreadyShown[questionIndex]);
            questionWasAlreadyShown[questionIndex] = true;
        }else if(level==2){
            do{
                questionIndex = (int) Math.floor(Math.random()*10)+10;
            }while(questionWasAlreadyShown[questionIndex]);
            questionWasAlreadyShown[questionIndex] = true;
        }else{
            do{
                questionIndex = (int) Math.floor(Math.random()*10)+20;
            }while(questionWasAlreadyShown[questionIndex]);
            questionWasAlreadyShown[questionIndex] = true;
        }   
    }
    
    /***
     * Funcion para moverse a traves de las opciones explicaciones y las opciones de respuesta de cada nivel
     * @param firstQuestionIndex index de la primera pregunta
     * @param winningScreen index del screen de ganar/perder
     * @param direction direccion a moverse. 0 = arriba; 1 = derecha; 2 = abajo; 3 = izquierda
     * @param level nivel actual
     */
    public void moveThroughLevelOptions(int firstQuestionIndex, int winningScreen, int direction, int level) throws IOException{
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
                if(firstQuestionIndex==0){
                    if(selected_choise<4 && selected_option>=firstQuestionIndex && selected_option < winningScreen){
                        selected_choise++;
                    }
                }else{
                    if(selected_choise<3 && selected_option>=firstQuestionIndex && selected_option < winningScreen){
                        selected_choise++;
                    }
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
                    boolean isCorrect;
                    if(firstQuestionIndex==0){
                        isCorrect = isAnswerCorrect(questionIndex,5);
                    }else{
                        isCorrect = isAnswerCorrect(questionIndex,4);
                    }
                    if(isCorrect){
                        gatheredMedals[level-1][selected_option-firstQuestionIndex] = true;
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
                            //If the progress is saved at level 1
                            if(winningScreen == 39){
                                if(correctCounter > player.getPuntajes().get(0)){
                                    String correctas = correctCounter+".0";
                                    player.getPuntajes().set(0, Float.parseFloat(correctas));
                                    if(correctCounter > 2){
                                        selected_menu = 1;
                                        selected_option = 0;
                                        correctCounter = 0;
                                        player.getTiempos().get(0)[1] = System.currentTimeMillis();
                                        player.getTiempos().get(0)[2] = player.getTiempos().get(0)[1] - player.getTiempos().get(0)[0];
                                    }
                                }
                                actualizarDatos();
                            }else if(winningScreen == 17){ //If the progress is saved at level 2
                                if(correctCounter > player.getPuntajes().get(1)){
                                    String correctas = correctCounter+".0";
                                    player.getPuntajes().set(1, Float.parseFloat(correctas));
                                    if(correctCounter>2){
                                        selected_menu = 1;
                                        selected_option = 0;
                                        correctCounter = 0;
                                        player.getTiempos().get(1)[1] = System.currentTimeMillis();
                                        player.getTiempos().get(1)[2] = player.getTiempos().get(1)[1] - player.getTiempos().get(1)[0];
                                    }
                                }
                                actualizarDatos();
                            }else if(winningScreen == 5){ //If the progress is saved at level 3
                                if(correctCounter > player.getPuntajes().get(2)){
                                    String correctas = correctCounter+".0";
                                    player.getPuntajes().set(2, Float.parseFloat(correctas));
                                    if(correctCounter>2){
                                        selected_menu = 1;
                                        selected_option = 0;
                                        correctCounter = 0;
                                        player.getTiempos().get(2)[1] = System.currentTimeMillis();
                                        player.getTiempos().get(2)[2] = player.getTiempos().get(2)[1] - player.getTiempos().get(2)[0];
                                    }
                                }
                                actualizarDatos();
                            }
                            break;
                        case 5: //Retry
                            selected_option = 0;
                            selected_choise = 1;
                            if(winningScreen==39){
                                player.getIntentos().set(0, player.getIntentos().get(0)+1);
                                gatheredMedals[0] = new boolean[5];
                            }else if(winningScreen==17){
                                player.getIntentos().set(1, player.getIntentos().get(1)+1);
                                gatheredMedals[1] = new boolean[5];
                            }else if(winningScreen==5){
                                player.getIntentos().set(2, player.getIntentos().get(2)+1);
                                gatheredMedals[2] = new boolean[5];
                            }
                            correctCounter = 0;
                            questionWasAlreadyShown = new boolean[30];
                            break;
                        case 6: //Return to level menu
                            if(selected_option==39){
                                player.getTiempos().get(0)[1] = System.currentTimeMillis();
                                player.getTiempos().get(0)[2] = player.getTiempos().get(0)[1] - player.getTiempos().get(0)[0];
                            }else if(selected_option==17){
                                player.getTiempos().get(1)[1] = System.currentTimeMillis();
                                player.getTiempos().get(1)[2] = player.getTiempos().get(1)[1] - player.getTiempos().get(1)[0];
                            }else if(selected_option==5){
                                player.getTiempos().get(2)[1] = System.currentTimeMillis();
                                player.getTiempos().get(2)[2] = player.getTiempos().get(2)[1] - player.getTiempos().get(2)[0];
                            }
                            selected_menu = 1;
                            selected_option = 0;
                            selected_choise = 1;
                            correctCounter = 0;
                            actualizarDatos();
                            break;
                    }
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
                                    try {
                                        escribirExcel();
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
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
                                        if(player.getPuntajes().get(0)<3){
                                            gatheredMedals[0] = new boolean[5];
                                            startLevel(1);
                                            player.getTiempos().get(0)[0] = System.currentTimeMillis();
                                            player.getIntentos().set(0, player.getIntentos().get(0)+1);
                                        }else{
                                            drawText("You have passed this level already", paleta.getColores().get(paleta.getRED()), 20, 0, 20);
                                            lienzo.getBufferStrategy().show();
                                        }
                                    }
                                    break;
                                case 1:
                                    if(movement == KeyEvent.VK_RIGHT){
                                        selected_option++;
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        selected_option--;
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        if(player.getPuntajes().get(0)>2 && player.getPuntajes().get(1)<3){
                                            gatheredMedals[1] = new boolean[5];
                                            startLevel(2);
                                            player.getTiempos().get(1)[0] = System.currentTimeMillis();
                                            player.getIntentos().set(1, player.getIntentos().get(1)+1);
                                        }else{
                                            drawText("You have passed this level already", paleta.getColores().get(paleta.getRED()), 20, 0, 20);
                                            lienzo.getBufferStrategy().show();
                                        }
                                    }
                                    break;
                                case 2:
                                    if(movement == KeyEvent.VK_RIGHT){
                                        selected_option = 0;
                                    }else if(movement == KeyEvent.VK_LEFT){
                                        selected_option--;
                                    }else if (movement == KeyEvent.VK_ENTER){
                                        if(player.getPuntajes().get(1) > 2 && player.getPuntajes().get(2) < 3){
                                            gatheredMedals[2] = new boolean[5];
                                            startLevel(3);
                                            player.getTiempos().get(2)[0] = System.currentTimeMillis();
                                            player.getIntentos().set(2, player.getIntentos().get(2)+1);
                                        }else{
                                            drawText("You have passed this level already", paleta.getColores().get(paleta.getRED()), 20, 0, 20);
                                            lienzo.getBufferStrategy().show();
                                        }
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
                                if(selected_option==0){
                                        selected_option = 2;
                                }else{
                                    if(player==null){
                                        selected_option = 5;
                                    }
                                }
                            }else if(movement == KeyEvent.VK_ESCAPE){
                                selected_menu = 0;
                                selected_option = 2;
                            }
                        }else if(selected_option >= 2){
                            
                            if(movement == KeyEvent.VK_ESCAPE){
                                selected_option = 0;
                                Name = "";
                                Code = "";
                                Password = "";
                                created = false;
                            }
                            
                            if(movement == KeyEvent.VK_RIGHT){
                                if(selected_option < 4){
                                    selected_option++;
                                }else if(selected_option >= 5 && selected_option < 6){
                                    selected_option++;
                                }
                            }
                            
                            if(movement == KeyEvent.VK_LEFT){
                                if(selected_option > 2 && selected_option <= 4){
                                    selected_option--; 
                                }else if(selected_option > 5 && selected_option <= 6){
                                    selected_option--;
                                }
                            }
                            
                            else if(movement >= KeyEvent.VK_A && movement <= KeyEvent.VK_Z || movement>=KeyEvent.VK_0 && movement <= KeyEvent.VK_9){
                                if((selected_option == 2 || selected_option == 5) && Name.length()<30){
                                    Name+=tecla.getKeyChar();
                                }else if(selected_option == 3 && movement>=KeyEvent.VK_0 && movement <= KeyEvent.VK_9 && Code.length()<10){
                                    Code+=tecla.getKeyChar();
                                }else if (selected_option == 4 || selected_option == 6){
                                    Password+=tecla.getKeyChar();
                                }
                            }
                            
                            else if(movement == KeyEvent.VK_BACK_SPACE){
                                if(selected_option == 2 || selected_option == 5){
                                    if(Name.length()!=0){
                                        Name = Name.substring(0, Name.length()-1);
                                    }
                                }else if(selected_option==3){
                                    if(Code.length()!=0){
                                        Code = Code.substring(0, Code.length()-1);
                                    }
                                }
                                else if (selected_option == 4 || selected_option == 6){
                                    if(Password.length()!=0){
                                        Password = Password.substring(0, Password.length()-1);
                                    }
                                }
                            }
                            
                            else if(movement == KeyEvent.VK_ENTER){
                                if(Name.length() == 0 || Password.length() == 0 || (Code.length() < 10 && selected_option < 5)){
                                    if(Code.length()<10){
                                        if((Name.length() != 0 || Password.length() != 0)){
                                            drawText("Fill your 10 digit code", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
                                        }else{
                                            drawText("Fill all fields", paleta.getColores().get(paleta.getRED()), 25, 0, 20);
                                        }
                                    }
                                }else{
                                    if(selected_option == 2 || selected_option == 3 || selected_option == 4){
                                        try {
                                            crearNuevoJugador();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    }else if((selected_option == 5 || selected_option == 6) && player==null){
                                        cargarDatos();
                                        timeEntrada = System.currentTimeMillis();
                                    }
                                }
                                lienzo.getBufferStrategy().show();
                            }
                        }
                        break;
                    case 3:
                            //If attempts > 1 then make skip available
                        //Nivel 1
                        try{
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
                            }else if(movement==KeyEvent.VK_S && ((selected_option==0 && player.getIntentos().get(0)>1)||(selected_option>24 && selected_option < 34))){
                                selected_option = 34;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                            //If attempts > 1 then make skip available
                        //Nivel 2
                        try{
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
                            }else if(movement==KeyEvent.VK_S && ((selected_option > 0 && selected_option < 16) && player.getIntentos().get(0)>1)){
                                selected_option = 34;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case 5:
                        try{
                            if(movement==KeyEvent.VK_UP){
                                moveThroughLevelOptions(0, 5, 0, 3);
                            }else if(movement==KeyEvent.VK_RIGHT){
                                moveThroughLevelOptions(0, 5, 1, 3);
                            }else if(movement==KeyEvent.VK_DOWN){
                                moveThroughLevelOptions(0, 5, 2, 3);
                            }else if(movement==KeyEvent.VK_LEFT){
                                moveThroughLevelOptions(0, 5, 3, 3);
                            }else if(movement==KeyEvent.VK_ENTER){
                                moveThroughLevelOptions(0, 5, 4, 3);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
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
        
        this.addWindowListener(new WindowAdapter() {
            
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    escribirExcel();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                
            }
        }
        );
        
   }
    
    public boolean hasPlayerPastLogs(String player){
        int logIndex = 0;
        while(logIndex < playerLogs.size()){
            if(playerLogs.get(logIndex)[1].equals(player)){
                return true;
            }
            logIndex++;
        }
        return false;
    }
     
    public ArrayList<Integer> getPlayerSesionDataIndexes(String player){
        ArrayList<Integer> playerHistory = new ArrayList<Integer>();
        for (int i = 0; i < playerLogs.size(); i++) {
            if(playerLogs.get(i)[1].equals(player)){
                playerHistory.add(i);
            }
        }
        return playerHistory;
    }
    
    public void updateLogs(){
        
    }
    
    public int getAmmountOfCorrectQuestions(int level){
        int ammount = 0;
        for (int i = 0; i < 5; i++) {
            if(gatheredMedals[level-1][i]){
                ammount++;
            }
        }
        return ammount;
    }
    
    public String getCorrectQuestionsIndividually(int level){
        String correctAnswers = "";
        for (int i = 0; i < 5; i++) {
            if(gatheredMedals[level-1][i]){
                correctAnswers+="1;";
            }else{
                correctAnswers+="0;";
            }
        }
        return correctAnswers;
    }
    
    public void escribirExcel() throws FileNotFoundException, IOException, InvalidFormatException{
        long exitTime = System.currentTimeMillis();
        if(player!=null){
            // Actualiza el archivo de jugadores con la informacion que habia antes de cerrarse
            actualizarAchivoJugadoreS(); 
            // Actualiza el archivo del registro de sesiones
            EscritorDArchivos writer = new EscritorDArchivos(rutaArchivos+"logs.txt", true);
            String justAnotherLogForAnExistingPlayer = "";
            if(hasPlayerPastLogs(player.getNombre())){
                ArrayList<Integer> logHistoryIndex = getPlayerSesionDataIndexes(player.getNombre());
                boolean[] loggedQuestions = new boolean[3];
                int i = 0;
                while(!loggedQuestions[0] && i < logHistoryIndex.size()){
                    int puntaje = Integer.parseInt(playerLogs.get(logHistoryIndex.get(i))[11]);
                    if(puntaje > 3){
                        loggedQuestions[0] = true;
                    }
                    i++;
                }
                i = 0;
                while(!loggedQuestions[1] && i < logHistoryIndex.size()){
                    int puntaje = Integer.parseInt(playerLogs.get(logHistoryIndex.get(i))[21]);
                    if(puntaje > 3){
                        loggedQuestions[1] = true;
                    }
                    i++;
                }
                i = 0;
                while(!loggedQuestions[2] && i < logHistoryIndex.size()){
                    int puntaje = Integer.parseInt(playerLogs.get(logHistoryIndex.get(i))[31]);
                    if(puntaje > 3){
                        loggedQuestions[2] = true;
                    }
                    i++;
                }
                justAnotherLogForAnExistingPlayer
                        +=player.getID()+";"+
                        player.getNombre()+";"+
                        (playerLogs.size()+1)+";"+
                        player.getLastTimeLogin()+";"+
                        exitTime+";"+
                        (exitTime-player.getLastTimeLogin())+";";
                if(!loggedQuestions[0]){
                    justAnotherLogForAnExistingPlayer+=getCorrectQuestionsIndividually(1);
                    justAnotherLogForAnExistingPlayer+=getAmmountOfCorrectQuestions(1)+";";
                    justAnotherLogForAnExistingPlayer+=player.getTiempos().get(0)[0]+";"+player.getTiempos().get(0)[1]+";"+player.getTiempos().get(0)[2]+";";
                }else{
                    justAnotherLogForAnExistingPlayer+="0;0;0;0;0;0;";
                    justAnotherLogForAnExistingPlayer+="0;0;0;";
                }
                //Agregar STN1
                long STN = 0;
                if(logHistoryIndex.size()!=0 && !loggedQuestions[0]){
                    for (int j = 0; j < logHistoryIndex.size(); j++) {
                        STN += Long.parseLong(playerLogs.get(logHistoryIndex.get(j))[14]);
                    }
                    STN+=player.getTiempos().get(0)[2];
                    if(STN!=0){
                        player.changeWritenState(0, true);
                    }
                }
                justAnotherLogForAnExistingPlayer+=STN+";";
                if(!loggedQuestions[1]){
                    justAnotherLogForAnExistingPlayer+=getCorrectQuestionsIndividually(2);
                    justAnotherLogForAnExistingPlayer+=getAmmountOfCorrectQuestions(2)+";";
                    justAnotherLogForAnExistingPlayer+=player.getTiempos().get(1)[0]+";"+player.getTiempos().get(1)[1]+";"+player.getTiempos().get(1)[2]+";";
                }else{
                    justAnotherLogForAnExistingPlayer+="0;0;0;0;0;0;";
                }
                //Agregar SNT2
                STN = 0;
                if(logHistoryIndex.size()!=0 && !loggedQuestions[1]){
                    for (int j = 0; j < logHistoryIndex.size(); j++) {
                        STN += Long.parseLong(playerLogs.get(logHistoryIndex.get(j))[24]);
                    }
                    STN+=player.getTiempos().get(1)[2];
                    if(STN!=0){
                        player.changeWritenState(1, true);
                    }
                }
                justAnotherLogForAnExistingPlayer+=STN+";";
                if(!loggedQuestions[2]){
                    justAnotherLogForAnExistingPlayer+=getCorrectQuestionsIndividually(3);
                    justAnotherLogForAnExistingPlayer+=getAmmountOfCorrectQuestions(3)+";";
                    justAnotherLogForAnExistingPlayer+=player.getTiempos().get(2)[0]+";"+player.getTiempos().get(2)[1]+";"+player.getTiempos().get(2)[2]+";";
                }else{
                    justAnotherLogForAnExistingPlayer+="0;0;0;0;0;0;";
                }
                //Agregar SNT3
                STN = 0;
                if(logHistoryIndex.size()!=0 && !loggedQuestions[2]){
                    for (int j = 0; j < logHistoryIndex.size(); j++) {
                        STN += Long.parseLong(playerLogs.get(logHistoryIndex.get(j))[34]);
                    }
                    STN+=player.getTiempos().get(2)[2];
                    if(STN!=0){
                        player.changeWritenState(2, true);
                    }
                }
                justAnotherLogForAnExistingPlayer+=STN+";";
                //Cargar el CT Total
                justAnotherLogForAnExistingPlayer+=player.getScore()+";";
                long spentTime = 0;
                for (int j = 0; j < logHistoryIndex.size(); j++) {
                        spentTime += Long.parseLong(playerLogs.get(logHistoryIndex.get(j))[5]);
                }
                justAnotherLogForAnExistingPlayer+=(spentTime+(exitTime-player.getLastTimeLogin()));
            }else{
                justAnotherLogForAnExistingPlayer
                        +=player.getID()+";"+
                        player.getNombre()+";"+
                        (playerLogs.size()+1)+";"+
                        player.getLastTimeLogin()+";"+
                        exitTime+";"+
                        (exitTime-player.getLastTimeLogin())+";";
                //Terminar de rellenar con el resto de informacion de toda la sesion
                justAnotherLogForAnExistingPlayer+=getCorrectQuestionsIndividually(1);
                justAnotherLogForAnExistingPlayer+=getAmmountOfCorrectQuestions(1)+";";
                justAnotherLogForAnExistingPlayer+=player.getTiempos().get(0)[0]+";"+player.getTiempos().get(0)[1]+";"+player.getTiempos().get(0)[2]+";";
                justAnotherLogForAnExistingPlayer+=player.getTiempos().get(0)[2]+";"; //STN1
                justAnotherLogForAnExistingPlayer+=getCorrectQuestionsIndividually(2);
                justAnotherLogForAnExistingPlayer+=getAmmountOfCorrectQuestions(2)+";";
                justAnotherLogForAnExistingPlayer+=player.getTiempos().get(1)[0]+";"+player.getTiempos().get(1)[1]+";"+player.getTiempos().get(1)[2]+";";
                justAnotherLogForAnExistingPlayer+=player.getTiempos().get(1)[2]+";"; //STN2
                justAnotherLogForAnExistingPlayer+=getCorrectQuestionsIndividually(3);
                justAnotherLogForAnExistingPlayer+=getAmmountOfCorrectQuestions(3)+";";
                justAnotherLogForAnExistingPlayer+=player.getTiempos().get(2)[0]+";"+player.getTiempos().get(2)[1]+";"+player.getTiempos().get(2)[2]+";";
                justAnotherLogForAnExistingPlayer+=player.getTiempos().get(2)[2]+";"; //STN3
                justAnotherLogForAnExistingPlayer+=player.getScore()+";";
                justAnotherLogForAnExistingPlayer+=exitTime-player.getLastTimeLogin();
            }
            writer.escribir(justAnotherLogForAnExistingPlayer);
            writer.cerrar();
            ////////////////////////////////////////////////////////////////////
            ///////////Pasar la infrmacion a archivo excel//////////////////////
            ////////////////////////////////////////////////////////////////////
            LectorDArchivos reader = new LectorDArchivos(rutaArchivos+"logs.txt");
            XSSFWorkbook libro = new XSSFWorkbook();
            XSSFSheet hoja = libro.createSheet("Test");
            XSSFRow row;
            String[] informacion;
            String cellValue;
            int rowNumber = 0;
            reader.leerLinea();
            while(reader.getLineaActual()!=null){
                informacion = reader.getLineaActual().split(";");
                row = hoja.createRow(rowNumber);
                for (int i = 0; i < informacion.length; i++) {
                    cellValue = informacion[i];
                    row.createCell(i).setCellValue(cellValue);  
                }
                reader.leerLinea();
                rowNumber++;
            }
            new File(rutaArchivos+"estadisticas.xls").delete();
            FileOutputStream archivoFinal = new FileOutputStream(new File(rutaArchivos+"estadisticas.xls"));
            libro.write(archivoFinal);
            archivoFinal.close();
        }
    }
    
    
    ////////////////////////////////////////////////////////////////////////////
    //////////////////////////Getter and Setter/////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public Graphics getGraficos() {
        return graficos;
        //No hay repeticion de niveles
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
