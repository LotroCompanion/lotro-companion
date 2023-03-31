package delta.games.lotro.gui.lore.traits.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.RawTablePanelController;
import delta.common.utils.tables.DataTable;
import delta.common.utils.tables.DataTableRow;
import delta.games.lotro.Config;
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.stats.ScalableStatProvider;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatProvider;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.common.stats.TieredScalableStatProvider;

/**
 * Controller for a panel to display the stats of a trait.
 * @author DAM
 */
public class TraitStatsPanelController
{
  private TraitDescription _trait;
  private JPanel _panel;

  /**
   * Constructor.
   * @param trait Trait to use.
   */
  public TraitStatsPanelController(TraitDescription trait)
  {
    _trait=trait;
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
    StatsProvider statsProvider=_trait.getStatsProvider();
    int nbStats=statsProvider.getNumberOfStatProviders();
    if (nbStats<1)
    {
      return null;
    }
    int nbTiers=_trait.getTiersCount();
    List<RawTablePanelController> tables=new ArrayList<RawTablePanelController>();
    for(int i=0;i<nbTiers;i++)
    {
      DataTable table=buildTable(statsProvider,i+1,nbTiers);
      RawTablePanelController tableController=new RawTablePanelController(table);
      tables.add(tableController);
    }
    if (nbTiers==1)
    {
      JPanel ret=GuiFactory.buildPanel(new BorderLayout());
      ret.add(tables.get(0).getPanel(),BorderLayout.WEST);
      return ret;
    }
    JTabbedPane tab=GuiFactory.buildTabbedPane();
    for(int i=0;i<nbTiers;i++)
    {
      String tabName="Tier "+(i+1);
      JPanel tabPanel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
      GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
      tabPanel.add(tables.get(i).getPanel(),c);
      JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
      c=new GridBagConstraints(1,1,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
      tabPanel.add(paddingPanel,c);
      tab.add(tabName,tabPanel);
    }
    JPanel ret=GuiFactory.buildPanel(new BorderLayout());
    ret.add(tab,BorderLayout.CENTER);
    return ret;
  }

  private DataTable buildTable(StatsProvider statsProvider, int tier, int nbTiers)
  {
    DataTable ret=new DataTable();
    int nbStats=statsProvider.getNumberOfStatProviders();
    // Columns
    ret.addColumn("0","Level",Integer.class,null);
    for(int i=0;i<nbStats;i++)
    {
      StatProvider statProvider=statsProvider.getStatProvider(i);
      StatDescription stat=statProvider.getStat();
      String statName=stat.getName();
      ret.addColumn(String.valueOf(i+1),statName,String.class,null);
    }
    // Rows
    int levelCap=Config.getInstance().getMaxCharacterLevel();
    for(int i=1;i<=levelCap;i++)
    {
      DataTableRow row=ret.addRow();
      row.setData(0,Integer.valueOf(i));
      for(int statIndex=0;statIndex<nbStats;statIndex++)
      {
        StatProvider statProvider=statsProvider.getStatProvider(statIndex);
        Float rawValue=getStatValue(i,tier,nbTiers,statProvider);
        if (rawValue!=null)
        {
          StatDescription stat=statProvider.getStat();
          String cell=StatUtils.getStatDisplay(rawValue,stat);
          row.setData(statIndex+1,cell);
        }
      }
    }
    return ret;
  }

  private Float getStatValue(int level, int tier, int nbTiers, StatProvider provider)
  {
    Float value=null;
    if (provider instanceof TieredScalableStatProvider)
    {
      value=provider.getStatValue(tier,level);
    }
    else if (provider instanceof ScalableStatProvider)
    {
      ScalableStatProvider scalableStatProvider=(ScalableStatProvider)provider;
      if (nbTiers>1)
      {
        value=scalableStatProvider.getStatValue(1,tier);
      }
      else
      {
        value=scalableStatProvider.getStatValue(1,level);
      }
    }
    else
    {
      value=provider.getStatValue(1,level);
    }
    return value;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _trait=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
