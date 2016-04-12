/*
 * JugadorAleatorio.java
 *
 * Created on March 2, 2005, 11:48 AM
 */

package es.uam.eps.multij;

/**
 *
 * @author mfreire
 */
public class JugadorAleatorio implements Jugador {
    
    private String nombre;
    private static int numAleatorios = 0;
    

    
    /** Creates a new instance of JugadorAleatorio */
    public JugadorAleatorio(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Recibe una notificacion de un cambio en la partida
     */
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                break;
                
            case Evento.EVENTO_CONFIRMA:
                // este jugador confirma al azar
                try {
                    evento.getPartida().confirmaAccion(
                            this, evento.getCausa(), (Math.random() > .5));
                }
                catch(Exception e) {
                	
                }
                break;
                
            case Evento.EVENTO_TURNO:

                // jugar al azar, que gran idea
               // Tablero t = evento.getPartida().getTablero();
               // int r = (int)(Math.random() * t.movimientosValidos().size());
               /* try {
                	evento.getPartida().realizaAccion(new AccionMover(
                			this, t.movimientosValidos().get(r)));
                }
                catch(Exception e) {
                	
                }*/
                break;
        }
    }    
    
    /**
     * Devuelve el nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }
    
    /**
     * Este jugador juega *todos* los juegos
     */
    public boolean puedeJugar(Tablero tablero)  {
        return true;
    }    
}
