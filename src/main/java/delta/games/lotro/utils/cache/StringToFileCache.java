package delta.games.lotro.utils.cache;

import java.io.File;

import delta.games.lotro.utils.Escapes;

/**
 * Converts string identifiers to file paths.
 * @author DAM
 */
public class StringToFileCache
{
  private File _rootDir;
  private String[] _seeds;

  /**
   * Constructor.
   * @param rootDir Root directory for cache.
   * @param seed Seed to remove for entries.
   */
  public StringToFileCache(File rootDir, String seed)
  {
    this(rootDir,new String[]{seed});
  }
  
  /**
   * Constructor.
   * @param rootDir Root directory for cache.
   * @param seeds Seeds to remove for entries.
   */
  public StringToFileCache(File rootDir, String[] seeds)
  {
    _rootDir=rootDir;
    int length=(seeds!=null)?seeds.length:0;
    _seeds=new String[length];
    for(int i=0;i<length;i++)
    {
      _seeds[i]=seeds[i]; 
    }
  }
  
  /**
   * Get the file associated to the given string.
   * @param s Key string.
   * @return A file.
   */
  public File getFileForString(String s)
  {
    File ret=null;
    if (s!=null)
    {
      ret=_rootDir;
      if (_seeds!=null)
      {
        for(int i=0;i<_seeds.length;i++)
        {
          if ((_seeds[i]!=null) && (s.startsWith(_seeds[i])))
          {
            s=s.substring(_seeds[i].length());
            break;
          }
        }
      }
      s=s.replace('\\','/');
      String[] path=s.split("/");
      String pathItem;
      for(int i=0;i<path.length;i++)
      {
        pathItem=path[i];
        pathItem=pathItem.trim();
        if (pathItem.length()>0)
        {
          pathItem=Escapes.escapeIdentifier(pathItem);
          ret=new File(ret,pathItem);
        }
      }
    }
    return ret;
  }

  /*
  public static void main(String[] args)
  {
    String[] testStrings={
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/tool/eq_c_craft_tool_voc_explorer_tier6.png",
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/craftingtrophies/it_trophy_eye_rar_51to52.png",
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/chest/eq_isengard_88_chest_hunter.png",
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/ingredient_optional/it_craft_s_t6_optional_ingredient.png",
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/item_hunter/eq_c_hunter_book_power_tier7.png",
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/weapon_crossbow/eq_ia_craft_hunter_crossbow_incomp_lvl75_1.png",
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/item/jewelry/eq_craft_j_t1_d_bracelet_1.png",
        "http://content.turbine.com/sites/lorebook.lotro.com/images/icons/trait/trait_legendary_burglar_dps_legendary.png"
    };
    File root=FileSystem.getTmpDir();
    String seed="http://content.turbine.com/sites/lorebook.lotro.com/images/icons/";
    StringToFileCache cache=new StringToFileCache(root,seed);
    for(String testString : testStrings)
    {
      File f=cache.getFileForString(testString);
      System.out.println(f);
    }
  }
  */
}
