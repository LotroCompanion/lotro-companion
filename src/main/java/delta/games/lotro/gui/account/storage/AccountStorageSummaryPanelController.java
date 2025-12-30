package delta.games.lotro.gui.account.storage;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import delta.common.ui.swing.GuiFactory;
import delta.common.ui.swing.panels.AbstractPanelController;
import delta.games.lotro.character.storage.AccountServerStorage;
import delta.games.lotro.character.storage.BaseStorage;
import delta.games.lotro.character.storage.CharacterStorage;
import delta.games.lotro.character.storage.vaults.Vault;
import delta.games.lotro.gui.character.storage.StorageUiUtils;
import delta.games.lotro.gui.utils.l10n.Labels;

/**
 * Controller for a panel that displays storage summary for an account/server.
 * @author DAM
 */
public class AccountStorageSummaryPanelController extends AbstractPanelController
{
  // Data
  private AccountServerStorage _storage;
  // UI
  private JProgressBar _sharedVault;
  private JProgressBar _vault;
  private JProgressBar _bags;

  /**
   * Constructor.
   */
  public AccountStorageSummaryPanelController()
  {
    setPanel(buildPanel());
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
      StorageUiUtils.updateProgressBar(_bags,used,max);
    }
    // Shared vault
    {
      Vault sharedVault=_storage.getSharedVault();
      if (sharedVault!=null)
      {
        Integer max=Integer.valueOf(sharedVault.getCapacity());
        Integer used=Integer.valueOf(sharedVault.getUsed());
        StorageUiUtils.updateProgressBar(_sharedVault,used,max);
      }
      else
      {
        StorageUiUtils.updateProgressBar(_sharedVault,null,null);
      }
    }
    // Vaults
    {
      Integer[] capacity=getCapacity(storage,false);
      Integer used=capacity[0];
      Integer max=capacity[1];
      StorageUiUtils.updateProgressBar(_vault,used,max);
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

  private JPanel buildPanel()
  {
    JPanel panel=GuiFactory.buildPanel(new GridBagLayout());
    GridBagConstraints c=new GridBagConstraints(0,0,1,1,0.0,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(5,5,5,5),0,0);
    // Bags
    JLabel bagsLabel=GuiFactory.buildLabel(Labels.getFieldLabel("storage.summary.field.bags"));
    panel.add(bagsLabel,c);
    c.gridx++;
    _bags=StorageUiUtils.buildProgressBar();
    panel.add(_bags,c);
    c.gridy++;c.gridx=0;
    // Vaults
    JLabel vaultLabel=GuiFactory.buildLabel(Labels.getFieldLabel("storage.summary.field.vaults"));
    panel.add(vaultLabel,c);
    c.gridx++;
    _vault=StorageUiUtils.buildProgressBar();
    panel.add(_vault,c);
    c.gridy++;c.gridx=0;
    // Shared vault
    JLabel sharedVaultLabel=GuiFactory.buildLabel(Labels.getFieldLabel("storage.summary.field.sharedVault"));
    panel.add(sharedVaultLabel,c);
    c.gridx++;
    _sharedVault=StorageUiUtils.buildProgressBar();
    panel.add(_sharedVault,c);
    TitledBorder border=GuiFactory.buildTitledBorder(Labels.getLabel("storage.summary.capacity"));
    panel.setBorder(border);
    return panel;
  }

  @Override
  public void dispose()
  {
    super.dispose();
    // Data
    _storage=null;
    // UI
    _bags=null;
    _vault=null;
  }
}
