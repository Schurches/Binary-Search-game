/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.search;


import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author steven
 */
public class EscritorDArchivos {
    
    private String ruta;
    private FileWriter archivo;
    private BufferedWriter escritor;
    
    public EscritorDArchivos(String ruta) throws IOException{
        this.ruta = ruta;
        archivo = new FileWriter(ruta, true);
        escritor = new BufferedWriter(archivo);
    }
    
    public void escribir(String linea) throws IOException{
        escritor.write(linea);
        escritor.newLine();
    }
    
    public int numberOfPlayers() throws FileNotFoundException, IOException{
        LectorDArchivos reader = new LectorDArchivos(ruta);
        int user = 0;
        String[] lineas = new String[20];
        while(reader.leerLinea()!=null){
            user++;
        }
        return user;
    }
    
    public void sobreescribir(String linea, int usuario) throws FileNotFoundException, IOException{
        LectorDArchivos reader = new LectorDArchivos(ruta);
        int user = 0;
        String[] lineas = new String[20];
        while(user < usuario && reader.leerLinea()!=null){
            lineas[user] = reader.getLineaActual();
            user++;
        }
        if(reader.getLineaActual() == null){
            System.out.println("No existe el player");
        }else if(user == usuario){
            lineas[user] = linea;
            user++;
        }
        while(reader.leerLinea()!=null){
            lineas[user] = reader.getLineaActual();
            user++;
        }
        for (int i = 0; i < user; i++) {
            escribir(lineas[i]);
        }
        
    }
    
    public void cerrar() throws IOException{
        escritor.close();
        archivo.close();
    }
}
