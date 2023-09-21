package delta.games.lotro.gui.lore.items;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconWithText.Position;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.Genus;
import delta.games.lotro.common.enums.ItemClass;
import delta.games.lotro.common.enums.comparator.LotroEnumEntryNameComparator;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.DamageType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemQualities;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.WeaponType;

/**
 * Tools related to items UI.
 * @author DAM
 */
public class ItemUiTools
{
  /**
   * Build an icon for an item.
   * @param item Item to use.
   * @return An icon.
   */
  public static Icon buildItemIcon(Item item)
  {
    Icon ret=null;
    String iconPath=(item!=null)?item.getIcon():null;
    ImageIcon icon=LotroIconsManager.getItemIcon(iconPath);
    ret=icon;
    Integer tier=item.getTier();
    if (tier!=null)
    {
      IconWithText iconWithText=new IconWithText(icon,tier.toString(),Color.WHITE);
      iconWithText.setPosition(Position.TOP_LEFT);
      ret=iconWithText;
    }
    return ret;
  }

  /**
   * Build an icon for an item count.
   * @param item Item to use.
   * @param count Count to display.
   * @return An icon.
   */
  public static Icon buildItemIcon(Item item, int count)
  {
    Icon ret=buildItemIcon(item);
    if (count>1)
    {
      IconWithText iconWithText=new IconWithText(ret,String.valueOf(count),Color.WHITE);
      iconWithText.setPosition(Position.BOTTOM_RIGHT);
      ret=iconWithText;
    }
    return ret;
  }

  /**
   * Build an item link controller.
   * @param parent Parent window.
   * @param item Item to use.
   * @return a new controller.
   */
  public static HyperLinkController buildItemLink(final WindowController parent, final Item item)
  {
    return buildItemLink(parent,item,GuiFactory.buildLabel(""));
  }

  /**
   * Build an item link controller.
   * @param parent Parent window.
   * @param item Item to use.
   * @param label Label to use.
   * @return a new controller.
   */
  public static HyperLinkController buildItemLink(final WindowController parent, final Item item, JLabel label)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showItemForm(parent,item);
      }
    };
    String text=(item!=null)?item.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action,label);
    return controller;
  }

  /**
   * Build an item instance link controller.
   * @param parent Parent window.
   * @param itemInstance Item instance to use.
   * @return a new controller.
   */
  public static HyperLinkController buildItemInstanceLink(final WindowController parent, final ItemInstance<? extends Item> itemInstance)
  {
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        showItemInstanceWindow(parent,itemInstance);
      }
    };
    Item item=itemInstance.getReference();
    String text=(item!=null)?item.getName():"???";
    LocalHyperlinkAction action=new LocalHyperlinkAction(text,al);
    HyperLinkController controller=new HyperLinkController(action);
    return controller;
  }

  /**
   * Show the form window for an item.
   * @param parent Parent window.
   * @param item Item to show.
   */
  public static void showItemForm(WindowController parent, Item item)
  {
    PageIdentifier ref=ReferenceConstants.getItemReference(item.getIdentifier());
    NavigationUtils.navigateTo(ref,parent);
  }

  /**
   * Show the form window for an item.
   * @param parent Parent window.
   * @param itemInstance Item instance to show.
   */
  public static void showItemInstanceWindow(WindowController parent, ItemInstance<? extends Item> itemInstance)
  {
    ItemInstanceDisplayWindowController newWindow=new ItemInstanceDisplayWindowController(parent,itemInstance);
    newWindow.show();
  }

  /**
   * Build a controller for a combo box to choose an item quality.
   * @return A new controller.
   */
  public static ComboBoxController<ItemQuality> buildQualityCombo()
  {
    ComboBoxController<ItemQuality> ctrl=new ComboBoxController<ItemQuality>();
    ctrl.addEmptyItem("");
    for(ItemQuality quality : ItemQualities.ALL)
    {
      ctrl.addItem(quality,quality.getMeaning());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose an item category.
   * @return A new controller.
   */
  public static ComboBoxController<ItemClass> buildCategoryCombo()
  {
    ComboBoxController<ItemClass> ctrl=new ComboBoxController<ItemClass>();
    ctrl.addEmptyItem("");
    Set<ItemClass> categories=new HashSet<ItemClass>();
    for(Item item : ItemsManager.getInstance().getAllItems())
    {
      ItemClass itemClass=item.getItemClass();
      if (itemClass!=null)
      {
        categories.add(itemClass);
      }
    }
    List<ItemClass> sortedCategories=new ArrayList<ItemClass>(categories);
    Collections.sort(sortedCategories,new LotroEnumEntryNameComparator<ItemClass>());
    for(ItemClass sortedCategory : sortedCategories)
    {
      ctrl.addItem(sortedCategory,sortedCategory.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose a weapon type.
   * @param weaponTypes Weapon types to use.
   * @return A new controller.
   */
  public static ComboBoxController<WeaponType> buildWeaponTypeCombo(Iterable<WeaponType> weaponTypes)
  {
    ComboBoxController<WeaponType> ctrl=new ComboBoxController<WeaponType>();
    ctrl.addEmptyItem("");
    for(WeaponType weaponType : weaponTypes)
    {
      ctrl.addItem(weaponType,weaponType.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose a damage type.
   * @param damageTypes Damage types to use.
   * @return A new controller.
   */
  public static ComboBoxController<DamageType> buildDamageTypeCombo(Iterable<DamageType> damageTypes)
  {
    ComboBoxController<DamageType> ctrl=new ComboBoxController<DamageType>();
    ctrl.addEmptyItem("");
    for(DamageType damageType : damageTypes)
    {
      ctrl.addItem(damageType,damageType.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose a genus.
   * @param genuses Genuses to use.
   * @return A new controller.
   */
  public static ComboBoxController<Genus> buildGenus(Iterable<Genus> genuses)
  {
    ComboBoxController<Genus> ctrl=new ComboBoxController<Genus>();
    ctrl.addEmptyItem("");
    for(Genus genus : genuses)
    {
      ctrl.addItem(genus,genus.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose a armour type.
   * @param armourTypes Armour types to use.
   * @return A new controller.
   */
  public static ComboBoxController<ArmourType> buildArmourTypeCombo(Iterable<ArmourType> armourTypes)
  {
    ComboBoxController<ArmourType> ctrl=new ComboBoxController<ArmourType>();
    ctrl.addEmptyItem("");
    for(ArmourType armourType : armourTypes)
    {
      ctrl.addItem(armourType,armourType.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose a location.
   * @return A new controller.
   */
  public static ComboBoxController<Set<EquipmentLocation>> buildLocationsCombo()
  {
    ComboBoxController<Set<EquipmentLocation>> ctrl=new ComboBoxController<Set<EquipmentLocation>>();
    // All
    {
      Set<EquipmentLocation> locations=new HashSet<EquipmentLocation>();
      locations.add(null);
      for(EquipmentLocation location : EquipmentLocation.getAll())
      {
        locations.add(location);
      }
      ctrl.addItem(locations,"(all)");
    }
    // No location
    {
      Set<EquipmentLocation> noLocation=new HashSet<EquipmentLocation>();
      noLocation.add(null);
      ctrl.addItem(noLocation,"No location");
    }
    // One location
    for(EquipmentLocation location : EquipmentLocation.getAll())
    {
      Set<EquipmentLocation> locations=new HashSet<EquipmentLocation>();
      locations.add(location);
      ctrl.addItem(locations,location.getLabel());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Get the color for an item.
   * @param item Item to use.
   * @param defaultColor Default color.
   * @return A color.
   */
  public static Color getColorForItem(Item item, Color defaultColor)
  {
    ItemQuality quality=item.getQuality();
    Color color=getColorFromQuality(quality,defaultColor);
    return color;
  }

  /**
   * Get the color for an item quality.
   * @param quality Quality to use.
   * @param defaultColor Default color.
   * @return A color.
   */
  public static Color getColorFromQuality(ItemQuality quality, Color defaultColor)
  {
    Color ret=defaultColor;
    if (quality==ItemQualities.LEGENDARY) ret=new Color(223,178,0); // Gold
    if (quality==ItemQualities.INCOMPARABLE) ret=new Color(0,165,218); // Teal
    if (quality==ItemQualities.RARE) ret=new Color(244,74,178); // Mauve (Pink)
    if (quality==ItemQualities.UNCOMMON) ret=new Color(111,145,2); // Yellow
    if (quality==ItemQualities.COMMON) ret=defaultColor; // Default
    return ret;
  }

  /**
   * Build a tooltip for the given item instance.
   * @param itemInstance Item instance.
   * @param html Use HTML output or not.
   * @return A tooltip.
   */
  public static String buildItemTooltip(ItemInstance<? extends Item> itemInstance, boolean html)
  {
    Item item=itemInstance.getReference();
    StringBuilder sb=new StringBuilder();
    String name=item.getName();
    sb.append("Name: ").append(name);
    Integer itemLevel=itemInstance.getEffectiveItemLevel();
    if (itemLevel!=null)
    {
      sb.append(" (item level ").append(itemLevel).append(')');
    }
    sb.append(EndOfLine.NATIVE_EOL);
    BasicStatsSet stats=itemInstance.getStats();
    String[] lines=StatUtils.getStatsDisplayLines(stats);
    for(String line : lines)
    {
      sb.append(line).append(EndOfLine.NATIVE_EOL);
    }
    String ret=sb.toString().trim();
    if (html)
    {
      ret="<html>"+ret.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    }
    return ret;
  }
}
