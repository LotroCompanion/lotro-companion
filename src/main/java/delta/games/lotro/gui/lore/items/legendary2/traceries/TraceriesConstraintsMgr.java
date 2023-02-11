package delta.games.lotro.gui.lore.items.legendary2.traceries;

import java.util.HashSet;
import java.util.Set;

import delta.games.lotro.common.enums.ItemUniquenessChannel;
import delta.games.lotro.lore.items.legendary2.Tracery;

/**
 * Manages the uniqueness constraints for traceries.
 * @author DAM
 */
public class TraceriesConstraintsMgr
{
  private TraceriesConstraintsMgr _parentMgr;
  private Set<ItemUniquenessChannel> _usedChannels;

  /**
   * Constructor.
   * @param parentMgr Parent manager, if any.
   */
  public TraceriesConstraintsMgr(TraceriesConstraintsMgr parentMgr)
  {
    _parentMgr=parentMgr;
    _usedChannels=new HashSet<ItemUniquenessChannel>();
  }

  /**
   * Indicates if the given tracery can be used in
   * the context described by this manager.
   * @param tracery Tracery to test.
   * @return <code>true</code> if it can be used.
   */
  public boolean canBeUsed(Tracery tracery)
  {
    // Check for a uniqueness channel
    ItemUniquenessChannel uniquenessChannel=tracery.getUniquenessChannel();
    if (uniquenessChannel==null)
    {
      return true;
    }
    // Check parent
    if (_parentMgr!=null)
    {
      boolean parentOK=_parentMgr.canBeUsed(tracery);
      if (!parentOK)
      {
        return false;
      }
    }
    if (_usedChannels.contains(uniquenessChannel))
    {
      return false;
    }
    return true;
  }

  /**
   * Clear all tracery usages.
   */
  public void clear()
  {
    _usedChannels.clear();
  }

  /**
   * Mark the given tracery as used.
   * @param tracery Tracery to... use.
   */
  public void use(Tracery tracery)
  {
    if (tracery==null)
    {
      return;
    }
    ItemUniquenessChannel uniquenessChannel=tracery.getUniquenessChannel();
    if (uniquenessChannel==null)
    {
      return;
    }
    _usedChannels.add(uniquenessChannel);
  }
}
