package delta.games.lotro.stats.traitPoints;

import delta.games.lotro.common.CharacterClass;

/**
 * Represents a trait point.
 * @author DAM
 */
public class TraitPoint
{
  private String _id;
  private String _label;
  private CharacterClass _requiredCharacterlass;

  /**
   * Constructor.
   * @param id Internal unique identifier.
   * @param requiredCharacterClass Required character class (may be <code>null</code>).
   */
  public TraitPoint(String id, CharacterClass requiredCharacterClass)
  {
    _id=id;
    _label=id;
    _requiredCharacterlass=requiredCharacterClass;
  }

  /**
   * Get the internal identifier.
   * @return a string identifier.
   */
  public String getId()
  {
    return _id;
  }

  /**
   * Get the short descriptive label.
   * @return a label.
   */
  public String getLabel()
  {
    return _label;
  }

  /**
   * Set the label.
   * @param label Label to set.
   */
  public void setLabel(String label)
  {
    _label=label;
  }

  /**
   * Get the required class, if any.
   * @return the required class or <code>null</code>.
   */
  public CharacterClass getRequiredClass()
  {
    return _requiredCharacterlass;
  }
}
