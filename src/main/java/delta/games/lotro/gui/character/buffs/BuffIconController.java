package delta.games.lotro.gui.character.buffs;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffInstance;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.StatDisplayUtils;

/**
 * Controller for a buff icon.
 * @author DAM
 */
public class BuffIconController
{
  private BuffInstance _buff;
  private int _level;
  private JLabel _label;
  private IconWithText _icon;

  /**
   * Constructor.
   * @param buff Buff to use.
   * @param level Character level.
   */
  public BuffIconController(BuffInstance buff, int level)
  {
    _buff=buff;
    _level=level;
    _icon=buildBuffIcon(buff);
    _label=GuiFactory.buildIconLabel(_icon);
    _label.setSize(_icon.getIconWidth(),_icon.getIconHeight());
    update();
  }

  /**
   * Get the managed buff.
   * @return the managed buff.
   */
  public BuffInstance getBuff()
  {
    return _buff;
  }

  /**
   * Update the managed icon using the state of the managed buff.
   */
  public void update()
  {
    Integer tier=_buff.getTier();
    String text="";
    if ((tier!=null) && (tier.intValue()>0))
    {
      text=String.valueOf(tier);
    }
    _icon.setText(text);
    String tooltip=buildToolTip();
    _label.setToolTipText(tooltip);
    _label.repaint();
  }

  /**
   * Get the managed label.
   * @return the managed label.
   */
  public JLabel getLabel()
  {
    return _label;
  }

  /**
   * Get the icon for the given virtue.
   * @param buff Buff to use.
   * @return An icon with embedded text to display tier.
   */
  private IconWithText buildBuffIcon(BuffInstance buff)
  {
    String iconFile=buff.getBuff().getIcon();
    ImageIcon icon=LotroIconsManager.getBuffIcon(iconFile);
    IconWithText labeledIcon=new IconWithText(icon,"",Color.WHITE);
    return labeledIcon;
  }

  private String buildToolTip()
  {
    Buff buff=_buff.getBuff();
    BasicStatsSet stats=_buff.getStats(_level);
    String html=StatDisplayUtils.buildToolTip(buff.getLabel(),stats);
    return html;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _buff=null;
    _label=null;
    _icon=null;
  }
}
