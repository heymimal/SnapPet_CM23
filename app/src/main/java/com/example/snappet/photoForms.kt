package com.example.snappet

import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.snappet.data.MonthAnimal
import com.example.snappet.data.Photo
import com.example.snappet.database.saveImageToMediaStore
import com.example.snappet.database.uploadImageToStorage
import com.example.snappet.sign_In.UserData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.appcheck.internal.util.Logger.TAG
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoForm(modifier: Modifier = Modifier, uri: Uri, imageBitmap: ImageBitmap, takenPicture : Bitmap, file: File, loc : LatLng?, userData: UserData){

    var photoType by remember {
        mutableStateOf<String?>("")
    }

    var contextPhotoType by remember{
        mutableStateOf<String?>("")
    }

    var descriptionPhoto by remember{
        mutableStateOf<String?>("")
    }

    val storage = Firebase.storage
    val storageRef = storage.reference

    val context = LocalContext.current

    val user = Firebase.auth.currentUser
    val userId = user?.uid

    //var navController = rememberNavController()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Photo Form",
            color = Color.Black,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(5.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ){
            Image(
                bitmap = imageBitmap,
                contentDescription = "description",
                modifier = Modifier
                    //.padding(16.dp, 8.dp)
                    //.size(200.dp).align(Alignment.TopCenter)
                    //.align(Alignment.CenterHorizontally)
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Animal Type")
                }
                withStyle(style = SpanStyle(fontSize = 15.sp)) {
                    append(" *")
                }
            },
            color = Color.Black,
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(5.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            val options = listOf("Bee", "Bird", "Butterfly", "Cat", "Chicken", "Cow", "Dog", "Duck", "Gecko", "Goat",
                "Horse", "Lizard", "Peacock", "Pig", "Rabbit", "Sheep")
            var expanded by remember { mutableStateOf(false) } //menu drop down aberto ou nao
            //var selectedOptionText by remember { mutableStateOf(options[0]) } //current selected
            var selectedOptionText by remember { mutableStateOf<String?>(null) } // current selected


            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                TextField(
                    modifier = Modifier.menuAnchor(),
                    readOnly = true,
                    value = selectedOptionText?: "",
                    onValueChange = {},
                    label = { Text("Animal") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                //photoType = selectedOptionText
                                photoType = if (selectedOptionText != null) selectedOptionText else null

                            },
                            )
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Context")
                }
                withStyle(style = SpanStyle(fontSize = 15.sp)) {
                    append(" *")
                }
            },
            color = Color.Black,
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )

        radioButton { selectedOption ->
            contextPhotoType = selectedOption
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 20.sp)) {
                    append("Description")
                }
                withStyle(style = SpanStyle(fontSize = 15.sp)) {
                    append(" *")
                }
            },
            color = Color.Black,
            modifier = Modifier.align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))

        var ttext by remember { mutableStateOf<String?>("") }

        val maxChar = 60

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {

            // Character count display
            Text(
                text = "Characters: ${ttext?.length}/$maxChar",
                color = Color.Gray,
                style = TextStyle(fontSize = 12.sp),
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)

            )

            // TextField with character limit
            TextField(
                value = ttext!!,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onValueChange = {
                    if (it.length <= maxChar) {
                        ttext = it
                    }
                },
                label = { Text("Description") }
            )
            descriptionPhoto = ttext
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "(Options selected with * are mandatory to fill in!)",
            color = Color.Black,
            style = TextStyle(fontSize = 10.sp, fontStyle = FontStyle.Italic),
            modifier = Modifier.align(alignment = Alignment.Start)
        )


    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        var latitude = 190.0
        var longitude = 190.0

        var isLocationChecked = false

        val showAlertMessage = remember{ mutableStateOf(false) }

        if(showAlertMessage.value){
            AlertDialog(
                onDismissRequest = { showAlertMessage.value = false },
                title = {Text(text = "Warning")},
                text = {Text (
                    text = "Fill everything!",
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )},
                confirmButton = {
                    Button(
                        onClick = {showAlertMessage.value = false},
                        colors = ButtonDefaults.buttonColors(Color.Black)
                    ){
                        Text(text = "OK", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
        ){
            isLocationChecked = SwitchOption()
        }


        Button(
            onClick = {
                val fileName = "photo_${System.currentTimeMillis()}.jpg"

                if(photoType == "" || contextPhotoType == "" || descriptionPhoto == ""){
                        showAlertMessage.value = true
                }else{
                    showAlertMessage.value = false
                    var savedUri = saveImageToMediaStore(takenPicture,context,file)

                if(loc!=null){
                    latitude = loc.latitude
                    longitude = loc.longitude
                }

                if(isLocationChecked){
                    var photo = Photo(savedUri!!, photoType!!, contextPhotoType!!, descriptionPhoto!!, "", "",userId!!,latitude,longitude, 0)
                    uploadImageToStorage(uri.toString(), imageBitmap, photo, userData);
                }else{
                    var photo = Photo(savedUri!!, photoType!!, contextPhotoType!!, descriptionPhoto!!, "", "",userId!!,190.0,190.0, 0)
                    uploadImageToStorage(uri.toString(), imageBitmap, photo, userData);
                }
                    if (contextPhotoType == "Needs Help") {
                        val toast = Toast.makeText(
                            context,
                            "Animal Shelter Entities Have Been Notified!",
                            Toast.LENGTH_LONG,
                        )
                        toast.show()
                    }
                }
            },
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xffe2590b)),
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(end = 30.dp, bottom = 30.dp)
                .height(50.dp)
                .width(150.dp)
        )
        {
            Text(text = "Upload", style = TextStyle(fontSize = 20.sp), color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun SwitchOption(): Boolean{
    var isChecked by remember{ mutableStateOf(true) }

    val icons = if(isChecked) Icons.Filled.Check else Icons.Filled.Close

    val icon: (@Composable () -> Unit)? =
        if (isChecked){
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize)
                )
            }

        }else null

    Column(
        modifier = Modifier
            .padding(20.dp)
    ){
        Text(
            text = "Share Location",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Switch(
            checked = isChecked,
            onCheckedChange = {isChecked = it},
            thumbContent = icon,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

    }

    return isChecked

}

//when the user takes a photo, the missions progress are updated
fun updateDailyMissions(userData: UserData?, animalType: String) {
    val monthAnimal = MonthAnimal()
    val presentMonthAnimal = monthAnimal.getAnimalForPresentMonth()
    val database = Firebase.database
    val myReference = database.getReference("Users")

    if (userData != null) {
        if(animalType == presentMonthAnimal){
            updateSnaPoints(userData.userId, myReference, 5)
        }else{
            updateSnaPoints(userData.userId, myReference, 1)
        }
        val thisUserRef = myReference.child(userData.userId)// Get reference to the user's DailyMissions
        val missionsReference = thisUserRef.child("DailyMissions")
        missionsReference.orderByChild("missionType")
            .equalTo("10PicturesMission")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (missionSnapshot in dataSnapshot.children) {
                        val completed = missionSnapshot.child("completed").getValue(Boolean::class.java) ?: false// Check if the mission is not completed
                        if (!completed) {
                            val userProgress = missionSnapshot.child("userProgress").getValue(Int::class.java) ?: 0// Update the userProgress property
                            missionSnapshot.ref.child("userProgress").setValue(userProgress + 1)
                            val goal = missionSnapshot.child("goal").getValue(Int::class.java) ?: 0// Check if userProgress equals the goal
                            if (userProgress + 1 == goal) {
                                val missionPoints = missionSnapshot.child("points").getValue(Int::class.java) ?: 0// Fetch the mission points and add to user's snaPoints
                                updateSnaPoints(userData.userId, myReference, missionPoints)
                                missionSnapshot.ref.child("completed").setValue(true) // Set completed to true
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        if (animalType.isNotEmpty()) {// If the animalType is specified, also update the userProgress for that animalType
            missionsReference.orderByChild("missionType")
                .equalTo(animalType)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (missionSnapshot in dataSnapshot.children) {
                            val completed = missionSnapshot.child("completed").getValue(Boolean::class.java) ?: false// Check if the mission is not completed
                            if (!completed) {
                                val userProgress = missionSnapshot.child("userProgress").getValue(Int::class.java) ?: 0// Update the userProgress property
                                missionSnapshot.ref.child("userProgress").setValue(userProgress + 1)
                                val goal = missionSnapshot.child("goal").getValue(Int::class.java) ?: 0// Check if userProgress equals the goal
                                if (userProgress + 1 == goal) {
                                    val missionPoints = missionSnapshot.child("points").getValue(Int::class.java) ?: 0// Fetch the mission points and add to user's snaPoints
                                    updateSnaPoints(userData.userId, myReference, missionPoints)
                                    missionSnapshot.ref.child("completed").setValue(true)// Set completed to true
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
        }
    }
}

//updates the missions according with the taken photo type
fun updateMonthlyMissions(userData: UserData?, animalType: String) {
    val database = Firebase.database
    val myReference = database.getReference("Users")

    if (userData != null) {
        val thisUserRef = myReference.child(userData.userId)// Get reference to the user's MonthlyMissions
        val missionsReference = thisUserRef.child("MonthlyMissions")
        missionsReference.orderByChild("missionType")
            .equalTo("100PicturesMission")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (missionSnapshot in dataSnapshot.children) {
                        val completed = missionSnapshot.child("completed").getValue(Boolean::class.java) ?: false// Check if the mission is not completed
                        if (!completed) {
                            val userProgress = missionSnapshot.child("userProgress").getValue(Int::class.java) ?: 0// Update the userProgress property
                            missionSnapshot.ref.child("userProgress").setValue(userProgress + 1)

                            val goal = missionSnapshot.child("goal").getValue(Int::class.java) ?: 0// Check if userProgress equals the goal
                            if (userProgress + 1 == goal) {
                                val missionPoints = missionSnapshot.child("points").getValue(Int::class.java) ?: 0// Fetch the mission points and add to user's snaPoints
                                updateSnaPoints(userData.userId, myReference, missionPoints)
                                missionSnapshot.ref.child("completed").setValue(true)// Set completed to true
                            }
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        if (animalType.isNotEmpty()) {// If the animalType is specified, also update the userProgress for that animalType
            missionsReference.orderByChild("missionType")
                .equalTo(animalType)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (missionSnapshot in dataSnapshot.children) {
                            val completed = missionSnapshot.child("completed").getValue(Boolean::class.java) ?: false// Check if the mission is not completed
                            if (!completed) {
                                val userProgress = missionSnapshot.child("userProgress").getValue(Int::class.java) ?: 0// Update the userProgress property
                                missionSnapshot.ref.child("userProgress").setValue(userProgress + 1)

                                val goal = missionSnapshot.child("goal").getValue(Int::class.java) ?: 0// Check if userProgress equals the goal
                                if (userProgress + 1 == goal) {
                                    val missionPoints = missionSnapshot.child("points").getValue(Int::class.java) ?: 0// Fetch the mission points and add to user's snaPoints
                                    updateSnaPoints(userData.userId, myReference, missionPoints)
                                    missionSnapshot.ref.child("completed").setValue(true)// Set completed to true
                                }
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
        }
    }
}

//updates the missions according with the taken photo type
fun updateFullTimeMissions(userData: UserData?, animalType: String) {
    val database = Firebase.database
    val myReference = database.getReference("Users")
    if (userData != null) {
        val thisUserRef = myReference.child(userData.userId)
        val missionsReference = thisUserRef.child("FullTimeMissions")
        missionsReference.orderByChild("missionType")
            .equalTo("1000PicturesMission")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (missionSnapshot in dataSnapshot.children) {
                        val completed = missionSnapshot.child("completed").getValue(Boolean::class.java) ?: false// Check if the mission is not completed
                        if (!completed) {
                            val userProgress = missionSnapshot.child("userProgress").getValue(Int::class.java) ?: 0// Update the userProgress property
                            missionSnapshot.ref.child("userProgress").setValue(userProgress + 1)
                            val goal = missionSnapshot.child("goal").getValue(Int::class.java) ?: 0// Check if userProgress equals the goal
                            if (userProgress + 1 == goal) {
                                val missionPoints = missionSnapshot.child("points").getValue(Int::class.java) ?: 0// Fetch the mission points and add to user's snaPoints
                                updateSnaPoints(userData.userId, myReference, missionPoints)
                                missionSnapshot.ref.child("completed").setValue(true)// Set completed to true
                            }
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })

        if (animalType.isNotEmpty()) { // If the animalType is specified, also update the userProgress for that animalType
            missionsReference.orderByChild("missionType")
                .equalTo(animalType)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (missionSnapshot in dataSnapshot.children) {
                            val completed = missionSnapshot.child("completed").getValue(Boolean::class.java) ?: false// Check if the mission is not completed
                            if (!completed) {
                                val userProgress = missionSnapshot.child("userProgress").getValue(Int::class.java) ?: 0// Update the userProgress property
                                missionSnapshot.ref.child("userProgress").setValue(userProgress + 1)

                                val goal = missionSnapshot.child("goal").getValue(Int::class.java) ?: 0// Check if userProgress equals the goal
                                if (userProgress + 1 == goal) {
                                    val missionPoints = missionSnapshot.child("points").getValue(Int::class.java) ?: 0// Fetch the mission points and add to user's snaPoints
                                    updateSnaPoints(userData.userId, myReference, missionPoints)

                                    missionSnapshot.ref.child("completed").setValue(true)// Set completed to true
                                }
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                    }
                })
        }
    }
}

//updates users snaPoints
 fun updateSnaPoints(userId: String, myReference: DatabaseReference, missionPoints: Int) {
    val snaPointsReference = myReference.child(userId).child("snaPoints")// Fetch the current snaPoints
    snaPointsReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val currentSnaPoints = dataSnapshot.getValue(String::class.java) ?: "0"
            val newSnaPoints = (currentSnaPoints.toInt() + missionPoints).toString()// Add missionPoints to the current snaPoints and update in the database
            snaPointsReference.setValue(newSnaPoints)

        }
        override fun onCancelled(databaseError: DatabaseError) {
        }
    })
}


@Composable
fun radioButton(onOptionSelected: (String) -> Unit) {
    val radioOptions = listOf("Entertainment", "Needs Help")
    //var selectedOption by remember { mutableStateOf(radioOptions[0]) }
    var selectedOption by remember { mutableStateOf<String?>("") }

    Row(
        modifier = Modifier
            .padding(start = 8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        radioOptions.forEach { option ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = {
                        //selectedOption = option
                        selectedOption = if (selectedOption == option) null else option
                        onOptionSelected(selectedOption!!)
                    }
                )
                Text(
                    text = option,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
    }
}
