package delta.games.lotro.gui.utils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;

/**
 * Utility methods for UI layout.
 * @author DAM
 */
public class LayoutUtils
{
  /**
   * Build a vertical panel of components.
   * @param components Components to use.
   * @param nbPerColumn Maximum number of components in a single column.
   * @return A panel.
   */
  public static JPanel buildVerticalPanel(List<? extends JComponent> components, int nbPerColumn)
  {
    return buildVerticalPanel(components,nbPerColumn,2,5);
  }

  /**
   * Build a vertical panel of components.
   * @param components Components to use.
   * @param nbPerColumn Maximum number of components in a single column.
   * @param deltaX Horizontal margin.
   * @param deltaY Vertical margin.
   * @return A panel.
   */
  public static JPanel buildVerticalPanel(List<? extends JComponent> components, int nbPerColumn, int deltaX, int deltaY)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(deltaY,deltaX,deltaY,deltaX),0,0);
    for(JComponent component : components)
    {
      panel.add(component,c);
      c.gridy++;
      if (c.gridy==nbPerColumn)
      {
        c.gridy=0;
        c.gridx++;
        c.insets.top=deltaY;
      }
      else if (c.gridy>0)
      {
        c.insets.top=0;
      }
    }
    return panel;
  }

  /**
   * Build an horizontal panel of components.
   * @param components Components to use.
   * @param nbPerLine Maximum number of components in a single row.
   * @return A panel.
   */
  public static JPanel buildHorizontalPanel(List<? extends JComponent> components, int nbPerLine)
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    for(JComponent component : components)
    {
      panel.add(component,c);
      c.gridx++;
      if (c.gridx==nbPerLine)
      {
        c.gridx=0;
        c.gridy++;
        c.insets.left=5;
      }
      else if (c.gridy>0)
      {
        c.insets.left=0;
      }
    }
    return panel;
  }
}
