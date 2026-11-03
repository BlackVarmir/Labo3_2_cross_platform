package ua.edu.chnu.labo3.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun HomePage(
    onRemindersButtonClick: () -> Unit,
    onNetworkButtonClick: () -> Unit,
    onGetTextButtonClick: () -> Unit,
    onPostTextButtonClick: () -> Unit,
    onPutTextButtonClick: () -> Unit,
    onDeleteTextButtonClick: () -> Unit,
    onAboutButtonClick: () -> Unit,
) {
    Column {
        Toolbar(onAboutButtonClick = onAboutButtonClick)
        ContentView(
            onRemindersButtonClick = onRemindersButtonClick,
            onNetworkButtonClick = onNetworkButtonClick,
            onGetTextButtonClick = onGetTextButtonClick,
            onPostTextButtonClick = onPostTextButtonClick,
            onPutTextButtonClick = onPutTextButtonClick,
            onDeleteTextButtonClick = onDeleteTextButtonClick,
            onAboutButtonClick = onAboutButtonClick,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Toolbar(
    onAboutButtonClick: () -> Unit,
) {
    TopAppBar(
        title = { Text(text = "labo3 Cross Platform") },
        actions = {
            IconButton(onClick = onAboutButtonClick) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "About Device",
                )
            }
        }
    )
}

@Composable
private fun ContentView(
    onRemindersButtonClick: () -> Unit,
    onNetworkButtonClick: () -> Unit,
    onGetTextButtonClick: () -> Unit,
    onPostTextButtonClick: () -> Unit,
    onPutTextButtonClick: () -> Unit,
    onDeleteTextButtonClick: () -> Unit,
    onAboutButtonClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = "Manage your reminders, test the network API, or view platform-specific system information.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp, bottom = 24.dp),
        )
        Button(
            onClick = onRemindersButtonClick,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Open reminders")
        }
        Button(
            onClick = onNetworkButtonClick,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
            Text("API networking")
        }
        Button(
            onClick = onGetTextButtonClick,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
            Text("GET as text")
        }
        Button(
            onClick = onPostTextButtonClick,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
            Text("POST as text")
        }
        Button(
            onClick = onPutTextButtonClick,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
            Text("PUT as text")
        }
        Button(
            onClick = onDeleteTextButtonClick,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
            Text("DELETE as text")
        }
        OutlinedButton(
            onClick = onAboutButtonClick,
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp),
        ) {
            Text("About this device")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    HomePage(
        onRemindersButtonClick = {},
        onNetworkButtonClick = {},
        onGetTextButtonClick = {},
        onPostTextButtonClick = {},
        onPutTextButtonClick = {},
        onDeleteTextButtonClick = {},
        onAboutButtonClick = {},
    )
}
