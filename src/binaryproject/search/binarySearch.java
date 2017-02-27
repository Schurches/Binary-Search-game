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
    
    
    public int search(int targetValue){
        int iterations = 0;
        int lowerBound = 0; 
        int upperBound = vector.length-1;
        int guessPosition;
        while(lowerBound <= upperBound){
            iterations++;
            guessPosition = (lowerBound + upperBound)/2;
            if(vector[guessPosition] == targetValue){
                System.out.println(iterations);
                return guessPosition;
            }else if(targetValue > vector[guessPosition]){
                lowerBound = guessPosition+1;
            }else{
                upperBound = guessPosition-1;
            }
        }
        System.out.println(iterations);
        return -1;
    }
    
    
    
}
