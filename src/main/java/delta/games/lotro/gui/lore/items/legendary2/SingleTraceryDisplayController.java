package delta.games.lotro.gui.lore.items.legendary2;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
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
  // UI
  private JLabel _advancement;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param type Socket type.
   */
  public SingleTraceryDisplayController(WindowController parent, SocketType type)
  {
    super();
    _type=type;
    _advancement=GuiFactory.buildLabel("");
    setIconController(new ItemIconController(parent));
    Integer characterLevel=parent.getContextProperty(ContextPropertyNames.CHARACTER_LEVEL,Integer.class);
    _characterLevel=(characterLevel!=null)?characterLevel.intValue():Config.getInstance().getMaxCharacterLevel();
    // Initialize with nothing slotted
    setTracery(null,1);
  }

  /**
   * Get the advancement label.
   * @return the advancement label.
   */
  public JLabel getAdvancement()
  {
    return _advancement;
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
    // Advancement
    if ((traceryInstance!=null) && (tracery!=null))
    {
      int currentItemLevel=traceryInstance.getItemLevel();
      int maxItemLevel=tracery.getMaxItemLevel();
      int max=Math.min(maxItemLevel,itemLevel);
      if (currentItemLevel==max)
      {
        _advancement.setText(String.valueOf(currentItemLevel));
      }
      else
      {
        _advancement.setText(currentItemLevel+"/"+max);
      }
    }
    else
    {
      _advancement.setText("-");
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
      List<String> lines=StatUtils.getStatsForDisplay(stats);
      setStats(lines);
    }
    else
    {
      setStats(Collections.emptyList());
    }
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _advancement=null;
    _type=null;
  }
}
