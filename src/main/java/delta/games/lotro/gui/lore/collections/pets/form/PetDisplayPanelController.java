package delta.games.lotro.gui.lore.collections.pets.form;

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
import delta.games.lotro.common.enums.Species;
import delta.games.lotro.common.enums.SubSpecies;
import delta.games.lotro.gui.LotroIconsManager;
import delta.games.lotro.lore.agents.EntityClassification;
import delta.games.lotro.lore.collections.pets.CosmeticPetDescription;

/**
 * Controller for a pet display panel.
 * @author DAM
 */
public class PetDisplayPanelController
{
  // Data
  private CosmeticPetDescription _pet;
  // GUI
  private JPanel _panel;

  private JLabel _icon;
  private JLabel _name;
  private JLabel _initialName;
  private JLabel _genus;
  private JLabel _species;
  private JLabel _subSpecies;
  private JEditorPane _details;

  /**
   * Constructor.
   * @param pet Pet to show.
   */
  public PetDisplayPanelController(CosmeticPetDescription pet)
  {
    _pet=pet;
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
    // Initial name
    _initialName=buildLabelLine(panel,c,"Initial name: "); // 18n
    // Genus
    _genus=buildLabelLine(panel,c,"Genus: "); // 18n
    // Species
    _species=buildLabelLine(panel,c,"Species: ");
    // Sub-species
    _subSpecies=buildLabelLine(panel,c,"Sub-species: "); // 18n

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
    setPet();
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
    String description=_pet.getDescription();
    String sourceDescription=_pet.getSourceDescription();
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
   * Set the pet to display.
   */
  private void setPet()
  {
    String name=_pet.getName();
    // Name
    _name.setText(name);
    // Icon
    ImageIcon icon=LotroIconsManager.getPetIcon(_pet.getIconId());
    _icon.setIcon(icon);
    // Initial name
    String initialName=_pet.getInitialName();
    _initialName.setText(initialName);
    // Classification
    EntityClassification classification=_pet.getClassification();
    // Genus
    String genus=classification.getGenusLabel();
    String genusStr=(genus!=null)?genus:"-";
    _genus.setText(genusStr);
    // Species
    Species species=classification.getSpecies();
    String speciesStr=(species!=null)?species.toString():"-";
    _species.setText(speciesStr);
    // Sub-species
    SubSpecies subSpecies=classification.getSubSpecies();
    String subSpeciesStr=(subSpecies!=null)?subSpecies.toString():"-";
    _subSpecies.setText(subSpeciesStr);
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
    _pet=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _icon=null;
    _name=null;
    _initialName=null;
    _genus=null;
    _species=null;
    _subSpecies=null;
    _details=null;
  }
}
