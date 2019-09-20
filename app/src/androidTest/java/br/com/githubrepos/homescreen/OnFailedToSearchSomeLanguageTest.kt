package br.com.githubrepos.homescreen

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.runner.AndroidJUnit4
import br.com.githubrepos.R
import br.com.githubrepos.library.retrofit.RetrofitFactory
import br.com.githubrepos.library.retrofit.endpoint.SearchRepositoriesEndpoint
import br.com.githubrepos.mocks.REPOSITORIES_NOT_FOUND_422
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
class OnFailedToSearchSomeLanguageTest {

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
    fun testFailedToFetchAGitHubRepositories() {
        val response = MockResponse()
            .setResponseCode(422)
            .setBody(REPOSITORIES_NOT_FOUND_422)
            .setBodyDelay(1, TimeUnit.SECONDS)

        mockWebServer.enqueue(response)

        onView(withId(R.id.searchView)).perform(typeText("qwerty"))
        onView(withId(R.id.recyclerView)).check { view, _ -> !view.isVisible }
        onView(withId(R.id.searchNotFound)).check { view, _ -> view.isVisible }
    }

    @Test
    fun testDefaultErrorWhileFetchingAGitHubRepositories() {
        val response = MockResponse()
            .setResponseCode(HttpURLConnection.HTTP_BAD_GATEWAY)
            .setBody(REPOSITORIES_NOT_FOUND_422)
            .setBodyDelay(1, TimeUnit.SECONDS)

        mockWebServer.enqueue(response)

        onView(withId(R.id.searchView)).perform(typeText("qwerty"))
        onView(withId(R.id.recyclerView)).check { view, _ -> !view.isVisible }
        onView(withId(R.id.defaultError)).check { view, _ -> view.isVisible }
    }

    @After
    @Throws
    fun teardown() {
        mockWebServer.shutdown()
    }
}
