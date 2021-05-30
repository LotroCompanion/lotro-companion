package delta.games.lotro.gui.character.storage.own;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.bags.BagsManager;
import delta.games.lotro.character.storage.vaults.Vault;

/**
 * Controller for a panel that displays storage summary for a single character.
 * @author DAM
 */
public class StorageSummaryPanelController
{
  // Data
  private CharacterStorage _storage;
  // UI
  private JPanel _panel;
  private JProgressBar _vault;
  private JProgressBar _bags;

  /**
   * Constructor.
   */
  public StorageSummaryPanelController()
  {
    _panel=buildPanel();
  }

  /**
   * Get the managed panel.
   * @return the managed panel.
   */
  public JPanel getPanel()
  {
    return _panel;
  }

  /**
   * Update values.
   * @param storage Storage to show.
   */
  public void update(CharacterStorage storage)
  {
    _storage=storage;
    // Bags
    {
      BagsManager bags=_storage.getBags();
      if (bags!=null)
      {
        Integer used=Integer.valueOf(bags.getUsed());
        Integer max=Integer.valueOf(bags.getCapacity());
        updateProgressBar(_bags,used,max);
      }
      else
      {
        updateProgressBar(_bags,null,null);
      }
    }
    // Vault
    {
      Vault ownVault=_storage.getOwnVault();
      if (ownVault!=null)
      {
        Integer used=Integer.valueOf(ownVault.getUsed());
        Integer max=Integer.valueOf(ownVault.getCapacity());
        updateProgressBar(_vault,used,max);
      }
      else
      {
        updateProgressBar(_vault,null,null);
      }
    }
  }

  private JProgressBar buildProgressBar()
  {
    JProgressBar bar=new JProgressBar(SwingConstants.HORIZONTAL,0,100);
    bar.setBackground(GuiFactory.getBackgroundColor());
    bar.setBorderPainted(true);
    bar.setStringPainted(true);
    bar.setPreferredSize(new Dimension(200,25));
    bar.setMinimumSize(new Dimension(200,25));
    return bar;
  }

  private void updateProgressBar(JProgressBar bar, Integer value, Integer maxValue)
  {
    if ((value!=null) && (maxValue!=null))
    {
      Color color=getColor(value.intValue(),maxValue.intValue());
      bar.setForeground(color);
      bar.setString(value+" / "+maxValue);
      bar.setMaximum(maxValue.intValue());
      bar.setValue(value.intValue());
    }
    else
    {
      bar.setForeground(Color.LIGHT_GRAY);
      bar.setString("(unknown)");
      bar.setMaximum(100);
      bar.setValue(100);
    }
  }

  private Color getColor(int value, int maxValue)
  {
    if (value * 100 > maxValue * 80) return Color.RED; // > 80%
    if (value * 100 > maxValue * 50) return Color.YELLOW; // > 80%
    return Color.GREEN;
  }

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Bags
    JLabel bagsLabel=GuiFactory.buildLabel("Bags:");
    panel.add(bagsLabel,c);
    c.gridx++;
    _bags=buildProgressBar();
    panel.add(_bags,c);
    c.gridy++;c.gridx=0;
    // Vault
    JLabel vaultLabel=GuiFactory.buildLabel("Vault:");
    panel.add(vaultLabel,c);
    c.gridx++;
    _vault=buildProgressBar();
    panel.add(_vault,c);
    TitledBorder border=GuiFactory.buildTitledBorder("Capacity");
    panel.setBorder(border);
    return panel;
  }

  /**
   * Release all managed resources.
   */
  public void dispose()
  {
    // Data
    _storage=null;
    // UI
    if (_panel!=null)
    {
      _panel.removeAll();
      _panel=null;
    }
    _bags=null;
    _vault=null;
  }
}
