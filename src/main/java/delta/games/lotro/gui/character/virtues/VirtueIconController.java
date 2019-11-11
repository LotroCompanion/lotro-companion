package delta.games.lotro.gui.character.virtues;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.virtues.VirtuesContributionsMgr;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.common.stats.StatDescription;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.gui.utils.StatDisplayUtils;

/**
 * Controller for a virtue icon.
 * @author DAM
 */
public class VirtueIconController
{
  private static final String NO_VIRTUE_ICON="/resources/gui/virtues/noVirtue.png";

  private VirtueId _virtueId;
  private boolean _active;
  private JLabel _label;
  private IconWithText _icon;

  /**
   * Constructor.
   * @param virtueId Virtue to use.
   * @param active Indicates if this icon represents an active virtue or not.
   */
  public VirtueIconController(VirtueId virtueId, boolean active)
  {
    _virtueId=virtueId;
    _active=active;
    _icon=buildVirtueIcon(virtueId,0);
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
    _icon=buildVirtueIcon(virtueId,0);
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
   * @return An icon with embedded text to display tier.
   */
  private IconWithText buildVirtueIcon(VirtueId virtueId, int tier)
  {
    ImageIcon icon=null;
    String text;
    if (virtueId!=null)
    {
      icon=LotroIconsManager.getVirtueIcon(virtueId.name());
      text=String.valueOf(tier);
    }
    else
    {
      icon=IconsManager.getIcon(NO_VIRTUE_ICON);
      text="";
    }
    IconWithText labeledIcon=new IconWithText(icon,text,Color.WHITE);
    return labeledIcon;
  }

  private String buildToolTip(VirtueId virtueId, int tier)
  {
    VirtuesContributionsMgr virtuesMgr=VirtuesContributionsMgr.get();
    StringBuilder sb=new StringBuilder();
    sb.append(virtueId.name()).append(EndOfLine.NATIVE_EOL);
    if (_active)
    {
      BasicStatsSet stats=virtuesMgr.getContribution(virtueId,tier,false);
      sb.append("Active:").append(EndOfLine.NATIVE_EOL);
      addStatsTooltipText(stats,sb);
    }
    BasicStatsSet passiveStats=virtuesMgr.getContribution(virtueId,tier,true);
    sb.append("Passive:").append(EndOfLine.NATIVE_EOL);
    addStatsTooltipText(passiveStats,sb);
    String text=sb.toString().trim();
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    return html;
  }

  private void addStatsTooltipText(BasicStatsSet stats, StringBuilder sb)
  {
    if (stats!=null)
    {
      for(StatDescription stat : stats.getStats())
      {
        String line=StatDisplayUtils.getStatDisplay(stat,stats);
        sb.append(line).append(EndOfLine.NATIVE_EOL);
      }
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _virtueId=null;
    _label=null;
    _icon=null;
  }
}
