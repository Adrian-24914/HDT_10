import java.util.*;

public class Grafo {
    private final int INF = Integer.MAX_VALUE / 2;
    private List<String> ciudades = new ArrayList<>();
    private Map<Clima, int[][]> matrices = new EnumMap<>(Clima.class);

    public void agregarCiudad(String ciudad) {
        if (!ciudades.contains(ciudad)) {
            ciudades.add(ciudad);
            for (Clima c : Clima.values()) {
                redimensionarMatriz(c);
            }
        }
    }

    private void redimensionarMatriz(Clima clima) {
        int size = ciudades.size();
        int[][] vieja = matrices.getOrDefault(clima, new int[size][size]);
        int[][] nueva = new int[size][size];
        for (int[] row : nueva) Arrays.fill(row, INF);
        for (int i = 0; i < vieja.length; i++)
            for (int j = 0; j < vieja.length; j++)
                nueva[i][j] = vieja[i][j];
        matrices.put(clima, nueva);
    }

    public void agregarArco(String origen, String destino, int[] tiempos) {
        agregarCiudad(origen);
        agregarCiudad(destino);
        int i = ciudades.indexOf(origen);
        int j = ciudades.indexOf(destino);
        Clima[] climas = Clima.values();
        for (int k = 0; k < climas.length; k++) {
            matrices.get(climas[k])[i][j] = tiempos[k];
        }
    }

    public void eliminarArco(String origen, String destino) {
        int i = ciudades.indexOf(origen);
        int j = ciudades.indexOf(destino);
        for (Clima c : Clima.values()) {
            matrices.get(c)[i][j] = INF;
        }
    }

    public int[][] getMatriz(Clima clima) {
        return matrices.get(clima);
    }

    public List<String> getCiudades() {
        return ciudades;
    }
}
