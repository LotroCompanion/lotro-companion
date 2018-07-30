package delta.games.lotro.stats.deeds.io;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delta.common.utils.NumericTools;
import delta.common.utils.misc.IntegerHolder;
import delta.games.lotro.LotroCoreConfig;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.log.CharacterLog;
import delta.games.lotro.character.log.CharacterLogItem;
import delta.games.lotro.character.log.CharacterLogItem.LogItemType;
import delta.games.lotro.character.log.LotroTestUtils;
import delta.games.lotro.lore.deeds.DeedDescription;
import delta.games.lotro.lore.deeds.DeedsManager;
import delta.games.lotro.lore.deeds.io.xml.DeedXMLParser;
import delta.games.lotro.stats.deeds.DeedStatus;
import delta.games.lotro.stats.deeds.DeedsStatusManager;

/**
 * Resolve deed statuses using the character log.
 * @author DAM
 */
public class DeedsStatusFromCharacterLog
{
  private Map<String,IntegerHolder> _fails=new HashMap<String,IntegerHolder>();
  private Map<Integer,DeedDescription> _lorebookDeeds=new HashMap<Integer,DeedDescription>();

  private void loadLorebookDeeds()
  {
    File loreDir=LotroCoreConfig.getInstance().getLoreDir();
    File deedFile=new File(loreDir,"deeds-from-lorebook.xml");
    DeedXMLParser parser=new DeedXMLParser();
    List<DeedDescription> deeds=parser.parseXML(deedFile);
    for(DeedDescription deed : deeds)
    {
      _lorebookDeeds.put(Integer.valueOf(deed.getIdentifier()),deed);
    }
  }

  private String findKeyForId(int id)
  {
    if (id==1879071774) return "Bear-slayer_(Misty_Mountains)";
    if (id==1879071775) return "Bear-slayer_(Advanced)_(Misty_Mountains)";
    return null;
  }

  /**
   * Do it.
   */
  public void doIt()
  {
    loadLorebookDeeds();
    List<CharacterFile> files=new LotroTestUtils().getAllFiles();
    for(CharacterFile file : files)
    {
      doIt(file);
    }
    List<String> failedLabels=new ArrayList<String>(_fails.keySet());
    Collections.sort(failedLabels);
    for(String failedLabel : failedLabels)
    {
      System.out.println(failedLabel+"\t"+_fails.get(failedLabel));
      int id=NumericTools.parseInt(failedLabel.substring(0,failedLabel.indexOf(" - ")),0);
      if (id!=0)
      {
        DeedDescription lorebookDeed=_lorebookDeeds.get(Integer.valueOf(id));
        if (lorebookDeed!=null)
        {
          String category=lorebookDeed.getCategory();
          if ((category!=null) && (category.length()>0))
          {
            System.out.println(category);
          }
          System.out.println(lorebookDeed.getObjectives());
          System.out.println(lorebookDeed.getDescription());
        }
      }
    }
  }

  private void doIt(CharacterFile character)
  {
    DeedsStatusManager status=DeedsStatusIo.load(character);
    CharacterLog log=character.getLastCharacterLog();
    if (log!=null)
    {
      useCharacterLog(status,log);
      DeedsStatusIo.save(character,status);
    }
  }

  private void useCharacterLog(DeedsStatusManager status, CharacterLog log)
  {
    int nbItems=log.getNbItems();
    for(int i=0;i<nbItems;i++)
    {
      CharacterLogItem item=log.getLogItem(i);
      if (item.getLogItemType()==LogItemType.DEED)
      {
        String label=item.getLabel();
        DeedDescription deed=resolveDeedByName(item.getResourceIdentifier(),label);
        if (deed!=null)
        {
          DeedStatus deedStatus=status.get(deed.getKey(),true);
          Long date=deedStatus.getCompletionDate();
          if (date==null)
          {
            deedStatus.setCompletionDate(Long.valueOf(item.getDate()));
          }
          deedStatus.setCompleted(Boolean.TRUE);
        }
      }
    }
  }

  private DeedDescription resolveDeedByName(String label)
  {
    List<DeedDescription> deeds=DeedsManager.getInstance().getAll();
    for(DeedDescription deed : deeds)
    {
      if (label.equalsIgnoreCase(deed.getName()))
      {
        return deed;
      }
    }
    return null;
  }

  private DeedDescription resolveDeedById(int id)
  {
    DeedDescription ret=null;
    String key=findKeyForId(id);
    if (key!=null)
    {
      ret=DeedsManager.getInstance().getDeed(key);
    }
    return ret;
  }

  private DeedDescription resolveDeedByName(Integer id, String label)
  {
    DeedDescription deed = resolveDeedByName(label);
    if (deed != null) 
    {
      return deed;
    }
    if (id == null)
    {
      return null;
    }
    DeedDescription lorebookDeed = _lorebookDeeds.get(id);
    if (lorebookDeed != null)
    {
      deed = resolveDeedByName(lorebookDeed.getName());
      if (deed != null)
      {
        //System.out.println("Match found for: "+label);
        return deed;
      }
    }
    deed = resolveDeedById(id.intValue());
    if (deed != null)
    {
      //System.out.println("Match found for: "+label);
      return deed;
    }
    //System.out.println("Match not found for: "+label);
    String key=id+" - "+label;
    IntegerHolder holder=_fails.get(key);
    if (holder==null)
    {
      holder=new IntegerHolder();
      _fails.put(key,holder);
    }
    holder.increment();
    return null;
  }

  /**
   * Main method for this tool.
   * @param args Not used.
   */
  public static void main(String[] args)
  {
    new DeedsStatusFromCharacterLog().doIt();
  }
}
