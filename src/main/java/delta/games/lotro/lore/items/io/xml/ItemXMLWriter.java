package delta.games.lotro.lore.items.io.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.xml.sax.helpers.AttributesImpl;

import delta.common.utils.io.StreamTools;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.common.Money;
import delta.games.lotro.common.money.io.xml.MoneyXMLWriter;
import delta.games.lotro.lore.items.Armour;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemBinding;
import delta.games.lotro.lore.items.ItemCategory;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemSturdiness;
import delta.games.lotro.lore.items.Weapon;
import delta.games.lotro.lore.items.WeaponType;
import delta.games.lotro.utils.LotroLoggers;

/**
 * Writes LOTRO items to XML files.
 * @author DAM
 */
public class ItemXMLWriter
{
  private static final Logger _logger=LotroLoggers.getLotroLogger();

  private static final String CDATA="CDATA";
  
  /**
   * Write an item to a XML file.
   * @param outFile Output file.
   * @param item Item to write.
   * @param encoding Encoding to use.
   * @return <code>true</code> if it succeeds, <code>false</code> otherwise.
   */
  public boolean write(File outFile, Item item, String encoding)
  {
    boolean ret;
    FileOutputStream fos=null;
    try
    {
      File parentFile=outFile.getParentFile();
      if (!parentFile.exists())
      {
        parentFile.mkdirs();
      }
      fos=new FileOutputStream(outFile);
      SAXTransformerFactory tf=(SAXTransformerFactory)TransformerFactory.newInstance();
      TransformerHandler hd=tf.newTransformerHandler();
      Transformer serializer=hd.getTransformer();
      serializer.setOutputProperty(OutputKeys.ENCODING,encoding);
      serializer.setOutputProperty(OutputKeys.INDENT,"yes");

      StreamResult streamResult=new StreamResult(fos);
      hd.setResult(streamResult);
      hd.startDocument();
      write(hd,item);
      hd.endDocument();
      ret=true;
    }
    catch (Exception exception)
    {
      _logger.error("",exception);
      ret=false;
    }
    finally
    {
      StreamTools.close(fos);
    }
    return ret;
  }
  
  private void write(TransformerHandler hd, Item item) throws Exception
  {
    AttributesImpl itemAttrs=new AttributesImpl();

    // Key
    String key=item.getKey();
    if (key!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_ID_ATTR,CDATA,key);
    }
    // Set identifier
    String setIdentifier=item.getSetKey();
    if (setIdentifier!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_SET_ID_ATTR,CDATA,setIdentifier);
    }
    // Name
    String name=item.getName();
    if (name!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_NAME_ATTR,CDATA,name);
    }
    // Icon URL
    String iconURL=item.getIconURL();
    if (iconURL!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_ICON_URL_ATTR,CDATA,iconURL);
    }
    // Category
    ItemCategory category=item.getCategory();
    if (category!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_CATEGORY_ATTR,CDATA,category.name());
    }
    // Sub-category
    String subCategory=item.getSubCategory();
    if (subCategory!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_SUBCATEGORY_ATTR,CDATA,subCategory);
    }
    // Binding
    ItemBinding binding=item.getBinding();
    if (binding!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_BINDING_ATTR,CDATA,binding.name());
    }
    // Unique
    boolean unique=item.isUnique();
    itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_UNIQUE_ATTR,CDATA,String.valueOf(unique));
    // Durability
    Integer durability=item.getDurability();
    if (durability!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_DURABILITY_ATTR,CDATA,String.valueOf(durability.intValue()));
    }
    // Sturdiness
    ItemSturdiness sturdiness=item.getSturdiness();
    if (sturdiness!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_STURDINESS_ATTR,CDATA,sturdiness.name());
    }
    // Quality
    ItemQuality quality=item.getQuality();
    if (quality!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_QUALITY_ATTR,CDATA,quality.getCode());
    }
    // Minimum level
    Integer minLevel=item.getMinLevel();
    if (minLevel!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_MINLEVEL_ATTR,CDATA,String.valueOf(minLevel.intValue()));
    }
    // Required class
    CharacterClass requiredClass=item.getRequiredClass();
    if (requiredClass!=null)
    {
      String className=requiredClass.getKey();
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_REQUIRED_CLASS_ATTR,CDATA,className);
    }
    // Description
    String description=item.getDescription();
    if (description!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_DESCRIPTION_ATTR,CDATA,description);
    }
    // Stack max
    Integer stackMax=item.getStackMax();
    if (stackMax!=null)
    {
      itemAttrs.addAttribute("","",ItemXMLConstants.ITEM_STACK_MAX_ATTR,CDATA,String.valueOf(stackMax.intValue()));
    }
    
    // Armor specific:
    if (category==ItemCategory.ARMOUR)
    {
      Armour armour=(Armour)item;
      int armourValue=armour.getArmourValue();
      itemAttrs.addAttribute("","",ItemXMLConstants.ARMOUR_ATTR,CDATA,String.valueOf(armourValue));
      ArmourType type=armour.getArmourType();
      if (type!=null)
      {
        itemAttrs.addAttribute("","",ItemXMLConstants.ARMOUR_TYPE_ATTR,CDATA,type.getKey());
      }
    }
    // Weapon specific:
    else if (category==ItemCategory.WEAPON)
    {
      Weapon weapon=(Weapon)item;
      float dps=weapon.getDPS();
      itemAttrs.addAttribute("","",ItemXMLConstants.DPS_ATTR,CDATA,String.valueOf(dps));
      int minDamage=weapon.getMinDamage();
      itemAttrs.addAttribute("","",ItemXMLConstants.MIN_DAMAGE_ATTR,CDATA,String.valueOf(minDamage));
      int maxDamage=weapon.getMaxDamage();
      itemAttrs.addAttribute("","",ItemXMLConstants.MAX_DAMAGE_ATTR,CDATA,String.valueOf(maxDamage));
      DamageType type=weapon.getDamageType();
      if (type!=null)
      {
        itemAttrs.addAttribute("","",ItemXMLConstants.DAMAGE_TYPE_ATTR,CDATA,type.getKey());
      }
      WeaponType weaponType=weapon.getWeaponType();
      if (weaponType!=null)
      {
        itemAttrs.addAttribute("","",ItemXMLConstants.WEAPON_TYPE_ATTR,CDATA,weaponType.getName());
      }
    }
    hd.startElement("","",ItemXMLConstants.ITEM_TAG,itemAttrs);

    // Money
    Money value=item.getValue();
    MoneyXMLWriter.writeMoney(hd,value);
    // Bonuses
    // TODO better
    List<String> bonuses=item.getBonus();
    if (bonuses!=null)
    {
      for(String bonus : bonuses)
      {
        AttributesImpl attrs=new AttributesImpl();
        attrs.addAttribute("","",ItemXMLConstants.BONUS_VALUE_ATTR,CDATA,bonus);
        hd.startElement("","",ItemXMLConstants.BONUS_TAG,attrs);
        hd.endElement("","",ItemXMLConstants.BONUS_TAG);
      }
    }
    hd.endElement("","",ItemXMLConstants.ITEM_TAG);
  }
}
