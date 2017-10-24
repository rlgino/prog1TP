
// ...............................................................................................................................
//
// (C) Copyright  2011/2017 TekGenesis.  All Rights Reserved
// THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF TekGenesis.
// The copyright notice above does not evidence any actual or intended
// publication of such source code.
//
// ...............................................................................................................................

package juego;

import java.util.Random;

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

	private final int ALTO_FRAME = 600;
	public final static int ANCHO_FRAME = 800;
	private final int DISTANCIA_AGENTE = 50;
	private final int ALTURA_MAX_SALTO = 80;
	private final int CANT_VIGAS = 5;// Incluyendo al piso

	// Variables y métodos propios de cada grupo
	// Personajes
	Agente agente;

	// Elementos
	Barril[] barriles;
	Escalera[] escaleras;
	Monkey monkey;
	boolean saltando = false;
	boolean saltandoArriba = true;
	Viga[] vigas;

	boolean volver = true;
	int alturaSalto = 0;

	// El objeto Entorno que controla el tiempo y otros
	private final Entorno entorno;

	// Variables y métodos propios de cada grupo
	// ...

	Juego() {
		// Inicializa el objeto entorno
		entorno = new Entorno(this, "Donkey kong - Gino Luraschi - V1.0", ANCHO_FRAME, ALTO_FRAME);

		// Inicializar lo que haga falta para el juego
		// Iniciamos personajes
		monkey = new Monkey(50, 50);
		iniciarAgente();

		// Iniciamos elementos
		barriles = new Barril[5];
		iniciarVigas();
		iniciarEscaleras();

		// Inicia el juego!
		entorno.iniciar();
	}

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
		moverBarril();
		verificarAgenteBarril();
	}

	private void dibujarElementos() {
		dibujarVigas();
		dibujarEscaleras();
	}

	private void dibujarEscaleras() {
		for (final Escalera escalera : escaleras)
			escalera.dibujarse(entorno);
	}

	private void dibujarVigas() {
		for (final Viga viga : vigas)
			viga.dibujarse(entorno);
	}

	private void iniciarAgente() {
		agente = new Agente(DISTANCIA_AGENTE, ALTO_FRAME - DISTANCIA_AGENTE);
	}

	private void iniciarEscaleras() {
		escaleras = new Escalera[CANT_VIGAS - 1]; // Se tiene una escalera menos que vigas
		for (int x = 0; x < escaleras.length; x++)
			escaleras[x] = ubicarEscalera(vigas[x], vigas[x + 1]);
	}

	private void iniciarVigas() {
		vigas = new Viga[5];
		vigas[0] = new Viga(0, monkey.primeraViga());
		for (int x = 1; x < vigas.length; x++) {
			final int posX;

			if (vigas[x - 1].getIzq())
				posX = ANCHO_FRAME - Viga.ANCHO_DEFAULT;
			else
				posX = 0;

			final int y = vigas[x - 1].getY() + ALTO_FRAME / (CANT_VIGAS);
			vigas[x] = new Viga(posX, y);
		}
		vigas[CANT_VIGAS - 1].setAncho(ANCHO_FRAME); // Se tomara el piso como ultimo
	}

	int segundos = 0;
	int proxBarril = 0;

	private void lanzarBarril() {
		if (segundos == proxBarril) {
			for (int y = 0; y < barriles.length; y++) {
				if (barriles[y] == null) {
					barriles[y] = monkey.lanzarBarril();
					final Random generador = new Random();
					proxBarril = 300 + generador.nextInt(3) * 100;
					segundos = 0;
					break;
				}
			}
		}
		segundos++;
	}

	private void moverAgente() {
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA) || entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			for (final Escalera escalera : escaleras) {
				if (agente.verificarEscalera(escalera))
					agente.moverPorEscalera(entorno, escalera);
			}
			agente.dibujarse(entorno);
			return;
		}
		if (entorno.sePresiono(entorno.TECLA_ESPACIO) || saltando) {
			agenteSaltar();
			agente.dibujarse(entorno);
		} else {
			for (final Viga viga : vigas) {
				if (agente.verificarViga(viga))
					agente.moverPorViga(entorno, viga);
				if (!saltando)
					ubicarYdibujarAgente();
			}
		}
	}

	private void agenteSaltar() {
		if (!saltando)
			saltando = true;

		if (alturaSalto < ALTURA_MAX_SALTO && saltandoArriba) {
			agente.subir(-3);
			alturaSalto += 3;
			if (alturaSalto >= ALTURA_MAX_SALTO)
				saltandoArriba = false;
		}
		if (saltando && !saltandoArriba) {
			agente.subir(3);
			alturaSalto -= 3;
			if (alturaSalto <= 0) {
				saltandoArriba = true;
				saltando = false;
				alturaSalto = 0;
			}
		}
		if (entorno.estaPresionada(entorno.TECLA_DERECHA))
			agente.mover(3);
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA))
			agente.mover(-3);
	}

	private void moverBarril() {
		for (int x = 0; x < barriles.length; x++) {
			if (barriles[x] != null) {
				barriles[x] = barriles[x].moverse(vigas, entorno);
				if (barriles[x] == null)
					segundos = 0;
			}
		}
	}

	private void moverMonkey() {
		if (monkey.llegoTope(vigas[0]))
			volver = !volver;
		if (volver) // muevo a la izq
			monkey.mover(-2);
		else // Muevo a la derecha
			monkey.mover(+2);

		monkey.dibujarse(entorno);
	}

	private void moverPersonajes() {
		moverMonkey();
		moverAgente();
	}

	private Escalera ubicarEscalera(Viga viga, Viga viga2) {
		if (viga.getIzq()) {
			final int x = viga.getX() + viga.getAncho();
			return new Escalera(x, viga.getY(), viga2.getY() - viga.getY());
		}
		final int x = viga.getX();
		return new Escalera(x, viga.getY(), viga2.getY() - viga.getY());
	}

	private void ubicarYdibujarAgente() {
		for (final Viga viga : vigas) {
			if (agente.verificarViga(viga))
				agente.setY(viga.getY() - 15);
		}
		agente.dibujarse(entorno);
	}

	private void verificarAgenteBarril() {
		for (Barril barril : barriles) {
			if (barril != null && agente.lePega(barril)) {
				iniciarAgente();
				barril = null;
			}
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		final Juego juego = new Juego();
	}
}
