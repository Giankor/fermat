package com.bitdubai.reference_niche_wallet.loss_protected_wallet.fragments.wallet_final_version;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitdubai.android_fermat_ccp_loss_protected_wallet_bitcoin.R;
import com.bitdubai.fermat_android_api.ui.Views.DividerItemDecoration;
import com.bitdubai.fermat_android_api.ui.adapters.FermatAdapter;
import com.bitdubai.fermat_android_api.ui.enums.FermatRefreshTypes;
import com.bitdubai.fermat_android_api.ui.fragments.FermatWalletListFragment;
import com.bitdubai.fermat_android_api.ui.interfaces.FermatListItemListeners;
import com.bitdubai.fermat_android_api.ui.util.FermatAnimationsUtils;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_api.layer.all_definition.enums.UISource;
import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.enums.Activities;
import com.bitdubai.fermat_api.layer.all_definition.settings.structure.SettingsManager;
import com.bitdubai.fermat_api.layer.dmp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.BalanceType;
import com.bitdubai.fermat_ccp_api.layer.basic_wallet.common.enums.TransactionType;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.crypto_wallet.exceptions.CantListCryptoWalletIntraUserIdentityException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.LossProtectedWalletSettings;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetCryptoLossProtectedWalletException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantGetLossProtectedBalanceException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.exceptions.CantListLossProtectedTransactionsException;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedPaymentRequest;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWallet;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletIntraUserIdentity;
import com.bitdubai.fermat_ccp_api.layer.wallet_module.loss_protected_wallet.interfaces.LossProtectedWalletTransaction;
import com.bitdubai.fermat_ccp_plugin.layer.wallet_module.loss_protected_wallet.developer.bitdubai.version_1.structure.LossProtectedWalletModuleManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedSubAppExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedUIExceptionSeverity;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.adapters.ChunckValuesHistoryAdapter;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.adapters.PaymentRequestHistoryAdapter;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.enums.ShowMoneyType;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.WalletUtils;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.common.utils.onRefreshList;
import com.bitdubai.reference_niche_wallet.loss_protected_wallet.session.LossProtectedWalletSession;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static android.widget.Toast.makeText;

/**
 * Created by mati on 2015.09.30..
 */
public class ChunckValuesHistoryFragment extends FermatWalletListFragment<LossProtectedWalletTransaction> implements FermatListItemListeners<LossProtectedWalletTransaction>,onRefreshList {

    /**
     * Session
     */
    LossProtectedWalletSession referenceWalletSession;
    String walletPublicKey = "loss_protected_wallet";
    /**
     * MANAGERS
    @Override
    protected void bindHolder(ChunckValuesHistoryItemViewHolder holder, LossProtectedWalletTransaction data, int position) {

    }

     */
    private LossProtectedWallet cryptoWallet;
     /**
     * DATA
     */
    private List<LossProtectedWalletTransaction> lstTransaction;
    private LossProtectedWalletTransaction selectedItem;
    private LossProtectedWalletModuleManager moduleManager;
    /**
     * Executor Service
     */
    private ExecutorService executor;
    private int MAX_TRANSACTIONS = 20;
    private int offset = 0;
    private View rootView;
    private LinearLayout empty;
    private LinearLayout chunck_header_container;
    private TextView chunk_balance_TextView;
    private  TextView exchange_rate;

    SettingsManager<LossProtectedWalletSettings> settingsManager;

    BlockchainNetworkType blockchainNetworkType;

    /**
     * Create a new instance of this fragment
     *
     * @return InstalledFragment instance object
     */
    public static ChunckValuesHistoryFragment newInstance() {
        return new ChunckValuesHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        referenceWalletSession = (LossProtectedWalletSession)appSession;

        lstTransaction = new ArrayList<LossProtectedWalletTransaction>();
        try {
            cryptoWallet = referenceWalletSession.getModuleManager().getCryptoWallet();

            //lstTransactionRequest = getMoreDataAsync(FermatRefreshTypes.NEW, 0); // get init data

            getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final Drawable drawable = getResources().getDrawable(R.drawable.background_gradient, null);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getPaintActivtyFeactures().setActivityBackgroundColor(drawable);
                                }catch (OutOfMemoryError o){
                                    o.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });

            settingsManager = referenceWalletSession.getModuleManager().getSettingsManager();


            LossProtectedWalletSettings bitcoinWalletSettings;
            try {
                bitcoinWalletSettings = settingsManager.loadAndGetSettings(referenceWalletSession.getAppPublicKey());
                this.blockchainNetworkType = bitcoinWalletSettings.getBlockchainNetworkType();
            }catch (Exception e){

            }

            onRefresh();
        } catch (Exception ex) {
            ex.printStackTrace();
            //CommonLogger.exception(TAG, ex.getMessage(), ex);
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();

        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            rootView = super.onCreateView(inflater, container, savedInstanceState);
            RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), R.drawable.divider_shape);
            recyclerView.addItemDecoration(itemDecoration);
            empty = (LinearLayout) rootView.findViewById(R.id.empty);
            //setUp();
            setUpHeader(inflater);

        }catch (Exception e){
            Toast.makeText(getActivity().getApplicationContext(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void  setUpHeader(LayoutInflater inflater) {
        try {
            final RelativeLayout container_header_balance = getToolbarHeader();
            try {
                container_header_balance.removeAllViews();
            } catch (Exception e) {

            }
            container_header_balance.setBackgroundColor(Color.parseColor("#fff"));


            final View chunk_balance_header = inflater.inflate(R.layout.chunck_header, container_header_balance, true);

            container_header_balance.setVisibility(View.VISIBLE);

            chunck_header_container = (LinearLayout) chunk_balance_header.findViewById(R.id.chunck_header_container);

            final String realBalance = WalletUtils.formatBalanceStringNotDecimal(moduleManager.getRealBalance(referenceWalletSession.getAppPublicKey(), blockchainNetworkType), ShowMoneyType.BITCOIN.getCode());

            final double actualExchangeRate = referenceWalletSession.getActualExchangeRate();

            chunk_balance_TextView = (TextView) chunk_balance_header.findViewById(R.id.txt_amount);
            exchange_rate = (TextView) chunk_balance_header.findViewById(R.id.txt_exchange_rate);

            chunk_balance_TextView.setText(realBalance);
            exchange_rate.setText("Exchange Rate: 1 BTC = "+actualExchangeRate);

        } catch (CantGetLossProtectedBalanceException e) {
            e.printStackTrace();
        }
    }


    private void setUp(){
        FrameLayout frameLayout = new FrameLayout(getActivity());

        FrameLayout.LayoutParams lbs = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

        frameLayout.setLayoutParams(lbs);

        //ImageView icon = new ImageView(getActivity());  Create an icon
        //icon.setImageResource(R.drawable.btn_request_selector);
        //icon.setImageResource(R.drawable.ic_contact_newcontact);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(Activities.CCP_BITCOIN_WALLET_REQUEST_FORM_ACTIVITY);
            }
        };
        View view = new View(getActivity());
        view.setLayoutParams(lbs);

        frameLayout.addView(view);
        frameLayout.setOnClickListener(onClickListener);
        view.setOnClickListener(onClickListener);
        final com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton actionButton = new com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton.Builder(getActivity())
                .setContentView(frameLayout).setBackgroundDrawable(R.drawable.btn_request_selector)
                .build();

        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(getActivity())
                .attachTo(actionButton)
                .build();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int mScrollOffset = 4;
                if (Math.abs(dy) > mScrollOffset) {
                    if (actionButton != null) {
                        if (dy > 0) {
                            actionButton.setVisibility(View.GONE);
                        } else {
                            actionButton.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        try {
            super.onActivityCreated(savedInstanceState);
            lstTransaction = new ArrayList<LossProtectedWalletTransaction>();
        } catch (Exception e){
            makeText(getActivity(), "Oooops! recovering from system error", Toast.LENGTH_SHORT).show();
            referenceWalletSession.getErrorManager().reportUnexpectedUIException(UISource.VIEW, UnexpectedUIExceptionSeverity.CRASH, e);
        }
    }


    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.chunck_values_fragment_transaction_main;
    }

    @Override
    protected int getSwipeRefreshLayoutId() {
        return R.id.swipe_refresh;
    }

    @Override
    protected int getRecyclerLayoutId() {
        return R.id.transactions_recycler_view;
    }

    @Override
    protected boolean recyclerHasFixedSize() {
        return true;
    }


    @Override
    @SuppressWarnings("unchecked")
    public FermatAdapter getAdapter() {
        if (adapter == null) {
            //WalletStoreItemPopupMenuListener listener = getWalletStoreItemPopupMenuListener();
            adapter = new ChunckValuesHistoryAdapter(getActivity(), lstTransaction,cryptoWallet,referenceWalletSession,this);
            adapter.setFermatListEventListener(this); // setting up event listeners

        }
        return adapter;
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        return layoutManager;
    }

    @Override
    public List<LossProtectedWalletTransaction> getMoreDataAsync(FermatRefreshTypes refreshType, int pos) throws CantListCryptoWalletIntraUserIdentityException, CantGetCryptoLossProtectedWalletException, CantListLossProtectedTransactionsException {
        try {


            LossProtectedWalletIntraUserIdentity intraUserLoginIdentity = null;
            intraUserLoginIdentity = referenceWalletSession.getIntraUserModuleManager();
            String intraUserPk = null;
            if (intraUserLoginIdentity != null) {
                intraUserPk = intraUserLoginIdentity.getPublicKey();
            }

            //when refresh offset set 0
            if (refreshType.equals(FermatRefreshTypes.NEW))
                offset = 0;

            lstTransaction = cryptoWallet.listLastActorTransactionsByTransactionType(
                    BalanceType.AVAILABLE,
                    TransactionType.DEBIT,
                    referenceWalletSession.getAppPublicKey(),
                    intraUserPk,
                    blockchainNetworkType,
                    20, 0);

        } catch (Exception e) {
            referenceWalletSession.getErrorManager().reportUnexpectedSubAppException(SubApps.CWP_WALLET_STORE,
                    UnexpectedSubAppExceptionSeverity.DISABLES_SOME_FUNCTIONALITY_WITHIN_THIS_FRAGMENT, e);
           e.printStackTrace();
       }

        return lstTransaction;
    }

    @Override
    public void onItemClickListener(LossProtectedWalletTransaction item, int position) {
        selectedItem = item;
        onRefresh();
        //showDetailsActivityFragment(selectedItem);
    }

    /**
     * On Long item Click Listener
     *
     * @param data
     * @param position
     */
    @Override
    public void onLongItemClickListener(LossProtectedWalletTransaction data, int position) {

    }


    @Override
    public void onPostExecute(Object... result) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            if (result != null && result.length > 0) {
                lstTransaction = (ArrayList) result[0];
                if (adapter != null)
                    adapter.changeDataSet(lstTransaction);
                if(lstTransaction.isEmpty()) FermatAnimationsUtils.showEmpty(getActivity(),true,empty);
                else FermatAnimationsUtils.showEmpty(getActivity(),false,empty);

            }
        }
    }

    @Override
    public void onErrorOccurred(Exception ex) {
        isRefreshing = false;
        if (isAttached) {
            swipeRefreshLayout.setRefreshing(false);
            //CommonLogger.exception(TAG, ex.getMessage(), ex);
        }
    }



    public void setReferenceWalletSession(LossProtectedWalletSession referenceWalletSession) {
        this.referenceWalletSession = referenceWalletSession;
    }


}
