/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author steven
 */
public class LectorDArchivos {
    
    private String ruta;
    private FileReader archivo;
    private BufferedReader lector;
    private String lineaActual;
    
    public LectorDArchivos(String ruta) throws FileNotFoundException{
        this.ruta = ruta;
        archivo = new FileReader(ruta);
        lector = new BufferedReader(archivo);
        this.lineaActual = "";
    }
    
    public String[] leerLinea() throws IOException{
        lineaActual = lector.readLine();
        if(lineaActual != null){
            return lineaActual.split(",");
        }
        return null;
    }
    
    public void cerrarArchivo() throws IOException{
        lector.close();
    }

    public String getRuta() {
        return ruta;
    }

    public String getLineaActual() {
        return lineaActual;
    }
    
    
    /*
    @Override
    public String toString(){
        if(lineaActual != null){
            return lineaActual;
        }
        return "";
    }
*/
}
