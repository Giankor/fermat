package com.bitdubai.reference_wallet.crypto_broker_wallet.common.holders;

import android.view.View;

import com.bitdubai.fermat_android_api.layer.definition.wallet.views.FermatTextView;
import com.bitdubai.reference_wallet.crypto_broker_wallet.R;

/**
 * Created by nelson on 30/12/15.
 */
public class LocationViewHolder extends SingleDeletableItemViewHolder<String> {

    private FermatTextView title;


    public LocationViewHolder(View itemView) {
        super(itemView);

        title = (FermatTextView) itemView.findViewById(R.id.cbw_title);
        itemView.findViewById(R.id.cbw_sub_title).setVisibility(View.GONE);
    }

    @Override
    public void bind(String data) {
        title.setText(data);
    }

    @Override
    public int getCloseButtonResource() {
        return R.id.cbw_close_img_button;
    }
}
