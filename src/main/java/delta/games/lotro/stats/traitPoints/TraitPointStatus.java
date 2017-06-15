package delta.games.lotro.stats.traitPoints;

/**
 * Status for a single trait point.
 * @author DAM
 */
public class TraitPointStatus
{
  private TraitPoint _point;
  private boolean _acquired;

  /**
   * Constructor.
   * @param traitPoint Managed trait point.
   */
  public TraitPointStatus(TraitPoint traitPoint)
  {
    _point=traitPoint;
  }

  /**
   * Get the managed trait point.
   * @return the managed trait point.
   */
  public TraitPoint getTraitPoint()
  {
    return _point;
  }

  /**
   * Set the value of the 'acquired' flag.
   * @param acquired Flag to set.
   */
  public void setAcquired(boolean acquired)
  {
    _acquired=acquired;
  }

  /**
   * Get the value of the 'acquired' flag.
   * @return the value of the 'acquired' flag.
   */
  public boolean isAcquired()
  {
    return _acquired;
  }
}
