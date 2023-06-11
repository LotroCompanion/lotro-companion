package delta.games.lotro.gui.lore.items.legendary.relics;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.lore.items.legendary.relics.RelicTypes;
import delta.games.lotro.lore.items.legendary.relics.RelicsSet;

/**
 * Panel to edit relics.
 * @author DAM
 */
public class RelicsEditionPanelController implements ActionListener
{
  /**
   * Relic types, ordered like in the LOTRO UI.
   */
  private static final RelicType[] RELIC_TYPES={RelicTypes.SETTING,RelicTypes.GEM,RelicTypes.RUNE,RelicTypes.CRAFTED_RELIC};

  // Data
  private RelicsSet _relics;
  private EquipmentLocation _slot;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  private List<SingleRelicEditionController> _editors;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param slot Slot to use.
   * @param relics Attributes to edit.
   */
  public RelicsEditionPanelController(WindowController parent, EquipmentLocation slot, RelicsSet relics)
  {
    _parent=parent;
    _slot=slot;
    _relics=relics;
    _editors=new ArrayList<SingleRelicEditionController>();
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    List<Relic> relics=_relics.getRelics();
    int nbRelics=relics.size();
    for(int i=0;i<nbRelics;i++)
    {
      SingleRelicEditionController editor=new SingleRelicEditionController();
      // Icon
      JButton relicIconButton=editor.getIcon();
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(relicIconButton,c);
      relicIconButton.addActionListener(this);
      // Text
      MultilineLabel2 text=editor.getLinesGadget();
      c=new GridBagConstraints(1,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(text,c);
      // Delete button
      JButton deleteButton=editor.getDeleteButton();
      deleteButton.addActionListener(this);
      c=new GridBagConstraints(2,y,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(deleteButton,c);
      y++;
      _editors.add(editor);
    }
    return panel;
  }

  /**
   * Get the contents of the edited data into the given storage.
   * @param relics Source data.
   */
  public void getData(RelicsSet relics)
  {
    relics.setSetting(_relics.getSetting());
    relics.setGem(_relics.getGem());
    relics.setRune(_relics.getRune());
    relics.setCraftedRelic(_relics.getCraftedRelic());
  }

  /**
   * Update UI from the managed data.
   */
  private void update()
  {
    List<Relic> relics=_relics.getRelics();
    int nbRelics=relics.size();
    for(int i=0;i<nbRelics;i++)
    {
      Relic relic=relics.get(i);
      SingleRelicEditionController editor=_editors.get(i);
      // Set relic icon
      JButton button=editor.getIcon();
      ImageIcon icon=null;
      if (relic!=null)
      {
        String filename=relic.getIconFilename();
        icon=LotroIconsManager.getRelicIcon(filename);
      }
      else
      {
        icon=LotroIconsManager.getDefaultItemIcon();
      }
      button.setIcon(icon);
      // Text
      String name=(relic!=null)?relic.getName():"";
      editor.setName(name);
      // Stats
      if (relic!=null)
      {
        BasicStatsSet stats=relic.getStats();
        String[] lines=StatUtils.getStatsDisplayLines(stats);
        editor.setStats(lines);
      }
      else
      {
        editor.setStats(new String[]{""});
      }
    }
    // - Resize window
    _parent.pack();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    int index=0;
    for(SingleRelicEditionController editor : _editors)
    {
      // Relic icon button
      JButton iconButton=editor.getIcon();
      if (source==iconButton)
      {
        Relic initialRelic=_relics.getRelics().get(index);
        RelicType type=RELIC_TYPES[index];
        Relic relic=RelicChooser.selectRelic(_parent,type,_slot,initialRelic);
        if (relic!=null)
        {
          _relics.slotRelic(relic,type);
          update();
        }
      }
      // Delete button
      JButton deleteButton=editor.getDeleteButton();
      if (source==deleteButton)
      {
        RelicType type=RELIC_TYPES[index];
        if (type==RelicTypes.SETTING) _relics.setSetting(null);
        if (type==RelicTypes.GEM) _relics.setGem(null);
        if (type==RelicTypes.RUNE) _relics.setRune(null);
        if (type==RelicTypes.CRAFTED_RELIC) _relics.setCraftedRelic(null);
        update();
      }
      index++;
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
    if (_editors!=null)
    {
      for(SingleRelicEditionController editor : _editors)
      {
        editor.dispose();
      }
      _editors=null;
    }
  }
}
