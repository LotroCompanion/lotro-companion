package delta.games.lotro.gui.items.relics;

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

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.legendary.LegendaryAttrs;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicType;

/**
 * Panel to edit relics.
 * @author DAM
 */
public class RelicsEditionPanelController implements ActionListener
{
  private static final String ITEM_WITH_NO_ICON="/resources/gui/equipment/itemNoIcon.png";

  /**
   * Relic types, ordered like in the LOTRO UI.
   */
  private static final RelicType[] RELIC_TYPES={RelicType.SETTING,RelicType.GEM,RelicType.RUNE,RelicType.CRAFTED_RELIC};

  // Data
  private LegendaryAttrs _legAttrs;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  private List<JButton> _buttons;
  private List<JLabel> _relicNames;
  private List<JButton> _deleteButtons;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param legAttrs Attributes to edit.
   */
  public RelicsEditionPanelController(WindowController parent,LegendaryAttrs legAttrs)
  {
    _parent=parent;
    _legAttrs=legAttrs;
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
    List<Relic> relics=_legAttrs.getRelics();
    int nbRelics=relics.size();
    _buttons=new ArrayList<JButton>();
    _relicNames=new ArrayList<JLabel>();
    _deleteButtons=new ArrayList<JButton>();
    for(int i=0;i<nbRelics;i++)
    {
      // Button
      JButton relicIconButton=GuiFactory.buildButton("");
      relicIconButton.setOpaque(false);
      _buttons.add(relicIconButton);
      relicIconButton.setBorderPainted(false);
      relicIconButton.setMargin(new Insets(0,0,0,0));
      GridBagConstraints c=new GridBagConstraints(0,i,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(relicIconButton,c);
      relicIconButton.addActionListener(this);
      // Label
      JLabel relicName=GuiFactory.buildLabel("");
      _relicNames.add(relicName);
      c=new GridBagConstraints(1,i,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
      panel.add(relicName,c);
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
   * Get the contents of the edited data into the given storage.
   * @param attrs Storage attributes.
   */
  public void getData(LegendaryAttrs attrs)
  {
    attrs.setSetting(_legAttrs.getSetting());
    attrs.setGem(_legAttrs.getGem());
    attrs.setRune(_legAttrs.getRune());
    attrs.setCraftedRelic(_legAttrs.getCraftedRelic());
  }

  /**
   * Update UI from the managed data.
   */
  private void update()
  {
    List<Relic> relics=_legAttrs.getRelics();
    int nbRelics=relics.size();
    for(int i=0;i<nbRelics;i++)
    {
      Relic relic=relics.get(i);
      // Set relic icon
      JButton button=_buttons.get(i);
      ImageIcon icon=null;
      if (relic!=null)
      {
        String filename=relic.getIconFilename();
        icon=LotroIconsManager.getRelicIcon(filename);
      }
      else
      {
        icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
      }
      button.setIcon(icon);
      // Text
      String text=(relic!=null)?relic.getName():"";
      JLabel relicName=_relicNames.get(i);
      relicName.setText(text);
    }
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    // Relic icon button
    int index=_buttons.indexOf(source);
    if (index!=-1)
    {
      Relic initialRelic=_legAttrs.getRelics().get(index);
      RelicType type=RELIC_TYPES[index];
      Relic relic=RelicChooser.selectRelic(_parent,type,initialRelic);
      if (relic!=null)
      {
        _legAttrs.slotRelic(relic);
        update();
      }
    }
    else
    {
      index=_deleteButtons.indexOf(source);
      if (index!=-1)
      {
        RelicType type=RELIC_TYPES[index];
        if (type==RelicType.SETTING) _legAttrs.setSetting(null);
        if (type==RelicType.GEM) _legAttrs.setGem(null);
        if (type==RelicType.RUNE) _legAttrs.setRune(null);
        if (type==RelicType.CRAFTED_RELIC) _legAttrs.setCraftedRelic(null);
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
    _relicNames.clear();
    _deleteButtons.clear();
  }
}
