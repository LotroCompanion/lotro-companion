package delta.games.lotro.gui.character.status.traits.racial;

import delta.games.lotro.character.CharacterData;
import delta.games.lotro.character.CharacterSummary;
import delta.games.lotro.character.races.RaceDescription;
import delta.games.lotro.character.races.RacesManager;
import delta.games.lotro.character.status.traits.shared.TraitSlotsStatus;
import delta.games.lotro.character.traits.TraitsManager;

/**
 * Test class for the racial traits status panel.
 * @author DAM
 */
public class MainTestRacialTraitsEditionDialogController
{
  /**
   * Main method for this test.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    for(RaceDescription r : RacesManager.getInstance().getAll())
    {
      CharacterData data=new CharacterData();
      CharacterSummary s=new CharacterSummary();
      s.setRace(r);
      s.setLevel(140);
      data.getSummary().setSummary(s);
      data.getTraits().setRacialTraitsStatus(new TraitSlotsStatus());
      TraitSlotsStatus newStatus=RacialTraitsEditionDialogController.editTraits(null,data.getTraits().getRacialTraitsStatus(),data.getSummary());
      if (newStatus!=null)
      {
        int nbSlots=newStatus.getSlotsCount();
        for(int i=0;i<nbSlots;i++)
        {
          System.out.print("#"+i+": ");
          int traitID=newStatus.getTraitAt(i);
          if (traitID!=0)
          {
            System.out.println(TraitsManager.getInstance().getTrait(traitID));
          }
        }
      }
    }
  }
}
