package br.com.githubrepos.homescreen

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import br.com.githubrepos.R
import br.com.githubrepos.library.retrofit.RetrofitFactory
import br.com.githubrepos.library.retrofit.endpoint.SearchRepositoriesEndpoint
import br.com.githubrepos.mocks.REPOSITORIES_RESPONSE_200_OK
import br.com.githubrepos.okhttpIdle.OkHttpIdlingResourceRule
import br.com.githubrepos.screens.home.HomeActivity
import br.com.githubrepos.screens.home.SearchAdapter
import kotlinx.serialization.UnstableDefault
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class OnSearchLanguageTest {

    @get:Rule
    var okHttpRule = OkHttpIdlingResourceRule()

    private val mockWebServer = MockWebServer()
    private lateinit var searchRepositoriesApi: SearchRepositoriesEndpoint

    @UnstableDefault
    @Before
    @Throws
    fun setup() {
        mockWebServer.start()

        searchRepositoriesApi = RetrofitFactory().create(
            SearchRepositoriesEndpoint::class.java,
            mockWebServer.url("/").toString()
        )

        ActivityScenario.launch(HomeActivity::class.java)
    }

    @Test
    fun testFetchTypedLanguageGitHubRepositories() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody(REPOSITORIES_RESPONSE_200_OK)
            .setBodyDelay(1, TimeUnit.SECONDS)

        mockWebServer.enqueue(response)

        onView(withId(R.id.searchView)).perform(ViewActions.typeText("Java"))

        onView(withId(R.id.recyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<SearchAdapter.ViewHolder>(
                2,
                click()
            )
        )
    }

    @After
    @Throws
    fun teardown() {
        mockWebServer.shutdown()
    }
}
