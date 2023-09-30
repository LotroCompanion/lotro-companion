package delta.games.lotro.gui.character.status.achievables.statistics.virtues;

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
import delta.games.lotro.character.status.achievables.statistics.AchievablesStatistics;
import delta.games.lotro.character.status.achievables.statistics.virtues.VirtueXPStatsFromAchievable;
import delta.games.lotro.gui.character.status.achievables.AchievableUIMode;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for the virtue XP display panel.
 * @author DAM
 */
public class VirtueXPDisplayPanelController
{
  // Data
  private AchievablesStatistics _stats;
  // GUI
  private JPanel _panel;
  private JLabel _statsLabel;
  // Controllers
  private VirtueXPFromAchievablesTableController _tableController;
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param stats Stats to show.
   * @param mode UI mode.
   */
  public VirtueXPDisplayPanelController(WindowController parent, AchievablesStatistics stats, AchievableUIMode mode)
  {
    _parent=parent;
    _stats=stats;
    _tableController=new VirtueXPFromAchievablesTableController(stats,mode);
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
    JPanel panel=GuiFactory.buildBackgroundPanel(new BorderLayout());
    TitledBorder border=GuiFactory.buildTitledBorder("Virtue XP"); // I18n
    panel.setBorder(border);

    // Table
    JTable table=_tableController.getTable();
    JScrollPane scroll=GuiFactory.buildScrollPane(table);
    panel.add(scroll,BorderLayout.CENTER);
    // Stats
    JPanel statsPanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
    _statsLabel=GuiFactory.buildLabel("-");
    statsPanel.add(_statsLabel);
    JButton choose=GuiFactory.buildButton(Labels.getLabel("shared.chooseColumns.button"));
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        TableColumnsChooserController<VirtueXPStatsFromAchievable> chooser=new TableColumnsChooserController<VirtueXPStatsFromAchievable>(_parent,_tableController.getTableController());
        chooser.editModal();
      }
    };
    choose.addActionListener(al);
    statsPanel.add(choose);
    panel.add(statsPanel,BorderLayout.NORTH);
    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    updateStatsLabel();
    _tableController.update();
  }

  private void updateStatsLabel()
  {
    int nbItems=_stats.getVirtueXPStats().getEntriesCount();
    String label="Element(s): "+nbItems; // I18n
    _statsLabel.setText(label);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _stats=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _statsLabel=null;
    // Controllers
    _tableController=null;
    _parent=null;
  }
}
