package delta.games.lotro.gui.utils.l10n;

import delta.common.utils.i18n.Translator;
import delta.common.utils.i18n.TranslatorsManager;

/**
 * Access to internationalized labels.
 * @author DAM
 */
public class Labels
{
  private static Translator _translator=TranslatorsManager.getInstance().createTranslator(Labels.class);

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
    return _translator.translate(key);
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
    return _translator.translate(key,params);
  }
}
