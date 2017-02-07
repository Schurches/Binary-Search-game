/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.search;

/**
 *
 * @author steven
 */
public class binarySearch {
    
    int[] vector;
    
    public binarySearch(int[] elementos){
        this.vector = elementos;
    }
    
    
    public int busqueda(int[] vector, int elemento){
        int posInicial = 0;
        int posFinal = vector.length-1;
        int puntoMedio;
        
        while(posInicial <= posFinal){
            
            puntoMedio = (posInicial + posFinal)/2;
            
            if(vector[puntoMedio] == elemento){
                return puntoMedio;
            }else if(elemento > vector[puntoMedio]){
                posInicial = puntoMedio+1;
            }else{
                posFinal = puntoMedio-1;
            }
            
        }
        
        return -1;
    }
    
    
    
}
