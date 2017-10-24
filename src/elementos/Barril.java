package elementos;

import java.awt.Color;

import entorno.Entorno;
import juego.Juego;

public class Barril {
	// Variables de instancia
	private int x;
	private int y;
	private int tam = 10;

	public Barril(int x_barril, int y_barril) {
		this.x = x_barril;
		this.y = y_barril;
	}

	public void moverse(int x) {
		this.x += x;
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarRectangulo(x, y, 20, 20, 0, Color.yellow);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean verificarViga(Viga viga) {
		int yy = this.y - viga.getY();
		double dist = Math.sqrt(yy * yy);
		return dist <= this.tam * 3;
	}

	public void bajar() {
		this.y = this.y + 3;
	}

	public boolean llegoTopeIzq(Viga viga) {
		return x < viga.getX();
	}

	public boolean llegoTopeDer(Viga viga) {
		return x > viga.getX() + viga.getAncho();
	}

	public boolean tocoCostado(int anchoFrame) {
		int xx = this.x - anchoFrame;
		double dist = Math.sqrt(xx * xx);
		return dist <= this.tam * 3;
	}

	boolean volverBarril = false;

	public Barril moverse(Viga[] vigas, Entorno entorno) {
		boolean seMovio = false;
		for (final Viga viga : vigas) {
			if (this.verificarViga(viga)) {
				if (vigas[vigas.length - 1] == viga && (llegoTopeDer(viga) || llegoTopeIzq(viga)))
					return null;

				// Tope derecho de viga
				if (this.llegoTopeDer(viga) && this.tocoCostado(Juego.ANCHO_FRAME) && !volverBarril)
					volverBarril = true;
				else if (this.llegoTopeDer(viga))
					seMovio = false;
				else if (!this.llegoTopeDer(viga) && !volverBarril) {
					seMovio = true;
					this.moverse(2);
					break;
				}

				// Tope izquierdo de viga
				if (this.llegoTopeIzq(viga) && this.tocoCostado(0) && volverBarril)
					volverBarril = false;
				else if (this.llegoTopeIzq(viga))
					seMovio = false;
				else if (!this.llegoTopeIzq(viga) && volverBarril) {
					seMovio = true;
					this.moverse(-2);
					break;
				}
			}
		}
		if (!seMovio)
			this.bajar();

		this.dibujarse(entorno);
		return this;
	}

}
