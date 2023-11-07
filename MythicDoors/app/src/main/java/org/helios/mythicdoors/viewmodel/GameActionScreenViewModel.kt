package org.helios.mythicdoors.viewmodel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameActionPreviewPreview() {
    // En esta función de previsualización, puedes mostrar cómo se verá la vista de previsualización
    Column {
        // Agrega el botón a la vista de previsualización
        val myDoorObject = DoorDBoj("EASY_DOOR", -3, 1, 1.0, R.drawable.easy_door)
        ClickableImage(myDoorObject,Modifier.fillMaxHeight().weight(1f))

    }
}

