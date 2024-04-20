package delta.games.lotro.gui.character.xp;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.progress.ProgressBarController;
import delta.common.utils.l10n.L10n;
import delta.games.lotro.character.xp.XPTable;
import delta.games.lotro.character.xp.XPTableManager;
import delta.games.lotro.config.LotroCoreConfig;

/**
 * Panel to display the XP status of a character.
 * @author DAM
 */
public class XpDisplayPanelController extends AbstractPanelController
{
  // Line 1
  private JLabel _xp;
  // Line 2
  private JLabel _currentLevel;
  private ProgressBarController _bar;
  // Line 3
  private JLabel _nextLevelInfo;

  /**
   * Constructor.
   */
  public XpDisplayPanelController()
  {
    setPanel(buildPanel());
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Line 1
    _xp=GuiFactory.buildLabel("");
    GridBagConstraints c=new GridBagConstraints(0,0,3,1,1.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(2,5,2,0),0,0);
    ret.add(_xp,c);
    // Line 2
    _currentLevel=GuiFactory.buildLabel("");
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    ret.add(_currentLevel,c);
    _bar=new ProgressBarController();
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    ret.add(_bar.getProgressBar(),c);
    // Line 3
    _nextLevelInfo=GuiFactory.buildLabel("");
    c=new GridBagConstraints(0,2,3,1,0.0,0.0,GridBagConstraints.NORTHWEST,GridBagConstraints.NONE,new Insets(2,5,0,0),0,0);
    ret.add(_nextLevelInfo,c);
    return ret;
  }

  /**
   * Set the displayed XP value.
   * @param xp
   */
  public void setXP(long xp)
  {
    XPTable xpTable=XPTableManager.getInstance().getXPTable();
    int level=xpTable.getLevel(xp);
    int maxLevel=LotroCoreConfig.getInstance().getMaxCharacterLevel();
    if (level>maxLevel)
    {
      level=maxLevel;
    }
    _xp.setText("XP: "+L10n.getString(xp));
    _currentLevel.setText(L10n.getString(level));
    String nextLevelInfo;
    if (level==maxLevel)
    {
      _bar.setRange(0,1);
      _bar.setValue(1);
      _bar.setColor(Color.LIGHT_GRAY);
      _bar.getProgressBar().setString("");
      nextLevelInfo="(at level cap)";
    }
    else
    {
      long minXP=xpTable.getXp(level);
      long maxXP=xpTable.getXp(level+1);
      // TODO Shall use long values when we'll be at level ~180
      _bar.setRange(0,(int)(maxXP-minXP));
      _bar.setValue((int)(xp-minXP));
      _bar.setColor(Color.BLUE);
      long xpForLevel=maxXP-minXP;
      float percentage=(100.0f*(xp-minXP))/xpForLevel;
      String label=L10n.getString(xp-minXP)+" / "+L10n.getString(xpForLevel)+" ("+L10n.getString(percentage,1)+"%)";
      _bar.getProgressBar().setString(label);
      long diff=maxXP-xp;
      nextLevelInfo=L10n.getString(diff)+" XP to level "+(level+1);
    }
    _nextLevelInfo.setText(nextLevelInfo);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    _xp=null;
    _currentLevel=null;
    if (_bar!=null)
    {
      _bar.dispose();
      _bar=null;
    }
    _nextLevelInfo=null;
  }
}
