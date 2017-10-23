package personajes;


import java.awt.Color;
import java.awt.Image;

import elementos.Barril;
import elementos.Escalera;
import elementos.Viga;
import entorno.Entorno;
import entorno.Herramientas;

public class Agente {
	// Variables de instancia
	private int x;
	private int y;
	private int tam = 50;
	private Image img;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getTam() {
		return tam;
	}

	public void setTam(int tam) {
		this.tam = tam;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Agente(int x, int y) {
		this.x = x;
		this.y = y;
		this.img = Herramientas.cargarImagen("agente.png");
	}

	public void dibujarse(Entorno entorno) {
		entorno.dibujarCirculo(this.x, this.y, Math.PI * 10, Color.green);
//		entorno.dibujarImagen(img, x, y, 0);
	}

	public void mover(int dx) {
		this.x = this.x + dx;
	}

	public void subir(int dy) {
		this.y = this.y + dy;
	}

	public boolean llegoTopeIzq(Viga viga) {
		return x < viga.getX();
	}

	public boolean llegoTopeDer(Viga viga) {
		return x > viga.getX() + viga.getAncho();
	}

	public boolean lePega(Barril barril) {
		int xx = this.x - barril.getX();
		int yy = this.y - barril.getY();
		double dist = Math.sqrt(xx * xx + yy * yy);

		return dist <= this.tam / 2;
	}

	public boolean verificarViga(Viga viga) {
		int yy = this.y - viga.getY();
		double dist = Math.sqrt(yy * yy);
		return dist <= this.tam / 2;
	}

	public void moverPorViga(Entorno entorno, Viga viga) {
		if (!this.llegoTopeDer(viga) && entorno.estaPresionada(entorno.TECLA_DERECHA))
			this.mover(+3);

		if (!this.llegoTopeIzq(viga) && entorno.estaPresionada(entorno.TECLA_IZQUIERDA))
			this.mover(-3);
	}

	public boolean verificarEscalera(Escalera escalera) {
		int xx = this.x - escalera.getX();
		double disty = Math.sqrt(xx * xx);
		return disty <= (this.tam / 2);
	}

	public boolean llegoTope(Escalera escalera) {
		int yy = this.y - escalera.getY();
		double disty = Math.sqrt(yy * yy);
		return disty >= ((escalera.getAlto() / 2))+20;
	}

	public void moverPorEscalera(Entorno entorno, Escalera escalera) {
		if (!llegoTope(escalera) && entorno.estaPresionada(entorno.TECLA_ABAJO))
			this.subir(+3);

		if (!llegoTope(escalera) && entorno.estaPresionada(entorno.TECLA_ARRIBA))
			this.subir(-3);		
	}

	boolean sube = true;
	int altura = 0;
	public void saltar() {
		if(altura >= 30)
			sube = false;
		if(sube && altura < 30) {
			this.y = this.y + 3;
			altura += 3;
		}
		if(!sube) {
			this.y = this.y - 3;
		}
	}

}