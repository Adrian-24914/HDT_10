import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CentroGrafoTest {
    @Test
    public void testCentroSimple() {
        int INF = Integer.MAX_VALUE / 2;

        int[][] distancias = {
            {0, 2, 5},
            {2, 0, 3},
            {5, 3, 0}
        };

        int centro = CentroGrafo.calcularCentro(distancias);
        assertEquals(1, centro);  // El nodo 1 tiene la menor excentricidad
    }
}
