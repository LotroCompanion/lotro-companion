package delta.games.lotro.gui.character.status.effects;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.common.ui.swing.text.FloatEditionController;
import delta.common.ui.swing.windows.WindowController;
import delta.common.utils.collections.filters.Filter;
import delta.common.utils.l10n.LocalizedFormats;
import delta.common.utils.misc.TypedProperties;
import delta.games.lotro.character.status.effects.EffectInstance;
import delta.games.lotro.common.comparators.NamedComparator;
import delta.games.lotro.common.effects.Effect;
import delta.games.lotro.common.effects.EffectsManager;
import delta.games.lotro.gui.common.effects.EffectIconController;
import delta.games.lotro.gui.common.effects.chooser.EffectChooser;
import delta.games.lotro.gui.common.effects.chooser.EffectFilterController;
import delta.games.lotro.gui.utils.l10n.Labels;
import delta.games.lotro.utils.gui.chooser.ObjectChoiceWindowController;

/**
 * Controller for an item instance edition panel.
 * @author DAM
 */
public class EffectInstanceEditionPanelController extends AbstractPanelController
{
  /**
   * UI mode.
   * @author DAM
   */
  public enum MODE
  {
    /**
     * Creation.
     */
    CREATION,
    /**
     * Edition.
     */
    EDITION
  }
  // Data
  private EffectInstance _effectInstance;
  private MODE _mode;
  // GUI
  private JLabel _effectName;
  // Controllers
  private EffectIconController _effectIcon;
  private FloatEditionController _spellcraftEditor;

  /**
   * Constructor.
   * @param parent Parent window.
   * @param effectInstance Item instance.
   * @param mode UI mode.
   */
  public EffectInstanceEditionPanelController(WindowController parent, EffectInstance effectInstance, MODE mode)
  {
    super(parent);
    _effectInstance=effectInstance;
    _mode=mode;
    JPanel panel=buildPanel();
    setPanel(panel);
    update();
  }

  private JPanel buildEffectEditionPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    // - icon
    _effectIcon=new EffectIconController(getWindowController(),null,true);
    ret.add(_effectIcon.getIcon(),c);
    // - name
    _effectName=GuiFactory.buildLabel("");
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,0,0,0),0,0);
    ret.add(_effectName,c);
    // - choose button
    JButton chooseButton=GuiFactory.buildButton("Choose...");
    c=new GridBagConstraints(2,0,1,1,0.0,0.0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    ret.add(chooseButton,c);
    ActionListener al=new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        chooseEffect();
      }
    };
    chooseButton.addActionListener(al);
    if (_mode==MODE.EDITION)
    {
      chooseButton.setEnabled(false);
    }
    return ret;
  }

  private void chooseEffect()
  {
    Effect chosenEffect=chooseAnEffect();
    if (chosenEffect==null)
    {
      return;
    }
    _effectInstance.setEffect(chosenEffect);
    updateEffect();
  }

  private Effect chooseAnEffect()
  {
    List<Effect> selectedItems=EffectsManager.getInstance().getEffects();
    Collections.sort(selectedItems,new NamedComparator());

    WindowController parentWindow=getWindowController();
    TypedProperties filterProps=parentWindow.getUserProperties("EffectFilter");
    EffectFilterController filterController=new EffectFilterController(filterProps);

    TypedProperties prefs=null;
    if (parentWindow!=null)
    {
      prefs=parentWindow.getUserProperties(EffectChooser.EFFECT_CHOOSER_PROPERTIES_ID);
    }

    Filter<Effect> filter=filterController.getFilter();
    ObjectChoiceWindowController<Effect> chooser=EffectChooser.buildChooser(parentWindow,prefs,selectedItems,filter,filterController);
    Effect ret=chooser.editModal();
    return ret;
  }

  private JPanel buildPanel()
  {
    JPanel ret=GuiFactory.buildPanel(new GridBagLayout());
    // Effect
    JLabel effect=GuiFactory.buildLabel("Effect:");
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    ret.add(effect,c);
    JPanel effectPanel=buildEffectEditionPanel();
    c=new GridBagConstraints(1,0,1,1,1.0,0.0,GridBagConstraints.WEST,GridBagConstraints.HORIZONTAL,new Insets(0,5,0,0),0,0);
    ret.add(effectPanel,c);
    // Spellcraft
    JLabel spellcraft=GuiFactory.buildLabel("Level:");
    c=new GridBagConstraints(0,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    ret.add(spellcraft,c);
    JTextField value=GuiFactory.buildTextField("");
    _spellcraftEditor=new FloatEditionController(value);
    NumberFormat format=LocalizedFormats.getRealNumberFormat(0,1);
    _spellcraftEditor.setFormat(format);
    c=new GridBagConstraints(1,1,1,1,0.0,0.0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,5,0,0),0,0);
    ret.add(_spellcraftEditor.getTextField(),c);
    return ret;
  }

  /**
   * Update display.
   */
  private void update()
  {
    // Effect
    updateEffect();
    // Spellcraft
    Float spellcraft=_effectInstance.getSpellcraft();
    _spellcraftEditor.setValue(spellcraft);
  }

  private void updateEffect()
  {
    Effect effect=_effectInstance.getEffect();
    _effectIcon.setEffect(effect);
    String effectName=(effect!=null)?effect.getName():"";
    _effectName.setText(effectName);
  }

  /**
   * Update the managed data from the UI state.
   */
  public void updateFromUi()
  {
    // Effect: nothing to do: already done when choosing
    // Spellcraft
    Float spellcraft=_spellcraftEditor.getValue();
    _effectInstance.setSpellcraft(spellcraft);
  }

  /**
   * Check data validity.
   * @return A error message or <code>null</code> if OK.
   */
  public String checkData()
  {
    String errorMsg=null;
    Effect effect=_effectInstance.getEffect();
    if (effect==null)
    {
      errorMsg=Labels.getLabel("effect.edition.validation.error.noEffect");
    }
    return errorMsg;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _effectInstance=null;
    // GUI
    _effectName=null;
    // Controllers
    if (_effectIcon!=null)
    {
      _effectIcon.dispose();
      _effectIcon=null;
    }
    if (_spellcraftEditor!=null)
    {
      _spellcraftEditor.dispose();
      _spellcraftEditor=null;
    }
  }
}
