package com.bitdubai.reference_niche_wallet.loss_protected_wallet.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Wallets;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantGetSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.CantPersistSettingsException;
import com.bitdubai.fermat_api.layer.all_definition.settings.exceptions.SettingsNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.LossProtectedWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetCryptoLossProtectedWalletException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWallet;
import com.bitdubai.fermat_cer_api.layer.provider.interfaces.CurrencyExchangeRateProviderManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedWalletExceptionSeverity;
import com.bitdubai.fermat_wpd_api.layer.wpd_network_service.wallet_resources.interfaces.WalletResourcesProviderManager;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.session.LossProtectedWalletSession;
import com.mati.fermat_preference_settings.drawer.FermatPreferenceFragment;
import com.mati.fermat_preference_settings.drawer.interfaces.PreferenceSettingsItem;
import com.mati.fermat_preference_settings.drawer.models.PreferenceSettingsTextPlusRadioItem;
import com.mati.fermat_preference_settings.drawer.models.PreferenceSettingsOpenDialogText;
import com.mati.fermat_preference_settings.drawer.models.PreferenceSettingsSwithItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.WalletUtils.showMessage;

/**
 * Created by mati on 2016.02.09..
 */
public class LossProtectedSettingsFragment extends FermatPreferenceFragment<LossProtectedWalletSession,WalletResourcesProviderManager> {



    private LossProtectedWalletSession referenceWalletSession;
    private LossProtectedWallet cryptoWallet;
    SettingsManager<LossProtectedWalletSettings> settingsManager;
    private LossProtectedWalletSettings bitcoinWalletSettings = null;

    public static LossProtectedSettingsFragment newInstance() {
        return new LossProtectedSettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        referenceWalletSession = appSession;
        try {
            cryptoWallet = referenceWalletSession.getModuleManager().getCryptoWallet();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            settingsManager = referenceWalletSession.getModuleManager().getSettingsManager();
        } catch (CantGetCryptoLossProtectedWalletException e) {
            referenceWalletSession.getErrorManager().reportUnexpectedWalletException(Wallets.CWP_WALLET_RUNTIME_WALLET_BITCOIN_WALLET_ALL_BITDUBAI, UnexpectedWalletExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
            showMessage(getActivity(), "CantGetCryptoWalletException- " + e.getMessage());
        }
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected List<PreferenceSettingsItem> setSettingsItems() {
        BlockchainNetworkType blockchainNetworkType= null;
        UUID exchangeProviderId = null;
        List<PreferenceSettingsItem> list = new ArrayList<>();
        try{


            bitcoinWalletSettings = settingsManager.loadAndGetSettings(referenceWalletSession.getAppPublicKey());

            list.add(new PreferenceSettingsSwithItem(1,"Enabled Notifications",bitcoinWalletSettings.getNotificationEnabled()));

            list.add(new PreferenceSettingsSwithItem(2,"Enabled Loss Protected",bitcoinWalletSettings.getNotificationEnabled()));


            if (bitcoinWalletSettings.getBlockchainNetworkType()!=null)
            blockchainNetworkType =  bitcoinWalletSettings.getBlockchainNetworkType();

        List<PreferenceSettingsTextPlusRadioItem> strings = new ArrayList<PreferenceSettingsTextPlusRadioItem>();

        strings.add(new PreferenceSettingsTextPlusRadioItem(6,"MainNet",(blockchainNetworkType.equals(BlockchainNetworkType.PRODUCTION)) ? true : false));
        strings.add(new PreferenceSettingsTextPlusRadioItem(7,"TestNet",(blockchainNetworkType.equals(BlockchainNetworkType.TEST_NET)) ? true : false));
        strings.add(new PreferenceSettingsTextPlusRadioItem(8,"RegTest",(blockchainNetworkType.equals(BlockchainNetworkType.REG_TEST)) ? true : false));

        list.add(new PreferenceSettingsOpenDialogText(5,"Select Network",strings));

            //Exchange Rate Provider

            if (cryptoWallet.getExchangeProvider()!=null)
                exchangeProviderId=  cryptoWallet.getExchangeProvider();

            List<PreferenceSettingsTextPlusRadioItem> stringsProviders = new ArrayList<PreferenceSettingsTextPlusRadioItem>();

            //get providers list
            List<CurrencyExchangeRateProviderManager> providers = new ArrayList(cryptoWallet.getExchangeRateProviderManagers());

            int position = 11;
            for (CurrencyExchangeRateProviderManager provider :  providers)
            {
                stringsProviders.add(new PreferenceSettingsTextPlusRadioItem(position,provider.getProviderName(),(provider.getProviderId().equals(exchangeProviderId)) ? true : false));
                position++;
            }

            list.add(new PreferenceSettingsOpenDialogText(10,"Exchange Rate Providers",stringsProviders));

        } catch (CantGetSettingsException e) {
            e.printStackTrace();
        } catch (SettingsNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    /**
     * Cuando se elige algun settings va a pasar por acá
     *
     * @param preferenceSettingsItem
     * @param position
     */
    @Override
    public void onSettingsTouched(PreferenceSettingsItem preferenceSettingsItem, int position) {

        try {

            try {
                bitcoinWalletSettings = settingsManager.loadAndGetSettings(referenceWalletSession.getAppPublicKey());
            } catch (CantGetSettingsException e) {
                e.printStackTrace();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }
            bitcoinWalletSettings.setIsPresentationHelpEnabled(false);


            if (preferenceSettingsItem.getId() == 5){
                //blockchainNetworkType settings
                PreferenceSettingsTextPlusRadioItem preferenceSettingsTextPlusRadioItem = (PreferenceSettingsTextPlusRadioItem) preferenceSettingsItem;
                BlockchainNetworkType blockchainNetworkType = null;

                switch (preferenceSettingsTextPlusRadioItem.getText()) {

                    case "MainNet":
                        blockchainNetworkType = BlockchainNetworkType.PRODUCTION;

                        break;

                    case "TestNet":
                        blockchainNetworkType = BlockchainNetworkType.TEST_NET;
                        break;

                    case "RegTest":
                        blockchainNetworkType = BlockchainNetworkType.REG_TEST;
                        break;

                    default:
                        blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();
                        break;

                }

                preferenceSettingsTextPlusRadioItem.setIsRadioTouched(true);

                System.out.println("SETTING SELECTED IS " + preferenceSettingsTextPlusRadioItem.getText());
                System.out.println("NETWORK TYPE TO BE SAVED IS  " + blockchainNetworkType.getCode());

                if (blockchainNetworkType == null) {
                    if (bitcoinWalletSettings.getBlockchainNetworkType() != null) {
                        blockchainNetworkType = bitcoinWalletSettings.getBlockchainNetworkType();
                    } else {
                        blockchainNetworkType = BlockchainNetworkType.getDefaultBlockchainNetworkType();
                    }
                }

                bitcoinWalletSettings.setBlockchainNetworkType(blockchainNetworkType);   }
            else {
                //Exchange Rate provider settings
                PreferenceSettingsTextPlusRadioItem preferenceSettingsTextPlusRadioItem = (PreferenceSettingsTextPlusRadioItem) preferenceSettingsItem;


                //get providers list
                List<CurrencyExchangeRateProviderManager> providers = new ArrayList(cryptoWallet.getExchangeRateProviderManagers());

                cryptoWallet.setExchangeProvider(providers.get(0).getProviderId());

                for (CurrencyExchangeRateProviderManager provider :  providers)
                {
                    if(provider.getProviderName().equals(preferenceSettingsTextPlusRadioItem.getText()))
                        cryptoWallet.setExchangeProvider(provider.getProviderId());
                }
            }

            try {
                settingsManager.persistSettings(referenceWalletSession.getAppPublicKey(), bitcoinWalletSettings);
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
        }

    }

    @Override
    public void onSettingsChanged(PreferenceSettingsItem preferenceSettingsItem, int position, boolean isChecked) {

        try {

            try {
                bitcoinWalletSettings = settingsManager.loadAndGetSettings(referenceWalletSession.getAppPublicKey());
            } catch (CantGetSettingsException e) {
                e.printStackTrace();
            } catch (SettingsNotFoundException e) {
                e.printStackTrace();
            }


            if (preferenceSettingsItem.getId() == 1){
                //enable notifications settings
                bitcoinWalletSettings.setNotificationEnabled(isChecked);
            }

            if (preferenceSettingsItem.getId() == 2){
                //enable Loss Protected
                bitcoinWalletSettings.setLossProtectedEnabled(isChecked);
            }

            try {
                settingsManager.persistSettings(referenceWalletSession.getAppPublicKey(), bitcoinWalletSettings);
            } catch (CantPersistSettingsException e) {
                e.printStackTrace();
            }
        } catch (Exception e){
        }

    }


    @Override
    public int getBackgroundColor() {
        return Color.WHITE;
    }

    @Override
    public int getBackgroundAlpha() {
        return 95;
    }


}
