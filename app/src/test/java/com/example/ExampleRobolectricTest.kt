package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.database.AppDatabase
import com.example.data.repository.DevotionalRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Pequenos Corações, Grande Fé", appName)
  }

  @Test
  fun `verify devotional repository has more than 50 stories and 30 day plan`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val db = AppDatabase.getDatabase(context)
    val repo = DevotionalRepository(db.appDao())

    assertTrue("Stories count must exceed 50", repo.allStories.size >= 50)
    assertEquals(30, repo.thirtyDayPlan.size)
    assertTrue("Prayers collection populated", repo.prayers.isNotEmpty())
    assertTrue("Jesus teachings populated", repo.jesusTeachings.isNotEmpty())
    assertTrue("Quiz questions populated", repo.quizQuestions.isNotEmpty())
  }
}
