package delta.games.lotro.gui.character;

import java.util.List;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.character.classes.ClassDescription;
import delta.games.lotro.character.gear.GearSlot;
import delta.games.lotro.gui.character.status.currencies.CurrenciesPreferences;
import delta.games.lotro.gui.lore.items.chooser.ItemChoiceTableColumnsManager;
import delta.games.lotro.gui.lore.items.chooser.ItemChooser;
import delta.games.lotro.gui.lore.items.essences.EssenceChoice;

/**
 * Provides access to character level preferences.
 * @author DAM
 */
public class CharacterPreferencesManager
{
  /**
   * Get the preferences for a character.
   * @param toonFile Character file.
   * @param id Identifier of the preferences set.
   * @return Some properties or <code>null</code> if not managed.
   */
  public static TypedProperties getUserProperties(CharacterFile toonFile, String id)
  {
    Preferences prefs=toonFile.getPreferences();
    TypedProperties props=prefs.getPreferences(id);
    if (id.startsWith(ItemChooser.ITEM_CHOOSER_PROPERTIES_ID))
    {
      List<String> columnIds=props.getStringList(ItemChooser.COLUMNS_PROPERTY);
      if (columnIds==null)
      {
        columnIds=ItemChoiceTableColumnsManager.getItemChoiceItemColumns();
        columnIds.addAll(getDefaultItemColumnsUsingClassAndSlot(toonFile,id));
        props.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
        prefs.savePreferences(props);
      }
    }
    else if (id.startsWith(ItemChooser.ITEM_INSTANCE_CHOOSER_PROPERTIES_ID))
    {
      List<String> columnIds=props.getStringList(ItemChooser.COLUMNS_PROPERTY);
      if (columnIds==null)
      {
        columnIds=ItemChoiceTableColumnsManager.getItemInstanceChoiceItemColumns();
        columnIds.addAll(getDefaultItemColumnsUsingClassAndSlot(toonFile,id));
        props.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
        prefs.savePreferences(props);
      }
    }
    else if (EssenceChoice.ESSENCE_CHOOSER_PROPERTIES_ID.equals(id))
    {
      List<String> columnIds=props.getStringList(ItemChooser.COLUMNS_PROPERTY);
      if (columnIds==null)
      {
        ClassDescription characterClass=toonFile.getSummary().getCharacterClass();
        columnIds=ItemChoiceTableColumnsManager.getEssenceChoiceColumns(characterClass);
        props.setStringList(ItemChooser.COLUMNS_PROPERTY,columnIds);
        prefs.savePreferences(props);
      }
    }
    else if (CurrenciesPreferences.CURRENCIES_PREFERENCES_ID.equals(id))
    {
      List<String> currencyKeys=props.getStringList(CurrenciesPreferences.SELECTED_CURRENCIES_PROPERTY_NAME);
      if (currencyKeys==null)
      {
        currencyKeys=CurrenciesPreferences.getDefaultCurrenciesForCharacter();
        props.setStringList(CurrenciesPreferences.SELECTED_CURRENCIES_PROPERTY_NAME,currencyKeys);
        prefs.savePreferences(props);
      }
    }
    return props;
  }

  private static List<String> getDefaultItemColumnsUsingClassAndSlot(CharacterFile toonFile, String propsSetId)
  {
    String slotKey=propsSetId.substring(propsSetId.indexOf("#")+1);
    GearSlot slot=GearSlot.getByKey(slotKey);
    ClassDescription characterClass=toonFile.getSummary().getCharacterClass();
    List<String> columnIds=ItemChoiceTableColumnsManager.getItemChoiceColumnsUsingClassAndSlot(characterClass,slot);
    return columnIds;
  }
}
