package elementos;

import java.awt.Color;

import entorno.Entorno;

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

}
