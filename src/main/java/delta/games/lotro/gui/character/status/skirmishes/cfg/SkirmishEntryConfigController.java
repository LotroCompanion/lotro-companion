package delta.games.lotro.gui.character.status.skirmishes.cfg;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.checkbox.CheckboxController;
import delta.games.lotro.character.status.skirmishes.cfg.SkirmishEntriesPolicy;
import delta.games.lotro.gui.utils.ConfigUpdateListener;

/**
 * Controller for an edition panel for a skirmish entry policy.
 * @author DAM
 */
public class SkirmishEntryConfigController
{
  // Data
  private SkirmishEntriesPolicy _policy;
  // GUI
  private JPanel _panel;
  // Controllers
  private CheckboxController _mergeLevels;
  private CheckboxController _mergeSizes;
  // Listeners
  private ConfigUpdateListener _configUpdateListener;

  /**
   * Constructor.
   * @param policy Managed policy.
   * @param configUpdateListener Filter update listener.
   */
  public SkirmishEntryConfigController(SkirmishEntriesPolicy policy, ConfigUpdateListener configUpdateListener)
  {
    _policy=policy;
    _configUpdateListener=configUpdateListener;
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
      setConfig();
      configUpdated();
    }
    return _panel;
  }

  /**
   * Invoked when the managed config has been updated.
   */
  private void configUpdated()
  {
    if (_configUpdateListener!=null)
    {
      _configUpdateListener.configurationUpdated();
    }
  }

  private void setConfig()
  {
    // Levels
    _mergeLevels.setSelected(_policy.isMergeLevels());
    // Sizes
    _mergeSizes.setSelected(_policy.isMergeSizes());
  }

  private JPanel build()
  {
    // Build UI elements
    {
      _mergeLevels=new CheckboxController("Merge Levels"); // I18n
      final JCheckBox mergeLevelsCheckbox=_mergeLevels.getCheckbox();
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          _policy.setMergeLevels(mergeLevelsCheckbox.isSelected());
          configUpdated();
        }
      };
      mergeLevelsCheckbox.addActionListener(al);
    }
    {
      _mergeSizes=new CheckboxController("Merge Sizes"); // I18n
      final JCheckBox mergeSizesCheckbox=_mergeSizes.getCheckbox();
      ActionListener al=new ActionListener()
      {
        @Override
        public void actionPerformed(ActionEvent e)
        {
          _policy.setMergeSizes(mergeSizesCheckbox.isSelected());
          configUpdated();
        }
      };
      mergeSizesCheckbox.addActionListener(al);
    }

    // Assembly
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_mergeSizes.getCheckbox(),c);
    c=new GridBagConstraints(1,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(0,0,0,0),0,0);
    panel.add(_mergeLevels.getCheckbox(),c);

    return panel;
  }
  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _policy=null;
    // GUI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    // Controllers
    if (_mergeLevels!=null)
    {
      _mergeLevels.dispose();
      _mergeLevels=null;
    }
    if (_mergeSizes!=null)
    {
      _mergeSizes.dispose();
      _mergeSizes=null;
    }
    // Listeners
    _configUpdateListener=null;
  }
}
