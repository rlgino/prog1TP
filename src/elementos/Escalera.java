package elementos;


import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Escalera {
	public static int ANCHO_DEFAULT = 50;

	// Variables de instancia
	private int x;
	private int y;
	private double alto;
	private double ancho = ANCHO_DEFAULT;
	Image img;

	public Escalera(int x, int y, int alto) {
		this.x = x + (int) ancho / 2;
		this.y = y + (int) alto / 2;
		this.alto = alto;
		this.img =  Herramientas.cargarImagen("escalera.png");
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void dibujarse(Entorno entorno) {
		//entorno.dibujarRectangulo(x, y, ancho, alto, 0, Color.red);
		entorno.dibujarImagen(img, x, y, 0);
	}

	public boolean getIzq() {
		return x - (int) ancho / 2 == 0;
	}

	public int getAncho() {
		return (int) ancho;
	}

	public int getAlto() {
		return (int) alto;
	}

}
