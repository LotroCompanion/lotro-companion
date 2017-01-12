package delta.games.lotro.gui.items.essences;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemPropertyNames;
import delta.games.lotro.lore.items.essences.EssencesSet;
import delta.games.lotro.utils.gui.WindowController;

/**
 * Panel to edit essences.
 * @author DAM
 */
public class EssencesEditionPanelController implements ActionListener
{
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  // Data
  private Item _item;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  private List<JButton> _buttons;
  private List<JLabel> _essenceNames;
  private List<JButton> _deleteButtons;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param item Item to edit.
   */
  public EssencesEditionPanelController(WindowController parent,Item item)
  {
    _parent=parent;
    _item=item;
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
      update();
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    _buttons=new ArrayList<JButton>();
    _essenceNames=new ArrayList<JLabel>();
    _deleteButtons=new ArrayList<JButton>();
    int nbSlots=_item.getEssenceSlots();
    for(int i=0;i<nbSlots;i++)
    {
      // Button
      JButton essenceIconButton=GuiFactory.buildButton("");
      essenceIconButton.setOpaque(false);
      _buttons.add(essenceIconButton);
      essenceIconButton.setBorderPainted(false);
      essenceIconButton.setMargin(new Insets(0,0,0,0));
      GridBagConstraints c=new GridBagConstraints(0,i,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(essenceIconButton,c);
      essenceIconButton.addActionListener(this);
      // Label
      JLabel essenceName=GuiFactory.buildLabel("");
      _essenceNames.add(essenceName);
      c=new GridBagConstraints(1,i,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(essenceName,c);
      // Delete button
      ImageIcon icon=IconsManager.getIcon("/resources/gui/icons/cross.png");
      JButton deleteButton=GuiFactory.buildButton("");
      deleteButton.setIcon(icon);
      deleteButton.setMargin(new Insets(0,0,0,0));
      deleteButton.setContentAreaFilled(false);
      deleteButton.setBorderPainted(false);
      deleteButton.addActionListener(this);
      _deleteButtons.add(deleteButton);
      c=new GridBagConstraints(2,i,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(deleteButton,c);
    }
    return panel;
  }

  /**
   * Update UI from the managed data.
   */
  public void update()
  {
    int nbSlots=_item.getEssenceSlots();
    for(int i=0;i<nbSlots;i++)
    {
      Item essence=getEssenceAt(i);
      // Set essence icon
      JButton button=_buttons.get(i);
      ImageIcon icon=null;
      if (essence!=null)
      {
        String iconId=essence.getProperty(ItemPropertyNames.ICON_ID);
        String backgroundIconId=essence.getProperty(ItemPropertyNames.BACKGROUND_ICON_ID);
        icon=IconsManager.getItemIcon(iconId,backgroundIconId);
      }
      else
      {
        icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
      }
      button.setIcon(icon);
      // Text
      String text=(essence!=null)?essence.getName():"";
      JLabel essenceName=_essenceNames.get(i);
      essenceName.setText(text);
    }
  }

  private Item getEssenceAt(int index)
  {
    Item essence=null;
    EssencesSet essences=_item.getEssences();
    if (essences!=null)
    {
      if (index<essences.getSize())
      {
        essence=essences.getEssence(index);
      }
    }
    return essence;
  }

  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    // Essence icon button
    int index=_buttons.indexOf(source);
    if (index!=-1)
    {
      //Item initialEssence=getEssenceAt(index);
      Item essence=EssenceChoice.chooseEssence(_parent);
      if (essence!=null)
      {
        EssencesSet essences=_item.getEssences();
        if (essences==null)
        {
          essences=new EssencesSet(_item.getEssenceSlots());
          _item.setEssences(essences);
        }
        essences.setEssence(index,essence);
        update();
      }
    }
    else
    {
      index=_deleteButtons.indexOf(source);
      if (index!=-1)
      {
        EssencesSet essences=_item.getEssences();
        if (essences==null)
        {
          essences=new EssencesSet(_item.getEssenceSlots());
          _item.setEssences(essences);
        }
        essences.setEssence(index,null);
        update();
      }
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
    _buttons.clear();
    _essenceNames.clear();
    _deleteButtons.clear();
  }
}
