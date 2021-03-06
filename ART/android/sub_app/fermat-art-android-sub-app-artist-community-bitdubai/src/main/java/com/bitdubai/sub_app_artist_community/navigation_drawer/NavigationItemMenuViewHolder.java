package com.bitdubai.sub_app_artist_community.navigation_drawer;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitdubai.fermat_android_api.ui.holders.FermatViewHolder;
import com.bitdubai.reference_wallet.artist_community.R;

/**
 * Created by Gabriel Araujo (gabe_512@hotmail.com) on 08/04/16.
 */
public class NavigationItemMenuViewHolder extends FermatViewHolder {
    private TextView label;
    private ImageView icon;
    private LinearLayout rowContainer;


    public NavigationItemMenuViewHolder(View itemView) {
        super(itemView);

        label = (TextView) itemView.findViewById(R.id.afc_textView_label);
        icon = (ImageView) itemView.findViewById(R.id.afc_imageView_icon);
        rowContainer = (LinearLayout) itemView.findViewById(R.id.afc_row_container);

    }

    public TextView getLabel() {
        return label;
    }

    public ImageView getIcon() {
        return icon;
    }

    public LinearLayout getRowContainer() {
        return rowContainer;
    }
}
