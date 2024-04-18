package com.utsman.appyxrouter

import App
import RootNode
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import com.bumble.appyx.navigation.integration.NodeActivity
import com.bumble.appyx.navigation.integration.NodeHost
import com.bumble.appyx.navigation.platform.AndroidLifecycle

class MainActivity : NodeActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val lifecycle = LocalLifecycleOwner.current
            NodeHost(
                lifecycle = AndroidLifecycle(lifecycle.lifecycle),
                integrationPoint = appyxV2IntegrationPoint
            ) {
                RootNode(it)
            }
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}