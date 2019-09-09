package delta.games.lotro.gui.items.legendary.titles;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;

/**
 * Panel to edit a legendary title.
 * @author DAM
 */
public class LegendaryTitleEditionPanelController
{
  // GUI
  private JPanel _panel;
  private SingleTitleEditionController _controller;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param legendaryAttrs Attributes to edit.
   */
  public LegendaryTitleEditionPanelController(WindowController parent, LegendaryInstanceAttrs legendaryAttrs)
  {
    LegendaryTitle title=legendaryAttrs.getTitle();
    _controller=new SingleTitleEditionController(parent,title);
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    // Label
    MultilineLabel2 label=_controller.getValueLabel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
    panel.add(label,c);
    // Choose button
    JButton chooser=_controller.getChooseButton();
    c=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    panel.add(chooser,c);
    // Delete button
    JButton deleteButton=_controller.getDeleteButton();
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
    panel.add(deleteButton,c);
    return panel;
  }

  /**
   * Get the contents of the edited data into the given storage.
   * @param legendaryAttrs Storage for data.
   */
  public void getData(LegendaryInstanceAttrs legendaryAttrs)
  {
    LegendaryTitle title=_controller.getLegendaryTitle();
    legendaryAttrs.setTitle(title);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_controller!=null)
    {
      _controller.dispose();
      _controller=null;
    }
  }
}
