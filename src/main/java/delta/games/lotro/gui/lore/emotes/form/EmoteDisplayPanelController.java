package delta.games.lotro.gui.lore.emotes.form;

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
import delta.common.ui.swing.navigator.NavigablePanelController;
import delta.common.ui.swing.navigator.NavigatorWindowController;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.emotes.EmoteDescription;

/**
 * Controller for an emote display panel.
 * @author DAM
 */
public class EmoteDisplayPanelController implements NavigablePanelController
{
  // Data
  private EmoteDescription _emote;
  // GUI
  private JPanel _panel;

  private JLabel _icon;
  private JLabel _name;
  private JLabel _auto;
  private JEditorPane _details;
  // Controllers
  private EmoteReferencesDisplayController _references;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param emote Emote to show.
   */
  public EmoteDisplayPanelController(NavigatorWindowController parent, EmoteDescription emote)
  {
    _emote=emote;
    _references=new EmoteReferencesDisplayController(parent,emote.getIdentifier());
  }

  @Override
  public String getTitle()
  {
    return "Emote: "+_emote.getName(); // I18n
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
      panel.setBorder(GuiFactory.buildTitledBorder("References")); // I18n
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
      // Auto?
      panelLine.add(GuiFactory.buildLabel("Auto: ")); // I18n
      _auto=GuiFactory.buildLabel("");
      panelLine.add(_auto);
    }

    // Description
    _details=buildDescriptionPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Description")); // I18n
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(detailsPane,c);
    c.gridy++;

    _panel=panel;
    setEmote();
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
    sb.append(toHtml(_emote.getDescription()));
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
   * Set the emote to display.
   */
  private void setEmote()
  {
    String name=_emote.getName();
    // Name
    _name.setText(name);
    // Icon
    ImageIcon icon=LotroIconsManager.getEmoteIcon(_emote.getIconId());
    _icon.setIcon(icon);
    // Auto
    boolean auto=_emote.isAuto();
    _auto.setText(auto?"Yes":"No"); // I18n
    // Details
    _details.setText(buildHtml());
    _details.setCaretPosition(0);
  }

  @Override
  public void dispose()
  {
    // Data
    _emote=null;
    // Controllers
    if (_references!=null)
    {
      _references.dispose();
      _references=null;
    }
    // UI
    _auto=null;
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
