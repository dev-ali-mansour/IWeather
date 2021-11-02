package dev.alimansour.iweather.data.util

import com.google.common.truth.Truth.assertThat
import dev.alimansour.iweather.TestUtil.TEST_ASWAN_HISTORICAL_LIST
import dev.alimansour.iweather.data.local.entity.toModel
import dev.alimansour.iweather.toHistoricalResponse
import dev.alimansour.iweather.util.Resource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response

/**
 * WeatherApp Android Application developed by: Ali Mansour
 * ----------------- WeatherApp IS FREE SOFTWARE -------------------
 * https://www.alimansour.dev   |   mailto:dev.ali.mansour@gmail.com
 */
class HelperMethodsKtTest {
    private lateinit var response: Response<*>

    @Before
    fun setUp() {
        response = mock(Response::class.java)
    }

    @Test
    fun `toResource() When Response is un successful Then return Error Resource`() = runBlocking {
        //GIVEN
        val aswanHistorical = TEST_ASWAN_HISTORICAL_LIST.map { it.toModel() }
        `when`(response.isSuccessful).thenReturn(true)
        `when`(response.body()).thenReturn(aswanHistorical.toHistoricalResponse())

        //WHEN
        val resource = response.toResource()

        //THEN
        assertThat(resource).isInstanceOf(Resource.Success::class.java)
        assertThat(resource.data).isEqualTo(aswanHistorical.toHistoricalResponse())
    }

    @Test
    fun `toResource() When Response is successful Then return Success Resource`() = runBlocking {
        //GIVEN
        `when`(response.isSuccessful).thenReturn(false)
        `when`(response.message()).thenReturn("Page Not Found!")

        //WHEN
        val resource = response.toResource()

        //THEN
        assertThat(resource).isInstanceOf(Resource.Error::class.java)
        assertThat(resource.data).isNull()
        assertThat(resource.message).isEqualTo("Page Not Found!")
    }
}