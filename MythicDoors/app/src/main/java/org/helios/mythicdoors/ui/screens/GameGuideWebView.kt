package org.helios.mythicdoors.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import org.helios.mythicdoors.MainActivity
import org.helios.mythicdoors.utils.AppConstants.ScreensViewModels.GAME_GUIDE_SCREEN_VIEWMODEL
import org.helios.mythicdoors.utils.AppConstants.WEB_GUIDE_URl
import org.helios.mythicdoors.viewmodel.GameGuideWebViewViewModel
import org.helios.mythicdoors.R
import org.helios.mythicdoors.utils.AppConstants.ScreenConstants

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun GameGuideWebView(navController: NavController) {
    val controller: GameGuideWebViewViewModel = (MainActivity.viewModelsMap[GAME_GUIDE_SCREEN_VIEWMODEL] as GameGuideWebViewViewModel).apply { setNavController(navController) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val url = WEB_GUIDE_URl

    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Button(onClick = { controller.navigateToOptionsScreen(
                scope = scope,
                snackbarHostState = snackbarHostState)
            },
                modifier = Modifier
                    .padding(top = ScreenConstants.AVERAGE_PADDING.dp, start = ScreenConstants.AVERAGE_PADDING.dp)
                    .align(Alignment.Start)
            ) {
                Text(
                    text = stringResource(id = R.string.back),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            WebViewComponent(url)
        }

    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewComponent(url: String) {
    lateinit var webView: WebView
    AndroidView({ context ->
        WebView(context).apply {
            webChromeClient = android.webkit.WebChromeClient()
            webViewClient = android.webkit.WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(url)

            webView = this
        }
    })

    BackHandler {
        if (webView.canGoBack()) webView.goBack()
    }
}
