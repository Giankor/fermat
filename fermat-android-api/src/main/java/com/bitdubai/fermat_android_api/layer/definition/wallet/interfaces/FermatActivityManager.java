package com.bitdubai.fermat_android_api.layer.definition.wallet.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.navigation_structure.interfaces.FermatRuntime;
import com.bitdubai.fermat_api.layer.osa_android.broadcaster.FermatBundle;

/**
 * Created by mati on 2016.02.13..
 */
public interface FermatActivityManager {


    FermatRuntime getRuntimeManager();

    //esto no va acá
    void reportError(String userTo) throws Exception;

    int notificateProgressBroadcast(FermatBundle bundle);
}
