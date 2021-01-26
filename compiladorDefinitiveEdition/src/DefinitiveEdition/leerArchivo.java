package DefinitiveEdition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class leerArchivo {
   /* public static void main(String[] args) {
      
    }
    */
	
	String matriz[][] = new String[91][51];
	public leerArchivo() {
	  File archivo = null;
      FileReader Fr = null;
      BufferedReader br = null;
      
      try {
          archivo = new File("\\tabla.txt");
          Fr = new FileReader(archivo.toString());
          br = new BufferedReader(Fr);
          String linea;
          String delimitador = "\t";
          
          
          //Cuenta las líneas y a la vez sería el número de filas
          int numlinea=0;
          //validación si existe línea
          while (((linea = br.readLine()) != null)) {
              //Guardar datos de linea en la matriz
              String a[]=linea.split(delimitador);
             //Ingresa linea por linea a la matriz
              for (int l = 0; l < a.length; l++) {
                  //ingresamos los datos de cada columna de "a" a la matriz.
                  //"numlinea" hace de fila, "l" es el numero de la columna.
              	if(a[l].isEmpty() || a[l] == null)
              		matriz[numlinea][l] = "er";
              	else {
              		matriz[numlinea][l] = a[l];
              	}
                  //Prueba de que los datos están llenando la fila de la matriz.
                  //System.out.print(matriz[numlinea][l]+" ");  
              }
              //Incremento de numero de línea.
              numlinea++;
          }
          //Impresión de la matriz
           System.out.println("MATRIZ");
           System.out.println("------");
             for (int filas = 0; filas < matriz.length; filas++) {
                  for (int colum = 0; colum < matriz[filas].length; colum++) {
                      //Imprime las columnas de cada fila
                  	//PARCHE CORRECTIVO CON LOS NULL
                  	if(matriz[filas][colum] == null) {
                  		matriz[filas][colum] = "er";
                  	}
                      System.out.print(matriz[filas][colum]+" ");
                  }
                  //Imprime uns alto de línea para cada fila
                  System.out.println();   
              }
      } catch (IOException e) {
          System.out.println(e);
      }
	}
}