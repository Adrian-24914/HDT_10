import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Grafo grafo = new Grafo();

        // Leer el archivo logistica.txt usando try-with-resources
        System.out.println("Directorio de trabajo actual: " + System.getProperty("user.dir"));
        try (Scanner file = new Scanner(new File("logistica.txt"))) {
            while (file.hasNext()) {
                String origen = file.next();
                String destino = file.next();
                int[] tiempos = new int[4];
                for (int i = 0; i < 4; i++) {
                    tiempos[i] = file.nextInt();
                }
                grafo.agregarArco(origen, destino, tiempos);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: No se encontró el archivo logistica.txt.");
            return;
        }

        Clima climaActual = Clima.NORMAL;
        FloydWarshall floyd = new FloydWarshall();
        floyd.aplicar(grafo.getMatriz(climaActual));

        // Scanner para entrada del usuario usando try-with-resources
        try (Scanner sc = new Scanner(System.in)) {
            int opcion = 0;

            while (opcion != 4) {
                System.out.println("\n--- MENÚ PRINCIPAL ---");
                System.out.println("1. Ruta más corta entre dos ciudades");
                System.out.println("2. Centro del grafo");
                System.out.println("3. Modificar el grafo");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");

                // Verifica si lo ingresado es un entero
                if (!sc.hasNextInt()) {
                    System.out.println("❌ NUH UH! Opción inválida. Debe ingresar un número.");
                    sc.nextLine(); // limpia el buffer
                    continue;
                }

                opcion = sc.nextInt();
                sc.nextLine(); // consumir el salto de línea

                switch (opcion) {
                    case 1 -> {
                        System.out.print("Ciudad origen: ");
                        String origen = sc.nextLine();
                        System.out.print("Ciudad destino: ");
                        String destino = sc.nextLine();

                        int i = grafo.getCiudades().indexOf(origen);
                        int j = grafo.getCiudades().indexOf(destino);

                        if (i == -1 || j == -1) {
                            System.out.println("❌ Una o ambas ciudades no existen en el grafo.");
                            break;
                        }

                        var camino = floyd.reconstruirCamino(i, j);
                        if (camino.isEmpty()) {
                            System.out.println("⚠️ No hay ruta entre las ciudades indicadas.");
                        } else {
                            System.out.print("Ruta más corta: ");
                            camino.forEach(idx -> System.out.print(grafo.getCiudades().get(idx) + " "));
                            System.out.println("\nTiempo total: " + floyd.getDistancias()[i][j]);
                        }
                    }
                    case 2 -> {
                        int centro = CentroGrafo.calcularCentro(floyd.getDistancias());
                        String nombreCentro = grafo.getCiudades().get(centro);
                        System.out.println("🏙️ Centro del grafo: " + nombreCentro);
                    }
                    case 3 -> {
                        System.out.println("a. Interrupción de tráfico");
                        System.out.println("b. Nueva conexión");
                        System.out.println("c. Cambiar clima");
                        System.out.print("Seleccione una opción: ");
                        String subop = sc.nextLine().trim().toLowerCase();

                        switch (subop) {
                            case "a" -> {
                                System.out.print("Ciudad origen: ");
                                String c1 = sc.nextLine();
                                System.out.print("Ciudad destino: ");
                                String c2 = sc.nextLine();
                                grafo.eliminarArco(c1, c2);
                                System.out.println("✅ Conexión eliminada.");
                            }
                            case "b" -> {
                                System.out.print("Ciudad origen: ");
                                String c1 = sc.nextLine();
                                System.out.print("Ciudad destino: ");
                                String c2 = sc.nextLine();
                                int[] tiempos = new int[4];
                                for (Clima clima : Clima.values()) {
                                    System.out.print("Tiempo en " + clima + ": ");
                                    if (!sc.hasNextInt()) {
                                        System.out.println("❌ Entrada inválida. Se canceló la operación.");
                                        sc.nextLine(); // limpia entrada inválida
                                        tiempos = null;
                                        break;
                                    }
                                    tiempos[clima.ordinal()] = sc.nextInt();
                                }
                                sc.nextLine(); // limpiar
                                if (tiempos != null) {
                                    grafo.agregarArco(c1, c2, tiempos);
                                    System.out.println("✅ Conexión agregada.");
                                }
                            }
                            case "c" -> {
                                System.out.print("Nuevo clima (NORMAL, LLUVIA, NIEVE, TORMENTA): ");
                                String nuevo = sc.nextLine().toUpperCase();
                                try {
                                    climaActual = Clima.valueOf(nuevo);
                                    System.out.println("🌦️ Clima actualizado a " + climaActual);
                                } catch (IllegalArgumentException e) {
                                    System.out.println("❌ Clima inválido.");
                                }
                            }
                            default -> System.out.println("❌ Subopción no reconocida.");
                        }
                        floyd.aplicar(grafo.getMatriz(climaActual)); // Recalcular rutas
                    }
                    case 4 -> System.out.println("👋 Orale, finalizando programa...");
                    default -> System.out.println("❌ Opción no válida. Intente de nuevo.");
                }
            }
        }
    }
}