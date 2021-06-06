package delta.games.lotro.gui.kinship.filter;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.combobox.ComboBoxController;
import delta.common.ui.swing.combobox.ItemSelectionListener;
import delta.common.utils.collections.filters.Filter;
import delta.games.lotro.gui.items.FilterUpdateListener;
import delta.games.lotro.kinship.Kinship;
import delta.games.lotro.kinship.KinshipMember;
import delta.games.lotro.kinship.KinshipRank;
import delta.games.lotro.kinship.KinshipRoster;
import delta.games.lotro.kinship.filters.KinshipMemberFilter;
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
  private ComboBoxController<KinshipRank> _rank;
  // -- Requirements UI --
  private CharacterSummaryFilterController _summary;
  // Controllers
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
      _rank.selectItem(null);
      _summary.reset();
    }
  }

  private void setFilter()
  {
    // Rank
    KinshipRankFilter rankFilter=_filter.getRankFilter();
    KinshipRank rank=rankFilter.getRank();
    _rank.selectItem(rank);
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
    // Rank filter
    {
      JLabel label=GuiFactory.buildLabel("Rank:");
      linePanel.add(label);
      _rank=buildRankCombo(_kinship.getRoster());
      ItemSelectionListener<KinshipRank> rankListener=new ItemSelectionListener<KinshipRank>()
      {
        @Override
        public void itemSelected(KinshipRank rank)
        {
          KinshipRankFilter rankFilter=_filter.getRankFilter();
          rankFilter.setRank(rank);
          filterUpdated();
        }
      };
      _rank.addListener(rankListener);
      linePanel.add(_rank.getComboBox());
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
  public static ComboBoxController<KinshipRank> buildRankCombo(KinshipRoster roster)
  {
    ComboBoxController<KinshipRank> ctrl=new ComboBoxController<KinshipRank>();
    ctrl.addEmptyItem("");
    for(KinshipRank rank : roster.getRanks())
    {
      ctrl.addItem(rank,rank.getName());
    }
    ctrl.selectItem(null);
    return ctrl;
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
    if (_summary!=null)
    {
      _summary.dispose();
      _summary=null;
    }
    _reset=null;
  }
}
