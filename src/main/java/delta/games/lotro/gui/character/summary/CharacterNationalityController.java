package delta.games.lotro.gui.character.summary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.games.lotro.character.races.NationalityDescription;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.common.comparators.NamedComparator;

/**
 * Controller for a character nationality combo box.
 * @author DAM
 */
public class CharacterNationalityController
{
  private ComboBoxController<NationalityDescription> _nationalityController;
  private RaceDescription _race;

  /**
   * Constructor.
   */
  public CharacterNationalityController()
  {
    _nationalityController=new ComboBoxController<NationalityDescription>();
    setRace(RacesManager.getInstance().getAll().get(0));
  }

  /**
   * Get the managed combobox controller.
   * @return a combobox controller.
   */
  public ComboBoxController<NationalityDescription> getComboBoxController()
  {
    return _nationalityController;
  }

  /**
   * Set the current race.
   * @param race Race to set.
   */
  public void setRace(RaceDescription race)
  {
    if (_race!=race)
    {
      List<NationalityDescription> nationalities=getNationalitiesForRace(race);
      for(NationalityDescription nationality : nationalities)
      {
        _nationalityController.addItem(nationality,nationality.getName());
      }
      if (_race!=null)
      {
        List<NationalityDescription> oldNationalities=getNationalitiesForRace(_race);
        for(NationalityDescription nationality : oldNationalities)
        {
          _nationalityController.removeItem(nationality);
        }
      }
      _race=race;
    }
  }

  private List<NationalityDescription> getNationalitiesForRace(RaceDescription race)
  {
    List<NationalityDescription> ret=new ArrayList<NationalityDescription>();
    for(NationalityDescription nationality : race.getNationalities())
    {
      ret.add(nationality);
    }
    Collections.sort(ret,new NamedComparator());
    return ret;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_nationalityController!=null)
    {
      _nationalityController.dispose();
      _nationalityController=null;
    }
    _race=null;
  }
}
