package delta.games.lotro.gui.lore.hobbies.form;

import delta.games.lotro.lore.maps.Territory;

/**
 * Hobby rewards selector.
 * @author DAM
 */
public class HobbyRewardsSelection
{
  private Territory _territory;
  private Integer _proficiency;

  /**
   * Constructor.
   */
  public HobbyRewardsSelection()
  {
    _territory=null;
    _proficiency=null;
  }

  /**
   * @return the territory
   */
  public Territory getTerritory()
  {
    return _territory;
  }

  /**
   * @param territory the territory to set
   */
  public void setTerritory(Territory territory)
  {
    _territory=territory;
  }

  /**
   * @return the proficiency
   */
  public Integer getProficiency()
  {
    return _proficiency;
  }

  /**
   * @param proficiency the proficiency to set
   */
  public void setProficiency(Integer proficiency)
  {
    _proficiency=proficiency;
  }

  @Override
  public String toString()
  {
    return "Territory: "+_territory+", proficiency: "+_proficiency;
  }
}
