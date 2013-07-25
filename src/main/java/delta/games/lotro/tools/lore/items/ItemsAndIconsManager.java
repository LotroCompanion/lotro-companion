package delta.games.lotro.tools.lore.items;

import java.io.File;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.utils.files.TextFileWriter;
import delta.common.utils.text.TextUtils;
import delta.games.lotro.Config;
import delta.games.lotro.common.icons.LotroIconsManager;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.io.web.ItemPageParser;
import delta.games.lotro.lore.recipes.Recipe.ItemReference;
import delta.games.lotro.utils.Escapes;
import delta.games.lotro.utils.LotroLoggers;

/**
 * @author DAM
 */
public class ItemsAndIconsManager
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private HashSet<String> _handledIcons=new HashSet<String>();
  private HashSet<String> _handledItems=new HashSet<String>();

  private File _workDir;
  
  /**
   * Constructor.
   * @param workDir Workspace directory.
   */
  public ItemsAndIconsManager(File workDir)
  {
    _workDir=workDir;
  }

  public void loadMaps()
  {
    File iconFile=new File(_workDir,"icons.txt");
    List<String> lines=TextUtils.readAsLines(iconFile);
    if (lines!=null)
    {
      _handledIcons.addAll(lines);
    }
    File itemsFile=new File(_workDir,"items.txt");
    lines=TextUtils.readAsLines(itemsFile);
    if (lines!=null)
    {
      _handledItems.addAll(lines);
    }
  }

  public void saveMaps()
  {
    File iconFile=new File(_workDir,"icons.txt");
    saveMap(_handledIcons,iconFile);
    File itemsFile=new File(_workDir,"items.txt");
    saveMap(_handledItems,itemsFile);
  }

  private void saveMap(HashSet<String> map, File toFile)
  {
    TextFileWriter w=new TextFileWriter(toFile);
    if (w.start())
    {
      for(String l : map)
      {
        w.writeNextLine(l);
      }
      w.terminate();
    }
  }

  public void handleIcon(String iconURL)
  {
    if (!_handledIcons.contains(iconURL))
    {
      try
      {
        LotroIconsManager iconsManager=LotroIconsManager.getInstance();
        File f=iconsManager.getIconFile(iconURL);
        if (f!=null)
        {
          iconURL=new String(iconURL);
          _handledIcons.add(iconURL);
        }
        System.out.println("Icon: "+f);
      }
      catch(Throwable t)
      {
        System.out.println("Error:");
        t.printStackTrace(System.out);
      }
    }
  }

  private void loadItems(String key)
  {
    System.out.println("Loading item ["+key+"]");
    File itemsDir=Config.getInstance().getItemsDir();
    if (!itemsDir.exists())
    {
      itemsDir.mkdirs();
    }
    key=Escapes.escapeIdentifier(key);
    String url="http://lorebook.lotro.com/wiki/"+key;
    ItemPageParser parser=new ItemPageParser();
    List<Item> items=parser.parseItemPage(url);
    if ((items!=null) && (items.size()>0))
    {
      ItemsManager itemsManager=ItemsManager.getInstance();
      for(Item item : items)
      {
        System.out.println(item.dump());
        int id=item.getIdentifier();
        if (id!=0)
        {
          itemsManager.writeItemFile(item);
        }
        else
        {
          _logger.warn("Item ["+key+"]: identifier=0!");
        }
      }
    }
    else
    {
      _logger.error("Cannot parse item ["+key+"] at URL ["+url+"]!");
    }
  }

  public void handleItemId(String itemId)
  {
    if (itemId!=null)
    {
      if (!_handledItems.contains(itemId))
      {
        System.out.println("Item: "+itemId);
        try
        {
          loadItems(itemId);
          itemId=new String(itemId);
          _handledItems.add(itemId);
        }
        catch(Throwable t)
        {
          System.out.println("Error:");
          t.printStackTrace(System.out);
        }
      }
    }
  }

  public void handleItem(ItemReference item)
  {
    if (item!=null)
    {
      String iconURL=item.getIcon();
      if (iconURL!=null)
      {
        handleIcon(iconURL);
      }
      String itemId=item.getItemId();
      handleItemId(itemId);
    }
  }
}
