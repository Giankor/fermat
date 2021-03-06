package com.bitdubai.fermat_ccp_core.layer.crypto_transaction.unhold;

import com.bitdubai.fermat_core_api.layer.all_definition.system.abstract_classes.AbstractPluginSubsystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSubsystemException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_ccp_plugin.layer.crypto_transaction.unhold.developer.bitdubai.DeveloperBitDubai;


/**
 * Created by franklin on 24/11/15.
 */
public class UnHoldCryptoMoneyTransactionPluginSubsystem extends AbstractPluginSubsystem {
    public UnHoldCryptoMoneyTransactionPluginSubsystem() {
        super(new PluginReference(Plugins.BITCOIN_UNHOLD));
    }
    @Override
    public void start() throws CantStartSubsystemException {
        try {
            registerDeveloper(new DeveloperBitDubai());
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
            throw new CantStartSubsystemException(e, null, null);
        }
    }
}
