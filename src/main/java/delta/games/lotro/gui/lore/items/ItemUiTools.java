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

import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconWithText.Position;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.labels.LocalHyperlinkAction;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.windows.WindowController;
import delta.common.ui.swing.windows.WindowsManager;
import delta.common.utils.NumericTools;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.common.stats.StatsRegistry;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.navigation.NavigatorFactory;
import delta.games.lotro.lore.items.ArmourType;
import delta.games.lotro.lore.items.EquipmentLocation;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.ItemInstance;
import delta.games.lotro.lore.items.ItemQuality;
import delta.games.lotro.lore.items.ItemsManager;
import delta.games.lotro.lore.items.WeaponType;

/**
 * Tools related to items UI.
 * @author DAM
 */
public class ItemUiTools
{
  private static final String TIER_PATTERN=":Tier";

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
    String subCategory=item.getSubCategory();
    if ((subCategory!=null) && (subCategory.contains(TIER_PATTERN)))
    {
      int index=subCategory.indexOf(TIER_PATTERN);
      String tierStr=subCategory.substring(index+TIER_PATTERN.length());
      Integer tier=NumericTools.parseInteger(tierStr);
      if (tier!=null)
      {
        IconWithText iconWithText=new IconWithText(icon,tier.toString(),Color.WHITE);
        iconWithText.setPosition(Position.TOP_LEFT);
        ret=iconWithText;
      }
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
    NavigatorWindowController window=null;
    if (parent instanceof NavigatorWindowController)
    {
      window=(NavigatorWindowController)parent;
    }
    else
    {
      WindowsManager windows=parent.getWindowsManager();
      int id=windows.getAll().size();
      window=NavigatorFactory.buildNavigator(parent,id);
      windows.registerWindow(window);
    }
    PageIdentifier ref=ReferenceConstants.getItemReference(item.getIdentifier());
    window.navigateTo(ref);
    window.show(false);
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
    for(ItemQuality quality : ItemQuality.ALL)
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
  public static ComboBoxController<String> buildCategoryCombo()
  {
    ComboBoxController<String> ctrl=new ComboBoxController<String>();
    ctrl.addEmptyItem("");
    Set<String> categories=new HashSet<String>();
    for(Item item : ItemsManager.getInstance().getAllItems())
    {
      categories.add(item.getSubCategory());
    }
    List<String> sortedCategories=new ArrayList<String>(categories);
    Collections.sort(sortedCategories);
    for(String sortedCategory : sortedCategories)
    {
      ctrl.addItem(sortedCategory,sortedCategory);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose a weapon type.
   * @return A new controller.
   */
  public static ComboBoxController<WeaponType> buildWeaponTypeCombo()
  {
    return buildWeaponTypeCombo(WeaponType.getAll());
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
      ctrl.addItem(weaponType,weaponType.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for a combo box to choose a armour type.
   * @return A new controller.
   */
  public static ComboBoxController<ArmourType> buildArmourTypeCombo()
  {
    return buildArmourTypeCombo(ArmourType.getAll());
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
    for(ArmourType quality : armourTypes)
    {
      ctrl.addItem(quality,quality.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  /**
   * Build a controller for combo box to choose a stat.
   * @return A new controller.
   */
  public static ComboBoxController<StatDescription> buildStatChooser()
  {
    ComboBoxController<StatDescription> controller=new ComboBoxController<StatDescription>();
    controller.addEmptyItem("");
    List<StatDescription> indexedStats=StatsRegistry.getInstance().getIndexedStats();
    for(StatDescription stat : indexedStats)
    {
      String label=stat.getName();
      controller.addItem(stat,label);
    }
    controller.selectItem(null);
    return controller;
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
}
