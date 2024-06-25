package delta.games.lotro.gui.lore.virtues.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.RawTablePanelController;
import delta.common.utils.tables.DataTable;
import delta.common.utils.tables.DataTableRow;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.virtues.VirtuesContributionsMgr;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatProvider;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.parameters.Game;

/**
 * Controller for a panel to display the stats of a virtue.
 * @author DAM
 */
public class VirtueStatsPanelController
{
  private VirtueDescription _virtue;
  private JPanel _panel;

  /**
   * Constructor.
   * @param virtue Virtue to use.
   */
  public VirtueStatsPanelController(VirtueDescription virtue)
  {
    _virtue=virtue;
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    StatsProvider statsProvider=_virtue.getStatsProvider();
    int nbStats=statsProvider.getStatProviders().size();
    if (nbStats<1)
    {
      return null;
    }
    DataTable table=buildTable();
    RawTablePanelController tableController=new RawTablePanelController(table);
    // Header cells
    {
      StatsProvider activeStatsProvider=_virtue.getStatsProvider();
      int nbActiveStats=activeStatsProvider.getStatProviders().size();
      if (nbActiveStats>0)
      {
        String label=(nbActiveStats>1)?"Active bonuses":"Passive bonus";
        tableController.setHeaderCell(1,nbActiveStats,label,Color.LIGHT_GRAY);
      }
      StatsProvider passiveStatsProvider=_virtue.getPassiveStatsProvider();
      int nbPassiveStats=passiveStatsProvider.getStatProviders().size();
      if (nbPassiveStats>0)
      {
        String label=(nbPassiveStats>1)?"Passive bonuses":"Passive bonus";
        tableController.setHeaderCell(1+nbActiveStats,nbPassiveStats,label,Color.LIGHT_GRAY);
      }
    }
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    ret.add(tableController.getPanel(),BorderLayout.WEST);
    return ret;
  }

  private DataTable buildTable()
  {
    DataTable ret=new DataTable();
    // Columns
    ret.addColumn("0","Rank",Integer.class,null);
    StatsProvider activeStatsProvider=_virtue.getStatsProvider();
    List<StatProvider> activeStatProviders=activeStatsProvider.getStatProviders();
    int nbActiveStats=activeStatProviders.size();
    for(int i=0;i<nbActiveStats;i++)
    {
      StatProvider statProvider=activeStatProviders.get(i);
      StatDescription stat=statProvider.getStat();
      String statName=stat.getName();
      ret.addColumn(String.valueOf(i+1),statName,String.class,null);
    }
    StatsProvider passiveStatsProvider=_virtue.getPassiveStatsProvider();
    List<StatProvider> passiveStatProviders=passiveStatsProvider.getStatProviders();
    int nbPassiveStats=passiveStatProviders.size();
    for(int i=0;i<nbPassiveStats;i++)
    {
      StatProvider statProvider=passiveStatProviders.get(i);
      StatDescription stat=statProvider.getStat();
      String statName=stat.getName();
      ret.addColumn(String.valueOf(nbActiveStats+i+1),statName,String.class,null);
    }

    VirtuesContributionsMgr mgr=VirtuesContributionsMgr.get();
    // Rows
    int maxTier=Game.getParameters().getMaxVirtueRank();
    for(int i=0;i<=maxTier;i++)
    {
      DataTableRow row=ret.addRow();
      row.setData(0,Integer.valueOf(i));
      BasicStatsSet activeStats=mgr.getContribution(_virtue,i,false);
      for(int statIndex=0;statIndex<nbActiveStats;statIndex++)
      {
        StatProvider statProvider=activeStatProviders.get(statIndex);
        StatDescription stat=statProvider.getStat();
        Number value=activeStats.getStat(stat);
        if (value!=null)
        {
          String cell=StatUtils.getStatDisplay(value,stat);
          row.setData(statIndex+1,cell);
        }
      }
      BasicStatsSet passiveStats=mgr.getContribution(_virtue,i,true);
      for(int statIndex=0;statIndex<nbPassiveStats;statIndex++)
      {
        StatProvider statProvider=passiveStatProviders.get(statIndex);
        StatDescription stat=statProvider.getStat();
        Number value=passiveStats.getStat(stat);
        if (value!=null)
        {
          String cell=StatUtils.getStatDisplay(value,stat);
          row.setData(statIndex+1+nbActiveStats,cell);
        }
      }
    }
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _virtue=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
