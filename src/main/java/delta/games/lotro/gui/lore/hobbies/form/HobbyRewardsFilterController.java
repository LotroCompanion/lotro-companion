package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewards;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewardsProfile;
import delta.games.lotro.lore.maps.Territory;

/**
 * Controller for a 'hobby rewards' filter edition panel.
 * @author DAM
 */
public class HobbyRewardsFilterController
{
  // Data
  private HobbyDescription _hobby;
  private HobbyRewardsSelection _filter;
  // GUI
  private JPanel _panel;
  // -- Mount attributes UI --
  private ComboBoxController<Territory> _territory;
  private ComboBoxController<Integer> _proficiency;
  // Controllers
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param hobby Hobby to use.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public HobbyRewardsFilterController(HobbyDescription hobby, HobbyRewardsSelection filter, FilterUpdateListener filterUpdateListener)
  {
    _hobby=hobby;
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public HobbyRewardsSelection getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=buildPanel();
      setFilter();
      filterUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  protected void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  private void setFilter()
  {
    // Territory
    Territory territory=_filter.getTerritory();
    _territory.selectItem(territory);
    // Proficiency
    Integer proficiency=_filter.getProficiency();
    _proficiency.selectItem(proficiency);
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel line1Panel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Territory
    {
      JLabel label=GuiFactory.buildLabel("Territory:"); // I18n
      line1Panel.add(label);
      _territory=HobbiesUiUtils.buildTerritoryCombo(_hobby);
      ItemSelectionListener<Territory> categoryListener=new ItemSelectionListener<Territory>()
      {
        @Override
        public void itemSelected(Territory territory)
        {
          _filter.setTerritory(territory);
          updateProficienciesCombo(territory);
          _filter.setProficiency(_proficiency.getSelectedItem());
          filterUpdated();
        }
      };
      _territory.addListener(categoryListener);
      line1Panel.add(_territory.getComboBox());
    }
    // Proficiencies
    {
      JLabel label=GuiFactory.buildLabel("Proficiency:"); // I18n
      line1Panel.add(label);
      _proficiency=new ComboBoxController<Integer>();
      ItemSelectionListener<Integer> proficiencyListener=new ItemSelectionListener<Integer>()
      {
        @Override
        public void itemSelected(Integer proficiency)
        {
          _filter.setProficiency(proficiency);
          filterUpdated();
        }
      };
      _proficiency.addListener(proficiencyListener);
      updateProficienciesCombo(_territory.getSelectedItem());
      line1Panel.add(_proficiency.getComboBox());
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(line1Panel,c);
    y++;

    return panel;
  }

  private void updateProficienciesCombo(Territory territory)
  {
    HobbyRewards rewards=_hobby.getRewards();
    HobbyRewardsProfile profile=null;
    if (territory!=null)
    {
      profile=rewards.getProfile(territory.getIdentifier());
    }
    HobbiesUiUtils.updateProficienciesCombo(_proficiency,profile);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // Controllers
    if (_territory!=null)
    {
      _territory.dispose();
      _territory=null;
    }
    if (_proficiency!=null)
    {
      _proficiency.dispose();
      _proficiency=null;
    }
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
