package com.example.weatherapp.domain.use_case.user

import com.example.weatherapp.common.hashPassword
import com.example.weatherapp.data.local.InvalidUserException
import com.example.weatherapp.data.local.User
import com.example.weatherapp.data.repository.FakeUserRepository
import com.google.common.truth.Truth
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertUserTest {

    private lateinit var insertUser: InsertUser
    private lateinit var fakeRepository: FakeUserRepository

    @Before
    fun setUp() {
        fakeRepository = FakeUserRepository()
        insertUser = InsertUser(fakeRepository)

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
    fun `insert valid user details`() : Unit = runBlocking {
        val user = User(
            firstname= "Jomarie",
            lastname = "Rafa",
            username = "username",
            password = "password",
            confirmPassword = "password",
        )

        val result = insertUser(user)
        Truth.assertThat(result).isEqualTo(Unit)

    }


    @Test
    fun `insert user with existing username`() : Unit = runBlocking {
        val user = User(
            firstname= "Jomarie",
            lastname = "Rafa",
            username = "jom",
            password = "password",
            confirmPassword = "password",
        )

        testInputs(user, "Username already exists.")
    }


    @Test
    fun `insert user with empty firstname`() : Unit = runBlocking {
        val user = User(
            firstname= "",
            lastname = "Rafa",
            username = "jom",
            password = "password",
            confirmPassword = "password",
        )

        testInputs(user, "Firstname can't be empty.")
    }

    @Test
    fun `insert user with empty lastname`() : Unit = runBlocking {
        val user = User(
            firstname= "Jomarie",
            lastname = "",
            username = "jom",
            password = "password",
            confirmPassword = "password",
        )

        testInputs(user, "LastName can't be empty.")
    }

    @Test
    fun `insert user with empty username`() : Unit = runBlocking {
        val user = User(
            firstname= "Jomarie",
            lastname = "Rafa",
            username = "",
            password = "password",
            confirmPassword = "password",
        )

        testInputs(user, "Username can't be empty.")
    }

    @Test
    fun `insert user with empty password`() : Unit = runBlocking {
        val user = User(
            firstname= "Jomarie",
            lastname = "Rafa",
            username = "jom",
            password = "",
            confirmPassword = "password",
        )

        testInputs(user, "Password can't be empty.")
    }

    @Test
    fun `insert user with empty confirm password`() : Unit = runBlocking {
        val user = User(
            firstname= "Jomarie",
            lastname = "Rafa",
            username = "jom",
            password = "password",
            confirmPassword = "",
        )

        testInputs(user, "Confirm Password can't be empty.")
    }

    @Test
    fun `insert user with empty not match password and confirm password`() : Unit = runBlocking {
        val user = User(
            firstname= "Jomarie",
            lastname = "Rafa",
            username = "jom",
            password = "password",
            confirmPassword = "passwword123",
        )

        testInputs(user, "Password not match.")
    }

    private suspend fun testInputs(user: User, exceptionMessage: String) {
        try {
            insertUser(user)
        } catch (e: InvalidUserException) {
            Truth.assertThat(e).hasMessageThat().isEqualTo(exceptionMessage)
        }
    }

}