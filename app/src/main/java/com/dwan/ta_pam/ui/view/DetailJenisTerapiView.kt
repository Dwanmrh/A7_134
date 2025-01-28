package com.dwan.ta_pam.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dwan.ta_pam.model.JenisTerapi
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.customwidget.MenuButton
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.DetailJenisTerapiViewModel
import com.dwan.ta_pam.ui.viewmodel.DetailJnsUiState
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel

object DestinasiDetailJenisTerapi : DestinasiNavigasi {
    override val route = "detail_jenis_terapi"
    override val titleRes = "Detail Jenis Terapi"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailJnsScreen(
    id_jenis_terapi: String,
    navigateToPasien: () -> Unit,
    navigateToTerapis: () -> Unit,
    navigateToJenisTerapi: () -> Unit,
    navigateToSesiTerapi: () -> Unit,
    navigateToJenisTerapiEntry: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailJenisTerapiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(id_jenis_terapi) {
        viewModel.getJenisTerapiById(id_jenis_terapi)
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(0xFF121212)), // Warna latar belakang gelap
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetailJenisTerapi.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToJenisTerapiEntry,
                shape = CircleShape,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF8B0000),
                contentColor = Color.White // Warna teks putih
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Jenis Terapi")
            }
        },
        bottomBar = {
            MenuButton(
                onPasienClick = navigateToPasien,
                onTerapisClick = navigateToTerapis,
                onJenisTerapiClick = navigateToJenisTerapi,
                onSesiTerapiClick = navigateToSesiTerapi,
            )
        }
    ) { innerPadding ->
        val detailJnsUiState = viewModel.detailJnsUiState.collectAsState().value
        DetailBodyJns(
            detailJnsUiState = detailJnsUiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onDeleteClick = {
                viewModel.deleteJns(id_jenis_terapi)
                navigateBack()
            }
        )
    }
}

@Composable
fun DetailBodyJns(
    detailJnsUiState: DetailJnsUiState,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    when (detailJnsUiState) {
        is DetailJnsUiState.Loading -> JenisTerapiLoading(modifier = modifier)
        is DetailJnsUiState.Success -> Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            DetailContentJns(jenisTerapi = detailJnsUiState.jenisTerapi)
            Button(
                onClick = { deleteConfirmationRequired = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B0000), // Warna merah gelap
                    contentColor = Color.White // Warna teks putih
                )
            ) {
                Text(text = "Delete Data")
            }
        }
        is DetailJnsUiState.Error -> JenisTerapiError(retryAction = {}, modifier = modifier)
    }

    if (deleteConfirmationRequired) {
        DeleteConfirmationDialog(
            onDeleteConfirm = {
                deleteConfirmationRequired = false
                onDeleteClick()
            },
            onDeleteCancel = { deleteConfirmationRequired = false }
        )
    }
}

@Composable
fun DetailContentJns(jenisTerapi: JenisTerapi, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8B0000), // Warna merah gelap
            contentColor = Color.White // Warna teks putih
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailJns(judul = "ID Jenis Terapi", isinya = jenisTerapi.id_jenis_terapi)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailJns(judul = "Nama Jenis Terapi", isinya = jenisTerapi.nama_jenis_terapi)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailJns(judul = "Deskripsi Terapi", isinya = jenisTerapi.deskripsi_terapi)
        }
    }
}

@Composable
fun ComponentDetailJns(judul: String, isinya: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = judul,
            Modifier.weight(0.8f),
            fontSize = 20.sp,
            color = Color.LightGray
        )
        Text(
            text = ":",
            Modifier.weight(0.2f),
            fontSize = 20.sp,
            color = Color.LightGray
        )
        Text(
            text = isinya,
            Modifier.weight(2f),
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Delete Data", color = Color.White) },
        text = { Text("Apakah anda yakin ingin menghapus data ini?", color = Color.White) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel", color = Color.White)
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes", color = Color.White)
            }
        },
        containerColor = Color(0xFF8B0000), // Warna merah gelap
        textContentColor = Color.White, // Warna teks putih
        titleContentColor = Color.White // Warna teks putih
    )
}