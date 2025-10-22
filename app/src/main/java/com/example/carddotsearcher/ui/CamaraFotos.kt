package com.example.carddotsearcher.ui

import android.Manifest
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.example.carddotsearcher.viewmodel.MainViewModel
import java.io.File

@Composable
fun CamaraFotos(viewModel: MainViewModel, onPhotoTaken: () -> Unit) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && imageUri != null) {
            val bmp = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            bitmap = bmp
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val file = File.createTempFile("photo", ".jpg", context.cacheDir)
            file.deleteOnExit()
            val uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                file
            )
            imageUri = uri
            takePictureLauncher.launch(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (bitmap == null) {
            Button(onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }) {
                Text("Tomar foto")
            }
        } else {
            Image(
                bitmap = bitmap!!.asImageBitmap(),
                contentDescription = "Foto de la carta",
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(onClick = {
                    viewModel.setPhotoBitmap(bitmap)
                    onPhotoTaken()
                }) { 
                    Text("Confirmar")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }) {
                    Text("Tomar de Nuevo")
                }
            }
        }
    }
}
