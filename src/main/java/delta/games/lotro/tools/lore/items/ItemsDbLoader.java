package delta.games.lotro.tools.lore.items;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import delta.common.framework.objects.data.ObjectsManager;
import delta.common.utils.NumericTools;
import delta.common.utils.files.filter.ExtensionPredicate;
import delta.common.utils.misc.IntegerHolder;
import delta.games.lotro.Config;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.bonus.Bonus;
import delta.games.lotro.lore.items.bonus.BonusManager;
import delta.games.lotro.lore.items.bonus.BonusType;
import delta.games.lotro.lore.sql.LotroDataSource;

/**
 * Items/sets loader for item XML files.
 * @author DAM
 */
public class ItemsDbLoader
{
  /**
   * Main method.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    LotroDataSource ds=LotroDataSource.getInstance("lotro");
    ObjectsManager<Item> itemsSource=ds.getManager(Item.class);
    ItemsManager mgr=ItemsManager.getInstance();
    File itemsDir=Config.getInstance().getItemsDir();
    FileFilter fileFilter=new ExtensionPredicate("xml");
    File[] itemFiles=itemsDir.listFiles(fileFilter);
    if (itemFiles!=null)
    {
      HashMap<String,IntegerHolder> other=new HashMap<String,IntegerHolder>();
      //int nb=0;
      //Set<String> subCategories=new HashSet<String>();
      //HashMap<String,List<String>> itemsByCategory=new HashMap<String,List<String>>();
      for(File itemFile : itemFiles)
      {
        //if (nb==10000) break;
        String idStr=itemFile.getName();
        idStr=idStr.substring(0,idStr.length()-4);
        int id=NumericTools.parseInt(idStr,-1);
        if (id!=-1)
        {
          System.out.println(id);
          Item item=mgr.getItem(Integer.valueOf(id));
          //System.out.println(item.dump());
          BonusManager bonusMgr=item.getBonusManager();
          if (bonusMgr!=null)
          {
            //System.out.println(bonusMgr.dump());
            int nbBonus=bonusMgr.getNumberOfBonus();
            for(int i=0;i<nbBonus;i++)
            {
              Bonus bonus=bonusMgr.getBonusAt(i);
              if (bonus.getBonusType()==BonusType.OTHER)
              {
                String value=(String)bonus.getValue();
                IntegerHolder holder=other.get(value);
                if (holder==null)
                {
                  holder=new IntegerHolder();
                  other.put(value,holder);
                }
                holder.setInt(holder.getInt()+1);
              }
            }
          }
          itemsSource.create(item);
        }
        //nb++;
      }
      
      List<String> others=new ArrayList<String>(other.keySet());
      Collections.sort(others);
      for(String value : others)
      {
        IntegerHolder counter=other.get(value);
        System.out.println(value+" -> "+counter);
      }
      /*
      for(Map.Entry<String,IntegerHolder> entry : other.entrySet())
      {
        System.out.println(entry.getKey()+" -> "+entry.getValue());
      }
      */
    }
  }
}
