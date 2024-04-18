import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

import com.bumble.appyx.components.backstack.BackStack
import com.bumble.appyx.components.backstack.BackStackModel
import com.bumble.appyx.components.backstack.operation.pop
import com.bumble.appyx.components.backstack.operation.push
import com.bumble.appyx.components.backstack.operation.replace
import com.bumble.appyx.components.backstack.ui.fader.BackStackFader
import com.bumble.appyx.components.backstack.ui.parallax.BackStackParallax
import com.bumble.appyx.interactions.core.model.transition.Operation
import com.bumble.appyx.interactions.core.ui.gesture.GestureFactory
import com.bumble.appyx.navigation.composable.AppyxNavigationContainer
import com.bumble.appyx.navigation.modality.NodeContext
import com.bumble.appyx.navigation.node.LeafNode
import com.bumble.appyx.navigation.node.Node
import com.bumble.appyx.navigation.node.node
import com.bumble.appyx.utils.multiplatform.Parcelable
import com.bumble.appyx.utils.multiplatform.Parcelize

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        Text("anu")
    }
}


@Composable
fun MainContent(
    goToMain: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(color = Color.Green),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                goToMain.invoke()
            }
        ) {
            Text("to detail")
        }
    }
}

@Composable
fun DetailContent(
    toUser: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                toUser.invoke()
            }
        ) {
            Text("ini detail",
                modifier = Modifier.padding(12.dp))
        }
    }
}

@Composable
fun UserContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Text("ini user",
            modifier = Modifier.padding(12.dp))
    }
}


class MainNode(
    nodeContext: NodeContext,
    private val toDetail: () -> Unit
) : LeafNode(nodeContext) {

    @Composable
    override fun Content(modifier: Modifier) {
        MainContent(toDetail)
    }
}

class DetailNode(
    nodeContext: NodeContext,
    private val toUser: () -> Unit
) : LeafNode(nodeContext) {

    @Composable
    override fun Content(modifier: Modifier) {
        DetailContent(toUser)
    }
}

class UserNode(
    nodeContext: NodeContext
) : LeafNode(nodeContext) {

    @Composable
    override fun Content(modifier: Modifier) {
        UserContent()
    }
}

class RootNode(
    nodeContext: NodeContext,
    private val backStack: BackStack<Router> = BackStack(
        model = BackStackModel(
            initialTarget = Router.Main,
            savedStateMap = nodeContext.savedStateMap,
        ),
        visualisation = {
            if (isAndroid) {
                BackStackFader(it, defaultAnimationSpec = spring())
            } else {
                BackStackParallax(it)
            }
        },
        gestureFactory = {
            if (isAndroid) {
                GestureFactory.Noop()
            } else {
                BackStackParallax.Gestures(it)
            }

        }
    )
) : Node<Router>(
    nodeContext = nodeContext,
    appyxComponent = backStack
) {

    @Composable
    override fun Content(modifier: Modifier) {
        AppyxNavigationContainer(
            appyxComponent = backStack,
            modifier = modifier.fillMaxSize()
        )
    }

    override fun buildChildNode(navTarget: Router, nodeContext: NodeContext): Node<*> {
        return when (navTarget) {
            Router.Main -> MainNode(nodeContext) {
                backStack.push(Router.Detail)
            }
            Router.Detail -> DetailNode(nodeContext) {
                backStack.replace(Router.User, mode = Operation.Mode.IMMEDIATE)
            }
            Router.User -> {
                UserNode(nodeContext)
            }
        }
    }

}

@Parcelize
sealed class Router : Parcelable {
    @Parcelize
    data object Main : Router()

    @Parcelize
    data object Detail : Router()

    @Parcelize
    data object User : Router()
}