package delta.games.lotro.gui.items.essences;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;

/**
 * Panel to edit essences.
 * @author DAM
 */
public class EssencesEditionPanelController
{
  // Data
  private CharacterSummary _character;
  // GUI
  private JPanel _essencesPanel;
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
    JPanel wholePanel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    _essencesPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    _essencesPanel.setOpaque(true);
    wholePanel.add(_essencesPanel,BorderLayout.CENTER);
    JButton button=GuiFactory.buildButton("New slot");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        addSlot();
      }
    };
    button.addActionListener(al);
    JPanel buttonsPanel=GuiFactory.buildBackgroundPanel(null);
    BoxLayout layout=new BoxLayout(buttonsPanel,BoxLayout.Y_AXIS);
    buttonsPanel.setLayout(layout);
    buttonsPanel.add(button);
    wholePanel.add(buttonsPanel,BorderLayout.EAST);
    return wholePanel;
  }

  /**
   * Initialize the managed panel with the given item.
   * @param itemInstance Item instance to set.
   */
  public void initFromItem(ItemInstance<? extends Item> itemInstance)
  {
    _essenceControllers.clear();
    int nbSlots=itemInstance.getEssencesCount();
    for(int i=0;i<nbSlots;i++)
    {
      Item essence=itemInstance.getEssenceAt(i);
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
    _essencesPanel.removeAll();
    int index=0;
    for(SingleEssenceEditionController ctrl : _essenceControllers)
    {
      buildStatUi(_essencesPanel,ctrl,index);
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

  private void addSlot()
  {
    SingleEssenceEditionController ctrl=new SingleEssenceEditionController(_parent,1,_character);
    _essenceControllers.add(ctrl);
    updateUi();
  }

  /**
   * Get the selected essences.
   * @return a list of essences (with <code>null</code> items possible).
   */
  public List<Item> getEssences()
  {
    List<Item> essences=new ArrayList<Item>();
    for(SingleEssenceEditionController ctrl : _essenceControllers)
    {
      Item essence=ctrl.getEssence();
      essences.add(essence);
    }
    return essences;
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
    if (_essencesPanel!=null)
    {
      _essencesPanel.removeAll();
      _essencesPanel=null;
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
