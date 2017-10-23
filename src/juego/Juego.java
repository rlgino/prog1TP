package juego;

// ...............................................................................................................................

//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

import elementos.Barril;
import elementos.Escalera;
import elementos.Viga;
import entorno.Entorno;
import entorno.InterfaceJuego;
import personajes.Agente;
import personajes.Monkey;

public class Juego extends InterfaceJuego {

	// ~ Instance Fields
	// ..............................................................................................................................

	// Variables y métodos propios de cada grupo
	// Personajes
	Agente agente;
	Monkey monkey;

	// Elementos
	Barril barril;
	Viga vigas[];
	Escalera escaleras[];

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private int CANT_VIGAS = 5;

	// Variables y métodos propios de cada grupo
	// ...

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Donkey - Gino Luraschi - V0.01", ANCHO_FRAME, ALTO_FRAME);

		// Inicializar lo que haga falta para el juego
		// Iniciamos personajes
		monkey = new Monkey(50, 50);
		iniciarAgente();

		// Iniciamos elementos
		iniciarVigas();
		iniciarEscaleras();

		// Inicia el juego!
		this.entorno.iniciar();
	}

	private void iniciarAgente() {
		agente = new Agente(50, ALTO_FRAME - 50);
	}

	private void iniciarEscaleras() {
		escaleras = new Escalera[CANT_VIGAS - 1];// Se tiene una escalera menos que que vigas
		for (int x = 0; x < escaleras.length; x++) {
			escaleras[x] = ubicarEscalera(vigas[x], vigas[x + 1]);
		}
	}

	private Escalera ubicarEscalera(Viga viga, Viga viga2) {
		if (viga.getIzq()) {
			int x = viga.getX() + viga.getAncho();
			return new Escalera(x, viga.getY(), viga2.getY() - viga.getY());
		}
		int x = viga.getX();
		return new Escalera(x, viga.getY(), viga2.getY() - viga.getY());
	}

	private void iniciarVigas() {
		vigas = new Viga[5];
		vigas[0] = new Viga(0, monkey.primeraViga());
		for (int x = 1; x < vigas.length; x++) {
			int posX;

			if (vigas[x - 1].getIzq())
				posX = ANCHO_FRAME - Viga.ANCHO_DEFAULT;
			else
				posX = 0;

			int y = vigas[x - 1].getY() + ALTO_FRAME / (CANT_VIGAS);
			vigas[x] = new Viga(posX, y);
		}
		vigas[CANT_VIGAS - 1].setAncho(ANCHO_FRAME);// Se tomara el piso
	}

	// ~ Methods
	// ......................................................................................................................................

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y por lo
	 * tanto es el método más importante de esta clase. Aquí se debe actualizar el
	 * estado interno del juego para simular el paso del tiempo (ver el enunciado
	 * del TP para mayor detalle).
	 */
	public void tick() {
		// Procesamiento de un instante de tiempo
		dibujarElementos();
		moverPersonajes();
		lanzarBarril();
		verificarAgenteBarril();
	}

	private void verificarAgenteBarril() {
		if (agente.lePega(barril)) {
			iniciarAgente();
			barril = null;
		}
	}

	private void lanzarBarril() {
		if (barril == null)
			barril = monkey.lanzarBarril();
		moverBarril();
	}

	boolean volverBarril = false;
	boolean bajar = false;

	private void moverBarril() {
		boolean seMovio = false;
		for (Viga viga : vigas) {
			if (barril.verificarViga(viga)) {
				// Tope derecho de viga
				if (barril.llegoTopeDer(viga) && barril.tocoCostado(ANCHO_FRAME) && !volverBarril) {
					volverBarril = true;
				} else if (barril.llegoTopeDer(viga)) {
					seMovio = false;
				} else if (!barril.llegoTopeDer(viga) && !volverBarril) {
					seMovio = true;
					barril.moverse(3);
					break;
				}

				// Tope izquierdo de viga
				if (barril.llegoTopeIzq(viga) && barril.tocoCostado(0) && volverBarril) {
					volverBarril = false;
				} else if (barril.llegoTopeIzq(viga)) {
					seMovio = false;
				} else if (!barril.llegoTopeIzq(viga) && volverBarril) {
					seMovio = true;
					barril.moverse(-3);
					break;
				}
			}
		}
		if (!seMovio) {
			barril.bajar();
		}
		barril.dibujarse(entorno);
	}

	private void moverPersonajes() {
		moverMonkey();
		moverAgente();
	}

	int alturaSalto = 0;
	boolean saltando = true;
	private void moverAgente() {
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA) || entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			for (Escalera escalera : escaleras) {
				if (agente.verificarEscalera(escalera)) {
					agente.moverPorEscalera(entorno, escalera);
				}
			}
			agente.dibujarse(entorno);
			return;
		}
		if (entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
			
		} else {
			for (Viga viga : vigas) {
				if (agente.verificarViga(viga)) {
					agente.moverPorViga(entorno, viga);
				}
				ubicarYdibujarAgente();
			}
		}
	}

	boolean volver = true;

	private void moverMonkey() {
		if (monkey.llegoTope(vigas[0]))
			volver = !volver;
		if (volver) // muevo a la izq
			monkey.mover(-2);
		else // Muevo a la derecha
			monkey.mover(+2);

		monkey.dibujarse(entorno);
	}

	private void dibujarElementos() {
		dibujarVigas();
		dibujarEscaleras();
	}

	private void dibujarEscaleras() {
		for (Escalera escalera : escaleras)
			escalera.dibujarse(entorno);
	}

	private void ubicarYdibujarAgente() {
		for (Viga viga : vigas) {
			if (agente.verificarViga(viga))
				agente.setY(viga.getY() - 15);
		}
		agente.dibujarse(entorno);
	}

	private void dibujarVigas() {
		for (Viga viga : vigas)
			viga.dibujarse(entorno);
	}

	// ~ Methods
	// ......................................................................................................................................

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}

	// ~ Static Fields
	// ................................................................................................................................

	private static final int ALTO_FRAME = 600;
	private static final int ANCHO_FRAME = 800;
} // end class Juego
