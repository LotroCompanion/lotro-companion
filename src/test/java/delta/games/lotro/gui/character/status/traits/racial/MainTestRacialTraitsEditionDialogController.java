package delta.games.lotro.gui.character.status.traits.racial;

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
      CharacterSummary s=new CharacterSummary();
      s.setRace(r);
      s.setLevel(140);
      TraitSlotsStatus status=new TraitSlotsStatus();
      RacialTraitsEditionDialogController controller=new RacialTraitsEditionDialogController(null,status,s);
      TraitSlotsStatus newStatus=controller.editModal();
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
