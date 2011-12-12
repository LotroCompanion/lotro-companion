package delta.games.lotro.character;

import java.util.HashMap;

import delta.common.utils.text.EndOfLine;

/**
 * Equipment of a character.
 * @author DAM
 */
public class CharacterEquipment
{
  private static HashMap<Integer,EQUIMENT_SLOT> _positionToSlot=new HashMap<Integer,EQUIMENT_SLOT>();

  /**
   * Equipment slot designators.
   * @author DAM
   */
  public enum EQUIMENT_SLOT
  {
    /**
     * Left ear.
     */
    LEFT_EAR(14),
    /**
     * Right ear.
     */
    RIGHT_EAR(15),
    /**
     * Neck.
     */
    NECK(11),
    /**
     * Pocket.
     */
    POCKET(16),
    /**
     * Left wrist.
     */
    LEFT_WRIST(9),
    /**
     * Right wrist.
     */
    RIGHT_WRIST(10),
    /**
     * Left finger.
     */
    LEFT_FINGER(12),
    /**
     * Right finger.
     */
    RIGHT_FINGER(13),
    /**
     * Head.
     */
    HEAD(2),
    /**
     * Shoulder.
     */
    SHOULDER(7),
    /**
     * Breast.
     */
    BREAST(3),
    /**
     * Back.
     */
    BACK(8),
    /**
     * Hands.
     */
    HANDS(5),
    /**
     * Legs.
     */
    LEGS(4),
    /**
     * Feet.
     */
    FEET(6),
    /**
     * Main melee object.
     */
    MAIN_MELEE(17),
    /**
     * Other melee object.
     */
    OTHER_MELEE(18),
    /**
     * Ranged object.
     */
    RANGED(19),
    /**
     * Tool object.
     */
    TOOL(20),
    /**
     * Class object item.
     */
    CLASS_ITEM(21);
    
    private int _position;

    private EQUIMENT_SLOT(int position)
    {
      _position=position;
      _positionToSlot.put(Integer.valueOf(position),this);
    }

    /**
     * Get the integer position associated with this slot.
     * @return an integer value.
     */
    public int getPosition()
    {
      return _position;
    }
  }

  /**
   * Contents of a single slot.
   * @author DAM
   */
  public static class SlotContents
  {
    private EQUIMENT_SLOT _slot;
    private String _objectPageURL;
    private String _objectIconURL;
    
    /**
     * Constructor.
     * @param slot Targeted equipment slot.
     * @param objectURL URL of the object in this slot.
     * @param iconURL URL of the icon of object in this slot.
     */
    public SlotContents(EQUIMENT_SLOT slot, String objectURL, String iconURL)
    {
      _slot=slot;
      _objectPageURL=objectURL;
      _objectIconURL=iconURL;
    }

    /**
     * Get the managed slot.
     * @return the managed slot.
     */
    public EQUIMENT_SLOT getSlot()
    {
      return _slot;
    }

    /**
     * Set the URL of the object in this slot.
     * @param objectPageURL URL to set.
     */
    public void setObjectURL(String objectPageURL)
    {
      _objectPageURL=objectPageURL;
    }

    /**
     * Set the URL of the icon of the object in this slot.
     * @param objectIconURL URL to set.
     */
    public void setIconURL(String objectIconURL)
    {
      _objectIconURL=objectIconURL;
    }
    
    /**
     * Get the URL of the object in this slot.
     * @return an URL.
     */
    public String getObjectURL()
    {
      return _objectPageURL;
    }

    /**
     * Get the URL of the icon of the object in this slot.
     * @return an URL.
     */
    public String getIconURL()
    {
      return _objectIconURL;
    }

    @Override
    public String toString()
    {
      return "Slot "+_slot+": object=["+_objectPageURL+"], icon=["+_objectIconURL+"]";
    }
  }

  private HashMap<Integer,SlotContents> _contents; 

  /**
   * Constructor.
   */
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

  /**
   * Get an equipment slot designator using its position index. 
   * @param index Index to search.
   * @return An equipment slot or <code>null</code> if not found.
   */
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
