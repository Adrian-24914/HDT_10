import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GrafoTest {
    private Grafo grafo;

    @BeforeEach
    public void setUp() {
        grafo = new Grafo();
    }

    @Test
    public void testAgregarCiudad() {
        grafo.agregarCiudad("CiudadX");
        assertTrue(grafo.getCiudades().contains("CiudadX"));
        assertEquals(1, grafo.getCiudades().size());
    }

    @Test
    public void testAgregarYEliminarArco() {
        grafo.agregarArco("A", "B", new int[]{5, 10, 15, 20});
        int[][] matriz = grafo.getMatriz(Clima.NORMAL);
        int i = grafo.getCiudades().indexOf("A");
        int j = grafo.getCiudades().indexOf("B");
        assertEquals(5, matriz[i][j]);

        grafo.eliminarArco("A", "B");
        assertEquals(Integer.MAX_VALUE / 2, grafo.getMatriz(Clima.NORMAL)[i][j]);
    }
}
