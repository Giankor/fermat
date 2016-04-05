package com.bitdubai.fermat_cht_plugin.layer.identity.chat.developer.bitdubai.version_1;

import com.bitdubai.fermat_api.CantStartAgentException;
import com.bitdubai.fermat_api.CantStartPluginException;
import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.annotations.NeededAddonReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.interfaces.FermatManager;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.developer.DatabaseManagerForDevelopers;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabase;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTable;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperDatabaseTableRecord;
import com.bitdubai.fermat_api.layer.all_definition.developer.DeveloperObjectFactory;
import com.bitdubai.fermat_api.layer.all_definition.enums.Addons;
import com.bitdubai.fermat_api.layer.all_definition.enums.Layers;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantOpenDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_cht_api.layer.identity.exceptions.CantCreateNewChatIdentityException;
import com.bitdubai.fermat_cht_plugin.layer.identity.chat.developer.bitdubai.version_1.database.ChatIdentityDatabaseConstants;
import com.bitdubai.fermat_cht_plugin.layer.identity.chat.developer.bitdubai.version_1.database.ChatIdentityDatabaseFactory;
import com.bitdubai.fermat_cht_plugin.layer.identity.chat.developer.bitdubai.version_1.database.ChatIdentityDeveloperFactory;
import com.bitdubai.fermat_cht_plugin.layer.identity.chat.developer.bitdubai.version_1.exceptions.CantInitializeChatIdentityDatabaseException;
import com.bitdubai.fermat_cht_plugin.layer.identity.chat.developer.bitdubai.version_1.structure.ChatIdentityManagerImpl;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.enums.UnexpectedPluginExceptionSeverity;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.interfaces.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_pip_api.layer.user.device_user.interfaces.DeviceUserManager;

import java.util.List;

/**
 * Created by franklin on 30/03/16.
 */
public class ChatIdentityPluginRoot extends AbstractPlugin implements
        DatabaseManagerForDevelopers {
    public ChatIdentityPluginRoot() {
        super(new PluginVersionReference(new Version()));
    }

    private ChatIdentityManagerImpl chatIdentityManager;
    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_DATABASE_SYSTEM)
    private PluginDatabaseSystem pluginDatabaseSystem;

    @NeededAddonReference(platform = Platforms.OPERATIVE_SYSTEM_API, layer = Layers.SYSTEM, addon = Addons.PLUGIN_FILE_SYSTEM)
    private PluginFileSystem pluginFileSystem;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.USER, addon = Addons.DEVICE_USER)
    private DeviceUserManager deviceUserManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.ERROR_MANAGER)
    private ErrorManager errorManager;

    @NeededAddonReference(platform = Platforms.PLUG_INS_PLATFORM, layer = Layers.PLATFORM_SERVICE, addon = Addons.EVENT_MANAGER)
    private EventManager eventManager;

    @Override
    public FermatManager getManager() {
        return chatIdentityManager;
    }

    @Override
    public void start() throws CantStartPluginException {
        chatIdentityManager = new ChatIdentityManagerImpl(pluginDatabaseSystem, pluginId, errorManager, deviceUserManager, pluginFileSystem);
        try {
            Database database = pluginDatabaseSystem.openDatabase(pluginId, ChatIdentityDatabaseConstants.CHAT_DATABASE_NAME);
            System.out.println("******* Init Chat Identity ******");
            //super.start();
            this.serviceStatus = ServiceStatus.STARTED;
        } catch (CantOpenDatabaseException | DatabaseNotFoundException e) {
            try {
                System.out.println("******* Init Chat Identity ******");
                ChatIdentityDatabaseFactory databaseFactory = new ChatIdentityDatabaseFactory(pluginDatabaseSystem);
                databaseFactory.createDatabase(this.pluginId, ChatIdentityDatabaseConstants.CHAT_DATABASE_NAME);
            } catch (CantCreateDatabaseException cantCreateDatabaseException) {
                errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, cantCreateDatabaseException);
                throw new CantStartPluginException();
            } catch (Exception exception) {
                errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, exception);
                throw new CantStartPluginException("Unhandled Exception.", FermatException.wrapException(exception), null, null);
            }
        }
    }

    @Override
    public void stop() {
        this.serviceStatus = ServiceStatus.STOPPED;
        //super.stop();
    }

    @Override
    public List<DeveloperDatabase> getDatabaseList(DeveloperObjectFactory developerObjectFactory) {
        ChatIdentityDeveloperFactory developerFactory = new ChatIdentityDeveloperFactory(pluginDatabaseSystem, pluginId);
        return developerFactory.getDatabaseList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTable> getDatabaseTableList(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase) {
        ChatIdentityDeveloperFactory developerFactory = new ChatIdentityDeveloperFactory(pluginDatabaseSystem, pluginId);
        return developerFactory.getDatabaseTableList(developerObjectFactory);
    }

    @Override
    public List<DeveloperDatabaseTableRecord> getDatabaseTableContent(DeveloperObjectFactory developerObjectFactory, DeveloperDatabase developerDatabase, DeveloperDatabaseTable developerDatabaseTable) {
        ChatIdentityDeveloperFactory developerFactory = new ChatIdentityDeveloperFactory(pluginDatabaseSystem, pluginId);
        List<DeveloperDatabaseTableRecord> developerDatabaseTableRecordList = null;
        try {
            developerFactory.initializeDatabase();
            developerDatabaseTableRecordList = developerFactory.getDatabaseTableContent(developerObjectFactory, developerDatabaseTable);
        } catch (CantInitializeChatIdentityDatabaseException e) {
            errorManager.reportUnexpectedPluginException(this.getPluginVersionReference(), UnexpectedPluginExceptionSeverity.DISABLES_THIS_PLUGIN, e);
        }
        return developerDatabaseTableRecordList;
    }
}
