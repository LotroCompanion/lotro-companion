package delta.games.lotro.gui.character.status.recipes;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.status.recipes.RecipesStatistics;

/**
 * Controller for a "recipes stats" display panel.
 * @author DAM
 */
public class RecipesStatsDisplayPanelController
{
  // GUI
  private JPanel _panel;

  private JLabel _autoRecipesCount;
  private JLabel _learntRecipesCount;
  private JLabel _notKnownRecipesCount;

  /**
   * Constructor.
   */
  public RecipesStatsDisplayPanelController()
  {
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
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Auto
    _autoRecipesCount=buildLabelLine(panel,c,"Auto: "); // I18n
    // Learnt
    _learntRecipesCount=buildLabelLine(panel,c,"Learnt: "); // I18n
    // Not known
    _notKnownRecipesCount=buildLabelLine(panel,c,"Not known: "); // I18n
    return panel;
  }

  private JLabel buildLabelLine(JPanel parent, GridBagConstraints c, String fieldName)
  {
    // Build line panel
    FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,5,2);
    JPanel panelLine=GuiFactory.buildPanel(flowLayout);
    // Build field label
    panelLine.add(GuiFactory.buildLabel(fieldName));
    // Build value label
    JLabel label=GuiFactory.buildLabel("");
    panelLine.add(label);
    // Add line panel to parent
    parent.add(panelLine,c);
    c.gridy++;
    return label;
  }

  /**
   * Update UI to reflect data.
   * @param stats Statistics to show.
   */
  public void updateUI(RecipesStatistics stats)
  {
    int auto=stats.getAutoRecipesCount();
    int learnt=stats.getLearntRecipesCount();
    int notKnown=stats.getNotKnownRecipesCount();
    int total=auto+learnt+notKnown;
    // Auto
    updateStateCount(auto,total,_autoRecipesCount);
    // Learnt
    updateStateCount(learnt,total,_learntRecipesCount);
    // Not known
    updateStateCount(notKnown,total,_notKnownRecipesCount);
  }

  private void updateStateCount(int count, int total, JLabel gadget)
  {
    String displayStr="0";
    if (total>0)
    {
      double percentage=(100.0*count)/total;
      String percentageStr=L10n.getString(percentage,1);
      displayStr=String.format("%s / %s (%s%%)",L10n.getString(count),L10n.getString(total),percentageStr);
    }
    gadget.setText(displayStr);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _autoRecipesCount=null;
    _learntRecipesCount=null;
    _notKnownRecipesCount=null;
  }
}
