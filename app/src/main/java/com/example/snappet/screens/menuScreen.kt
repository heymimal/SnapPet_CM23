package com.example.snappet.screens

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.snappet.data.Photo
import com.example.snappet.navigation.Navigation
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeMenu(navController: NavHostController) {

    val database = Firebase.database
    val databaseReference = database.reference

    var recentPhotos by remember { mutableStateOf<List<Photo>>(emptyList()) }

    LaunchedEffect(Unit) {
        // Chame LoadRecentPhotos em uma coroutine
        LoadRecentPhotos(databaseReference)?.let {
            recentPhotos = it
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Navigation(navController =navController)
        }) {paddingValues ->
        Text(text = "", modifier = Modifier.padding(paddingValues = paddingValues))
        ThreeByThreeGrid1(recentPhotos)

    }
}


@Composable
fun CardWithImageAndText(photo: Photo, text: String) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .width(120.dp)
            .height(160.dp)
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            /*Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )*/

            Image(
                painter = rememberImagePainter(photo.imageUri),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(shape = MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = text,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ThreeByThreeGrid1(recentPhotos: List<Photo>) {

    val database = Firebase.database
    val databaseReference = database.reference

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // First Row: "Animais"
       /* item {
            Column {
                Text("Animais", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog") }
                    item { CardWithImageAndText(Icons.Default.Place, "Peacock") }
                }
            }
        }*/


        // Second Row: "Most Recent Photos"
        /*item {
            Column {
                Text("Recent Photos", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog1") }
                    item { CardWithImageAndText(Icons.Default.Place, "Peacock1") }
                }
            }
        }*/
        item {
            Column {
                Text("Recent Photos", fontWeight = FontWeight.Bold)
                Log.d(TAG, "TAMANHO")
                Log.d(TAG, recentPhotos.size.toString())
                Log.d(TAG, LoadRecentPhotos(databaseReference)?.size.toString())
                LazyRow {
                    LoadRecentPhotos(databaseReference)?.forEach { photo ->
                        Log.d(TAG, "TESTAR SEI LA ")
                        Log.d(TAG, photo.animalType)
                        Log.d(TAG, photo.contextPhoto)
                        Log.d(TAG, photo.description)
                        Log.d(TAG, photo.imageUri.toString())
                        item { CardWithImageAndText(photo = photo, text = "s")}
                    }
                }
            }


        }

        /*item {
            Column {
                Text("Most Popular Photos", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat2") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Peacock2") }
                }
            }
        }

        item {
            Column {
                Text("Dogs", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Cat1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Cat2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Cat3") }
                }
            }
        }

        item {
            Column {
                Text("Cats", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Dog1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Dog3") }
                }
            }
        }

        item {
            Column {
                Text("Cats", fontWeight = FontWeight.Bold)
                LazyRow {
                    item { CardWithImageAndText(Icons.Default.Person, "Peacock1") }
                    item { CardWithImageAndText(Icons.Default.Phone, "Dog2") }
                    item { CardWithImageAndText(Icons.Default.Place, "Dog3") }
                }
            }
        }*/


    }
}


/*fun LoadRecentPhotos(databaseReference: DatabaseReference) {
    // Consulta para obter as últimas N fotos (ajuste N conforme necessário)
    val query = databaseReference.child("imagesTest")
        .limitToLast(3) // Obtenha as últimas 3 fotos, por exemplo

    // Esta função será chamada quando os dados forem recuperados
    val onDataChange = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Aqui, você processa os dados obtidos do banco de dados
            val photosList = mutableListOf<Photo>()

            dataSnapshot.children.forEach { photoSnapshot ->
                val imageUrl = photoSnapshot.child("imageUrl").getValue(String::class.java)
                val animalType = photoSnapshot.child("animal").getValue(String::class.java)
                val contextPhoto = photoSnapshot.child("context").getValue(String::class.java)
                val description = photoSnapshot.child("description").getValue(String::class.java)

                // Crie um objeto Photo com os dados
                val photo = Photo(
                    imageUri = Uri.parse(imageUrl),
                    animalType = animalType.orEmpty(),
                    contextPhoto = contextPhoto.orEmpty(),
                    description = description.orEmpty()
                )

                // Adicione a foto à lista
                photosList.add(photo)
            }

        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Esta função será chamada se houver um erro na recuperação dos dados
            // Trate o erro conforme necessário
            // Aqui você pode exibir uma mensagem de erro, fazer log, etc.
        }
    }

    // Adicione um ouvinte de dados à consulta
    query.addListenerForSingleValueEvent(onDataChange)
}*/


fun LoadRecentPhotos(databaseReference: DatabaseReference): List<Photo>? {
    // Consulta para obter as últimas N fotos (ajuste N conforme necessário)
    val query = databaseReference.child("imagesTest").child("allImages")
        .limitToLast(3) // Obtenha as últimas 3 fotos, por exemplo

    // Esta função será chamada quando os dados forem recuperados
    val onDataChange: (DataSnapshot) -> List<Photo> = { dataSnapshot ->
        // Aqui, você processa os dados obtidos do banco de dados
        val photosList = mutableListOf<Photo>()

        dataSnapshot.children.forEach { photoSnapshot ->
            val imageUrl = photoSnapshot.child("imageUrl").getValue(String::class.java)
            val animalType = photoSnapshot.child("animal").getValue(String::class.java)
            val contextPhoto = photoSnapshot.child("context").getValue(String::class.java)
            val description = photoSnapshot.child("description").getValue(String::class.java)

            if(imageUrl != null){
                // Crie um objeto Photo com os dados
                Log.d(TAG, "não é nulo lol")
                //Log.d(TAG, imageUrl!!)
                Log.d(TAG, animalType!!)
                Log.d(TAG, contextPhoto!!)
                Log.d(TAG, description!!)
                val photo = Photo(
                    imageUri = Uri.parse(imageUrl),
                    animalType = animalType.orEmpty(),
                    contextPhoto = contextPhoto.orEmpty(),
                    description = description.orEmpty()

                )

                photosList.add(photo)
            }else{
                Log.d(TAG, "é nulo lol")
                //Log.d(TAG, imageUrl!!)
                Log.d(TAG, animalType!!)
                Log.d(TAG, contextPhoto!!)
                Log.d(TAG, description!!)
            }



            // Adicione a foto à lista

        }

        Log.d(TAG, "tamanho da photosList")
        Log.d(TAG, photosList.size.toString())
        // Retorna a lista de fotos
        photosList
    }

    // Configurar o ouvinte para a consulta
    query.addValueEventListener(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Chama a função onDataChange quando os dados são alterados
            onDataChange(dataSnapshot)
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Lidar com erros, se necessário
        }
    })

    // Retorna null por padrão (você pode ajustar isso conforme necessário)
    return null
}







data class ImageData(val path: String, val name: String)
