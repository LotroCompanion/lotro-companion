package delta.games.lotro.gui.character.status.reputation.synopsis;

import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.lore.reputation.FactionFilter;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Controller for a faction filter edition panel.
 * @author DAM
 */
public class ReputationSynopsisFilterController
{
  // Data
  private FactionFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<String> _category;
  private ReputationSynopsisPanelController _panelController;

  /**
   * Constructor.
   * @param filter Faction filter.
   * @param panelController Associated panel controller.
   */
  public ReputationSynopsisFilterController(FactionFilter filter, ReputationSynopsisPanelController panelController)
  {
    _filter=filter;
    _panelController=panelController;
  }

  private void updateFilter()
  {
    _panelController.updateFilter();
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
      setFilter();
      updateFilter();
    }
    return _panel;
  }

  private void setFilter()
  {
    String category=_filter.getCategory();
    _category.selectItem(category);
  }

  private String[] getCategories()
  {
    FactionsRegistry registry=FactionsRegistry.getInstance();
    List<String> categories=registry.getFactionCategories();
    categories.remove("Guild"); // I18n
    String[] ret=categories.toArray(new String[categories.size()]);
    return ret;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));

    // Regions
    String[] categories=getCategories();
    _category=new ComboBoxController<String>();
    _category.addEmptyItem(" ");
    for(String category : categories)
    {
      _category.addItem(category,category);
    }
    ItemSelectionListener<String> categoryListener=new ItemSelectionListener<String>()
    {
      @Override
      public void itemSelected(String region)
      {
        _filter.setCategory(region);
        updateFilter();
      }
    };
    _category.addListener(categoryListener);
    panel.add(GuiFactory.buildLabel("Category:")); // I18n
    panel.add(_category.getComboBox());
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    _panelController=null;
    if (_category!=null)
    {
      _category.dispose();
      _category=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
