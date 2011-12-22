package delta.games.lotro.common;

import delta.common.utils.text.EndOfLine;

/**
 * Trait.
 * @author DAM
 */
public class Trait
{
  private String _identifier;
  private String _name;

  /**
   * Constructor.
   * @param identifier Internal identifier.
   * @param name Trait's name.
   */
  public Trait(String identifier, String name)
  {
    _identifier=identifier;
    _name=name;
  }
  
  /**
   * Get the internal identifier.
   * @return the internal identifier.
   */
  public String getIdentifier()
  {
    return _identifier;
  }

  /**
   * Get the trait's name.
   * @return the trait's name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * Dump the contents of this trait as a string.
   * @return A readable string.
   */
  public String dump()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Trait: ").append(_name);
    sb.append(" (").append(_identifier).append(')');
    sb.append(EndOfLine.NATIVE_EOL);
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return _name;
  }
}
