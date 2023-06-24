package com.sukses.devterm.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sukses.devterm.view.addterm.AddTermScreen
import com.sukses.devterm.view.addterm.AddTermViewModel
import com.sukses.devterm.view.editterm.EditTermScreen
import com.sukses.devterm.view.editterm.EditTermViewModel
import com.sukses.devterm.view.home.HomeScreen
import com.sukses.devterm.view.home.HomeViewModel
import com.sukses.devterm.view.login.LoginViewModel
import com.sukses.devterm.view.login.LoginScreen
import com.sukses.devterm.view.login.SignUpScreen
import com.sukses.devterm.view.myterm.MyTermScreen
import com.sukses.devterm.view.myterm.MyTermViewModel
import com.sukses.devterm.view.searchresult.DetailScreen
import com.sukses.devterm.view.searchresult.SearchResult
import com.sukses.devterm.view.termcategory.CategoryListScreen
import com.sukses.devterm.view.termcategory.CategoryScreen
import com.sukses.devterm.view.termcategory.CategoryViewModel

enum class LoginRoutes {
    Signup,
    SignIn
}

enum class HomeRoutes {
    Home,
    AddTerm,
    MyTerm,
    EditTerm,
    SearchResult,
    Detail,
    CategoryList,
    Category
}

enum class NestedRoutes {
    Main,
    Login
}

@Composable
fun Navigation(
    navController: NavHostController = rememberNavController(),
    loginViewModel: LoginViewModel,
    addTermViewModel: AddTermViewModel,
    homeViewModel: HomeViewModel,
    myTermViewModel: MyTermViewModel,
    editTermViewModel: EditTermViewModel,
    categoryViewModel: CategoryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = NestedRoutes.Login.name
    ) {
        authGraph(navController, loginViewModel)
        homeGraph(
            navController = navController,
            addTermViewModel,
            homeViewModel,
            myTermViewModel,
            editTermViewModel,
            categoryViewModel
        )
    }
}

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    loginViewModel: LoginViewModel,
) {
    navigation(
        startDestination = LoginRoutes.SignIn.name,
        route = NestedRoutes.Login.name
    ) {
        composable(route = LoginRoutes.SignIn.name) {
            LoginScreen(onNavToHomePage = {
                navController.navigate(NestedRoutes.Main.name) {
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
                navController.navigate(NestedRoutes.Main.name) {
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
    }
}

fun NavGraphBuilder.homeGraph(
    navController: NavHostController,
    addTermViewModel: AddTermViewModel,
    homeViewModel: HomeViewModel,
    myTermViewModel: MyTermViewModel,
    editTermViewModel: EditTermViewModel,
    categoryViewModel: CategoryViewModel
){
    navigation(
        startDestination = HomeRoutes.Home.name,
        route = NestedRoutes.Main.name,
    ){
        composable(HomeRoutes.Home.name){
            HomeScreen(
                onItemClick = { termID->
                    navController.navigate(HomeRoutes.Detail.name + "?id=$termID") {
                        launchSingleTop = true
                    }
                },
                navController = navController,
                homeViewModel = homeViewModel
            ) {
                navController.navigate(NestedRoutes.Login.name) {
                    launchSingleTop = true
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }
        }

        composable(
            route = HomeRoutes.AddTerm.name
        ){
            AddTermScreen(
                addTermViewModel = addTermViewModel
            )
        }
        composable(route = HomeRoutes.MyTerm.name) {
            MyTermScreen(
                onTermClick = {termId ->
                    navController.navigate(HomeRoutes.EditTerm.name + "?id=$termId") {
                        launchSingleTop = true
                    }
                }, myTermViewModel = myTermViewModel
            )
        }

        composable(
            route = HomeRoutes.EditTerm.name + "?id={id}",
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
                defaultValue = ""
            })
        ){ entry ->

            EditTermScreen(
                editTermViewModel = editTermViewModel,
                termId = entry.arguments?.getString("id") as String,
            )
        }
        
        composable(route = HomeRoutes.SearchResult.name) {
            SearchResult(onItemClick = { termID->
                navController.navigate(HomeRoutes.Detail.name + "?id=$termID") {
                    launchSingleTop = true
                }
            }, homeViewModel = homeViewModel)
        }

        composable(
            route = HomeRoutes.Detail.name + "?id={id}",
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
                defaultValue = ""
            })
        ){ entry ->
            DetailScreen(
                homeViewModel = homeViewModel,
                termID = entry.arguments?.getString("id") as String,
            )
        }

        composable(route = HomeRoutes.CategoryList.name) {
            CategoryListScreen(
                onCategoryClick = { catName->
                    navController.navigate(HomeRoutes.Category.name + "?catName=$catName")
                },
                categoryViewModel = categoryViewModel
            )
        }

        composable(
            route = HomeRoutes.Category.name + "?catName={catName}",
            arguments = listOf(navArgument("catName"){
                type = NavType.StringType
                defaultValue = "Mobile Development"
            })
        ) { entry ->
            CategoryScreen(
                onItemClick = { termID->
                    navController.navigate(HomeRoutes.Detail.name + "?id=$termID") {
                        launchSingleTop = true
                    }
                },
                categoryViewModel = categoryViewModel,
                CategoryName = entry.arguments?.getString("catName") as String
            )
        }
    }
}