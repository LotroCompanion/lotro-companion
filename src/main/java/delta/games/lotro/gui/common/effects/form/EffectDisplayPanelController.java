package delta.games.lotro.gui.common.effects.form;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.labels.MultilineLabel2;
import delta.common.ui.swing.navigator.AbstractNavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.common.ui.swing.navigator.PageIdentifier;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.effects.ParentEffect;
import delta.games.lotro.gui.common.effects.EffectIconUtils;
import delta.games.lotro.gui.common.navigation.ReferenceConstants;
import delta.games.lotro.gui.utils.NavigationUtils;
import delta.games.lotro.gui.utils.UiConfiguration;
import delta.games.lotro.gui.utils.navigation.NavigationHyperLink;
import delta.games.lotro.lore.items.effects.DisplayEffectsUtils;
import delta.games.lotro.utils.html.HtmlUtils;

/**
 * Controller for an effect display panel.
 * @author DAM
 */
public class EffectDisplayPanelController extends AbstractNavigablePanelController
{
  // Data
  private Effect _effect;
  private int _level;
  // Controllers
  private EffectReferencesDisplayController _references;
  private List<NavigationHyperLink> _links;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param effect Effect to show.
   * @param level Level override.
   */
  public EffectDisplayPanelController(NavigatorWindowController parent, Effect effect, int level)
  {
    super(parent);
    _effect=effect;
    _level=level;
    _references=new EffectReferencesDisplayController(parent,effect.getIdentifier());
    _links=new ArrayList<NavigationHyperLink>();
    setPanel(build());
  }

  @Override
  public String getTitle()
  {
    String title="Effect: "+_effect.getName()+" (level "+_level+")";
    return title;
  }

  private JPanel build()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Top panel
    JPanel topPanel=buildTopPanel();
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(topPanel,c);
    // Center
    Component center=buildCenterPanel();
    c=new GridBagConstraints(0,1,1,1,1.0,1.0,GridBagConstraints.WEST,GridBagConstraints.BOTH,new Insets(0,0,0,0),0,0);
    panel.add(center,c);
    return panel;
  }

  private MultilineLabel2 buildEffectDisplay()
  {
    MultilineLabel2 ret=null;
    List<String> lines=getEffectLines();
    if (!lines.isEmpty())
    {
      ret=new MultilineLabel2();
      String[] linesToShow=lines.toArray(new String[lines.size()]);
      ret.setText(linesToShow);
      ret.setBorder(GuiFactory.buildTitledBorder("Stats"));
    }
    return ret;
  }

  private List<String> getEffectLines()
  {
    List<String> ret=new ArrayList<String>();
    DisplayEffectsUtils.showEffect(ret,_effect,_level,false);
    return ret;
  }

  private JPanel buildCenterPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Build components for tabs
    JPanel mainAttrs=buildMainAttrsPanel();
    JTabbedPane tabbedPane=GuiFactory.buildTabbedPane();
    tabbedPane.add("Main",buildPanelForTab(mainAttrs));
    // - references
    JEditorPane references=_references.getComponent();
    if (references!=null)
    {
      tabbedPane.add("References",buildPanelForTab(references));
    }
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,1.0,1.0,GridBagConstraints.NORTHWEST,GridBagConstraints.BOTH,new Insets(5,5,5,5),0,0);
    panel.add(tabbedPane,c);
    return panel;
  }

  private JPanel buildPanelForTab(Component contents)
  {
    JPanel wrapper=GuiFactory.buildBackgroundPanel(new BorderLayout());
    JScrollPane scrollPane=GuiFactory.buildScrollPane(contents);
    wrapper.add(scrollPane,BorderLayout.CENTER);
    return wrapper;
  }

  private JPanel buildTopPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    // Icon
    Icon icon=EffectIconUtils.buildEffectIcon(_effect);
    JLabel iconLabel=GuiFactory.buildIconLabel(icon);
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,5),0,0);
    panel.add(iconLabel,c);
    // Name
    String name=_effect.getName();
    JLabel nameLabel=GuiFactory.buildLabel(name);
    nameLabel.setFont(nameLabel.getFont().deriveFont(16f).deriveFont(Font.BOLD));
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    panel.add(nameLabel,c);
    return panel;
  }

  private JComponent buildSelectableLabel(String text)
  {
    JTextField f=GuiFactory.buildTextField(text);
    f.setEditable(false);
    f.setBorder(null);
    f.setOpaque(false);
    f.setFont(UIManager.getFont("Label.font"));
    Dimension d=f.getPreferredSize();
    f.setPreferredSize(new Dimension(d.width+5,d.height));
    return f;
  }

  private JPanel buildMainAttrsPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());

    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    List<String> lines=getAttributesLines();
    for(String line : lines)
    {
      JPanel panelLine=GuiFactory.buildPanel(new FlowLayout(FlowLayout.LEFT));
      panel.add(panelLine,c);
      c.gridy++;
      panelLine.add(buildSelectableLabel(line));
    }
    c.insets=new Insets(0,5,0,0);
    int y=c.gridy;
    // Description
    JEditorPane description=buildDescription();
    if (description!=null)
    {
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(description,c);
      y++;
    }
    // Child effects
    JPanel childEffectsPanel=buildChildEffectsPanel();
    if (childEffectsPanel!=null)
    {
      childEffectsPanel.setBorder(GuiFactory.buildTitledBorder("Sub-effects"));
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(childEffectsPanel,c);
      y++;
    }
    // Stats
    MultilineLabel2 effectLines=buildEffectDisplay();
    if (effectLines!=null)
    {
      c=new GridBagConstraints(0,y,1,1,1.0,0,GridBagConstraints.NORTHWEST,GridBagConstraints.HORIZONTAL,new Insets(5,5,5,5),0,0);
      panel.add(effectLines,c);
      y++;
    }
    // Padding to push everything on left and top
    JPanel paddingPanel=GuiFactory.buildPanel(new BorderLayout());
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(paddingPanel,c);
    return panel;
  }

  private List<String> getAttributesLines()
  {
    List<String> ret=new ArrayList<String>();
    if (UiConfiguration.showTechnicalColumns())
    {
      ret.add("ID: "+_effect.getIdentifier());
    }
    // TODO Probability
    // TODO Duration
    // Flags?
    return ret;
  }

  private JEditorPane buildDescription()
  {
    JEditorPane editor=null;
    String description=_effect.getDescription();
    if (!description.isEmpty())
    {
      editor=GuiFactory.buildHtmlPanel();
      StringBuilder sb=new StringBuilder();
      sb.append("<html><body style='width: 400px'>");
      sb.append(HtmlUtils.toHtml(description));
      sb.append("</body></html>");
      editor.setText(sb.toString());
    }
    return editor;
  }

  private JPanel buildChildEffectsPanel()
  {
    if (!(_effect instanceof ParentEffect))
    {
      return null;
    }
    ParentEffect parentEffect=(ParentEffect)_effect;
    List<Effect> childEffects=new ArrayList<Effect>(parentEffect.getChildEffects());
    if (childEffects.isEmpty())
    {
      return null;
    }
    Collections.sort(childEffects,new NamedComparator());
    List<NavigationHyperLink> links=new ArrayList<NavigationHyperLink>();
    for(Effect childEffect : childEffects)
    {
      PageIdentifier pageId=ReferenceConstants.getEffectReference(childEffect);
      String text=childEffect.getName();
      NavigationHyperLink link=NavigationUtils.buildNavigationLink(getParent(),text,pageId);
      links.add(link);
    }
    _links.addAll(links);
    return NavigationUtils.buildPanel(links);
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _effect=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    if (_links!=null)
    {
      for(NavigationHyperLink links : _links)
      {
        links.dispose();
      }
      _links=null;
    }
  }
}
