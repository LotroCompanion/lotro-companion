package delta.games.lotro.gui.lore.titles.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ImageIcon;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a title display panel.
 * @author DAM
 */
public class TitleDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private TitleDescription _title;
  // GUI
  private JPanel _panel;

  private JLabel _icon;
  private JLabel _category;
  private JLabel _name;
  private JEditorPane _details;
  // Controllers
  private TitleReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param title Title to show.
   */
  public TitleDisplayPanelController(NavigatorWindowController parent, TitleDescription title)
  {
    super(parent);
    _title=title;
    _references=new TitleReferencesDisplayController(parent,title.getIdentifier());
  }

  @Override
  public String getTitle()
  {
    return "Title: "+getRenderedTitle();
  }

  private String getRenderedTitle()
  {
    String rawTitle=_title.getRawName();
    String title=ContextRendering.render(this,rawTitle);
    return title;
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
    }
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

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // Main data line
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Icon
      _icon=GuiFactory.buildIconLabel(null);
      panelLine.add(_icon);
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }

    // Line 2
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      // Category
      panelLine.add(GuiFactory.buildLabel("Category: "));
      _category=GuiFactory.buildLabel("");
      panelLine.add(_category);
    }

    // Description
    _details=buildDescriptionPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Description"));
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(detailsPane,c);
    c.gridy++;

    _panel=panel;
    setTitle();
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
    sb.append(toHtml(_title.getDescription()));
    sb.append("</body></html>");
    return sb.toString();
  }

  private String toHtml(String text)
  {
    text=text.trim();
    text=text.replace("\n","<br>");
    return text;
  }

  /**
   * Set the title to display.
   */
  private void setTitle()
  {
    String name=getRenderedTitle();
    // Name
    _name.setText(name);
    // Icon
    ImageIcon icon=LotroIconsManager.getTitleIcon(_title.getIconId());
    _icon.setIcon(icon);
    // Category
    String category=_title.getCategory();
    _category.setText((category!=null)?category:"");
    // Details
    _details.setText(buildHtml());
    _details.setCaretPosition(0);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _title=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    // UI
    _category=null;
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _icon=null;
    _name=null;
    _details=null;
  }
}
