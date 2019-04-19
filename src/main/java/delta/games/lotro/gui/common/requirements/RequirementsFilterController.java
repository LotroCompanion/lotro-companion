package delta.games.lotro.gui.common.requirements;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Race;
import delta.games.lotro.common.requirements.filters.UsageRequirementFilter;
import delta.games.lotro.gui.character.summary.CharacterUiUtils;
import delta.games.lotro.gui.items.FilterUpdateListener;

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
  private ComboBoxController<CharacterClass> _class;
  private ComboBoxController<Race> _race;

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
  }

  /**
   * Apply current filter into the managed gadgets.
   */
  public void setFilter()
  {
    // - class
    CharacterClass requiredClass=_filter.getCharacterClass();
    _class.selectItem(requiredClass);
    // - race
    Race requiredRace=_filter.getRace();
    _race.selectItem(requiredRace);
  }

  private JPanel buildRequirementsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    int y=0;

    GridBagConstraints c;
    {
      JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
      // Class
      linePanel.add(GuiFactory.buildLabel("Class:"));
      _class=buildCharacterClassCombobox();
      linePanel.add(_class.getComboBox());
      // Race
      linePanel.add(GuiFactory.buildLabel("Race:"));
      _race=buildRaceCombobox();
      linePanel.add(_race.getComboBox());
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(5,0,0,0),0,0);
      panel.add(linePanel,c);
      y++;
    }

    return panel;
  }

  private ComboBoxController<CharacterClass> buildCharacterClassCombobox()
  {
    ComboBoxController<CharacterClass> combo=CharacterUiUtils.buildClassCombo(true);
    ItemSelectionListener<CharacterClass> listener=new ItemSelectionListener<CharacterClass>()
    {
      @Override
      public void itemSelected(CharacterClass requiredClass)
      {
        _filter.setCharacterClass(requiredClass);
        filterUpdated();
      }
    };
    combo.addListener(listener);
    return combo;
  }

  private ComboBoxController<Race> buildRaceCombobox()
  {
    ComboBoxController<Race> combo=CharacterUiUtils.buildRaceCombo(true);
    ItemSelectionListener<Race> listener=new ItemSelectionListener<Race>()
    {
      @Override
      public void itemSelected(Race requiredRace)
      {
        _filter.setRace(requiredRace);
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
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
