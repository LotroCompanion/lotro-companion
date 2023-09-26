package delta.games.lotro.gui.lore.items.essences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.BasicCharacterAttributes;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.essences.Essence;
import delta.games.lotro.lore.items.essences.EssencesSet;

/**
 * Panel to edit essences.
 * @author DAM
 */
public class EssencesEditionPanelController implements ActionListener
{
  // Data
  private BasicCharacterAttributes _attrs;
  private List<Essence> _essences;
  private Item _item;
  // GUI
  private JPanel _panel;
  // Controllers
  private WindowController _parent;
  private List<SingleEssenceEditionController> _essenceControllers;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param attrs Attributes of toon to use.
   * @param item Item to add essences to.
   */
  public EssencesEditionPanelController(WindowController parent, BasicCharacterAttributes attrs, Item item)
  {
    _parent=parent;
    _attrs=attrs;
    _item=item;
    _essenceControllers=new ArrayList<SingleEssenceEditionController>();
    _essences=new ArrayList<Essence>();
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
    _essences.clear();
    _essenceControllers.clear();
    int nbSlots=essences.getSize();
    for(int i=0;i<nbSlots;i++)
    {
      Essence essence=essences.getEssence(i);
      _essences.add(essence);
      SocketType type=essences.getType(i);
      SingleEssenceEditionController ctrl=new SingleEssenceEditionController(type);
      ctrl.setEssence(essence);
      _essenceControllers.add(ctrl);
    }
    buildUi();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void buildUi()
  {
    _panel.removeAll();

    int y=0;
    int nbEssences=_essenceControllers.size();
    for(int i=0;i<nbEssences;i++)
    {
      SingleEssenceEditionController editor=_essenceControllers.get(i);
      // Icon
      JButton iconButton=editor.getIcon();
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      _panel.add(iconButton,c);
      iconButton.addActionListener(this);
      // Text
      MultilineLabel2 text=editor.getLinesGadget();
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      _panel.add(text,c);
      // Delete button
      JButton deleteButton=editor.getDeleteButton();
      deleteButton.addActionListener(this);
      c=new GridBagConstraints(2,y,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      _panel.add(deleteButton,c);
      y++;
    }
    _panel.revalidate();
    _panel.repaint();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    int index=0;
    for(SingleEssenceEditionController editor : _essenceControllers)
    {
      // Icon button
      JButton iconButton=editor.getIcon();
      if (source==iconButton)
      {
        SocketType type=_item.getEssenceSlotsSetup().getSlotType(index);
        Essence essence=EssenceChoice.chooseEssence(_parent,_attrs,type);
        if (essence!=null)
        {
          editor.setEssence(essence);
          _essences.set(index,essence);
        }
      }
      // Delete button
      JButton deleteButton=editor.getDeleteButton();
      if (source==deleteButton)
      {
        editor.setEssence(null);
        _essences.set(index,null);
      }
      index++;
    }
    _parent.pack();
  }

  /**
   * Get the current values of the edited essences set.
   * @param essencesSet Storage for read data.
   */
  public void getEssences(EssencesSet essencesSet)
  {
    int nbSlots=_essences.size();
    for(int i=0;i<nbSlots;i++)
    {
      Essence essence=_essences.get(i);
      essencesSet.setEssence(i,essence);
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
    _essences=null;
    _item=null;
  }
}
