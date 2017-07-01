package delta.games.lotro.gui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;

/**
 * Factory for GUI items.
 * @author DAM
 */
public class GuiFactory
{
  private static Color BACKGROUND=Color.WHITE;
  private static Color FOREGROUND=Color.BLACK;

  private static boolean USE_BACKGROUND_PATTERN=true;

  /**
   * Get the standard foreground color.
   * @return a color.
   */
  public static Color getForegroundColor()
  {
    return FOREGROUND;
  }

  /**
   * Get the standard background color.
   * @return a color.
   */
  public static Color getBackgroundColor()
  {
    return BACKGROUND;
  }

  /**
   * Get a new panel using the given layout.
   * @param layout Layout to use.
   * @return a new panel.
   */
  public static JPanel buildPanel(LayoutManager layout)
  {
    JPanel panel=new JPanel(layout);
    if (USE_BACKGROUND_PATTERN)
    {
      panel.setOpaque(false);
    }
    else
    {
      panel.setBackground(BACKGROUND);
      panel.setOpaque(true);
    }
    return panel;
  }

  /**
   * Get the background pattern image.
   * @return an image.
   */
  public static BufferedImage getBackgroundImage()
  {
    BufferedImage backgroundImage=IconsManager.getImage("/resources/gui/fond.png");
    return backgroundImage;
  }

  /**
   * Get a painter for background.
   * @return A background painter.
   */
  public static Paint getBackgroundPaint()
  {
    if (USE_BACKGROUND_PATTERN)
    {
      BufferedImage backgroundImage=GuiFactory.getBackgroundImage();
      Rectangle r=new Rectangle(0,0,backgroundImage.getWidth(),backgroundImage.getHeight());
      TexturePaint paint=new TexturePaint(backgroundImage,r);
      return paint;
    }
    return BACKGROUND;
  }

  /**
   * Build a background panel.
   * @param layout Layout manager.
   * @return a new panel.
   */
  public static JPanel buildBackgroundPanel(LayoutManager layout)
  {
    JPanel panel;
    if (USE_BACKGROUND_PATTERN)
    {
      BufferedImage backgroundImage=getBackgroundImage();
      panel=new BackgroundPanel(backgroundImage,layout);
    }
    else
    {
      panel=new JPanel(layout);
      panel.setBackground(BACKGROUND);
    }
    return panel;
  }

  /**
   * Get a new button using the given label.
   * @param label Label to use.
   * @return a new button.
   */
  public static JButton buildButton(String label)
  {
    JButton b=new JButton(label);
    b.setBackground(BACKGROUND);
    b.setForeground(FOREGROUND);
    return b;
  }

  /**
   * Get a new titled border using the given title.
   * @param title Title to use.
   * @return a new titled border.
   */
  public static TitledBorder buildTitledBorder(String title)
  {
    Border border=BorderFactory.createBevelBorder(BevelBorder.LOWERED,FOREGROUND,Color.GRAY);
    TitledBorder titledBorder=BorderFactory.createTitledBorder(border,title);
    titledBorder.setTitleColor(FOREGROUND);
    return titledBorder;
  }

  /**
   * Get a new label using the given text.
   * @param label Text to use.
   * @return a new label.
   */
  public static JLabel buildLabel(String label)
  {
    return buildLabel(label,null);
  }

  /**
   * Get a new label using the given text and font size.
   * @param label Text to use.
   * @param size Font size.
   * @return a new label.
   */
  public static JLabel buildLabel(String label, float size)
  {
    return buildLabel(label,Float.valueOf(size));
  }

  /**
   * Build an iconic label.
   * @param icon Icon to display.
   * @return A label.
   */
  public static JLabel buildIconLabel(Icon icon)
  {
    JLabel l=new JLabel(icon);
    l.setOpaque(false);
    return l;
  }

  /**
   * Build a transparent icon label.
   * @param size Size of icon.
   * @return A label.
   */
  public static JLabel buildTransparentIconlabel(int size)
  {
    BufferedImage image=new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
    ImageIcon icon=new ImageIcon(image);
    return buildIconLabel(icon);
  }

  /**
   * Get a new label using the given text.
   * @param label Text to use.
   * @param size Font size.
   * @return a new label.
   */
  private static JLabel buildLabel(String label, Float size)
  {
    JLabel l=new JLabel(label);
    l.setForeground(FOREGROUND);
    if (USE_BACKGROUND_PATTERN)
    {
      l.setOpaque(false);
    }
    else
    {
      l.setBackground(BACKGROUND);
      l.setOpaque(true);
    }
    if (size!=null)
    {
      l.setFont(l.getFont().deriveFont(size.floatValue()));
    }
    return l;
  }

  /**
   * Get a new check box using the given label.
   * @param label Label to use.
   * @return a new checkbox.
   */
  public static JCheckBox buildCheckbox(String label)
  {
    JCheckBox checkbox=new JCheckBox();
    checkbox.setText(label);
    checkbox.setForeground(FOREGROUND);
    if (USE_BACKGROUND_PATTERN)
    {
      checkbox.setOpaque(false);
    }
    else
    {
      checkbox.setBackground(BACKGROUND);
      checkbox.setOpaque(true);
    }
    return checkbox;
  }

  /**
   * Get a new text field using the given text.
   * @param text Text to use.
   * @return a new text field.
   */
  public static JTextField buildTextField(String text)
  {
    JTextField tf=new JTextField();
    tf.setText(text);
    tf.setForeground(FOREGROUND);
    tf.setBackground(BACKGROUND);
    return tf;
  }

  /**
   * Get a new text area using the given text.
   * @param text Text to use.
   * @param opaque Indicates if the component to build is opaque or not.
   * @return a new text area.
   */
  public static JTextArea buildTextArea(String text, boolean opaque)
  {
    JTextArea tf=new JTextArea();
    tf.setText(text);
    tf.setForeground(FOREGROUND);
    tf.setOpaque(opaque);
    if (opaque)
    {
      tf.setBackground(BACKGROUND);
    }
    return tf;
  }

  /**
   * Get a new combo-box.
   * @return a new combo-box.
   */
  public static JComboBox buildComboBox()
  {
    JComboBox cb=new JComboBox();
    cb.setBackground(BACKGROUND);
    cb.setForeground(FOREGROUND);
    return cb;
  }

  /**
   * Get a new table.
   * @return a new table.
   */
  public static JTable buildTable()
  {
    JTable table=new JTable();
    table.setForeground(FOREGROUND);
    JTableHeader header=table.getTableHeader();
    header.setForeground(FOREGROUND);
    table.setGridColor(FOREGROUND);
    if (USE_BACKGROUND_PATTERN)
    {
      table.setOpaque(false);
      table.setBackground(new Color(0,true));
      header.setOpaque(false);
      header.setBackground(new Color(0,true));
    }
    else
    {
      table.setBackground(BACKGROUND);
      table.setOpaque(true);
      header.setBackground(BACKGROUND);
      header.setOpaque(true);
    }
    return table;
  }

  /**
   * Get a new scroll pane around the given component.
   * @param component Wrapped component.
   * @return a new scroll pane.
   */
  public static JScrollPane buildScrollPane(Component component)
  {
    JScrollPane scrollPane=new JScrollPane(component);
    scrollPane.setForeground(FOREGROUND);
    if (USE_BACKGROUND_PATTERN)
    {
      scrollPane.setOpaque(false);
      // Make main viewport transparent
      JViewport viewport=scrollPane.getViewport();
      viewport.setOpaque(false);
      viewport.setBackground(new Color(0,true));
      // Make header transparent (for tables)
      // - create a fake view so that the column header viewport gets created!
      scrollPane.setColumnHeaderView(new JPanel());
      JViewport hviewport=scrollPane.getColumnHeader();
      hviewport.setOpaque(false);
      hviewport.setBackground(new Color(0,true));
    }
    else
    {
      scrollPane.setBackground(BACKGROUND);
      scrollPane.setOpaque(true);
    }
    JScrollBar vBar=scrollPane.getVerticalScrollBar();
    if (vBar!=null)
    {
      vBar.setForeground(FOREGROUND);
      vBar.setBackground(BACKGROUND);
    }
    return scrollPane;
  }

  /**
   * Get a new tabbed pane.
   * @return a new tabbed pane.
   */
  public static JTabbedPane buildTabbedPane()
  {
    JTabbedPane tabbedPane=new JTabbedPane();
    tabbedPane.setBackground(BACKGROUND);
    tabbedPane.setForeground(FOREGROUND);
    return tabbedPane;
  }

  /**
   * Get a new menu bar.
   * @return a new menu bar.
   */
  public static JMenuBar buildMenuBar()
  {
    JMenuBar mb=new JMenuBar();
    mb.setForeground(FOREGROUND);
    mb.setBackground(BACKGROUND);
    return mb;
  }

  /**
   * Get a new menu using the given text.
   * @param label Text to use.
   * @return a new menu.
   */
  public static JMenu buildMenu(String label)
  {
    JMenu menu=new JMenu(label);
    menu.setForeground(FOREGROUND);
    menu.setBackground(BACKGROUND);
    return menu;
  }

  /**
   * Get a new menu item using the given text.
   * @param label Text to use.
   * @return a new text field.
   */
  public static JMenuItem buildMenuItem(String label)
  {
    JMenuItem menuItem=new JMenuItem(label);
    menuItem.setForeground(FOREGROUND);
    menuItem.setBackground(BACKGROUND);
    return menuItem;
  }

  /**
   * General UI initializations.
   */
  public static void init()
  {
    UIManager.put("OptionPane.background",BACKGROUND);
    UIManager.put("Panel.background",BACKGROUND);
    UIManager.put("OptionPane.messageForeground", FOREGROUND);
  }

  /**
   * Show a modal question dialog.
   * @param parent Parent component.
   * @param message Question message.
   * @param title Title of the dialog window.
   * @param optionType Options configuration.
   * @return A result code (see {@link JOptionPane}).
   */
  public static int showQuestionDialog(Component parent, String message, String title, int optionType)
  {
    int ret=JOptionPane.showConfirmDialog(parent,message,title,optionType);
    return ret;
  }

  /**
   * Show a information dialog.
   * @param parent Parent component.
   * @param message Information message.
   * @param title Title of the dialog window.
   */
  public static void showInformationDialog(Component parent, String message, String title)
  {
    JOptionPane.showMessageDialog(parent,message,title,JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Show an error dialog.
   * @param parent Parent component.
   * @param message Information message.
   * @param title Title of the dialog window.
   */
  public static void showErrorDialog(Component parent, String message, String title)
  {
    JOptionPane.showMessageDialog(parent,message,title,JOptionPane.ERROR_MESSAGE);
  }
}
