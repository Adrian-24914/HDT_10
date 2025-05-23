import java.io.File;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Grafo grafo = new Grafo();

        // Leer archivo logistica.txt
        Scanner file = new Scanner(new File("logistica.txt"));
        while (file.hasNext()) {
            String origen = file.next();
            String destino = file.next();
            int[] tiempos = new int[4];
            for (int i = 0; i < 4; i++) tiempos[i] = file.nextInt();
            grafo.agregarArco(origen, destino, tiempos);
        }

        Clima climaActual = Clima.NORMAL;
        FloydWarshall floyd = new FloydWarshall();
        floyd.aplicar(grafo.getMatriz(climaActual));

        while (true) {
            System.out.println("\n1. Ruta más corta\n2. Centro del grafo\n3. Modificar grafo\n4. Salir");
            int op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> {
                    System.out.print("Ciudad origen: ");
                    String origen = sc.nextLine();
                    System.out.print("Ciudad destino: ");
                    String destino = sc.nextLine();
                    int i = grafo.getCiudades().indexOf(origen);
                    int j = grafo.getCiudades().indexOf(destino);
                    if (i == -1 || j == -1) {
                        System.out.println("Ciudad no encontrada.");
                        break;
                    }
                    List<Integer> camino = floyd.reconstruirCamino(i, j);
                    if (camino.isEmpty()) {
                        System.out.println("No hay camino.");
                    } else {
                        System.out.print("Ruta más corta: ");
                        camino.forEach(idx -> System.out.print(grafo.getCiudades().get(idx) + " "));
                        System.out.println("\nCosto total: " + floyd.getDistancias()[i][j]);
                    }
                }
                case 2 -> {
                    int centro = CentroGrafo.calcularCentro(floyd.getDistancias());
                    System.out.println("Centro del grafo: " + grafo.getCiudades().get(centro));
                }
                case 3 -> {
                    System.out.println("a. Interrupción\nb. Nueva conexión\nc. Cambio de clima");
                    String subop = sc.nextLine();
                    switch (subop) {
                        case "a" -> {
                            System.out.print("Ciudad1: ");
                            String c1 = sc.nextLine();
                            System.out.print("Ciudad2: ");
                            String c2 = sc.nextLine();
                            grafo.eliminarArco(c1, c2);
                        }
                        case "b" -> {
                            System.out.print("Ciudad1: ");
                            String c1 = sc.nextLine();
                            System.out.print("Ciudad2: ");
                            String c2 = sc.nextLine();
                            int[] tiempos = new int[4];
                            for (Clima clima : Clima.values()) {
                                System.out.print("Tiempo en " + clima + ": ");
                                tiempos[clima.ordinal()] = sc.nextInt();
                            }
                            sc.nextLine();
                            grafo.agregarArco(c1, c2, tiempos);
                        }
                        case "c" -> {
                            System.out.print("Nuevo clima (NORMAL, LLUVIA, NIEVE, TORMENTA): ");
                            climaActual = Clima.valueOf(sc.nextLine().toUpperCase());
                        }
                    }
                    floyd.aplicar(grafo.getMatriz(climaActual));
                }
                case 4 -> {
                    System.out.println("Finalizando...");
                    return;
                }
            }
        }
    }
}

