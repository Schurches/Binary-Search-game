/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package binaryproject.elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author steven
 */
public class Ambiente {
    
    private AudioInputStream cancion;
    private Clip reproductor;
    
    public Ambiente(InputStream sound) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
        InputStream ruta = new BufferedInputStream(sound);
        cancion = AudioSystem.getAudioInputStream(ruta);
        reproductor = AudioSystem.getClip();
        reproductor.open(cancion);
    }
    
    public void reproducir_cancion(){
        reproductor.loop(300);
    }
    
    public void reproducir_sfx(){
        reproductor.start();
        reproductor.setFramePosition(0);
    }
            
    
    public void detener_cancion(){
        reproductor.stop();
    }
    
}
