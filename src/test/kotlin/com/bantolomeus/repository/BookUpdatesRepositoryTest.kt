package com.bantolomeus.repository

import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringRunner
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
class BookUpdatesRepositoryTest {

    @Test
    fun testRepoFileGetsCreatedIfMissing() {
        assertFalse(File("testBookUpdatesRepo.json").exists(), "repo file should not exist, but it does")
        BookUpdatesRepository("testBookUpdatesRepo.json")
        assertTrue(File("testBookUpdatesRepo.json").exists(), "repo file should have been created, but it wasn't")
    }

    @Test
    fun testExistingRepoFileDoesNotGetOverwritten() {
        File("testBookUpdatesRepo.json").writeText("batman ❤ robin")
        BookUpdatesRepository("testBookUpdatesRepo.json")
        assertEquals(File("testBookUpdatesRepo.json").readText(), "batman ❤ robin")
    }

    @After
    fun removeRepoFile() {
        File("testBookUpdatesRepo.json").deleteRecursively()
    }

}
