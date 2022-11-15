package delta.games.lotro.gui.character.status.levelling;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.text.dates.DateCodec;
import delta.common.ui.swing.text.dates.DateEditionController;
import delta.games.lotro.character.status.levelling.LevelHistory;
import delta.games.lotro.gui.utils.l10n.DateFormat;

/**
 * Controller for a level history edition panel.
 * @author DAM
 */
public class LevelHistoryEditionPanelController
{
  private static final int LEVELS_BY_TAB=50;
  private static final int LEVELS_BY_COLUMNS=10;
  private JPanel _panel;
  private HashMap<Integer,DateEditionController> _editors;
  private int _maxLevel;
  private LevelHistory _data;

  /**
   * Constructor.
   * @param maxLevel Maximum level to edit.
   * @param data Data to edit.
   */
  public LevelHistoryEditionPanelController(int maxLevel, LevelHistory data)
  {
    _maxLevel=maxLevel;
    _data=data;
    _editors=new HashMap<Integer,DateEditionController>();
    _panel=buildPanel();
    initData();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Update internal data from GUI.
   */
  public void updateData()
  {
    for(int level=1;level<=_maxLevel;level++)
    {
      DateEditionController editor=_editors.get(Integer.valueOf(level));
      Long date=editor.getDate();
      _data.setLevel(level,date);
    }
  }

  private void initData()
  {
    for(int level=1;level<=_maxLevel;level++)
    {
      Long date=_data.getDate(level);
      DateEditionController editor=_editors.get(Integer.valueOf(level));
      editor.setDate(date);
    }
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new BorderLayout());
    DateCodec codec=DateFormat.getDateTimeCodec();
    for(int level=1;level<=_maxLevel;level++)
    {
      DateEditionController editor=new DateEditionController(codec);
      _editors.put(Integer.valueOf(level),editor);
    }
    JTabbedPane pane=GuiFactory.buildTabbedPane();
    int nbLevels=_maxLevel;
    int nbTabs=(nbLevels/LEVELS_BY_TAB)+(((nbLevels%LEVELS_BY_TAB)==0)?0:1);
    int currentLevel=1;
    for(int i=0;i<nbTabs;i++)
    {
      int endLevel=Math.min(nbLevels,currentLevel+LEVELS_BY_TAB-1);
      initTab(pane,currentLevel,endLevel);
      currentLevel=endLevel+1;
    }
    pane.setSelectedIndex(nbTabs-1);
    panel.add(pane,BorderLayout.CENTER);
    return panel;
  }

  private void initTab(JTabbedPane pane, int startLevel, int endLevel)
  {
    JPanel panel=GuiFactory.buildBackgroundPanel(new GridBagLayout());
    int nbLevels=endLevel-startLevel+1;
    int nbColumns=(nbLevels/LEVELS_BY_COLUMNS)+(((nbLevels%LEVELS_BY_COLUMNS)==0)?0:1);
    int currentLevel=startLevel;
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    for(int i=0;i<nbColumns;i++)
    {
      int row=0;
      while ((row<LEVELS_BY_COLUMNS) && (currentLevel<=endLevel))
      {
        DateEditionController editor=_editors.get(Integer.valueOf(currentLevel));
        JLabel label=GuiFactory.buildLabel(String.valueOf(currentLevel)+": ");
        c.gridx=i*2;
        c.gridy=row;
        panel.add(label,c);
        c.gridx++;
        panel.add(editor.getTextField(),c);
        currentLevel++;
        row++;
      }
    }
    // Add glue (bottom and east)
    JPanel bottomGlue=GuiFactory.buildPanel(new BorderLayout());
    GridBagConstraints bottomGlueC=new GridBagConstraints(0,LEVELS_BY_COLUMNS,nbColumns*2+1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(bottomGlue,bottomGlueC);
    JPanel eastGlue=GuiFactory.buildPanel(new BorderLayout());
    GridBagConstraints eastGlueC=new GridBagConstraints(nbColumns*2,0,1,LEVELS_BY_COLUMNS,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(eastGlue,eastGlueC);
    // Add tab
    String title=String.valueOf(startLevel)+"-"+endLevel;
    pane.add(title,panel);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    if (_editors!=null)
    {
      for(DateEditionController editor : _editors.values())
      {
        editor.dispose();
      }
      _editors.clear();
      _editors=null;
    }
    _data=null;
  }
}
