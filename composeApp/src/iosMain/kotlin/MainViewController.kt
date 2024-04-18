import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.bumble.appyx.navigation.integration.IosNodeHost
import com.bumble.appyx.navigation.integration.MainIntegrationPoint
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

val backEvents: Channel<Unit> = Channel()
val integrationPoint = MainIntegrationPoint()

fun MainViewController() = ComposeUIViewController {
    IosNodeHost(
        onBackPressedEvents = backEvents.receiveAsFlow(),
        modifier = Modifier.fillMaxSize(),
        integrationPoint = integrationPoint
    ) {
        RootNode(it)
    }
}