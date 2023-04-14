package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.tables.panel.FilterUpdateListener;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.hobbies.rewards.HobbyDropTable;
import delta.games.lotro.lore.hobbies.rewards.HobbyDropTableEntry;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewards;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewardsProfile;
import delta.games.lotro.lore.maps.GeoAreasManager;
import delta.games.lotro.lore.maps.Territory;

/**
 * Controller for a panel to display the rewards of a hobby:
 * <ul>
 * <li>a territory/proficiency chooser,
 * <li>a drop table display
 * </ul>
 * @author DAM
 */
public class HobbyRewardsPanelController implements FilterUpdateListener
{
  // Data
  private HobbyDescription _hobby;
  private HobbyRewardsSelection _filter;
  // Controllers
  private HobbyRewardsFilterController _filterController;
  private HobbyDropTablePanelController _tableController;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param hobby Hobby to use.
   */
  public HobbyRewardsPanelController(WindowController parent, HobbyDescription hobby)
  {
    _hobby=hobby;
    _filter=new HobbyRewardsSelection();
    initFilter();
    _filterController=new HobbyRewardsFilterController(hobby,_filter,this);
    _tableController=new HobbyDropTablePanelController(parent);
    _panel=buildPanel();
  }

  private void initFilter()
  {
    HobbyRewards rewards=_hobby.getRewards();
    List<Integer> territories=rewards.getKnownTerritories();
    if (!territories.isEmpty())
    {
      int territoryID=territories.get(0).intValue();
      Territory territory=GeoAreasManager.getInstance().getTerritoryById(territoryID);
      _filter.setTerritory(territory);
    }
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    ret.add(_filterController.getPanel(),c);
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.EAST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    ret.add(_tableController.getPanel(),c);
    return ret;
  }

  @Override
  public void filterUpdated()
  {
    Territory territory=_filter.getTerritory();
    Integer proficiency=_filter.getProficiency();

    List<HobbyDropTableEntry> entries=new ArrayList<HobbyDropTableEntry>();
    if ((territory!=null) && (proficiency!=null))
    {
      HobbyRewards rewards=_hobby.getRewards();
      HobbyRewardsProfile profile=rewards.getProfile(territory.getIdentifier());
      HobbyDropTable table=profile.buildDropTable(proficiency.intValue());
      if (table!=null)
      {
        entries.addAll(table.getEntries());
      }
    }
    _tableController.setEntries(entries);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _hobby=null;
    _filter=null;
    if (_filterController!=null)
    {
      _filterController.dispose();
      _filterController=null;
    }
    if (_tableController!=null)
    {
      _tableController.dispose();
      _tableController=null;
    }
  }
}
