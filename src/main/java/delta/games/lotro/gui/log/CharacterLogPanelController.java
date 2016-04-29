package delta.games.lotro.gui.log;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Controller the character log panel.
 * @author DAM
 */
public class CharacterLogPanelController
{
  // Data
  private CharacterLogTableController _tableController;
  // GUI
  private JPanel _panel;
  private JLabel _statsLabel;

  /**
   * Constructor.
   * @param tableController Associated log table controller.
   */
  public CharacterLogPanelController(CharacterLogTableController tableController)
  {
    _tableController=tableController;
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
    TitledBorder logFrameBorder=GuiFactory.buildTitledBorder("Log");
    panel.setBorder(logFrameBorder);

    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    panel.add(scroll,BorderLayout.CENTER);
    // Stats
    JPanel statsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    _statsLabel=GuiFactory.buildLabel("-");
    statsPanel.add(_statsLabel);
    panel.add(statsPanel,BorderLayout.NORTH);
    return panel;
  }

  /**
   * Set a new character log.
   * @param log Character log to set.
   */
  public void setLog(CharacterLog log)
  {
    _tableController.setLog(log);
    updateStatsLabel();
  }

  /**
   * Update filter.
   */
  public void updateFilter()
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
      label="Item(s): "+nbItems;
    }
    else
    {
      label="Item(s): "+nbFiltered+"/"+nbItems;
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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _statsLabel=null;
  }
}
