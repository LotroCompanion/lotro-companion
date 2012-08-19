package delta.games.lotro.tools;

import java.io.File;
import java.io.FileFilter;

import delta.common.utils.NumericTools;
import delta.common.utils.files.filter.ExtensionPredicate;
import delta.games.lotro.Config;
import delta.games.lotro.lore.items.ItemsManager;

/**
 * @author DAM
 */
public class UpdateItemDefinitions
{

  /**
   * @param args
   */
  public static void main(String[] args)
  {
    ItemsManager mgr=ItemsManager.getInstance();
    File itemsDir=Config.getInstance().getItemsDir();
    FileFilter fileFilter=new ExtensionPredicate("xml");
    File[] itemFiles=itemsDir.listFiles(fileFilter);
    if (itemFiles!=null)
    {
      for(File itemFile : itemFiles)
      {
        String name=itemFile.getName();
        name=name.substring(0,name.length()-4);
        int id=NumericTools.parseInt(name,-1);
        if (id!=-1)
        {
          itemFile.delete();
          mgr.getItem(name);
        }
      }
    }
  }
}
