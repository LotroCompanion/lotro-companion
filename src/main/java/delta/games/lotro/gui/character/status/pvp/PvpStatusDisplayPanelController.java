package delta.games.lotro.gui.character.status.pvp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.pvp.PVPStatus;
import delta.games.lotro.lore.pvp.Rank;
import delta.games.lotro.lore.pvp.RankScale;
import delta.games.lotro.lore.pvp.RankScaleEntry;
import delta.games.lotro.lore.pvp.RankScaleKeys;
import delta.games.lotro.lore.pvp.RanksManager;

/**
 * Panel to display the PVP status of a Free People character.
 * @author DAM
 */
public class PvpStatusDisplayPanelController extends AbstractPanelController
{
  // UI
  private JLabel _rank;
  private JProgressBar _renownProgress;
  private JLabel _totalRenown;
  private JLabel _rating;
  private JLabel _kills;
  private JLabel _deaths;
  private JLabel _killsToDeathsRattio;
  private JLabel _killingBlows;
  private JLabel _killsAboveRating;
  private JLabel _killsBelowRating;
  private JLabel _highestRatingDefeated;

  /**
   * Constructor.
   */
  public PvpStatusDisplayPanelController()
  {
    super();
    initGadgets();
    JPanel panel=buildPanel();
    setPanel(panel);
  }

  private void initGadgets()
  {
    _rank=GuiFactory.buildLabel("");
    _renownProgress=buildProgressBar();
    _totalRenown=GuiFactory.buildLabel("");
    _rating=GuiFactory.buildLabel("");
    _kills=GuiFactory.buildLabel("");
    _deaths=GuiFactory.buildLabel("");
    _killsToDeathsRattio=GuiFactory.buildLabel("");
    _killingBlows=GuiFactory.buildLabel("");
    _killsAboveRating=GuiFactory.buildLabel("");
    _killsBelowRating=GuiFactory.buildLabel("");
    _highestRatingDefeated=GuiFactory.buildLabel("");
  }

  private JProgressBar buildProgressBar()
  {
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,1);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setForeground(Color.BLUE);
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(new Dimension(200,25));
    return bar;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    GridBagConstraints cLabel=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,0),0,0);
    GridBagConstraints cValue=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,5),0,0);
    // Rank
    ret.add(GuiFactory.buildLabel("Rank:"),cLabel);
    ret.add(_rank,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Renown progress
    GridBagConstraints c=new GridBagConstraints(0,cLabel.gridy,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,0),0,0);
    ret.add(GuiFactory.buildLabel("Progress to next rank:"),c);
    c=new GridBagConstraints(0,cLabel.gridy+1,2,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,0),0,0);
    ret.add(_renownProgress,c);
    cLabel.gridy+=2;cValue.gridy+=2;
    // Renown
    ret.add(GuiFactory.buildLabel("Total Renown:"),cLabel);
    ret.add(_totalRenown,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Rating
    ret.add(GuiFactory.buildLabel("Rating:"),cLabel);
    ret.add(_rating,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Kills
    ret.add(GuiFactory.buildLabel("Kills:"),cLabel);
    ret.add(_kills,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Deaths
    ret.add(GuiFactory.buildLabel("Deaths:"),cLabel);
    ret.add(_deaths,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Kills/deaths
    ret.add(GuiFactory.buildLabel("Kills/deaths:"),cLabel);
    ret.add(_killsToDeathsRattio,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Killing blows
    ret.add(GuiFactory.buildLabel("Killing Blows:"),cLabel);
    ret.add(_killingBlows,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Kills Above Rating
    ret.add(GuiFactory.buildLabel("Kills Above Rating:"),cLabel);
    ret.add(_killsAboveRating,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Kills Below Rating
    ret.add(GuiFactory.buildLabel("Kills Below Rating:"),cLabel);
    ret.add(_killsBelowRating,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Highest Rating Defeated
    cLabel.insets.bottom=5;cValue.insets.bottom=5;
    ret.add(GuiFactory.buildLabel("Highest Rating Defeated:"),cLabel);
    ret.add(_highestRatingDefeated,cValue);
    cLabel.gridy++;cValue.gridy++;
    return ret;
  }

  private String getRankLabel(Rank rank)
  {
    if (rank!=null)
    {
      return rank.getCode()+" ("+rank.getName()+")";
    }
    return "-";
  }

  /**
   * Set the data to show.
   * @param status Data to show.
   */
  public void setData(PVPStatus status)
  {
    // Rank
    Rank rank=status.getRank();
    String rankLabel=getRankLabel(rank);
    _rank.setText(rankLabel);
    // Renown progress
    int glory=status.getGloryPoints();
    int rankCode=(rank!=null)?rank.getCode():0;
    setBar(_renownProgress,glory,rankCode);
    // Renown
    _totalRenown.setText(L10n.getString(glory));
    // Rating
    float rating=status.getRating();
    _rating.setText(L10n.getString(rating,1));
    // Kills
    int kills=status.getKills();
    _kills.setText(L10n.getString(kills));
    // Deaths
    int deaths=status.getDeaths();
    _deaths.setText(L10n.getString(deaths));
    // Kills/Deaths ratio
    float killsToDeathsRatio=status.getKill2deathRatio();
    _killsToDeathsRattio.setText(L10n.getString(killsToDeathsRatio,2));
    // Killing Blows
    int killingBlows=status.getDeathBlows();
    _killingBlows.setText(L10n.getString(killingBlows));
    // Kills Above Rating
    int killsAboveRating=status.getKillsAboveRating();
    _killsAboveRating.setText(L10n.getString(killsAboveRating));
    // Kills below Rating
    int killsBelowRating=status.getKillsBelowRating();
    _killsBelowRating.setText(L10n.getString(killsBelowRating));
    // Highest Rating Defeated
    float highestRatingDefeated=status.getHighestRatingKilled();
    _highestRatingDefeated.setText(L10n.getString(highestRatingDefeated,1));
  }

  private void setBar(JProgressBar bar, int glory, int rank)
  {
    RankScale scale=RanksManager.getInstance().getRankScale(RankScaleKeys.RENOWN);
    RankScaleEntry entry=scale.getRankByCode(rank);
    int minForRank=entry.getValue();
    int value=glory-minForRank;
    int max;
    RankScaleEntry nextEntry=scale.getRankByCode(rank+1);
    if (nextEntry!=null)
    {
      max=nextEntry.getValue()-minForRank;
      bar.setMinimum(0);
      bar.setMaximum(max);
      bar.setValue(value);
      String label=value+"/"+max;
      bar.setString(label);
    }
    else
    {
      // Maxed
      bar.setMinimum(0);
      bar.setMaximum(100);
      bar.setValue(100);
      bar.setString("Max");
    }
  }

  @Override
  public void dispose()
  {
    _rank=null;
    _renownProgress=null;
    _totalRenown=null;
    _rating=null;
    _kills=null;
    _deaths=null;
    _killsToDeathsRattio=null;
    _killingBlows=null;
    _killsAboveRating=null;
    _killsBelowRating=null;
    _highestRatingDefeated=null;
  }
}
