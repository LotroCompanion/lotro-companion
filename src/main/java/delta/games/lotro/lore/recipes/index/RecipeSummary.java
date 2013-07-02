package delta.games.lotro.lore.recipes.index;

/**
 * Recipe summary.
 * @author DAM
 */
public class RecipeSummary
{
  private String _key;
  private String _name;
  private String _profession;
  private int _tier;
  
  /**
   * Constructor.
   * @param key Recipe key.
   * @param name Recipe name.
   */
  public RecipeSummary(String key, String name, String profession, int tier)
  {
    _key=key;
    _name=name;
    _profession=profession;
    _tier=tier;
  }
  
  /**
   * Get the recipe key.
   * @return the recipe key.
   */
  public String getKey()
  {
    return _key;
  }

  /**
   * Get the recipe name.
   * @return the recipe name.
   */
  public String getName()
  {
    return _name;
  }

  /**
   * @return the profession
   */
  public String getProfession()
  {
    return _profession;
  }

  /**
   * @return the tier
   */
  public int getTier()
  {
    return _tier;
  }

  @Override
  public String toString()
  {
    return _key+"\t"+_name+"\t"+_profession+"\t"+_tier;
  }
}
