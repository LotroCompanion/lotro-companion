package delta.games.lotro.lore.items.io.tulkas;

/**
 * @author DAM
 */
public class TulkasEquipmentExtractor
{
  /*
  private void doIt()
  {
    HashMap<Integer,HashMap<Object,Object>> items=null;//loadItems();
    inspectEquipment(items,"Elendilmir#Feroce");
    inspectEquipment(items,"Elendilmir#Glumlug");
    inspectEquipment(items,"Elendilmir#Beleganth");
    inspectEquipment(items,"Elendilmir#Utharr");
  }

  private void inspectEquipment(HashMap<Integer,HashMap<Object,Object>> items, String toonID)
  {
    System.out.println(toonID);
    HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> equipment=loadEquipment(items,toonID);
    HashMap<String,Integer> bonuses=loadBonus(equipment);
    bonuses.remove("ItemLevel");
    bonuses.remove("Level");
    bonuses.remove("MinDMG");
    bonuses.remove("MaxDMG");
    System.out.println("Bonuses: "+bonuses);
  }

  private HashMap<String,Integer> loadBonus(HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> equipment)
  {
    HashMap<String,Integer> bonuses=new HashMap<String,Integer>();
    for(Map.Entry<EQUIMENT_SLOT,HashMap<Object,Object>> entry:equipment.entrySet())
    {
      // System.out.println("Slot: "+entry.getKey());
      HashMap<Object,Object> map=entry.getValue();
      if (map!=null)
      {
        loadBonus(bonuses,map);
      }
    }
    return bonuses;
  }

  private Integer getIdFromURL(String url)
  {
    Integer ret=null;
    String separator="?id=";
    int index=url.indexOf(separator);
    if (index!=-1)
    {
      String indexStr=url.substring(index+separator.length());
      int id=NumericTools.parseInt(indexStr,-1);
      if (id!=-1)
      {
        ret=Integer.valueOf(id);
      }
    }
    return ret;
  }

  private HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> loadEquipment(HashMap<Integer,HashMap<Object,Object>> items, String toonID)
  {
    HashMap<EQUIMENT_SLOT,HashMap<Object,Object>> ret=new HashMap<EQUIMENT_SLOT,HashMap<Object,Object>>();
    CharactersManager cm=CharactersManager.getInstance();
    CharacterFile glumFile=cm.getToonById(toonID);
    Character glum=glumFile.getLastCharacterInfo();
    CharacterEquipment equipment=glum.getEquipment();
    for(CharacterEquipment.EQUIMENT_SLOT slot:EQUIMENT_SLOT.values())
    {
      SlotContents contents=equipment.getSlotContents(slot,false);
      if (contents!=null)
      {
        String url=contents.getObjectURL();
        Integer id=getIdFromURL(url);
        if (id!=null)
        {
          HashMap<Object,Object> item=items.get(id);
          if (item!=null)
          {
            Item i=buildItem(id,item);
            System.out.println("Item: "+i.dump());
            ret.put(slot,item);
          }
          else
          {
            System.out.println("Missing id: "+id+" (slot="+slot+")");
          }
          // System.out.println("Slot "+slot+": "+item);
        }
      }
      else
      {
        // System.out.println("Slot "+slot+": no item!");
      }
    }
    return ret;
  }
  */
}
