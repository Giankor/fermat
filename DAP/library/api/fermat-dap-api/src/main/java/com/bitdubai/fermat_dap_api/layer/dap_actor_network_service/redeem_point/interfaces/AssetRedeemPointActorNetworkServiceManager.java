package com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.BlockchainNetworkType;
import com.bitdubai.fermat_dap_api.layer.all_definition.network_service_message.DAPNetworkService;
import com.bitdubai.fermat_dap_api.layer.dap_actor.redeem_point.interfaces.ActorAssetRedeemPoint;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAcceptConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantAskConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantCancelConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantConfirmActorAssetNotificationException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDenyConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantDisconnectConnectionActorAssetException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.exceptions.CantGetActorAssetNotificationException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.interfaces.ActorNotification;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.exceptions.CantRegisterActorAssetRedeemPointException;
import com.bitdubai.fermat_dap_api.layer.dap_actor_network_service.redeem_point.exceptions.CantRequestListActorAssetRedeemPointRegisteredException;

import java.util.List;
import java.util.UUID;

/**
 * Created by franklin on 15/10/15.
 */
public interface AssetRedeemPointActorNetworkServiceManager extends FermatManager, DAPNetworkService {
    /**
     * Register the ActorAssetUser in the cloud server like online
     *
     * @param actorAssetRedeemPointToRegister
     * @throws CantRegisterActorAssetRedeemPointException
     */
    void registerActorAssetRedeemPoint(ActorAssetRedeemPoint actorAssetRedeemPointToRegister) throws CantRegisterActorAssetRedeemPointException;

    /**
     * Update the ActorAssetUser in the cloud server like online
     *
     * @param actorAssetRedeemPointToRegister
     * @throws CantRegisterActorAssetRedeemPointException
     */
    void updateActorAssetRedeemPoint(ActorAssetRedeemPoint actorAssetRedeemPointToRegister) throws CantRegisterActorAssetRedeemPointException;

    /**
     * Get the content of the list previously requested, this method have to call after the
     * the notification event is receive <code>Nombre del Evento</>
     *
     * @return List<ActorAssetRedeemPoint>
     */
    List<ActorAssetRedeemPoint> getListActorAssetRedeemPointRegistered() throws CantRequestListActorAssetRedeemPointRegisteredException;


    /**
     * The method <code>askConnectionActorAsset</code> sends a connection request to another Actor.
     *
     * @param actorAssetLoggedInPublicKey   The logged public key of the
     * @param actorAssetLoggedName          The logged name
     * @param senderType                    The senderType
     * @param actorAssetToAddPublicKey      The actorAssetToAddPublicKey
     * @param actorAssetToAddName           The actorAssetToAddName
     * @param destinationType               The public key of the
     * @param profileImage                  The profile image of the user sending the request
     */
    void askConnectionActorAsset(String actorAssetLoggedInPublicKey,
                                 String actorAssetLoggedName,
                                 Actors senderType,
                                 String actorAssetToAddPublicKey,
                                 String actorAssetToAddName,
                                 Actors destinationType,
                                 byte[] profileImage,
                                 BlockchainNetworkType blockchainNetworkType) throws CantAskConnectionActorAssetException;

    /**
     * The method <code>acceptConnectionActorAsset</code> send an acceptance message of a connection request.
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset accepting the connection request.
     * @param ActorAssetToAddPublicKey    The public key of the actor asset to add
     */
    void acceptConnectionActorAsset(String actorAssetLoggedInPublicKey, String ActorAssetToAddPublicKey) throws CantAcceptConnectionActorAssetException;

    /**
     * The method <code>denyConnectionActorAsset</code> send an rejection message of a connection request.
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset accepting the connection request.
     * @param actorAssetToRejectPublicKey The public key of the actor asset to add
     */
    void denyConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToRejectPublicKey) throws CantDenyConnectionActorAssetException;

    /**
     * The method <coda>disconnectConnectionActorAsset</coda> disconnects and informs the other intra user the disconnecting
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset disconnecting the connection
     * @param actorAssetToDisconnectPublicKey The public key of the user to disconnect
     * @throws CantDisconnectConnectionActorAssetException
     */
    void disconnectConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToDisconnectPublicKey) throws CantDisconnectConnectionActorAssetException;

    /**
     * The method <coda>cancelConnectionActorAsset</coda> cancels and informs the other intra user the cancelling
     *
     * @param actorAssetLoggedInPublicKey The public key of the actor asset cancelling the connection
     * @param actorAssetToCancelPublicKey The public key of the user to cancel
     * @throws CantCancelConnectionActorAssetException
     */
    void cancelConnectionActorAsset(String actorAssetLoggedInPublicKey, String actorAssetToCancelPublicKey) throws CantCancelConnectionActorAssetException;

    /**
     * The method <coda>getPendingNotifications</coda> returns all pending notifications
     * of responses to requests for connection
     *
     * @return List of IntraUserNotification
     */
    List<ActorNotification> getPendingNotifications() throws CantGetActorAssetNotificationException;

    /**
     * The method <coda>confirmActorAssetNotification</coda> confirm the pending notification
     */
    void confirmActorAssetNotification(UUID notificationID) throws CantConfirmActorAssetNotificationException;

}
