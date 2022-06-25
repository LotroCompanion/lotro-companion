package delta.games.lotro.gui.lore.items.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.RawTablePanelController;
import delta.games.lotro.Config;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatProvider;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.scaling.Munging;
import delta.games.lotro.utils.maths.Progression;

/**
 * Controller for a panel to display a table of the scalable stats of an item.
 * @author DAM
 */
public class ItemScalableStatsPanelController
{
  private Item _item;

  // GUI
  private JPanel _panel;

  /**
   * Constructor.
   * @param item Item to show.
   */
  public ItemScalableStatsPanelController(Item item)
  {
    _item=item;
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel or <code>null</code> if no scaling.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=null;
    StatsProvider statsProvider=_item.getStatsProvider();
    if (statsProvider==null)
    {
      return null;
    }
    Munging munging=_item.getMunging();
    if (munging==null)
    {
      return null;
    }
    Progression progression=munging.getProgression();
    if (progression==null)
    {
      return null;
    }
    Integer min=munging.getMin();
    int minLevel=(min!=null?min.intValue():1);
    Integer max=munging.getMax();
    int levelCap=Config.getInstance().getMaxCharacterLevel();
    int maxLevel=(max!=null?max.intValue():levelCap);

    List<StatDescription> stats=getStats(statsProvider);
    List<Integer> itemLevels=getItemLevels(minLevel,maxLevel,progression);

    // Headers
    List<String> headers=new ArrayList<String>();
    headers.add("Level(s)");
    headers.add("Item level");
    for(StatDescription stat : stats)
    {
      headers.add(stat.getName());
    }
    // Rows
    List<Object[]> rows=new ArrayList<Object[]>();
    for(Integer itemLevel : itemLevels)
    {
      Object[] row=new Object[headers.size()];
      row[0]=getLevelsForItemLevel(minLevel,maxLevel,progression,itemLevel.intValue());
      row[1]=itemLevel;
      BasicStatsSet values=statsProvider.getStats(1,itemLevel.intValue());
      int index=2;
      for(StatDescription stat : stats)
      {
        Number value=values.getStat(stat);
        row[index]=StatUtils.getStatDisplay(value,stat.isPercentage());
        index++;
      }
      rows.add(row);
    }
    RawTablePanelController tableController=new RawTablePanelController(headers,rows);
    panel=tableController.getPanel();
    padPanel(panel,headers.size(),rows.size()+1);
    return panel;
  }

  private void padPanel(JPanel panel, int nbCellsX, int nbCellsY)
  {
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    GridBagConstraints c=new GridBagConstraints(nbCellsX,nbCellsY,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(paddingPanel,c);
  }

  private List<StatDescription> getStats(StatsProvider statsProvider)
  {
    List<StatDescription> stats=new ArrayList<StatDescription>();
    int nbStats=statsProvider.getNumberOfStatProviders();
    for(int i=0;i<nbStats;i++)
    {
      StatProvider statProvider=statsProvider.getStatProvider(i);
      StatDescription stat=statProvider.getStat();
      stats.add(stat);
    }
    return stats;
  }

  private String getLevelsForItemLevel(int minLevel, int maxLevel, Progression progression, int itemLevel)
  {
    List<Integer> levels=new ArrayList<Integer>();
    for(int level=minLevel;level<=maxLevel;level++)
    {
      Float itemLevelForLevel=progression.getValue(level);
      if ((itemLevelForLevel!=null) && (itemLevelForLevel.intValue()==itemLevel))
      {
        levels.add(Integer.valueOf(level));
      }
    }
    int nbLevels=levels.size();
    if (nbLevels==1)
    {
      return levels.get(0).toString();
    }
    else if (nbLevels>1)
    {
      return levels.get(0)+"-"+levels.get(nbLevels-1);
    }
    return "";
  }

  private List<Integer> getItemLevels(int minLevel, int maxLevel, Progression progression)
  {
    Set<Integer> itemLevels=new HashSet<Integer>();
    for(int level=minLevel;level<=maxLevel;level++)
    {
      Float itemLevel=progression.getValue(level);
      if (itemLevel!=null)
      {
        itemLevels.add(Integer.valueOf(itemLevel.intValue()));
      }
    }
    List<Integer> ret=new ArrayList<Integer>(itemLevels);
    Collections.sort(ret);
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _item=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
