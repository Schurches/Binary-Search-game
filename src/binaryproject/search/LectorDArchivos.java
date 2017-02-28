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
        this.archivo = new FileReader(ruta);
        this.lector = new BufferedReader(this.archivo);
        this.lineaActual = null;
    }
    
    public String[] leerLinea() throws IOException{
        this.lineaActual = this.lector.readLine();
        if(lineaActual != null){
            return this.lineaActual.split(",");
        }
        return null;
    }
    
    public void cerrarArchivo() throws IOException{
        this.lector.close();
        this.archivo.close();
    }

    public String getRuta() {
        return this.ruta;
    }

    public String getLineaActual() {
        return this.lineaActual;
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
