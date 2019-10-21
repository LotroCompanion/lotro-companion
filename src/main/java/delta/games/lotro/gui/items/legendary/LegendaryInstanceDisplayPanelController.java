package delta.games.lotro.gui.items.legendary;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.gui.items.legendary.imbued.ImbuedLegendaryAttrsDisplayPanelController;
import delta.games.lotro.gui.items.legendary.non_imbued.NonImbuedLegendaryAttrsDisplayPanelController;
import delta.games.lotro.gui.items.legendary.relics.RelicsSetDisplayController;
import delta.games.lotro.gui.items.legendary.titles.LegendaryTitleDisplayPanelController;
import delta.games.lotro.gui.utils.StatDisplayUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstance;
import delta.games.lotro.lore.items.legendary.LegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.imbued.ImbuedLegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacy;
import delta.games.lotro.lore.items.legendary.non_imbued.DefaultNonImbuedLegacyInstance;
import delta.games.lotro.lore.items.legendary.non_imbued.NonImbuedLegendaryInstanceAttrs;
import delta.games.lotro.lore.items.legendary.titles.LegendaryTitle;

/**
 * Panel to display the a legendary instance.
 * @author DAM
 */
public class LegendaryInstanceDisplayPanelController
{
  // Data
  private ItemInstance<? extends Item> _itemInstance;
  // UI
  private JPanel _panel;
  private JLabel _name;
  private MultilineLabel2 _passives;
  private MultilineLabel2 _defaultLegacyStats;
  private JPanel _legacies;
  // Controllers
  private LegendaryTitleDisplayPanelController _title;
  private NonImbuedLegendaryAttrsDisplayPanelController _nonImbuedAttrs;
  private ImbuedLegendaryAttrsDisplayPanelController _imbuedAttrs;
  private RelicsSetDisplayController _relics;

  /**
   * Constructor.
   * @param itemInstance Item instance.
   */
  public LegendaryInstanceDisplayPanelController(ItemInstance<? extends Item> itemInstance)
  {
    _itemInstance=itemInstance;
    _name=GuiFactory.buildLabel("");
    _passives=new MultilineLabel2();
    _passives.setBorder(GuiFactory.buildTitledBorder("Passives"));
    _defaultLegacyStats=new MultilineLabel2();
    _legacies=GuiFactory.buildPanel(new BorderLayout());
    _legacies.setBorder(GuiFactory.buildTitledBorder("Legacies"));
    _title=new LegendaryTitleDisplayPanelController();
    _title.getPanel().setBorder(GuiFactory.buildTitledBorder("Title"));
    _nonImbuedAttrs=new NonImbuedLegendaryAttrsDisplayPanelController(itemInstance);
    _imbuedAttrs=new ImbuedLegendaryAttrsDisplayPanelController();
    _relics=new RelicsSetDisplayController();
    _relics.getPanel().setBorder(GuiFactory.buildTitledBorder("Relics"));
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;
    // Name
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    ret.add(_name,c);
    y++;
    // Default legacy stats
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    ret.add(_defaultLegacyStats,c);
    y++;
    // Info panel
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_nonImbuedAttrs.getInfosPanel(),c);
    y++;
    // Title
    JPanel titlePanel=_title.getPanel();
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(titlePanel,c);
    y++;
    // Passives
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_passives,c);
    y++;
    // Legacies
    c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_legacies,c);
    y++;
    // Relics
    JPanel relicsPanel=_relics.getPanel();
    c=new GridBagConstraints(1,3,1,3,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(relicsPanel,c);
    y++;
    return ret;
  }

  /**
   * Update the data to display.
   */
  public void update()
  {
    LegendaryInstance legendaryInstance=(LegendaryInstance)_itemInstance;
    LegendaryInstanceAttrs attrs=legendaryInstance.getLegendaryAttributes();
    // Name
    String name=attrs.getLegendaryName();
    _name.setText(name);
    _name.setVisible(name.length()>0);
    // Default legacy stats
    BasicStatsSet defaultLegacyStats=getDefaultStats(attrs);
    _defaultLegacyStats.setText(StatDisplayUtils.getStatsDisplayLines(defaultLegacyStats));
    // Info panel
    _nonImbuedAttrs.getInfosPanel().setVisible(!attrs.isImbued());
    // Title
    LegendaryTitle title=attrs.getTitle();
    _title.setData(title);
    _title.getPanel().setVisible(title!=null);
    // Passives
    Integer itemLevelInt=_itemInstance.getEffectiveItemLevel();
    int itemLevel=(itemLevelInt!=null)?itemLevelInt.intValue():1;
    BasicStatsSet passives=getPassives(itemLevel,attrs);
    _passives.setText(StatDisplayUtils.getStatsDisplayLines(passives));
    _passives.setVisible(passives.getStatsCount()>0);
    // Legacies
    _nonImbuedAttrs.update();
    _imbuedAttrs.setData(attrs.getImbuedAttrs());
    _legacies.removeAll();
    if (attrs.isImbued())
    {
      _legacies.add(_imbuedAttrs.getPanel(),BorderLayout.CENTER);
    }
    else
    {
      _legacies.add(_nonImbuedAttrs.getLegaciesPanel(),BorderLayout.CENTER);
    }
    // Relics
    _relics.setData(attrs.getRelicsSet());
  }

  private BasicStatsSet getPassives(int itemLevel, LegendaryInstanceAttrs attrs)
  {
    BasicStatsSet ret=new BasicStatsSet();
    List<Effect> passives=attrs.getPassives();
    for(Effect passive : passives)
    {
      StatsProvider statsProvider=passive.getStatsProvider();
      BasicStatsSet singlePassiveStats=statsProvider.getStats(1,itemLevel);
      ret.addStats(singlePassiveStats);
    }
    return ret;
  }

  private BasicStatsSet getDefaultStats(LegendaryInstanceAttrs attrs)
  {
    BasicStatsSet ret;
    boolean isImbued=attrs.isImbued();
    if (isImbued)
    {
      ImbuedLegendaryInstanceAttrs imbuedAttrs=attrs.getImbuedAttrs();
      ImbuedLegacyInstance legacyInstance=imbuedAttrs.getMainLegacy();
      ret=legacyInstance.getStats();
    }
    else
    {
      NonImbuedLegendaryInstanceAttrs nonImbuedAttrs=attrs.getNonImbuedAttrs();
      DefaultNonImbuedLegacyInstance defaultLegacyInstance=nonImbuedAttrs.getDefaultLegacy();
      DefaultNonImbuedLegacy defaultLegacy=defaultLegacyInstance.getLegacy();
      StatsProvider statsProvider=defaultLegacy.getEffect().getStatsProvider();
      int rank=defaultLegacyInstance.getRank();
      ret=statsProvider.getStats(1,rank);
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _itemInstance=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _passives=null;
    _defaultLegacyStats=null;
    if (_legacies!=null)
    {
      _legacies.removeAll();
      _legacies=null;
    }
    if (_title!=null)
    {
      _title.dispose();
      _title=null;
    }
    if (_nonImbuedAttrs!=null)
    {
      _nonImbuedAttrs.dispose();
      _nonImbuedAttrs=null;
    }
    if (_imbuedAttrs!=null)
    {
      _imbuedAttrs.dispose();
      _imbuedAttrs=null;
    }
    if (_relics!=null)
    {
      _relics.dispose();
      _relics=null;
    }
  }
}
