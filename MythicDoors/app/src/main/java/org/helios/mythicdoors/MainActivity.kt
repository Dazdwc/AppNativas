package org.helios.mythicdoors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.viewmodel.MainActivityViewModel

class MainActivity : ComponentActivity() {
    private val dbHelper: Connection = Connection(this)
    private val dataController: DataController = DataController.getInstance(dbHelper)
    private val mainViewModel: MainActivityViewModel = MainActivityViewModel(
        dbHelper,
        dataController)
    private val viewModels: List<Any> = listOf(
        mainViewModel
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MythicDoorsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppNavigation()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    override fun onPause() {
        super.onPause()
        dbHelper.close()
    }

    override fun onResume() {
        super.onResume()
        // dbHelper.open()
    }

    override fun onStop() {
        super.onStop()
        dbHelper.close()
    }

    override fun onStart() {
        super.onStart()
        // dbHelper.open()
    }

    override fun onRestart() {
        super.onRestart()
        // dbHelper.open()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(showBackground = true, showSystemUi = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun GreetingPreview() {
    MythicDoorsTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            AppNavigation()
        }
    }
}