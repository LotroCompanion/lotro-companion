package delta.games.lotro.gui.character.storage.own;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.character.storage.StoredItem;
import delta.games.lotro.gui.character.storage.StorageFilter;
import delta.games.lotro.gui.character.storage.cosmetics.SameCosmeticsWindowController;
import delta.games.lotro.gui.character.storage.statistics.StorageStatisticsWindowController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

/**
 * Controller for a panel to show the storage buttons.
 * @author DAM
 */
public class StorageButtonsPanelController implements FilterUpdateListener
{
  // Data
  private StorageFilter _filter;
  private List<StoredItem> _items;
  // Controllers
  private WindowController _parent;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param filter Filter.
   * @param items Items to show.
   */
  public StorageButtonsPanelController(WindowController parent, StorageFilter filter, List<StoredItem> items)
  {
    _parent=parent;
    _filter=filter;
    _items=items;
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

  private WindowsManager getWindowsManager()
  {
    return _parent.getWindowsManager();
  }

  private JPanel build()
  {
    // Stats button
    JButton statsButton;
    {
      statsButton=GuiFactory.buildButton("Statistics"); // I18n
      statsButton.setToolTipText("Statistics on the selected items..."); // I18n
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          showStatistics();
        }
      };
      statsButton.addActionListener(al);
    }
    // Cosmetics button
    JButton cosmeticsButton;
    {
      cosmeticsButton=GuiFactory.buildButton("Cosmetics"); // I18n
      cosmeticsButton.setToolTipText("A tool to find items with same look among the selected items..."); // I18n
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          showSameCosmetics();
        }
      };
      cosmeticsButton.addActionListener(al);
    }
    JPanel buttonsPanel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,5,0),0,0);
    buttonsPanel.add(statsButton,c);
    c=new GridBagConstraints(0,1,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    buttonsPanel.add(cosmeticsButton,c);
    return buttonsPanel;
  }

  private StorageStatisticsWindowController getStatisticsWindow()
  {
    WindowsManager windowsMgr=getWindowsManager();
    StorageStatisticsWindowController ret=(StorageStatisticsWindowController)windowsMgr.getWindow(StorageStatisticsWindowController.IDENTIFIER);
    return ret;
  }

  private void showStatistics()
  {
    StorageStatisticsWindowController statisticsController=getStatisticsWindow();
    if (statisticsController==null)
    {
      statisticsController=new StorageStatisticsWindowController(_parent);
      WindowsManager windowsMgr=getWindowsManager();
      windowsMgr.registerWindow(statisticsController);
      statisticsController.getWindow().setLocationRelativeTo(_parent.getWindow());
      List<StoredItem> toUse=getFilteredItems();
      statisticsController.updateDisplay(toUse);
    }
    statisticsController.bringToFront();
  }

  private SameCosmeticsWindowController getSameCosmeticsWindow()
  {
    WindowsManager windowsMgr=getWindowsManager();
    SameCosmeticsWindowController ret=(SameCosmeticsWindowController)windowsMgr.getWindow(SameCosmeticsWindowController.IDENTIFIER);
    return ret;
  }

  private void showSameCosmetics()
  {
    SameCosmeticsWindowController sameCosmeticsController=getSameCosmeticsWindow();
    if (sameCosmeticsController==null)
    {
      sameCosmeticsController=new SameCosmeticsWindowController(_parent);
      WindowsManager windowsMgr=getWindowsManager();
      windowsMgr.registerWindow(sameCosmeticsController);
      sameCosmeticsController.getWindow().setLocationRelativeTo(_parent.getWindow());
      List<StoredItem> toUse=getFilteredItems();
      sameCosmeticsController.updateDisplay(toUse);
    }
    sameCosmeticsController.bringToFront();
  }

  /**
   * Update the managed items.
   * @param items Items to use.
   */
  public void update(List<StoredItem> items)
  {
    _items.clear();
    _items.addAll(items);
    filterUpdated();
  }

  @Override
  public void filterUpdated()
  {
    StorageStatisticsWindowController statisticsWindow=getStatisticsWindow();
    SameCosmeticsWindowController sameCosmeticsWindow=getSameCosmeticsWindow();
    if ((statisticsWindow!=null) || (sameCosmeticsWindow!=null))
    {
      List<StoredItem> toUse=getFilteredItems();
      if (statisticsWindow!=null)
      {
        statisticsWindow.updateDisplay(toUse);
      }
      if (sameCosmeticsWindow!=null)
      {
        sameCosmeticsWindow.updateDisplay(toUse);
      }
    }
  }

  private List<StoredItem> getFilteredItems()
  {
    List<StoredItem> toUse=new ArrayList<StoredItem>();
    for(StoredItem item : _items)
    {
      if ((_filter!=null) && (_filter.accept(item)))
      {
        toUse.add(item);
      }
    }
    return toUse;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _parent=null;
    _filter=null;
    _items=null;
  }
}
