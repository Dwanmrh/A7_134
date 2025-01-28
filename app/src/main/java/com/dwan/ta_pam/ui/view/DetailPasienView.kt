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
import com.dwan.ta_pam.model.Pasien
import com.dwan.ta_pam.ui.customwidget.CustomTopAppBar
import com.dwan.ta_pam.ui.customwidget.MenuButton
import com.dwan.ta_pam.ui.navigation.DestinasiNavigasi
import com.dwan.ta_pam.ui.viewmodel.DetailPasienViewModel
import com.dwan.ta_pam.ui.viewmodel.DetailUiState
import com.dwan.ta_pam.ui.viewmodel.PenyediaViewModel

object DestinasiDetail : DestinasiNavigasi {
    override val route = "detail_pasien"
    override val titleRes = "Detail Pasien"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPasScreen(
    id_pasien: String,
    navigateToPasien: () -> Unit,
    navigateToTerapis: () -> Unit,
    navigateToJenisTerapi: () -> Unit,
    navigateToSesiTerapi: () -> Unit,
    navigateBack: () -> Unit,
    navigateSesi: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailPasienViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    LaunchedEffect(id_pasien) {
        viewModel.getPasienById(id_pasien)
    }

    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .background(Color(0xFF121212)), // Warna latar belakang gelap
        topBar = {
            CustomTopAppBar(
                title = DestinasiDetail.titleRes,
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateSesi(DestinasiEntrySesiTerapi.route)
                },
                shape = CircleShape,
                modifier = Modifier.padding(18.dp),
                containerColor = Color(0xFF8B0000),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Sesi"
                )
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
        val detailUiState = viewModel.detailUiState.collectAsState().value
        DetailBodyPas(
            detailUiState = detailUiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            onDeleteClick = {
                viewModel.deletePas(id_pasien)
                navigateBack()
            }
        )
    }
}

@Composable
fun DetailBodyPas(
    detailUiState: DetailUiState,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit
) {
    var deleteConfirmationRequired by remember { mutableStateOf(false) }

    when (detailUiState) {
        is DetailUiState.Loading -> OnLoading(modifier = modifier)
        is DetailUiState.Success -> Column(
            modifier = modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            DetailContentPas(pasien = detailUiState.pasien)
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
        is DetailUiState.Error -> OnError(retryAction = {}, modifier = modifier)
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
fun DetailContentPas(pasien: Pasien, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF8B0000), // Warna merah gelap
            contentColor = Color.White // Warna teks putih
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ComponentDetailPas(judul = "ID Pasien", isinya = pasien.id_pasien)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailPas(judul = "Nama Pasien", isinya = pasien.nama_pasien)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailPas(judul = "Alamat", isinya = pasien.alamat)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailPas(judul = "Nomor Telepon", isinya = pasien.nomor_telepon)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailPas(judul = "Tanggal Lahir", isinya = pasien.tanggal_lahir)
            Spacer(modifier = Modifier.padding(8.dp))
            ComponentDetailPas(judul = "Riwayat Medikal", isinya = pasien.riwayat_medikal)
        }
    }
}

@Composable
fun ComponentDetailPas(judul: String, isinya: String, modifier: Modifier = Modifier) {
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