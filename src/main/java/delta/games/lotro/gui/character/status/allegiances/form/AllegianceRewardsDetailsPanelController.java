package delta.games.lotro.gui.character.status.allegiances.form;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.windows.WindowController;
import delta.games.lotro.character.status.allegiances.AllegianceStatus;
import delta.games.lotro.gui.common.rewards.form.RewardsPanelController;
import delta.games.lotro.lore.allegiances.AllegianceDescription;
import delta.games.lotro.lore.allegiances.Points2LevelCurve;
import delta.games.lotro.lore.deeds.DeedDescription;

/**
 * Controller for a panel to display rewards details.
 * @author DAM
 */
public class AllegianceRewardsDetailsPanelController
{
  // Data
  private AllegianceStatus _status;
  private AllegianceRewardsFilter _filter;
  // Controllers
  private List<RewardsPanelController> _rewards;
  // UI
  private JPanel _panel;

  /**
   * Constructor.
   * @param parent Parent controller.
   * @param status Status to display.
   * @param filter Allegiance filter.
   */
  public AllegianceRewardsDetailsPanelController(WindowController parent, AllegianceStatus status, AllegianceRewardsFilter filter)
  {
    _status=status;
    _filter=filter;
    _rewards=new ArrayList<RewardsPanelController>();
    _panel=GuiFactory.buildPanel(new GridBagLayout());
    buildRewardsPanels(parent,status);
    updatePanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  private void buildRewardsPanels(WindowController parent, AllegianceStatus status)
  {
    int maxLevel=status.getMaxLevel();
    AllegianceDescription allegiance=status.getAllegiance();
    List<DeedDescription> deeds=allegiance.getDeeds();
    for(int i=0;i<maxLevel;i++)
    {
      DeedDescription deed=deeds.get(i);
      RewardsPanelController panel=new RewardsPanelController(parent,deed.getRewards());
      _rewards.add(panel);
    }
  }

  /**
   * Update the contents of the managed panel according to the filter status.
   */
  public void updatePanel()
  {
    _panel.removeAll();
    int nbLevels=_status.getMaxLevel();
    int y=0;
    for(int i=1;i<=nbLevels;i++)
    {
      AllegianceRewardState state=AllegianceRewardsUtils.getState(_status,i);
      boolean ok=_filter.accept(state);
      if (!ok)
      {
        continue;
      }
      // Label
      String text=getLabel(i,state);
      int top=(y==0)?5:0;
      // Rewards
      GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(top,5,5,5),0,0);
      JPanel rewardsPanel=_rewards.get(i-1).getPanel();
      rewardsPanel.setBorder(GuiFactory.buildTitledBorder(text));
      _panel.add(rewardsPanel,c);
      y++;
    }
    // Push everything on left
    Component glue=Box.createGlue();
    GridBagConstraints c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    _panel.add(glue,c);
    _panel.revalidate();
    _panel.repaint();
  }

  private String getLabel(int level, AllegianceRewardState state)
  {
    StringBuilder sb=new StringBuilder();
    sb.append("Level ").append(level); // I18n
    sb.append(": ");
    if (state==AllegianceRewardState.CLAIMED)
    {
      sb.append("claimed"); // I18n
    }
    else if (state==AllegianceRewardState.UNLOCKED)
    {
      sb.append("unlocked"); // I18n
    }
    else if (state==AllegianceRewardState.FUTURE)
    {
      Points2LevelCurve curve=_status.getPoints2LevelCurve();
      if (curve!=null)
      {
        int minPoints=curve.getMinPointsForLevel(level);
        int currentPoints=_status.getPointsEarned();
        int missingPoints=minPoints-currentPoints;
        if (missingPoints<0)
        {
          sb.append('?');
        }
        else
        {
          sb.append("need ").append(missingPoints).append(" points"); // I18n
        }
      }
      else
      {
        sb.append("not started"); // I18n
      }
    }
    return sb.toString();
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _status=null;
    _filter=null;
    // Controllers
    if (_rewards!=null)
    {
      for(RewardsPanelController reward : _rewards)
      {
        reward.dispose();
      }
      _rewards.clear();
      _rewards=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
  }
}
