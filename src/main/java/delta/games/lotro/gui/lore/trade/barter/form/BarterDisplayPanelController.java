package delta.games.lotro.gui.lore.trade.barter.form;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.common.ui.swing.tables.GenericTableController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.common.requirements.RequirementsUtils;
import delta.games.lotro.lore.agents.npcs.NpcDescription;
import delta.games.lotro.lore.trade.barter.BarterEntry;
import delta.games.lotro.lore.trade.barter.BarterNpc;

/**
 * Controller for a barterer display panel.
 * @author DAM
 */
public class BarterDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private BarterNpc _barterer;
  // GUI
  private JLabel _name;
  private JLabel _requirements;
  private BarterEntriesTableController _entries;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param barterer Barterer to show.
   */
  public BarterDisplayPanelController(NavigatorWindowController parent, BarterNpc barterer)
  {
    super(parent);
    _barterer=barterer;
    JPanel panel=build();
    setPanel(panel);
    setData();
  }

  @Override
  public String getTitle()
  {
    return "Barterer: "+_barterer.getNpc().getName();
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }
    // Requirements
    _requirements=buildLabelLine(panel,c,"Requirements: ");

    // Barter entries table
    initBarterEntriesTable();
    // Scroll
    JScrollPane itemsPane=GuiFactory.buildScrollPane(_entries.getTable());
    itemsPane.setBorder(GuiFactory.buildTitledBorder("Barters"));
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(itemsPane,c);
    c.gridy++;

    setData();
    return panel;
  }

  private void initBarterEntriesTable()
  {
    TypedProperties prefs=null;
    WindowController parent=getParentWindowController();
    if (parent!=null)
    {
      prefs=parent.getUserProperties("BartererDisplay");
    }
    final List<BarterEntry> items=_barterer.getEntries();
    _entries=new BarterEntriesTableController(this,prefs,null,items);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent event)
      {
        String action=event.getActionCommand();
        if (GenericTableController.DOUBLE_CLICK.equals(action))
        {
          BarterEntry entry=(BarterEntry)event.getSource();
          int index=items.indexOf(entry);
          showBarterEntry(entry,index);
        }
      }
    };
    _entries.addActionListener(al);
  }

  private void showBarterEntry(BarterEntry barterEntry, int index)
  {
    PageIdentifier ref=ReferenceConstants.getBarterEntryReference(_barterer.getIdentifier(),index);
    getParent().navigateTo(ref);
  }

  private JLabel buildLabelLine(JPanel parent, GridBagConstraints c, String fieldName)
  {
    // Build line panel
    JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
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
   * Set the barterer to display.
   */
  private void setData()
  {
    NpcDescription npc=_barterer.getNpc();
    // Name & title
    String fullName=npc.getName();
    String title=npc.getTitle();
    if (title.length()>0)
    {
      fullName=fullName+" ("+title+")";
    }
    _name.setText(fullName);
    // Requirements
    String requirements=RequirementsUtils.buildRequirementString(this,_barterer.getRequirements());
    if (requirements.length()==0) requirements="-";
    _requirements.setText(requirements);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    super.dispose();
    // Data
    _barterer=null;
    // GUI
    _name=null;
    _requirements=null;
    // Controllers
    if (_entries!=null)
    {
      _entries.dispose();
      _entries=null;
    }
  }
}
