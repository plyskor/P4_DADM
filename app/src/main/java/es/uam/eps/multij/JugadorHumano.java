
package es.uam.eps.multij;
import android.view.View;
import android.widget.*;

import com.example.jose.connect3.*;
import java.util.Scanner;



public class JugadorHumano implements Jugador {
	 private String nombre;
	public JugadorHumano(String string) {
		this.nombre=string;
		
	}

	@Override
	public void onCambioEnPartida(Evento evento)  {
		Scanner in = new Scanner ( System.in );

		switch (evento.getTipo()) {
        case Evento.EVENTO_CAMBIO:
            break;
            
        case Evento.EVENTO_CONFIRMA:
           
        	System.out.println("Votas a favor (1) o en contra(0)?;");
        	int opt=Character.getNumericValue(in.next().charAt(0));
            try {
            	if(opt==1){
                evento.getPartida().confirmaAccion(
                        this, evento.getCausa(), true);
            	}else{
            		evento.getPartida().confirmaAccion(
                            this, evento.getCausa(), false);
            	}
            }
            catch(Exception e) {
            	
            }
            break;
            
        case Evento.EVENTO_TURNO:


			evento.getPartida().getActivity().setViewTitleText(R.string.playString);

/*
            try {
            	evento.getPartida().realizaAccion(new AccionMover(
						this, new Movimiento3Raya(evento.getPartida().getActivity().getPulsado())));
            	in.close();
            }
            catch(Exception e) {
            	System.out.println(e.getMessage());
            	in.close();
            }
           */ break;
    }
		
	}



	@Override
	public String getNombre() {
		return this.nombre;
	}

	@Override
	public boolean puedeJugar(Tablero tablero) {
		
		return false;
	}

}
