package io.horizontalsystems.bankwallet.modules.fulltransactioninfo.dataprovider

import com.google.gson.JsonObject
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.horizontalsystems.bankwallet.core.INetworkManager
import io.horizontalsystems.bankwallet.core.ITransactionDataProviderManager
import io.horizontalsystems.bankwallet.modules.RxBaseTest
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class DataProviderSettingsInteractorTest {

    private val dataProviderManager = mock(ITransactionDataProviderManager::class.java)
    private val networkManager = mock(INetworkManager::class.java)
    private val delegate = mock(DataProviderSettingsModule.InteractorDelegate::class.java)

    private lateinit var interactor: DataProviderSettingsInteractor

    @Before
    fun setup() {
        RxBaseTest.setup()

        interactor = DataProviderSettingsInteractor(dataProviderManager, networkManager)
        interactor.delegate = delegate
    }

    @Test
    fun pingProvider_success() {
        whenever(networkManager.ping(any(), any()))
                .thenReturn(Flowable.just(JsonObject()))

        interactor.pingProvider("abc.com", "http://abc.com")

        verify(delegate).onPingSuccess("abc.com")
    }

    @Test
    fun pingProvider_failure() {
        whenever(networkManager.ping(any(), any()))
                .thenReturn(Flowable.error(Error("")))

        interactor.pingProvider("abc.com", "http://abc.com")

        verify(delegate).onPingFailure("abc.com")
    }

    @Test
    fun providers() {
        interactor.providers("BTC")

        verify(dataProviderManager).providers("BTC")
    }

    @Test
    fun baseProvider() {
        interactor.baseProvider("BTC")

        verify(dataProviderManager).baseProvider("BTC")
    }

    @Test
    fun setBaseProvider() {
        interactor.setBaseProvider("abc.com", "BTC")

        verify(dataProviderManager).setBaseProvider("abc.com", "BTC")
        verify(delegate).onSetDataProvider()
    }

}
