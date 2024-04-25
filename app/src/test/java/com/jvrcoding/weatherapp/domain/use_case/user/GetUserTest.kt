package com.jvrcoding.weatherapp.domain.use_case.user

import com.google.common.truth.Truth
import com.jvrcoding.weatherapp.common.hashPassword
import com.jvrcoding.weatherapp.data.local.InvalidUserException
import com.jvrcoding.weatherapp.data.local.UserEntity
import com.jvrcoding.weatherapp.data.repository.FakeDataStoreRepository
import com.jvrcoding.weatherapp.data.repository.FakeUserRepository
import com.google.common.truth.Truth.assertThat
import com.jvrcoding.weatherapp.data.repository.FakeRemoteConfigRepository
import com.jvrcoding.weatherapp.domain.model.User
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.Result
import com.jvrcoding.weatherapp.domain.util.UserDataValidator
import com.jvrcoding.weatherapp.domain.util.ifError
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetUserTest {

    private lateinit var getUser: GetUser
    private lateinit var fakeRepository: FakeUserRepository
    private lateinit var fakeDataStoreRepository: FakeDataStoreRepository
    private lateinit var fakeRemoteConfigRepository: FakeRemoteConfigRepository
    private lateinit var userDataValidator: UserDataValidator

    @Before
    fun setUp() {
        fakeRepository = FakeUserRepository()
        fakeDataStoreRepository = FakeDataStoreRepository()
        userDataValidator = UserDataValidator()
        fakeRemoteConfigRepository = FakeRemoteConfigRepository()

        getUser = GetUser(
            fakeRepository,
            fakeDataStoreRepository,
            fakeRemoteConfigRepository,
            userDataValidator
        )

        runBlocking {
            fakeRepository.insertUser(
                User(
                    firstname = "Jomarie",
                    lastname = "Rafa",
                    username = "jom",
                    password = "password123".hashPassword(),
                    confirmPassword = "password123".hashPassword()
                )
            )
        }
    }

    @Test
    fun `get user with valid credentials`() = runBlocking {
        val username = "jom"
        val password = "password123"

        val user = User(
            "Jomarie",
            "Rafa",
            username,
            password.hashPassword(),
            password.hashPassword()
        )
        val result = getUser(username, password)

        assertThat(result).isEqualTo(Result.Success(user))
    }

    @Test
    fun `get user with invalid credentials`(): Unit = runBlocking {
        val username = "john"
        val password = "pass123"

        testInputs(username, password, DataError.Local.NO_DATA_FOUND)
    }

    @Test
    fun `get user with invalid username`(): Unit = runBlocking {
        val username = "john"
        val password = "password123"

        testInputs(username, password, DataError.Local.NO_DATA_FOUND)
    }

    @Test
    fun `get user with invalid password`(): Unit = runBlocking {
        val username = "jom"
        val password = "pass123"

        testInputs(username, password, DataError.Local.NO_DATA_FOUND)
    }

    @Test
    fun `get user with empty username`(): Unit = runBlocking {
        val username = ""
        val password = "pass123"

        testInputs(username, password, UserDataValidator.CredentialError.USERNAME_IS_EMPTY)
    }

    @Test
    fun `get user with empty password`(): Unit = runBlocking {
        val username = "username"
        val password = ""

        testInputs(username, password, UserDataValidator.CredentialError.PASSWORD_IS_EMPTY)
    }

    private suspend fun testInputs(username: String, password: String, expectedError: Error) {
        getUser(username, password).ifError { error ->
            assertThat(error).isEqualTo(expectedError)
        }
    }

}