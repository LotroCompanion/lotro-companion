package delta.games.lotro.gui.stats.reputation;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.reputation.ReputationData;
import delta.games.lotro.character.reputation.ReputationDeedStatus;
import delta.games.lotro.character.reputation.ReputationDeedsComputer;
import delta.games.lotro.lore.reputation.FactionsRegistry;

/**
 * Controller for a panel that displays the status of reputation deeds.
 * @author DAM
 */
public class ReputationDeedsDisplayController
{
  private JPanel _panel;
  private List<JLabel> _countLabels;
  private ReputationData _reputation;

  /**
   * Constructor.
   * @param reputationData Reputation to use.
   */
  public ReputationDeedsDisplayController(ReputationData reputationData)
  {
    _reputation=reputationData;
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
    List<String> deedNames=registry.getFactionDeeds();
    for(String deedName : deedNames)
    {
      JLabel label=GuiFactory.buildLabel(deedName+":");
      label.setToolTipText("Status for the '"+deedName+"' deed");
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
    ReputationDeedsComputer deedComputer=new ReputationDeedsComputer();
    List<ReputationDeedStatus> deedStatuses=deedComputer.compute(_reputation);
    int index=0;
    for(ReputationDeedStatus deed : deedStatuses)
    {
      int acquired=deed.getAcquiredCount();
      int total=deed.getTotalCount(); 
      String deedLabel=acquired+" / "+total;
      _countLabels.get(index).setText(deedLabel);
      index++;
    }
  }
}
