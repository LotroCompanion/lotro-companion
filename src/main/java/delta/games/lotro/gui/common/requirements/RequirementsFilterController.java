package delta.games.lotro.gui.common.requirements;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.common.requirements.filters.UsageRequirementFilter;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.common.crafting.CraftingUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.lore.crafting.Profession;

/**
 * Controller for a requirements filter edition panel.
 * @author DAM
 */
public class RequirementsFilterController
{
  // Data
  private UsageRequirementFilter _filter;
  private FilterUpdateListener _filterUpdateListener;

  // GUI
  private JPanel _panel;

  // Controllers
  private ComboBoxController<ClassDescription> _class;
  private ComboBoxController<RaceDescription> _race;
  private ComboBoxController<Profession> _profession;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public RequirementsFilterController(UsageRequirementFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildRequirementsPanel();
      setFilter();
      filterUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  private void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  /**
   * Reset all gadgets.
   */
  public void reset()
  {
    _class.selectItem(null);
    _race.selectItem(null);
    _profession.selectItem(null);
  }

  /**
   * Apply current filter into the managed gadgets.
   */
  public void setFilter()
  {
    // - class
    ClassDescription requiredClass=_filter.getCharacterClassFilter().getCharacterClass();
    _class.selectItem(requiredClass);
    // - race
    RaceDescription requiredRace=_filter.getRaceFilter().getRace();
    _race.selectItem(requiredRace);
    // - profession
    Profession requiredProfession=_filter.getProfessionFilter().getProfession();
    _profession.selectItem(requiredProfession);
  }

  private JPanel buildRequirementsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Class
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("requirements.filter.class")));
      _class=buildCharacterClassCombobox();
      linePanel.add(_class.getComboBox());
      // Race
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("requirements.filter.race")));
      _race=buildRaceCombobox();
      linePanel.add(_race.getComboBox());
      // Profession
      linePanel.add(GuiFactory.buildLabel(Labels.getFieldLabel("requirements.filter.profession")));
      _profession=buildProfessionCombobox();
      linePanel.add(_profession.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,5,0),0,0);
      panel.add(linePanel,c);
    }

    return panel;
  }

  private ComboBoxController<ClassDescription> buildCharacterClassCombobox()
  {
    ComboBoxController<ClassDescription> combo=CharacterUiUtils.buildCharacterClassCombo(true);
    ItemSelectionListener<ClassDescription> listener=new ItemSelectionListener<ClassDescription>()
    {
      @Override
      public void itemSelected(ClassDescription requiredClass)
      {
        _filter.getCharacterClassFilter().setCharacterClass(requiredClass);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<RaceDescription> buildRaceCombobox()
  {
    ComboBoxController<RaceDescription> combo=CharacterUiUtils.buildRaceCombo(true);
    ItemSelectionListener<RaceDescription> listener=new ItemSelectionListener<RaceDescription>()
    {
      @Override
      public void itemSelected(RaceDescription requiredRace)
      {
        _filter.getRaceFilter().setRace(requiredRace);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Profession> buildProfessionCombobox()
  {
    ComboBoxController<Profession> combo=CraftingUiUtils.buildProfessionCombo();
    ItemSelectionListener<Profession> listener=new ItemSelectionListener<Profession>()
    {
      @Override
      public void itemSelected(Profession requiredProfession)
      {
        _filter.getProfessionFilter().setProfession(requiredProfession);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    if (_class!=null)
    {
      _class.dispose();
      _class=null;
    }
    if (_race!=null)
    {
      _race.dispose();
      _race=null;
    }
    if (_profession!=null)
    {
      _profession.dispose();
      _profession=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
