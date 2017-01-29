package delta.games.lotro.gui.character.tomes;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.STAT;
import delta.games.lotro.character.stats.tomes.TomesSet;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.utils.gui.IconWithText;

/**
 * Controller for a stat tome icon.
 * @author DAM
 */
public class TomeIconController
{
  private TomesSet _tomes;
  private STAT _stat;
  private JLabel _label;
  private IconWithText _icon;

  /**
   * Constructor.
   * @param tomes Tomes to edit.
   * @param stat Stat to use.
   * @param font Font to use to display tier.
   */
  public TomeIconController(TomesSet tomes, STAT stat, Font font)
  {
    _tomes=tomes;
    _stat=stat;
    _icon=buildStatIcon(stat,font);
    _label=GuiFactory.buildIconLabel(_icon);
    _label.setSize(_icon.getIconWidth(),_icon.getIconHeight());
    update();
  }

  /**
   * Get the managed stat.
   * @return the managed stat.
   */
  public STAT getStat()
  {
    return _stat;
  }

  /**
   * Update the managed icon using the state of the managed tome.
   */
  public void update()
  {
    int rank=_tomes.getTomeRank(_stat);
    String text="";
    if (rank>0)
    {
      text=String.valueOf(rank);
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
   * Get the icon for the given tome.
   * @param stat Stat to use.
   * @param font Font to use to display tier.
   * @return An icon with embedded text to display tier.
   */
  private IconWithText buildStatIcon(STAT stat, Font font)
  {
    ImageIcon icon=IconsManager.getTomeIcon(stat);
    IconWithText labeledIcon=new IconWithText(icon,font,"",Color.WHITE);
    return labeledIcon;
  }

  private String buildToolTip()
  {
    String label=_stat.getName();
    String text=label;
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
    return html;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    _stat=null;
    _label=null;
    _icon=null;
  }
}
