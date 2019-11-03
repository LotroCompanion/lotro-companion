package delta.games.lotro.gui.items.legendary.relics;

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
import delta.common.ui.swing.icons.IconsManager;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.StatDisplayUtils;
import delta.games.lotro.lore.items.legendary.relics.Relic;
import delta.games.lotro.lore.items.legendary.relics.RelicType;
import delta.games.lotro.lore.items.legendary.relics.RelicsSet;

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
  private RelicsSet _relics;
  // GUI
  private JPanel _panel;
  private WindowController _parent;
  private List<SingleRelicEditionController> _editors;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param relics Attributes to edit.
   */
  public RelicsEditionPanelController(WindowController parent, RelicsSet relics)
  {
    _parent=parent;
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

    int baseLine=0;
    List<Relic> relics=_relics.getRelics();
    int nbRelics=relics.size();
    for(int i=0;i<nbRelics;i++)
    {
      SingleRelicEditionController editor=new SingleRelicEditionController();
      // Icon
      JButton relicIconButton=editor.getIcon();
      GridBagConstraints c=new GridBagConstraints(0,baseLine,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(relicIconButton,c);
      relicIconButton.addActionListener(this);
      // Label
      MultilineLabel2 relicName=editor.getNameGadget();
      c=new GridBagConstraints(1,baseLine,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(relicName,c);
      // Stats
      MultilineLabel2 stats=editor.getStatsGadget();
      c=new GridBagConstraints(1,baseLine+1,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,5),0,0);
      panel.add(stats,c);
      // Delete button
      JButton deleteButton=editor.getDeleteButton();
      deleteButton.addActionListener(this);
      c=new GridBagConstraints(2,baseLine,1,2,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      panel.add(deleteButton,c);

      baseLine+=2;
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
        icon=IconsManager.getIcon(ITEM_WITH_NO_ICON);
      }
      button.setIcon(icon);
      // Text
      String text=(relic!=null)?relic.getName():"";
      MultilineLabel2 relicName=editor.getNameGadget();
      relicName.setText(new String[]{text});
      // Stats
      MultilineLabel2 relicStats=editor.getStatsGadget();
      if (relic!=null)
      {
        BasicStatsSet stats=relic.getStats();
        String[] lines=StatDisplayUtils.getStatsDisplayLines(stats);
        relicStats.setText(lines);
      }
      else
      {
        relicStats.setText(new String[]{""});
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
        Relic relic=RelicChooser.selectRelic(_parent,type,initialRelic);
        if (relic!=null)
        {
          _relics.slotRelic(relic);
          update();
        }
      }
      // Delete button
      JButton deleteButton=editor.getDeleteButton();
      if (source==deleteButton)
      {
        RelicType type=RELIC_TYPES[index];
        if (type==RelicType.SETTING) _relics.setSetting(null);
        if (type==RelicType.GEM) _relics.setGem(null);
        if (type==RelicType.RUNE) _relics.setRune(null);
        if (type==RelicType.CRAFTED_RELIC) _relics.setCraftedRelic(null);
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
