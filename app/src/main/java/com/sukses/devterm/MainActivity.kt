package com.sukses.devterm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sukses.devterm.navigation.Navigation
import com.sukses.devterm.ui.theme.DevTermTheme
import com.sukses.devterm.view.addterm.AddTermViewModel
import com.sukses.devterm.view.editterm.EditTermViewModel
import com.sukses.devterm.view.home.HomeViewModel
import com.sukses.devterm.view.login.LoginViewModel
import com.sukses.devterm.view.myterm.MyTermViewModel
import com.sukses.devterm.view.termcategory.CategoryViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loginViewModel = viewModel(modelClass = LoginViewModel::class.java)
            val addTermViewModel = viewModel(modelClass = AddTermViewModel::class.java)
            val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
            val myTermViewModel = viewModel(modelClass = MyTermViewModel::class.java)
            val editTermViewModel = viewModel(modelClass = EditTermViewModel::class.java)
            val categoryViewModel = viewModel(modelClass = CategoryViewModel::class.java)
            DevTermTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation(
                        loginViewModel = loginViewModel,
                        addTermViewModel = addTermViewModel,
                        homeViewModel = homeViewModel,
                        myTermViewModel = myTermViewModel,
                        editTermViewModel = editTermViewModel,
                        categoryViewModel = categoryViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DevTermTheme {
        Greeting("Android")
    }
}