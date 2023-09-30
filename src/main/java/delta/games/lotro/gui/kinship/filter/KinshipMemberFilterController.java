package delta.games.lotro.gui.kinship.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.ui.swing.text.DynamicTextEditionController;
import delta.common.ui.swing.text.TextListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.common.Genders;
import delta.games.lotro.gui.kinship.KinshipRankRenderer;
import delta.games.lotro.gui.lore.items.FilterUpdateListener;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipMember;
import delta.games.lotro.kinship.KinshipRank;
import delta.games.lotro.kinship.KinshipRoster;
import delta.games.lotro.kinship.filters.KinshipMemberFilter;
import delta.games.lotro.kinship.filters.KinshipMemberNotesFilter;
import delta.games.lotro.kinship.filters.KinshipRankFilter;

/**
 * Controller for a kinship member filter edition panel.
 * @author DAM
 */
public class KinshipMemberFilterController implements ActionListener
{
  // Data
  private Kinship _kinship;
  private KinshipMemberFilter _filter;
  // GUI
  private JPanel _panel;
  private JButton _reset;
  // -- Member attributes UI --
  private JTextField _contains;
  private ComboBoxController<Integer> _rank;
  // -- Summary filter UI --
  private CharacterSummaryFilterController _summary;
  // Controllers
  private DynamicTextEditionController _textController;
  private FilterUpdateListener _filterUpdateListener;

  /**
   * Constructor.
   * @param kinship Kinship to use.
   * @param filter Managed filter.
   * @param filterUpdateListener Filter update listener.
   */
  public KinshipMemberFilterController(Kinship kinship, KinshipMemberFilter filter, FilterUpdateListener filterUpdateListener)
  {
    _kinship=kinship;
    _filter=filter;
    _filterUpdateListener=filterUpdateListener;
    _summary=new CharacterSummaryFilterController(filter.getSummaryFilter(),filterUpdateListener);
  }

  /**
   * Get the managed filter.
   * @return the managed filter.
   */
  public Filter<KinshipMember> getFilter()
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
    _rank.selectItem(null);
    _contains.setText("");
    _summary.reset();
  }

  private void setFilter()
  {
    // Notes
    KinshipMemberNotesFilter notesFilter=_filter.getNotesFilter();
    String contains=notesFilter.getPattern();
    if (contains!=null)
    {
      _contains.setText(contains);
    }
    // Rank
    KinshipRankFilter rankFilter=_filter.getRankFilter();
    Integer rankID=rankFilter.getRankID();
    _rank.selectItem(rankID);
    // Summary
    _summary.setFilter();
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;

    // Summary
    JPanel summaryPanel=_summary.getPanel();
    Border summaryBorder=GuiFactory.buildTitledBorder("Character"); // I18n
    summaryPanel.setBorder(summaryBorder);
    GridBagConstraints c=new GridBagConstraints(0,y,2,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(summaryPanel,c);
    y++;

    // Member attributes
    JPanel memberPanel=buildMemberPanel();
    Border memberBorder=GuiFactory.buildTitledBorder("Member"); // I18n
    memberPanel.setBorder(memberBorder);
    c=new GridBagConstraints(0,y,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(memberPanel,c);

    // Reset
    _reset=GuiFactory.buildButton(Labels.getLabel("shared.reset"));
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
    // Rank filter
    {
      JLabel label=GuiFactory.buildLabel("Rank:"); // I18n
      linePanel.add(label);
      _rank=buildRankCombo(_kinship.getRoster());
      ItemSelectionListener<Integer> rankListener=new ItemSelectionListener<Integer>()
      {
        @Override
        public void itemSelected(Integer rankID)
        {
          KinshipRankFilter rankFilter=_filter.getRankFilter();
          rankFilter.setRankID(rankID);
          filterUpdated();
        }
      };
      _rank.addListener(rankListener);
      linePanel.add(_rank.getComboBox());
    }
    // Notes filter
    {
      linePanel.add(GuiFactory.buildLabel("Notes filter:")); // I18n
      _contains=GuiFactory.buildTextField("");
      _contains.setColumns(10);
      linePanel.add(_contains);
      TextListener listener=new TextListener()
      {
        @Override
        public void textChanged(String newText)
        {
          if (newText.length()==0) newText=null;
          KinshipMemberNotesFilter notesFilter=_filter.getNotesFilter();
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
   * Build a combo-box controller to choose a rank.
   * @param roster Kinship roster.
   * @return A new combo-box controller.
   */
  public static ComboBoxController<Integer> buildRankCombo(KinshipRoster roster)
  {
    ComboBoxController<Integer> ctrl=new ComboBoxController<Integer>();
    ctrl.addEmptyItem("");
    for(KinshipRank rank : roster.getRanks())
    {
      Integer code=Integer.valueOf(rank.getCode());
      String label=getRankLabel(rank);
      ctrl.addItem(code,label);
    }
    ctrl.selectItem(null);
    return ctrl;
  }

  private static String getRankLabel(KinshipRank rank)
  {
    String male=KinshipRankRenderer.render(rank,Genders.MALE);
    String female=KinshipRankRenderer.render(rank,Genders.FEMALE);
    if (Objects.equals(male,female))
    {
      return male;
    }
    return female+" / "+male;
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
    if (_rank!=null)
    {
      _rank.dispose();
      _rank=null;
    }
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
