package delta.games.lotro.gui.character.status.summary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.status.summary.AchievementsSummary;

/**
 * Panel to show an achievements summary.
 * @author DAM
 */
public class AchievementsSummaryPanelController extends AbstractPanelController
{
  private JLabel _quests;
  private JLabel _deeds;
  private JLabel _titles;

  /**
   * Constructor.
   * @param parent Parent window.
   */
  public AchievementsSummaryPanelController(WindowController parent)
  {
    super(parent);
    _quests=GuiFactory.buildLabel("");
    _deeds=GuiFactory.buildLabel("");
    _titles=GuiFactory.buildLabel("");
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints cLabel=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,0),0,0);
    GridBagConstraints cValue=new GridBagConstraints(1,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(2,5,0,5),0,0);
    // Quests
    ret.add(GuiFactory.buildLabel("Quests:"),cLabel);
    ret.add(_quests,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Deeds
    ret.add(GuiFactory.buildLabel("Deeds:"),cLabel);
    ret.add(_deeds,cValue);
    cLabel.gridy++;cValue.gridy++;
    // Titles
    ret.add(GuiFactory.buildLabel("Titles:"),cLabel);
    ret.add(_titles,cValue);
    cLabel.gridy++;cValue.gridy++;
    return ret;
  }

  /**
   * Set the data to show.
   * @param summary Summary to show.
   */
  public void setSummary(AchievementsSummary summary)
  {
    // Quests
    Integer quests=summary.getQuestsCount();
    String questsText=(quests!=null)?L10n.getString(quests.intValue()):"?";
    _quests.setText(questsText);
    // Deeds
    Integer deeds=summary.getDeedsCount();
    String deedsText=(deeds!=null)?L10n.getString(deeds.intValue()):"?";
    _deeds.setText(deedsText);
    // Titles
    Integer titles=summary.getTitlesCount();
    String titlesText=(titles!=null)?L10n.getString(titles.intValue()):"?";
    _titles.setText(titlesText);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _quests=null;
    _deeds=null;
    _titles=null;
  }
}
