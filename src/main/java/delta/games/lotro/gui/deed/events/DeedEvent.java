package delta.games.lotro.gui.deed.events;

import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.utils.events.Event;

/**
 * Data for a deed event.
 * @author DAM
 */
public class DeedEvent extends Event
{
  private DeedEventType _type;
  private DeedDescription _data;

  /**
   * Constructor.
   * @param type Event type.
   * @param data Targeted deed.
   */
  public DeedEvent(DeedEventType type, DeedDescription data)
  {
    _type=type;
    _data=data;
  }

  /**
   * Get the type of this event.
   * @return A deed event type.
   */
  public DeedEventType getType()
  {
    return _type;
  }

  /**
   * Get the targeted deed.
   * @return the targeted deed.
   */
  public DeedDescription getDeed()
  {
    return _data;
  }
}
