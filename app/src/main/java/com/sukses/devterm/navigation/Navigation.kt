package com.sukses.devterm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sukses.devterm.view.addterm.AddTermScreen
import com.sukses.devterm.view.addterm.AddTermViewModel
import com.sukses.devterm.view.home.HomeScreen
import com.sukses.devterm.view.home.HomeViewModel
import com.sukses.devterm.view.login.LoginViewModel
import com.sukses.devterm.view.login.LoginScreen
import com.sukses.devterm.view.login.SignUpScreen

enum class LoginRoutes {
    Signup,
    SignIn
}

enum class HomeRoutes {
    Home,
    AddTerm
}


@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    homeViewModel: HomeViewModel,
    addTermViewModel: AddTermViewModel
) {
    NavHost(
        navController = navController,
        startDestination = LoginRoutes.SignIn.name
    ) {
        composable(route = LoginRoutes.SignIn.name) {
            LoginScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name) {
                    launchSingleTop = true
                    popUpTo(route = LoginRoutes.SignIn.name) {
                        inclusive = true
                    }
                }
            },
                loginViewModel = loginViewModel

            ) {
                navController.navigate(LoginRoutes.Signup.name) {
                    launchSingleTop = true
                    popUpTo(LoginRoutes.SignIn.name) {
                        inclusive = true
                    }
                }
            }
        }

        composable(route = LoginRoutes.Signup.name) {
            SignUpScreen(onNavToHomePage = {
                navController.navigate(HomeRoutes.Home.name) {
                    popUpTo(LoginRoutes.Signup.name) {
                        inclusive = true
                    }
                }
            },
                loginViewModel = loginViewModel
            ) {
                navController.navigate(LoginRoutes.SignIn.name)
            }
        }

        composable(route = HomeRoutes.Home.name) {
            HomeScreen(navController, homeViewModel)
        }
        
        composable(route = HomeRoutes.AddTerm.name) {
            AddTermScreen(addTermViewModel = addTermViewModel) {
                navController.navigate(HomeRoutes.AddTerm.name)
            }
        }
    }
}