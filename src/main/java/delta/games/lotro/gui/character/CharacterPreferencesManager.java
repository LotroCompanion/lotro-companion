package delta.games.lotro.gui.character;

import java.util.List;

import delta.common.utils.misc.Preferences;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.CharacterEquipment.EQUIMENT_SLOT;
import delta.games.lotro.character.CharacterFile;
import delta.games.lotro.common.CharacterClass;
import delta.games.lotro.gui.items.chooser.ItemChoiceTableColumnsManager;
import delta.games.lotro.gui.items.chooser.ItemChoiceWindowController;

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
    TypedProperties props=null;
    if (id.startsWith(ItemChoiceWindowController.ITEM_CHOOSER_PROPERTIES_ID))
    {
      if (toonFile!=null)
      {
        Preferences prefs=toonFile.getPreferences();
        props=prefs.getPreferences(id);
        List<String> columnIds=props.getStringList(ItemChoiceWindowController.COLUMNS_PROPERTY);
        if (columnIds==null)
        {
          String slotKey=id.substring(id.indexOf("#")+1);
          EQUIMENT_SLOT slot=EQUIMENT_SLOT.valueOf(slotKey);
          CharacterClass characterClass=toonFile.getSummary().getCharacterClass();
          columnIds=ItemChoiceTableColumnsManager.getItemChoiceColumns(characterClass,slot);
          props.setStringList(ItemChoiceWindowController.COLUMNS_PROPERTY,columnIds);
          prefs.savePreferences(props);
        }
      }
    }
    else if (ItemChoiceWindowController.ESSENCE_CHOOSER_PROPERTIES_ID.equals(id))
    {
      Preferences prefs=toonFile.getPreferences();
      props=prefs.getPreferences(id);
      List<String> columnIds=props.getStringList(ItemChoiceWindowController.COLUMNS_PROPERTY);
      if (columnIds==null)
      {
        CharacterClass characterClass=toonFile.getSummary().getCharacterClass();
        columnIds=ItemChoiceTableColumnsManager.getEssenceChoiceColumns(characterClass);
        props.setStringList(ItemChoiceWindowController.COLUMNS_PROPERTY,columnIds);
        prefs.savePreferences(props);
      }
    }
    else
    {
      Preferences prefs=toonFile.getPreferences();
      props=prefs.getPreferences(id);
    }
    return props;
  }
}
