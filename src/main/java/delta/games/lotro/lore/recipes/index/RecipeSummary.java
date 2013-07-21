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
   * @param profession Related profession.
   * @param tier Recipe tier.
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
   * Get the related profession.
   * @return the related profession.
   */
  public String getProfession()
  {
    return _profession;
  }

  /**
   * Get the recipe tier.
   * @return the recipe tier.
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
