import networkx as nx
import numpy as np
from enum import Enum
import os

class Clima(Enum):
    NORMAL = 0
    LLUVIA = 1
    NIEVE = 2
    TORMENTA = 3

class SistemaLogistica:
    def __init__(self):
        self.grafos = {
            Clima.NORMAL: nx.DiGraph(),
            Clima.LLUVIA: nx.DiGraph(),
            Clima.NIEVE: nx.DiGraph(),
            Clima.TORMENTA: nx.DiGraph()
        }
        self.clima_actual = Clima.NORMAL
        
    def cargar_archivo(self, nombre_archivo):
        """Carga el grafo desde un archivo txt"""
        try:
            with open(nombre_archivo, 'r', encoding='utf-8') as archivo:
                for linea in archivo:
                    partes = linea.strip().split()
                    if len(partes) >= 6:
                        ciudad1 = partes[0]
                        ciudad2 = partes[1]
                        tiempos = [int(partes[i]) for i in range(2, 6)]
                        
                        # Agregar nodos a todos los grafos
                        for grafo in self.grafos.values():
                            grafo.add_node(ciudad1)
                            grafo.add_node(ciudad2)
                        
                        # Agregar arcos con pesos según el clima
                        for i, clima in enumerate(Clima):
                            self.grafos[clima].add_edge(ciudad1, ciudad2, weight=tiempos[i])
                            
            print(f"Archivo {nombre_archivo} cargado exitosamente.")
            
        except FileNotFoundError:
            print(f"Error: No se encontró el archivo {nombre_archivo}")
        except Exception as e:
            print(f"Error al cargar el archivo: {e}")
    

    
    def encontrar_ruta_mas_corta(self, origen, destino):
        """Encuentra la ruta más corta entre dos ciudades usando Floyd-Warshall"""
        grafo = self.grafos[self.clima_actual]
        
        if origen not in grafo.nodes() or destino not in grafo.nodes():
            print("Error: Una o ambas ciudades no existen en el grafo.")
            return None, None
        
        try:
            # NetworkX implementa Floyd-Warshall internamente en shortest_path
            ruta = nx.shortest_path(grafo, origen, destino, weight='weight')
            distancia = nx.shortest_path_length(grafo, origen, destino, weight='weight')
            
            return ruta, distancia
            
        except nx.NetworkXNoPath:
            print(f"No existe ruta entre {origen} y {destino}")
            return None, None
    
    def calcular_centro_grafo(self):
        """Calcula el centro del grafo usando excentricidad"""
        grafo = self.grafos[self.clima_actual]
        nodos = list(grafo.nodes())
        
        if len(nodos) == 0:
            return None
        
        if len(nodos) == 1:
            return nodos[0]
        
        excentricidades = {}
        
        for nodo in nodos:
            max_distancia = 0
            try:
                # Calcular distancias desde este nodo a todos los demás
                distancias = nx.single_source_shortest_path_length(grafo, nodo, weight='weight')
                
                for otro_nodo in nodos:
                    if otro_nodo != nodo:
                        if otro_nodo in distancias:
                            max_distancia = max(max_distancia, distancias[otro_nodo])
                        else:
                            # Si no hay camino, la excentricidad es infinita
                            max_distancia = float('inf')
                            break
                            
                excentricidades[nodo] = max_distancia
                
            except Exception:
                excentricidades[nodo] = float('inf')
        
        # Encontrar el nodo con menor excentricidad
        centro = min(excentricidades.keys(), key=lambda x: excentricidades[x])
        return centro
    
    def agregar_conexion(self, ciudad1, ciudad2, tiempos):
        """Agrega una nueva conexión entre dos ciudades"""
        if len(tiempos) != 4:
            print("Error: Debe proporcionar 4 tiempos (normal, lluvia, nieve, tormenta)")
            return
        
        # Agregar nodos si no existen
        for grafo in self.grafos.values():
            grafo.add_node(ciudad1)
            grafo.add_node(ciudad2)
        
        # Agregar arcos con pesos según el clima
        for i, clima in enumerate(Clima):
            self.grafos[clima].add_edge(ciudad1, ciudad2, weight=tiempos[i])
        
        print(f"Conexión agregada entre {ciudad1} y {ciudad2}")
    
    def eliminar_conexion(self, ciudad1, ciudad2):
        """Elimina la conexión entre dos ciudades"""
        for grafo in self.grafos.values():
            if grafo.has_edge(ciudad1, ciudad2):
                grafo.remove_edge(ciudad1, ciudad2)
        
        print(f"Conexión eliminada entre {ciudad1} y {ciudad2}")
    
    def cambiar_clima(self, nuevo_clima):
        """Cambia el clima actual para los cálculos"""
        if isinstance(nuevo_clima, str):
            try:
                self.clima_actual = Clima[nuevo_clima.upper()]
            except KeyError:
                print("Clima no válido. Use: NORMAL, LLUVIA, NIEVE, TORMENTA")
                return
        else:
            self.clima_actual = nuevo_clima
        
        print(f"Clima cambiado a: {self.clima_actual.name}")
    
    def cargar_archivo_manual(self):
        """Permite al usuario especificar la ruta del archivo"""
        print("\nCargar archivo de datos:")
        print("Archivos .txt en el directorio actual:")
        
        try:
            archivos_txt = [f for f in os.listdir('.') if f.endswith('.txt')]
            if archivos_txt:
                for i, archivo in enumerate(archivos_txt, 1):
                    print(f"{i}. {archivo}")
            else:
                print("No se encontraron archivos .txt")
        except Exception as e:
            print(f"Error al listar archivos: {e}")
        
        nombre_archivo = input("\nIngrese el nombre del archivo (con extensión): ").strip()
        
        if nombre_archivo:
            # Limpiar grafos existentes
            for grafo in self.grafos.values():
                grafo.clear()
            
            self.cargar_archivo(nombre_archivo)
        else:
            print("Nombre de archivo no válido.")
    
    def menu_principal(self):
        """Menú principal del programa"""
        while True:
            print("\n" + "="*50)
            print("SISTEMA DE LOGÍSTICA - NETWORKX")
            print("="*50)
            print("1. Consultar ruta más corta entre ciudades")
            print("2. Mostrar centro del grafo")
            print("3. Modificar grafo")
            print("4. Cambiar condiciones climáticas")
            print("5. Cargar archivo de datos")
            print("6. Finalizar programa")
            print("="*50)
            
            try:
                opcion = input("Seleccione una opción (1-6): ").strip()
                
                if opcion == "1":
                    self.consultar_ruta()
                elif opcion == "2":
                    self.mostrar_centro()
                elif opcion == "3":
                    self.menu_modificar()
                elif opcion == "4":
                    self.menu_clima()
                elif opcion == "5":
                    self.cargar_archivo_manual()
                elif opcion == "6":
                    print("¡Hasta luego!")
                    break
                else:
                    print("Opción no válida. Intente nuevamente.")
                    
            except KeyboardInterrupt:
                print("\n¡Hasta luego!")
                break
    
    def consultar_ruta(self):
        """Consulta la ruta más corta entre dos ciudades"""
        print(f"\nConsulta de ruta (Clima actual: {self.clima_actual.name})")
        ciudades = list(self.grafos[self.clima_actual].nodes())
        
        if len(ciudades) < 2:
            print("Necesita al menos 2 ciudades para consultar rutas.")
            return
        
        print("Ciudades disponibles:", ", ".join(sorted(ciudades)))
        
        origen = input("Ciudad de origen: ").strip()
        destino = input("Ciudad de destino: ").strip()
        
        ruta, distancia = self.encontrar_ruta_mas_corta(origen, destino)
        
        if ruta and distancia is not None:
            print(f"\nRuta más corta de {origen} a {destino}:")
            print(f"Camino: {' → '.join(ruta)}")
            print(f"Tiempo total: {distancia} horas")
        else:
            print("No se pudo encontrar una ruta.")
    
    def mostrar_centro(self):
        """Muestra el centro del grafo"""
        centro = self.calcular_centro_grafo()
        if centro:
            print(f"\nCentro del grafo (Clima: {self.clima_actual.name}): {centro}")
        else:
            print("No se pudo calcular el centro del grafo.")
    
    def menu_modificar(self):
        """Menú para modificar el grafo"""
        print("\nModificar grafo:")
        print("1. Agregar conexión entre ciudades")
        print("2. Eliminar conexión entre ciudades")
        print("3. Volver al menú principal")
        
        opcion = input("Seleccione una opción (1-3): ").strip()
        
        if opcion == "1":
            ciudad1 = input("Primera ciudad: ").strip()
            ciudad2 = input("Segunda ciudad: ").strip()
            print("Ingrese los tiempos de viaje:")
            try:
                normal = int(input("Tiempo normal (horas): "))
                lluvia = int(input("Tiempo con lluvia (horas): "))
                nieve = int(input("Tiempo con nieve (horas): "))
                tormenta = int(input("Tiempo con tormenta (horas): "))
                
                self.agregar_conexion(ciudad1, ciudad2, [normal, lluvia, nieve, tormenta])
                
            except ValueError:
                print("Error: Ingrese valores numéricos válidos.")
                
        elif opcion == "2":
            ciudades = list(self.grafos[self.clima_actual].nodes())
            print("Ciudades disponibles:", ", ".join(sorted(ciudades)))
            
            ciudad1 = input("Primera ciudad: ").strip()
            ciudad2 = input("Segunda ciudad: ").strip()
            
            self.eliminar_conexion(ciudad1, ciudad2)
    
    def menu_clima(self):
        """Menú para cambiar las condiciones climáticas"""
        print("\nCondiciones climáticas disponibles:")
        for i, clima in enumerate(Clima, 1):
            marca = "✓" if clima == self.clima_actual else " "
            print(f"{i}. {clima.name} {marca}")
        
        try:
            opcion = int(input("Seleccione clima (1-4): "))
            if 1 <= opcion <= 4:
                nuevo_clima = list(Clima)[opcion - 1]
                self.cambiar_clima(nuevo_clima)
            else:
                print("Opción no válida.")
        except ValueError:
            print("Ingrese un número válido.")

def main():
    sistema = SistemaLogistica()
    
    # Buscar archivo en diferentes ubicaciones
    posibles_rutas = [
        "logistica.txt",
        "./logistica.txt",
        os.path.join(os.path.dirname(__file__), "logistica.txt"),
        os.path.join(os.getcwd(), "logistica.txt")
    ]
    
    archivo_encontrado = False
    for ruta in posibles_rutas:
        if os.path.exists(ruta):
            print(f"Archivo encontrado en: {ruta}")
            sistema.cargar_archivo(ruta)
            archivo_encontrado = True
            break
    
    # Ejecutar menú principal
    sistema.menu_principal()

if __name__ == "__main__":
    main()