package delta.games.lotro.gui.utils.l10n;

import java.util.Locale;

import delta.common.utils.i18n.Translator;
import delta.games.lotro.config.LotroCoreConfig;
import delta.games.lotro.config.labels.LabelsConfiguration;

/**
 * Access to internationalized labels.
 * @author DAM
 */
public class Labels
{
  private static final Labels _instance=new Labels();

  private Translator _translator;

  private Labels()
  {
    LabelsConfiguration cfg=LotroCoreConfig.getInstance().getLabelsConfiguration();
    String appLabelsLocale=cfg.getAppLabelsKey();
    Locale locale=Locale.forLanguageTag(appLabelsLocale);
    _translator=new Translator(Labels.class.getName(),null,locale);
  }

  /**
   * Get a label for a field.
   * The returned label is suffixed by ": " or " : ".
   * @param key I18n key.
   * @return A field label.
   */
  public static String getFieldLabel(String key)
  {
    return getLabel(key)+": ";
  }

  /**
   * Get a label.
   * @param key I18n key.
   * @return A label.
   */
  public static String getLabel(String key)
  {
    return _instance._translator.translate(key);
  }

  /**
   * Get a label for a field (with params).
   * The returned label is suffixed by ": " or " : ".
   * @param key I18n key.
   * @param params Label parameters.
   * @return A field label.
   */
  public static String getFieldLabel(String key, Object[] params)
  {
    return getLabel(key,params)+": ";
  }

  /**
   * Get a label (with params).
   * @param key I18n key.
   * @param params Label parameters.
   * @return A label.
   */
  public static String getLabel(String key, Object[] params)
  {
    return _instance._translator.translate(key,params);
  }
}
