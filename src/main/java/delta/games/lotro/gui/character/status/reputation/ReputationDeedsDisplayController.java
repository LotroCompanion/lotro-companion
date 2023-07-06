package delta.games.lotro.gui.character.status.reputation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.status.reputation.ReputationDeedStatus;
import delta.games.lotro.character.status.reputation.ReputationDeedsStatus;
import delta.games.lotro.character.status.reputation.ReputationStatus;
import delta.games.lotro.lore.reputation.FactionsRegistry;
import delta.games.lotro.lore.reputation.ReputationDeed;

/**
 * Controller for a panel that displays the status of reputation deeds.
 * @author DAM
 */
public class ReputationDeedsDisplayController
{
  private JPanel _panel;
  private List<JLabel> _countLabels;
  private ReputationStatus _reputation;

  /**
   * Constructor.
   * @param reputationStatus Reputation to use.
   */
  public ReputationDeedsDisplayController(ReputationStatus reputationStatus)
  {
    _reputation=reputationStatus;
    _countLabels=new ArrayList<JLabel>();
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    FactionsRegistry registry=FactionsRegistry.getInstance();

    int y=0;
    List<ReputationDeed> deeds=registry.getReputationDeeds();
    for(ReputationDeed deed : deeds)
    {
      String deedName=deed.getName();
      JLabel label=GuiFactory.buildLabel(deedName+":");
      label.setToolTipText("Status for the '"+deedName+"' deed"); // I18n
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
      panel.add(label,c);
      c.gridx++;
      c.weightx=1.0;
      c.fill=GridBagConstraints.HORIZONTAL;
      JLabel countLabel=GuiFactory.buildLabel("NN / NN");
      panel.add(countLabel,c);
      _countLabels.add(countLabel);
      y++;
    }
    return panel;
  }

  /**
   * Update display.
   */
  public void update()
  {
    _reputation.update();
    ReputationDeedsStatus deedsStatus=_reputation.getDeedsStatus();
    List<ReputationDeedStatus> deedStatuses=deedsStatus.getAllDeedStatuses();
    int index=0;
    for(ReputationDeedStatus deedStatus : deedStatuses)
    {
      int acquired=deedStatus.getAcquiredCount();
      int total=deedStatus.getTotalCount(); 
      String deedLabel=acquired+" / "+total;
      _countLabels.get(index).setText(deedLabel);
      index++;
    }
  }
}
