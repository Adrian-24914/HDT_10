public class CentroGrafo {
    public static int calcularCentro(int[][] distancias) {
        int centro = -1;
        int minEccentricidad = Integer.MAX_VALUE;

        for (int i = 0; i < distancias.length; i++) {
            int max = 0;
            for (int j = 0; j < distancias.length; j++) {
                if (i != j && distancias[i][j] > max) {
                    max = distancias[i][j];
                }
            }
            if (max < minEccentricidad) {
                minEccentricidad = max;
                centro = i;
            }
        }
        return centro;
    }
}
