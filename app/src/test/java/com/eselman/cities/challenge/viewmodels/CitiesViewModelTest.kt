package com.eselman.cities.challenge.viewmodels

import app.cash.turbine.turbineScope
import com.eselman.cities.challenge.model.CitiesResult
import com.eselman.cities.challenge.repositories.CitiesRepository
import com.eselman.cities.challenge.utils.dummyCities
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CitiesViewModelTest {

    @MockK
    private lateinit var citiesRepository: CitiesRepository

    private lateinit var viewModel: CitiesViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getCities success`() = runTest {
        coEvery { citiesRepository.getCities() } returns flowOf(
            CitiesResult.CitiesResultSuccess(dummyCities)
        )
        viewModel = CitiesViewModel(citiesRepository)
        viewModel.getCities()

        turbineScope {
            val citiesUIState = viewModel.citiesUIState.testIn(backgroundScope)
            citiesUIState.awaitItem()
            assertTrue(viewModel.citiesUIState.value is CitiesUIState.Success)
            val cities = (viewModel.citiesUIState.value as CitiesUIState.Success).cities
            assertTrue(cities.isNotEmpty())
            assertEquals(dummyCities.size, cities.size)
        }
    }


    @Test
    fun `test getCities error`() = runTest {
        coEvery { citiesRepository.getCities() } returns flowOf(
            CitiesResult.CitiesResultError("Error")
        )
        viewModel = CitiesViewModel(citiesRepository)
        viewModel.getCities()
        turbineScope {
            val citiesUIState = viewModel.citiesUIState.testIn(backgroundScope)
            citiesUIState.awaitItem()
            assertTrue(viewModel.citiesUIState.value is CitiesUIState.Error)
        }
    }

    @Test
    fun `test filterCities success with search text`() = runTest {
        val searchText = "Si"
        coEvery { citiesRepository.getCities() } returns flowOf(
            CitiesResult.CitiesResultSuccess(dummyCities)
        )
        coEvery { citiesRepository.filterCities(any(), any()) } returns flowOf(
            dummyCities.filter { it.name.startsWith(searchText, false) }
        )
        viewModel = CitiesViewModel(citiesRepository)
        viewModel.filterCities(searchText, false)

        turbineScope {
            val citiesUIState = viewModel.citiesUIState.testIn(backgroundScope)
            citiesUIState.awaitItem()
            assertTrue(viewModel.citiesUIState.value is CitiesUIState.Success)
            val cities = (viewModel.citiesUIState.value as CitiesUIState.Success).cities
            assertEquals(2, cities.size)
            assertEquals("Sisali", cities[0].name)
            assertEquals("Sidney", cities[1].name)
        }
    }

    @Test
    fun `test filterCities success with search text and only favorites`() = runTest {
        val searchText = "Si"
        coEvery { citiesRepository.getCities() } returns flowOf(
            CitiesResult.CitiesResultSuccess(dummyCities)
        )
        coEvery { citiesRepository.filterCities(any(), any()) } returns flowOf(
            dummyCities.filter { it.isFavorite && it.name.startsWith(searchText, false) }
        )
        viewModel = CitiesViewModel(citiesRepository)
        viewModel.filterCities(searchText, true)

        turbineScope {
            val citiesUIState = viewModel.citiesUIState.testIn(backgroundScope)
            citiesUIState.awaitItem()
            assertTrue(viewModel.citiesUIState.value is CitiesUIState.Success)
            val cities = (viewModel.citiesUIState.value as CitiesUIState.Success).cities
            assertEquals(1, cities.size)
            assertEquals("Sisali", cities[0].name)
        }
    }
}
