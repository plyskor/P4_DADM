/*
 * ExcepcionJuego.java
 *
 * Created on February 11, 2005, 3:33 PM
 */

package es.uam.eps.multij;

/**
 * Base de las excepciones para juegos
 */
public class ExcepcionJuego extends Exception {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private int error;
	/**
     * Constructor con descripcion
     * @param descripcion un mensaje con lo que ha ocurrido
     */
    public ExcepcionJuego(String descripcion,int e) {
        super(descripcion);
        error=e;
    }
    public int getError(){
        return  error;
    }
    /**
     * Constructor con descripcion y una causa anidada
     * @param descripcion un mensaje con lo que ha ocurrido
     * @param causa una excepcion que causo a esta
     */
    public ExcepcionJuego(String descripcion, Exception causa) {
        super(descripcion, causa);
    }
}
