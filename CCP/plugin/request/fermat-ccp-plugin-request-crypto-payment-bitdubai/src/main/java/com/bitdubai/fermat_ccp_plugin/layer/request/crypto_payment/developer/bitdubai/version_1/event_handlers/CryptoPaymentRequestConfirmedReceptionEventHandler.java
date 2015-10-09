package com.bitdubai.fermat_ccp_plugin.layer.request.crypto_payment.developer.bitdubai.version_1.event_handlers;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.exceptions.UnexpectedEventException;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_ccp_api.all_definition.enums.EventType;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.events.CryptoPaymentRequestConfirmedReceptionEvent;
import com.bitdubai.fermat_ccp_api.layer.network_service.crypto_payment_request.interfaces.CryptoPaymentRequestManager;
import com.bitdubai.fermat_ccp_plugin.layer.request.crypto_payment.developer.bitdubai.version_1.CryptoPaymentRequestPluginRoot;
import com.bitdubai.fermat_ccp_plugin.layer.request.crypto_payment.developer.bitdubai.version_1.exceptions.CryptoPaymentRequestPluginNotStartedException;
import com.bitdubai.fermat_ccp_plugin.layer.request.crypto_payment.developer.bitdubai.version_1.structure.CryptoPaymentRequestEventActions;

import java.util.UUID;

/**
 * Throw the event handler <code>CryptoPaymentRequestConfirmedReceptionEventHandler</code> we can handle
 * Crypto Payment Request confirmed reception events.
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 08/10/2015.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class CryptoPaymentRequestConfirmedReceptionEventHandler implements FermatEventHandler {

    private final CryptoPaymentRequestManager    cryptoPaymentRequestManager   ;
    private final CryptoPaymentRequestPluginRoot cryptoPaymentRequestPluginRoot;
    private final PluginDatabaseSystem           pluginDatabaseSystem          ;
    private final UUID                           pluginId                      ;

    public CryptoPaymentRequestConfirmedReceptionEventHandler(final CryptoPaymentRequestManager cryptoPaymentRequestManager,
                                                              final CryptoPaymentRequestPluginRoot cryptoPaymentRequestPluginRoot,
                                                              final PluginDatabaseSystem pluginDatabaseSystem,
                                                              final UUID pluginId) {

        this.cryptoPaymentRequestManager    = cryptoPaymentRequestManager   ;
        this.cryptoPaymentRequestPluginRoot = cryptoPaymentRequestPluginRoot;
        this.pluginDatabaseSystem           = pluginDatabaseSystem          ;
        this.pluginId                       = pluginId                      ;
    }

    /**
     * FermatEventHandler interface implementation
     */
    @Override
    public void handleEvent(FermatEvent fermatEvent) throws FermatException {

        if (this.cryptoPaymentRequestPluginRoot.getStatus() == ServiceStatus.STARTED) {

            if (fermatEvent instanceof CryptoPaymentRequestConfirmedReceptionEvent) {

                CryptoPaymentRequestEventActions cryptoPaymentRequestEventActions = new CryptoPaymentRequestEventActions(
                        cryptoPaymentRequestManager,
                        pluginDatabaseSystem       ,
                        pluginId                   ,
                        null
                );

                cryptoPaymentRequestEventActions.initialize();

                cryptoPaymentRequestEventActions.handleCryptoPaymentRequestConfirmedReception(
                        ((CryptoPaymentRequestConfirmedReceptionEvent) fermatEvent).getRequestId()
                );

            } else {
                EventType eventExpected = EventType.CRYPTO_PAYMENT_REQUEST_CONFIRMED_RECEPTION;
                String context = "Event received: " + fermatEvent.getEventType().toString() + " - " + fermatEvent.getEventType().getCode()+"\n"+
                                 "Event expected: " + eventExpected.toString()              + " - " + eventExpected.getCode();
                throw new UnexpectedEventException(context);
            }
        } else {
            throw new CryptoPaymentRequestPluginNotStartedException(null, "Plugin is not started.", "");
        }
    }

}