import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.snappet.sign_In.UserData

@Composable
//para este Screen não precisamos de um view model pois ele não
// contém nenhum estado nem nada que "mude" ao longo do tempo
//só mostra user data estática
fun ProfileScreen(
    userData: UserData?,
    //queremos ter um lambeda quando ele fizer sign out
    //ele recebe esta função como paramentro de entrada
    onSignOut: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        //conteudo centrado verticalmente
        verticalArrangement = Arrangement.Center,
        //e horizontalmente
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //se o profile picture que estiver dentro do UserData  não for nulo
        if(userData?.profilePictureUrl != null) {
            //mostra a imagem
            AsyncImage(
                model = userData.profilePictureUrl,
                //descrição da imagem
                contentDescription = "Profile picture",
                //o tamanho que a imagem vai ter
                modifier = Modifier
                    .size(150.dp)
                    //clipar a imagem para uma forma circular
                    .clip(CircleShape),
                //faz crop da imagem (retira porções indesejadas da imagem)
                contentScale = ContentScale.Crop
            )
            //deixamos um espacinho depois da imagem
            Spacer(modifier = Modifier.height(16.dp))
        }
        //se o userData não tiver um user name que seja nulo
        if(userData?.username != null) {
            Text(
                //mostra o username
                text = userData.username,
                //o texto vai ser centrado
                textAlign = TextAlign.Center,
                //tamanho do texto
                fontSize = 36.sp,
                //por negrito no texto
                fontWeight = FontWeight.SemiBold
            )
            //espacinho depois do username
            Spacer(modifier = Modifier.height(16.dp))
        }
        //butão para logout
        Button(onClick = onSignOut) {
            Text(text = "Sign out")
        }
    }
}