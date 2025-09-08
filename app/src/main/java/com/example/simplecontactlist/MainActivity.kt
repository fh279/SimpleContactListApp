package com.example.simplecontactlist

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplecontactlist.ui.theme.SimpleContactListTheme
import com.example.simplecontactlist.ui.view.AddContact
import com.example.simplecontactlist.ui.view.ContactList
import com.example.simplecontactlist.viewModel.MyViewModel
import com.example.simplecontactlist.viewModel.MyViewModelFactory

class MainActivity : ComponentActivity() {
    private val vmFactory = MyViewModelFactory(StringResourcesProvider(this))
    private lateinit var viewModel: MyViewModel

    @OptIn(ExperimentalLayoutApi::class)
    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =  ViewModelProvider(
            owner = this,
            factory = vmFactory
        ).get(MyViewModel::class.java)
        setContent {
            SimpleContactListTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Composable
    fun MainScreen(modifier: Modifier) {
        val state: State<ListState> = viewModel.state.collectAsStateWithLifecycle()
        val listState: ListState = state.value

        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(all = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ContactList(
                modifier = modifier,
                state = listState,
                viewModel = viewModel
            )
            Spacer(Modifier.height(20.dp))
            AddContact(
                modifier = modifier,
                state = listState,
                viewModel = viewModel
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Preview(showBackground = true)
    @Composable
    fun ContactList() {
        SimpleContactListTheme {
            MainScreen(Modifier)
        }
    }
}
