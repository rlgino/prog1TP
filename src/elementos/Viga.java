package elementos;


import java.awt.Color;

import entorno.Entorno;

public class Viga {
	public static int ANCHO_DEFAULT = 500;

	// Variables de instancia
	private int x;
	private int y;
	private double alto = 10;
	private double ancho = ANCHO_DEFAULT;

	public Viga(int x, int y) {
		this.x = x + (int) ancho / 2;
		this.y = y;
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.red);
	}

	public void setAncho(Integer nuevoAncho) {
		this.x = x - (int) ancho / 2;
		ancho = nuevoAncho;
		this.x = x + (int) ancho / 2;
	}

	public Integer getAncho() {
		return (int) ancho;
	}

	/**
	 * Indica si la viga esta a la izquierda o a la derecha
	 * 
	 * @return
	 */
	public boolean getIzq() {
		return x - (int) ancho / 2 == 0;
	}

	public int getX() {
		return (int) (x - ancho / 2);
	}

	public int getY() {
		return y;
	}

}
