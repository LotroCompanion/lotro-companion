package delta.games.lotro.gui.character.status.reputation;

import java.awt.FlowLayout;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.icons.IconsManager;
import delta.games.lotro.character.status.reputation.ReputationStatus;

/**
 * Controller for a panel that displays the rewards from reputation.
 * @author DAM
 */
public class ReputationRewardsDisplayController
{
  private JPanel _panel;
  private JLabel _lpLabel;
  private ReputationStatus _reputation;

  /**
   * Constructor.
   * @param reputationStatus Reputation to use.
   */
  public ReputationRewardsDisplayController(ReputationStatus reputationStatus)
  {
    _reputation=reputationStatus;
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new FlowLayout());
    Icon lpIcon=IconsManager.getIcon("/resources/gui/icons/LP.png");
    JLabel label=GuiFactory.buildIconLabel(lpIcon);
    panel.add(label);
    _lpLabel=GuiFactory.buildLabel("");
    panel.add(_lpLabel);
    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _reputation.update();
    int lotroPoint=_reputation.getAcquiredLotroPoints();
    _lpLabel.setText(String.valueOf(lotroPoint));
  }
}
