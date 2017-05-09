package delta.games.lotro.gui.character.buffs;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import delta.common.utils.text.EndOfLine;
import delta.games.lotro.character.stats.buffs.Buff;
import delta.games.lotro.character.stats.buffs.BuffInstance;
import delta.games.lotro.gui.utils.GuiFactory;
import delta.games.lotro.gui.utils.IconsManager;
import delta.games.lotro.utils.gui.IconWithText;

/**
 * Controller for a buff icon.
 * @author DAM
 */
public class BuffIconController
{
  private BuffInstance _buff;
  private JLabel _label;
  private IconWithText _icon;

  /**
   * Constructor.
   * @param buff Buff to use.
   */
  public BuffIconController(BuffInstance buff)
  {
    _buff=buff;
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
    ImageIcon icon=IconsManager.getBuffIcon(iconFile);
    IconWithText labeledIcon=new IconWithText(icon,"",Color.WHITE);
    return labeledIcon;
  }

  private String buildToolTip()
  {
    Buff buff=_buff.getBuff();
    String label=buff.getLabel();
    String text=label;
    String html="<html>"+text.replace(EndOfLine.NATIVE_EOL,"<br>")+"</html>";
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
