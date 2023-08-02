package delta.games.lotro.gui.character.virtues;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JLabel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconWithText;
import delta.common.ui.swing.icons.IconsManager;
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.virtues.VirtuesContributionsMgr;
import delta.games.lotro.character.virtues.VirtueDescription;
import delta.games.lotro.common.stats.StatUtils;
import delta.games.lotro.gui.LotroIconsManager;

/**
 * Controller for a virtue icon.
 * @author DAM
 */
public class VirtueIconController
{
  private static final String NO_VIRTUE_ICON="/resources/gui/virtues/noVirtue.png";

  private VirtueDescription _virtue;
  private boolean _active;
  private int _tier;
  private int _bonus;
  private JLabel _label;
  private IconWithText _icon;

  /**
   * Constructor.
   * @param virtue Virtue to use.
   * @param active Indicates if this icon represents an active virtue or not.
   */
  public VirtueIconController(VirtueDescription virtue, boolean active)
  {
    _virtue=virtue;
    _active=active;
    _icon=buildVirtueIcon(virtue);
    _label=GuiFactory.buildIconLabel(_icon);
    _label.setSize(_icon.getIconWidth(),_icon.getIconHeight());
    setTier(0);
    setBonus(0);
  }

  /**
   * Get the managed virtue.
   * @return the managed virtue.
   */
  public VirtueDescription getVirtue()
  {
    return _virtue;
  }

  /**
   * Set the managed virtue.
   * @param virtue Virtue to set or <code>null</code>.
   */
  public void setVirtue(VirtueDescription virtue)
  {
    _virtue=virtue;
    _icon=buildVirtueIcon(virtue);
    _label.setIcon(_icon);
  }

  /**
   * Set the tier of the managed virtue.
   * @param tier Tier to set.
   */
  public void setTier(int tier)
  {
    _tier=tier;
    updateUi();
  }

  /**
   * Set the bonus of the managed virtue.
   * @param bonus Bonus to set.
   */
  public void setBonus(int bonus)
  {
    _bonus=bonus;
    updateUi();
  }

  private void updateUi()
  {
    String text="";
    if ((_tier>0) || (_bonus>0))
    {
      text=String.valueOf(_tier);
      if (_bonus>0)
      {
        text=text+"+"+_bonus;
      }
    }
    _icon.setText(text);
    if (_virtue!=null)
    {
      String tooltip=buildToolTip(_virtue);
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
   * @param virtue A virtue identifier or <code>null</code>.
   * @return An icon with embedded text to display tier.
   */
  private IconWithText buildVirtueIcon(VirtueDescription virtue)
  {
    Icon icon=null;
    if (virtue!=null)
    {
      icon=LotroIconsManager.getVirtueIcon(virtue);
    }
    else
    {
      icon=IconsManager.getIcon(NO_VIRTUE_ICON);
    }
    IconWithText labeledIcon=new IconWithText(icon,"",Color.WHITE);
    return labeledIcon;
  }

  private String buildToolTip(VirtueDescription virtue)
  {
    VirtuesContributionsMgr virtuesMgr=VirtuesContributionsMgr.get();
    StringBuilder sb=new StringBuilder();
    sb.append(virtue.getName()).append(EndOfLine.NATIVE_EOL);
    if (_active)
    {
      BasicStatsSet stats=virtuesMgr.getContribution(virtue,_tier+_bonus,false);
      sb.append("Active:").append(EndOfLine.NATIVE_EOL); // I18n
      addStatsTooltipText(stats,sb);
    }
    BasicStatsSet passiveStats=virtuesMgr.getContribution(virtue,_tier,true);
    sb.append("Passive:").append(EndOfLine.NATIVE_EOL); // I18n
    addStatsTooltipText(passiveStats,sb);
    String text=sb.toString().trim();
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    return html;
  }

  private void addStatsTooltipText(BasicStatsSet stats, StringBuilder sb)
  {
    if (stats!=null)
    {
      String[] lines=StatUtils.getStatsDisplayLines(stats);
      for(String line : lines)
      {
        sb.append(line).append(EndOfLine.NATIVE_EOL);
      }
    }
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _virtue=null;
    _label=null;
    _icon=null;
  }
}
