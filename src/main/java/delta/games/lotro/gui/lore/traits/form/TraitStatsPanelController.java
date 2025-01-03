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
import delta.games.lotro.character.traits.TraitDescription;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatProvider;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.common.stats.StatsProvider;
import delta.games.lotro.lore.parameters.Game;

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
    List<StatProvider> statProviders=statsProvider.getStatProviders();
    if (statProviders.isEmpty())
    {
      return null;
    }
    int nbTiers=_trait.getTiersCount();
    List<RawTablePanelController> tables=new ArrayList<RawTablePanelController>();
    for(int i=0;i<nbTiers;i++)
    {
      DataTable table=buildTable(statProviders,i+1,nbTiers);
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

  private DataTable buildTable(List<StatProvider> statProviders, int tier, int nbTiers)
  {
    DataTable ret=new DataTable();
    int nbStats=statProviders.size();
    // Columns
    ret.addColumn("0","Level",Integer.class,null);
    for(int i=0;i<nbStats;i++)
    {
      StatProvider statProvider=statProviders.get(i);
      StatDescription stat=statProvider.getStat();
      String statName=stat.getName();
      ret.addColumn(String.valueOf(i+1),statName,String.class,null);
    }
    // Rows
    int levelCap=Game.getParameters().getMaxCharacterLevel();
    for(int i=1;i<=levelCap;i++)
    {
      DataTableRow row=ret.addRow();
      row.setData(0,Integer.valueOf(i));
      for(int statIndex=0;statIndex<nbStats;statIndex++)
      {
        StatProvider statProvider=statProviders.get(statIndex);
        Float rawValue=StatUtils.getStatValue(i,tier,nbTiers,statProvider);
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
