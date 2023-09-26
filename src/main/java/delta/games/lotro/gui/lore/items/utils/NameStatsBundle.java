package delta.games.lotro.gui.lore.items.utils;

import java.util.ArrayList;
import java.util.List;

import delta.common.ui.swing.labels.LabelLineStyle;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.misc.Disposable;

/**
 * Controller for the UI items to display an icon, a name, and some stats.
 * @author DAM
 */
public class NameStatsBundle implements Disposable
{
  // Data
  private String _name;
  private List<String> _stats;
  // UI
  protected MultilineLabel2 _lines;

  /**
   * Constructor.
   */
  public NameStatsBundle()
  {
    _stats=new ArrayList<String>();
    _lines=new MultilineLabel2();
    LabelLineStyle defaultStyle=LabelLineStyle.DEFAULT_LINE_STYLE.setFontSize(10);
    _lines.setDefaultStyle(defaultStyle);
    _lines.setLineStyle(0,LabelLineStyle.DEFAULT_LINE_STYLE);
  }

  /**
   * Get the managed gadget.
   * @return a multi-lines label.
   */
  public MultilineLabel2 getLinesGadget()
  {
    return _lines;
  }

  /**
   * Set the name.
   * @param name Name to set (<code>null</code> if no name line).
   */
  public void setName(String name)
  {
    _name=name;
    updateUI();
  }

  /**
   * Set the stat lines.
   * @param stats Stats to set.
   */
  public void setStats(String[] stats)
  {
    _stats.clear();
    for(String stat : stats)
    {
      _stats.add(stat);
    }
    updateUI();
  }

  private void updateUI()
  {
    int nbLines=((_name!=null)?1:0)+(_stats.size());
    String[] text=new String[nbLines];
    int index=0;
    if (_name!=null)
    {
      text[index]=_name;
      index++;
    }
    if (_stats.size()>0)
    {
      for(String stat : _stats)
      {
        text[index]=stat;
        index++;
      }
    }
    _lines.setText(text);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // UI
    _lines=null;
  }
}
