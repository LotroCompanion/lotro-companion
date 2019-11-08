package delta.games.lotro.gui.character.stats.details;

import delta.common.utils.i18n.Translator;
import delta.common.utils.i18n.TranslatorsManager;
import delta.games.lotro.common.stats.StatDescription;

/**
 * Stat labels provider for the detailed stats window.
 * @author DAM
 */
public class DetailedStatsLabels
{
  private static Translator initTranslator()
  {
    String translatorName=DetailedStatsLabels.class.getPackage().getName()+".detailedStatLabels";
    TranslatorsManager translatorsMgr=TranslatorsManager.getInstance();
    Translator translator=translatorsMgr.getTranslatorByName(translatorName,true,false);
    return translator;
  }

  private static final Translator TRANSLATOR=initTranslator();

  /**
   * Get a displayable label for a stat.
   * @param stat Targeted stat.
   * @return A displayable label.
   */
  public static String getStatLabel(StatDescription stat)
  {
    String label=TRANSLATOR.unsafeTranslate(stat.getPersistenceKey());
    if (label==null)
    {
      label=stat.getName();
    }
    return label;
  }
}
