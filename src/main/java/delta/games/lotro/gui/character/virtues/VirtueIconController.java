package delta.games.lotro.gui.character.virtues;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.virtues.VirtuesContributionsMgr;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.utils.gui.IconWithText;

/**
 * Controller for a virtue icon.
 * @author DAM
 */
public class VirtueIconController
{
  private static final String NO_VIRTUE_ICON="/resources/gui/virtues/noVirtue.png";

  private VirtueId _virtueId;
  private JLabel _label;
  private IconWithText _icon;
  private Font _font;

  /**
   * Constructor.
   * @param virtueId Virtue to use.
   * @param font Font to use to display tier.
   */
  public VirtueIconController(VirtueId virtueId, Font font)
  {
    _virtueId=virtueId;
    _font=font;
    _icon=buildVirtueIcon(virtueId,0,font);
    _label=GuiFactory.buildIconLabel(_icon);
    _label.setSize(_icon.getIconWidth(),_icon.getIconHeight());
    setTier(0);
  }

  /**
   * Get the managed virtue.
   * @return the managed virtue.
   */
  public VirtueId getVirtue()
  {
    return _virtueId;
  }

  /**
   * Set the managed virtue.
   * @param virtueId Virtue to set or <code>null</code>.
   */
  public void setVirtue(VirtueId virtueId)
  {
    _virtueId=virtueId;
    _icon=buildVirtueIcon(virtueId,0,_font);
    _label.setIcon(_icon);
    setTier(0);
  }

  /**
   * Set the tier of the managed virtue.
   * @param tier Tier to set.
   */
  public void setTier(int tier)
  {
    String text=(tier>0)?String.valueOf(tier):"";
    _icon.setText(text);
    if (_virtueId!=null)
    {
      String tooltip=buildToolTip(_virtueId,tier);
      _label.setToolTipText(tooltip);
    }
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
   * @param virtueId A virtue identifier or <code>null</code>.
   * @param tier Tier to show.
   * @param font Font to use to display tier.
   * @return An icon with embedded text to display tier.
   */
  private IconWithText buildVirtueIcon(VirtueId virtueId, int tier, Font font)
  {
    ImageIcon icon=null;
    String text;
    if (virtueId!=null)
    {
      icon=IconsManager.getVirtueIcon(virtueId.name());
      text=String.valueOf(tier);
    }
    else
    {
      icon=IconsManager.getIcon(NO_VIRTUE_ICON);
      text="";
    }
    IconWithText labeledIcon=new IconWithText(icon,font,text,Color.WHITE);
    return labeledIcon;
  }

  private String buildToolTip(VirtueId virtueId, int tier)
  {
    VirtuesContributionsMgr virtuesMgr=VirtuesContributionsMgr.get();
    BasicStatsSet stats=virtuesMgr.getContribution(virtueId,tier);
    StringBuilder sb=new StringBuilder();
    sb.append(virtueId.name()).append(EndOfLine.NATIVE_EOL);
    for(STAT stat : stats.getStats())
    {
      String name=stat.getName();
      String value=stats.getStat(stat).toString();
      sb.append(name).append(": ").append(value).append(EndOfLine.NATIVE_EOL);
    }
    String text=sb.toString().trim();
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    return html;
  }
}
