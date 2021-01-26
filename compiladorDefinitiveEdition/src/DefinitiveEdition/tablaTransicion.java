package DefinitiveEdition;

public class tablaTransicion {
	
	/*
	 * 
	 *   !START!
	 *   	Entero suma, resta, contadorPar, entraForInterno;
	 *   	Flotante entrada;
	 *   	leer entrada ;
	 *   	suma = 0;
	 *   	resta = 0;
	 *   	entraForInterno = 0;
	 *   	contadorPar = 0;
	 *      for( int i=0; i<10; i++ ){
	 *         contadorPar = contadorPar + 2;
	 *         if(contadorPar < 8){
	 *            suma = contadorPar + i;
	 *         }else{
	 *            for( int j=0; j<contadorPar; j++ ){
	 *               entraForInterno = entraForInterno + 1;
	 *            }
	 *         }
	 *      }
	 *      
	 *      imp contadorPar ;
	 *      imp entraForInterno ;
	 *      imp suma ;
	 *      imp resta ;
	 *      imp entrada ;
	 *      // imp "La suma es: " + suma ;   --> ¿Se podrá?
	 *      
	 *   !END!
	 *       
	 */
	
	String Tabla [][]= new String [33][22];
	String producciones[][]=new String[39][2];
	int tiposDatos[][]=new int[3][3];
	public tablaTransicion() {		
			leerArchivo tablaNueva = new leerArchivo();
			Tabla = tablaNueva.matriz;
			System.out.println("GENERADO");
			for (int i = 0; i < Tabla.length; i++) {
				for (int j = 0; j < Tabla[0].length; j++) {
					System.out.print(Tabla[i][j] + " ");
				}
				System.out.println();
			}
		
		//LLENADO DE LAS PRODUCCIONES
		producciones[0][0]="INIC'";
		producciones[0][1]="INIC";
		
		producciones[1][0]="INIC";
		producciones[1][1]="!START! P !END!";
		
		producciones[2][0]="P";
		producciones[2][1]="Tipo id V";
		
		producciones[3][0]="P";
		producciones[3][1]="{ A }";
		
		producciones[4][0]="Tipo";
		producciones[4][1]="entero";
		
		producciones[5][0]="Tipo";
		producciones[5][1]="flotante";
		
		producciones[6][0]="Tipo";
		producciones[6][1]="caracter";
		
		producciones[7][0]="V";
		producciones[7][1]=", id V";
		
		producciones[8][0]="V";
		producciones[8][1]="; P";
		
		producciones[9][0]="A";
		producciones[9][1]="id = E ; A";
		
		producciones[10][0]="A";
		producciones[10][1]="id = leer ; A";
		
		producciones[11][0]="A";
		producciones[11][1]="SENT A";
		
		producciones[12][0]="A";
		producciones[12][1]="imp id ; A";
		
		producciones[13][0]="E";
		producciones[13][1]="E + T";
		
		producciones[14][0]="E";
		producciones[14][1]="E - T";
		
		producciones[15][0]="E";
		producciones[15][1]="T";
		        
		producciones[16][0]="T";
		producciones[16][1]="T * F";
		
		producciones[17][0]="T";
		producciones[17][1]="T / F";
		
		producciones[18][0]="T";
		producciones[18][1]="F";
		
		producciones[19][0]="F";
		producciones[19][1]="( E )";
		
		producciones[20][0]="F";
		producciones[20][1]="id";
		
		producciones[21][0]="SENT";
		producciones[21][1]="IF";
		
		producciones[22][0]="SENT";
		producciones[22][1]="FOR";
		
		producciones[23][0]="IF";
		producciones[23][1]="si ( CONDICION1 ) then A ELSE";
		
		producciones[24][0]="ELSE";
		producciones[24][1]="finsi";
		        
		producciones[25][0]="ELSE";
		producciones[25][1]="sino A finsi";
		
		producciones[26][0]="FOR";
		producciones[26][1]="para ( CONDICION2 ) A finpara";
		
		producciones[27][0]="CONDICION1";
		producciones[27][1]="id SIGNOS id";
		
		producciones[28][0]="SIGNOS";
		producciones[28][1]=">";
		
		producciones[29][0]="SIGNOS";
		producciones[29][1]="<";
		
		producciones[30][0]="SIGNOS";
		producciones[30][1]=">=";
		
		producciones[31][0]="SIGNOS";
		producciones[31][1]="<=";
		
		producciones[32][0]="SIGNOS";
		producciones[32][1]="==";
		
		producciones[33][0]="CONDICION2";
		producciones[33][1]="Tipo id = id ; CONDICION1 ; id SM";
		
		producciones[34][0]="SM";
		producciones[34][1]="++";
		
		producciones[35][0]="SM";
		producciones[35][1]="--";
		
		producciones[36][0]="SM";
		producciones[36][1]="+ id";
		
		producciones[37][0]="SM";
		producciones[37][1]="- id";
		
		producciones[38][0]="A";
		producciones[38][1]="finasigna";	
		
		/*
         *                 TABLA DE TIPOS
		 *    -------------------------------------
		 *   | +,-,*,/ |  int 0 | float 1 | char 2 | 
		 *    -------------------------------------
		 *   |  int 0  |    0   |    e    |   e    |
		 *    -------------------------------------
		 * 	 | float 1 |    e   |    1    |   e    |
		 *    -------------------------------------
		 *   |  char 2 |    e   |    e    |   2    |
		 *    -------------------------------------
		 *    int -> 0
		 *    float -> 1
		 *    char -> 2     
		 *    error -> 9
		 *    
		 */
		tiposDatos[0][0]=0; 
		tiposDatos[0][1]=9;
		tiposDatos[0][2]=9; //El 9 es error.
		
		tiposDatos[1][0]=9;
		tiposDatos[1][1]=1;
		tiposDatos[1][2]=9;//El 9 es error.
		
		tiposDatos[2][0]=9;//El 9 es error.
		tiposDatos[2][1]=9;//El 9 es error.
		tiposDatos[2][2]=2;
	
		
	}
	
}
