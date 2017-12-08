package delta.games.lotro.gui.character.stats;

import java.util.Set;

import delta.common.utils.i18n.Translator;
import delta.common.utils.i18n.TranslatorsManager;
import delta.games.lotro.character.stats.STAT;

/**
 * Stat labels provider.
 * @author DAM
 */
public class StatLabels
{
  private static Translator initTranslator()
  {
    String translatorName=StatLabels.class.getPackage().getName()+".statLabels";
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
  public static String getStatLabel(STAT stat)
  {
    return TRANSLATOR.translate(stat.getKey());
  }

  /**
   * Get the set of translatable stats.
   * @return a set of stat keys.
   */
  public static Set<String> getTranslatedStats()
  {
    return TRANSLATOR.getKeys();
  }
}
