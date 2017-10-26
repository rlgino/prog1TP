package personajes;

import java.awt.Color;

import elementos.Barril;
import elementos.Viga;
import entorno.Entorno;

public class Monkey implements Personaje {
	private int x;
	private int y;
	private int altura;

	public Monkey(int x, int y) {
		this.x = x;
		this.y = y;
		this.altura = 60;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getY() {
		return y;
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarTriangulo(this.x, this.y, this.altura, 40, -Math.PI / 2, Color.green);
	}

	public void mover(int dx) {
		this.x = this.x + dx;
	}

	public Barril lanzarBarril() {
		int x_barril = this.x;
		int y_barril = this.y + (altura / 3);

		return new Barril(x_barril, y_barril);
	}

	/**
	 * 
	 * @param viga
	 * @return true si llego al tope false si aún no
	 */
	public boolean llegoTope(Viga viga) {
		return x == 500 || x == 50;
	}

	public int primeraViga() {
		return y + altura / 2;
	}

	boolean volver = true;

	public void moverMonkey(Viga viga) {
		if (this.llegoTope(viga))
			volver = !volver;
		if (volver) // muevo a la izq
			this.mover(-2);
		else // Muevo a la derecha
			this.mover(+2);
	}

}
