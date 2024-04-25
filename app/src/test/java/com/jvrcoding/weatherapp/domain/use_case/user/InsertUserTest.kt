package com.jvrcoding.weatherapp.domain.use_case.user

import com.jvrcoding.weatherapp.common.hashPassword
import com.jvrcoding.weatherapp.data.local.InvalidUserException
import com.jvrcoding.weatherapp.data.local.UserEntity
import com.jvrcoding.weatherapp.data.repository.FakeUserRepository
import com.google.common.truth.Truth
import com.jvrcoding.weatherapp.domain.model.User
import com.jvrcoding.weatherapp.domain.util.DataError
import com.jvrcoding.weatherapp.domain.util.Error
import com.jvrcoding.weatherapp.domain.util.UserDataValidator
import com.jvrcoding.weatherapp.domain.util.ifError
import com.jvrcoding.weatherapp.domain.util.ifSuccess
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class InsertUserTest {

    private lateinit var insertUser: InsertUser
    private lateinit var fakeRepository: FakeUserRepository
    private lateinit var userDataValidator: UserDataValidator

    @Before
    fun setUp() {
        fakeRepository = FakeUserRepository()
        userDataValidator = UserDataValidator()
        insertUser = InsertUser(fakeRepository, userDataValidator)

        runBlocking {
            fakeRepository.insertUser(
                User(firstname = "Jomarie",
                    lastname = "Rafa",
                    username = "jom",
                    password = "password123".hashPassword(),
                    confirmPassword = "password123".hashPassword()
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

        insertUser(user).ifSuccess { result ->
            Truth.assertThat(result).isEqualTo(Unit)
        }

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

        testInputs(user, DataError.Local.USERNAME_ALREADY_EXIST)
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

        testInputs(user, UserDataValidator.RegistrationError.FIRSTNAME_IS_EMPTY)
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

        testInputs(user, UserDataValidator.RegistrationError.LASTNAME_IS_EMPTY)
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

        testInputs(user, UserDataValidator.RegistrationError.USERNAME_IS_EMPTY)
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

        testInputs(user, UserDataValidator.RegistrationError.PASSWORD_IS_EMPTY)
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

        testInputs(user, UserDataValidator.RegistrationError.CONFIRM_PASSWORD_IS_EMPTY)
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

        testInputs(user, UserDataValidator.RegistrationError.PASSWORD_NOT_MATCH)
    }

    private suspend fun testInputs(user: User, expectedError: Error) {
        insertUser(user).ifError { error ->
            Truth.assertThat(error).isEqualTo(expectedError)
        }
    }

}