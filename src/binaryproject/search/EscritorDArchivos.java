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
    
    public EscritorDArchivos(String ruta, boolean change) throws IOException{
        this.ruta = ruta;
        this.archivo = new FileWriter(ruta, change);
        this.escritor = new BufferedWriter(this.archivo);
    }
    
    public void escribir(String linea) throws IOException{
        this.escritor.write(linea);
        this.escritor.newLine();
    }
    
    public void cerrar() throws IOException{
        this.escritor.close();
        this.archivo.close();
    }
}
