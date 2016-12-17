package delta.games.lotro.gui.character.virtues;

import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.games.lotro.common.VirtueId;
import delta.games.lotro.gui.utils.GuiFactory;

/**
 * Gather UI items to edit a single virtue.
 * @author DAM
 */
public class VirtueEditionUiController implements ActionListener
{
  private static final int MAX_TIER=19;

  private int _tier;
  private VirtueIconController _iconController;
  private JButton _plus;
  private JButton _minus;

  /**
   * Constructor.
   * @param virtueId Virtue to use.
   * @param panel Parent panel.
   */
  public VirtueEditionUiController(VirtueId virtueId, JPanel panel)
  {
    _tier=0;
    Font font=panel.getFont();
    _iconController=new VirtueIconController(virtueId,font);
    panel.add(_iconController.getLabel());
    _plus=buildButton('+');
    panel.add(_plus);
    _minus=buildButton('-');
    panel.add(_minus);
  }

  /**
   * Set the location of the managed UI items in the parent panel.
   * @param x Base horizontal location.
   * @param y Base vertical location.
   */
  public void setLocation(int x, int y)
  {
    JLabel label=_iconController.getLabel();
    label.setLocation(x,y);
    int buttonX=x+label.getWidth()+2;
    int buttonY=y+(label.getHeight()-_plus.getHeight())/2;
    _plus.setLocation(buttonX,buttonY-7);
    _minus.setLocation(buttonX,buttonY+7);
  }

  private JButton buildButton(char text)
  {
    JButton button=GuiFactory.buildButton(String.valueOf(text));
    button.setSize(15,15);
    button.setMargin(new Insets(1,1,1,1));
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.addActionListener(this);
    return button;
  }

  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (_plus==source)
    {
      if (_tier<MAX_TIER)
      {
        _tier++;
        updateUi();
      }
    }
    if (_minus==source)
    {
      if (_tier>0)
      {
        _tier--;
        updateUi();
      }
    }
  }

  /**
   * Get the displayed tier.
   * @return a tier value.
   */
  public int getTier()
  {
    return _tier;
  }

  /**
   * Set the tier for the managed virtue.
   * @param tier Tier to set.
   */
  public void setTier(int tier)
  {
    _tier=tier;
    updateUi();
  }

  private void updateUi()
  {
    _iconController.setTier(_tier);
    _minus.setEnabled(_tier>0);
    _plus.setEnabled(_tier<MAX_TIER);
  }
}
