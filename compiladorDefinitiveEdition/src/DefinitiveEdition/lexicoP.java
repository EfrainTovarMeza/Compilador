package DefinitiveEdition;

import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class lexicoP {
	
	StringTokenizer separador;
	Vector<String> tokens;
	Vector<String> tokensRes;
	Vector<String> lexemas=new Vector<String>();
	String reservadas = "(!START!)|(id)|(entero)|(flotante)|(caracter)|(leer)|(imp)|(si)|(then)|(finsi)|(sino)(para)|(finpara)|(finasigna)|(!END!)|(P)|(Tipo)|(V)|(A)|(E)|(T)|(F)|(INIC)|(SENT)|(FOR)|(IF)|(ELSE)|(CONDICION1)|(CONDICION2)|(SIGNOS)|(SM)|(FinProg)";
	Pattern p;
    Matcher m;
    int contadorVariables = 0;
    int contadorNumerosFlotantes = 0;
    int contadorNumerosEnteros = 0;
    int contadorCaracter = 0;
    int contadorTexto = 0;
    int errorL=0;
    String tipos[][];
    Vector<String> pilaTipos=new Vector<String>();
    Vector<String> varInt=new Vector<String>();
    Vector<String> varFloat=new Vector<String>();
    Vector<String> varChar=new Vector<String>();
    int valInt=0, valFloat=1, valChar=2;
    int tipoDatos=0; //0 -> No TD, 1 -> Int, 2 -> Float, 3 -> Char
    
    //idsVal
    Vector<idsval> idVal=new Vector<idsval>();
    boolean banderaAsignacion = false, banderaIgual=false;
    String idTemp="";
    //PARA LA SEMANTICA
    Vector<asignaciones> vAsignacion=new Vector<asignaciones>();
    
    
    public lexicoP(String codigo) {
    	System.out.println(" ...::: INICIA ANÁLISIS LEXICO :::...");
		separador=new StringTokenizer(codigo, " ");
		tokens=new Vector<String>();
		tokensRes=new Vector<String>();
		while(separador.hasMoreTokens()) {
			tokens.add(separador.nextToken());
		}
		tipos=new String[tokens.size()][2];
		System.out.println("Numero de tokens: " + tokens.size());
	    p = Pattern.compile(reservadas);
		for (int i = 0; i < tokens.size(); i++) {
			/*VA A ANALIZAR TODOS LOS TOKENS*/
			tokensRes.add(analisis(tokens.get(i), i));
		} 
	    
		System.out.println("TOKEN --- LEXEMA");
		for (int i = 0; i < lexemas.size(); i++) {
			System.out.println(tokensRes.get(i) + " --- " + lexemas.get(i));
			if(lexemas.get(i).equals("error"))
				errorL = 1;
		}
		System.out.println("VARIABLES INT REGISTRADAS");
		for (int i = 0; i < varInt.size(); i++) {
			System.out.println(varInt.get(i));
		}
		System.out.println("VARIABLES FLOAT REGISTRADAS");
		for (int i = 0; i < varFloat.size(); i++) {
			System.out.println(varFloat.get(i));
		}
		
		System.out.println("VARIABLES CHAR REGISTRADAS");
		for (int i = 0; i < varChar.size(); i++) {
			System.out.println(varChar.get(i));
		}
		
		System.out.println("   -------  ");
		for (int i = 0; i < idVal.size(); i++) {
			System.out.println("........ - ITEM - .........");
			System.out.println("Token: " + idVal.get(i).getToken());
			System.out.println("Tipo: " + idVal.get(i).getTipo());
			System.out.println("Nombre: " + idVal.get(i).getNombre());
			System.out.println("Valor: " + idVal.get(i).getValor());
			System.out.println("idVal: " + idVal.get(i).getIdval());
			
			System.out.println("........ - FIN ITEM - .........");
		}
		System.out.println("   -------  ");
		
		//IDENTIFICACION DE LOS TIPOS DE DATOS A LAS VARIABLES ASIGNADAS POR LOS TOKENS
		System.out.println("TIPOS DE DATOS");
		for (int i = 0; i < tokens.size(); i++) {
			System.out.println(tipos[i][0] + " - " + tipos[i][1]);
		}
		
		System.out.println("");
		System.out.println("       ...::: TERMINA ANÁLISIS LEXICO :::...");
    }
    
    public String analisis(String token, int i) {
		if(token.equals(";")) {
			lexemas.add("Terminador de sentencia");
			pilaTipos.add("No es declaracion");
			tipoDatos = 0;
			tipos[i][0]=";";
			tipos[i][1]="NA";
			banderaAsignacion = false;
			banderaIgual=false;
			return token;
		}else if(token.equals("+") | token.equals("-") | token.equals("*") | token.equals("/")
				| token.equals("++") | token.equals("--") | token.equals("<") | token.equals(">") 
				| token.equals("<=") | token.equals(">=") | token.equals("==") | token.equals(".")) {
			lexemas.add("OPERADOR ARITMETICO");
			tipoDatos = 0;
			pilaTipos.add("No es declaracion");
			tipos[i][0]=token.toString();
			tipos[i][1]="NA";
			return token;
		}else if(token.equals(",")) {
			lexemas.add("SEPARADOR (COMA)");
			pilaTipos.add("No es declaracion");
			tipos[i][0]=",";
			tipos[i][1]="NA";
			return token;
		}else if(token.equals("=")) {
			banderaIgual=true;
			
			for (int j = 0; j < idVal.size(); j++) {
  				if(idVal.get(j).nombre.equals(idTemp)) {
  					asignaciones asignacion = new asignaciones(idTemp, idVal.get(j).getTipo());
  					vAsignacion.add(asignacion);
  				}
  			  }
			System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{  ASIGNACION  }}}}}}}}}}}}}}}}}}}}}}}}}}}");
			System.out.println(vAsignacion.lastElement().nombre);
			System.out.println(vAsignacion.lastElement().tipo);
			System.out.println("{{{{{{{{{{{{{{{{{{{{{{{{{{{  ASIGNACION  }}}}}}}}}}}}}}}}}}}}}}}}}}}");
			return token;
		}
		
		m = p.matcher(token);
		
        if (m.find()) {
            {
            	if(token.equals("entero")) {
        			tipos[i][0]="int";
    				tipos[i][1]="NA";
            		tipoDatos = 1; //ENTERO
            		banderaAsignacion = true;
            	}
            	else if(token.equals("flotante")) {
            		tipoDatos = 2;
        			tipos[i][0]="float";
        			tipos[i][1]="NA";
        			banderaAsignacion = true;
            	}else if(token.equals("caracter")) {
            		tipoDatos = 3;
        			tipos[i][0]="char";
        			tipos[i][1]="NA";
        			banderaAsignacion = true;
            	}else {
            		tipoDatos = 0;
            		tipos[i][0]=token.toString();
        			tipos[i][1]="NA";
        			//return "error";
            	}
            		
            	pilaTipos.add("No es declaracion");
            	lexemas.add("Palabra reservada");
            	return token;
            }
        }else {

            String eRegVariables = "([[A-Z]*[a-z]*]+[0-9]*)";
            Pattern pVariables;
            pVariables = Pattern.compile(eRegVariables);
            Matcher mVariables;
            mVariables = pVariables.matcher(token);
            if (mVariables.find()) {
            	
            	if(token.contains("'")) { // a = 'a' :
            		//DE AFUERZAS ES UN CARACTER
            		lexemas.add("caracter" + contadorCaracter);
					contadorCaracter++;
					tipos[i][0]="id";
					tipos[i][1]="2";
					pilaTipos.add("caracter");
					 
            		  if(banderaIgual) {
                    	  //ES UNA ASIGNACIÓN
						  //POR LO TANTO SOLO AFECTA EL VALOR QUE ESTÁ YA REGISTRADO SIN AGREGAR MÁS COSAS.
                    	  for (int j = 0; j < idVal.size(); j++) {
              				if(idVal.get(j).nombre.equals(idTemp)) {
              					idVal.get(j).setValor(token);
              				}
              			  }
                      }else {
						 
                    	idsval componente= new idsval("id", "0", "nulo", token, "val"+contadorCaracter);
						idVal.add(componente);
						System.out.println();
						System.out.println(" ******** CAR ********** ");
						System.out.println(idVal.lastElement().nombre + " - " + idVal.lastElement().idval + " - " + 
								idVal.lastElement().valor);
						System.out.println(" ******** FIN CAR ********** ");
						System.out.println();
                      }
            		
            		 return "id";
            	}else if( token.equals("para") ) {
            		return token;
            	}
            	
            	contadorVariables++;
            	lexemas.add("Identificador " + contadorVariables);
            	
                if(tipoDatos == 1) {
                	pilaTipos.add("int");
                	varInt.add(token);
                	tipos[i][0]="id";
        			tipos[i][1]="int";
        			if(banderaAsignacion) {
        				idTemp = token;
        				idsval componente= new idsval("id", "0", token, "nulo", "id"+contadorNumerosEnteros);
            			idVal.add(componente);	
            			contadorNumerosEnteros++;
        			}
        			
                }else if(tipoDatos == 2) {
                	pilaTipos.add("float");
                	varFloat.add(token);
                	tipos[i][0]="id";
        			tipos[i][1]="float";
        			if(banderaAsignacion) {
        				idTemp = token;
        				idsval componente= new idsval("id", "1", token, "nulo", "id"+contadorNumerosFlotantes);
            			idVal.add(componente);	
        			}
        			contadorNumerosFlotantes++;
        			
                }else if(tipoDatos == 3) {
                	pilaTipos.add("char");
                	varChar.add(token);
                	tipos[i][0]="id";
        			tipos[i][1]="char";
        			if(banderaAsignacion) {
        				idTemp = token;
        				idsval componente= new idsval("id", "2", token, "nulo", "id"+contadorCaracter);
            			idVal.add(componente);	
        			}
        			contadorCaracter++;
                }else {
                	//COMPARAR SI ES QUE YA EXISTE O ESTÁ DECLARO CON ALGÚN TIPO, SI ES ASÍ ASIGNARLE ESE TIPO DE DATO
                	//BUSCA SI ES ENTERO
                	int bandera=0;

                	for (int j = 0; j < varInt.size(); j++) {
						if(varInt.get(j).equals(token)) {
							pilaTipos.add("entero");
		                	tipos[i][0]="id";
		        			tipos[i][1]=valInt+"";
		        			bandera=1;
		        			
		        			
		        			
		        			//b = a ;
		        			int posAnterior = 0;
		        			if(banderaIgual) {
		        				
		        				System.out.println(" ************************************** ");
			        			System.out.println(" ************************************** ");
			        			System.out.println("          Valor anterior: " + idTemp);
			        			System.out.println(" ************************************** ");
			        			System.out.println(" ************************************** ");
		        				
		        				for (int j2 = 0; j2 < idVal.size(); j2++) {
		        					if(idVal.get(j2).nombre.equals(idTemp)) {
		        						posAnterior=j2;
                    				}
								}
		        				
		        				idTemp = token;
		        				
		        				for (int j2 = 0; j2 < idVal.size(); j2++) {
		        					if(idVal.get(j2).nombre.equals(idTemp)) {
                    					idVal.get(posAnterior).setValor(idVal.get(j2).getValor());
                    				}
								}
		        				

			        			System.out.println(" ************************************** ");
			        			System.out.println(" ************************************** ");
			        			System.out.println("          Valor actual: " + idTemp);
			        			System.out.println(" ************************************** ");
			        			System.out.println(" ************************************** ");
		        				
		        			}else {
		        				idTemp = token;
		        			}
		        			
		        			
		        			
		        			
						}
					}
                	if(bandera != 1) {
                		for (int j = 0; j < varFloat.size(); j++) {
    						if(varFloat.get(j).equals(token)) {
    							pilaTipos.add("entero");
    		                	tipos[i][0]="id";
    		        			tipos[i][1]=valFloat+"";
    		        			bandera=1;
    		        			idTemp = token;
    						}
    					}
                	}
                	
                	if(bandera != 1) {
                		for (int j = 0; j < varChar.size(); j++) {
    						if(varChar.get(j).equals(token)) {
    							pilaTipos.add("caracter");
    		                	tipos[i][0]="id";
    		        			tipos[i][1]=valChar+"";
    		        			bandera=1;
    		        			idTemp = token;
    						}
    					}
                	}
                	
                	
                }
                return "id";
            } else {
                    	//VERIFICA SI PERTENECE A UN NUMERO
                        String eRegnumero = "[0-9]+";
                        Pattern pnumero;
                        pnumero = Pattern.compile(eRegnumero);
                        Matcher mnumero;
                        mnumero = pnumero.matcher(token);

                        if (mnumero.find()) {
                            String eRegnumeroF = "([0-9]+[.][0-9]+)";
                            Pattern pnumeroF;
                            pnumeroF = Pattern.compile(eRegnumeroF);
                            Matcher mnumeroF;
                            mnumeroF = pnumeroF.matcher(token);
                            if (mnumeroF.find()) {
                                lexemas.add("NUMFLOAT" + contadorNumerosFlotantes);
                                //contadorNumerosFlotantes++;
                                pilaTipos.add("flotante");
                                tipos[i][0]="id";
                    			tipos[i][1]="1";
                    			 
                    			 
                    			System.out.println();
                    			System.out.println();
                    			System.out.println();
                    			System.out.println(" +++++++++++++++++++");
                    			System.out.println(idTemp);
                    			System.out.println(" +++++++++++++++++++");
                    			System.out.println();
                    			System.out.println();
                    			System.out.println();
                    			  if(banderaIgual) {
                                	  //ES UNA ASIGNACIÓN
                    				  for (int j = 0; j < idVal.size(); j++) {
                            				if(idVal.get(j).nombre.equals(idTemp)) {
                            					idVal.get(j).setValor(token);
                            				}
                            			  }
                                  }else {
                                	  idsval componente= new idsval("id", "1", "nulo", token, "val"+contadorNumerosFlotantes);
                          			idVal.add(componente);
                          			System.out.println();
      								System.out.println(" ******** FLOAT ********** ");
      								System.out.println(idVal.lastElement().nombre + " - " + idVal.lastElement().idval + " - " + 
      										idVal.lastElement().valor);
      								System.out.println(" ******** FIN FLO ********** ");
      								System.out.println();  
                                  }
                    			
                    			
                                return "id";
                            } else {
                            	
                            	// VALIDAR SI ES UN CARACTER EN BASE A LA EXPRESIÓN REGULAR
    	                		// SE INTERPRETA A UN CARACTER SI TRAE EL VALOR: 'a' POR EJEMPLO                        	
                            	
    	                    	String eRegChar = "(([%].[%]))";
                        	    Pattern pChar;
                        	    pChar = Pattern.compile(eRegChar);
                                Matcher mChar;
                                mChar = pChar.matcher(token);
                                
                                if(mChar.find()) {
                                	//ES UN CARACTER
                                	  lexemas.add("caracter" + contadorCaracter);
                                	  //contadorCaracter++;
                                      tipos[i][0]="id";
                                      tipos[i][1]="2";
                                      pilaTipos.add("caracter");
                                      
                                      if(banderaIgual) {
                                    	  //ES UNA ASIGNACIÓN
                                    	  for (int j = 0; j < idVal.size(); j++) {
                                				if(idVal.get(j).nombre.equals(idTemp)) {
                                					idVal.get(j).setValor(token);
                                				}
                                			  }
                                      }else{
                                    	idsval componente= new idsval("id", "2", "nulo", token, "val"+contadorCaracter);
      									idVal.add(componente);
      									System.out.println(" ******** CAR ********** ");
      									System.out.println(idVal.lastElement().nombre + " - " + idVal.lastElement().idval + " - " + 
      											idVal.lastElement().valor);
      									System.out.println(" ******** FIN CAR ********** "); 
                                            
                                      }
                                      
									 
                                    	  
                                      
                                      
                                      return "id";
                                }else {
									lexemas.add("NUMENTERO" + contadorNumerosEnteros);
									
									tipos[i][0]="id";
									tipos[i][1]="0";
									pilaTipos.add("entero");
									
									System.out.println();
	                    			System.out.println();
	                    			System.out.println();
	                    			System.out.println(" +++++++++++++++++++");
	                    			System.out.println(idTemp);
	                    			System.out.println(" +++++++++++++++++++");
	                    			System.out.println();
	                    			System.out.println();
	                    			System.out.println();
									
									  if(banderaIgual) {
                                    	  //ES UNA ASIGNACIÓN
										  //POR LO TANTO SOLO AFECTA EL VALOR QUE ESTÁ YA REGISTRADO SIN AGREGAR MÁS COSAS.
                                    	  for (int j = 0; j < idVal.size(); j++) {
                              				if(idVal.get(j).nombre.equals(idTemp)) {
                              					idVal.get(j).setValor(token);
                              				}
                              			  }
                                      }else {
                                    	idsval componente= new idsval("id", "0", "nulo", token, "val"+contadorNumerosEnteros);
										idVal.add(componente);
										System.out.println();
										System.out.println(" ******** ENTERO ********** ");
										System.out.println(idVal.lastElement().nombre + " - " + idVal.lastElement().idval + " - " + 
												idVal.lastElement().valor);
										System.out.println(" ******** FIN EN ********** ");
										System.out.println();
                                      }
									
									
									return "id";
                                }
                       	
                                
                            }
                            
                         
                            
                        } else {
	                    	
            		
                    	 String operadorAgrupacion = "(\\{)|(\\})|(\\()|(\\))|(\\[)|(\\])";
                         Pattern poperadoresAgrupacion;
                         poperadoresAgrupacion = Pattern.compile(operadorAgrupacion);
                         Matcher moperadorAgrupacion;
                         moperadorAgrupacion = poperadoresAgrupacion.matcher(token);
                         if (moperadorAgrupacion.find()) {
                         	lexemas.add("OPERADOR DE AGRUPACION");
                         	pilaTipos.add("NA");
                         	tipos[i][0]=token.toString();
                 			tipos[i][1]="NA";
                         	return token;
                         }
                            
                            
                           
                         }
                         }
                	  }
		
        lexemas.add("ERROR LEXICO");
		return "error";
    }
}