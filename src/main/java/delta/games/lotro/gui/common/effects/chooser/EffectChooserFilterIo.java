package delta.games.lotro.gui.common.effects.chooser;

import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.filters.NamedFilter;

/**
 * I/O methods for the effect chooser filter.
 * @author DAM
 */
public class EffectChooserFilterIo
{
  private static final String NAME_PATTERN="namePattern";

  /**
   * Load filter data from the given properties.
   * @param filter Filter to update.
   * @param props Properties to load from.
   */
  public static void loadFrom(EffectChooserFilter filter, TypedProperties props)
  {
    if (props==null)
    {
      return;
    }
    // Name
    NamedFilter<Effect> nameFilter=filter.getNameFilter();
    if (nameFilter!=null)
    {
      String namePattern=props.getStringProperty(NAME_PATTERN,null);
      nameFilter.setPattern(namePattern);
    }
  }

  /**
   * Save filter data to the given properties.
   * @param filter Source filter.
   * @param props Properties to update.
   */
  public static void saveTo(EffectChooserFilter filter, TypedProperties props)
  {
    // Name
    NamedFilter<Effect> nameFilter=filter.getNameFilter();
    if (nameFilter!=null)
    {
      String namePattern=nameFilter.getPattern();
      props.setStringProperty(NAME_PATTERN,namePattern);
    }
  }
}
