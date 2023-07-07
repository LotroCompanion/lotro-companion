package delta.games.lotro.gui.lore.billingGroups.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.lore.billingGroups.BillingGroupDescription;
import delta.games.lotro.lore.titles.TitleDescription;
import delta.games.lotro.utils.gui.HtmlUtils;
import delta.games.lotro.utils.strings.ContextRendering;

/**
 * Controller for a billing group display panel.
 * @author DAM
 */
public class BillingGroupDisplayPanelController implements NavigablePanelController
{
  // Controllers
  private NavigatorWindowController _parent;
  // Data
  private BillingGroupDescription _billingGroup;
  // GUI
  private JPanel _panel;

  private JLabel _name;
  private JEditorPane _details;
  // Controllers
  private BillingGroupReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param billingGroup Billing group to show.
   */
  public BillingGroupDisplayPanelController(NavigatorWindowController parent, BillingGroupDescription billingGroup)
  {
    _parent=parent;
    _billingGroup=billingGroup;
    _references=new BillingGroupReferencesDisplayController(parent,_billingGroup.getIdentifier());
  }

  @Override
  public String getTitle()
  {
    return "Account feature: "+_billingGroup.getName(); // 18n
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
      panel.setBorder(GuiFactory.buildTitledBorder("References")); // 18n
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
      // Name
      _name=GuiFactory.buildLabel("");
      _name.setFont(_name.getFont().deriveFont(16f).deriveFont(Font.BOLD));
      panelLine.add(_name);
    }

    // Details
    _details=buildDetailsPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Titles")); // 18n
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(detailsPane,c);
    c.gridy++;

    _panel=panel;
    setBillingGroup();
    return _panel;
  }

  private JEditorPane buildDetailsPane()
  {
    JEditorPane editor=new JEditorPane("text/html","");
    editor.setEditable(false);
    editor.setPreferredSize(new Dimension(300,100));
    editor.setOpaque(false);
    HyperlinkListener l=new HyperlinkListener()
    {
      @Override
      public void hyperlinkUpdate(HyperlinkEvent e)
      {
        if(e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
        {
          String reference=e.getDescription();
          PageIdentifier pageId=PageIdentifier.fromString(reference);
          _parent.navigateTo(pageId);
        }
      }
    };
    editor.addHyperlinkListener(l);
    return editor;
  }

  private String buildHtml()
  {
    StringBuilder sb=new StringBuilder();
    sb.append("<html><body>");
    List<TitleDescription> titles=_billingGroup.getAccountTitles();
    Collections.sort(titles,new NamedComparator());
    for(TitleDescription title : titles)
    {
      buildHtmlForTitleReference(sb,title);
    }
    sb.append("</body></html>");
    return sb.toString();
  }

  private void buildHtmlForTitleReference(StringBuilder sb, TitleDescription title)
  {
    sb.append("<p>Gives title: "); // 18n
    sb.append("<b>");
    int titleID=title.getIdentifier();
    PageIdentifier to=ReferenceConstants.getTitleReference(titleID);
    String rawTitleName=title.getRawName();
    String titleName=ContextRendering.render(this,rawTitleName);
    HtmlUtils.printLink(sb,to.getFullAddress(),titleName);
    sb.append("</b></p>");
  }

  /**
   * Set the billing group to display.
   */
  private void setBillingGroup()
  {
    String name=_billingGroup.getName();
    // Name
    _name.setText(name);
    // Details
    _details.setText(buildHtml());
    int nbTitles=_billingGroup.getAccountTitles().size();
    _details.setPreferredSize(new Dimension(300,50+(30*nbTitles)));
    _details.setCaretPosition(0);
  }

  @Override
  public void dispose()
  {
    _parent=null;
    // Data
    _billingGroup=null;
    // Controllers
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
