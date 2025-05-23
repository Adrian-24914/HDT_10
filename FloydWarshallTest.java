import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FloydWarshallTest {
    @Test
    public void testFloydDistanciasBasicas() {
        int INF = Integer.MAX_VALUE / 2;
        int[][] matriz = {
            {0,     3,     INF},
            {INF,   0,     2},
            {INF,   INF,   0}
        };

        FloydWarshall floyd = new FloydWarshall();
        floyd.aplicar(matriz);
        int[][] dist = floyd.getDistancias();

        assertEquals(3, dist[0][1]);
        assertEquals(5, dist[0][2]);
        assertEquals(2, dist[1][2]);
    }

    @Test
    public void testReconstuirCamino() {
        int INF = Integer.MAX_VALUE / 2;
        int[][] matriz = {
            {0, 1, INF},
            {INF, 0, 2},
            {INF, INF, 0}
        };

        FloydWarshall floyd = new FloydWarshall();
        floyd.aplicar(matriz);
        var camino = floyd.reconstruirCamino(0, 2);

        assertEquals(3, camino.size());
        assertEquals(0, camino.get(0));
        assertEquals(1, camino.get(1));
        assertEquals(2, camino.get(2));
    }
}
