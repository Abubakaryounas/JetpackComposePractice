package com.example.jetpackcomposecrash

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.jetpackcomposecrash.ui.theme.JetpackComposeCrashTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce

class MainActivity : ComponentActivity() {
    private val prefs by lazy {
        applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeCrashTheme {
                var name by remember {
                    mutableStateOf("")
                }
                 var text by remember {
                     mutableStateOf("")
                 }

                var namesList by remember {
                    mutableStateOf(listOf<String>(""))
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { text ->
                                name = text
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Button(
                            onClick = {
                                namesList = namesList + name
                                name = ""
                            },
                            shape = CircleShape
                        ) {
                            Text(text = "Add in List")
                        }
                    }
                    //using CustomMultilineHinttextField here
                    /*CustomMultilineTextField(
                        value = text,
                        onValueChanged = {text=it},
                    hintText = "hellow world\n",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100))
                        .background(Color.Black)
                        .padding(16.dp))*/
                    DisplayNameList(namesList = namesList, prefs)
                }
            }
        }
    }
}

//we can also pass Modifier in function
// if we want to change from outside
@Composable
fun DisplayNameList(namesList: List<String>, prefs: SharedPreferences) {

    //code to remember last scrolled position of list
    val scrollPosition = prefs.getInt("scroll_position", 0)
    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollPosition
    )

    LaunchedEffect(lazyListState) {
        snapshotFlow {
            lazyListState.firstVisibleItemIndex
        }
            //using debounce to remember state after 500 milliseconds
            .debounce(500L)
            .collectLatest { index ->
                prefs.edit().putInt("scroll_position", index)
                    .apply()
            }
    }

    LazyColumn(state = lazyListState) {
        items(namesList) { currentName ->
            Text(
                text = currentName,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
            /*items(100){
                Text(text = "hey $it")*/
            Divider()

        }
    }
}



