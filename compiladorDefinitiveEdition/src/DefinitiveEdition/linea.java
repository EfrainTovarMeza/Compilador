package DefinitiveEdition;

import java.awt.*;
import java.beans.*;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class linea extends JPanel
    implements CaretListener, DocumentListener, PropertyChangeListener {

    public final static float LEFT = 0.0f;
    public final static float CENTER = 0.5f;
    public final static float RIGHT = 1.0f;
    private final static Border OUTER = new MatteBorder(0, 0, 0, 2, Color.GREEN);
    private final static int HEIGHT = Integer.MAX_VALUE - 1000000;

    private JTextComponent component;

    private boolean updateFont;
    private int borderGap;
    private Color currentLineForeground;
    private float digitAlignment;
    private int minimumDisplayDigits;

    private int lastDigits;
    private int lastHeight;
    private int lastLine;

    private HashMap<String, FontMetrics> fonts;

    /**
     * Crea un componenete de número de línea para el componenete de texto. Este
     * ancho mínimo de pantalla se basará en 3 dígitos.
     *
     * @param component lo relacionado al componenete de texto
     */
    public linea(JTextComponent component) {
        this(component, 3);
    }

    /**
     * Crea un componenete de número de línea para el componenete de texto.
     *
     * @param component lo relacionado al componenete de texto
     * @param minimumDisplayDigits el número de dígitos utilizados para calcular
     * el ancho mínimo del componente
     */
    public linea(JTextComponent component, int minimumDisplayDigits) {
        this.component = component;

        setFont(component.getFont());

        setBorderGap(5);
        setCurrentLineForeground(Color.RED);
        setDigitAlignment(RIGHT);
        setMinimumDisplayDigits(minimumDisplayDigits);

        component.getDocument().addDocumentListener(this);
        component.addCaretListener(this);
        component.addPropertyChangeListener("font", this);
    }

    /**
     * Obtiene la actualización de la propiedad font (fuente de letra)
     *
     * @return la actualización de la propiedad font (fuente de letra)
     */
    public boolean getUpdateFont() {
        return updateFont;
    }

    /**
     * Establece la actualización de la propiedad font. Indica si esta fuente se
     * debe actualizar automáticamente cuando se cambia la fuente del componente
     * de texto relacionado.
     *
     * @param updateFont cuando realiza la actualización de Font y vuelva a
     * pintar los números de línea, de lo contrario simplemente repinta los
     * números de línea.
     */
    public void setUpdateFont(boolean updateFont) {
        this.updateFont = updateFont;
    }

    /**
     * Obtiene la brecha de borde
     *
     * @return la brecha de borde en píxeles
     */
    public int getBorderGap() {
        return borderGap;
    }

    /**
     * La separación del borde se utiliza para calcular las inserciones
     * izquierda y derecha del borde. El valor predeterminado es 5.
     *
     * @param borderGap la brecha en píxeles
     */
    public void setBorderGap(int borderGap) {
        this.borderGap = borderGap;
        Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
        setBorder(new CompoundBorder(OUTER, inner));
        lastDigits = 0;
        setPreferredWidth();
    }

    /**
     * Obtiene la representación del Color de la línea actual
     *
     * @return el Color usado para representar el número de línea actual
     */
    public Color getCurrentLineForeground() {
        return currentLineForeground == null ? getForeground() : currentLineForeground;
    }

    /**
     * El color utilizado para representar los dígitos de la línea actual. Por
     * defecto es Color.RED
     *
     * @param currentLineForeground el Color usado para representar la línea
     * actual
     */
    public void setCurrentLineForeground(Color currentLineForeground) {
        this.currentLineForeground = currentLineForeground;
    }

    /**
     * Obtiene la alineación de dígitos
     *
     * @return La alineación de los dígitos pintados
     */
    public float getDigitAlignment() {
        return digitAlignment;
    }

    /**
     * Especifica la alineación horizontal de los dígitos dentro del componente.
     * Los valores comunes serían:
     * <ul>
     * <li>TextLineNumber.LEFT
     * <li>TextLineNumber.CENTER
     * <li>TextLineNumber.RIGHT (por defecto)
     * </ul>
     */
    public void setDigitAlignment(float digitAlignment) {
        this.digitAlignment
                = digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
    }

    /**
     * Obtiene los dígitos mínimos de visualización
     *
     * @return los dígitos mínimos de visualización
     */
    public int getMinimumDisplayDigits() {
        return minimumDisplayDigits;
    }

    /**
     * Especifica el número mínimo de dígitos usados para calcular el ancho
     * preferido del componente. El valor predeterminado es 3.
     *
     * @param minimumDisplayDigits el número de dígitos utilizados en el cálculo
     * de ancho preferido
     */
    public void setMinimumDisplayDigits(int minimumDisplayDigits) {
        this.minimumDisplayDigits = minimumDisplayDigits;
        setPreferredWidth();
    }

    /**
     * Calcula el ancho necesario para mostrar el número de línea máximo
     */
    private void setPreferredWidth() {
        Element root = component.getDocument().getDefaultRootElement();
        int lines = root.getElementCount();
        int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

        // Actualiza los tamaños cuando el número de dígitos en el número de línea cambia
        if (lastDigits != digits) {
            lastDigits = digits;
            FontMetrics fontMetrics = getFontMetrics(getFont());
            int width = fontMetrics.charWidth('0') * digits;
            Insets insets = getInsets();
            int preferredWidth = insets.left + insets.right + width;

            Dimension d = getPreferredSize();
            d.setSize(preferredWidth, HEIGHT);
            setPreferredSize(d);
            setSize(d);
        }
    }

    /**
     * Dibujar los números de línea
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Determina el ancho del espacio disponible para dibujar el número de línea
        FontMetrics fontMetrics = component.getFontMetrics(component.getFont());
        Insets insets = getInsets();
        int availableWidth = getSize().width - insets.left - insets.right;

        // Determina las filas a dibujar dentro de los límites recortados.
        Rectangle clip = g.getClipBounds();
        int rowStartOffset = component.viewToModel(new Point(0, clip.y));
        int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));

        while (rowStartOffset <= endOffset) {
            try {
                if (isCurrentLine(rowStartOffset)) {
                    g.setColor(getCurrentLineForeground());
                } else {
                    g.setColor(getForeground());
                }

                // Obtiene el número de línea como una cadena y luego determina
                // los desplazamientos "X" y "Y" para dibujar la cadena.
                String lineNumber = getTextLineNumber(rowStartOffset);
                int stringWidth = fontMetrics.stringWidth(lineNumber);
                int x = getOffsetX(availableWidth, stringWidth) + insets.left;
                int y = getOffsetY(rowStartOffset, fontMetrics);
                g.drawString(lineNumber, x, y);

                // Pasa a la siguiente fila
                rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
            } catch (Exception e) {
                break;
            }
        }
    }

    /**
     * Necesitamos saber si el cursor está actualmente en la línea que estamos a
     * punto de pintar para que el número de línea pueda resaltarse.
     */
    private boolean isCurrentLine(int rowStartOffset) {
        int caretPosition = component.getCaretPosition();
        Element root = component.getDocument().getDefaultRootElement();

        if (root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Obtiene el número de línea a dibujar. Se devolverá una cadena vacía
     * cuando una línea de texto se haya envuelto.
     */
    protected String getTextLineNumber(int rowStartOffset) {
        Element root = component.getDocument().getDefaultRootElement();
        int index = root.getElementIndex(rowStartOffset);
        Element line = root.getElement(index);

        if (line.getStartOffset() == rowStartOffset) {
            return String.valueOf(index + 1);
        } else {
            return "";
        }
    }

    /**
     * Determina el desplazamiento en X para alinear correctamente el número de
     * línea cuando se dibuja
     */
    private int getOffsetX(int availableWidth, int stringWidth) {
        return (int) ((availableWidth - stringWidth) * digitAlignment);
    }

    /**
     * Determina el desplazamiento en Y de la fila actual
     */
    private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics)
            throws BadLocationException {

        // Obtiene el rectángulo delimitador de la fila
        Rectangle r = component.modelToView(rowStartOffset);
        int lineHeight = fontMetrics.getHeight();
        int y = r.y + r.height;
        int descent = 0;

        // El texto debe colocarse encima de la parte inferior del rectángulo
        // delimitador en función del descenso de las fuentes contenidas en la fila.
        if (r.height == lineHeight) { // Se utiliza la fuente predeterminada
            descent = fontMetrics.getDescent();
        } else { // Necesitamos comprobar todos los atributos para los cambios de fuente
            if (fonts == null) {
                fonts = new HashMap<String, FontMetrics>();
            }

            Element root = component.getDocument().getDefaultRootElement();
            int index = root.getElementIndex(rowStartOffset);
            Element line = root.getElement(index);

            for (int i = 0; i < line.getElementCount(); i++) {
                Element child = line.getElement(i);
                AttributeSet as = child.getAttributes();
                String fontFamily = (String) as.getAttribute(StyleConstants.FontFamily);
                Integer fontSize = (Integer) as.getAttribute(StyleConstants.FontSize);
                String key = fontFamily + fontSize;

                FontMetrics fm = fonts.get(key);

                if (fm == null) {
                    Font font = new Font(fontFamily, Font.PLAIN, fontSize);
                    fm = component.getFontMetrics(font);
                    fonts.put(key, fm);
                }

                descent = Math.max(descent, fm.getDescent());
            }
        }
        return y - descent;
    }

//  Implementa la interfaz CaretListener
    @Override
    public void caretUpdate(CaretEvent e) {
        // Obtiene la línea en la que se encuentra el cursor
        int caretPosition = component.getCaretPosition();
        Element root = component.getDocument().getDefaultRootElement();
        int currentLine = root.getElementIndex(caretPosition);

        // Necesitamos repintar para que el número de línea correcto se pueda resaltar
        if (lastLine != currentLine) {
            repaint();
            lastLine = currentLine;
        }
    }

//  Implementa la interfaz DocumentListener
    @Override
    public void changedUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        documentChanged();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        documentChanged();
    }

    /**
     * Un cambio de documento puede afectar el número de líneas de texto
     * mostradas. Por lo tanto, el número de líneas también cambiará.
     */
    private void documentChanged() {
        // La vista del componente no se ha actualizado en el momento en que se
        // desencadena el objeto DocumentEvent
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    int endPos = component.getDocument().getLength();
                    Rectangle rect = component.modelToView(endPos);

                    if (rect != null && rect.y != lastHeight) {
                        setPreferredWidth();
                        repaint();
                        lastHeight = rect.y;
                    }
                } catch (BadLocationException ex) {
                    /* nada que hacer */ }
            }
        });
    }

//  Implementa la interzar PropertyChangeListener
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof Font) {
            if (updateFont) {
                Font newFont = (Font) evt.getNewValue();
                setFont(newFont);
                lastDigits = 0;
                setPreferredWidth();
            } else {
                repaint();
            }
        }
    }
}