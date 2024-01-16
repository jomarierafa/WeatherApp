package com.jvrcoding.weatherapp.domain.use_case.user

import com.jvrcoding.weatherapp.common.hashPassword
import com.jvrcoding.weatherapp.data.local.InvalidUserException
import com.jvrcoding.weatherapp.data.local.User
import com.jvrcoding.weatherapp.data.repository.FakeDataStoreRepository
import com.jvrcoding.weatherapp.data.repository.FakeUserRepository
import com.google.common.truth.Truth.assertThat
import com.jvrcoding.weatherapp.data.repository.FakeRemoteConfigRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserTest {

    private lateinit var getUser: GetUser
    private lateinit var fakeRepository: FakeUserRepository
    private lateinit var fakeDataStoreRepository: FakeDataStoreRepository
    private lateinit var fakeRemoteConfigRepository: FakeRemoteConfigRepository

    @Before
    fun setUp() {
        fakeRepository = FakeUserRepository()
        fakeDataStoreRepository = FakeDataStoreRepository()
        fakeRemoteConfigRepository = FakeRemoteConfigRepository()

        getUser = GetUser(fakeRepository, fakeDataStoreRepository, fakeRemoteConfigRepository)

        runBlocking {
            fakeRepository.insertUser(
                User(firstname = "Jomarie",
                    lastname = "Rafa",
                    username = "jom",
                    password = "password123".hashPassword()
                )
            )
        }
    }

    @Test
    fun `get user with valid credentials`() = runBlocking {
        val username = "jom"
        val password = "password123"

        val user = User("Jomarie", "Rafa", username, password.hashPassword())
        val result = getUser(username, password)

        assertThat(result).isEqualTo(user)
    }

    @Test
    fun `get user with invalid credentials`(): Unit = runBlocking {
        val username = "john"
        val password = "pass123"

        testInputs(username, password, "Invalid Credential.")
    }

    @Test
    fun `get user with invalid username`() : Unit = runBlocking {
        val username = "john"
        val password = "password123"

        testInputs(username, password, "Invalid Credential.")
    }

    @Test
    fun `get user with invalid password`() : Unit = runBlocking {
        val username = "jom"
        val password = "pass123"

        testInputs(username, password, "Invalid Credential.")
    }

    @Test
    fun `get user with empty username`() : Unit = runBlocking {
        val username = ""
        val password = "pass123"

        testInputs(username, password, "Username can't be empty.")
    }

    @Test
    fun `get user with empty password`() : Unit = runBlocking {
        val username = "username"
        val password = ""

        testInputs(username, password, "Password can't be empty.")
    }

    private suspend fun testInputs(username: String, password: String, exceptionMessage: String) {
        try {
            getUser(username, password)
        } catch (e: InvalidUserException) {
            assertThat(e).hasMessageThat().isEqualTo(exceptionMessage)
        }
    }

}