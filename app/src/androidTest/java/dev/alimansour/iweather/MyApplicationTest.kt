package dev.alimansour.iweather

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MyApplicationTest {
    private lateinit var app:Application

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun useAppContextToGetThePackageName_returnTheRightOne() {
        assertThat(app.packageName).isEqualTo("dev.alimansour.iweather")
    }
}
