package delta.games.lotro.lore.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.apache.log4j.Logger;

import delta.common.framework.objects.sql.ObjectSqlDriver;
import delta.common.utils.jdbc.CleanupManager;
import delta.common.utils.jdbc.JDBCTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Money;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemCategory;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemSturdiness;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.lore.items.bonus.Bonus;
import delta.games.lotro.lore.items.bonus.BonusManager;
import delta.games.lotro.utils.LotroLoggers;

/**
 * SQL driver for items.
 * @author DAM
 */
public class ItemsSqlDriver extends ObjectSqlDriver<Item>
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String PREFIX="";
  private static final String ITEM_TABLE_NAME=PREFIX+"item";
  private static final String ITEM_BONUS_TABLE_NAME=PREFIX+"item_bonus";
  private static final String ARMOR_TABLE_NAME=PREFIX+"armor";
  private static final String WEAPON_TABLE_NAME=PREFIX+"weapon";
  private PreparedStatement _psGetByPrimaryKey;
  //private PreparedStatement _psGetArmorByPrimaryKey;
  //private PreparedStatement _psGetWeaponByPrimaryKey;
  private PreparedStatement _psInsert;
  private PreparedStatement _psInsertArmor;
  private PreparedStatement _psInsertWeapon;
  private PreparedStatement _psCount;
  private PreparedStatement _psGetItemBonuses;
  private PreparedStatement _psInsertItemBonus;

  @Override
  protected void buildPreparedStatements(Connection newConnection)
  {
    try
    {
      String fields="internal_id,lotro_id,item_key,set_key,slot,name,icon_url,item_type,category,"
          +"binding,unicity,durability,sturdiness,item_level,min_level,char_class,description,"
          +"item_value,stack_max,quality";
      String armorFields="internal_id,armor_value,armor_type";
      String weaponFields="internal_id,min_damage,max_damage,damage_type,dps,weapon_type";
      // Select
      String sql="SELECT "+fields+" FROM "+ITEM_TABLE_NAME+" WHERE internal_id = ?";
      _psGetByPrimaryKey=newConnection.prepareStatement(sql);
      sql="SELECT "+armorFields+" FROM "+ARMOR_TABLE_NAME+" WHERE internal_id = ?";
      //_psGetArmorByPrimaryKey=newConnection.prepareStatement(sql);
      sql="SELECT "+weaponFields+" FROM "+WEAPON_TABLE_NAME+" WHERE internal_id = ?";
      //_psGetWeaponByPrimaryKey=newConnection.prepareStatement(sql);
      // Insert
      sql="INSERT INTO "+ITEM_TABLE_NAME+" ("+fields+") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      String sqlArmor="INSERT INTO "+ARMOR_TABLE_NAME+" ("+armorFields+") VALUES (?,?,?)";
      String sqlWeapon="INSERT INTO "+WEAPON_TABLE_NAME+" ("+weaponFields+") VALUES (?,?,?,?,?,?)";
      if (usesHSQLDB())
      {
        _psInsert=newConnection.prepareStatement(sql);
        _psInsertArmor=newConnection.prepareStatement(sqlArmor);
        _psInsertWeapon=newConnection.prepareStatement(sqlWeapon);
      }
      else
      {
        _psInsert=newConnection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        _psInsertArmor=newConnection.prepareStatement(sqlArmor,Statement.RETURN_GENERATED_KEYS);
        _psInsertWeapon=newConnection.prepareStatement(sqlWeapon,Statement.RETURN_GENERATED_KEYS);
      }
      // Select count
      sql="SELECT COUNT(*) FROM item WHERE internal_id = ?";
      _psCount=newConnection.prepareStatement(sql);
      // Get bonuses
      String bonusFields="internal_item_id,item_order,bonus_name,bonus_value,bonus_occurrence,bonus_duration";
      sql="SELECT "+bonusFields+" FROM "+ITEM_BONUS_TABLE_NAME+" WHERE internal_item_id = ? ORDER by order";
      _psGetItemBonuses=newConnection.prepareStatement(sql);
      // Insert bonus
      sql="INSERT INTO "+ITEM_BONUS_TABLE_NAME+" ("+bonusFields+") VALUES (?,?,?,?,?,?)";
      if (usesHSQLDB())
      {
        _psInsertItemBonus=newConnection.prepareStatement(sql);
      }
      else
      {
        _psInsertItemBonus=newConnection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
      }
    }
    catch (SQLException sqlException)
    {
      _logger.error("Exception while building prepared statements for class Item",sqlException);
    }
  }

  @Override
  public Item getByPrimaryKey(Long primaryKey)
  {
    if (primaryKey==null)
    {
      return null;
    }
    Connection connection=getConnection();
    synchronized (connection)
    {
      Item ret=null;
      ResultSet rs=null;
      try
      {
        _psGetByPrimaryKey.setLong(1,primaryKey.longValue());
        rs=_psGetByPrimaryKey.executeQuery();
        if (rs.next())
        {
          ret=buildItem(primaryKey,rs);
          loadBonuses(ret);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
      return ret;
    }
  }

  private Item buildItem(Long primaryKey, ResultSet rs) throws SQLException
  {
    Item item;
    // Item type
    String itemType=rs.getString(8);
    if (ItemCategory.ARMOUR.name().equals(itemType)) item=new Armour();
    else if (ItemCategory.WEAPON.name().equals(itemType)) item=new Weapon();
    else item=new Item();
    item.setPrimaryKey(primaryKey);

    int n=2;
    // Lotro ID
    int lotroId=rs.getInt(n);
    if (!rs.wasNull()) item.setIdentifier(lotroId);
    n++;
    // lorebook Key
    String key=rs.getString(n);
    if (!rs.wasNull()) item.setKey(key);
    n++;
    // set key
    String setKey=rs.getString(n);
    if (!rs.wasNull()) item.setSetKey(setKey);
    n++;
    // slot
    String slotStr=rs.getString(n);
    if (!rs.wasNull())
    {
      EquipmentLocation location=EquipmentLocation.getByName(slotStr);
      if (location!=null)
      {
        item.setEquipmentLocation(location);
      }
    }
    n++;
    // name
    String name=rs.getString(n);
    if (!rs.wasNull()) item.setName(name);
    n++;
    // icon URL
    String iconURL=rs.getString(n);
    if (!rs.wasNull()) item.setIconURL(iconURL);
    n++;
    // Item type - TODO switch item/armor/weapon
    //int itemType=rs.getInt(n);
    n++;
    // category
    String category=rs.getString(n);
    if (!rs.wasNull()) item.setSubCategory(category);
    n++;
    // binding
    String bindingStr=rs.getString(n);
    if (!rs.wasNull())
    {
      ItemBinding binding=ItemBinding.valueOf(bindingStr);
      item.setBinding(binding);
    }
    n++;
    // unique
    boolean unique=rs.getBoolean(n);
    if (!rs.wasNull()) item.setUnique(unique);
    n++;
    // durability
    int durability=rs.getInt(n);
    if (!rs.wasNull()) item.setDurability(Integer.valueOf(durability));
    n++;
    // sturdiness
    String sturdinessStr=rs.getString(n);
    if (!rs.wasNull())
    {
      ItemSturdiness sturdiness=ItemSturdiness.valueOf(sturdinessStr);
      item.setSturdiness(sturdiness);
    }
    n++;
    // item_level
    int itemLevel=rs.getInt(n);
    if (!rs.wasNull()) item.setItemLevel(Integer.valueOf(itemLevel));
    n++;
    // min_level
    int minLevel=rs.getInt(n);
    if (!rs.wasNull()) item.setMinLevel(Integer.valueOf(minLevel));
    n++;
    // char_class
    String charClassStr=rs.getString(n);
    if (!rs.wasNull())
    {
      CharacterClass charClass=CharacterClass.getByKey(charClassStr);
      item.setRequiredClass(charClass);
    }
    n++;
    // description
    String description=rs.getString(n);
    if (!rs.wasNull()) item.setDescription(description);
    n++;
    // value
    int value=rs.getInt(n);
    if (!rs.wasNull())
    {
      Money m=new Money(value/(1000*1000),value/1000,value%1000);
      item.setValue(m);
    }
    n++;
    // stack_max
    int stackMax=rs.getInt(n);
    if (!rs.wasNull()) item.setStackMax(Integer.valueOf(stackMax));
    n++;
    // quality
    String qualityStr=rs.getString(n);
    if (!rs.wasNull())
    {
      ItemQuality quality=ItemQuality.fromCode(qualityStr);
      item.setQuality(quality);
    }
    n++;
    return item;
  }

  private void loadBonuses(Item item)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      ResultSet rs=null;
      try
      {
        long primaryKey=item.getPrimaryKey().longValue();
        _psGetItemBonuses.setLong(1,primaryKey);
        rs=_psGetItemBonuses.executeQuery();
        while (rs.next())
        {
          int n=3;
          String bonusName=rs.getString(n);
          n++;
          String bonusValue=rs.getString(n);
          n++;
          String bonus=bonusName;
          if (bonusValue.length()>0)
          {
            bonus=bonus+" : "+bonusValue;
          }
          // TODO better
          item.getBonus().add(bonus);
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
    }
  }

  /**
   * Indicates if the item identified by <code>primaryKey</code>
   * exists or not.
   * @param primaryKey Identifier for the targeted item.
   * @return <code>true</code> if it does, <code>false</code> otherwise.
   */
  public boolean exists(long primaryKey)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      boolean ret=false;
      ResultSet rs=null;
      try
      {
        _psCount.setLong(1,primaryKey);
        rs=_psGetByPrimaryKey.executeQuery();
        if (rs.next())
        {
          long count=rs.getLong(1);
          if (count>0)
          {
            ret=true;
          }
        }
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psGetByPrimaryKey);
      }
      finally
      {
        CleanupManager.cleanup(rs);
      }
      return ret;
    }
  }

  @Override
  public List<Long> getRelatedObjectIDs(String relationName, Long primaryKey)
  {
    List<Long> ret=null;
    return ret;
  }

  @Override
  public void create(Item item)
  {
    Connection connection=getConnection();
    synchronized (connection)
    {
      try
      {
        int n=1;
        Long key=item.getPrimaryKey();
        if (key==null)
        {
          _psInsert.setNull(n,Types.INTEGER);
        }
        else
        {
          _psInsert.setLong(n,key.longValue());
        }
        n++;

        // Lotro ID
        int lotroId=item.getIdentifier();
        if (lotroId!=0)
        {
          _psInsert.setInt(n,lotroId);
        }
        else
        {
          _psInsert.setNull(n,Types.INTEGER);
        }
        n++;
        // Lorebook key
        n=writeString(_psInsert,n,item.getKey());
        // Set key
        n=writeString(_psInsert,n,item.getSetKey());
        // Slot
        EquipmentLocation location=item.getEquipmentLocation();
        String slotStr=(location!=null)?location.getKey():null;
        n=writeString(_psInsert,n,slotStr);
        // Name
        n=writeString(_psInsert,n,item.getName());
        // Icon URL
        n=writeString(_psInsert,n,item.getIconURL());
        // Item type
        ItemCategory itemCategory=item.getCategory();
        String categoryStr=(itemCategory!=null)?itemCategory.name():null;
        n=writeString(_psInsert,n,categoryStr);
        // Category
        n=writeString(_psInsert,n,item.getSubCategory());
        // Binding
        ItemBinding binding=item.getBinding();
        String bindingStr=(binding!=null)?binding.name():null;
        n=writeString(_psInsert,n,bindingStr);
        // Unique
        boolean unique=item.isUnique();
        _psInsert.setBoolean(n,unique);
        n++;
        // Durability
        Integer durability=item.getDurability();
        n=writeInteger(_psInsert,n,durability);
        // Sturdiness
        ItemSturdiness sturdiness=item.getSturdiness();
        String sturdinessStr=(sturdiness!=null)?sturdiness.name():null;
        n=writeString(_psInsert,n,sturdinessStr);
        // Item_level
        Integer itemLevel=item.getItemLevel();
        n=writeInteger(_psInsert,n,itemLevel);
        // Min_level
        Integer minLevel=item.getMinLevel();
        n=writeInteger(_psInsert,n,minLevel);
        // Char_class
        CharacterClass requiredClass=item.getRequiredClass();
        String requiredClassStr=(requiredClass!=null)?requiredClass.getKey():null;
        n=writeString(_psInsert,n,requiredClassStr);
        // Description
        n=writeString(_psInsert,n,item.getDescription());
        // Value
        Money m=item.getValue();
        Integer value=null;
        if (m!=null)
        {
          int v=m.getGoldCoins();
          v=v*1000+m.getSilverCoins();
          v=v*1000+m.getCopperCoins();
          value=Integer.valueOf(v);
        }
        n=writeInteger(_psInsert,n,value);
        // Stack_max
        Integer stackMax=item.getStackMax();
        n=writeInteger(_psInsert,n,stackMax);
        // Quality
        ItemQuality quality=item.getQuality();
        String qualityStr=(quality!=null)?quality.getCode():null;
        n=writeString(_psInsert,n,qualityStr);
        _psInsert.executeUpdate();
        if (key==null)
        {
          if (usesHSQLDB())
          {
            Long primaryKey=JDBCTools.getPrimaryKey(connection,1);
            item.setPrimaryKey(primaryKey);
          }
          else
          {
            ResultSet rs=_psInsert.getGeneratedKeys();
            if (rs.next())
            {
              long primaryKey=rs.getLong(1);
              item.setPrimaryKey(Long.valueOf(primaryKey));
            }
          }
        }
        if (item instanceof Armour) createArmor(connection,(Armour)item);
        else if (item instanceof Weapon) createWeapon(connection,(Weapon)item);
        writeBonuses(connection,item);
      }
      catch (SQLException sqlException)
      {
        _logger.error("",sqlException);
        CleanupManager.cleanup(_psInsert);
      }
    }
  }

  private void createArmor(Connection connection, Armour armor) throws SQLException
  {
    int n=1;
    Long key=armor.getPrimaryKey();
    _psInsertArmor.setLong(n,key.longValue());
    n++;
    // Armor value
    int armorValue=armor.getArmourValue();
    n=writeInteger(_psInsertArmor,n,Integer.valueOf(armorValue));
    // Armor type
    ArmourType armorType=armor.getArmourType();
    String armorTypeStr=(armorType!=null)?armorType.getKey():null;
    n=writeString(_psInsertArmor,n,armorTypeStr);
    _psInsertArmor.executeUpdate();
  }

  private void createWeapon(Connection connection, Weapon armor) throws SQLException
  {
    int n=1;
    Long key=armor.getPrimaryKey();
    _psInsertWeapon.setLong(n,key.longValue());
    n++;
    // Min damage
    int minDamage=armor.getMinDamage();
    n=writeInteger(_psInsertWeapon,n,Integer.valueOf(minDamage));
    // Max damage
    int maxDamage=armor.getMaxDamage();
    n=writeInteger(_psInsertWeapon,n,Integer.valueOf(maxDamage));
    // damage type
    DamageType damageType=armor.getDamageType();
    String damageTypeStr=(damageType!=null)?damageType.getKey():null;
    n=writeString(_psInsertWeapon,n,damageTypeStr);
    // DPS
    float dps=armor.getDPS();
    n=writeFloat(_psInsertWeapon,n,Float.valueOf(dps));
    // Weapon type
    WeaponType weaponType=armor.getWeaponType();
    String weaponTypeStr=(weaponType!=null)?weaponType.getKey():null;
    n=writeString(_psInsertWeapon,n,weaponTypeStr);
    _psInsertWeapon.executeUpdate();
  }

  private void writeBonuses(Connection c, Item item) throws SQLException
  {
    BonusManager mgr=item.getBonusManager();
    if (mgr==null) return;
    int nbBonus=mgr.getNumberOfBonus();
    if (nbBonus==0) return;
    int order=1;
    for(int i=0;i<nbBonus;i++)
    {
      int n=1;
      Bonus bonus=mgr.getBonusAt(i);
      Long key=item.getPrimaryKey();
      _psInsertItemBonus.setLong(n,key.longValue());
      n++;
      _psInsertItemBonus.setInt(n,order);
      n++;
      // Bonus type
      String bonusType=bonus.getBonusType().getKey();
      n=writeString(_psInsertItemBonus,n,bonusType);
      // Bonus value
      Object value=bonus.getValue();
      String valueStr=(value!=null)?value.toString():null;
      n=writeString(_psInsertItemBonus,n,valueStr);
      // Bonus occurrence
      String bonusOccurrence=bonus.getBonusOccurrence().name();
      n=writeString(_psInsertItemBonus,n,bonusOccurrence);
      String bonusDuration=bonus.getDuration();
      n=writeString(_psInsertItemBonus,n,bonusDuration);
      // Update
      _psInsertItemBonus.executeUpdate();
      order++;
    }
  }

  private int writeString(PreparedStatement ps, int n, String value) throws SQLException
  {
    if (value!=null)
    {
      ps.setString(n,value);
    }
    else
    {
      ps.setNull(n,Types.VARCHAR);
    }
    n++;
    return n;
  }

  private int writeInteger(PreparedStatement ps, int n, Integer value) throws SQLException
  {
    if (value!=null)
    {
      ps.setInt(n,value.intValue());
    }
    else
    {
      ps.setNull(n,Types.INTEGER);
    }
    n++;
    return n;
  }

  private int writeFloat(PreparedStatement ps, int n, Float value) throws SQLException
  {
    if (value!=null)
    {
      ps.setFloat(n,value.floatValue());
    }
    else
    {
      ps.setNull(n,Types.FLOAT);
    }
    n++;
    return n;
  }

  @Override
  public void update(Item item)
  {
    // Not implemented
  }
}
