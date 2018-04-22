package delta.games.lotro.gui.stats.deeds;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.TableColumnsChooserController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.gui.stats.deeds.statistics.DeedStatisticsWindowController;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.stats.deeds.DeedsStatusManager;

/**
 * Controller the deeds status edition panel.
 * @author DAM
 */
public class DeedsStatusEditionPanelController implements FilterUpdateListener
{
  // Data
  private DeedStatusTableController _tableController;
  private DeedsStatusManager _status;
  // GUI
  private JPanel _panel;
  private JLabel _statsLabel;
  private WindowsManager _windowsManager;
  // Controllers
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param tableController Associated table controller.
   * @param status Deeds status to edit.
   */
  public DeedsStatusEditionPanelController(WindowController parent, DeedStatusTableController tableController, DeedsStatusManager status)
  {
    _parent=parent;
    _status=status;
    _tableController=tableController;
    _windowsManager=new WindowsManager();
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
    }
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    TitledBorder itemsFrameBorder=GuiFactory.buildTitledBorder("Status of deeds");
    panel.setBorder(itemsFrameBorder);

    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    panel.add(scroll,BorderLayout.CENTER);
    // Stats
    JPanel statsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    _statsLabel=GuiFactory.buildLabel("-");
    statsPanel.add(_statsLabel);
    // - choose columns button
    JButton choose=GuiFactory.buildButton("Choose columns...");
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        TableColumnsChooserController<DeedDescription> chooser=new TableColumnsChooserController<DeedDescription>(_parent,_tableController.getTableController());
        chooser.editModal();
      }
    };
    choose.addActionListener(al);
    statsPanel.add(choose);
    // - statistics button
    JButton showStatsButton=GuiFactory.buildButton("Statistics...");
    al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        doShowStatistics();
      }
    };
    showStatsButton.addActionListener(al);
    statsPanel.add(showStatsButton);
    // Statistics button
    panel.add(statsPanel,BorderLayout.NORTH);
    return panel;
  }

  private void doShowStatistics()
  {
    DeedStatisticsWindowController summaryController=(DeedStatisticsWindowController)_windowsManager.getWindow(DeedStatisticsWindowController.IDENTIFIER);
    if (summaryController==null)
    {
      summaryController=new DeedStatisticsWindowController(_parent);
      summaryController.updateStats(_status);
      _windowsManager.registerWindow(summaryController);
      summaryController.getWindow().setLocationRelativeTo(_parent.getWindow());
    }
    summaryController.bringToFront();
  }

  /**
   * Update statistics using the given deeds status.
   * @param status Deeds status to use.
   */
  public void updateStats(DeedsStatusManager status)
  {
    DeedStatisticsWindowController summaryController=(DeedStatisticsWindowController)_windowsManager.getWindow(DeedStatisticsWindowController.IDENTIFIER);
    if (summaryController!=null)
    {
      summaryController.updateStats(status);
    }
  }

  /**
   * Update filter.
   */
  public void filterUpdated()
  {
    _tableController.updateFilter();
    updateStatsLabel();
  }

  private void updateStatsLabel()
  {
    int nbFiltered=_tableController.getNbFilteredItems();
    int nbItems=_tableController.getNbItems();
    String label="";
    if (nbFiltered==nbItems)
    {
      label="Deed(s): "+nbItems;
    }
    else
    {
      label="Deed(s): "+nbFiltered+"/"+nbItems;
    }
    _statsLabel.setText(label);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _tableController=null;
    _status=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _statsLabel=null;
    if (_windowsManager!=null)
    {
      _windowsManager.disposeAll();
      _windowsManager=null;
    }
    // Controllers
    _parent=null;
  }
}
