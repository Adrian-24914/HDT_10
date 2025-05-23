import java.util.List;
import java.util.ArrayList;


public class FloydWarshall {
    private int[][] dist;
    private int[][] next;

    public void aplicar(int[][] grafo) {
        int n = grafo.length;
        dist = new int[n][n];
        next = new int[n][n];

        for (int i = 0; i < n; i++) {
            System.arraycopy(grafo[i], 0, dist[i], 0, n);
            for (int j = 0; j < n; j++) {
                next[i][j] = (grafo[i][j] != Integer.MAX_VALUE / 2) ? j : -1;
            }
        }

        for (int k = 0; k < n; k++)
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
    }

    public List<Integer> reconstruirCamino(int i, int j) {
        if (next[i][j] == -1) return new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        while (i != j) {
            path.add(i);
            i = next[i][j];
        }
        path.add(j);
        return path;
    }

    public int[][] getDistancias() {
        return dist;
    }
}
