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
    
    private String nombre;
    private final String contraseña;
    private ArrayList<Long> tiempos;
    private ArrayList<Float> puntajes;
    private ArrayList<Integer> intentos;
    private float score; //Tener en cuenta los puntajes y el tiempo en cada nivel

    /**
     * Constructor para un nuevo jugador
     * @param nombre Player Name
     * @param contraseña Password
     */
    public Jugador(String nombre, String contraseña) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.tiempos = new ArrayList<Long>();
        this.puntajes = new ArrayList<Float>();
        this.intentos = new ArrayList<Integer>();
        this.score = 0;
    }
    
    /**
     * Constructor para cuando esta cargando los datos de un jugador ya creado
     * @param nombre Nombre del jugador
     * @param contraseña Contraseña
     * @param tiempos Tiempo en cada nivel
     * @param puntajes Puntaje en cada nivel
     * @param intentos Intentos de cada nivel
     * @param score puntaje total
     */
    public Jugador(String nombre, String contraseña, ArrayList<Long> tiempos, ArrayList<Float> puntajes, ArrayList<Integer> intentos, float score) {
        this.nombre = nombre;
        this.contraseña = contraseña;
        this.tiempos = tiempos;
        this.puntajes = puntajes;
        this.intentos = intentos;
        this.score = score;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Long> getTiempos() {
        return tiempos;
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
    
    
    
    
    
}
