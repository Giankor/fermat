/*
 * @#AssetTransmissionPluginRoot.java - 2015
 * Copyright bitDubai.com., All rights reserved.
 * You may not modify, use, reproduce or distribute this software.
 * BITDUBAI/CONFIDENTIAL
 */
package com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.Plugin;
import com.bitdubai.fermat_api.Service;
import com.bitdubai.fermat_api.layer.all_definition.components.enums.PlatformComponentType;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.DiscoveryQueryParameters;
import com.bitdubai.fermat_api.layer.all_definition.components.interfaces.PlatformComponentProfile;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.developer.LogManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Plugins;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.EventSource;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.network_service.enums.NetworkServiceType;
import com.bitdubai.fermat_api.layer.all_definition.network_service.interfaces.NetworkService;
import com.bitdubai.fermat_api.layer.all_definition.network_service.interfaces.NetworkServiceConnectionManager;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.DealsWithPluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.location_system.Location;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.DealsWithLogger;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.asset_issuer.exceptions.CantRegisterActorAssetIssuerException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.exceptions.CantRegisterActorAssetRedeemPointException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.interfaces.ActorNetworkServiceRedeemPoint;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.interfaces.AssetRedeemPointActorNetworkServiceManager;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.communications.CommunicationNetworkServiceConnectionManager;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.communications.CommunicationRegistrationProcessNetworkServiceAgent;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDatabaseConstants;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.database.communications.CommunicationNetworkServiceDeveloperDatabaseFactory;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.event_handlers.CompleteComponentConnectionRequestNotificationEventHandler;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.event_handlers.CompleteComponentRegistrationNotificationEventHandler;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.event_handlers.CompleteRequestListComponentRegisteredNotificationEventHandler;
import com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.redeem_point.developer.bitdubai.version_1.exceptions.CantInitializeTemplateNetworkServiceDatabaseException;
import com.bitdubai.fermat_p2p_api.layer.all_definition.communication.enums.P2pEventType;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.DealsWithWsCommunicationsCloudClientManager;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.WsCommunicationsCloudClientManager;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.client.CommunicationsClientConnection;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRegisterComponentException;
import com.bitdubai.fermat_p2p_api.layer.p2p_communication.commons.exceptions.CantRequestListException;
import com.bitdubai.fermat_pip_api.layer.pip_actor.exception.CantGetLogTool;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.DealsWithErrors;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.DealsWithEvents;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.event_manager.interfaces.EventManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * The Class <code>com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.AssetRedeemPointActorNetworkServicePluginRoot</code> is
 * the responsible to initialize all component to work together, and hold all resources they needed.
 * <p/>
 *
 * Created by Roberto Requena - (rrequena) on 21/07/15.
 * Modified by franklin on 17/10/2015
 *
 * @version 1.0
 */
public class AssetRedeemPointActorNetworkServicePluginRoot implements ActorNetworkServiceRedeemPoint, AssetRedeemPointActorNetworkServiceManager, DealsWithWsCommunicationsCloudClientManager, DealsWithErrors, DealsWithEvents, DealsWithPluginDatabaseSystem, DealsWithPluginFileSystem,DealsWithLogger, LogManagerForDevelopers, Plugin, Service, Serializable, NetworkService {

    /**
     * Represent the logManager
     */
    private LogManager logManager;
    /**
     * Represent the EVENT_SOURCE
     */
    public final static EventSource EVENT_SOURCE = EventSource.NETWORK_SERVICE_ACTOR_ASSET_ISSUER;
    /**
     * Service Interface member variables.
     */
    ServiceStatus serviceStatus = ServiceStatus.CREATED;

    /**
     * Represent the register
     */
    private boolean register;

    /**
     * Represent the name
     */
    private String name;

    /**
     * Represent the alias
     */
    private String alias;

    /**
     * Represent the extraData
     */
    private String extraData;

    /**
     * Represent the dataBase
     */
    private Database dataBase;

    /**
     * Represent the identity
     */
    private ECCKeyPair identity;

    /**
     * Represent the networkServiceType
     */
    private NetworkServiceType networkServiceType;

    /**
     * Represent the platformComponentProfile
     */
    private PlatformComponentProfile platformComponentProfile;
    /**
     * DealsWithPlatformDatabaseSystem Interface member variables.
     */
    PluginDatabaseSystem pluginDatabaseSystem;
    /**
     * FileSystem Interface member variables.
     */
    PluginFileSystem pluginFileSystem;
    /**
     * Plugin Interface member variables.
     */
    UUID pluginId;
    /**
     * DealsWithErrors Interface member variables.
     */
    ErrorManager errorManager;
    /**
     * DealsWithEvents Interface member variables.
     */
    EventManager eventManager;

    List<FermatEventListener> listenersAdded = new ArrayList<>();
    /**
     * DealsWithLogger interface member variable
     */

    static Map<String, LogLevel> newLoggingLevel = new HashMap<String, LogLevel>();

    /**
     * Represent the platformComponentType
     */
    private PlatformComponentType platformComponentType;

    /**
     * Represent the remoteNetworkServicesRegisteredList
     */
    private List<PlatformComponentProfile> remoteNetworkServicesRegisteredList;

    private List<PlatformComponentProfile> remoteActorNetworkServicesRegisteredListAux;

    /**
     * Represent the communicationNetworkServiceDeveloperDatabaseFactory
     */
    private CommunicationNetworkServiceDeveloperDatabaseFactory communicationNetworkServiceDeveloperDatabaseFactory;

    /**
     * Represent the communicationRegistrationProcessNetworkServiceAgent
     */
    private CommunicationRegistrationProcessNetworkServiceAgent communicationRegistrationProcessNetworkServiceAgent;

    /**
     * Represent the communicationNetworkServiceConnectionManager
     */
    private CommunicationNetworkServiceConnectionManager communicationNetworkServiceConnectionManager;

    /**
     * Represent the wsCommunicationsCloudClientManager
     */
    private WsCommunicationsCloudClientManager wsCommunicationsCloudClientManager;

    /**
     * Represent the actorAssetUserRegisteredList
     */
    private List<ActorAssetRedeemPoint> actorAssetRedeemPointRegisteredList;

    /**
     * Represent the actorAssetUserPendingToRegistration
     */
    private List<PlatformComponentProfile> actorAssetRedeemPointPendingToRegistration;


    /**
     * Constructor
     */
    public AssetRedeemPointActorNetworkServicePluginRoot() {
        super();
        this.listenersAdded = new ArrayList<>();
        this.platformComponentType = PlatformComponentType.NETWORK_SERVICE;
        this.networkServiceType = NetworkServiceType.ASSET_REDEEM_POINT_ACTOR;
        this.name = "Actor Network Service Asset RedeemPoint";
        this.alias = "ActorNetworkServiceAssetRedeemPoin";
        this.extraData = null;
        this.actorAssetRedeemPointRegisteredList = new ArrayList<>();
        this.actorAssetRedeemPointPendingToRegistration = new ArrayList<>();
    }

    @Override
    public void setEventManager(EventManager DealsWithEvents) {
        this.eventManager = DealsWithEvents;
    }

    /**
     * DealsWithErrors Interface implementation.
     */

    @Override
    public void setErrorManager(ErrorManager errorManager) {
        this.errorManager = errorManager;
    }

    /**
     * DealsWithPluginDatabaseSystem interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem(PluginDatabaseSystem pluginDatabaseSystem) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * DealWithPluginFileSystem Interface implementation.
     */
    @Override
    public void setPluginFileSystem(PluginFileSystem pluginFileSystem) {
        this.pluginFileSystem = pluginFileSystem;
    }

    @Override
    public void setId(UUID pluginId) {
        this.pluginId = pluginId;
    }


    @Override
    public List<String> getClassesFullPath() {
        List<String> returnedClasses = new ArrayList<String>();
        returnedClasses.add("com.bitdubai.fermat_dap_plugin.layer.actor.network.service.asset.issuer.developer.bitdubai.version_1.AssetRedeemPointActorNetworkServicePluginRoot");
        /**
         * I return the values.
         */
        return returnedClasses;
    }

    @Override
    public void setLoggingLevelPerClass(Map<String, LogLevel> newLoggingLevel) {
        /**
         * Modify by Manuel on 25/07/2015
         * I will wrap all this method within a try, I need to catch any generic java Exception
         */
        try {

            /**
             * I will check the current values and update the LogLevel in those which is different
             */
            for (Map.Entry<String, LogLevel> pluginPair : newLoggingLevel.entrySet()) {
                /**
                 * if this path already exists in the Root.bewLoggingLevel I'll update the value, else, I will put as new
                 */
                if (AssetRedeemPointActorNetworkServicePluginRoot.newLoggingLevel.containsKey(pluginPair.getKey())) {
                    AssetRedeemPointActorNetworkServicePluginRoot.newLoggingLevel.remove(pluginPair.getKey());
                    AssetRedeemPointActorNetworkServicePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
                } else {
                    AssetRedeemPointActorNetworkServicePluginRoot.newLoggingLevel.put(pluginPair.getKey(), pluginPair.getValue());
                }
            }

        } catch (Exception exception) {
            FermatException e = new CantGetLogTool(CantGetLogTool.DEFAULT_MESSAGE, FermatException.wrapException(exception), "setLoggingLevelPerClass: " + AssetRedeemPointActorNetworkServicePluginRoot.newLoggingLevel, "Check the cause");
            // this.errorManager.reportUnexpectedAddonsException(Addons.EXTRA_USER, UnexpectedAddonsExceptionSeverity.DISABLES_THIS_ADDONS, e);
        }
    }

    @Override
    public void start() throws CantStartPluginException {

        System.out.println("Star Plugin AssetIssuerActorNetworkService");
        logManager.log(AssetRedeemPointActorNetworkServicePluginRoot.getLogLevelByClass(this.getClass().getName()), "AssetIssuerActorNetworkService - Starting", "AssetRedeemPointActorNetworkServicePluginRoot - Starting", "AssetRedeemPointActorNetworkServicePluginRoot - Starting");


        /*
         * Validate required resources
         */
        validateInjectedResources();

        try {
             /*
             * Create a new key pair for this execution
             */
            identity = new ECCKeyPair();
            /*
            * Initialize the data base
            */
            initializeDb();

                        /*
             * Initialize Developer Database Factory
             */
            communicationNetworkServiceDeveloperDatabaseFactory = new CommunicationNetworkServiceDeveloperDatabaseFactory(pluginDatabaseSystem, pluginId);
            communicationNetworkServiceDeveloperDatabaseFactory.initializeDatabase();
                        /*
             * Initialize listeners
             */
            initializeListener();

            initilizelistener2();

            /*
             * Verify if the communication cloud client is active
             */
            if (!wsCommunicationsCloudClientManager.isDisable()) {

                /*
                 * Initialize the agent and start
                 */
                communicationRegistrationProcessNetworkServiceAgent = new CommunicationRegistrationProcessNetworkServiceAgent(this, wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection());
                communicationRegistrationProcessNetworkServiceAgent.start();
            }

            this.serviceStatus = ServiceStatus.STARTED;
        } catch (CantInitializeTemplateNetworkServiceDatabaseException exception) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("Database Name: " + CommunicationNetworkServiceDatabaseConstants.DATA_BASE_NAME);

            String context = contextBuffer.toString();
            String possibleCause = "The Asset RedeemPoint Actor Network Service Database triggered an unexpected problem that wasn't able to solve by itself";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, exception, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE,UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;
        }
    }


    /**
     * Initialize the event listener and configure
     */
    private void initializeListener() {
                 /*
         * Listen and handle Complete Component Registration Notification Event
         */
        FermatEventListener fermatEventListener = eventManager.getNewListener(P2pEventType.COMPLETE_COMPONENT_REGISTRATION_NOTIFICATION);
        fermatEventListener.setEventHandler(new CompleteComponentRegistrationNotificationEventHandler(this));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

         /*
         * Listen and handle Complete Request List Component Registered Notification Event
         */
        fermatEventListener = eventManager.getNewListener(P2pEventType.COMPLETE_REQUEST_LIST_COMPONENT_REGISTERED_NOTIFICATION);
        fermatEventListener.setEventHandler(new CompleteRequestListComponentRegisteredNotificationEventHandler(this));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

        /*
         * Listen and handle Complete Request List Component Registered Notification Event
         */
        fermatEventListener = eventManager.getNewListener(P2pEventType.COMPLETE_COMPONENT_CONNECTION_REQUEST_NOTIFICATION);
        fermatEventListener.setEventHandler(new CompleteComponentConnectionRequestNotificationEventHandler(this));
        eventManager.addListener(fermatEventListener);
        listenersAdded.add(fermatEventListener);

    }

    private void initilizelistener2() {

    }


    /**
     * This method initialize the database
     *
     * @throws CantInitializeTemplateNetworkServiceDatabaseException
     */
    private void initializeDb() throws CantInitializeTemplateNetworkServiceDatabaseException {

    }

    /**
     * This method validate is all required resource are injected into
     * the plugin root by the platform
     *
     * @throws CantStartPluginException
     */
    private void validateInjectedResources() throws CantStartPluginException {

         /*
         * If all resources are inject
         */
        if (wsCommunicationsCloudClientManager == null ||
                pluginDatabaseSystem == null ||
                errorManager == null ||
                eventManager == null) {

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "No all required resource are injected";
            CantStartPluginException pluginStartException = new CantStartPluginException(CantStartPluginException.DEFAULT_MESSAGE, null, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_ISSUER_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);
            throw pluginStartException;
        }

    }

    @Override
    public void pause() {
        /*
         * Pause
         */
        communicationNetworkServiceConnectionManager.pause();

        /*
         * Set the new status
         */
        this.serviceStatus = ServiceStatus.PAUSED;
    }

    @Override
    public void resume() {
        /*
         * resume the managers
         */
        communicationNetworkServiceConnectionManager.resume();

        /*
         * Set the new status
         */
        this.serviceStatus = ServiceStatus.STARTED;
    }

    @Override
    public void stop() {
        //Clear all references of the event listeners registered with the event manager.
        listenersAdded.clear();

        /*
         * Stop all connection on the managers
         */
        communicationNetworkServiceConnectionManager.closeAllConnection();

        //set to not register
        register = Boolean.FALSE;

        /*
         * Set the new status
         */
        this.serviceStatus = ServiceStatus.STOPPED;
    }

    @Override
    public ServiceStatus getStatus() {
        return serviceStatus;
    }

    /**
     * Static method to get the logging level from any class under root.
     *
     * @param className
     * @return
     */
    public static LogLevel getLogLevelByClass(String className) {
        try {
            /**
             * sometimes the classname may be passed dinamically with an $moretext
             * I need to ignore whats after this.
             */
            String[] correctedClass = className.split((Pattern.quote("$")));
            return AssetRedeemPointActorNetworkServicePluginRoot.newLoggingLevel.get(correctedClass[0]);
        } catch (Exception e) {
            /**
             * If I couldn't get the correct loggin level, then I will set it to minimal.
             */
            return DEFAULT_LOG_LEVEL;
        }
    }

    public boolean isRegister() {
        return register;
    }

    /**
     * Get the IdentityPublicKey
     *
     * @return String
     */
    public String getIdentityPublicKey() {
        return this.identity.getPublicKey();
    }

    /**
     * Get the Name
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Get the Alias
     *
     * @return String
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Get the ExtraData
     *
     * @return String
     */
    public String getExtraData() {
        return extraData;
    }

    /**
     * Set the PlatformComponentProfile
     *
     * @param platformComponentProfile
     */
    public void setPlatformComponentProfile(PlatformComponentProfile platformComponentProfile) {
        this.platformComponentProfile = platformComponentProfile;
    }

    /**
     * This method initialize the communicationNetworkServiceConnectionManager.
     * IMPORTANT: Call this method only in the CommunicationRegistrationProcessNetworkServiceAgent, when execute the registration process
     * because at this moment, is create the platformComponentProfile for this component
     */
    public void initializeCommunicationNetworkServiceConnectionManager(){
        this.communicationNetworkServiceConnectionManager = new CommunicationNetworkServiceConnectionManager(platformComponentProfile, identity, wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection(), dataBase, errorManager, eventManager);
    }


    @Override
    public void registerActorAssetRedeemPoint(ActorAssetRedeemPoint actorAssetRedeemPointToRegister) throws CantRegisterActorAssetRedeemPointException {
        try {

            CommunicationsClientConnection communicationsClientConnection = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection();

            /*
             * If register
             */
            if(this.isRegister()){

                System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");

                System.out.println("Registrar Datos "+ actorAssetRedeemPointToRegister.getName());

                System.out.println("WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW");

                /*
                 * Construct the profile
                 */
                PlatformComponentProfile platformComponentProfileAssetRedeemPoint =  communicationsClientConnection.constructPlatformComponentProfileFactory(actorAssetRedeemPointToRegister.getPublicKey(),
                        actorAssetRedeemPointToRegister.getName().toLowerCase().trim(),
                        actorAssetRedeemPointToRegister.getName(),
                        NetworkServiceType.UNDEFINED,
                        PlatformComponentType.ACTOR_ASSET_REDEEM_POINT,
                        Arrays.toString(actorAssetRedeemPointToRegister.getProfileImage()));
                /*
                 * ask to the communication cloud client to register
                 */
                communicationsClientConnection.registerComponentForCommunication(platformComponentProfileAssetRedeemPoint);

            }else{

                /*
                 * Construct the profile
                 */
                PlatformComponentProfile platformComponentProfileAssetRedeemPoint =  communicationsClientConnection.constructPlatformComponentProfileFactory(actorAssetRedeemPointToRegister.getPublicKey(),
                        actorAssetRedeemPointToRegister.getName().toLowerCase().trim(),
                        actorAssetRedeemPointToRegister.getName(),
                        NetworkServiceType.UNDEFINED,
                        PlatformComponentType.ACTOR_ASSET_REDEEM_POINT,
                        Arrays.toString(actorAssetRedeemPointToRegister.getProfileImage()));
                /*
                 * Add to the list of pending to register
                 */
                actorAssetRedeemPointPendingToRegistration.add(platformComponentProfileAssetRedeemPoint);

            }

        }catch (Exception e){

            StringBuffer contextBuffer = new StringBuffer();
            contextBuffer.append("Plugin ID: " + pluginId);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("wsCommunicationsCloudClientManager: " + wsCommunicationsCloudClientManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("pluginDatabaseSystem: " + pluginDatabaseSystem);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("errorManager: " + errorManager);
            contextBuffer.append(CantStartPluginException.CONTEXT_CONTENT_SEPARATOR);
            contextBuffer.append("eventManager: " + eventManager);

            String context = contextBuffer.toString();
            String possibleCause = "Plugin was not registered";

            CantRegisterActorAssetRedeemPointException pluginStartException = new CantRegisterActorAssetRedeemPointException(CantStartPluginException.DEFAULT_MESSAGE, e, context, possibleCause);

            errorManager.reportUnexpectedPluginException(Plugins.BITDUBAI_DAP_ASSET_REDEEM_POINT_ACTOR_NETWORK_SERVICE, UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, pluginStartException);

            throw pluginStartException;

        }
    }

    @Override
    public void sendMessage(ActorAssetRedeemPoint actorAssetRedeemPoint, ActorAssetRedeemPoint actorAssetRedeemDestination, String msjContent) throws com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.exceptions.CantSendMessageException {

    }

    @Override
    public void requestListActorAssetRedeemPointRegistered() throws CantRegisterActorAssetRedeemPointException {
        /*
         * Construct the discovery query parameters
         */
        DiscoveryQueryParameters discoveryQueryParametersAssetIssuer = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().
                constructDiscoveryQueryParamsFactory(PlatformComponentType.ACTOR_ASSET_REDEEM_POINT, //applicant = who made the request
                        NetworkServiceType.UNDEFINED,
                        null,                     // alias
                        null,                     // identityPublicKey
                        null,                     // location
                        null,                     // distance
                        null,                     // name
                        null,                     // extraData
                        null,                     // offset
                        null,                     // max
                        null,                     // fromOtherPlatformComponentType, when use this filter apply the identityPublicKey
                        null);

        /*
         * request the list to the server
         */
        requestRemoteNetworkServicesRegisteredList(discoveryQueryParametersAssetIssuer);
    }

    @Override
    public List<ActorAssetRedeemPoint> getListActorAssetRedeemPointRegistered() throws com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.exceptions.RequestedListNotReadyRecevivedException {
        return actorAssetRedeemPointRegisteredList;
    }

//    @Override
//    public void handleCompleteRequestListRegisteredAssetIssuerActorNetworksNotificationEvent(List<ActorAssetIssuer> actorAssetIssuerList) {
//        System.out.println("Satisfactoriamente llego la lista remota de asset issuer actor");
//    }
//
//    @Override
//    public void handleCompleteClientAssetIssuerActorRegistrationNotificationEvent(ActorAssetIssuer actorAssetIssuerList) {
//        System.out.println("==========================================================");
//
//        System.out.println("Satisfactoriamente se Registro "+actorAssetIssuerList.getName());
//
//        System.out.println("==========================================================");
//    }

    @Override
    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    @Override
    public UUID getId() {
        return this.pluginId;
    }

    @Override
    public PlatformComponentProfile getPlatformComponentProfile() {
        return platformComponentProfile;
    }

    @Override
    public PlatformComponentType getPlatformComponentType() {
        return platformComponentType;
    }

    @Override
    public NetworkServiceType getNetworkServiceType() {
        return networkServiceType;
    }

    @Override
    public List<PlatformComponentProfile> getRemoteNetworkServicesRegisteredList() {
        return remoteNetworkServicesRegisteredList;
    }

    @Override
    public void requestRemoteNetworkServicesRegisteredList(DiscoveryQueryParameters discoveryQueryParameters) {
        System.out.println(" AssetRedeemPointActorNetworkServicePluginRoot - requestRemoteNetworkServicesRegisteredList");

        /*
         * Request the list of component registers
         */
        try {
            wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().requestListComponentRegistered(discoveryQueryParameters);
        } catch (CantRequestListException e) {
            e.printStackTrace();
        }
    }

    @Override
    public NetworkServiceConnectionManager getNetworkServiceConnectionManager() {
        return communicationNetworkServiceConnectionManager;
    }

    @Override
    public DiscoveryQueryParameters constructDiscoveryQueryParamsFactory(PlatformComponentType platformComponentType, NetworkServiceType networkServiceType, String alias, String identityPublicKey, Location location, Double distance, String name, String extraData, Integer firstRecord, Integer numRegister, PlatformComponentType fromOtherPlatformComponentType, NetworkServiceType fromOtherNetworkServiceType) {
        return wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection().constructDiscoveryQueryParamsFactory(platformComponentType               ,
                networkServiceType, alias, identityPublicKey, location, distance, name, extraData, firstRecord, numRegister, fromOtherPlatformComponentType, fromOtherNetworkServiceType);

    }

    @Override
    public void handleCompleteComponentRegistrationNotificationEvent(PlatformComponentProfile platformComponentProfileRegistered) {
        System.out.println(" CommunicationNetworkServiceConnectionManager - Starting method handleCompleteComponentRegistrationNotificationEvent");

          /*
         * If the component registered have my profile and my identity public key
         */
        if (platformComponentProfileRegistered.getPlatformComponentType()  == PlatformComponentType.NETWORK_SERVICE &&
                platformComponentProfileRegistered.getNetworkServiceType()  == NetworkServiceType.ASSET_REDEEM_POINT_ACTOR &&
                platformComponentProfileRegistered.getIdentityPublicKey().equals(identity.getPublicKey())){

            /*
             * Mark as register
             */
            this.register = Boolean.TRUE;

            /*
             * If exist actor asset redeem point pending to registration
             */
            if (actorAssetRedeemPointPendingToRegistration != null && !actorAssetRedeemPointPendingToRegistration.isEmpty()){

                CommunicationsClientConnection communicationsClientConnection = wsCommunicationsCloudClientManager.getCommunicationsCloudClientConnection();

                for (PlatformComponentProfile platformComponentProfileAssetIssuer: actorAssetRedeemPointPendingToRegistration) {

                     /*
                     * ask to the communication cloud client to register
                     */
                    try {
                        communicationsClientConnection.registerComponentForCommunication(platformComponentProfileAssetIssuer);
                    } catch (CantRegisterComponentException e) {
                        e.printStackTrace();
                    }

                }

            }
//
//            /* test register one actor */
//
//            ReturnAssetUserActorNetworkService returnactor =new AssetUserANS();
//
//            Location loca = null;
//
//
//            ActorAssetUser actorAssetUserNewRegsitered = null;
//            try {
//                actorAssetUserNewRegsitered = returnactor.creatActorAssetUser("123456789","Pedrito",new byte[]{10,3}, loca);
//            } catch (CantCreateAssetUserActorException e) {
//                e.printStackTrace();
//            }
//
//
//            try {
//                registerActorAssetUser(actorAssetUserNewRegsitered);
//
//            } catch (CantRegisterActorAssetUserException e) {
//                e.printStackTrace();
//            }
//
//           /* test register one actor */


        }

        /*
         * If is a actor registered
         */
        if (platformComponentProfileRegistered.getPlatformComponentType()  == PlatformComponentType.ACTOR_ASSET_ISSUER &&
                platformComponentProfileRegistered.getNetworkServiceType()  == NetworkServiceType.UNDEFINED){



            System.out.println(" Actor  Asset Redeem Point Registered "+platformComponentProfileRegistered.getIdentityPublicKey()+"\n Alias "+platformComponentProfileRegistered.getAlias());

//            ReturnAssetUserActorNetworkService returnactor = new AssetUserANS();
//
//            Location loca = null;
//
//            ActorAssetUser actorAssetUserNewRegsitered = null;
//            try {
//                actorAssetUserNewRegsitered = returnactor.creatActorAssetUser(platformComponentProfileRegistered.getIdentityPublicKey(),
//                        platformComponentProfileRegistered.getName(),convertoByteArrayfromString(platformComponentProfileRegistered.getExtraData()),loca);
//            } catch (CantCreateAssetUserActorException e) {
//                e.printStackTrace();
//            }
//
//
//            FermatEvent event =  eventManager.getNewEvent(DapEvenType.COMPLETE_ASSET_USER_REGISTRATION_NOTIFICATION);
//            event.setSource(EventSource.ACTOR_ASSET_USER);
//
//            ((CompleteClientAssetUserActorRegistrationNotificationEvent)event).setActorAssetUser(actorAssetUserNewRegsitered);
//            eventManager.raiseEvent(event);

        }
    }

    @Override
    public void handleFailureComponentRegistrationNotificationEvent(PlatformComponentProfile networkServiceApplicant, PlatformComponentProfile remo) {
        System.out.println(" CommunicationNetworkServiceConnectionManager - Starting method handleFailureComponentRegistrationNotificationEvent");


    }

    @Override
    public void handleCompleteRequestListComponentRegisteredNotificationEvent(List<PlatformComponentProfile> platformComponentProfileRegisteredList, DiscoveryQueryParameters discoveryQueryParameters) {
        System.out.println(" CommunicationNetworkServiceConnectionManager - Starting method handleCompleteComponentRegistrationNotificationEvent");



        /*
         * if have result create a ActorAssetRedeemPoint
         */

        if(platformComponentProfileRegisteredList != null && !platformComponentProfileRegisteredList.isEmpty()){

            /*
             * Get a remote network service registered from the list requested
             */
            PlatformComponentProfile remoteNetworkServiceToConnect = platformComponentProfileRegisteredList.get(0);

            /*
             * tell to the manager to connect to this remote network service
             */
            //communicationNetworkServiceConnectionManager.connectTo(remoteNetworkServiceToConnect);


            if(remoteNetworkServiceToConnect.getNetworkServiceType()== NetworkServiceType.UNDEFINED &&  remoteNetworkServiceToConnect.getPlatformComponentType()== PlatformComponentType.ACTOR_ASSET_USER ){




                for(PlatformComponentProfile p : platformComponentProfileRegisteredList){


//                    ReturnAssetUserActorNetworkService returnactor = new AssetUserANS();
//
//                    Location loca = null;
//
//                    ActorAssetUser actorAssetUserNew = null;
//                    try {
//                        actorAssetUserNew = returnactor.creatActorAssetUser(p.getIdentityPublicKey(),p.getName(),convertoByteArrayfromString(p.getExtraData()), loca);
//                    } catch (CantCreateAssetUserActorException e) {
//                        e.printStackTrace();
//                    }
//
//                    actorAssetUserRegisteredList.add(actorAssetUserNew);
                }

//                FermatEvent event =  eventManager.getNewEvent(DapEvenType.COMPLETE_REQUEST_LIST_ASSET_USER_REGISTERED_NOTIFICATION);
//                event.setSource(EventSource.ACTOR_ASSET_USER);
//
//                ((CompleteRequestListRegisteredAssetUserActorNetworksNotificationEvent)event).setActorAssetUserList(actorAssetUserRegisteredList);
//                eventManager.raiseEvent(event);


            }else if(remoteNetworkServiceToConnect.getNetworkServiceType()== NetworkServiceType.ASSET_ISSUER_ACTOR &&  remoteNetworkServiceToConnect.getPlatformComponentType()== PlatformComponentType.NETWORK_SERVICE ){

                /*
                 * save into the cache
                 */
                remoteNetworkServicesRegisteredList = platformComponentProfileRegisteredList;

            }
        }
    }

    @Override
    public void handleCompleteComponentConnectionRequestNotificationEvent(PlatformComponentProfile applicantComponentProfile, PlatformComponentProfile remoteComponentProfile) {

    }

    @Override
    public void setWsCommunicationsCloudClientConnectionManager(WsCommunicationsCloudClientManager wsCommunicationsCloudClientManager) {
        this.wsCommunicationsCloudClientManager = wsCommunicationsCloudClientManager;
    }

    @Override
    public void handleCompleteRequestListRegisteredAssetRedeemPointActorNetworksNotificationEvent(List<ActorAssetRedeemPoint> assetRedeemPointList) {

    }

    @Override
    public void handleCompleteClientAssetRedeemPointActorRegistrationNotificationEvent(ActorAssetRedeemPoint actorAssetRedeemPoint) {

    }
}