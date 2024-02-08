package delta.games.lotro.gui.lore.items.legendary.passives;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.items.legendary.LegendaryConstants;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.passives.Passive;

/**
 * Panel to edit passives.
 * @author DAM
 */
public class PassivesEditionPanelController
{
  // GUI
  private JPanel _panel;
  private List<SinglePassiveEditionController> _passiveGadgets;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param legendaryAttrs Attributes to edit.
   * @param itemId Item identifier.
   */
  public PassivesEditionPanelController(WindowController parent, LegendaryInstanceAttrs legendaryAttrs, int itemId)
  {
    _passiveGadgets=new ArrayList<SinglePassiveEditionController>();
    List<Passive> passives=legendaryAttrs.getPassives();
    int nbPassives=passives.size();
    for(int i=0;i<LegendaryConstants.NB_PASSIVES_MAX;i++)
    {
      Passive passive=(i<nbPassives)?passives.get(i):null;
      SinglePassiveEditionController controller=new SinglePassiveEditionController(parent,passive,itemId);
      _passiveGadgets.add(controller);
    }
  }

  /**
   * Set the level to use for passive stats computations.
   * @param level Level to set.
   */
  public void setLevel(int level)
  {
    for(SinglePassiveEditionController passiveGadgetsCtrl : _passiveGadgets)
    {
      passiveGadgetsCtrl.setLevel(level);
    }
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
    int y=0;
    for(SinglePassiveEditionController controller : _passiveGadgets)
    {
      // Label
      JLabel label=controller.getValueLabel();
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(2,5,2,5),0,0);
      panel.add(label,c);
      // Choose button
      JButton chooser=controller.getChooseButton();
      c=new GridBagConstraints(1,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      panel.add(chooser,c);
      // Delete button
      JButton deleteButton=controller.getDeleteButton();
      c=new GridBagConstraints(2,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,2,5),0,0);
      panel.add(deleteButton,c);
      y++;
    }
    return panel;
  }

  /**
   * Get the contents of the edited data into the given storage.
   * @param legendaryAttrs Storage for data.
   */
  public void getData(LegendaryInstanceAttrs legendaryAttrs)
  {
    legendaryAttrs.removeAllPassvies();
    for(SinglePassiveEditionController controller : _passiveGadgets)
    {
      Passive passive=controller.getPassive();
      if (passive!=null)
      {
        legendaryAttrs.addPassive(passive);
      }
    }
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
    if (_passiveGadgets!=null)
    {
      for(SinglePassiveEditionController controller : _passiveGadgets)
      {
        controller.dispose();
      }
      _passiveGadgets.clear();
    }
  }
}
