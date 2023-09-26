package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.Color;

import delta.common.ui.swing.labels.LabelLineStyle;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.Config;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.ItemUiTools;
import delta.games.lotro.gui.lore.items.utils.IconControllerNameStatsBundle;
import delta.games.lotro.gui.utils.items.ItemIconController;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * Controller for the UI items to display a single tracery.
 * @author DAM
 */
public class SingleTraceryDisplayController extends IconControllerNameStatsBundle<ItemIconController>
{
  // Data
  private int _characterLevel;
  private SocketType _type;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param type Socket type.
   */
  public SingleTraceryDisplayController(WindowController parent, SocketType type)
  {
    super();
    _type=type;
    setIconController(new ItemIconController(parent));
    Integer characterLevel=parent.getContextProperty(ContextPropertyNames.CHARACTER_LEVEL,Integer.class);
    _characterLevel=(characterLevel!=null)?characterLevel.intValue():Config.getInstance().getMaxCharacterLevel();
    // Initialize with nothing slotted
    setTracery(null,1);
  }

  /**
   * Set current essence.
   * @param traceryInstance Essence to set.
   * @param itemLevel Item level.
   */
  public void setTracery(SocketEntryInstance traceryInstance, int itemLevel)
  {
    // Set icon
    Item item=null;
    Tracery tracery=(traceryInstance!=null)?traceryInstance.getTracery():null;
    if (tracery!=null)
    {
      item=tracery.getItem();
      _icon.setItem(item,1);
    }
    else
    {
      int socketTypeCode=_type.getCode();
      _icon.clear(LotroIconsManager.getEmptySocketIcon(socketTypeCode));
    }
    // Color
    Color foreground=Color.BLACK;
    if (tracery!=null)
    {
      boolean ok=tracery.isApplicable(_characterLevel,itemLevel);
      foreground=ok?Color.BLACK:Color.RED;
    }
    // Name
    LabelLineStyle firstLineStyle=LabelLineStyle.DEFAULT_LINE_STYLE;
    String name="";
    if (item!=null)
    {
      name=item.getName();
      firstLineStyle=firstLineStyle.setHalo(true);
      Color nameColor=ItemUiTools.getColorForItem(item,Color.WHITE);
      firstLineStyle=firstLineStyle.setForegroundColor(nameColor);
    }
    else if (traceryInstance!=null)
    {
      // Nothing slotted. Give a hint on the expected socket type
      SocketType type=traceryInstance.getTemplate().getType();
      name="( "+type.getLabel()+" )";
    }
    _lines.setLineStyle(0,firstLineStyle);
    setName(name);
    // Stats
    LabelLineStyle defaultLineStyle=LabelLineStyle.DEFAULT_LINE_STYLE;
    defaultLineStyle=defaultLineStyle.setForegroundColor(foreground);
    defaultLineStyle=defaultLineStyle.setFontSize(10);
    _lines.setDefaultStyle(defaultLineStyle);
    if (tracery!=null)
    {
      // Stats
      BasicStatsSet stats=traceryInstance.getStats();
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      setStats(lines);
    }
    else
    {
      setStats(new String[]{});
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    if (_icon!=null)
    {
      _icon.dispose();
      _icon=null;
    }
  }
}
