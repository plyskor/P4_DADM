/*
 * Partida.java
 *
 * Created on March 2, 2005, 11:46 AM
 */

package es.uam.eps.multij;
import com.example.jose.connect3.MainActivity;
import java.util.*;


/**
 * Una Partida permite a varios Jugadores jugar sobre un Tablero del juego
 * que sea.
 * Los jugadores actuan sobre la partida mediante acciones (patron 'comando'), y
 * son notificados de los cambios que sufre la partida suscribiendose a eventos
 * de tipo 'Evento' (patron 'publicacion-suscripcion').
 * Las acciones pueden ser 'confirmadas', en cuyo caso solamente se llevara a 
 * cabo si todos los jugadores llaman al metodo confirmaAccion y contestan 
 * afirmativamente. 
 * Una partida no puede tener mas de una accion en proceso de confirmacion.
 *
 * @author mfreire
 */
public class Partida {


    /** todos los jugadores que estan jugando en este momento */
    private ArrayList<Jugador> jugadores;
    private MainActivity mainActivity;
    /** tablero sobre el que estan jugando */
    private Tablero tablero;
    
    /** accion que esta en proceso de confirmacion; puede ser null */
    private Accion porConfirmar = null; 
    
    /** jugadores que han dado su consentimiento a la accion en confirmacion */
    private boolean[] confirmaciones;
    
    /** todos los 'PartidaListener' no-jugadores registrados */
    private ArrayList<PartidaListener> observadores;        
    
    /** 
     * Crea una nueva partida, notificando a los jugadores de su estado, y 
     * avisando al jugador actual de que le toca mover 
     */
    public Partida(Tablero tablero, ArrayList<Jugador> jugadores,MainActivity main) {
        observadores = new ArrayList<>();
        this.tablero = tablero;
        this.jugadores = jugadores;
        this.mainActivity=main;

    }
    
    // Metodos internos de utilidad para notificar cosas a los jugadores y observadores

    /**
     * Notifica un cambio de estado a todos los jugadores y observadores
     */
    private void notificaCambio(String descripcion, Accion accion) {
        Evento e = new Evento(Evento.EVENTO_CAMBIO, descripcion, this, accion);

        for (Jugador j: jugadores) {
            j.onCambioEnPartida(e);
        }

        for (PartidaListener s : observadores) {
            s.onCambioEnPartida(e);
        }
    }

    /**
     * Notifica el cambio de turno al jugador al que le toca mover
     */
    private void notificaTurno(String descripcion, Accion accion) {
        Jugador j = jugadores.get(tablero.getTurno());
        Evento e = new Evento(Evento.EVENTO_TURNO, descripcion, this, accion);
        j.onCambioEnPartida(e);
    }

    /**
     * Notifica un error en una accion a un jugador (el jugador debe constar
     * como originario de la accion)
     */
    private void notificaError(String descripcion, Accion accion) {
        Evento e = new Evento(Evento.EVENTO_ERROR, descripcion, this, accion);
        accion.getOrigen().onCambioEnPartida(e);
    }

    private void ejecutaAccion(Accion a) throws ExcepcionJuego
    {
        a.ejecuta(this);

        if (tablero.getEstado() != Tablero.EN_CURSO) {
            String notaFinal = "Fin de partida - " + ((tablero.getEstado() == Tablero.TABLAS)
                    ? "todo queda en tablas"
                    : "gana " + getJugador(tablero.getTurno()).getNombre());
            notificaCambio(notaFinal, a);
        }
        else {
            notificaCambio(a.getOrigen().getNombre() + " ha jugado " + tablero.getUltimoMovimiento(), a);
            notificaTurno("Te toca", a);
        }

    }

    /**
     * Comienza una partida
     */
    public void comenzar(Tablero tablero, ArrayList<Jugador> jugadores) {
        this.tablero = tablero;
        this.jugadores = jugadores;
        comenzar();
    }

    /**
     * Comienza una partida
     */
    public void comenzar() {

        // comprueba argumentos
        if (jugadores.size() != tablero.getNumJugadores()) {
            throw new IllegalArgumentException(
                    "Faltan o sobran jugadores en este tablero");
        }
        if (tablero.estado != Tablero.EN_CURSO) {
            throw new IllegalArgumentException(
                    "Este tablero ya tiene una posicion final: no se puede empezar");
        }

        // envia a todos los jugadores el estado actual
        notificaCambio("La partida va a comenzar", null);

        // envia, al jugador al que le toca mover, un evento de tipo turno
        notificaTurno("Te toca empezar", null);
    }

    /**
     * Vuelve a enviar el evento turno a una partida
     */
    public void continuar() {
        // envia, al jugador al que le toca mover, un evento de tipo turno
        notificaTurno("Te toca seguir", null);
    }

    // Para registrar y dar de baja observadores
    
    /**
     * Da de alta un observador (parecido a un jugador, pero no juega)
     */
    public void addObservador(PartidaListener observador) {
        if ( ! observadores.contains(observador)) {
            observadores.add(observador);        
        }
    }
    
    /**
     * Quita un observador
     */
    public void removeObservador(PartidaListener observador) {
        observadores.remove(observador);        
    }        
    
    // Metodos de acceso a variables de instancia
    
    /**
     * Devuelve el tablero actual (solo para consultas - los jugadores no
     * deben usarlo para mover directamente).
     */
    public Tablero getTablero() {
        return tablero;
    }
    
    /**
     * Devuelve el jugador i-esimo
     */
    public Jugador getJugador(int i) {
        return jugadores.get(i);
    }
    
    // Metodos generales
    
    /**
     * Procesa una accion de un jugador
     */
    public void realizaAccion(Accion a)  throws ExcepcionJuego {
        Evento evento;
        
        if (a.requiereConfirmacion()) {
            if (porConfirmar != null) {
                // solo puede haber una accion pendiente de confirmacion en un momento dado...
                notificaError("Ya hay un evento en fase de confirmacion", a);
                return;
            }
            porConfirmar = a;
            confirmaciones = new boolean[jugadores.size()];
            
            // si el que la propone es un jugador, se asume que esta de acuerdo
            int jugadorPropone = jugadores.indexOf(a.getOrigen());
            if (jugadorPropone != -1) {
                confirmaciones[jugadorPropone] = true;
            }
            
            // solicita confirmacion a todos los restantes
            evento = new Evento(Evento.EVENTO_CONFIRMA, "Confirmacion requerida para accion", this, a);

            for (Jugador j : jugadores) {
                if (jugadorPropone != -1 && j != jugadores.get(jugadorPropone)) {
                    j.onCambioEnPartida(evento);
                }
            }         
        }
        else {
            ejecutaAccion(a);
        }        
    }

    /**
     * Confirma la accion actual para un jugador dado; si el voto es negativo,
     * la accion queda totalmente cancelada. Devuelve 'true' si, contando este
     * voto, la accion ha sido aprobada por todos los jugadores.
     */
    public void confirmaAccion(Jugador j, Accion a, boolean voto)  throws ExcepcionJuego {        
        int posJugador = jugadores.indexOf(j);        
        if (a == porConfirmar && posJugador != -1) {
            if (voto) {
                // confirmada: lo apunta, y mira a ver si ya estan todos
                confirmaciones[posJugador] = true;
                boolean ok = true;
                for (boolean confirmacion : confirmaciones) {
                    if (!confirmacion) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    // luz verde: todos de acuerdo
                    ejecutaAccion(porConfirmar);
                    porConfirmar = null;
                    confirmaciones = null;
                }
            }
            else {
                // cancelada: notifica al que la propuso (si era un jugador)
                if (jugadores.contains(porConfirmar.getOrigen())) {
                    notificaError("El jugador "+j.getNombre()+" ha votado en contra",
                            porConfirmar);
                }
                
                // cancela la accion
                porConfirmar = null;
                confirmaciones = null;
            }
        }
    }

public MainActivity getActivity(){
    return this.mainActivity;
}

	public void comienzaPartida() {
		
		
	}
}
