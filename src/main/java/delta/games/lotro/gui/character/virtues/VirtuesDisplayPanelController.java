package delta.games.lotro.gui.character.virtues;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.BasicStatsSet;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.virtues.VirtuesContributionsMgr;
import delta.games.lotro.character.stats.virtues.VirtuesSet;
import delta.games.lotro.common.VirtueId;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.utils.gui.IconWithText;

/**
 * @author DAM
 */
public class VirtuesDisplayPanelController
{
  private static final String NO_VIRTUE_ICON="/resources/gui/virtues/noVirtue.png";
  private static final int MAX_VIRTUES=5;
  private JLabel[] _icons;
  private JPanel _panel;
  private VirtuesContributionsMgr _virtuesStatsMgr;

  /**
   * Constructor.
   */
  public VirtuesDisplayPanelController()
  {
    _virtuesStatsMgr=new VirtuesContributionsMgr();
    _icons=new JLabel[MAX_VIRTUES];
    _panel=build();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new FlowLayout(FlowLayout.LEFT));
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      _icons[i]=GuiFactory.buildIconLabel(null);
      panel.add(_icons[i]);
    }
    return panel;
  }

  /**
   * Set virtues to show.
   * @param virtues Virtues to show.
   */
  public void setVirtues(VirtuesSet virtues)
  {
    for(int i=0;i<MAX_VIRTUES;i++)
    {
      Icon icon=null;
      String toolTip=null;
      VirtueId virtueId=virtues.getSelectedVirtue(i);
      if (virtueId!=null)
      {
        int tier=virtues.getVirtueRank(virtueId);
        icon=buildVirtueIcon(virtueId,tier,getFont());
        toolTip=buildToolTip(virtueId,tier);
      }
      else
      {
        icon=IconsManager.getIcon(NO_VIRTUE_ICON);
      }
      _icons[i].setIcon(icon);
      _icons[i].setToolTipText(toolTip);
    }
  }

  private String buildToolTip(VirtueId virtueId, int tier)
  {
    BasicStatsSet stats=_virtuesStatsMgr.getContribution(virtueId,tier);
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

  private Font getFont()
  {
    return getPanel().getFont();
  }

  private Icon buildVirtueIcon(VirtueId id, int tier, Font font)
  {
    // getPanel().getFont()
    ImageIcon virtueIcon=IconsManager.getVirtueIcon(id.name());
    String tierStr=String.valueOf(tier);
    Icon labeledVirtueIcon=new IconWithText(virtueIcon,font,tierStr,Color.WHITE);
    return labeledVirtueIcon;
  }
}
