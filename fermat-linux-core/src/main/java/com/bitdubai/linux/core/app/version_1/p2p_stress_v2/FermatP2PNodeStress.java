/*
* @#FermatP2PNodeStress.java - 2016
* Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
* BITDUBAI/CONFIDENTIAL
*/
package com.bitdubai.linux.core.app.version_1.p2p_stress_v2;

import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Developers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_core.FermatSystem;
import com.bitdubai.fermat_core_api.layer.all_definition.system.exceptions.CantStartSystemException;
import com.bitdubai.fermat_osa_linux_core.OSAPlatform;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.commons.clients.interfaces.NetworkClientManager;
import com.bitdubai.linux.core.app.version_1.structure.context.FermatLinuxContext;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * The Class <code>com.bitdubai.linux.core.app.version_1.p2p_stress_v2.FermatP2PNodeStress</code>
 * <p/>
 * Created by Hendry Rodriguez - (elnegroevaristo@gmail.com) on 19/07/16.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class FermatP2PNodeStress extends AbstractJavaSamplerClient implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Represent the SERVER IP DEFAULT
     */
    private static final String SERVER_IP_DEFAULT = "52.40.221.93";

    /**
     * Represent the WS_PROTOCOL
     */
    private static final String WS_PROTOCOL = "ws://";

    /**
     * Represent the DEFAULT_PORT
     */
    private static final int DEFAULT_PORT = 8080;

    /**
     * Represent the fermatContext instance
     */
    private static final FermatLinuxContext fermatLinuxContext = FermatLinuxContext.getInstance();

    /**
     * Represent the fermatSystem instance
     */
    private static final FermatSystem fermatSystem = FermatSystem.getInstance();


    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        /*
		*  Start the plugin JMeter
		*/
        SampleResult sampleResult = new SampleResult();
        sampleResult.sampleStart();

        try {

            fermatSystem.start(fermatLinuxContext, new OSAPlatform());
            fermatSystem.startAndGetPluginVersion(new PluginVersionReference(Platforms.COMMUNICATION_PLATFORM, Layers.COMMUNICATION, Plugins.NETWORK_CLIENT, Developers.BITDUBAI, new Version()));
            final NetworkClientManager clientManager = (NetworkClientManager) fermatSystem.startAndGetPluginVersion(new PluginVersionReference(Platforms.COMMUNICATION_PLATFORM, Layers.COMMUNICATION, Plugins.NETWORK_CLIENT, Developers.BITDUBAI, new Version()));


            /*
			* wait 5 minutes to complete All the work of the Plugins
			*/
            TimeUnit.MINUTES.sleep(5);

            clientManager.stop();

            sampleResult.setSuccessful(true);
            sampleResult.setResponseCode("200");

            StringBuffer stringBufferResult = new StringBuffer();
            stringBufferResult.append("totalOfProfileSendToCheckin: ").append(clientManager.getConnection().getTotalOfProfileSendToCheckin());
            stringBufferResult.append("\ntotalOfProfileSuccessChecked: ").append(clientManager.getConnection().getTotalOfProfileSuccessChecked());
            stringBufferResult.append(" totalOfProfileFailureToCheckin: ").append(clientManager.getConnection().getTotalOfProfileFailureToCheckin());
            stringBufferResult.append("\n\ntotalOfMessagesSents: ").append(clientManager.getConnection().getTotalOfMessagesSents());
            stringBufferResult.append("\ntotalOfMessagesSentsSuccessfully: ").append(clientManager.getConnection().getTotalOfMessagesSentsSuccessfully());
            stringBufferResult.append(" totalOfMessagesSentsFails: ").append(clientManager.getConnection().getTotalOfMessagesSentsFails());

            sampleResult.setSamplerData(stringBufferResult.toString());

            fermatSystem.onDestroy();


        } catch (Exception e) {
           /*
			* set true and the Response Code 500, the work has been BAD
			*/
            sampleResult.setSuccessful(false);
            sampleResult.setResponseCode("500");
            sampleResult.setResponseMessage("Exception: " + e);
        }

        return sampleResult;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {

    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments params = new Arguments();
        params.addArgument("URI", WS_PROTOCOL + SERVER_IP_DEFAULT + ":" + DEFAULT_PORT + "/fermat/ws/client-channel");
        return params;
    }


    public static void main(String[] args) {

        try {

            fermatSystem.start(fermatLinuxContext, new OSAPlatform());
            fermatSystem.startAndGetPluginVersion(new PluginVersionReference(Platforms.COMMUNICATION_PLATFORM, Layers.COMMUNICATION, Plugins.NETWORK_CLIENT, Developers.BITDUBAI, new Version()));
            final NetworkClientManager clientManager = (NetworkClientManager) fermatSystem.startAndGetPluginVersion(new PluginVersionReference(Platforms.COMMUNICATION_PLATFORM, Layers.COMMUNICATION, Plugins.NETWORK_CLIENT, Developers.BITDUBAI, new Version()));


            /*
			* wait 5 minutes to complete All the work of the Plugins
			*/
            TimeUnit.MINUTES.sleep(2);

            clientManager.stop();

            StringBuffer stringBufferResult = new StringBuffer();
            stringBufferResult.append("totalOfProfileSendToCheckin: ").append(clientManager.getConnection().getTotalOfProfileSendToCheckin());
            stringBufferResult.append("\ntotalOfProfileSuccessChecked: ").append(clientManager.getConnection().getTotalOfProfileSuccessChecked());
            stringBufferResult.append(" totalOfProfileFailureToCheckin: ").append(clientManager.getConnection().getTotalOfProfileFailureToCheckin());
            stringBufferResult.append("\n\ntotalOfMessagesSents: ").append(clientManager.getConnection().getTotalOfMessagesSents());
            stringBufferResult.append("\ntotalOfMessagesSentsSuccessfully: ").append(clientManager.getConnection().getTotalOfMessagesSentsSuccessfully());
            stringBufferResult.append(" totalOfMessagesSentsFails: ").append(clientManager.getConnection().getTotalOfMessagesSentsFails());

            System.out.println("\n********************************   SamplerData   *************************************");
            System.out.println("\n" + stringBufferResult.toString());

            fermatSystem.onDestroy();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
