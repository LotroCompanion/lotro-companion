package delta.games.lotro.gui.lore.hobbies.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ComboBoxItem;
import delta.common.utils.math.Range;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewards;
import delta.games.lotro.lore.hobbies.rewards.HobbyRewardsProfile;
import delta.games.lotro.lore.maps.GeoAreasManager;
import delta.games.lotro.lore.maps.Territory;

/**
 * Utility methods for hobbies-related UIs.
 * @author DAM
 */
public class HobbiesUiUtils
{
  /**
   * Build a combo-box controller to choose a hobby territory.
   * @param hobby Hobby to use.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Territory> buildTerritoryCombo(HobbyDescription hobby)
  {
    ComboBoxController<Territory> ctrl=new ComboBoxController<Territory>();
    HobbyRewards rewards=hobby.getRewards();
    List<Integer> territoryIDs=rewards.getKnownTerritories();
    GeoAreasManager geoMgr=GeoAreasManager.getInstance();
    List<Territory> territories=new ArrayList<Territory>();
    for(Integer territoryID : territoryIDs)
    {
      Territory territory=geoMgr.getTerritoryById(territoryID.intValue());
      if (territory!=null)
      {
        territories.add(territory);
      }
    }
    Collections.sort(territories,new NamedComparator());
    for(Territory territory : territories)
    {
      ctrl.addItem(territory,territory.getName());
    }
    if (!territories.isEmpty())
    {
      ctrl.selectItem(territories.get(0));
    }
    return ctrl;
  }

  /**
   * Update a proficiencies combo-box.
   * @param combo Targeted combo-box.
   * @param profile Hobby rewards profile.
   */
  public static void updateProficienciesCombo(ComboBoxController<Integer> combo, HobbyRewardsProfile profile)
  {
    List<ComboBoxItem<Integer>> items=new ArrayList<ComboBoxItem<Integer>>();
    if (profile!=null)
    {
      Range range=profile.getProficiencyRange();
      int min=range.getMin().intValue();
      int max=range.getMax().intValue();
      for(int proficiency=min;proficiency<=max;proficiency++)
      {
        ComboBoxItem<Integer> item=new ComboBoxItem<Integer>(Integer.valueOf(proficiency),String.valueOf(proficiency));
        items.add(item);
      }
    }
    combo.updateItems(items);
  }
}
