package delta.games.lotro.gui.quests;

import delta.games.lotro.gui.stats.achievables.form.AchievableStatusUtils;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.quests.AchievableProxiesResolver;
import delta.games.lotro.lore.quests.objectives.Objective;
import delta.games.lotro.lore.quests.objectives.ObjectiveCondition;
import delta.games.lotro.lore.quests.objectives.ObjectivesManager;

/**
 * Simple tool class to dump the deeds, as displayed in status edition forms.
 * @author DAM
 */
public class MainDumpDeedsDisplay
{
  private void doIt()
  {
    DeedsManager deedsMgr=DeedsManager.getInstance();
    for(DeedDescription deed : deedsMgr.getAll())
    {
      handleDeed(deed);
    }
  }

  private void handleDeed(DeedDescription deed)
  {
    ObjectivesManager objectivesMgr=deed.getObjectives();
    int nbObjectives=objectivesMgr.getObjectivesCount();
    if (nbObjectives<3) return;
    AchievableProxiesResolver.resolve(deed);
    System.out.println("Deed: "+deed.getName()+", ID="+deed.getIdentifier());
    for(Objective objective : objectivesMgr.getObjectives())
    {
      String objectiveOverride=objective.getProgressOverride();
      if (objectiveOverride.length()>0)
      {
        System.out.println("\t"+objectiveOverride);
      }
      for(ObjectiveCondition condition : objective.getConditions())
      {
        String label=AchievableStatusUtils.getConditionLabel(condition);
        // Compute the panel visibility
        boolean showProgress=condition.isShowProgressText();
        boolean showBillboard=condition.isShowBillboardText();
        String progressOverride=condition.getProgressOverride();
        boolean hasProgressOverride=((progressOverride!=null) && (progressOverride.length()>0));
        boolean visible=((showProgress&&showBillboard)||hasProgressOverride);
        if (visible)
        {
          System.out.println("\t"+label);
        }
      }
    }
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new MainDumpDeedsDisplay().doIt();
  }
}
