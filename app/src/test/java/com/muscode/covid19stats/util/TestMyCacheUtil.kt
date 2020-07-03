package com.muscode.covid19stats.util

import com.google.common.truth.Truth.assertThat
import com.muscode.covid19stats.model.CovidSummary
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TestMyCacheUtil {


    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
        mainThreadSurrogate.close()
    }

    @Test
    fun testIsLoadingFromCache() = runBlocking {
        val mockNetworkChecker: NetworkChecker = mock()
        val mockList: List<CovidSummary> = mock()
        whenever(mockNetworkChecker.isOnline()).thenReturn(false)
        whenever(mockList.isNullOrEmpty()).thenReturn(false)

        val myCacheUtil = MyCacheUtil(mockNetworkChecker)
        myCacheUtil.data = mockList
        val isCached: Boolean? = runBlocking { myCacheUtil.isLoadingFromCache() }
        assertThat(isCached).isTrue()
    }



    @Test
    fun testIsNotLoadingFromCache() = runBlocking {
        val mockNetworkChecker: NetworkChecker = mock()
        whenever(mockNetworkChecker.isOnline()).thenReturn(true)
        val myCacheUtil = MyCacheUtil(mockNetworkChecker)
        val isCached: Boolean? = runBlocking { myCacheUtil.isLoadingFromCache() }
        assertThat(isCached).isFalse()
    }
}