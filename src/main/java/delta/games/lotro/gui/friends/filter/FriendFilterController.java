package delta.games.lotro.gui.friends.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.character.social.friends.Friend;
import delta.games.lotro.character.social.friends.filters.FriendFilter;
import delta.games.lotro.character.social.friends.filters.FriendNoteFilter;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;

/**
 * Controller for a kinship member filter edition panel.
 * @author DAM
 */
public class FriendFilterController implements ActionListener
{
  // Data
  private FriendFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Member attributes UI --
  private JTextField _contains;
  // -- Summary filter UI --
  private FriendSummaryFilterController _summary;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public FriendFilterController(FriendFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    _summary=new FriendSummaryFilterController(filter,filterUpdateListener);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<Friend> getFilter()
  {
    return _filter;
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    if (_panel==null)
    {
      _panel=build();
      setFilter();
      filterUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed filter has been updated.
   */
  private void filterUpdated()
  {
    _filterUpdateListener.filterUpdated();
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    Object source=e.getSource();
    if (source==_reset)
    {
      reset();
    }
  }

  /**
   * Reset all gadgets.
   */
  private void reset()
  {
    _contains.setText("");
    _summary.reset();
  }

  private void setFilter()
  {
    // Notes
    FriendNoteFilter notesFilter=_filter.getNotesFilter();
    String contains=notesFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Summary
    _summary.setFilter();
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Summary
    JPanel summaryPanel=_summary.getPanel();
    Border summaryBorder=GuiFactory.buildTitledBorder("Character");
    summaryPanel.setBorder(summaryBorder);
    GridBagConstraints c=new GridBagConstraints(0,y,2,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(summaryPanel,c);
    y++;

    // Member attributes
    JPanel memberPanel=buildMemberPanel();
    Border memberBorder=GuiFactory.buildTitledBorder("Member");
    memberPanel.setBorder(memberBorder);
    c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(memberPanel,c);

    // Reset
    _reset=GuiFactory.buildButton("Reset");
    _reset.addActionListener(this);
    c=new GridBagConstraints(1,y,1,1,1.0,0,GridBagConstraints.SOUTHEAST,GridBagConstraints.NONE,new Insets(0,5,5,5),0,0);
    panel.add(_reset,c);
    y++;

    return panel;
  }

  private JPanel buildMemberPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    JPanel linePanel=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEADING,5,0));
    // Notes filter
    {
      linePanel.add(GuiFactory.buildLabel("Notes filter:"));
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      linePanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          FriendNoteFilter notesFilter=_filter.getNotesFilter();
          notesFilter.setPattern(newText);
          filterUpdated();
        }
      };
      _textController=new DynamicTextEditionController(_contains,listener);
    }
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,5,0),0,0);
    panel.add(linePanel,c);
    y++;
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _filter=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_textController!=null)
    {
      _textController.dispose();
      _textController=null;
    }
    if (_summary!=null)
    {
      _summary.dispose();
      _summary=null;
    }
    _reset=null;
    _contains=null;
  }
}
