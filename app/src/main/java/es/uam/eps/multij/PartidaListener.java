/*
 * PartidaListener.java
 *
 * Created on March 2, 2005, 11:46 AM
 */

package es.uam.eps.multij;

/**
 * Interfaz que debe implementar  suscriptor a los eventos que genera una
 * Partida.
 *
 * @author mfreire
 */
public interface PartidaListener {
    
    /**
     * Llamado para notificar un evento en la partida
     */
    void onCambioEnPartida(Evento evento) ;
}
