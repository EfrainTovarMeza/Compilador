package DefinitiveEdition;

import java.util.NoSuchElementException;

/*
 * int a , b , c , y ;
 * float x ;
 * y = a + b * c - x ;
 * 
 */

/*
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
*/
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class sintactico {
	Vector<String> pilaSintactica=new Vector<String>();
	Vector<String> pilaSemantica=new Vector<String>();
	Vector<String> pilaOperadores=new Vector<String>();
	tablaTransicion tabla=new tablaTransicion();
	Vector<String> pilaEntrada=new Vector<String>();
	Vector<String> historialSemantico=new Vector<String>();
	Vector<String> historialSintactico=new Vector<String>();
	Vector<String> historialEntrada=new Vector<String>();
	Vector<String> historialSalidas=new Vector<String>();
	Vector<String> historialIntermedio=new Vector<String>();
	//DECLARACACIÓN DE LA TABLA PARA MOSTRAR SU RESULTADO ACÁ BIEN BONIS
	DefaultTableModel modeloResultado;
	//VARIABLES NECESARIAS PARA EL PROCESO
	int col=0, fila=0;
	String tiposD[][];
	int cualToken=0;
	int errorSemantico=0;	//SI ENCUENTRA UN ERROR SEMÁNTICO SE PONDRÁ EN 1 Y ACABARÁ DE ANALIZAR EL RESTO DE LA CADENA
	int tipoErrorSemantico=0; //SI EL TIPO DE ERROR ES 1 QUIERE DECIR QUE NO ENCONTRÓ EL TOKEN Y SI ES 2 ENTONCES LOS TIPOS DE DATOS NO COINCIDEN, NO SON COMPATIBLES.
	int errorSintactico=0;  //SI ENCUENTRA UN ERROR SINTÁCTICO SE PONDRÁ EN 1 Y ACABARÁ DE ANALIZAR EL RESTO DE LA CADENA
	
	//VERIFICACIONES DE CODIGO INTERMEDIO
	int contIF=0, contElse = 0, puntero=0, punteroInternoIF=0;
	Vector<String> condicionesIF=new Vector<String>();
	Vector<String> hack=new Vector<String>();
	Vector<String> hack2 =new Vector<String>();
	Vector<String> codigoEntrante=new Vector<String>();
	Vector<asignaciones> asignaciones=new Vector<asignaciones>();
	boolean banderaIgual = false;
	boolean banderaPara  = false;
	boolean banderaLeer  = false;
	boolean banderaImp   = false;
	Vector<String> pilaIF=new Vector<String>();
	Vector<String> vectorIF=new Vector<String>();
	Vector<idsval> idVal=new Vector<idsval>();
	
	int contFOR=0;
	Vector<String> pilaFOR=new Vector<String>();
	Vector<String> vectorFOR=new Vector<String>();
	Vector<String> condicionesFOR=new Vector<String>();
	Vector<String> finalesFOR=new Vector<String>();
	String estructuraFOR="" , finalFOR="";
	
	public sintactico(String codigo) {
		errorSemantico=0;errorSintactico=0;		
		StringTokenizer separador=new StringTokenizer(codigo, " ");
		codigoEntrante = new Vector<String>();
		while(separador.hasMoreTokens()) {
			codigoEntrante.add(separador.nextToken());
		}
		
		lexicoP lexico=new lexicoP(codigo);

		for (int i = 0; i < lexico.tokensRes.size(); i++) {
			pilaEntrada.add(lexico.tokensRes.get(i));
		}
		
		for (int i = 0; i < lexico.vAsignacion.size() ; i++) {
			asignaciones.add(lexico.vAsignacion.get(i));
		}
		
		for (int i = 0; i < lexico.idVal.size(); i++) {
			idVal.add(lexico.idVal.get(i));
		}
		
		tiposD=new String[lexico.tipos.length][lexico.tipos[0].length];
		for (int i = 0; i < lexico.tipos.length; i++) {
			for (int j = 0; j < lexico.tipos[0].length; j++) {
				tiposD[i][j]=lexico.tipos[i][j];
			}
		}
		System.out.println("       ...::: INICIA ANÁLISIS SINTACTICO :::...");
		System.out.println("");
		
		pilaSintactica.add("I0");
		verPilaSintactica();
		analizaSintactica();
	}
	
	public void analizaSintactica() {
		while(!pilaEntrada.isEmpty()) {
			if(compara(pilaEntrada.firstElement(), pilaSintactica.lastElement()).equals("er")) {	//Le manda a comparar el token, NO el tipo de dato.
				System.out.println("\nRetorna error: "+ col + " - " + fila);
				errorSintactico=1;
				break;
			}else {
				//IMRIME ESTADO DE LA PILA
				if(errorSemantico == 1) {
					System.out.println("FIN DE EJECUCIÓN, SE ENCONTRÓ UN ERROR SEMÁNTICO");
					break;
				}else {					
					verPilaSintactica();
				}
			}
		}
		
		for (int i = 0; i < historialSemantico.size(); i++) {
			if(historialSemantico.get(i).equals("9"))
				errorSemantico = 1;
		}
		
			
		//SI CAE AQUÍ Y LA ÚLTIMA PRODUCCIÓN FUE P0 ADEMÁS DE QUE LA ENTRADA YA NO HAY NADA O ES FINPROG SE ACEPTA
		if(pilaEntrada.isEmpty() && pilaSintactica.lastElement().equals("INIC'")) {
			if(errorSemantico == 0 && errorSintactico == 0) {
				System.out.println("SE ACEPTA SINTÁCTICAMENTE\nSE ACEPTA SEMÁNTICAMENTE");
				historialSintactico.add("SE ACEPTA SINTACTICAMENTE");
				historialSemantico.add("SE ACEPTA SEMANTICAMENTE");
				//tablaSintactica.setBackground(Color.green);
				JOptionPane.showMessageDialog(null, "SE ACEPTA SINTÁCTICAMENTE\nSE ACEPTA SEMÁNTICAMENTE");
				//INICIA EL PROCESO PARA CREACIÓN DE CÓDIGO INTERMEDIO
				codigoObj(codigoEntrante);
			}
		}else {
			if(errorSintactico == 0 && errorSemantico == 1) {
				historialSintactico.add("SE ACEPTA SINTACTICAMENTE");
				historialSemantico.add("SE RECHAZA SEMANTICAMENTE");
				JOptionPane.showMessageDialog(null, "SE ACEPTA SINTÁCTICAMENTE\nSE RECHAZA SEMÁNTICAMENTE");
				System.out.println("SE ENCONTRARON FALLAS SEMÁNTICAS EN LA CADENA ANALIZADA");
				JOptionPane.showMessageDialog(null, "SE ENCONTRARON FALLAS SEMÁNTICAS EN LA CADENA ANALIZADA");
				if(tipoErrorSemantico == 1) {
					JOptionPane.showMessageDialog(null, "ERROR SEMÁNTICO TIPO 1: SE ESPERABA UN TOKEN DECLARADO");
				}
			}else if(errorSintactico == 1 && errorSemantico == 0) {
				System.out.println("SE ENCONTRARON FALLAS SINTÁCTICAS EN LA CADENA ANALIZADA");
				JOptionPane.showMessageDialog(null, "SE ENCONTRARON FALLAS SINTÁCTICAS EN LA CADENA ANALIZADA");
				historialSintactico.add("SE RECHAZA SINTACTICAMENTE");
			}
		}
		//AGREGA TODOS LOS DATOS RECOLECTADOS EN EL ANALISIS A LA TABLA RESULTADOS
		modeloResultado=new DefaultTableModel(historialSintactico.size(), 0);
		modeloResultado.addColumn("ENTRADA");
		modeloResultado.addColumn("PILA SINTÁCTICA");
		modeloResultado.addColumn("SALIDA");
		modeloResultado.addColumn("PILA SEMÁNTICA");
		try {
			for (int i = 0; i < historialEntrada.size(); i++) {
				modeloResultado.setValueAt(historialEntrada.get(i), i, 0);
			}
			for (int i = 0; i < historialSintactico.size(); i++) {
				modeloResultado.setValueAt(historialSintactico.get(i), i, 1);
			}
			for (int i = 0; i < historialSalidas.size(); i++) {
				modeloResultado.setValueAt(historialSalidas.get(i), i, 2);
			}
			for (int i = 0; i < historialSemantico.size(); i++) {
				modeloResultado.setValueAt(historialSemantico.get(i), i, 3);
			}
		}catch(ArrayIndexOutOfBoundsException ex) {}
	}
	
	public String compara(String token, String estado){
		//BUSCAR EN LA TABLA SINTÁCTICA
		System.out.println("Busca en tabla: " + token + " , " + estado);
		buscarTabla(token, estado); //QUE COLUMA Y QUE FILA ESTA RESULTADO
		System.out.println("Encontré en la tabla: " + tabla.Tabla[col][fila]);
		if(tabla.Tabla[col][fila].contains("I")) {
		
			//SI EL VALOR DE LA TABLA CONTIENE UNA I QUIERE DECIR QUE RECIBIÓ UN ESTADO Y NO UNA PRODUCCIÓN U ERROR. 
			//POR LO TANTO DESLIZA EN LA PILA.
			System.out.println("Deslizar: " + token + " , " + tabla.Tabla[col][fila]);
			historialSalidas.add("Deslizar: " + token + " , " + tabla.Tabla[col][fila]);
			pilaSintactica.add(token);					// Token de entrada
			pilaSintactica.add(tabla.Tabla[col][fila]);	//Estado producido por tabla
			
			if(pilaEntrada.firstElement().equals("=")) banderaIgual = true;	
			
			if(pilaEntrada.firstElement().equals("leer")) {
				pilaSemantica.add("l");  // leer a ;
										// para ( entero i ; )
			}else if(pilaEntrada.firstElement().equals("para")) {
				banderaPara=true;
			}else if(pilaEntrada.firstElement().equals(")")) {
				banderaPara=false;
			}
			
			
			if(pilaEntrada.firstElement().equals(";") && banderaIgual && !banderaPara) {

				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println("             SI ES ;");
				System.out.println("      " + asignaciones.firstElement().tipo);
				System.out.println("      " + pilaSemantica.lastElement());
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				System.out.println(" --------------------------------- ");
				
				if(asignaciones.firstElement().tipo.equals(pilaSemantica.lastElement())) {
					//SACAR EL ELEMENTO YA ANALIZADO
					System.out.println("      " + asignaciones.firstElement().tipo);
					System.out.println("      " + pilaSemantica.lastElement());
					
					pilaSemantica.remove(pilaSemantica.size()-1);
					asignaciones.remove(0);
					banderaIgual = false;
					System.out.println("SI JALO");
					
				}else {
					if(!pilaSemantica.lastElement().equals("l")) {
						//ERROR SEMANTICO
						errorSemantico = 1; 
						banderaIgual = false;
						System.out.println("ERROR");
						System.out.println("      " + asignaciones.firstElement().tipo);
						System.out.println("      " + pilaSemantica.lastElement());
					}else {
						pilaSemantica.remove(pilaSemantica.size()-1);
					}
					
					
				}
			
				
			}
			
			//SE RETIRA DE LA ENTRADA EL VALOR DESPLAZADO
			pilaEntrada.remove(pilaEntrada.firstElement());
			if(token.equals("(") | token.equals("+") | token.equals("-")) {
				//SI ES UN OPERADOR LO AGREGA A LA PILA DE OPERADORES
				pilaOperadores.add(token);
			}else if(token.equals("/") | token.equals("*")) {
				//SI ES DE MAYOR PRIORIDAD
				pilaOperadores.add(token);
			}
			cualToken++;
			return "ok";
			
			
		}else if(tabla.Tabla[col][fila].contains("P")) {
			//SI EL VALOR DE LA TABLA CONTIENE UNA P QUIERE DECIR QUE RECIBIÓ UNA PRODUCCIÓN.
			//POR LO TANTO REDUCIR DE LA PILA.
			System.out.println("ES PRODUCCIÓN");
			String prod=tabla.Tabla[col][fila]; 
			String extraerNumeroP=prod.substring(1); // P1 
			try {
				int numProd=Integer.parseInt(extraerNumeroP);
				System.out.println("Producción " + tabla.Tabla[col][fila] + ": " + tabla.producciones[numProd][0] + " -> " + tabla.producciones[numProd][1]);
				historialSalidas.add("Producción "+ tabla.Tabla[col][fila] + ": " + tabla.producciones[numProd][0] + " -> " + tabla.producciones[numProd][1]);
				StringTokenizer separar=new StringTokenizer(tabla.producciones[numProd][1], " ");
				Vector<String> elementosProduccion=new Vector<String>();
					//GUARDAR CADA ELEMENTO DE LA PRODUCCIÓN PARA PODER REDUCIR LA PILA SINTACTICA
				while(separar.hasMoreTokens()) {
					elementosProduccion.add(separar.nextToken());
				}
				//SI LA PRODUCCION ES F -> id entonces agregar a la pila semántica el tipo de dato del id
				//SI LA PRODUCCIÓN ES ALGUNA OPERACIÓN, REALIZARLA BUSCANDO EN LA TABLA DE DATOS
				if(numProd == 20) {
					//P20: F -> id ENTONCES AL CAER EN ESTA PRODUCCION SE AGREGARÍA EL TIPO DE DATO DEL TOKEN ENTRANTE A LA PILA.
					if(tiposD[cualToken-1][0] == null | tiposD[cualToken-1][0]== "9") {
						//SI EL TOKEN QUE SE ESTÁ BUSCANDO ES UN NULO QUIERE DECIR QUE NO HA SIDO DECLARADO ESE TOKEN.
						//ERROR SEMÁNTICO, SE ESPERABA UN TOKEN DECLARADO
						/*
						 * TIPOS DE ERRORES SEMANTICOS
						 * 	1- NO EXISTE EL VALOR
						 *  2- NO COINCIDEN LOS TIPOS -> int = int
						 *  
						 */
						errorSemantico=1;
						tipoErrorSemantico=1;
						historialSemantico.add("Error");
					}else {
						//PARTE DE LA SEMÁNTICA, BUSCA EL TIPO DE DATO CORRESPONDIENTE EN LA TABLA DE DATOS
						//SI ES LA PRODUCCIÓN 16 AGREGA EL TIPO DE DATO A LA PILA SEMÁNTICA
						pilaSemantica.add(tiposD[cualToken-1][1]); //AGREGA EL VALOR DEL TIPO DE DATO CORRESPONDIENTE A LA PILA SEMANTICA
						System.out.println(cualToken-1);
						System.out.println("VALOR SEMANTICO: "+ tiposD[cualToken-1][0] + " " + pilaSemantica.lastElement());
					}
				}else if(numProd == 19) {
					//F -> ( E )
					//SE RETIRA EL PARENTESIS QUE CIERRA LA AGRUPACIÓN DE LA EXPRESIÓN
					pilaOperadores.remove(pilaOperadores.size()-1);
				}else if (numProd == 1) {
					
					System.out.println("AQUISI");
					
				}else if(numProd == 13 || numProd == 14 || numProd == 16 || numProd == 17) {
					//P9 ES E -> E + T POR LO TANTO SE EJECUTA LA EXPRESIÓN DE MANERA SEMÁNTICA
					//BUSCAR EN LA TABLA DE DATOS EL RESULTANTE
					String cor1 = pilaSemantica.get(pilaSemantica.size()-2);
					String cor2 = pilaSemantica.get(pilaSemantica.size()-1);
					int C1=Integer.parseInt(cor1);
					int C2=Integer.parseInt(cor2);
					System.out.println("BUSCANDO TIPOS: " + C1 + " - " + C2);
					int tipoR = tabla.tiposDatos[C1][C2];
					if(tipoR == 9)
						errorSemantico = 1;
					
					System.out.println("TIPOS DATOS: " + C1 + " - "+ C2);
					System.out.println("Tipo resultante de la operación semántica: " + tipoR);
					if(pilaSemantica.get(pilaSemantica.size()-2) == null)
						System.out.println("Error semántico, " + pilaSemantica.get(pilaSemantica.size()-2) + " NO EXISTE");
					
					pilaSemantica.remove(pilaSemantica.size()-1);
					pilaSemantica.remove(pilaSemantica.size()-1);
					
					pilaSemantica.add(tipoR+"");
					//SE RETIRA EL OPERADOR + DE LA PILA
					pilaOperadores.remove(pilaOperadores.size()-1);
				}else if(numProd == 27) {
					//ES CONDICION1 -> LA CUAL SE GENERA EN LOS IF
					historialIntermedio.add("bool");
				}
				
				String prueba2="";
				for (int i = 0; i < elementosProduccion.size(); i++) {
					System.out.println("Elementos producidos: " + elementosProduccion.size());
					System.out.println("Elimina de pila sintáctica: " + pilaSintactica.lastElement());
					pilaSintactica.remove(pilaSintactica.size()-1); //ELIMINA EL ESTADO DEL TOKEN
					System.out.println("Elimina de pila sintáctica: " + pilaSintactica.lastElement());
					prueba2=pilaSintactica.lastElement(); //ALMACENA EL VALOR ANTES DE BORRARLO TEMPORALEMNTE
					pilaSintactica.remove(pilaSintactica.size()-1);	//SE ELIMINA EL TOKEN
				}
				if(prueba2.equals(pilaSintactica.lastElement())) {
					System.out.println("Me repito, no me burre soy malo");
					pilaSintactica.remove(pilaSintactica.size()-1);	//SE ELIMINA EL ELEMENTO
					if(prueba2.equals(pilaSintactica.lastElement())) {
						System.out.println("LA MALDAD PURA");	
					}
				}
				//verPilaSintactica();
				//AGREGAR A LA PILA LA PRODUCCION OBTENIDO ARRIBA
				pilaSintactica.add(tabla.producciones[numProd][0]);	//DE TENER; F -> id, toma el valor de F y lo almacena en la pila sintáctica.
					//BUSCAR NUEVO ESTADO APARTIR DE LO YA ADQUIRIDO.
					//LE MANDA EL VALOR ACTUAL (ULTIMOD DE LA PILA) Y UNO ANTERIOR, ES DECIR, EL ESTADO
				System.out.println("\nBUSCA NUEVO ESTADO");
				System.out.println("Busca en tabla: " + pilaSintactica.lastElement() + " , " + pilaSintactica.get(pilaSintactica.size()-2));
				buscarTabla(pilaSintactica.lastElement(), pilaSintactica.get(pilaSintactica.size()-2));
				if(!pilaSintactica.lastElement().equals("INIC'"))
					procesoProduccion(pilaSintactica.lastElement(), pilaSintactica.get(pilaSintactica.size()-2));
			}catch(NumberFormatException ex) {
				if(!pilaSintactica.lastElement().equals("INIC'"))
					procesoProduccion(pilaSintactica.lastElement(), pilaSintactica.get(pilaSintactica.size()-2));
				else
					pilaEntrada.remove(pilaEntrada.size()-1);
			}
			return "ok";
		}else 
			return "er";	
	}
	
	public void procesoProduccion(String token, String estado) {
		if(tabla.Tabla[col][fila].contains("I")) {
			//SI EL VALOR DE LA TABLA CONTIENE UNA I QUIERE DECIR QUE RECIBIÓ UN ESTADO Y NO UNA PRODUCCIÓN U ERROR. 
			//POR LO TANTO DESLIZA EN LA PILA.
			System.out.println("Deslizar nuevo estado: " + tabla.Tabla[col][fila]);
			//pilaSintactica.add(token); NO SE AGREGA DEBIDO A QUE SE ESTÁ BUSCANDO EL NUEVO ESTADO SOLAMENTE
			pilaSintactica.add(tabla.Tabla[col][fila]);	//Estado producido
		}else if(tabla.Tabla[col][fila].contains("P")) {
			//SI EL VALOR DE LA TABLA CONTIENE UNA P QUIERE DECIR QUE RECIBIÓ UNA PRODUCCIÓN.
			//POR LO TANTO REDUCIR DE LA PILA.
			String prod=tabla.Tabla[col][fila];
			String extraerNumeroP=prod.substring(1);
			int numProd=Integer.parseInt(extraerNumeroP);
			System.out.println("Producción " + tabla.Tabla[col][fila] + ": " + tabla.producciones[numProd][0] + " -> " + tabla.producciones[numProd][1]);
			StringTokenizer separar=new StringTokenizer(tabla.producciones[numProd][1], " ");
			Vector<String> elementosProduccion=new Vector<String>();
				//GUARDAR CADA ELEMENTO DE LA PRODUCCIÓN PARA PODER REDUCIR LA PILA SINTACTICA
			while(separar.hasMoreTokens()) {
				elementosProduccion.add(separar.nextToken());
			}
			
			for (int i = 0; i < elementosProduccion.size(); i++) {
				pilaSintactica.remove(pilaSintactica.lastElement());	//SE ELIMINA EL ESTADO
				pilaSintactica.remove(pilaSintactica.lastElement());	//SE ELIMINA EL ELEMENTO
			}
				//AGREGAR A LA PILA LA PRODUCCION OBTENIDO ARRIBA
			pilaSintactica.add(tabla.producciones[numProd][0]);	//DE TENER; F -> id, toma el valor de F y lo almacena en la pila sintáctica.
			
				//BUSCAR NUEVO ESTADO APARTIR DE LO YA ADQUIRIDO.
				//LE MANDA EL VALOR ACTUAL (ULTIMOD DE LA PILA) Y UNO ANTERIOR, ES DECIR, EL ESTADO
			System.out.println("Busca en tabla: " + token + " , " + estado);
			buscarTabla(pilaSintactica.lastElement(), pilaSintactica.get(pilaSintactica.size()-1));	
			procesoProduccion(pilaSintactica.lastElement(), pilaSintactica.get(pilaSintactica.size()-1));
		}
	}
	
	public void buscarTabla(String token, String estado) {
		fila=0;
		col=0;
		for (int i = 0; i < tabla.Tabla.length; i++) {
			for (int j = 0; j <  tabla.Tabla[0].length; j++) {
				if(tabla.Tabla[i][j].equals(token)) {
					System.out.print("Encontré a " + token + " en: " + i + " " + j +"\n");
					//AHORA BUSCAR EL ESTADO ENTRANTE Y CONSEGUIR EL VALOR EN ESAS COORDENADAS.
					fila=j;
					for (int j2 = 0; j2 < tabla.Tabla.length; j2++) {
						if(tabla.Tabla[j2][col].equals(estado)) {
							col=j2;
							System.out.println(col + " - " + fila +": "+tabla.Tabla[col][fila]);
							break;
						}
					}
				}
			}
		}
	}
	
	public void verPilaSintactica() {
		String histoSintactico="";
		System.out.println("\n\nPILA SINTÁCTICA:");
		for (int i = 0; i < pilaSintactica.size(); i++) {
			histoSintactico=histoSintactico + " " + pilaSintactica.get(i);
		}
		historialSintactico.add(histoSintactico);
		System.out.println(historialSintactico.lastElement());
		
		String histoEntrada="";
		System.out.println("\nENTRADA ACTUAL:");
		for (int i = 0; i < pilaEntrada.size(); i++) {
			histoEntrada=histoEntrada + " " + pilaEntrada.get(i);
		}
		historialEntrada.add(histoEntrada);
		System.out.println(historialEntrada.lastElement());
		
		String histoSemantico="";
		System.out.println("\nPILA SEMÁNTICA:");
		for (int i = 0; i < pilaSemantica.size(); i++) {
			histoSemantico=histoSemantico + " " +pilaSemantica.get(i);
		}
		historialSemantico.add(histoSemantico);
		System.out.println("HISTORIAL SEMANTICO: " + historialSemantico.lastElement());	
		
		System.out.println("\nPILA OPERADORES:");
		for (int i = 0; i < pilaOperadores.size(); i++) {
			System.out.print(pilaOperadores.get(i) + " ");
		}
		System.out.println("\n");
	}
	
	public void codigoObj( Vector<String> codigoEntrada ) {
		String codigoGenerado = "#include <stdio.h>\n   int main() {\n";
		boolean banderaSino = false;
		
		int i = 0;
		System.out.println(codigoEntrada.size());
		while(!codigoEntrada.get(i).equals("!END!")) {
			System.out.println("ANALIZANDO: "  + codigoEntrada.get(i) + " en: " + i);
			if(codigoEntrada.get(i).equals("!START!") | codigoEntrada.get(i).equals("!END!") | codigoEntrada.get(i).equals("{") 
					| codigoEntrada.get(i).equals("}") | codigoEntrada.get(i).equals("FinProg") | codigoEntrada.get(i).equals("finasigna")) {
				System.out.println("IGNORADO");
				i++;
			}else if(codigoEntrada.get(i).equals("entero")) {
				//CAMBIA EL VALOR entero por int
				codigoGenerado = codigoGenerado + "   int ";
				i++;
			}else if(codigoEntrada.get(i).equals("flotante")) {
				//CAMBIA EL VALOR entero por int
				codigoGenerado = codigoGenerado + "   float ";
				i++;
			}else if(codigoEntrada.get(i).equals("caracter")) {
				//CAMBIA EL VALOR entero por int
				codigoGenerado = codigoGenerado + "   char ";
				i++;
			}else if(codigoEntrada.get(i).equals("si")) {				   // --------------->  SI
				contIF++;
				pilaIF.add(contIF+""); // si ( a < b )
				i++;i++;
				String condicion = " int VCond"+contIF+" = ";  // int VCond1 = a<b ;  
				String condicionP = "VCond"+contIF;
				while(!codigoEntrada.get(i).equals(")")) {
					condicion = condicion + codigoEntrada.get(i);	
					i++;
				}
				condicion = condicion + ";\n";
				System.out.println("CONDICION DEL IF: " + condicion);
				// int VCond1 = a < b ;
				//
				
				condicionesIF.add(condicion);
				vectorIF.add("if(!" + condicionP+ " )\n goto Sino"+pilaIF.lastElement() + ";"); // if(!VCond1) goto Sino1
				i++;
				
			}else if(codigoEntrada.get(i).equals("para")) {					// --------------> PARA
				i++;
				contFOR++;					// para ( entero i = 0 ; i < b; i++ )
				pilaFOR.add(contFOR+"");
				int punteroActual = obtenerEstructuraFOR(codigoEntrada, i);
				i=punteroActual + 1 ;
				codigoGenerado = codigoGenerado + estructuraFOR;
				
			}else if(codigoEntrada.get(i).equals("finpara")) {
				/*
				    goto FOR1;
					Fin_For1:
					printf("\n\nTermina el for" );
				 */
				try {
					String finPara = "goto FOR"+pilaFOR.lastElement() + ";\n  Fin_For" + pilaFOR.lastElement() + ":\n" 
							 + "printf(\"\\n\\nTermina el for\" );";
					pilaFOR.remove(pilaFOR.size()-1);
					codigoGenerado = codigoGenerado + finalesFOR.lastElement() + finPara;
					finalesFOR.remove(finalesFOR.size()-1);
				}catch(NoSuchElementException ex) {}
				
				i++;
			}else if(codigoEntrada.get(i).equals("then")) {					// --------------> si ( condicion ) then  
				//CONTENIDO DEL IF
				//i++;
				int puntero = obtenerContenidoIF(codigoEntrada, i); 
				i = puntero;
			}else if(codigoEntrada.get(i).equals("finsi")) {					// --------------> FINSI
				//TRONÓ EL THEN POR EL FINSI POR LO TANTO AGREGAR EL FINSI_ULTIMO ELEMENTO DE LA PILA DE IF
				
				if(banderaSino) {
					String finS = "goto FinSi"+pilaIF.lastElement() + ";\n";
					finS = finS + "FinSi" + pilaIF.lastElement() + ": \n";
					pilaIF.remove(pilaIF.size()-1);
					try {
						finS = finS + "goto FinSi"+pilaIF.lastElement() + ";\n";
					}catch(NoSuchElementException ex) {}
					vectorIF.add(finS);	
					i++;
					
					banderaSino = false;
				}else {
					String finS = "Sino"+pilaIF.lastElement()+":\ngoto FinSi"+pilaIF.lastElement() + ";\n";
					finS = finS + "FinSi" + pilaIF.lastElement() + ": \n";
					pilaIF.remove(pilaIF.size()-1);
					try {
						finS = finS + "goto FinSi"+pilaIF.lastElement() + ";\n";
					}catch(NoSuchElementException ex) {}
					vectorIF.add(finS);	
					i++;
				}
				
				
			}else if(codigoEntrada.get(i).equals("sino")) {					// --------------> sino
				//ES UN SINO POR LO TANTO VER MÁS CÓDIGO DENTRO DE EL SINO
				banderaSino = true;
				//i++;
				if(contIF == 1) {
					String sino= "goto FinSi"+pilaIF.lastElement() + ";\nSino" + pilaIF.lastElement() +": ";
					vectorIF.add(sino);
					int puntero = obtenerContenidoIF(codigoEntrada, i);
					i = puntero;
				}else {
					String sino= "Sino" + pilaIF.lastElement() +": ";
					vectorIF.add(sino);
					int puntero = obtenerContenidoIF(codigoEntrada, i);
					i = puntero;
				}
				
				
			}else if(codigoEntrada.get(i).equals(";")) {					// --------------> PUNTO Y COMA ;
				if(banderaImp) {
					codigoGenerado = codigoGenerado + ")"+codigoEntrada.get(i) + "\n";
					banderaImp = false;
					banderaLeer = false;
				}else if(banderaLeer) {
					codigoGenerado = codigoGenerado + ")"+codigoEntrada.get(i) + "\n";
					banderaImp = false;
					banderaLeer = false;
				}else {
					codigoGenerado = codigoGenerado + codigoEntrada.get(i) + "\n";
				}
				i++;
			}else if(codigoEntrada.get(i).equals("imp")) {					// --------------> IMP
				banderaImp=true;
				//CONVERTIR A FPRINT
				for (int j = 0; j < idVal.size(); j++) {
					System.out.println(idVal.get(j).getNombre() + " CON " + codigoEntrada.get(i+1));
					if(idVal.get(j).getNombre().equals(codigoEntrada.get(i+1))) {
						if(idVal.get(j).getTipo().equals("0")) {
							//ES UN ENTERO
							codigoEntrada.set(i, "\nprintf(\" %d \", ");   //printf( "%d,\n", a ;
						}else if(idVal.get(j).getTipo().equals("1")) {
							//ES UN ENTERO
							codigoEntrada.set(i, "\nprintf(\" %f \", ");   //printf( "%d,\n", a ;
						}else if(idVal.get(j).getTipo().equals("2")) {
							//ES UN ENTERO
							codigoEntrada.set(i, "\nprintf(\" %c \", ");   //printf( "%d,\n", a ;
						}
					}
				}
				codigoGenerado = codigoGenerado + "   " + codigoEntrada.get(i);
				i++;
			}else if(codigoEntrada.get(i).equals("leer")) {					// --------------> LEER
				//CONVERTIR A SCANF
				// a = leer ;         --> scanf("%d", &a);
				
				String variableLectora = "";
				variableLectora = codigoEntrada.get(i-2);
				
				banderaLeer=true;
				//CONVERTIR A FPRINT
				for (int j = 0; j < idVal.size(); j++) {
					System.out.println(idVal.get(j).getNombre() + " CON " + codigoEntrada.get(i+1));
					if(idVal.get(j).getNombre().equals(codigoEntrada.get(i-2))) {
						if(idVal.get(j).getTipo().equals("0")) {
							//ES UN ENTERO
							codigoEntrada.set(i, "\nscanf(\"%d\", &" + variableLectora);   
						}else if(idVal.get(j).getTipo().equals("1")) {
							//ES UN ENTERO
							codigoEntrada.set(i, "\nscanf(\"%f\", &" + variableLectora);    
						}else if(idVal.get(j).getTipo().equals("2")) {
							//ES UN ENTERO
							codigoEntrada.set(i, "\nscanf(\"%c\", &" + variableLectora); 
						}
					}
				}
				codigoGenerado = codigoGenerado + "   " + codigoEntrada.get(i);
				i++;
			}else {
				
				try {
					if(codigoEntrada.get(i+2).equals("leer")) {
						i++;i++;
					}else {
						codigoGenerado = codigoGenerado + codigoEntrada.get(i) + " ";
						i++;
					}
				}catch(NoSuchElementException ex) {
					codigoGenerado = codigoGenerado + codigoEntrada.get(i) + " ";
					i++;
				}
				
				
				
				
			}
		}
		
		
		String contenidosIF = "";
		for (int j = 0; j < condicionesIF.size(); j++) {
			//System.out.println(condicionesIF.get(j) + " ");
			contenidosIF = contenidosIF + condicionesIF.get(j);
		}
		for (int j = 0; j < vectorIF.size(); j++) {
			//System.out.println(vectorIF.get(j));
			contenidosIF = contenidosIF + vectorIF.get(j);
		}
		
		
		
		System.out.println("Código generado en C: ");
		System.out.println(codigoGenerado);
		
		System.out.println(contenidosIF);
		String codigoFinal = codigoGenerado + contenidosIF;
		codigoFinal = codigoFinal + "   printf(\"      ..:: Fin de la ejecución ::..\");\n}";
		new resultadoCodigoObj(codigoFinal);
		
	}
	

	public int obtenerContenidoIF(Vector<String> codigoEntrada, int i) {
		i++;
		System.out.println("ANALIZANDO DENTRO DEL IF: " + codigoEntrada.get(i) + " en: " + i);
		while(!codigoEntrada.get(i).equals("finsi") | !codigoEntrada.get(i).equals("si") | !codigoEntrada.get(i).equals("sino")) {
			if(codigoEntrada.get(i).equals("finsi")) break;
			if(codigoEntrada.get(i).equals("si")) break;
			if(codigoEntrada.get(i).equals("sino")) break;
			
			System.out.println(i + " ----> DENTRO WHILE --> " + codigoEntrada.get(i));
			if(!codigoEntrada.get(i).equals("finasigna")) {
				
				if(codigoEntrada.get(i).equals("imp")) {
					banderaImp=true;
					//CONVERTIR A FPRINT
					for (int j = 0; j < idVal.size(); j++) {
						System.out.println(idVal.get(j).getNombre() + " CON " + codigoEntrada.get(i+1));
						if(idVal.get(j).getNombre().equals(codigoEntrada.get(i+1))) {
							if(idVal.get(j).getTipo().equals("0")) {
								//ES UN ENTERO
								codigoEntrada.set(i, "\nprintf(\" %d \", ");   //printf( "%d,\n", a ;
								System.out.println("ENTERO");
							}else if(idVal.get(j).getTipo().equals("1")) {
								//ES UN ENTERO
								codigoEntrada.set(i, "\nprintf(\" %f \", ");   //printf( "%d,\n", a ;
							}else if(idVal.get(j).getTipo().equals("2")) {
								//ES UN ENTERO
								codigoEntrada.set(i, "\nprintf(\" %c \", ");   //printf( "%d,\n", a ;
							}
						}
					}
					
				}else if(codigoEntrada.get(i).equals("leer")) {
					//CONVERTIR A SCANF
					// a = leer ;         --> scanf("%d", &a);
					
					String variableLectora = "";
					variableLectora = codigoEntrada.get(i-2);
					
					banderaLeer=true;
					//CONVERTIR A FPRINT
					for (int j = 0; j < idVal.size(); j++) {
						System.out.println(idVal.get(j).getNombre() + " CON " + codigoEntrada.get(i+1));
						if(idVal.get(j).getNombre().equals(codigoEntrada.get(i-2))) {
							if(idVal.get(j).getTipo().equals("0")) {
								//ES UN ENTERO
								codigoEntrada.set(i, "\nscanf(\"%d\", &" + variableLectora + ")");   
							}else if(idVal.get(j).getTipo().equals("1")) {
								//ES UN ENTERO
								codigoEntrada.set(i, "\nscanf(\"%f\", &" + variableLectora+")");    
							}else if(idVal.get(j).getTipo().equals("2")) {
								//ES UN ENTERO
								codigoEntrada.set(i, "\nscanf(\"%c\", &" + variableLectora+")"); 
							}
						}
					}
					vectorIF.add(codigoEntrada.get(i));
					i++;
				}else if(codigoEntrada.get(i).equals(";")) {
					if(banderaImp) {
						vectorIF.add(")" + codigoEntrada.get(i) + "\n");
						banderaImp = false;
						banderaLeer = false;
					}else if(banderaLeer){
						vectorIF.add(codigoEntrada.get(i) + "\n");
						banderaImp = false;
						banderaLeer = false;
					}else {
						vectorIF.add(codigoEntrada.get(i) + "\n");
					}
					i++;
				}else if(codigoEntrada.get(i).equals("finasigna")) {
					//HOT FIXED, FALTABA EL ELSE ANIDADO AQUI ARRIBITA
					System.out.println(" --------------- NO AGREGARLO ----------------");
				}else {
					
					try {
						if(codigoEntrada.get(i+2).equals("leer")) {
							i++;i++;
						}else {
							vectorIF.add(codigoEntrada.get(i) + " ");
							i++;
						}
					}catch(NoSuchElementException ex) {
						vectorIF.add(codigoEntrada.get(i) + " ");
						i++;
					}
					
						
				}
				
				
				
								
			}else {
				i++;
			}
		}
		
		return i;
	}
	
	public int obtenerEstructuraFOR(Vector<String> codigoEntrada, int i) {
		estructuraFOR= "";
		finalFOR="";
		// para ( entero a = 0 ; a < 10 ; i ++ )
		boolean banderaV1 = false;
		boolean banderaV2 = false;
		String operadorR = "";
		
		while( !codigoEntrada.get(i).equals(")") ) {
			if(codigoEntrada.get(i).equals("=")) {
				//ASIGNAR EL VALOR CONTADOR
				banderaV1=true;
			}else if(banderaV1) {
				//SE AGREGÓ UN =
				
				String condVi="int Vi" + pilaFOR.lastElement() + " = " + codigoEntrada.get(i) + " ; \n";  // int vi1 = 1 ; 
				String a = "int a" + pilaFOR.lastElement() + " = Vi" + pilaFOR.lastElement()  + ";  \n";
					/*
					 *  int Vi1=1;
						int a1= Vi1;
						FOR1:
						Vi1= a1;   
					 */ 
				String c = "FOR" + pilaFOR.lastElement() + ": \n";
				String v = "Vi" + pilaFOR.lastElement() + " = a" + pilaFOR.lastElement() + ";  \n";
				
				String condiciones= condVi + a + c + v;
				System.out.println(" ------------------------------------ ");
				System.out.println(condiciones);
				System.out.println(" ------------------------------------ ");
				condicionesFOR.add(condiciones); 
				estructuraFOR = estructuraFOR + condiciones;
				banderaV1=false;
			}else if(codigoEntrada.get(i).equals("<") | codigoEntrada.get(i).equals("<=") 
					 | codigoEntrada.get(i).equals(">") | codigoEntrada.get(i).equals(">=")
					 | codigoEntrada.get(i).equals("==")) {
				//OPERADOR RELACIONAL ----->  i <= 10 ...
				banderaV2=true;
				operadorR = codigoEntrada.get(i);
			}else if(banderaV2) {
				
				/*
				    Vf1= 10;
					Vi1 = Vi1< Vf1;
					if (! Vi1)
					goto Fin_For1;
					//aqui va el cuerpo
				 */
				
				String vf="int Vf" + pilaFOR.lastElement() + " = " + codigoEntrada.get(i) + " ; \n";
				String cond = "Vi"+pilaFOR.lastElement() + " = Vi" + pilaFOR.lastElement() + operadorR + "Vf" + pilaFOR.lastElement() + "; \n";
				String if1 = "if (!Vi"+pilaFOR.lastElement()+")  \n goto Fin_For" + pilaFOR.lastElement() + ";  \n";
				String junta = vf+ cond + if1;
				condicionesFOR.add(junta);  // int vf1 =  10;
				estructuraFOR = estructuraFOR + junta;
				banderaV2=false;
			}else if( codigoEntrada.get(i).equals("++")) {
				/*
				    Vi1= a1;
					Vf1= 1;
					Vi1= Vi1+Vf1;
					a1= Vi1;
				 */
				String finales = "Vi" + pilaFOR.lastElement() + " = a" + pilaFOR.lastElement() + "; \n" + 
								 "Vf" + pilaFOR.lastElement() + " = 1 ; \n" + 
								 "Vi" + pilaFOR.lastElement() + " = Vi"+pilaFOR.lastElement() + " + Vf" + pilaFOR.lastElement() + ";  \n" + 
								  "a" + pilaFOR.lastElement() + " = Vi"+pilaFOR.lastElement() + ";  \n";
								 
				finalesFOR.add(finales);
				finalFOR = finalFOR + finales;
			}else if( codigoEntrada.get(i).equals("--")) {
				/*
			    Vi1= a1;
				Vf1= 1;
				Vi1= Vi1-Vf1;
				a1= Vi1;
			 */
				String finales = "Vi" + pilaFOR.lastElement() + " = a" + pilaFOR.lastElement() + "; \n" + 
							 "Vf" + pilaFOR.lastElement() + " = 1 ; \n" + 
							 "Vi" + pilaFOR.lastElement() + " = Vi"+pilaFOR.lastElement() + " - Vf" + pilaFOR.lastElement() + ";  \n" + 
							  "a" + pilaFOR.lastElement() + " = Vi"+pilaFOR.lastElement() + ";  \n";
							 
				finalesFOR.add(finales);
				finalFOR = finalFOR + finales;
			}
			
			i++;
		}
		
		
		
		return i;
	}

}
