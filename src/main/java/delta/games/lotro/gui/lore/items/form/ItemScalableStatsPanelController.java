package delta.games.lotro.gui.lore.items.form;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.RawTablePanelController;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.money.Money;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.scaling.ItemScaling;
import delta.games.lotro.lore.items.scaling.ItemScalingBuilder;
import delta.games.lotro.lore.items.scaling.ItemScalingEntry;

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
    ItemScaling scaling=ItemScalingBuilder.build(_item);
    if (scaling.getEntries().size()==0)
    {
      return null;
    }
    JPanel panel=null;
    List<StatDescription> stats=scaling.getStats();

    // Headers
    List<String> headers=new ArrayList<String>();
    headers.add("Level(s)");
    headers.add("Item level");
    for(StatDescription stat : stats)
    {
      headers.add(stat.getName());
    }
    headers.add("Value");
    // Rows
    List<Object[]> rows=new ArrayList<Object[]>();
    List<Integer> itemLevels=scaling.getItemLevels();
    for(Integer itemLevel : itemLevels)
    {
      List<ItemScalingEntry> entries=scaling.getEntriesForItemLevel(itemLevel.intValue());
      if (entries.size()==0)
      {
        continue;
      }
      Object[] row=new Object[headers.size()];
      int min=entries.get(0).getLevel();
      int max=entries.get(entries.size()-1).getLevel();
      row[0]=(min!=max)?String.valueOf(min)+"-"+max:String.valueOf(min);
      row[1]=itemLevel;
      ItemScalingEntry entry=entries.get(0);
      BasicStatsSet values=entry.getStats();
      int index=2;
      for(StatDescription stat : stats)
      {
        Number value=values.getStat(stat);
        row[index]=StatUtils.getStatDisplay(value,stat.isPercentage());
        index++;
      }
      Money money=entry.getMoney();
      row[index]=(money!=null)?money.getShortLabel():"-";
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
