package com.example.przepisy

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

data class Przepis(
    val nazwa: String,
    val skladniki: String,
    val opis: String,
    val zdjecie: Uri?
)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Aplikacja()
        }
    }
}

@Composable
fun Aplikacja() {

    var listaPrzepisow by remember { mutableStateOf(listOf<Przepis>()) }

    var nazwa by remember { mutableStateOf("") }
    var skladniki by remember { mutableStateOf("") }
    var opis by remember { mutableStateOf("") }
    var zdjecie by remember { mutableStateOf<Uri?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        zdjecie = uri
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Moje Przepisy", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nazwa,
            onValueChange = { nazwa = it },
            label = { Text("Nazwa przepisu") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = skladniki,
            onValueChange = { skladniki = it },
            label = { Text("Składniki") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = opis,
            onValueChange = { opis = it },
            label = { Text("Opis przygotowania") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { imagePicker.launch("image/*") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dodaj zdjęcie")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (nazwa.isNotBlank()) {
                    listaPrzepisow = listaPrzepisow + Przepis(nazwa, skladniki, opis, zdjecie)

                    nazwa = ""
                    skladniki = ""
                    opis = ""
                    zdjecie = null
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dodaj przepis")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(listaPrzepisow) { przepis ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {

                        Text(przepis.nazwa, style = MaterialTheme.typography.titleMedium)

                        przepis.zdjecie?.let {
                            Spacer(modifier = Modifier.height(6.dp))
                            Image(
                                painter = rememberAsyncImagePainter(it),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Składniki: ${przepis.skladniki}")
                        Text("Opis: ${przepis.opis}")
                    }
                }
            }
        }
    }
}