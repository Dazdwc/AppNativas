package org.helios.mythicdoors.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.helios.mythicdoors.R
import org.helios.mythicdoors.model.DataController
import org.helios.mythicdoors.model.entities.DoorDBoj

class GameActionScreenViewModel (
    private val dataController: DataController


) {


}
@Composable
fun ClickableImage(door: DoorDBoj, modifier: Modifier = Modifier) {

    Row {
        Image(
            painter = painterResource(id = R.drawable.easy_door), // Reemplaza 'tu_imagen' con el nombre de tu imagen
            contentDescription = null, // Puedes proporcionar una descripción adecuada
            modifier = modifier
                .padding(16.dp)
                .size(115.dp)
                .offset(y = (-250).dp)
                .clickable { door.setId("EASY_DOOR") }
        )

        Image(
            painter = painterResource(id = R.drawable.average_door), // Reemplaza 'tu_imagen' con el nombre de tu imagen
            contentDescription = null, // Puedes proporcionar una descripción adecuada
            modifier = modifier
                .padding(16.dp)
                .size(115.dp)
                .offset(y = (-250).dp)
                .clickable { door.setId("AVERAGE_DOOR") }
        )

        Image(
            painter = painterResource(id = R.drawable.hard_door), // Reemplaza 'tu_imagen' con el nombre de tu imagen
            contentDescription = null, // Puedes proporcionar una descripción adecuada
            modifier = modifier
                .padding(16.dp)
                .size(115.dp)
                .offset(y = (-250).dp)
                .clickable { door.setId("HARD_DOOR")}
        )
    }
}

@Composable
fun BetButton(){
    var textValue by remember { mutableStateOf("Texto de ejemplo") }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = { textValue = it },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions.Default,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp))
    }

    Spacer(modifier = Modifier.height(16.dp))


    Button(
        onClick = {
            // Agrega aquí las acciones que desees al hacer clic en el botón
        },
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .offset(y = (300).dp)
    ) {
        Text("BET")
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameActionPreviewPreview() {
    // En esta función de previsualización, puedes mostrar cómo se verá la vista de previsualización
    Column {

        BetButton()
        // Agrega el botón a la vista de previsualización
        val myDoorObject = DoorDBoj("EASY_DOOR", -3, 1, 1.0, R.drawable.easy_door)
        ClickableImage(myDoorObject,Modifier.fillMaxHeight().weight(1f))

    }
}

