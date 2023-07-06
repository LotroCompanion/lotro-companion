package delta.games.lotro.gui.character.status.crafting.synopsis;

import java.awt.FlowLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.lore.crafting.CraftingData;
import delta.games.lotro.lore.crafting.CraftingSystem;
import delta.games.lotro.lore.crafting.Profession;
import delta.games.lotro.lore.crafting.ProfessionComparator;
import delta.games.lotro.lore.crafting.ProfessionFilter;
import delta.games.lotro.lore.crafting.Professions;

/**
 * Controller for a crafting filter edition panel.
 * @author DAM
 */
public class CraftingSynopsisFilterController
{
  // Data
  private ProfessionFilter _filter;
  // GUI
  private JPanel _panel;
  private ComboBoxController<Profession> _professions;
  private CraftingSynopsisPanelController _panelController;

  /**
   * Constructor.
   * @param filter Filter.
   * @param panelController Associated panel controller.
   */
  public CraftingSynopsisFilterController(ProfessionFilter filter, CraftingSynopsisPanelController panelController)
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
    Profession profession=_filter.getProfession();
    _professions.selectItem(profession);
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING));

    // Professions
    CraftingData crafting=CraftingSystem.getInstance().getData();
    Professions professionsRegistry=crafting.getProfessionsRegistry();
    List<Profession> professions=professionsRegistry.getAll();
    Collections.sort(professions,new ProfessionComparator());
    _professions=new ComboBoxController<Profession>();
    _professions.addEmptyItem(" ");
    for(Profession profession : professions)
    {
      _professions.addItem(profession,profession.getName());
    }
    ItemSelectionListener<Profession> listener=new ItemSelectionListener<Profession>()
    {
      @Override
      public void itemSelected(Profession profession)
      {
        _filter.setProfession(profession);
        updateFilter();
      }
    };
    _professions.addListener(listener);
    panel.add(GuiFactory.buildLabel("Profession:")); // I18n
    panel.add(_professions.getComboBox());
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
    if (_professions!=null)
    {
      _professions.dispose();
      _professions=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
