package delta.games.lotro.gui.character.storage.account;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.BaseStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.vaults.Vault;

/**
 * Controller for a panel that displays storage summary for an account/server.
 * @author DAM
 */
public class AccountStorageSummaryPanelController
{
  // Data
  private AccountServerStorage _storage;
  // UI
  private JPanel _panel;
  private JProgressBar _sharedVault;
  private JProgressBar _vault;
  private JProgressBar _bags;

  /**
   * Constructor.
   */
  public AccountStorageSummaryPanelController()
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
  public void update(AccountServerStorage storage)
  {
    _storage=storage;
    // Bags
    {
      Integer[] capacity=getCapacity(storage,true);
      Integer used=capacity[0];
      Integer max=capacity[1];
      updateProgressBar(_bags,used,max);
    }
    // Shared vault
    {
      Vault sharedVault=_storage.getSharedVault();
      if (sharedVault!=null)
      {
        Integer max=Integer.valueOf(sharedVault.getCapacity());
        Integer used=Integer.valueOf(sharedVault.getUsed());
        updateProgressBar(_sharedVault,used,max);
      }
      else
      {
        updateProgressBar(_sharedVault,null,null);
      }
    }
    // Vaults
    {
      Integer[] capacity=getCapacity(storage,false);
      Integer used=capacity[0];
      Integer max=capacity[1];
      updateProgressBar(_vault,used,max);
    }
  }

  private Integer[] getCapacity(AccountServerStorage storage, boolean bags)
  {
    Integer totalUsed=null;
    Integer totalMax=null;
    Set<String> characterNames=storage.getCharacters();
    for(String characterName : characterNames)
    {
      CharacterStorage characterStorage=storage.getStorage(characterName);
      if (characterStorage!=null)
      {
        BaseStorage toUse=bags?characterStorage.getBags():characterStorage.getOwnVault();
        if (toUse!=null)
        {
          Integer used=Integer.valueOf(toUse.getUsed());
          if (used!=null)
          {
            totalUsed=(totalUsed!=null)?Integer.valueOf(totalUsed.intValue()+used.intValue()):used;
          }
          Integer max=Integer.valueOf(toUse.getCapacity());
          if (max!=null)
          {
            totalMax=(totalMax!=null)?Integer.valueOf(totalMax.intValue()+max.intValue()):max;
          }
        }
      }
    }
    Integer[] ret=new Integer[2];
    ret[0]=totalUsed;
    ret[1]=totalMax;
    return ret;
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
      bar.setString("(unknown)"); // I18n
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
    JLabel bagsLabel=GuiFactory.buildLabel("Bags:"); // I18n
    panel.add(bagsLabel,c);
    c.gridx++;
    _bags=buildProgressBar();
    panel.add(_bags,c);
    c.gridy++;c.gridx=0;
    // Vaults
    JLabel vaultLabel=GuiFactory.buildLabel("Vaults:"); // I18n
    panel.add(vaultLabel,c);
    c.gridx++;
    _vault=buildProgressBar();
    panel.add(_vault,c);
    c.gridy++;c.gridx=0;
    // Shared vault
    JLabel sharedVaultLabel=GuiFactory.buildLabel("Shared vault:"); // I18n
    panel.add(sharedVaultLabel,c);
    c.gridx++;
    _sharedVault=buildProgressBar();
    panel.add(_sharedVault,c);
    TitledBorder border=GuiFactory.buildTitledBorder("Capacity"); // I18n
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
