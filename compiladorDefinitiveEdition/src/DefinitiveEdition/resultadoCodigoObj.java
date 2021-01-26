package DefinitiveEdition;

import javax.swing.JFrame;
import javax.swing.JEditorPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

public class resultadoCodigoObj {

	private JFrame frmCdigoEnC;

	public resultadoCodigoObj( String codigo ) {
		frmCdigoEnC = new JFrame();
		frmCdigoEnC.setResizable(false);
		frmCdigoEnC.setTitle("C\u00F3digo en C");
		frmCdigoEnC.setSize(300,600);
		frmCdigoEnC.setLocation(30,100);
		JEditorPane editorResultado = new JEditorPane();
		
		editorResultado.setText(codigo);
		
		for (int i = 0; i < editorResultado.getText().length(); i++) {
			if(editorResultado.getText().contains("entero")) {
				editorResultado.setCaretColor(Color.red);
			}
		}
		
		editorResultado.setFont(new Font("Tahoma", Font.PLAIN, 17));
		frmCdigoEnC.getContentPane().add(editorResultado, BorderLayout.CENTER);
		frmCdigoEnC.setVisible(true);
		
		frmCdigoEnC.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
