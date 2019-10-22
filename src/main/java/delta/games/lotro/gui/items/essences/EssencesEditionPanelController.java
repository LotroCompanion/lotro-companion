package delta.games.lotro.gui.items.essences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.essences.EssencesSet;

/**
 * Panel to edit essences.
 * @author DAM
 */
public class EssencesEditionPanelController
{
  // Data
  private CharacterSummary _character;
  private EssencesSet _essences;
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<SingleEssenceEditionController> _essenceControllers;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param character Character.
   */
  public EssencesEditionPanelController(WindowController parent, CharacterSummary character)
  {
    _parent=parent;
    _character=character;
    _essenceControllers=new ArrayList<SingleEssenceEditionController>();
    _panel=build();
  }

  private JPanel build()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    return ret;
  }

  /**
   * Initialize the managed panel with the given essences set.
   * @param essences Item instance to set.
   */
  public void init(EssencesSet essences)
  {
    _essences=essences;
    _essenceControllers.clear();
    int nbSlots=essences.getSize();
    for(int i=0;i<nbSlots;i++)
    {
      Item essence=essences.getEssence(i);
      SingleEssenceEditionController ctrl=new SingleEssenceEditionController(_parent,1,_character);
      ctrl.setEssence(essence);
      _essenceControllers.add(ctrl);
    }
    updateUi();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void updateUi()
  {
    _panel.removeAll();
    int index=0;
    for(SingleEssenceEditionController ctrl : _essenceControllers)
    {
      buildStatUi(_panel,ctrl,index);
      index++;
    }
    _panel.revalidate();
    _panel.repaint();
  }

  private void buildStatUi(JPanel panel,SingleEssenceEditionController ctrl,int index)
  {
    GridBagConstraints c=new GridBagConstraints(0,index,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Essence icon
    JButton value=ctrl.getEssenceButton();
    panel.add(value,c);
    // Essence label
    JPanel unit=ctrl.getEssenceNameLabel();
    c=new GridBagConstraints(1,index,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(unit,c);
    // Delete button
    JButton deleteButton=ctrl.getDeleteButton();
    c=new GridBagConstraints(2,index,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    c.gridx++;
    panel.add(deleteButton,c);
  }

  /**
   * Get the current values of the edited essences set.
   */
  public void getEssences()
  {
    int nbSlots=_essences.getSize();
    for(int i=0;i<nbSlots;i++)
    {
      SingleEssenceEditionController ctrl =_essenceControllers.get(i);
      Item essence=ctrl.getEssence();
      _essences.setEssence(i,essence);
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_essenceControllers!=null)
    {
      for(SingleEssenceEditionController essenceController : _essenceControllers)
      {
        essenceController.dispose();
      }
      _essenceControllers.clear();
      _essenceControllers=null;
    }
  }
}
