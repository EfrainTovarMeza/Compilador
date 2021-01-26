package DefinitiveEdition;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import org.w3c.dom.Document;

public class ColorPane extends JTextPane {

private static final long serialVersionUID = 1L;

public void appendNaive(Color c, String s) { // naive implementation
    // bad: instiantiates a new AttributeSet object on each call
    SimpleAttributeSet aset = new SimpleAttributeSet();
    StyleConstants.setForeground(aset, c);

    int len = getText().length();
    setCaretPosition(len); // place caret at the end (with no selection)
    setCharacterAttributes(aset, false);
    replaceSelection(s); // there is no selection, so inserts at caret
  }

  public void append(Color c, String s) { // better implementation--uses
                      // StyleContext
    StyleContext sc = StyleContext.getDefaultStyleContext();
    AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
        StyleConstants.Foreground, c);

    int len = getDocument().getLength(); // same value as
                       // getText().length();
    setCaretPosition(len); // place caret at the end (with no selection)
    setCharacterAttributes(aset, false);
    replaceSelection(s); // there is no selection, so inserts at caret
  }

  public static void main(String argv[]) {

    ColorPane pane = new ColorPane();
    
    String codigo = "int a ; float b ; a = 2 ;";
    
    for (int n = 0; n < codigo.length(); n++) {
      if (codigo.charAt(n) == 'i') {
    	if(codigo.charAt(n+1) == 'n') {
    		for (int i = n; i < 3; i++) {
        		pane.append(Color.red, codigo.charAt(i) + "");	
    		}
    		pane.append(Color.black, " ");
            n = n + 3;
    	}else {
    		pane.append(Color.black, codigo.charAt(n) + "");
    	}
    	
      } else if (codigo.charAt(n) == 'f') { //flotante
    	  if(codigo.charAt(n+1) == 'l') {
      		for (int i = n; i < 5; i++) {
          		pane.append(Color.blue, codigo.charAt(i) + "");	
      		} 
      		pane.append(Color.black, " ");
            n = n + 5;	
      	  }else {
      		pane.append(Color.blue, codigo.charAt(n) + "");
      	  }
    	
      } else {
    	  pane.append(Color.black, codigo.charAt(n) + "");
      }
    }
  }
 

}
