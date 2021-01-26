/*
PRUEBAS

------------------

float a ;\nint b ;\nchar c ;\nfloat h , k , n ;\nn = a * b - h + k ;\n

------------------

int a ;\nfloat b , c ;\nc = a * b ;

-----------------

int a , b , c ;\nfloat d , e , f ;\nf = a * ( d - e ) / c * d + ( b - f ) ;

----------------

int a , b , c ;\nfloat d , e , f ;\nd = ( a + e ) * ( d / c ) - b * f ;

----------------

int a , b , c ;\nfloat x , y ;\ny = a + b * c - x ;

----------------

*/

package DefinitiveEdition;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

class ResultadoLEX {
	public ResultadoLEX(String res) {
		JOptionPane.showMessageDialog(null, res);
	}
}

public class interfaz {
	JFrame frameInterfaz;
	JEditorPane editor=new JEditorPane();
	JTextPane edit = new JTextPane();
	int banderaErrorL=0; //FUERA DE SERVICIO
	int inicio=0, longitudC = 0;
	StyleContext textocolor;
	DefaultStyledDocument dc;
	
	String cadenasPruebas[] = { "!START!\r\n"
								+ "    entero a , e ;\r\n"
								+ "    flotante c , z ;\r\n"
								+ " {\r\n"
								+ "     c = a + z ;\r\n"
								+ "     si ( c < 10 ) then\r\n"
								+ "         e = 1 ;\r\n"
								+ "         imp c ;\r\n"
								+ "         finasigna\r\n"
								+ "     sino\r\n"
								+ "         e = 0 ;\r\n"
								+ "         finasigna\r\n"
								+ "     finsi\r\n"
								+ "     finasigna\r\n"
								+ " }\r\n"
								+ " !END!\r\n"
								+ "", 
								"!START!\r\n"
								+ "entero a , b ;\r\n"
								+ "{\r\n"
								+ "    a = 4 ;\r\n"
								+ "    b = 2 ;\r\n"
								+ "    si ( a < b ) then\r\n"
								+ "        imp a ;\r\n"
								+ "        finasigna\r\n"
								+ "    sino\r\n"
								+ "        imp b ;\r\n"
								+ "        finasigna\r\n"
								+ "    finsi\r\n"
								+ "    finasigna\r\n"
								+ "}\r\n"
								+ "!END!\r\n",
								"!START!\r\n"
								+ "     entero a , b ;\r\n"
								+ "{\r\n"
								+ "     a = 4 ;\r\n"
								+ "     b = 2 ;\r\n"
								+ "     si ( a < b ) then\r\n"
								+ "          si ( a < 3 ) then\r\n"
								+ "               imp a ;\r\n"
								+ "               finasigna\r\n"
								+ "          finsi\r\n"
								+ "          finasigna\r\n"
								+ "     sino\r\n"
								+ "          imp b ;\r\n"
								+ "          finasigna\r\n"
								+ "     finsi\r\n"
								+ "     finasigna\r\n"
								+ "}\r\n"
								+ "!END!\r\n"
								+ "",
								"!START!\r\n"
								+ "    entero a , b ;\r\n"
								+ "    flotante c , d ;\r\n"
								+ "    caracter e , f ;\r\n"
								+ " {\r\n"
								+ "     a = 1 ;\r\n"
								+ "     c = 2.3 ;\r\n"
								+ "     e = 'l' ;\r\n"
								+ "     b = 89 ;\r\n"
								+ "     d = 31.2 ;\r\n"
								+ "     f = 'q' ;\r\n"
								+ "     finasigna\r\n"
								+ " }\r\n"
								+ " !END!\r\n"
								+ "",
								"!START!\r\n"
								+ "    entero a , e , s ;\r\n"
								+ "    caracter c ;\r\n"
								+ " {\r\n"
								+ "     a = 4 ;\r\n"
								+ "     e = 5 ;\r\n"
								+ "     s = a + e ;\r\n"
								+ "     c = 'a' ;\r\n"
								+ "     imp a ;\r\n"
								+ "     imp c ;\r\n"
								+ "     finasigna\r\n"
								+ " }\r\n"
								+ " !END!\r\n"
								+ "",
								"!START!\r\n"
								+ "entero a , b ;\r\n"
								+ "{\r\n"
								+ "    a = leer ;\r\n"
								+ "    b = 2 ;\r\n"
								+ "    a = a + 1 ;  \r\n"
								+ "    si ( a < b ) then\r\n"
								+ "        imp a ;\r\n"
								+ "        finasigna\r\n"
								+ "    sino\r\n"
								+ "       imp b ;\r\n"
								+ "       finasigna\r\n"
								+ "    finsi\r\n"
								+ "    finasigna\r\n"
								+ "}\r\n"
								+ "!END!\r\n"
								+ "", 
								"!START!\r\n"
								+ "entero a , b ;\r\n"
								+ "{\r\n"
								+ "    a = 0 ;\r\n"
								+ "    b = 10 ;\r\n"
								+ "    para ( entero i = 0 ; i < b  ; i ++ )\r\n"
								+ "       a = a + 1 ;\r\n"
								+ "       imp a ;\r\n"
								+ "       finasigna\r\n"
								+ "    finpara\r\n"
								+ "    finasigna\r\n"
								+ "}\r\n"
								+ "!END!\r\n"
								+ "", 
								"!START!\r\n"
								+ "entero a , b ;\r\n"
								+ "{\r\n"
								+ "    a = 0 ;\r\n"
								+ "    b = 10 ;\r\n"
								+ "    para ( entero i = 0 ; i < b  ; i ++ )\r\n"
								+ "         a = 0 ;\r\n"
								+ "         para ( entero j = 0 ; j < 4 ; j ++ )\r\n"
								+ "             a = a + 1 ;\r\n"
								+ "             imp a ;\r\n"
								+ "            finasigna\r\n"
								+ "         finpara\r\n"
								+ "         finasigna\r\n"
								+ "    finpara\r\n"
								+ "    finasigna\r\n"
								+ "}\r\n"
								+ "!END!\r\n"
								+ ""}; 
	private Set<Integer> presionado = new HashSet<Integer>(); 
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					interfaz window = new interfaz();
					window.frameInterfaz.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public interfaz() {
		initialize();
	}

	private void initialize() {
	
		frameInterfaz = new JFrame();
		frameInterfaz.setTitle("ANALIZADOR JEMA");
		frameInterfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTabbedPane pestañas=new JTabbedPane();
		
		JPanel opciones=new JPanel();
			JButton Analiza=new JButton("Analizar");
			Analiza.setBackground(Color.green);
			opciones.add(Analiza);
			JButton btnAzar=new JButton("Cadena al azar");
			opciones.add(btnAzar);
			JButton btnBorrar=new JButton("TERMINAR ANÁLISIS");
			JButton btnLimpiar=new JButton("Limpiar texto");
			opciones.add(btnLimpiar);
		
		JPanel panel = new JPanel( new BorderLayout() );
		ScrollPane scroll=new ScrollPane();
		edit.setText(cadenasPruebas[1]);
		
		scroll.add(edit);
		panel.add(scroll, BorderLayout.CENTER);
		linea lineNumber = new linea(edit, 3);
		panel.add(lineNumber, BorderLayout.WEST);
		panel.add(opciones, BorderLayout.SOUTH);
		JPanel panelResultados = new JPanel( new BorderLayout() );
		JLabel lblNoAnalisis=new JLabel("¡No se ha hecho un análisis aún!");
		panelResultados.add(lblNoAnalisis, BorderLayout.CENTER);
		lblNoAnalisis.setFont(new Font("Arial", 0, 28));
		lblNoAnalisis.setHorizontalAlignment(SwingConstants.CENTER);
		JTable resultados=new JTable();
		pestañas.add("Editor", panel);
		pestañas.add("Resultados obtenidos", panelResultados);
		btnBorrar.setBackground(Color.red);
		btnBorrar.setForeground(Color.white);
		
		Analiza.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panelResultados.removeAll(); //QUITA LA ETIQUETA LABEL
				if(edit.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "PARA ANALIZAR PRIMERO ESCRIBE ALGO");
				}else {
					JScrollPane scrollEntrada=new JScrollPane();
					//erroresLexicos();
					String codigo=edit.getText();
					String NuevoC=codigo.replaceAll("\\r\\n|\\r|\\n", " ");
					NuevoC = NuevoC + "FinProg";  // --> $
					sintactico analisis=new sintactico(NuevoC);
					//tablaEntrada.setModel(analisis.modeloEntrada);
					resultados.setModel(analisis.modeloResultado);
					scrollEntrada.setViewportView(resultados);
					panelResultados.add(scrollEntrada, BorderLayout.CENTER);
					panelResultados.add(btnBorrar, BorderLayout.SOUTH);
					pestañas.setSelectedIndex(1);
					pestañas.setEnabled(false);
				}
			}
		});
		
		
		btnBorrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				pestañas.setSelectedIndex(0);
				panelResultados.removeAll();
				panelResultados.add(lblNoAnalisis, BorderLayout.CENTER); //VUELVE A AGREGAR EL JLABEL
				pestañas.setEnabled(true);
			}
		});
		
		btnAzar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				edit.setText("");
				int nPrueba=(int) (Math.random()*8);
				nPrueba = (int) (Math.random()*8);
				String nuevaCadena=cadenasPruebas[nPrueba];
				edit.setText(nuevaCadena);
			}
		});
		
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				edit.setText("");
			}
		});
		
		edit.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent arg0) {
				
				
			}
			
			public void keyPressed(KeyEvent e) {
				String codigo=edit.getText();
				String NuevoC=codigo.replaceAll("\\r\\n|\\r|\\n", " ");
				Vector <String> tokens = new Vector<String>();
				boolean esPara=false;
				boolean esSi = false;
			    presionado.add(e.getKeyCode());
			    if (presionado.size() > 1) {
			        if(presionado.contains(38) && presionado.contains(27)) {
			        	StyledDocument doc = edit.getStyledDocument();
			        	edit.setText("");
			        	edit.setBackground(Color.decode("#2F2F2F"));
			        	edit.setCaretColor(Color.white);
			        	edit.setFont(edit.getFont().deriveFont(Font.BOLD, edit.getFont().getSize()));
			        	javax.swing.text.Style style = edit.addStyle("!START!", null);
			        	StringTokenizer separador=new StringTokenizer(NuevoC, " ");
			        	while(separador.hasMoreTokens()) {
			    			tokens.add(separador.nextToken());
			    		}
			        	
			        	for (int i = 0; i < tokens.size(); i++) {
							if(tokens.get(i).equals("!START!") | tokens.get(i).equals("!END!")) {
								StyleConstants.setForeground(style, Color.decode("#FF5E00")); 
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" \n", style);
								} catch (BadLocationException e1) {
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("entero")) {
								StyleConstants.setForeground(style, Color.decode("#FFFFC7"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("flotante")) {
								StyleConstants.setForeground(style, Color.decode("#FCAA67"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("caracter")) {
								StyleConstants.setForeground(style, Color.decode("#7EA172"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals(";")) {
								if(esPara) {
									StyleConstants.setForeground(style, Color.decode("#A7EC21"));
						            try {
										doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
									} catch (BadLocationException e1) {
										
										e1.printStackTrace();
									}
								}else {
									StyleConstants.setForeground(style, Color.decode("#A7EC21"));
						            try {
										doc.insertString(doc.getLength(), tokens.get(i)+" \n", style);
									} catch (BadLocationException e1) {
										
										e1.printStackTrace();
									}
								}
								
							}else if(tokens.get(i).equals("{") | tokens.get(i).equals("}")) {
								StyleConstants.setForeground(style, Color.green);
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" \n", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("(")) {
								StyleConstants.setForeground(style, Color.red);
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals(")")) {
								if(esSi) {
									StyleConstants.setForeground(style, Color.red);
						            try {
										doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
									} catch (BadLocationException e1) {
										
										e1.printStackTrace();
									}
								}else {
									StyleConstants.setForeground(style, Color.red);
						            try {
										doc.insertString(doc.getLength(), tokens.get(i)+" \n", style);
									} catch (BadLocationException e1) {
										
										e1.printStackTrace();
									}
								}
								esPara = false;
								esSi=false;
								
							}else if(tokens.get(i).equals("then") | tokens.get(i).equals("sino") | tokens.get(i).equals("finsi")) {
								StyleConstants.setForeground(style, Color.MAGENTA);
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" \n", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("si")){
								esSi=true;
								StyleConstants.setForeground(style, Color.MAGENTA);
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("finasigna")){
								StyleConstants.setForeground(style, Color.YELLOW);
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" \n", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("para")){
								esPara=true;
								StyleConstants.setForeground(style, Color.decode("#C5F4E0"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).contains("'")){
								StyleConstants.setForeground(style, Color.decode("#42F2F7"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals(",")){
								StyleConstants.setForeground(style, Color.decode("#DCEDFF"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("leer")){
								StyleConstants.setForeground(style, Color.decode("#01BAEF"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("imp")){
								StyleConstants.setForeground(style, Color.decode("#20BF55"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("<") | tokens.get(i).equals(">") | tokens.get(i).equals("<=") 
									| tokens.get(i).equals(">=") | tokens.get(i).equals("==") | tokens.get(i).equals("++") 
									| tokens.get(i).equals("--")| tokens.get(i).equals("-") | tokens.get(i).equals("+") 
									| tokens.get(i).equals("/") | tokens.get(i).equals("*")){
								StyleConstants.setForeground(style, Color.decode("#B3DEC1"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else if(tokens.get(i).equals("finpara")){
								esPara=false;
								StyleConstants.setForeground(style, Color.decode("#C5F4E0"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" \n", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
							}else {
								StyleConstants.setForeground(style, Color.decode("#FFFFFF"));
					            try {
									doc.insertString(doc.getLength(), tokens.get(i)+" ", style);
								} catch (BadLocationException e1) {
									
									e1.printStackTrace();
								}
								
								
							}
						}
			        	
			           

			            
			        }
			        	
			      }
			}
			
		  @Override
		   public  void keyReleased(KeyEvent e) {
		     presionado.remove(e.getKeyCode());
		   }

		});

		
		frameInterfaz.getContentPane().add(pestañas, BorderLayout.CENTER);
		frameInterfaz.setSize(800,600);
		frameInterfaz.setLocation(330,100);
	}
	

	
}


