package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.Color;

import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.Config;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.common.enums.SocketType;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.lore.items.utils.IconControllerNameStatsBundle;
import delta.games.lotro.gui.utils.IconControllerFactory;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.items.legendary2.SocketEntryInstance;
import delta.games.lotro.lore.items.legendary2.Tracery;
import delta.games.lotro.utils.ContextPropertyNames;

/**
 * Controller for the UI items to display a single tracery.
 * @author DAM
 */
public class SingleTraceryDisplayController extends IconControllerNameStatsBundle
{
  // Data
  private int _characterLevel;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public SingleTraceryDisplayController(WindowController parent)
  {
    super(parent);
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
      IconControllerFactory.updateItemIcon(_icon,item,1);
    }
    else
    {
      _icon.clear(LotroIconsManager.getDefaultItemIcon());
    }
    // Color
    Color foreground=Color.BLACK;
    if (tracery!=null)
    {
      boolean ok=tracery.isApplicable(_characterLevel,itemLevel);
      foreground=ok?Color.BLACK:Color.RED;
    }
    getLinesGadget().setForegroundColor(foreground);
    // Text
    String name="";
    if (item!=null)
    {
      name=item.getName();
    }
    else if (traceryInstance!=null)
    {
      // Nothing slotted. Give a hint on the expected socket type
      SocketType type=traceryInstance.getTemplate().getType();
      name="( "+type.getLabel()+" )";
    }
    setName(name);
    // Stats
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
}
