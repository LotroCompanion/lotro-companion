package delta.games.lotro.character;

import java.util.HashMap;

import delta.common.utils.text.EndOfLine;

/**
 * @author DAM
 */
public class CharacterEquipment
{
  private static HashMap<Integer,EQUIMENT_SLOT> _positionToSlot=new HashMap<Integer,EQUIMENT_SLOT>();

  public enum EQUIMENT_SLOT
  {
    LEFT_EAR(14),
    RIGHT_EAR(15),
    NECK(11),
    POCKET(16),
    LEFT_WRIST(9),
    RIGHT_WRIST(10),
    LEFT_FINGER(12),
    RIGHT_FINGER(13),
    HEAD(2),
    SHOULDER(7),
    BREAST(3),
    BACK(8),
    HANDS(5),
    LEGS(4),
    FEET(6),
    MAIN_MELEE(17),
    OTHER_MELEE(18),
    RANGED(19),
    TOOL(20),
    CLASS_ITEM(21);
    
    private int _position;

    private EQUIMENT_SLOT(int position)
    {
      _position=position;
      _positionToSlot.put(Integer.valueOf(position),this);
    }

    public int getPosition()
    {
      return _position;
    }
  }

  public static class SlotContents
  {
    private EQUIMENT_SLOT _slot;
    private String _objectPageURL;
    private String _objectIconURL;
    
    public SlotContents(EQUIMENT_SLOT slot, String objectURL, String iconURL)
    {
      _slot=slot;
      _objectPageURL=objectURL;
      _objectIconURL=iconURL;
    }

    public void setObjectURL(String objectPageURL)
    {
      _objectPageURL=objectPageURL;
    }

    public void setIconURL(String objectIconURL)
    {
      _objectIconURL=objectIconURL;
    }
    
    public String getObjectURL()
    {
      return _objectPageURL;
    }

    public String getIconURL()
    {
      return _objectIconURL;
    }

    @Override
    public String toString()
    {
      return "Slot "+_slot+": object=["+_objectPageURL+"], icon=["+_objectIconURL+"]";
    }
  };

  private HashMap<Integer,SlotContents> _contents; 

  public CharacterEquipment()
  {
    _contents=new HashMap<Integer,SlotContents>();
    // Force EQUIPMENT_SLOT class initialization
    EQUIMENT_SLOT.values();
  }

  /**
   * Get a slot contents.
   * @param slot Slot to get.
   * @param createIfNeeded <code>true</code> to create the slot if it does not exist,
   * <code>false</code> otherwise.
   * @return A slot contents or <code>null</code> if not found and <code>createIfNeeded</code> is <code>false</code>.
   */
  public SlotContents getSlotContents(EQUIMENT_SLOT slot, boolean createIfNeeded)
  {
    SlotContents contents=null;
    if (slot!=null)
    {
      int index=slot.getPosition();
      contents=_contents.get(Integer.valueOf(index));
      if ((contents==null) && (createIfNeeded))
      {
        contents=new SlotContents(slot,null,null);
        _contents.put(Integer.valueOf(index),contents);
      }
    }
    return contents;
  }

  public static EQUIMENT_SLOT getSlotByIndex(int index)
  {
    EQUIMENT_SLOT slot=_positionToSlot.get(Integer.valueOf(index));
    return slot;
  }

  @Override
  public String toString()
  {
    StringBuilder sb=new StringBuilder();
    EQUIMENT_SLOT[] slots=EQUIMENT_SLOT.values();
    for(EQUIMENT_SLOT slot : slots)
    {
      SlotContents contents=getSlotContents(slot,false);
      if (contents!=null)
      {
        sb.append(contents);
        sb.append(EndOfLine.NATIVE_EOL);
      }
    }
    return sb.toString();
  }
}
