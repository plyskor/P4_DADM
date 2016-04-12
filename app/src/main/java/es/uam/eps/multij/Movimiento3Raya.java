package es.uam.eps.multij;

public class Movimiento3Raya extends Movimiento {
	int casilla;
	public Movimiento3Raya(int casilla) {
		super();
		this.casilla = casilla;
	}

	public int getCasilla() {
		return casilla;
	}

	public void setCasilla(int casilla) {
		this.casilla = casilla;
	}

	@Override
	public String toString() {
		System.out.println("Movimiento a la casilla "+this.getCasilla()+".");
		return null;
	}



	@Override
	public boolean equals(Object o) {
		if(this.getCasilla()==((Movimiento3Raya) o).getCasilla())return true;
		return false;
	}

}
