package delta.games.lotro.gui.character.status.recipes;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.TableColumnsChooserController;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.recipes.RecipeStatus;
import delta.games.lotro.gui.character.status.recipes.table.RecipeStatusTableController;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

/**
 * Controller the recipes status display panel.
 * @author DAM
 */
public class RecipesStatusPanelController implements FilterUpdateListener
{
  // Data
  private RecipeStatusTableController _tableController;
  // GUI
  private JPanel _panel;
  private JLabel _statsLabel;
  // Controllers
  private WindowController _parent;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param tableController Associated table controller.
   */
  public RecipesStatusPanelController(WindowController parent, RecipeStatusTableController tableController)
  {
    _parent=parent;
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
    panel.setBorder(GuiFactory.buildTitledBorder("Status of recipes"));

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
        TableColumnsChooserController<RecipeStatus> chooser=new TableColumnsChooserController<RecipeStatus>(_parent,_tableController.getTableController());
        chooser.editModal();
      }
    };
    choose.addActionListener(al);
    statsPanel.add(choose);
    panel.add(statsPanel,BorderLayout.NORTH);
    return panel;
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
      label="Recipe(s): "+nbItems;
    }
    else
    {
      label="Recipe(s): "+nbFiltered+"/"+nbItems;
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
    // Controllers
    _parent=null;
  }
}