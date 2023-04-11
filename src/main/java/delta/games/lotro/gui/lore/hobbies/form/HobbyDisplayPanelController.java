package delta.games.lotro.gui.lore.hobbies.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.HyperLinkController;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.lore.billingGroups.form.BillingGroupReferencesDisplayController;
import delta.games.lotro.gui.lore.titles.TitleUiUtils;
import delta.games.lotro.gui.utils.ItemDisplayGadgets;
import delta.games.lotro.lore.hobbies.HobbyDescription;
import delta.games.lotro.lore.hobbies.HobbyTitleEntry;
import delta.games.lotro.lore.items.Item;
import delta.games.lotro.lore.titles.TitleDescription;

/**
 * Controller for a hobby display panel.
 * @author DAM
 */
public class HobbyDisplayPanelController implements NavigablePanelController
{
  // Controllers
  private NavigatorWindowController _parent;
  private List<HyperLinkController> _links;
  private List<ItemDisplayGadgets> _items;
  // Data
  private HobbyDescription _hobby;
  // GUI
  private JPanel _panel;

  private JLabel _name;
  private JEditorPane _details;
  // Controllers
  private BillingGroupReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param hobby Hobby to show.
   */
  public HobbyDisplayPanelController(NavigatorWindowController parent, HobbyDescription hobby)
  {
    _parent=parent;
    _links=new ArrayList<HyperLinkController>();
    _items=new ArrayList<ItemDisplayGadgets>();
    _hobby=hobby;
    _references=new BillingGroupReferencesDisplayController(parent,_hobby.getIdentifier());
    _panel=build();
  }

  @Override
  public String getTitle()
  {
    return "Hobby: "+_hobby.getName();
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

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // Center
    Component center=buildCenter();
    if (center!=null)
    {
      c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
      panel.add(center,c);
    }
    return panel;
  }

  private JPanel buildCenter()
  {
    JPanel panel=null;
    JEditorPane references=_references.getComponent();
    if (references!=null)
    {
      panel=GuiFactory.buildPanel(new BorderLayout());
      panel.add(references,BorderLayout.CENTER);
      panel.setBorder(GuiFactory.buildTitledBorder("References"));
    }
    return panel;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    int y=0;
    GridBagConstraints c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
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
    y++;

    // Items
    JPanel itemsPanel=buildItemsTable();
    itemsPanel.setBorder(GuiFactory.buildTitledBorder("Items"));
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(itemsPanel,c);
    y++;

    // Titles
    JPanel titlesPanel=buildTitlesTable();
    titlesPanel.setBorder(GuiFactory.buildTitledBorder("Titles"));
    c=new GridBagConstraints(0,y,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(titlesPanel,c);
    y++;

    // Details
    _details=buildDescriptionPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Description"));
    c=new GridBagConstraints(0,y,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(detailsPane,c);

    _panel=panel;
    setHobby();
    return _panel;
  }

  private JEditorPane buildDescriptionPane()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(300,100));
    editor.setOpaque(false);
    return editor;
  }

  private String buildHtml()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    sb.append(toHtml(_hobby.getDescription()));
    sb.append("</body></html>");
    return sb.toString();
  }

  private String toHtml(String text)
  {
    text=text.trim();
    text=text.replace("\n","<br>");
    return text;
  }

  private JPanel buildItemsTable()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    for(Item item : _hobby.getItems())
    {
      c.gridx=0;
      c.gridy++;
      ItemDisplayGadgets gadgets=new ItemDisplayGadgets(_parent,item.getIdentifier(),1,"");
      _items.add(gadgets);
      // Icon
      ret.add(gadgets.getIcon(),c);
      c.gridx++;
      // Link
      ret.add(gadgets.getName(),c);
      c.gridx++;
    }
    return ret;
  }

  private JPanel buildTitlesTable()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Headers
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(GuiFactory.buildLabel("Proficiency"),c);c.gridx++;
    ret.add(GuiFactory.buildLabel("Title"),c);c.gridx++;
    for(HobbyTitleEntry entry : _hobby.getTitles())
    {
      c.gridx=0;
      c.gridy++;
      // Proficiency
      int proficiency=entry.getProficiency();
      ret.add(GuiFactory.buildLabel(String.valueOf(proficiency)),c);
      c.gridx++;
      // Title
      TitleDescription title=entry.getTitle();
      JLabel titleLabel=GuiFactory.buildLabel(title.getName());
      HyperLinkController titleCtrl=TitleUiUtils.buildTitleLink(_parent,title,titleLabel);
      ret.add(titleCtrl.getLabel(),c);
      c.gridx++;
    }
    return ret;
  }

  /**
   * Set the hobby to display.
   */
  private void setHobby()
  {
    String name=_hobby.getName();
    // Name
    _name.setText(name);
    // Details
    _details.setText(buildHtml());
    int nbTitles=_hobby.getTitles().size();
    _details.setPreferredSize(new Dimension(300,50+(30*nbTitles)));
    _details.setCaretPosition(0);
  }

  @Override
  public void dispose()
  {
    _parent=null;
    // Data
    _hobby=null;
    // Controllers
    if (_links!=null)
    {
      for(HyperLinkController link : _links)
      {
        link.dispose();
      }
      _links.clear();
      _links=null;
    }
    if (_items!=null)
    {
      for(ItemDisplayGadgets gadgets : _items)
      {
        gadgets.dispose();
      }
      _items.clear();
      _items=null;
    }
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _name=null;
    _details=null;
  }
}
