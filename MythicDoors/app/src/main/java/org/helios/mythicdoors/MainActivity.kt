package org.helios.mythicdoors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.helios.mythicdoors.navigation.AppNavigation
import org.helios.mythicdoors.ui.theme.MythicDoorsTheme
import org.helios.mythicdoors.utils.Connection
import org.helios.mythicdoors.viewmodel.*

class MainActivity : ComponentActivity() {
    private val dbHelper: Connection = Connection(this)
    private val viewModelsMap: Map<String, Any> = hashMapOf(
            "MainActivityViewModel" to MainActivityViewModel(dbHelper),
            "OverviewScreenViewModel" to OverviewScreenViewModel(),
            "ActionResultScreenViewModel" to ActionResultScreenViewModel(),
            "GameActionScreenViewModel" to GameActionScreenViewModel(),
            "GameOptsScreenViewModel" to GameOptsScreenViewModel(),
            "LoginScreenViewModel" to LoginScreenViewModel(),
            "RegisterScreenViewModel" to RegisterScreenViewModel(),
            "ScoresScreenViewModel" to ScoresScreenViewModel()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MythicDoorsTheme {
                Scaffold(
                    topBar = {
                        // TODO: Add top bar
                    },
                    bottomBar = {
                        // TODO: Add bottom bar
                    }
                ) { innerPadding ->
//                    SurfaceWithbackground(innerPadding) {
//                        AppNavigation()
//                    }
                    Surface(
                        modifier = Modifier.fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        AppNavigation()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}

//@Composable
//fun SurfaceWithbackground(
//    innerPadding: PaddingValues,
//    modifier: Modifier = Modifier,
//    contentColor: Color = MaterialTheme.colorScheme.onBackground,
//    content: @Composable (PaddingValues) -> Unit
//) {
//    Surface(
//        modifier = Modifier
//            .padding(innerPadding)
//            .then(modifier),
//        color = MaterialTheme.colorScheme.background,
//        contentColor = contentColor,
//    ) {
//        content(PaddingValues(10.dp))
//    }
//}

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