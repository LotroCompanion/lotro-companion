package delta.games.lotro.gui.lore.collections.mounts.form;

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
import delta.common.utils.text.EndOfLine;
import delta.games.lotro.common.enums.MountType;
import delta.games.lotro.common.enums.SkillCharacteristicSubCategory;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.collections.mounts.MountDescription;

/**
 * Controller for a mount display panel.
 * @author DAM
 */
public class MountDisplayPanelController
{
  // Data
  private MountDescription _mount;
  // GUI
  private JPanel _panel;

  private JLabel _icon;
  private JLabel _name;
  private JLabel _category;
  private JLabel _mountType;
  private JLabel _initialName;
  private JLabel _morale;
  private JLabel _speed;
  private JLabel _size;
  private JEditorPane _details;

  /**
   * Constructor.
   * @param mount Mount to show.
   */
  public MountDisplayPanelController(MountDescription mount)
  {
    _mount=mount;
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
    // Category
    _category=buildLabelLine(panel,c,"Category: "); // 18n
    // Mount type
    _mountType=buildLabelLine(panel,c,"Mount type: "); // 18n
    // Initial name
    _initialName=buildLabelLine(panel,c,"Initial name: "); // 18n
    // Morale
    _morale=buildLabelLine(panel,c,"Morale: "); // 18n
    // Speed
    _speed=buildLabelLine(panel,c,"Speed: "); // 18n
    // Tall
    _size=buildLabelLine(panel,c,"Size: "); // 18n

    // Description
    _details=buildDescriptionPane();
    JScrollPane detailsPane=GuiFactory.buildScrollPane(_details);
    detailsPane.setBorder(GuiFactory.buildTitledBorder("Description")); // 18n
    c.fill=GridBagConstraints.BOTH;
    c.weightx=1.0;
    c.weighty=1.0;
    panel.add(detailsPane,c);
    c.gridy++;

    _panel=panel;
    setMount();
    return _panel;
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
    String description=_mount.getDescription();
    String sourceDescription=_mount.getSourceDescription();
    String text=description;
    if (sourceDescription.length()>0)
    {
      text=text+EndOfLine.NATIVE_EOL+sourceDescription;
    }
    sb.append(toHtml(text.trim()));
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
   * Set the mount to display.
   */
  private void setMount()
  {
    String name=_mount.getName();
    // Name
    _name.setText(name);
    // Icon
    ImageIcon icon=LotroIconsManager.getMountIcon(_mount.getIconId());
    _icon.setIcon(icon);
    // Category
    SkillCharacteristicSubCategory category=_mount.getMountCategory();
    String categoryText=(category!=null)?category.getLabel():"";
    _category.setText(categoryText);
    // Mount type
    MountType mountType=_mount.getMountType();
    String mountTypeText=(mountType!=null)?mountType.getLabel():"";
    _mountType.setText(mountTypeText);
    // Initial name
    String initialName=_mount.getInitialName();
    _initialName.setText(initialName);
    // Morale
    int morale=_mount.getMorale();
    _morale.setText(String.valueOf(morale));
    // Speed
    float speed=_mount.getSpeed();
    String speedStr=String.valueOf((int)(speed*100))+" %";
    _speed.setText(speedStr);
    // Size
    boolean tall=_mount.isTall();
    _size.setText(tall?"Tall":"Short"); // 18n
    // Details
    _details.setText(buildHtml());
    _details.setCaretPosition(0);
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _mount=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _icon=null;
    _name=null;
    _category=null;
    _mountType=null;
    _initialName=null;
    _morale=null;
    _speed=null;
    _details=null;
  }
}
