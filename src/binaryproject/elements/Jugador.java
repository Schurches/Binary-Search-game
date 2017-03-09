/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.elements;

import java.util.ArrayList;

/**
 *
 * @author steven
 */
public class Jugador {
    
    private final int codigo;
    private long lastTimeLogin;
    private String nombre;
    private final String contraseña;
    private ArrayList<Long[]> tiempos;
    private ArrayList<Float> puntajes;
    private ArrayList<Integer> intentos;
    private float score; //Tener en cuenta los puntajes y el tiempo en cada nivel
    //Excel data
    private boolean[] writen;
    
    /**
     * Constructor para un nuevo jugador
     * @param nombre Player Name
     * @param contraseña Password
     */
    public Jugador(String nombre, String contraseña, int ID) {
        this.codigo = ID;
        this.lastTimeLogin = 0;
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.tiempos = new ArrayList<Long[]>();
        this.tiempos.add(new Long[3]);
        this.tiempos.add(new Long[3]);
        this.tiempos.add(new Long[3]);
        this.puntajes = new ArrayList<Float>();
        this.intentos = new ArrayList<Integer>();
        this.score = 0;
        this.writen = new boolean[3];
    }
    
    /**
     * Constructor para cuando esta cargando los datos de un jugador ya creado
     * @param ID codigo de estudiante
     * @param nombre Nombre del jugador
     * @param loginTime Tiempo de inicio de sesion
     * @param contraseña Contraseña
     * @param tiempos Tiempo en cada nivel
     * @param puntajes Puntaje en cada nivel
     * @param intentos Intentos de cada nivel
     * @param score puntaje total
     */
    public Jugador(int ID, String nombre, long loginTime, String contraseña, ArrayList<Long[]> tiempos, ArrayList<Float> puntajes, ArrayList<Integer> intentos, float score, boolean[] writen) {
        this.codigo = ID;
        this.nombre = nombre;
        this.lastTimeLogin = loginTime;
        this.contraseña = contraseña;
        this.tiempos = tiempos;
        this.puntajes = puntajes;
        this.intentos = intentos;
        this.score = score;
        this.writen = writen;
    }

    public long getLastTimeLogin() {
        return lastTimeLogin;
    }

    public void setLastTimeLogin(long lastTimeLogin) {
        this.lastTimeLogin = lastTimeLogin;
    }
    
    public String getNombre() {
        return nombre;
    }

    public boolean[] getWriten() {
        return writen;
    }

    public void setWriten(boolean[] writen) {
        this.writen = writen;
    }
    
    public void changeWritenState(int index, boolean isWriten){
        this.writen[index] = isWriten;
    }
    
    public ArrayList<Long[]> getTiempos() {
        return tiempos;
    }

    public Long[] getTiempo(int index){
        return this.tiempos.get(index);
    }
    
    public void setTiempoI(int index, int time, long timeToAdd){
        this.tiempos.get(index)[time] = timeToAdd;
    }
    
    public ArrayList<Float> getPuntajes() {
        return puntajes;
    }

    public ArrayList<Integer> getIntentos() {
        return intentos;
    }

    public float getScore() {
        return score;
    }

    public int getID() {
        return codigo;
    }

    public String getContraseña() {
        return contraseña;
    }
    
    public void actualizarPuntaje(){
        this.score = (getPuntajes().get(0)+getPuntajes().get(1)+getPuntajes().get(2))/3;
    }
    
    public String[] playerInfoAsArray(){
        return toString().split(",");
    }
    
    public String hasThetimesBeenWritenAlready(){
        String times = "";
        for (int i = 0; i < 3; i++) {
            if(getWriten()[i]){
                times+="1,";
            }else{
                times+="0,";
            }
        }
        return times;
    }
    
    @Override
    public String toString(){
        String info = "";
        info = this.getID() + "," +
               this.getNombre() + "," + 
               this.getLastTimeLogin()+ "," +
               this.getContraseña() + "," +
               this.getTiempos().get(0)[0] + "," +
               this.getTiempos().get(0)[1] + "," +
               this.getTiempos().get(0)[2] + "," +
               this.getTiempos().get(1)[0] + "," +
               this.getTiempos().get(1)[1] + "," +
               this.getTiempos().get(1)[2] + "," +
               this.getTiempos().get(2)[0] + "," +
               this.getTiempos().get(2)[1] + "," +
               this.getTiempos().get(2)[2] + "," +
               this.getPuntajes().get(0) + "," +
               this.getPuntajes().get(1) + "," +
               this.getPuntajes().get(2) + "," +
               this.getIntentos().get(0) + "," +
               this.getIntentos().get(1) + "," +
               this.getIntentos().get(2) + "," +
               this.getScore() + "," +
               this.hasThetimesBeenWritenAlready();
        info = info.substring(0,info.length()-1);
        return info;
    }
    
    
    
}
