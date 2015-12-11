package unit.com.bitdubai.fermat_cbp_plugin.layer.network_service.transaction_transmission.developer.bitdubai.version_1.structure.BusinessTransactionMetadataRecord;

import com.bitdubai.fermat_cbp_plugin.layer.network_service.transaction_transmission.developer.bitdubai.version_1.structure.BusinessTransactionMetadataRecord;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by Gabriel Araujo (gabe_512@hotmail.com) on 08/12/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class GetContractHashTest {

    @Test
    public void getContractHash() throws Exception{

        BusinessTransactionMetadataRecord businessTransactionMetadataRecord = Mockito.mock(BusinessTransactionMetadataRecord.class);
        when(businessTransactionMetadataRecord.getContractHash()).thenReturn("1");
        assertThat(businessTransactionMetadataRecord.getContractHash()).isNotNull();
    }

}
