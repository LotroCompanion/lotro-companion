package delta.games.lotro.common;

/**
 * Quest size.
 * @author DAM
 */
public enum SIZE
{
  /**
   * Solo quest.
   */
  SOLO("Solo"),
  /**
   * Small fellowship quest.
   */
  SMALL_FELLOWSHIP("Small fellowship"),
  /**
   * Fellowship quest.
   */
  FELLOWSHIP("Fellowship"),
  /**
   * Raid quest.
   */
  RAID("Raid");

  private String _label;
  private SIZE(String label)
  {
    _label=label;
  }
  
  @Override
  public String toString()
  {
    return _label;
  }
}
