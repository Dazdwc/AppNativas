package org.helios.mythicdoors

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
import androidx.lifecycle.ViewModel
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.viewmodel.*

class MainActivity : ComponentActivity() {
    private val dbHelper: Connection = Connection(this)
    private val viewModels = mutableListOf<Any>().apply {
        add(MainActivityViewModel(dbHelper))
        add(OverviewScreenViewModel())
        add(ActionResultScreenViewModel())
        add(GameActionScreenViewModel())
        add(GameOptsScreenViewModel())
        add(LoginScreenViewModel())
        add(RegisterScreenViewModel())
        add(ScoresScreenViewModel())
    }


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