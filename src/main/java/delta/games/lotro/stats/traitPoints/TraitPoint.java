package delta.games.lotro.stats.traitPoints;

import java.util.HashSet;
import java.util.Set;

import delta.games.lotro.common.CharacterClass;

/**
 * Represents a trait point.
 * @author DAM
 */
public class TraitPoint
{
  private String _id;
  private String _category;
  private String _label;
  private Set<CharacterClass> _requiredCharacterClasses;

  /**
   * Constructor.
   * @param id Internal unique identifier.
   */
  public TraitPoint(String id)
  {
    _id=id;
    _category=null;
    _label=id;
    _requiredCharacterClasses=new HashSet<CharacterClass>();
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
   * Get the category.
   * @return a category.
   */
  public String getCategory()
  {
    return _category;
  }

  /**
   * Set the category.
   * @param category Category to set.
   */
  public void setCategory(String category)
  {
    _category=category;
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
  public Set<CharacterClass> getRequiredClasses()
  {
    return _requiredCharacterClasses;
  }

  /**
   * Add a required class.
   * @param characterClass Class to add.
   */
  public void addRequiredClass(CharacterClass characterClass)
  {
    if (characterClass!=null)
    {
      _requiredCharacterClasses.add(characterClass);
    }
  }

  /**
   * Indicates if this trait is enabled for the given class.
   * @param characterClass Character class to test.
   * @return <code>true</code> if enabled, <code>false</code> otherwise.
   */
  public boolean isEnabledForClass(CharacterClass characterClass)
  {
    return _requiredCharacterClasses.isEmpty()||_requiredCharacterClasses.contains(characterClass);
  }
}
